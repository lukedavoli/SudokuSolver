package grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Cage 
{
    int cageTotal;
    ArrayList<String> cells;
    ArrayList<ArrayList<CellVal>> cageAssortments;
    ArrayList<ArrayList<Integer>> cageCombos;

    public Cage(int cageTotal, ArrayList<String> cells) 
    {
        cageAssortments = new ArrayList<ArrayList<CellVal>>();
        cageCombos = new ArrayList<>();
        this.cageTotal = cageTotal;
        this.cells = cells;
    }

    public int getCageTotal() {
        return cageTotal;
    }

    public ArrayList<String> getCells() {
        return cells;
    }

    public void generateAssortments(String[] symbols)
    {
        ArrayList<Integer> iSymbols = new ArrayList<>();
        for(int s = 0; s < symbols.length; s++)
        {
            iSymbols.add(Integer.parseInt(symbols[s]));
        }
        
        generateCombos(iSymbols, null);
        generateAssortments();
    }

    private void generateAssortments() 
    {
        for(ArrayList<Integer> combo : cageCombos)
        {
            int start = 0;
            permuteCombo(combo, start);
        }
    }

    private void permuteCombo(ArrayList<Integer> combo, int start)
    {
        if(start == combo.size())
        {   
            ArrayList<CellVal> assortment = new ArrayList<>();
            for(int i = 0; i < combo.size(); i++)
            {
                String[] cell = cells.get(i).split(",");
                int r = Integer.parseInt(cell[0]);
                int c = Integer.parseInt(cell[1]);
                String s = Integer.toString(combo.get(i));  
                CellVal nextVal = new CellVal(r, c, s);
                assortment.add(nextVal);
            }
            cageAssortments.add(assortment);
            return;
        }
        for(int i = start; i < combo.size(); i++)
        {
            int temp = combo.get(i);
            combo.set(i, combo.get(start));
            combo.set(start, temp);

            permuteCombo(combo, start + 1);

            int temp2 = combo.get(i);
            combo.set(i, combo.get(start));
            combo.set(start, temp2);
        }
    }

    private void generateCombos(ArrayList<Integer> symbols, ArrayList<Integer> partial) 
    {
        if(partial == null)
        {
            partial = new ArrayList<>();   
        }
        int sum = partial.stream().mapToInt(Integer::intValue).sum();
        if(sum == cageTotal && partial.size() == cells.size())
        {
            cageCombos.add(partial);
        }

        for(int i = 0; i < symbols.size(); i++)
        {
            int n = symbols.get(i);
            ArrayList<Integer> remaining = new ArrayList<>();
            for(int j = i + 1; j < symbols.size(); j++)
            {
                remaining.add(symbols.get(j));
            }
            ArrayList<Integer> partialN = new ArrayList<>(partial);
            partialN.add(n);
            generateCombos(remaining, partialN);
        }
    }

    public ArrayList<ArrayList<CellVal>> getCageAssortments()
    {
        return cageAssortments;
    }

    
}