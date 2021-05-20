import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;


/** Program:      Lab 9 -  Finding Words in a 5x5 Grid in Non-straight Lines
 *  Author: Gill, pondy
 *  Date: February 1999
 *  Description:  The program searchs for a user entered string in a 5x5 
 *                grid which is read from a file. The string need not be in a 
 *                straight line. The program prints the number of times 
 *                the string appears in the grid. Letters are reused.
 *
 *                The program gives the user the following options
 *                - load a grid from a file, which is then displayed
 *                - enter the search word
 *
 *                We assume the file is going to be of the form of 5 lines
 *                each containing five characters.  Excess characters on a
 *                line are ignored, as are excess lines.  If the file does
 *                not contain either five lines or five characters on each
 *                of the first five lines the file is considered invalid.
 *
 */

public class WordGrid{
    public static final int SIZE = 5;	// size of the grid, we assume it is square 
    public static final double CELL_SIZE = 50;  // size of each cell on the screen
    public static final double LEFT = 50;  // left side of grid
    public static final double TOP  = 50;  // top of grid

    // Fields
    private boolean validGrid = false; // flag indicating whether the grid is valid 
    private String[][] grid = new String[SIZE][SIZE]; // the grid of characters  
    private String wordStr;  // the word we are searching for (as a string)
    private String[] word;  // the word we are searching for 
    private int count = 0;      // number of occurrences we have found
    private int delay = 20;
    private int foundPause = 300;
    

    /** Construct a new WordGrid object
     * and set up the GUI
     */
    public WordGrid(){
        setupGUI();
        validGrid = readGridFromFile("doggy.txt");
        drawGrid();
    }

