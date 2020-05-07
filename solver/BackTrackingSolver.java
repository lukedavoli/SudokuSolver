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
        // TODO: any initialisation you want to implement.
    } // end of BackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid) 
    {
        this.gDim = grid.getDimensions();
        this.bDim = grid.getBDim();
        this.board = grid.getBoard();
        this.symbols = grid.getSymbols();

        String currCell = "";
        for(int row = 0; row < gDim; row++)
        {
            for(int col = 0; col < gDim; col++)
            {
                currCell = board[row][col];
                if(currCell == null)
                {
                    for(String symb : symbols)
                    {
                        if(validEntry(symb, row, col))
                        {
                            grid.setGridCell(row, col, symb);

                            if(solve(grid))
                            {
                                return true;
                            }
                            else
                            {
                                grid.setGridCell(row, col, null);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;

    } // end of solve()

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

    private boolean symbInBox(int cellRow, int cellCol, String symb)
    {
        int bRow = cellRow / bDim;
        int bCol = cellCol / bDim;
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
