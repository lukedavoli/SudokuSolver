import java.io.FileNotFoundException;
import java.io.IOException;

import grid.KillerSudokuGrid;

public class test_KillerSudokuGrid {
    public static void main(String[] args) {
        
        KillerSudokuGrid grid = new KillerSudokuGrid();

        String inFileName = "sampleGames/easy-killer-44-01.in";
        String outFileName = "outFiles/easy-killer-44-01-unsolved.out";

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