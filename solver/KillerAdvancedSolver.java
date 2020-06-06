/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import grid.Cage;
import grid.CellVal;
import grid.KillerSudokuGrid;
import grid.SudokuGrid;


/**
 * Your advanced solver for Killer Sudoku.
 */
public class KillerAdvancedSolver extends KillerSudokuSolver
{
    int gDim;
    int bDim;
    String[][] board;
    String[] symbols;
    ArrayList<Cage> cages;

    public KillerAdvancedSolver() 
    {

    } // end of KillerAdvancedSolver()


    @Override
    public boolean solve(SudokuGrid grid)
    {
        KillerSudokuGrid kGrid = (KillerSudokuGrid) grid;
        this.gDim = kGrid.getDimensions();
        this.bDim = kGrid.getBDim();
        this.board = kGrid.getBoard();
        this.symbols = kGrid.getSymbols();
        this.cages = kGrid.getCages();

        //Find all possible assortments of values in each cell for each cage
        for(Cage cage : cages)
        {
            cage.generateAssortments(symbols);
        }

        boolean solved = solveRec(kGrid, 0);
        return solved;
    } // end of solve()

    //Recursively solve for the solution
    private boolean solveRec(KillerSudokuGrid kGrid, int i) 
    {
        //If the depth of the call stack has reached the amount of cages, stop
        if(i >= cages.size())
        {
            //solution found
            return true;
        }

        ArrayList<ArrayList<CellVal>> assortments = cages.get(i).getCageAssortments();
        //Loop through all assortments for all cages
        for(ArrayList<CellVal> assortment : assortments)
        {
            boolean assortmentValid = true;
            //Ensure each cell value in the next assortment is valid
            for(CellVal entry : assortment)
            {
                if(assortmentValid && !validEntry(entry.getSymb(), entry.getRow(), entry.getCol()))
                {
                    assortmentValid = false;
                }
            }
            if(assortmentValid)
            {
                //If the assortment is valid for the current state of the grid, set the cells
                for(CellVal entry : assortment)
                {
                    kGrid.setGridCell(entry.getRow(), entry.getCol(), entry.getSymb());
                }

                //Solve recursively
                if(solveRec(kGrid, i + 1))
                {
                    //Solution found
                    return true;
                }
                else //Dead end reached
                {
                    //Reset the cells in the grid where values were set for the current cage
                    for(CellVal entry : assortment)
                    {
                        kGrid.setGridCell(entry.getRow(), entry.getCol(), null);
                    }
                }
            }
        }
        return false;
    }

    //Check the entry is valid. No need to check for validity of cages, all assortments are already valid
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

} // end of class KillerAdvancedSolver
