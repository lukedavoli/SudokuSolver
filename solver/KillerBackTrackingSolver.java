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
                            kGrid.setGridCell(row, col, symb);

                            if(solveRec(kGrid))
                            {
                                return true;
                            }
                            else
                            {
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

        if(cellsFilled < cageSize && sum > cage.getCageTotal())
        {
            return true;
        }
        else if(cellsFilled == cageSize && sum != cage.getCageTotal())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

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
