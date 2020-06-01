package grid;

import java.util.ArrayList;

public class Cage 
{
    int cageTotal;
    ArrayList<String> cells;

    public Cage(int cageTotal, ArrayList<String> cells) 
    {
        this.cageTotal = cageTotal;
        this.cells = cells;
    }

    public int getCageTotal() {
        return cageTotal;
    }
    public ArrayList<String> getCells() {
        return cells;
    }
}