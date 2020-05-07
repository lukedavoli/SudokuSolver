import java.io.FileNotFoundException;
import java.io.IOException;

import grid.StdSudokuGrid;

public class test_StdSudokuGrid {
    public static void main(String[] args) {
        
        StdSudokuGrid grid = new StdSudokuGrid();

        String inFileName = "sampleGames/easy-std-99-01.in";
        String outFileName = "outFiles/easy-std-99-01-unsolved.out";

        try {
            grid.initGrid(inFileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(grid);
        
        try {
            grid.outputGrid(outFileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(grid.validate())
        {
            System.out.println("Grid is valid");
        }
        else
        {
            System.out.println("Grid is NOT valid");
        }
    }
    
}