    public void setupGUI(){
        UI.addButton("New Grid", this::newGrid);
        UI.addTextField("Word:", this::setWord);
        UI.addButton("Faster", this::faster);
        UI.addButton("Slower", this::slower);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);  // Hide the text area.
        UI.setFontSize(16);
    }


    public void newGrid(){
        validGrid = readGridFromFile(UIFileChooser.open("Choose grid file"));
        drawGrid();
    }

    public void setWord(String v){
        wordStr = v;
        word = new String[wordStr.length()];
        for (int i=0; i<word.length; i++){
            word[i]= ""+wordStr.charAt(i);
        }
        searchGrid();
    }


    public void faster(){
        this.delay = Math.max(0, this.delay-20);
        this.foundPause = Math.max(0, this.foundPause-50);
    }

    public void slower(){
        this.delay = Math.max(0, this.delay+20);
        this.foundPause = Math.max(0, this.foundPause+50);
    }

    /** 
     * Find the word in the grid
     */
    public void searchGrid() { 
        count = 0;
        if( !validGrid )
            this.report("No grid to search in");   
        else if ( word == null || word.length==0)  
            this.report("No word to search for");   
        else {
            displayWord();
            this.report("Found no occurences of " + wordStr);
            for(int row = 0; row < SIZE; row++)	     // search from each cell 
                for(int col = 0; col < SIZE; col++) {
                    searchWord(row, col, 0);
                }
        }
    }  

    /**
     *  Search for the search word from a place.
     * 
     *  Arguments:    row and col - the cell to look in (possibly outside the grid!)
     *                pos         - the number of characters of the searchWord found so far
     *
     *  Description:
     *    This is the recursive find method.
     *    It must check whether the row and col are a valid cell in the grid
     *    and then check whether the next character is at this cell
     *    If not, then don't search any further from this cell
     *    If the character is at this cell, then
     *      if this was the last character of the searchWord, then we have found it.
     *      else keep searching for the rest of the word in all the neighbour cells
     */
    public void searchWord(int row, int col, int pos ) {
	if  (row < 0 || row >= SIZE || col < 0 || col >= SIZE) // position is off the grid.
	    return;
	highlightCell(row, col, pos);
	if ( ! word[pos].equals(grid[row][col]) ){ 	// this character is not right
	    unHighlightCell(row, col, pos);
	    return;
	}
	if(pos + 1 == word.length) {	        // We have found the entire string 
	    count++;
	    this.report("Found occurence " + count + " of " + wordStr);
	    UI.sleep(foundPause);
	}
	else {						// we have more of the word to look for
	    searchWord(row-1, col,   pos+1);		// look north
	    searchWord(row-1, col+1, pos+1);		// look north-east
	    searchWord(row,   col+1, pos+1);		// look east
	    searchWord(row+1, col+1, pos+1);		// look south-east
	    searchWord(row+1, col,   pos+1);		// look south
	    searchWord(row+1, col-1, pos+1);		// look south-west
	    searchWord(row,   col-1, pos+1);		// look west
	    searchWord(row-1, col-1, pos+1);		// look north-west
	}
	unHighlightCell(row, col, pos);
    }

    /**
     * Read the grid from a file.
     * Returns true if it successfully read a grid from file, otherwise returns false
     */
    public boolean readGridFromFile(String fname) {                 
        try{
            Scanner letterfile = new Scanner(new File(fname));
            for(int row = 0; row < SIZE; row++) {      /* read the grid row by row */
                String line = letterfile.nextLine();
                for(int col = 0; col < SIZE; col++) {    /* filling the columns of each row */
                    grid[row][col] = ""+line.charAt(col);   /* put the character into the grid */
                }
            }
            return true;
        }
        catch(Exception e){return false;}
    }
    
    /** 
     * Display the grid in the graphics output region
     */
    public void drawGrid() {
        UI.clearGraphics();
        if ( !validGrid ) 
            this.report("There is no valid grid");    // tell the user the we have no grid
        else
            for(int row = 0; row < SIZE; row++)     // print the grid row by row 
                for(int col = 0; col < SIZE; col++)
                    drawCell(row, col);
    }

    /** 
     * Draw the cell: a square with a character in the middle
     */
    public void drawCell(int row, int col) {
        double x = LEFT + col*CELL_SIZE;
        double y = TOP + row*CELL_SIZE;
        UI.setLineWidth(1);
        UI.eraseRect(x, y, CELL_SIZE, CELL_SIZE);
        UI.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        UI.drawString(grid[row][col], x+CELL_SIZE/2-8, y+CELL_SIZE/2+6);
    }

    /** 
     * Highlight the cell by drawing a coloured square inside the cell.
     * The highlight depends on the level so a cell might be highlighted at several
     * different levels at once.
     * The greater the level, the smaller the red square.
     * Also highlights the character of the searchWord being looked at.
     */
    public void highlightCell(int row, int col, int level) {
        Color c = Color.getHSBColor(1.0f*level/word.length, 1.0f, 1.0f);
        highlightCell(row, col, level, c);
    }
    /** 
     * Remove the highlights of this cell at this and any higher levels 
     */
    public void unHighlightCell(int row, int col, int level) {
        highlightCell(row, col, level, Color.white);
    }

    public void highlightCell(int row, int col, int level, Color c) {
        double indent = 2+level*2;
        double x = LEFT + col*CELL_SIZE;
        double y = TOP + row*CELL_SIZE;
        UI.setColor(c);
        UI.setLineWidth(3);
        UI.drawRect(x+indent, y+indent, CELL_SIZE-2*indent, CELL_SIZE-2*indent);

        // highlight the character in the search word.
        x = (level+3.125)*CELL_SIZE;
        y = (SIZE+2.125)*CELL_SIZE;
        UI.setLineWidth(CELL_SIZE*0.25);
        UI.drawRect(x, y, CELL_SIZE*0.75, CELL_SIZE*0.75);
        UI.setColor(Color.black);
        UI.sleep(delay);
    }
    

    public void displayWord(){
        UI.eraseRect(0, (SIZE+2)*CELL_SIZE, 20*CELL_SIZE, CELL_SIZE);
        double y = (SIZE+2.5)*CELL_SIZE  + 6;
        UI.drawString("Searching", 10, y);
        UI.setFontSize(20);
        for (int pos = 0; pos <word.length; pos++)
            UI.drawString(word[pos], (pos+3.5)*CELL_SIZE-6, y);
        UI.setFontSize(16);
    }
    
    public void report(String s){
        UI.eraseRect(0, (SIZE+3.5)*CELL_SIZE, 400, CELL_SIZE);
        UI.drawString(s, 10, (SIZE+4)*CELL_SIZE+6);
    }
    
    // Main
    public static void main(String[] arguments){
        new WordGrid();
    }	

}
