import ecs100.*;
import java.awt.Color;


/** Does a slow paint fill, with little "fingers" to show the outstanding
 *    pixels that still need to be followed up.
 */
public class PaintPotSlow{

    // constants
    public static final int ROWS = 20;    // was 25
    public static final int COLS = 20;   // was 40

    public static final int SIZE = 20;     // was 20
    public static final int LEFT = 10;
    public static final int TOP = 10;

    //fields
    private Color[][] grid = new Color[ROWS][COLS]; // the grid of colours
  
    private boolean paintMode = true;   // painting mode (true) or pouring mode (false)
    private Color paint = Color.red;
    private int delay = 30;

    /** 
     * Construct a new PaintPot object
     * and set up the GUI
     */
    public PaintPotSlow(){
        setupGUI();
        clearGrid();
    }
        
    public void setupGUI(){
        UI.setMouseMotionListener(this::doMouse);

        UI.addButton("Paint", ()->{paintMode = true;});
        UI.addButton("Pour", ()->{paintMode = false;});
        UI.addButton("Red", ()->{paint = Color.red;});
        UI.addButton("Yellow", ()->{paint = Color.yellow;});
        UI.addButton("Green", ()->{paint = Color.green;});
        UI.addButton("Blue", ()->{paint = Color.blue;});
        UI.addButton("Orange", ()->{paint = Color.orange;});
        UI.addButton("Clear", this::clearGrid);
        UI.addSlider("Delay", 0,500,delay, (double val)->{delay=(int)val;});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);  // Hide the text area.        
    }


    /** 
     * Respond to mouse events
     */
    public void doMouse(String action, double x, double y){
        int row = (int) ( (y-TOP) / SIZE);   // how many rows down
        int col = (int) ( (x-LEFT) / SIZE);  // how many columns across
        if (!onGrid(row, col) ) { return; }
	if (action.equals("released")) {
            if ( paintMode ){setPixel(row, col, paint);}
            else            {pour(row, col);}
	}
	else if (action.equals("dragged") && paintMode){
            setPixel(row, col, paint);
	}
    }

    /**
     * return true if the mouse event is positioned within the grid
     */
    public boolean onGrid(int row, int col){
        return (row >=0 && row < ROWS && col >= 0 && col <COLS);
    }

    /**
     * Set all the pixels to white (redrawing them)
     */
    public void clearGrid(){
        for (int col=0; col<COLS; col++)
            for (int row=0; row<ROWS; row++){
                setPixel(row, col, Color.white);
            }
    }
    
    
    /**
     *  If this pixel is a different color from the current paint colour,
     *  then pour the paint on this pixel, and let it spread out
     *  through all adjacent pixels that are the same colour as this pixel was.
     *  This will need to call another recursive method which does the
     *  paints the pixel, and then spreads (recursively) to its neighbours.
     */
    public void pour(int row, int col){
        // Check that the pixel is different from the current paint
        // call spread, passing the old color of this pixel
        /*# YOUR CODE HERE */
        if ( !(grid[row][col].equals(paint)) )
            spread(row, col, grid[row][col], paint);
    }
    
    public void spread(int row, int col, Color oldColor, Color newColor){
        if (/*row >= 0 && row < ROWS && col >= 0 && col < COLS &&*/
            grid[row][col].equals(oldColor) ){
            /* change the colour of this cell */
            setPixel(row, col, newColor);
            //highlightPixel(row, col);
            markNeighbours(row, col, oldColor, newColor);
            /* spread to each of the four neighbours, if they are the oldColor */
            spread(row-1, col, oldColor, newColor);
            spread(row, col+1, oldColor, newColor);
            spread(row+1, col, oldColor, newColor);
            spread(row, col-1, oldColor, newColor);
        }
    }
    
    public void setPixel(int row, int col, Color c){
        grid[row][col] = c;
        drawPixel(row, col, c);
    }

    private void drawPixel(int row, int col, Color c){
        int x = LEFT+col*SIZE;
        int y = TOP+row*SIZE;
        UI.setColor(grid[row][col]);
        UI.fillRect(x, y, SIZE, SIZE);
        UI.setColor(Color.lightGray);
        UI.drawRect(x, y, SIZE, SIZE);
    }

    
    private void markNeighbours(int row, int col, Color oldColor, Color newColor){
        if ( row > 0 && grid[row-1][col].equals(oldColor) )
            markNeigh(row, col, -1, 0, newColor);
        
        if (col < COLS-1 && grid[row][col+1].equals(oldColor) )
            markNeigh(row, col, 0, 1, newColor);
        
        if (row < ROWS-1 && grid[row+1][col].equals(oldColor) )
            markNeigh(row, col, 1, 0, newColor);
        
        if (col > 0 && grid[row][col-1].equals(oldColor))
            markNeigh(row, col, 0, -1, newColor);
        
        UI.sleep(delay);
    }
    
    private void markNeigh(int row, int col, int drow, int dcol, Color c){
        //extend an arm from this pixel into the adjacent one
        //compute middle of adjacent pixel:
        int x = LEFT+SIZE*col+SIZE/2 + dcol*SIZE*6/10;  //center of this pixel
        int y = TOP+SIZE*row+SIZE/2 + drow*SIZE*6/10;   //center of this pixel
        int w = SIZE*(1+Math.abs(dcol)*3)/10;
        int h = SIZE*(1+Math.abs(drow)*3)/10;
        UI.setColor(c);
        UI.fillRect(x-w, y-h, w*2, h*2);
    }
    
    
    // Main
    public static void main(String[] arguments){
        new PaintPotSlow();
    }	
}
