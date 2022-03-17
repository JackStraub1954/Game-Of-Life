package com.gmail.johnstraub1954.game_of_life.main;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_CLICKED_PN;

import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.game_of_life.components.GridFrame;

/**
 * This class contains the main method that boots the 
 * Game of Life GUI.
 * 
 * @author Jack Straub
 *
 */
public class Main
{
    /**
     * Boots the Game of Life GUI.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        GridFrame   frame   = new GridFrame();
        SwingUtilities.invokeLater( () -> frame.run() );
        Parameters.INSTANCE.addNotificationListener( 
            GRID_CELL_CLICKED_PN, 
            e -> {
                Cell cell = (Cell)e.getSource();
                cell.setAlive( !cell.isAlive() );
                System.out.println( cell );
                Parameters.INSTANCE.getGridMap().put( cell );
                Parameters.INSTANCE.reset();
            });
    }
}
