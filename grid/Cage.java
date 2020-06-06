package grid;

import java.util.ArrayList;

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

    //Convert symbols to integers before generating combos and then assortments
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

    //Produce the all possible assortments for all combos for a cage
    private void generateAssortments() 
    {
        for(ArrayList<Integer> combo : cageCombos)
        {
            int start = 0;
            permuteCombo(combo, start);
        }
    }

    //Find all permutations of a combination of numbers
    private void permuteCombo(ArrayList<Integer> combo, int start)
    {
        //If at last element, nothing left to swap so can be added to assortment
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
        //Swap elements in combo and call recursively to permuteCombo
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

    /*Find all possible combinations of unique values which sum to the total
    * for the cage using the amount of cells in the cage and given symbols
    */
    private void generateCombos(ArrayList<Integer> symbols, ArrayList<Integer> partial) 
    {
        if(partial == null)
        {
            partial = new ArrayList<>();   
        }
        //Find the sum of the current subset of the symbols
        int sum = partial.stream().mapToInt(Integer::intValue).sum();
        //If it matches the total for the cage and uses the right amount of symbols, add it to the list of possible combos
        if(sum == cageTotal && partial.size() == cells.size())
        {
            cageCombos.add(partial);
        }

        //Iterating through each element in the array, recursively check each subset for validity
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