/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.Scanner;


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid extends SudokuGrid
{

    public StdSudokuGrid() {
        super();

    } // end of StdSudokuGrid()


    /* ********************************************************* */


    @Override //Create the grid based on information from file
    public void initGrid(String filename) throws FileNotFoundException, IOException 
    {
        File gridInitFile = new File(filename);
        Scanner scanner = new Scanner(gridInitFile);

        //Get dimensions from file and create the board based on them
        dimensions = Integer.parseInt(scanner.nextLine());
        bDim = (int) Math.sqrt(dimensions);
        board = new String[dimensions][dimensions];

        symbols = scanner.nextLine().split(" ");

        //Line by line, add exisiting values in the file to the grid
        while (scanner.hasNextLine()) {
            String initValuePos = scanner.nextLine();
            String[] posAndVal = initValuePos.split(" ");
            String[] posXY = posAndVal[0].split(",");
            int row = Integer.parseInt(posXY[0]);
            int col = Integer.parseInt(posXY[1]);
            String value = posAndVal[1];
            board[row][col] = value;
        }
        scanner.close();
    }

    @Override //Write grid to file
    public void outputGrid(String filename) throws FileNotFoundException, IOException {
        FileWriter writer = new FileWriter(filename);

        for (int row = 0; row < dimensions; row++)
         {
            for (int col = 0; col < dimensions; col++)
             {
                String output = board[row][col];
                if (output != null) {
                    writer.write(board[row][col]);
                } else {
                    writer.write(" ");
                }

                if (col != dimensions - 1) 
                {
                    writer.write(",");
                }
            }
            writer.write("\n");
        }
        writer.close();
    }

    @Override //String representation of grid
    public String toString() 
    {
        StringBuilder gridSB = new StringBuilder();
        for (int row = 0; row < dimensions; row++) 
        {
            for (int col = 0; col < dimensions; col++) 
            {
                 String output = board[row][col];
                if (output != null) {
                    gridSB.append(board[row][col]);
                } else {
                    gridSB.append(" ");
                }

                if (col != dimensions - 1) 
                {
                    gridSB.append(",");
                }
            }
            gridSB.append("\n");
        }

        return gridSB.toString();
    } // end of toString()

    @Override
    public boolean validate() 
    {
        //If any of the conditions of a valid standard sudoku grid are violated, return false
        if (!validateCells()) {
            return false;
        } else if (!validateAllRows()) {
            return false;
        } else if (!validateAllColumns()) {
            return false;
        } else if (!validateAllBlocks()) {
            return false;
        } else {
            return true;
        }
    } // end of validate()

    

} // end of class StdSudokuGrid
