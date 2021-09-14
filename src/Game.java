import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

public class Game {
    Scanner sc;
    Grid grid;
    
    Game(Scanner sc) {
        this.sc = sc;
    }
    
    public void start() {
        int height;
        int width;
        int mines;
        
        System.out.println("Welcome to Minesweeper Text Edition!\n");
        
        // Ensuring user enters an integer so program will not break
        height = Verify.intVerify("Please enter the height of the game grid (max 100):", sc);
        // Ensuring sure grid height is within reasonable bounds
        if (height > 100) {
            height = 100;
        } else if (height < 1) {
            System.out.println("Invalid height. Setting to 10.");
            height = 10;
        }
        
        width = Verify.intVerify("\nPlease enter the width of the game grid (max 100):", sc);
        // Ensuring sure grid height is within reasonable bounds
        if (width > 100) {
            width = 100;
        } else if (width < 1) {
            System.out.println("Invalid width. Setting to 10.");
            width = 10;
        }
        
        System.out.println("\nAmount of Spaces: " + (height*width));
        
        mines = Verify.intVerify("\nPlease enter the amount of mines (enter -1 for default {10}):", sc);
        
        // Ensures mines are in reasonable bounds. Mine limit is over half the spaces on the grid.
        if (mines > (height*width/2)) {
            mines = height*width/2;
            
            System.out.println("Beyond limit. Setting mines to maximum for current size: " + mines);
        } else if (mines < 0) {
            mines = 10;
        }
        
        this.grid = new Grid(height, width, mines);
        
        gameLoop();
    }
    
    private void gameLoop() {
        int rowSelect;
        int columnSelect;
        
        while (!hasWon()) {
            grid.displayGrid();
            
            rowSelect = Verify.intVerify("\nPlease enter the row number:", sc)-1;
            
            // Verifying user is selecting valid rows 
            if (rowSelect < 0 || rowSelect >= grid.height) {
                System.out.println("Row number invalid. Setting to row 1.");
                rowSelect = 0;
            }
            
            columnSelect = Verify.intVerify("Please enter the column number:", sc)-1;
            
            // Verifying user is selecting valid columns 
            if (columnSelect < 0 || columnSelect >= grid.height) {
                System.out.println("Column number invalid. Setting to column 1.");
                columnSelect = 0;
            }
            
            // Checking if user trips a mine
            if (checkLocation(rowSelect, columnSelect)) {
                System.out.println("\nGAME OVER! YOU LOST!");
                        
                grid.displayGridData();
                
                // returns method so that user does not get the "Win Text"
                return;
            }
        }
        
        System.out.println("\nYOU WON! CONGRATULATIONS!!");
        grid.displayGrid();
    }
    
    // Counts amount of mines adjacent to a particular space
    private int numberOfMines(int row, int col) {
        int[][] gridData = grid.getGridData();
        
        int mineCount = 0;
        
        // Iterating through all adjacent spaces
        for (int y = -1; y < 2; y++) {
            // Ensure program is not checking outside of array bounds
            if (y + row >= 0 && y + row < gridData.length) {
                for (int x = -1; x < 2; x++) {
                    // Ensuring program is not checking outside of array bounds
                    if (x + col >= 0 && x + col < gridData[row].length) {
                        // If mine is present, add to mineCount. Adds x+y to ensure method is not counting the initial location, ONLY adjacent.
                        if (gridData[row+y][col+x] == 1 && (Math.abs(x)+Math.abs(y) != 0) ) {
                            mineCount++;
                        }
                    }
                }
            }
        }
        
        return mineCount;
    }
    
    // iterates through entire game data to check if any nonmines are still unrevealed
    private boolean hasWon() {
        int[][] gridData = grid.getGridData();
        
        for (int row = 0; row < grid.height; row++) {
            for (int col = 0; col < grid.width; col++) {
                if (gridData[row][col] == 0) {
                    if(!grid.isRevealed(row, col)) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    // If mine is present, returns true. 
    // If there is no mines, it calls clearBanks method with current location.
    private boolean checkLocation(int row, int col) {
        if (grid.getGridData()[row][col] == 1) {
            return true;
        } else {
            clearBlanks(row, col);
        } 
        
        return false;
    }
    
    // Reveals all cells that are adjacent to the sell location given.
    // GAME DESIGN ISSUE. User can imply by cells not being revealed if they have a mine or not.
    // Note: I would change the adjacent reveal mechanic to ONlY reveal adjacent cells IF the cell at INITIAL location that is being revealed is ALSO blank. Would be more authentic to the usual game of Minesweeper.
    // Will not fix because adjacent mechanic is integrated into assignment requiremnts.
    private void clearBlanks(int row, int col) {
        int[][] gridData = grid.getGridData();
        
        grid.revealHiddenGrid(row, col, numberOfMines(row, col));
        
        for (int y = -1; y < 2; y++) {
            // Ensure program is not checking outside of array bounds
            if (y + row >= 0 && y + row < gridData.length) {
                for (int x = -1; x < 2; x++) {
                    // Ensuring program is not checking outside of array bounds
                    if (x + col >= 0 && x + col < gridData[row].length) {
                        // ensuring program does not falsly reveal a mine
                        if (gridData[row+y][col+x] == 0) {
                            int nearbyMines = numberOfMines(row+y, col+x);

                            // method calls itself with new location, and that revealed rows are not called again
                            if (nearbyMines == 0 && !grid.isRevealed(row+y, col+x) ) {
                                // reveal before clearBlanks is called again
                                grid.revealHiddenGrid(row+y, col+x, numberOfMines(row+y, col+x));
                                clearBlanks(row+y, col+x);
                            } else {
                                // reveal anyway
                                grid.revealHiddenGrid(row+y, col+x, numberOfMines(row+y, col+x));
                            }
                        }
                    }
                }
            }
        }
    }
}
