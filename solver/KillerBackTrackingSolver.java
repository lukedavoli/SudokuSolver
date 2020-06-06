/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;

import grid.Cage;
import grid.KillerSudokuGrid;
import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver
{
    int gDim;
    int bDim;
    String[][] board;
    String[] symbols;
    ArrayList<Cage> cages;

    public KillerBackTrackingSolver() {
        
    } // end of KillerBackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid)
    {
        KillerSudokuGrid kGrid = (KillerSudokuGrid) grid;
        this.gDim = kGrid.getDimensions();
        this.bDim = kGrid.getBDim();
        this.board = kGrid.getBoard();
        this.symbols = kGrid.getSymbols();
        this.cages = kGrid.getCages();

        boolean solved = solveRec(kGrid);
        return solved;
    } // end of solve()

    private boolean solveRec(SudokuGrid kGrid)
    {
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
                            kGrid.setGridCell(row, col, symb);

                            //Continue recursively
                            if(solveRec(kGrid))
                            {
                                //solve recursively
                                return true;
                            }
                            else
                            {
                                //If the previous call hit a dead end, undo the last cell set
                                kGrid.setGridCell(row, col, null);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    //Test whether or not a symbol can be added at a certain position based on killer sudoku constraints
    private boolean validEntry(String symb, int row, int col) 
    {
        if(!symbInRow(row, symb) &&
            !symbInCol(col, symb) &&
            !symbInBox(row, col, symb) &&
            !symbInCage(row, col, symb) &&
            !invalidSum(row, col, symb))
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

    //Identify whether or not a symbol currently exist in the cage of the cell
    private boolean symbInCage(int row, int col, String symb)
    {
        Cage cage = findCage(row, col);

        for(String cell : cage.getCells())
        {
            String[] rowCol = cell.split(",");
            int cellRow = Integer.parseInt(rowCol[0]);
            int cellCol = Integer.parseInt(rowCol[1]);
            if(board[cellRow][cellCol] != null && board[cellRow][cellCol].equals(symb))
            {
                return true;
            }
        }
        return false;
    }
    
    //Identify whether or not the sum produced by a combination of symbols in a cage is valid
    private boolean invalidSum(int row, int col, String symb) 
    {
        Cage cage = findCage(row, col);
        int sum = Integer.parseInt(symb);
        int cageSize = 0;
        int cellsFilled = 1;

        for(String cell : cage.getCells())
        {
            cageSize++;
            String[] rowCol = cell.split(",");
            int cellRow = Integer.parseInt(rowCol[0]);
            int cellCol = Integer.parseInt(rowCol[1]);
            String cellVal = board[cellRow][cellCol];
            if(cellVal != null)
            {
                sum += Integer.parseInt(cellVal);
                cellsFilled++;
            }
        }

        //If the cage is not yet full but the sum of existing elements surpasses the total, values are invalid
        if(cellsFilled < cageSize && sum > cage.getCageTotal())
        {
            return true;
        }
        //If the cage is full but the sum does not match the total, it is also invalid
        else if(cellsFilled == cageSize && sum != cage.getCageTotal())
        {
            return true;
        }
        //Otherwise we are not yet sure the existing elements will be invalid, it is valid for now so continue
        else
        {
            return false;
        }
    }

    //Identify which cage a given cell belongs to
    private Cage findCage(int row, int col)
    {
        for(Cage cage : cages)
        {
            for(String cell : cage.getCells())
            {
                String[] rowCol = cell.split(",");
                int cellRow = Integer.parseInt(rowCol[0]);
                int cellCol = Integer.parseInt(rowCol[1]);
                if(row == cellRow && col == cellCol)
                {
                    return cage;
                } 
            }
        }
        return null;
    }
} // end of class KillerBackTrackingSolver()
