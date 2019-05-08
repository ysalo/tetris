/*
 * TCSS 305 - Autumn 2016
 * Assignment 6 - Tetris
 */

package view;


import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import model.Board;


/** 
 * 
 * Creates the GUI for the Tetris application.
 * 
 * @author Yaro Salo
 * @version December 2, 2016
 */
public class TetrisGUI extends JFrame implements Observer {
    
    /** Generated serialization ID for this class. */
    private static final long serialVersionUID = -4763324919600478629L;

    /** Initial delay of one second. */
    private static final int DEFAULT_DELAY = 1000; 
    
    /** The number of rows to clear to move on to the next level. */
    private static final int MAX_ROWS = 5;
    
    /** The number of milliseconds to decrease the delay by when user levels up. */
    private static final int TIMER_DECREASE = 100; 
    
    /** Points for one line cleared. */
    private static final int ONE_LINE_P = 100; 
    
    /** Points for one line cleared. */
    private static final int TWO_LINE_P = 200; 
    
    /** Points for one line cleared. */
    private static final int THREE_LINE_P = 300;
    
    /** Represents one row. */
    private static final int ONE_LINE = 1; 
    
    /** Represents two row. */
    private static final int TWO_LINE = 2; 
    
    /** Represents three row to satisfy CheckStyle. */
    private static final int THREE_LINE = 3;

    /** The score multiplier when a player clears four rows. */
    private static final int FOUR_BONUS = 800;
    
    /** Bonus points awarded for leveling up. */
    private static final int LEVEL_BONUS = 200; 
    
    /** User option 5 by 10 grid size. */
    private static final String FIVE_BY_TEN = "5 X 10";
    
    /** User option 10 by 20 grid size. */
    private static final String TEN_BY_TWENTY = "10 X 20";
   
    /** User option 15 by 30 grid size. */
    private static final String FIFTEEN_BY_THIRTY = "15 X 30";
    
    /** Represents a string that is used to fire a property change when a score changes. */
    private static final String SCORE = "Score";
    
    /** Represents a string that is used to fire a property change when a lines is cleared. */
    private static final String LINE_CLEARED = "Cleared";
   
    /** Represents a string that is used to fire a property change when the level changes. */
    private static final String CUR_LEVEL = "Level";
   
    /** Number of lines to clear to get to the next level. */
    private static final int LINES_NEXT_LEVEL = 5;
    
    /** Represents a string that is used to fire a property change when the number 
     * of line until next level changes.. 
     */
    private static final String NEXT_LEVEL = "Next";
    
    
    /** Initial delay of the timer in milliseconds. */
    private int myDelay;
    
    /** The level the user is at. */
    private int myLevel;
    
    /** The number of lines until next level. */
    private int myNextLevel;

    /** The counter to see how many rows are cleared. */
    private int myRowCount;
    
    /** The total number of lines cleared. */
    private int myTotalLinesCleared;
    
    /** The score of the player. */
    private int myScore;
    

    
    /**
     * The panel on which the game is displayed.
     */
    private final PlayPanel myPlayPanel;
    
    /** Integer array storing the values for the grid width and height.*/
    private final int[] mySizeArr;
    /**
     * The timer for this GUI.
     */
    private final Timer myTimer;
    
    /**
     * The board.
     */
    private final Board myBoard;
    
    /**
     * Indicates if a game is over.
     */
    private boolean myGameOver;

      
    /**
     * 
     * The GUI constructor that passes the responsibility to the start() method.
     */
    public TetrisGUI() {
        
        super("Tetris");
        
        myTimer = new Timer(DEFAULT_DELAY, new TimerListener());
        mySizeArr = new int[2];
        userSize(); // Prompt the user to select a size of the grid.
        // Set the grid size to the users selection.
        myPlayPanel = new PlayPanel(mySizeArr[0], mySizeArr[1]);
        myBoard = myPlayPanel.getBoard();
        start(); // pass on responsibility for component creation.
        
    }
    
    /**
     * 
     * Sets up the components of this GUI's application.
     */
    private void start() {
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JMenuBar tetrisMenuBar = new TetrisMenuBar(this); 
        final InfoPanel previewPanel = new InfoPanel();
        
        setJMenuBar(tetrisMenuBar); 
        setIconImage(new ImageIcon("additional files/Tetris_Icon.png").getImage());
        // get the same board as the panel has.
        add(myPlayPanel, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.EAST);
                
        setResizable(false); // prevent the frame from resizing for now.
        setVisible(true);
        
        addKeyListener(new GameControls()); 
        
        myBoard.addObserver(this); // Add this frame as an observer of the board.
        myBoard.addObserver(myPlayPanel); // Add the play panel as an observer of the board.
        myBoard.addObserver(previewPanel); // Add the next piece preview as an observer.
        
        // register the panel as a listener for property changes from the menu bar.
        tetrisMenuBar.addPropertyChangeListener(myPlayPanel); 
        this.addPropertyChangeListener((PropertyChangeListener) tetrisMenuBar);
        this.addPropertyChangeListener(previewPanel);
        myBoard.newGame(); // Start a new game.
        
        pack();
        
        // Center the frame on the screen.
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(
            (int) (kit.getScreenSize().getWidth() / 2 - getWidth() / 2),
            (int) (kit.getScreenSize().getHeight() / 2 - getHeight() / 2));
    }

    
    /**
     * Gets the grid size the user wants.
     */
    private void userSize() {
        
        // Display a input dialog that gives the user 3 options.
        // Give the user three options only.
        // Store the grid size in an array based on the users choice.
        final String[] possibilities = {FIVE_BY_TEN, TEN_BY_TWENTY, FIFTEEN_BY_THIRTY};
        final String dialog = (String) JOptionPane.showInputDialog(this,
                            "Choose an option for the grid size:\n", null,
                            JOptionPane.PLAIN_MESSAGE, null, possibilities, FIVE_BY_TEN);

        if ((dialog != null) && (dialog.length() > 0)) {
            
            // The magic numbers represent the rows and columns on a grid, respectively.
            if (dialog.equals(FIVE_BY_TEN)) {
                mySizeArr[0] = 5;
                mySizeArr[1] = 10;
                
            
            } else if (dialog.equals(FIFTEEN_BY_THIRTY)) {
                
                mySizeArr[0] = 15;
                mySizeArr[1] = 30;
                
            } else { 
                
                mySizeArr[0] = 10;
                mySizeArr[1] = 20;   
            }
        
        } else {
            
            userSize(); // If no option is chosen prompt the user again.
            
        }
        
    }

