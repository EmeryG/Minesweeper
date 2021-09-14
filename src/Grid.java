import java.util.Arrays;

public class Grid {
    // double array for storing mine vs no-mine data. Stored as 0 (empty) or 1 (mine)
    int height;
    int width;
    private int gridData[][];
    private int hiddenGridData[][];
    
    Grid(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        
        this.gridData = new int[height][width]; 
        randomFillGrid(mines);
        
        this.hiddenGridData = new int[height][width];
        fillEmptyGrid();
    }
    
    private void fillEmptyGrid() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                // -1 indicates unknown
                hiddenGridData[row][column] = -1;
            }
        }
    }
    
    private void randomFillGrid(int mines) {
        // Method for randomly distributing mines across rows
        int[] mineRowDistribution = rowDistribution(mines);
        
        // Distribute mines horizontally in each row and insert the result into each respective row
        for (int row = 0; row < height; row++) {
            gridData[row] = columnDistribution(mineRowDistribution[row]);
        }
    }
    
    // Distributes mine counts randomly across all rows
    private int[] rowDistribution(int mines) {
        int[] rows = new int[height];
        
        // Converting height to double so decimal result is calculated.
        double avgMinesPerRow = Double.valueOf(mines)/height;
        // Calculating maximum value of mines. The max is the average * 2 or width, whichever is smaller. Code ensures that maximum per row does not exceed width.
        double mineRowMax = (avgMinesPerRow*2 > width) ? width : avgMinesPerRow*2;
        
        for (int row = 0; row < height; row++) {
            int mineCount = 0;
            
            // Mine Distribution Algorithm. Calculates based off of total average vs current average of mines per row.
            if ( (Double.valueOf(height)/mines) > avgMinesPerRow) {
                // Round down amount of mines if total avg > current avg
                mineCount = (int) Math.ceil(Math.random()*mineRowMax);
            } else {
                // Round up amount of mines if total avg > current avg
                mineCount = (int) Math.floor(Math.random()*mineRowMax);
            }
            
            // Check that the program is not exceeding the amount of mines
            if (mineCount > mines) {
                rows[row] = mines;
                mines = 0;
            } else {
                // Subtract mineCount from mines for more accurate distribution
                rows[row] = mineCount;
                mines -= mineCount;
            }
        }
        
        // I mines are remaining, this algorithm finds spaces for mines to fit.
        if (mines > 0) {
            for (int mine = 0; mine < mines; mine++) {
                // Rounds down since index starts at 0.
                int rowNum = (int) Math.floor(Math.random()*height);
                
                // if adding a mine to a row exceeds the maximum, iterate through rows to find an opening
                while (rows[rowNum] > mineRowMax) {
                    if (rowNum-1 < 0) {
                        rowNum = height-1;
                    } else {
                        // NOTE because of scale limitations, mines will have a slight bias to be near the top of the grid. It should not substantially affect user experience.
                        rowNum--;
                    }
                }
                
                rows[rowNum]++;
            }
        }
        
        return rows;
    }
    
    // Decides mine placements randomly across a single row
    private int[] columnDistribution(int rowMines) {
        int[] column = new int[width];
        // Fill the array with the nonmine value as default
        Arrays.fill(column, 0);
        
        int directionVector = 0;
        
        // Algorithm for distributing mines within a single row
        for (int mine = 0; mine < rowMines; mine++) {
            int columnNum = (int) Math.floor(Math.random()*width);
            
            // Checks if there is already a mine at the randomized location
            if (column[columnNum] == 1) {
                int iterator = -1;
                
                // Change iterator direction if there is more mines before the row's midpoint
                if(directionVector < 0) {
                    iterator = 1;
                }
                
                // iterate to find empty spaces
                while(column[columnNum] == 1) {
                    columnNum = columnNum + iterator;
                    
                    // if iterator passes row bounds, adjust to begin at other side of row
                    if (columnNum > width) {
                        columnNum = 0;
                    } else if (columnNum < 0) {
                        columnNum = width-1;
                    }
                }
            }
            
            // set column to have a mine
            column[columnNum] = 1;
            
            // calculate directionVector based off of whether the midpoint is passed or not.
            if (columnNum < Double.valueOf(width)/2) {
                directionVector -= 1;
            } else {
                directionVector += 1;
            }
        }
        
        return column;
    }
    
    // If not -1, which is unrevealed, return true.
    public boolean isRevealed(int row, int col) {
        if (hiddenGridData[row][col] != -1) {
            return true;
        }
        
        return false;
    }
    
    public int[][] getGridData() {
        return gridData;
    }
    
    // -1 is hidden
    // 0 is no mines
    // all other numbers indicate adjacent mines
    public void revealHiddenGrid(int row, int col, int value) {
        hiddenGridData[row][col] = value;
    }
    
    // Displays grid with hidden and revealed mines.
    public void displayGrid() {
        String[] display = new String[height+1];
        
        display[0] = topRow();
        
        // Formatting remaining rows
        for (int row = 1; row < height+1; row++) {
            // Reset coloring
            display[row] = Integer.valueOf(row) + "\t";
            
            for (int column = 0; column < width; column++) {
                // Switch on integer value in an attempt to make colors resemble Windows 98 Minesweeper
                switch (hiddenGridData[row-1][column]) {
                    case -1:
                        display[row] += "*  ";
                        break;
                    case 0:
                        display[row] += "   ";
                        break;
                    case 1:
                        display[row] += "1  ";
                        break;
                    case 2:
                        // Display a green 2 representing two adjacent mines
                        display[row] += "2  ";
                        break;
                    case 3:
                        // Display a red 3 representing three adjacent mines
                        display[row] += "3  ";
                        break;
                    case 4:
                        // Display a purple 4 representing four adjacent mines
                        display[row] += "4  ";
                        break;
                    case 5:
                        // Display a brown 5 representing five adjacent mines
                        display[row] += "5  ";
                        break;
                    case 6:
                        // Display a cyan 6 representing six adjacent mines
                        display[row] += "6  ";
                        break;
                    case 7:
                        // Display a magenta 7 representing seven adjacent mines
                        display[row] += "7  ";
                       break;
                    case 8:
                        // Display a black 8 representing eight adjacent mines
                        display[row] += "8  ";
                        break;
                }
            }
        }
        
        // Display Grid in Console
        for (String str : display) {
            System.out.println(str);
        }
    }
    
    // Displays mine and nonmine locations.
    public void displayGridData() {
        String[] display = new String[height+1];
        
        display[0] = topRow();
        
        // Formatting remaining rows
        for (int row = 1; row < height+1; row++) {
            // Reset coloring
            display[row] = Integer.valueOf(row) + "\t";
            
            for (int column = 0; column < width; column++) {
                if (gridData[row-1][column] == 1) {
                    // Adding a red "X" representing a mine
                    display[row] += "X  ";   
                } else {
                    // Adding a black "*" representing an empty space
                    display[row] += "*  ";
                }
            }
        }
        
        // Display Grid in Console
        for (String str : display) {
            System.out.println(str);
        }
    }
    
    // Formats top row String of grid
    private String topRow() {
        
        // Inserts line from previous input and a tab to match later grid formatting.
        String top = "\n\t";
        
        // Formatting top row
        for (int column = 1; column < width+1; column++) {
            // Checking size of column number so that grid formatting looks nicer
            if (column < 10) {
                top += Integer.toString(column) + "  ";
            } else if (column < 100) {
                top += Integer.toString(column) + " ";
            } else {
                top += Integer.toString(column);
            }
        }
        
        return top;
    }
}
