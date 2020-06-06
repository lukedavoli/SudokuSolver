/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import grid.SudokuGrid;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver
{
    final int CONSTRAINTS = 4;

    int gDim;
    int bDim;
    String[][] board;
    String[] symbols;
    boolean[][] ecMatrix;
    int dimSquared;
    int dimCubed;
    boolean[] ecmCoveredRows;
    boolean[] ecmCoveredCols;
    ArrayList<Integer> solution;

    public AlgorXSolver() {
        solution = new ArrayList<>();
    } // end of AlgorXSolver()

    @Override
    public boolean solve(SudokuGrid grid) {
        this.gDim = grid.getDimensions();
        this.bDim = grid.getBDim();
        this.board = grid.getBoard();
        this.symbols = grid.getSymbols();
        dimSquared = (int) Math.pow(gDim, 2);
        dimCubed = dimSquared * gDim;
        ecmCoveredRows = new boolean[dimCubed];
        ecmCoveredCols = new boolean[dimSquared * CONSTRAINTS];

        //construct the ECMatrix and cover values already in the grid
        constructECMatrix();
        coverExistingValues();
        //Find the solution
        boolean solved = solveECMatrix(ecmCoveredRows, ecmCoveredCols);
        
        //Iterate through the solution found and set the cells in the grid accordingly
        int r, c, v;
        for(int s : solution)
        {
            r = matrixRowToGridRow(s);
            c = matrixRowToGridCol(s);
            v = matrixRowToGridVal(s);
            grid.setGridCell(r - 1, c - 1, symbols[v - 1]);
        }

        return solved;
    } // end of solve()

    //Recursively search for the solution
    private boolean solveECMatrix(boolean[] ecmCoveredRowsF,
                                  boolean[] ecmCoveredColsF) 
    {
        //Create copies of the lists of covered rows and columns so not to alter lists
        //  from previous call
        boolean[] ecmCoveredRows = Arrays.copyOf(ecmCoveredRowsF,
                                                 ecmCoveredRowsF.length);
        boolean[] ecmCoveredCols = Arrays.copyOf(ecmCoveredColsF, 
                                                 ecmCoveredColsF.length);
        
        //Exact cover matrix is empty/all rows and columns covered, grid solved
        if(allRowsCovered(ecmCoveredRows) && allColsCovered(ecmCoveredCols))
        {
            //Solution found
            return true;
        }
        //All rows in ECM exhausted but column constraints remain
        else if(allRowsCovered(ecmCoveredRows) && !allColsCovered(ecmCoveredCols))
        {
            //BACKTRACK
            return false;
        }
        
        //Iterate through all remaining rows in the matrix
        for(int mr = 0; mr < ecmCoveredRows.length; mr++)
        {
            if(!ecmCoveredRows[mr])
            {
                //Add the next row to the solution and update the matrix by covering satisfied rows and columns
                solution.add(mr);
                ArrayList<Integer> colsCovered = coverSatisfiedColumns(ecmCoveredCols, mr);
                ArrayList<Integer> rowsCovered = coverSatisfiedRows(ecmCoveredRows, mr);

                //Solve recursively
                if(solveECMatrix(ecmCoveredRows, ecmCoveredCols))
                {
                    //Solution found
                    return true;
                }
                else
                {
                    //Uncover last rows and columns covered
                    for(int r : rowsCovered)
                    {
                        ecmCoveredRows[r] = false;
                    }
                    for(int c : colsCovered)
                    {
                        ecmCoveredCols[c] = false;
                    }
                    //Remove the row from the solution
                    solution.remove(Integer.valueOf(mr));
                }
            }
        }
        return false;
    }

    //Identify whether all the columns are covered or not
    private boolean allColsCovered(boolean[] ecmCoveredCols) 
    {
        for(boolean mc : ecmCoveredCols)
        {
            if(!mc)
            {
                return false;
            }
        }
        return true;
    }

    //Identify whether all the rows are covered or not
    private boolean allRowsCovered(boolean[] ecmCoveredRows) 
    {
        for(boolean mr : ecmCoveredRows)
        {
            if(!mr)
            {
                return false;
            }
        }
        return true;
    }

    private void coverExistingValues() 
    {
        //Iterate through each cell in the grid
        for(int i = 1; i <= gDim; i++)
        {
            for(int j = 1; j <= gDim; j++)
            {
                //If the board has a value at the current position, cover the satisfied rows and columns
                if(board[i - 1][j - 1] != null)
                {
                    int rowIndex = rcvToMatrixRow(i, j, board[i - 1][j - 1]);
                    coverSatisfiedColumns(ecmCoveredCols, rowIndex);
                    coverSatisfiedRows(ecmCoveredRows, rowIndex);
                }
            }
        }
    }

    // Get the row in the matrix associated with a specific grid row, column and value
    private int rcvToMatrixRow(int i, int j, String symb) 
    {
        int v = indexOf(symb);
        return (i - 1) * dimSquared + (j - 1) * gDim + (v - 1);
    }

    //Get a value 1-4, 1-9, 1-16, etc. for a string symbol
    private int indexOf(String symb)
    {
        int i = 0;
        while(i < symbols.length)
        {
            if(symbols[i].equals(symb))
            {
                break;
            }
            i++;
        }
        return i + 1;
    }

    //Cover all matrix rows satisfied by the solution row covered
    private ArrayList<Integer> coverSatisfiedRows(boolean[] ecmCoveredRows, int rowIndex) 
    {
        ArrayList<Integer> coveredRows = new ArrayList<>();
        int solR = matrixRowToGridRow(rowIndex);
        int solC = matrixRowToGridCol(rowIndex);
        int solV = matrixRowToGridVal(rowIndex);
        int r, c, v;

        for(int mRow = 0; mRow < dimCubed; mRow++)
        {
            //Get the current grid row, column and value for the current matrix row
            r = matrixRowToGridRow(mRow);
            c = matrixRowToGridCol(mRow);
            v = matrixRowToGridVal(mRow);

            if(!ecmCoveredRows[mRow])
            {
                if(r == solR && c == solC) //rows denote same cell
                {
                    ecmCoveredRows[mRow] = true;
                    coveredRows.add(mRow);
                }
                else if(r == solR && v == solV) //rows denote same value in same row
                {
                    ecmCoveredRows[mRow] = true;
                    coveredRows.add(mRow);
                }
                else if(c == solC && v == solV) //rows denote same value in same column
                {
                    ecmCoveredRows[mRow] = true;
                    coveredRows.add(mRow);
                }
                else if(getBox(r, c) == getBox(solR, solC) && v == solV) //rows denote same value in same box
                {
                    ecmCoveredRows[mRow] = true;
                    coveredRows.add(mRow);
                }
            }
        }
        return coveredRows;
    }

    //cover all matrix columns satisfied by the solution row
    private ArrayList<Integer> coverSatisfiedColumns(boolean[] ecmCoveredCols, int rowIndex)
    {
        ArrayList<Integer> coveredCols = new ArrayList<>();
        for(int j = 0; j < dimSquared * CONSTRAINTS; j++)
        {
            if(ecMatrix[rowIndex][j])
            {
                ecmCoveredCols[j] = true;
                coveredCols.add(j);
            }
        }
        return coveredCols;
    }

    //generate the exact cover matrix
    private void constructECMatrix() 
    {
        final int COLUMNS = dimSquared * CONSTRAINTS;
        ecMatrix = new boolean[dimCubed][COLUMNS];
        int r, c, v, v1, v2;

        //Loop through each row of the ECM
        for(int i = 0; i < dimCubed; i++)
        {
            //Get the grid row, column and value for the current row in the ECM
            r = matrixRowToGridRow(i);
            c = matrixRowToGridCol(i);
            v = matrixRowToGridVal(i);
            //Loop through each column of the ECM
            for(int j = 0; j < COLUMNS; j++)
            {
                //Get the values for the current constraint column using j
                v1 = matrixColToV1(j);
                v2 = matrixColToV2(j);

                //Compare with the values for the current row, if equal set to true
                if(j < dimSquared) //Cell occupied constraint
                {
                    if(v1 == r && v2 == c)
                    {
                        ecMatrix[i][j] = true;
                    }
                }
                else if(j >= dimSquared && j < dimSquared * 2) //Row value constraint
                {
                    if(v1 == r && v2 == v)
                    {
                        ecMatrix[i][j] = true;
                    }
                }
                else if(j >= dimSquared * 2 && j < dimSquared * 3) //Column value constraint
                {
                    if(v1 == c && v2 == v)
                    {
                        ecMatrix[i][j] = true;
                    }
                }
                else if(j >= dimSquared * 3 && j < dimSquared * 4) //Box value constraint
                {
                    if(v1 == getBox(r, c) && v2 == v)
                    {
                        ecMatrix[i][j] = true;
                    }
                }
            }
        }
    }

    //Get the box of a cell based on its grid row and column
    private int getBox(int r, int c) 
    {
        int bR = (r - 1) / bDim + 1;
        int bC = (c - 1) / bDim + 1;
        return (bR - 1) * bDim + bC;
    }

    //Convert matrix column index to the second value in a constraint pair
    private int matrixColToV2(int j)
    {
        return ((j % dimSquared) % gDim) + 1;
    }

    //Convert matrix column index to the first value in a constraint pair
    private int matrixColToV1(int j) 
    {
        return (j % dimSquared) / gDim + 1;
    }

    //Get the symbol associated with a matrix row index
    private int matrixRowToGridVal(int i) 
    {
        return i % gDim + 1;
    }

    //Get the grid col associated with a matrix row index
    private int matrixRowToGridCol(int i) 
    {
        return (i % dimSquared) / gDim + 1;
    }

    //Get the grid row associated with a matrix row index
    private int matrixRowToGridRow(int i) 
    {
        return i / dimSquared + 1;
    }

} // end of class AlgorXSolver