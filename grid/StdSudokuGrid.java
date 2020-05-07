/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.Scanner;


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid extends SudokuGrid
{

    public StdSudokuGrid() {
        super();

    } // end of StdSudokuGrid()


    /* ********************************************************* */


    @Override
    public void initGrid(String filename) throws FileNotFoundException, IOException {
        File gridInitFile = new File(filename);
        Scanner scanner = new Scanner(gridInitFile);

        dimensions = Integer.parseInt(scanner.nextLine());
        bDim = (int) Math.sqrt(dimensions);
        board = new String[dimensions][dimensions];

        symbols = scanner.nextLine().split(" ");

        while (scanner.hasNextLine()) {
            String initValuePos = scanner.nextLine();
            int row = Character.getNumericValue(initValuePos.charAt(0));
            int col = Character.getNumericValue(initValuePos.charAt(2));
            String value = initValuePos.substring(4);
            board[row][col] = value;
        }
        scanner.close();
    }

    @Override
    public void outputGrid( String filename) throws FileNotFoundException, IOException {
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
    }

    @Override
    public String toString() 
    {
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
    public boolean validate() 
    {
        // TODO: untested and most likely incomplete

        if (!validateCells()) {
            return false;
        } else if (!validateAllRows()) {
            return false;
        } else if (!validateAllColumns()) {
            return false;
        } else if (!validateAllBlocks()) {
            return false;
        } else {
            return true;
        }
    } // end of validate()

    

} // end of class StdSudokuGrid
