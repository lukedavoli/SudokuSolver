/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import grid.SudokuGrid;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
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
    ArrayList<ECMNode> solution;
    ColNode head;
    

    public DancingLinksSolver()
    {
        solution = new ArrayList<>();
    } // end of DancingLinksSolver()


    @Override
    public boolean solve(SudokuGrid grid) 
    {
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
        head = constructDancingLinks(ecMatrix);
        boolean solved = solve(0);

        return solved;
    } // end of solve()

    private boolean solve(int call){
        if (head.right == head)
        {
            return true;
        } 
        else
        {
            ColNode nextCol = (ColNode) head.right;
            nextCol.coverNode();

            for(ECMNode r = nextCol.down; r != nextCol; r = r.down)
            {
                solution.add(r);

                for(ECMNode c = r.right; c != r; c = c.right)
                {
                    c.col.coverNode();
                }

                if(solve(call + 1))
                {
                    return true;
                }

                r = solution.remove(solution.size() - 1);
                nextCol = r.col;

                for(ECMNode c = r.left; c != r; c = c.left)
                {
                    c.col.uncoverNode();
                }
            }
            nextCol.uncoverNode();
            return false;
        }
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

    private ColNode constructDancingLinks(boolean[][] grid)
    {
        ColNode head = new ColNode("H");
        ArrayList<ColNode> colNodes = new ArrayList<>();

        ColNode lastNode = head;
        for(int c = 0; c < dimSquared * CONSTRAINTS; c++)
        {
            ColNode nextColNode = new ColNode(Integer.toString(c));
            colNodes.add(nextColNode);
            lastNode.linkNewRight(nextColNode);
            lastNode = nextColNode;
        }

        for(int r = 0; r < dimCubed; r++)
        {
            ECMNode lastNodeAdded = null;
            for(int c = 0; c < dimSquared * CONSTRAINTS; c++)
            {
                if(ecMatrix[r][c])
                {
                    ColNode colOfNewNode = colNodes.get(c);
                    ECMNode newNode = new ECMNode(colOfNewNode);
                    if(lastNodeAdded == null)
                    {
                        lastNodeAdded = newNode;
                    }

                    colOfNewNode.up.linkNewDown(newNode);
                    colOfNewNode.size++;
                }
            }    
        }
        
        head.size = dimSquared * CONSTRAINTS;
        return head;
    }

}