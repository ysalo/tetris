/*
 * TCSS 305 - Autumn 2016
 * Assignment 6 - Tetris
 */

package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import sound.SoundPlayer;


/**
 * 
 * A menu bar for the tetris application.
 * 
 * @author Yaro Salo
 * @version December 2, 2016
 */
public class TetrisMenuBar extends JMenuBar implements PropertyChangeListener {

    /** Generated serialization ID for this class. */
    private static final long serialVersionUID = -8436264661459312329L;
    
    /** Grid string to satisfy CheckStyle. **/
    private static final String GRID_STR = "Grid";
    
    /** The name location of the tetris song file. */
    private static final String TETRIS_SONG = "additional files/Tetris_Song.wav";
    
    /** The tetris GUI. */
    private final TetrisGUI myGUI;
    
    /** The new game button. */
    private final JMenuItem myNewGameBtn;
    
    /** The pause game button. */
    private final JMenuItem myPauseGameBtn;
    
    /** The resume game button. */
    private final JMenuItem myResumeGameBtn;
    
    /** The end game button. */
    private final JMenuItem myEndGameBtn;

    /** A check box that is selected when a user wants to have a grid displayed. */
    private final JCheckBox myGridSelect;
    
    /** Sound player. */
    private final SoundPlayer mySoundPlayer;
    
    /** Indicate if a player muted the music. */
    private boolean myMute;
   
    
    
    /**
     * 
     * Instantiate the instance fields.
     * @param theGUI is the tetris GUI.
     */
    public TetrisMenuBar(final TetrisGUI theGUI) {
        
        super();
        mySoundPlayer = new SoundPlayer();
        mySoundPlayer.preLoad(TETRIS_SONG);
        myGUI = theGUI;
        myNewGameBtn = new JMenuItem("New Game");
        myPauseGameBtn = new JMenuItem("PauseGame");
        myResumeGameBtn = new JMenuItem("Resume Game");
        myEndGameBtn = new JMenuItem("End Game");
        myGridSelect  = new JCheckBox("Grid   ");
        myMute = false;
        
        setup();
    }
    
