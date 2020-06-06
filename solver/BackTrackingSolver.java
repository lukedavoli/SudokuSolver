/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver
{
    int gDim;
    int bDim;
    String[][] board;
    String[] symbols;

    public BackTrackingSolver() {
        
    } // end of BackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid) 
    {
        //Get information on grid
        this.gDim = grid.getDimensions();
        this.bDim = grid.getBDim();
        this.board = grid.getBoard();
        this.symbols = grid.getSymbols();

        String currCell = "";
        //Iterate through each row and column of the grid
        for(int row = 0; row < gDim; row++)
        {
            for(int col = 0; col < gDim; col++)
            {
                //Skip if the cell contains a value, otherwise continue
                currCell = board[row][col];
                if(currCell == null)
                {
                    //Test all symbols for validity at the current cell
                    for(String symb : symbols)
                    {
                        if(validEntry(symb, row, col))
                        {
                            //If valid, set the cell
                            grid.setGridCell(row, col, symb);

                            //Continue recursively
                            if(solve(grid))
                            {
                                return true;
                            }
                            else
                            {
                                //If the previous call hit a dead end, undo the last cell set
                                grid.setGridCell(row, col, null);
                            }
                        }
                    }
                    //BACKTRACK
                    return false;
                }
            }
        }
        return true;

    } // end of solve()

    //Test whether or not a symbol can be added at a certain position based on standard sudoku constraints
    private boolean validEntry(String symb, int row, int col) 
    {
        if(!symbInRow(row, symb) &&
            !symbInCol(col, symb) &&
            !symbInBox(row, col, symb))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Identify whether or not a symbol is currently present in a box
    private boolean symbInBox(int cellRow, int cellCol, String symb)
    {
        //Identify the box of the given cell
        int bRow = cellRow / bDim;
        int bCol = cellCol / bDim;
        //Iterate through all cells in a box and check for the given value
        for(int row = bDim * bRow; row < (bDim * bRow + bDim); row++)
        {
            for(int col = bDim * bCol; col < (bDim * bCol + bDim); col++)
            {
                if(board[row][col] != null && board[row][col].equals(symb))
                {
                    return true;
                }
            }
        }
        return false;
    }

    //Identify whether or not a symbol is currently present in a column
    private boolean symbInCol(int col, String symb)
    {
        for(int i = 0; i < gDim; i++)
        {
            if(board[i][col] != null && board[i][col].equals(symb))
            {
                return true;
            }
        }
        return false;
    }

    //Identify whether or not a symbol is currently present in a row
    private boolean symbInRow(int row, String symb) 
    {
        for(int i = 0; i < gDim; i++)
        {
            if(board[row][i] != null && board[row][i].equals(symb))
            {
                return true;
            }
        }
        return false;
    }

} // end of class BackTrackingSolver()
