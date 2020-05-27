/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Arrays;

import grid.SudokuGrid;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{
    int gDim;
    int bDim;
    String[][] board;
    String[] symbols;
    ColNode entryPoint;
    ArrayList<ECMNode> solution;
    boolean[][] ecMatrix;
    

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

        constructStartingECMatrix();
        System.out.println("matrix constructed");
        entryPoint = construct2DLL();

        boolean solved = solveDancingLinks();

        if(solved)
        { 
            //fillGrid();
            for(ECMNode node : solution)
            {
                System.out.println(node.col.name);
            }
        }
        
       

        return solved;
    } // end of solve()

    /*
    private fillGrid()
    {
        for(ECMNode solNode : solution)
        {
            ECMNode 
        }
    }
    */

    private boolean solveDancingLinks()
    {
        if(entryPoint.right == entryPoint)
        {
            return true;
        }
        
        ColNode smallestCol = findSmallestColNode();
        smallestCol.coverNode();

        ECMNode nodeInCol = smallestCol.down;
        while(nodeInCol != smallestCol)
        {
            solution.add(nodeInCol);

            ECMNode nodeInRow = nodeInCol.right;
            while(nodeInRow != nodeInCol)
            {
                nodeInRow.col.coverNode();
                nodeInRow = nodeInRow.right;
            }

            if(solveDancingLinks())
            {
                return true;
            }

            nodeInCol = solution.remove(solution.size() - 1);
            smallestCol = nodeInCol.col;

            ECMNode nodeToRecover = nodeInCol.left;
            while(nodeToRecover != nodeInCol)
            {
                nodeToRecover.col.uncoverNode();
                nodeToRecover = nodeToRecover.left;
            }

            nodeInCol = nodeInCol.down;
        }
        smallestCol.uncoverNode();
        return false;
    }

    private int findECMRow(int row, int col, String symb)
    {
        int symbInd = 0;
        for(int s = 1; s <= symbols.length; s++)
        {
            if(symbols[s - 1].equals(symb))
            {
                symbInd = s - 1;
                break;
            }
        }
        int rowInd = (int) Math.pow(gDim, 2) * (row - 1);
        int colInd = gDim * (col - 1);
        return rowInd + colInd + symbInd;
    }

    private ColNode findSmallestColNode()
    {
        ColNode currNode = (ColNode) entryPoint.right;
        ColNode chosenNode = null;
        int smallestColSize = Integer.MAX_VALUE;
        while(currNode != entryPoint)
        {
            if(currNode.size < smallestColSize)
            {
                smallestColSize = currNode.size;
                chosenNode = currNode;
            }
            currNode = (ColNode) currNode.right;
        }
        return chosenNode;
    }

    private void constructStartingECMatrix()
    {
        constructBlankECMatrix();
        for(int row = 1; row <= gDim; row++)
        {
            for(int col = 1; col <= gDim; col++)
            {
                String cellVal = board[row - 1][col - 1];
                if(cellVal != null)
                {
                    for(String symb : symbols)
                    {
                        if(!symb.equals(cellVal))
                        {
                            Arrays.fill(ecMatrix[findECMRow(row, col, symb)],
                                        false);
                        }
                    }
                }
            }
        }
        System.out.println("stop");
    }

    private ColNode construct2DLL()
    {
        int totalColNodes = ecMatrix[0].length;
        ColNode entryNode = new ColNode("h");
        ArrayList<ColNode> colNodes = new ArrayList<>();

        for(int i = 0; i < totalColNodes; i++)
        {
            ColNode nextColNode = new ColNode(Integer.toString(i));
            colNodes.add(nextColNode);
            ColNode currNode = entryNode;
            currNode = (ColNode) currNode.linkNewRight(nextColNode);
        }

        for(boolean[] row : ecMatrix)
        {
            ECMNode last = null;
            for(int col = 0; col < totalColNodes; col++)
            {
                if(row[col])
                {
                    ColNode colNode = colNodes.get(col);
                    ECMNode nextNode = new ECMNode(colNode);
                    if(last == null)
                    {
                        last = nextNode;
                    }
                    colNode.up.linkNewDown(nextNode);
                    last = last.linkNewRight(nextNode);
                    colNode.size++;
                }
            }
        }

        entryNode.size = totalColNodes;
        entryPoint = entryNode;
        return entryNode;
    }
    
    private void constructBlankECMatrix()
    {
        ecMatrix = new boolean[(int) Math.pow(gDim, 3)][(int) Math.pow(gDim, 2) * 4];
        fillMatrix();
    }

    private void fillMatrix()
    {
        fillCellConstraint();
        fillRowConstraint();
        fillColConstraint();
        fillBoxConstraint();
    }
    
    private int getConstraintFirstCol(int multiplier) 
    {
        return multiplier * gDim * gDim;
    }

    private void fillCellConstraint()
    {
        int ecmCol = getConstraintFirstCol(0);
        for(int row = 1; row <= gDim; row++)
        {
            for(int col = 1; col <= gDim; col++)
            {
                for(String symb : symbols)
                {
                    ecMatrix[findECMRow(row, col, symb)][ecmCol] = true;
                }
                ecmCol++;
            }
        }
    }
    
    private void fillRowConstraint()
    {
        int ecmCol = getConstraintFirstCol(1);
        for(int row = 1; row <= gDim; row++)
        {
            for(String symb : symbols)
            {
                for(int col = 1; col <= gDim; col++)
                {
                    ecMatrix[findECMRow(row, col, symb)][ecmCol] = true;
                }
                ecmCol++;
            }
        }
    }

    private void fillColConstraint()
    {
        int ecmCol = getConstraintFirstCol(2);
        for(int col = 1; col <= gDim; col++)
        {
            for(String symb : symbols)
            {
                for(int row = 1; row <= gDim; row++)
                {
                    ecMatrix[findECMRow(row, col, symb)][ecmCol] = true;
                }
                ecmCol++;
            }
        }
    }

    private void fillBoxConstraint()
    {
        int ecmCol = getConstraintFirstCol(3);
        for(int row = 1; row <= gDim; row += bDim)
        {
            for(int col = 1; col <= gDim; col += bDim)
            {
                for(String symb : symbols)
                {
                    for(int bRow = 0; bRow < bDim; bRow++)
                    {
                        for(int bCol = 0; bCol < bDim; bCol++)
                        {
                            ecMatrix[findECMRow(row + bRow, col + bCol, symb)]
                                    [ecmCol] = true;
                        }
                    }
                    ecmCol++;
                }
            }
        }
    }
}