    /**
     * 
     * Call necessary functions to create the components of the JMenuBar.
     */
    private void setup() {
        
        // Add accelerators.
        myNewGameBtn.setAccelerator(KeyStroke.getKeyStroke
                                    (KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        myPauseGameBtn.setAccelerator(KeyStroke.getKeyStroke
                                    (KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        myResumeGameBtn.setAccelerator(KeyStroke.getKeyStroke
                                    (KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        
        myEndGameBtn.setAccelerator(KeyStroke.getKeyStroke
                                    (KeyEvent.VK_E, ActionEvent.CTRL_MASK));

        
        
//        try {
//            
//            final Clip clip = AudioSystem.getClip();
//            final AudioInputStream stream 
//            = AudioSystem.getAudioInputStream(new File("additional files/Tetris_Song.wav"));
//            clip.open(stream);
//            clip.loop(Clip.LOOP_CONTINUOUSLY);
//            
//        } catch (final Exception e) {
//            e.printStackTrace();
//        }
//                        
        
        // Create the menues.    
        createGameMenu();
        createOptionsMenu();
        createHelpMenu();

        
    }
    
    /**
     * 
     * Create the file menu.
     */
    private void createGameMenu() {
       
        final JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        
        // Initially only the new game button is enabled.
        myEndGameBtn.setEnabled(false);
        myPauseGameBtn.setEnabled(false);
        myResumeGameBtn.setEnabled(false);
        
        myNewGameBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                if (!myMute) {
                    mySoundPlayer.loop(TETRIS_SONG);
                    
                }
                // When a new button get clicked 
                // disable certain buttons and enable the others.
                myGUI.newGame();
                myNewGameBtn.setEnabled(false);
                
                myGridSelect.setEnabled(true);
                
                myPauseGameBtn.setEnabled(true);
                myEndGameBtn.setEnabled(true);
            }
        });
             
        myEndGameBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                mySoundPlayer.stopAll();
                // End the game and enable back the new game button.
                myGUI.pauseGame();
                myGUI.endGameMessage();
                
                myNewGameBtn.setEnabled(true);

                myPauseGameBtn.setEnabled(false);
                myResumeGameBtn.setEnabled(false);
                myEndGameBtn.setEnabled(false);
                

            }
        });
        
        myPauseGameBtn.addActionListener(new ActionListener() {
            
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                // Pause the game and give the user the ability to resume.
                myGUI.pauseGame();
                myPauseGameBtn.setEnabled(false);
                myResumeGameBtn.setEnabled(true);
                mySoundPlayer.pause(TETRIS_SONG);

                
            }
        });
             
        
        myResumeGameBtn.addActionListener(new ActionListener() {
            
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                myGUI.resumeGame();
                myResumeGameBtn.setEnabled(false);
                myPauseGameBtn.setEnabled(true); 
                //mySoundPlayer.loop(TETRIS_SONG);
                
            }
        });
         

        // Add everything to the menu bar.
        gameMenu.add(myNewGameBtn);
        gameMenu.addSeparator();
        gameMenu.add(myPauseGameBtn);
        gameMenu.addSeparator();
        gameMenu.add(myResumeGameBtn);
        gameMenu.addSeparator();
        gameMenu.add(myEndGameBtn);
        add(gameMenu);
        
    }
    

    /**
     * 
     * Create the options menu.
     */
    private void createOptionsMenu() {
        
        final JMenu optionsMenu = new JMenu("Options");
        
        
        optionsMenu.setMnemonic(KeyEvent.VK_O);
        
        
        
        myGridSelect.setEnabled(false);
      
        myGridSelect.addActionListener(new ActionListener() {
            
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                if (myGridSelect.isSelected()) {
                    
                    //Let the listeners know when a grid should be drawn.
                    firePropertyChange(GRID_STR, false, true);
                    
               
                } else {
                    // Let the listeners know when a grid shouldn't be draw.
                    firePropertyChange(GRID_STR, true, false);
                }
                
            }
        });
        
        // A mute button for the song.
        final JCheckBox playMusic = new JCheckBox("Mute");
        
        playMusic.addActionListener(new ActionListener() {
            
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                if (playMusic.isSelected()) {
                    
                    mySoundPlayer.stopAll(); // stop the music.
                    myMute = true;
                } else {
                    myMute = false;
                    mySoundPlayer.loop(TETRIS_SONG); //resume the song.
                }
                
            }
        });
       
        optionsMenu.add(myGridSelect);
        optionsMenu.add(playMusic);
        add(optionsMenu);
        
    }
    
    /**
     * 
     * Create the help menu.
     */
    private void createHelpMenu() {
        
        final JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        final JMenuItem howToPlay = new JMenuItem("How to Play...");
        
        howToPlay.setMnemonic(KeyEvent.VK_W);
        
        final JMenuItem credits = new JMenuItem("Credits...");
        credits.setMnemonic(KeyEvent.VK_C);
        
        howToPlay.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                final String message = new String("To Play the Game press: \n \n" 
                    + "\t The 'Up Arrow' to rotate a piece in the clockwise direction. \n" 
                    + "\t The 'Down Arrow' to move the piece down one block. \n" 
                    + "\t The 'Left Arrow' to move the piece to the left one block. \n" 
                    + "\t The 'Right Arrow' to move the piece to the right one block. \n" 
                    + "\t The 'SpaceBar' to hard drop a piece all the way down. \n"
                    + "\t The 'Ctrl + P' key to pause the game. \n"
                    + "\t The 'Ctrl + R' key to resume a pause game.");
                
                JOptionPane.showMessageDialog(null, message, "How to Play", 
                                  JOptionPane.PLAIN_MESSAGE, 
                                  null);
            }
        });

        
        
        
        final JMenuItem scoring = new JMenuItem("Scoring...");
        
        scoring.setMnemonic(KeyEvent.VK_S);
        
        scoring.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                // Display scoring information.
                final String message = new String("Point are awarded based on lines cleared."
                    + "\n \n"
                    + "\t 1 line cleared awards 100. \n"
                    + "\t 2 lines cleared awards 200.\n"  
                    + "\t 3 lines cleared awards 300.\n" 
                    + "\t 4 lines cleared awards a bonus of 800 points. \n"
                    + "\t \n A bonus of 200 points is awarded for every level cleared. \n"
                    + "\n Every time a new level is reached the pieces decent faster.");
                
                JOptionPane.showMessageDialog(null, message, "Scoring", 
                                  JOptionPane.PLAIN_MESSAGE, 
                                  null);
            }
        });

        credits.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                // Display scoring information.
                final String message = new String("Tetris Song: " 
                    + "©2006-2015 GamesThemeSongs.com A Jayzoo.com Site \n" 
                    + "http://gamethemesongs.com/Tetris_-_GameBoy_-_Type_A.html \n"
                    + "\n Tetris Icon: " 
                    + " at ICONFINDER 2016 " 
                    + "https://www.iconfinder.com/"
                );
                
                JOptionPane.showMessageDialog(null, message, "Credits", 
                                  JOptionPane.PLAIN_MESSAGE, 
                                  null);
            }
        });
        helpMenu.add(scoring);
        helpMenu.add(howToPlay);
        helpMenu.add(credits);
        add(helpMenu);
    }
    
  
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        
        // When the user loose the game reset the buttons back.
        if ("game over".equals(theEvent.getPropertyName())) {
            
            mySoundPlayer.stopAll(); // stop the music player.
            myNewGameBtn.setEnabled((boolean) theEvent.getNewValue());
            myPauseGameBtn.setEnabled(false);
            myResumeGameBtn.setEnabled(false);
            myEndGameBtn.setEnabled(false);
            myGridSelect.setEnabled(false);
            
        }
        
        
        
    }




}
