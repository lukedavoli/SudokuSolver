/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Arrays;

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

        for(Cage cage : cages)
        {
            cage.generateAssortments(symbols);
        }

        boolean solved = solveRec(kGrid, 0);
        return solved;
    } // end of solve()

    private boolean solveRec(KillerSudokuGrid kGrid, int i) 
    {
        if(i >= cages.size())
        {
            return true;
        }

        ArrayList<ArrayList<CellVal>> assortments = cages.get(i).getCageAssortments();
        for(ArrayList<CellVal> assortment : assortments)
        {
            boolean assortmentValid = true;
            for(CellVal entry : assortment)
            {
                if(assortmentValid && !validEntry(entry.getSymb(), entry.getRow(), entry.getCol()))
                {
                    assortmentValid = false;
                }
            }
            if(assortmentValid)
            {
                for(CellVal entry : assortment)
                {
                    kGrid.setGridCell(entry.getRow(), entry.getCol(), entry.getSymb());
                }

                //System.out.println(Arrays.deepToString(kGrid.getBoard())); 

                if(solveRec(kGrid, i + 1))
                {
                    return true;
                }
                else
                {
                    for(CellVal entry : assortment)
                    {
                        kGrid.setGridCell(entry.getRow(), entry.getCol(), null);
                    }
                }
            }
        }
        return false;
    }

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

} // end of class KillerAdvancedSolver
