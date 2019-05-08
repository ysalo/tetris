/*
 * TCSS 305 - Autumn 2016
 * Assignment 6 - Tetris
 */

package view;


import java.awt.EventQueue;


import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 
 * Starts the Tetris application.
 * 
 * @author Yaro Salo
 * @version December 2, 2016
 */
public final class TetrisMain {

    /**
     * 
     * Private constructor to prevent instantiation.
     */
    private TetrisMain() {
    
        throw new AssertionError("Prevent default constructor instantiation.");
    }

    /**
     * 
     * Sets the look and feel of this program.
     */
    private static void setLookAndFeel() {
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            
        } catch (final UnsupportedLookAndFeelException e) {
            System.out.println("UnsupportedLookAndFeelException");
        } catch (final ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
        } catch (final InstantiationException e) {
            System.out.println("InstantiationException");
        } catch (final IllegalAccessException e) {
            System.out.println("IllegalAccessException");
        }
        
    }
    
    /**
    *
    * The starting point for this application.
    * 
    * @param theArgs Command line arguments ignored in this application.
    */
    public static void main(final String[] theArgs) {

        EventQueue.invokeLater(new Runnable() {
           
            @Override
            public void run() {
                
                setLookAndFeel();
                new TetrisGUI();
            }
        });


    }
}
