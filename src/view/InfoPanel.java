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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.MovableTetrisPiece;

/**
 * 
 * A panel to display the next piece preview.
 * 
 * @author Yaro Salo
 * @version December 3, 2016
 */
public class InfoPanel extends JPanel implements Observer, PropertyChangeListener  {

    /** Generated serialization if for this class. */
    private static final long serialVersionUID = 3457243357781833162L;
    
    /** Default size of this panel. */
    private static final int DEFAULT_SIZE = 180;
    
    /** Spacing between the information displayed. */
    private static final int STRUT = 30;
    
    /** Default size of the tetris piece. */
    private static final int PIECE_SIZE = 30;
    
    /** The coordinate if a tetris piece is an even length. */
    private static final int EVEN_C = 30;
    
    /** The coordinate if a tetris piece is an even length. */
    private static final int ODD_C = 45;
    
    /** The padding between the blocks to add a spacing effect. */
    private static final int SPACING = 3;
    
    /** Label to display the score.*/
    private final JLabel myScoreLabel;
    
    /** Label to display the score.*/
    private final JLabel myLinesCleared;

    /** Label to display the score.*/
    private final JLabel myCurrentLevel;
    
    /** Label to display the score.*/
    private final JLabel myNextLevel;


    /** The string representing a piece. */
    private String myPieceString;

    /** The movable tetris piece. */
    private MovableTetrisPiece myTetrisPiece;
    
    /**
     * 
     * Creates an instance of the preview panel.
     */
    public InfoPanel() {
    
        super();
        
        myScoreLabel = new JLabel(" Score: 0");
        myLinesCleared = new JLabel(" Lines Cleared: 0");
        myCurrentLevel = new JLabel(" Current Level: 1");
        myNextLevel = new JLabel(" Next Level: 5");

        setup();
        
    }
    
    /**
     * 
     *  Sets up the components.
     */
    private void setup() {
        
        setPreferredSize(new Dimension(DEFAULT_SIZE, DEFAULT_SIZE));
        setBackground(Color.GRAY);
        add(new JLabel("                Piece Preview"));       
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        
        
        final JPanel inforPanel = new JPanel();
        
        inforPanel.setBackground(Color.GRAY);
        inforPanel.setLayout(new BoxLayout(inforPanel, BoxLayout.PAGE_AXIS));
        
        inforPanel.add(Box.createVerticalStrut(STRUT));
        inforPanel.add(myScoreLabel);
        
        inforPanel.add(Box.createVerticalStrut(STRUT));
        inforPanel.add(myLinesCleared);
        
        inforPanel.add(Box.createVerticalStrut(STRUT));
        inforPanel.add(myCurrentLevel);
        
        inforPanel.add(Box.createVerticalStrut(STRUT));
        inforPanel.add(myNextLevel);
        
        add(Box.createVerticalStrut(DEFAULT_SIZE));
        add(inforPanel);
    }

    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        
        super.paintComponent(theGraphics);
        final Graphics2D g2D = (Graphics2D) theGraphics;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a white border around the preview panel.
        final Rectangle2D rectangle = new Rectangle2D.Double(0, 0, DEFAULT_SIZE - 1, 
                                                             DEFAULT_SIZE - 1);
        
        g2D.setColor(Color.DARK_GRAY);
        g2D.fill(rectangle);
        g2D.setColor(Color.LIGHT_GRAY);
        g2D.draw(rectangle);
        
        // Initialize the x and the y. 
        int xCord = EVEN_C;
        int yCord = EVEN_C;
        
        // parse the string that contains the next piece.
        for (final char ch: myPieceString.toCharArray()) {
            
            // If the new line character is hit it's time to update the y-coordinate.
            if (ch == '\n') {
                
                // To center the piece in the panel vertically:
                // A tetris piece can either have a height that is divisible by 2 
                // or not.
                // To center a piece if it is not divisible by 2 the y-coordinate 
                // needs to be increased by half the height of the piece. If this is done then 
                // we jump to the next iteration of the loop so that we do not 
                // change the coordinates or draw anything because there is not thing to draw.
                // To center the piece horizontally if it has width that is not divisible by 2
                // then we need to set the x coordinate with to a width and a half of one 
                // square.
                if (myTetrisPiece.getHeight() % 2 != 0) {
                    xCord = EVEN_C;
                    yCord += ODD_C;   
                    continue;
                    
                }
                
                if (myTetrisPiece.getWidth() % 2 != 0) {
                    xCord = ODD_C;
                    yCord += EVEN_C;
                    continue;
                    
                } 
                
                // This centers a piece if it's height and width is divisible by 2
                // no extra work needs to be done, reset the x coordinate and 
                // increment the y coordinate. Jump to the next iteration of the loop
                // so that we don't actually paint anything, because there is nothing to 
                // paint.
                xCord = EVEN_C;
                yCord += EVEN_C;
                continue;
            }
            
            // This will only be reached if the character is not a new line 
            // Check that it is also not an empty character.
            if (ch != ' ') {
                
                // Now we have something to draw and we know the appropriate coordinates 
                // where it need to be drawn.
                
                // -3 creates a spacing effect between blocks.
                final Rectangle2D piece = 
                                new Rectangle2D.Double(xCord, yCord, 
                                                       PIECE_SIZE - SPACING, 
                                                       PIECE_SIZE - SPACING);
                g2D.setColor(getColor(ch)); // Set the color based on what piece it is.
                g2D.fill(piece);
                g2D.setColor(Color.BLACK);
                g2D.draw(piece);
            }
            xCord += PIECE_SIZE; // increase the x coordinate to the next appropriate position.

        }
        
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
        
        // This panel only cares when it receives a movable tetris piece.
        if (theArgs instanceof MovableTetrisPiece) {
            
            // Get the string representation of the 
            // piece so that we can draw it on the panel.
            myPieceString =  theArgs.toString();
            
            // Get the tetris piece so that we can get it's height and width so
            // that it can be centered.
            myTetrisPiece = (MovableTetrisPiece) theArgs;
            
            repaint(); // repaint the panel.
        }
        
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        
        // Update the information labels.
        if ("Score".equals(theEvent.getPropertyName())) {
            myScoreLabel.setText(" Score: " + (int) theEvent.getNewValue());
            
        }
        
        if ("Cleared".equals(theEvent.getPropertyName())) {
            myLinesCleared.setText(" Lines Cleared: " + (int) theEvent.getNewValue());
            
        }
        
        if ("Level".equals(theEvent.getPropertyName())) {
            myCurrentLevel.setText(" Current Level: " + (int) theEvent.getNewValue());
            
        }
        
        if ("Next".equals(theEvent.getPropertyName())) {
            myNextLevel.setText(" Next Level: " + (int) theEvent.getNewValue());
            
        }
               
    }
}
