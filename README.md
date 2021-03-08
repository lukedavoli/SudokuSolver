# Sudoku Solver
A sudoku puzzle solver appplication that applies transform-and-conquer strategies to solve the constraint satisfaction problem of standard and [killer sudoku](https://en.wikipedia.org/wiki/Killer_sudoku) puzzles. 

![console example](https://i.imgur.com/1RWYiXu.png)

## Algorithms

### Backtracking
An improvement on blind brute force generation of solutions. The backtracking algorithm tries to guess the value of each cell by trial and error, trying the next value if a guess breaks constraints on sudoku puzzles and backtracking if it has run out of possible values until the puzzle is solved or deemed unsolvable.

### Algorithm X
Utilises a binary matrix representation of all possible cell values to represent the puzzle as an [exact cover problem](https://en.wikipedia.org/wiki/Exact_cover). This approach works in a similar way to the backtracking approach, using the binary matrix representation of the grid rather than the grid itself.

### Dancing Links
Algorithm X is limited by the need to search a sparse binary matrix every time it seeks to access, remove or reinsert values. The Dancing Links approach instead represents the exact cover problem as doubly-linked linked 2D list (a very elegant implementation in my opinion).

## Running the Program
```
$ javac *.java grid/*.java solver/*.java
$ java Sudoku [puzzle fileName] [game type] [solver type] [visualisation] <output fileName>
```

* **puzzle files** are in `/sampleGames`
* **game type** may be one of `sudoku` or `killer`
* **solver type**
    * if game type is `sudoku` then choose be one of `backtracking`, `algorx` or `dancing`
    * if game type is `killer` then choose one of `backtracking` or `advanced`
* **visualisation**: y or n

For ease of use, if you open this project in VSCode, I have included the launch.json file which allows running a range of preset configurations in the debugger.