    /** 
     * 
     * Starts a new game.
     */
    protected void newGame() {
        
        // Set the values for a new game.
        myBoard.newGame();
        myDelay = DEFAULT_DELAY;
        myTimer.setDelay(myDelay);
        myTimer.start();
        myGameOver = false;
        myLevel = 1;
        myNextLevel = LINES_NEXT_LEVEL;
        myScore = 0;
        myRowCount = 0;
        myTotalLinesCleared = 0;
        
        // Lets the information panel know that it's labels need to be reset/
        firePropertyChange(SCORE, null, 0);
        firePropertyChange(LINE_CLEARED, null, 0);
        firePropertyChange(CUR_LEVEL, null, 1);
        firePropertyChange(NEXT_LEVEL, null, LINES_NEXT_LEVEL);

        
    }
    
    /**
     * Pause the game.
     */
    protected void pauseGame() {
        
        myTimer.stop();
    }
    
    /**
     * Resumes the game.
     */
    protected void resumeGame() {
        
        myTimer.start();
    }
    
    /** 
     * Displays that a game is over. 
     */
    protected void endGameMessage() {
        
        JOptionPane.showMessageDialog(null, "Game Over", "Sorry", 
                          JOptionPane.PLAIN_MESSAGE, 
                          null); // Display an indication that the game is over.
        
    }

    @Override
    public void update(final Observable theObservable, final Object theArgs) {
      
        // If a boolean was received it's game over.
        if (theArgs instanceof Boolean) {
            
            myGameOver = (boolean) theArgs;
            
            // Notify listeners that the game is over.
            firePropertyChange("game over" , null, myGameOver);
            myTimer.stop(); // stop pieces from moving.
            endGameMessage();

        }
        
        // If and integer array was received then rows were cleared.
        if (theArgs instanceof Integer[]) {
            
            final Integer[] array = (Integer[]) theArgs;
            
            
            myRowCount += array.length; // The number of rows cleared.
            
            myTotalLinesCleared += array.length; // Add the rows cleared to a running sum.
            
            // The next level is reached when five rows are cleared.
            myNextLevel = LINES_NEXT_LEVEL - myRowCount;
            
            // Add a different number of points based on how many lines are cleared.
            if (array.length == ONE_LINE) {
                
                myScore += ONE_LINE_P;
            
            } else if (array.length == TWO_LINE) {
               
                myScore += TWO_LINE_P;
            
            } else if (array.length == THREE_LINE) {
               
                myScore += THREE_LINE_P;
            
            } else {
                
                myScore += FOUR_BONUS;
            }
            
            // Every time a user clears 5 rows their level goes up.
            if (myRowCount >= MAX_ROWS) {
                
                myLevel++;
                myNextLevel = LINES_NEXT_LEVEL;
                myScore += LEVEL_BONUS; // add a bonus to the score.
                
                // Decrease the number of rows cleared by 5 in case the user 
                // cleared more than 4 consecutive rows.
                myRowCount -= MAX_ROWS; 
                
                // Decrease the timer delay.
                // The lowest the delay can be is 100 milliseconds.
                if (myDelay - TIMER_DECREASE > 0) {
                    
                    myDelay -= TIMER_DECREASE;
                    myTimer.setDelay(myDelay);
                }    
            }
            
            // Let the information panel know the knew values.
            firePropertyChange(SCORE, null, myScore);
            firePropertyChange(LINE_CLEARED, null, myTotalLinesCleared);
            firePropertyChange(CUR_LEVEL, null, myLevel);
            firePropertyChange(NEXT_LEVEL, null, myNextLevel);

        }
    }
    
    
    /**
     * 
     * The listener for the timer.
     * 
     * @author Yaro Salo
     * @version December 5, 2016
     */
    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            
            myBoard.down();   
        }
           
    }
    
    /**
     * 
     * A key listener for this panel.
     * 
     * @author Yaro Salo
     * @version December 6, 2016
     */
    private class GameControls extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            
            // If the game is over ignore these key presses.
            if (!myGameOver && myTimer.isRunning()) {
                
                if (theEvent.getKeyCode() == KeyEvent.VK_UP) {
                    myBoard.rotate();
                }
                if (theEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    myBoard.down();
                }
                
                if (theEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    myBoard.left();
                }
                if (theEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    myBoard.right();
                }
                if (theEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    myBoard.drop();
                }

            }

        }

    }
}
