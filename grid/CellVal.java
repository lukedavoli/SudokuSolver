package grid;

public class CellVal 
{
    int row;
    int col;
    String symb;

    public CellVal(int r, int c, String s)
    {
        row = r;
        col = c;
        symb = s;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getSymb() {
        return symb;
    }
}
