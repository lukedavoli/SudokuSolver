/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Class implementing the grid for Killer Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task E and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid
{
    ArrayList<Cage> cages;

    public KillerSudokuGrid() {
        super();
        cages = new ArrayList<>();

    } // end of KillerSudokuGrid()


    /* ********************************************************* */


    @Override
    public void initGrid(String filename) throws FileNotFoundException, IOException
    {
        File gridInitFile = new File(filename);
        Scanner scanner = new Scanner(gridInitFile);

        dimensions = Integer.parseInt(scanner.nextLine());
        bDim = (int) Math.sqrt(dimensions);
        board = new String[dimensions][dimensions];

        symbols = scanner.nextLine().split(" ");

        int numCages = Integer.parseInt(scanner.nextLine());

        while(scanner.hasNextLine())
        {
            String nextCage = scanner.nextLine();

            String[] cageInfo = nextCage.split(" ");
            int cageTotal = Integer.parseInt(cageInfo[0]);
            ArrayList<String> cellLocs = new ArrayList<>();
            for (String cell : cageInfo) 
            {
                if(cell.contains(","))
                {
                    cellLocs.add(cell); 
                }
            }

            Cage newCage = new Cage(cageTotal, cellLocs);
            cages.add(newCage);
        }

        scanner.close();
        
    } // end of initBoard()


    @Override
    public void outputGrid(String filename)
        throws FileNotFoundException, IOException
    {
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
    } // end of outputBoard()


    @Override
    public String toString() {
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
    public boolean validate() {
        if (!validateCells()) {
            return false;
        } else if (!validateAllRows()) {
            return false;
        } else if (!validateAllColumns()) {
            return false;
        } else if (!validateAllBlocks()) {
            return false;
        } else if (!validateAllCages()) {
            return false;
        } else {
            return true;
        }
    } // end of validate()

    private boolean validateAllCages() 
    {
        for(int c = 0; c < cages.size(); c++)
        {
            if(!validateCage(c))
            {
                return false;
            }
        }
        return true;
    }

    private boolean validateCage(int c) 
    {
        ArrayList<String> cageVals = new ArrayList<>();
        Cage cage = cages.get(c);
        String currCell = "";
        int sum = 0;
        //Loop through all the cells in the cage
        for (String cell : cage.cells) 
        {
            //Get the row and column of the current cell
            int row = Character.getNumericValue(cell.charAt(0));
            int col = Character.getNumericValue(cell.charAt(2));

            //Get the value in the current cell
            currCell = board[row][col];
            //Test if it already exists in the list
            if(cageVals.contains(currCell))
            {
                return false;
            }
            //Add it to the list of existing cell values
            cageVals.add(currCell);
            
            //Add its value to the sum for the cage
            int value = Integer.parseInt(currCell);
            sum += value;
        }
        //Test if the sum of values in the cage meets the expected sum
        if(sum == cage.getCageTotal()){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Cage> getCages() 
    {
        return cages;
    }
    

} // end of class KillerSudokuGrid
