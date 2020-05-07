/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package grid;

import java.io.*;
import java.util.ArrayList;


/**
 * Abstract class representing the general interface for a Sudoku grid.
 * Both standard and Killer Sudoku extend from this abstract class.
 */
public abstract class SudokuGrid
{
    protected String[][] board;
    protected int dimensions;
    protected int bDim;
    protected String[] symbols;

    /**
     * Load the specified file and construct an initial grid from the contents
     * of the file.  See assignment specifications and sampleGames to see
     * more details about the format of the input files.
     *
     * @param filename Filename of the file containing the intial configuration
     *                  of the grid we will solve.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void initGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Write out the current values in the grid to file.  This must be implemented
     * in order for your assignment to be evaluated by our testing.
     *
     * @param filename Name of file to write output to.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void outputGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Converts grid to a String representation.  Useful for displaying to
     * output streams.
     *
     * @return String representation of the grid.
     */
    public abstract String toString();


    /**
     * Checks and validates whether the current grid satisfies the constraints
     * of the game in question (either standard or Killer Sudoku).  Override to
     * implement game specific checking.
     *
     * @return True if grid satisfies all constraints of the game in question.
     */
    public abstract boolean validate();

    public int getDimensions() 
    {
        return dimensions;
    }

    public int getBDim()
    {
        return bDim;
    }

    public String[][] getBoard() 
    {
		return board;
	}


    public String[] getSymbols() 
    {
		return symbols;
    }
    
    public boolean validateCells() 
    {
        String currCell = "";
        for (int row = 0; row < dimensions; row++) {
            for (int col = 0; col < dimensions; col++) {
                currCell = board[row][col];
                if (currCell == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateAllColumns() 
    {
        for (int col = 0; col < dimensions; col++) 
        {
            if(!validateColumn(col))
            {
                return false;
            }
        }
        return true;
    }

    public boolean validateColumn(int col)
    {
        ArrayList<String> colVals = new ArrayList<>();
        String currCell = "";
        for (int row = 0; row < dimensions; row++) 
        {
            currCell = board[row][col];
            if (colVals.contains(currCell)) 
            {
                return false;
            }
            colVals.add(currCell);
        }
        return true;
    }

    public boolean validateAllRows()
    {
        for (int row = 0; row < dimensions; row++) 
        {
            if(!validateRow(row))
            {
                return false;
            }
        }
        return true;
    }

    public boolean validateRow(int row)
    {
        ArrayList<String> rowVals = new ArrayList<>();
        String currCell = "";
        for (int col = 0; col < dimensions; col++)
        {
            currCell = board[row][col];
            if(rowVals.contains(currCell))
            {
                return false;
            }
            rowVals.add(currCell);
        }
        return true;
    }

    public boolean validateAllBlocks() 
    {
        for (int bRow = 0; bRow < bDim; bRow++)
        {
            for (int bCol = 0; bCol < bDim; bCol++)
            {
                if(!validateBlock(bRow, bCol))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateBlock(int bRow, int bCol)
    {
        ArrayList<String> blockVals = new ArrayList<>();
        String currCell = "";

        for (int row = bRow * bDim; row < bRow * bDim + bDim; row++)
        {
            for(int col = bCol * bDim; col < bCol * bDim + bDim; col++)
            {
                currCell = board[row][col];
                if(blockVals.contains(currCell))
                {
                    return false;
                }
                blockVals.add(currCell);
            }
        }
        return true;
    }

    public void setGridCell(int row, int col, String symbol)
    {
        board[row][col] = symbol;
    }
    

} // end of abstract class SudokuGrid
