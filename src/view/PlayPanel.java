/*
 * TCSS 305 - Autumn 2016
 * Assignment 6 - Tetris
 */


package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;

import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;



import model.Board;


/**
 * 
 * Represents the main panel on which the tetris pieces are displayed.
 * 
 * @author Yaro Salo
 * @version December 3, 2016
 */
public class PlayPanel extends JPanel implements Observer, PropertyChangeListener  {

    
    /** Generated serialization id for this class. */
    private static final long serialVersionUID = 810569363798500887L;
    
    /** The default width of the panel. */
    private static final int DEFAULT_WIDTH = 300;
    
    /** The default height of the panel. */
    private static final int DEFAULT_HEIGHT = 600;
    
    /** The number of characters to strip from the string when height is ten. */
    private static final int TEN_STRIP = 39;
    
    /** The number of characters to strip from the string when height is ten. */
    private static final int TWENTY_STRIP = 64;
    
    /** The number of characters to strip from the string when height is ten. */
    private static final int THIRTY_STRIP = 89;
    
    /** The board for this panel. */
    private final Board myBoard;

    /** The number of rows in a grid.*/
    private final int myGridWidth;
    
    /** The number of columns in a grid. */
    private final int myGridHeight;
    
    /** The string representing a board. */
    private String myBoardString;
    
    /** A flag representing if a user wants to display a grid on the panel. */
    private boolean myGridFlag; 
    
    
    /** A flag to check if we have something from which we can draw. */
    private boolean myDrawFlag;
    
    
    /** How much to strip a string by. */
    private int myStringStrip;


    /**
     * Initialize the instance fields and pass on responsibility for 
     * component set up to other methods.
     * @param theGridWidth is the number of rows in the tetris grid.
     * @param theGridHeight is the number of columns in the tetris grid.
     */
    public PlayPanel(final int theGridWidth, final int theGridHeight) {
        
        super();
        myGridFlag = false;
        myDrawFlag = false;
        myGridWidth = theGridWidth;
        myGridHeight = theGridHeight;
        myBoard = new Board(myGridWidth, myGridHeight); 
        setup();
        
        

    }
    
    /**
     * Sets up the components of the panel.
     */
    private void setup() {
       
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
            
        setBackground(Color.DARK_GRAY);
        
        
        // Decide how many characters need to be striped from the begging of the 
        // board string based on the boards height.
        if (myGridHeight == 10) {
            
            myStringStrip = TEN_STRIP;
        
        } else if (myGridHeight == 20) {
            
            myStringStrip = TWENTY_STRIP;
        
        }  else { 
            
            myStringStrip = THIRTY_STRIP;
        }
        
        
    }

    /**
     * 
     * Returns the board that this panel uses.
     * 
     * @return the board.
     */
    public Board getBoard() {
        
        return myBoard;
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        
        super.paintComponent(theGraphics);
        final Graphics2D g2D = (Graphics2D) theGraphics;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fix the string out of range.
        if (myDrawFlag) {
        
            // Remove the unnecessary characters of the the board representation.
            myBoardString = myBoardString.substring(myStringStrip, myBoardString.length());
            
            
            int xCord = 0;
            int yCord = 0;
            
            for (final char ch: myBoardString.toCharArray()) {
                
                
                // If it is this character just move on to the next iteration.
                if (ch == '|') {
                    continue;
                }
                
                // If it is this character it is time to reset the x coordinate 
                // and increase the y coordinate, move on to the next iteration 
                // because we don't have anything to draw yet.
                if (ch == '\n') {
                    xCord = 0;
                    yCord += getHeight() / myGridHeight;
                    continue;
                }
                
                // Draw the grid on the board
                if (myGridFlag) {
                    
                    g2D.setColor(Color.GRAY);
                    final Rectangle2D rectangle = 
                                    new Rectangle2D.Double(xCord, yCord, 
                                                           getWidth() / (myGridWidth + 1), 
                                                           getHeight() / (myGridHeight + 1));
                    g2D.draw(rectangle);
                    
                }
                
                if (ch != ' ') {
                   
                    // This part is only reached if we have something to paint. 
                    // Paint the shape at the appropriate coordinates, with the
                    // appropriate size.
                    final Color color = getColor(ch); // Grab the color of the block.
                    final Rectangle2D rectangle = 
                                    new Rectangle2D.Double(xCord, yCord, 
                                                           getWidth() / (myGridWidth + 1), 
                                                           getHeight() / (myGridHeight + 1));
                    
                    g2D.setColor(color);
                    g2D.fill(rectangle);
                    g2D.setColor(Color.BLACK);
                    g2D.draw(rectangle);
                    
                }
                // increment the x coordinate to the next appropriate position.
                xCord += getWidth() / myGridWidth;
            }
        }
       
        myDrawFlag = false; // fix string out of range exception.

    }

    /**
     * 
     * Returns a different color based on what character was passed in.
     * 
     * @param theChar the character to be checked.
     * @return the color.
     */
    private Color getColor(final char theChar) {
        Color color = Color.BLACK;
        if (theChar == 'I') {
           
            color = Color.CYAN;
            
        } else if (theChar == 'J') {
            
            color = Color.BLUE;
            color = color.brighter();
            
        } else if (theChar == 'L') {
            
            color = Color.ORANGE;
            
        } else if (theChar == 'O') {
            
            color = Color.YELLOW;
            
        } else if (theChar == 'S') {
            
            color = Color.GREEN;
            
        } else if (theChar == 'T') {
            
            color = Color.MAGENTA;
            
        } else if (theChar == 'Z') {
            
            color = Color.RED;
            
        } 
        
        return color;
    }
    
    @Override
    public void update(final Observable theObservable, final Object theArgs) {
     
        // This panel only cares if it received a string, in this 
        // case a representation of the board.
        if (theArgs instanceof String) {
        
            myDrawFlag = true; // we have something from which to draw.
            myBoardString = myBoard.toString(); // Get the string representation of the board.
            
            repaint(); 
            
        }
        
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        
        if ("Grid".equals(theEvent.getPropertyName())) {
            
            myGridFlag = (boolean) theEvent.getNewValue();
        }

        
    }
    

}
