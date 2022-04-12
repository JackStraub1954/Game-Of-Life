package app;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.game_of_life.components.ControlsPanel;

public class ShowPanel
{
    private static final Component  panelToShow = 
        //new PatternPropertiesPanel( new ActionRegistrar() );
        //new GridPropertiesPanel( new ActionRegistrar() );
        new ControlsPanel();
//        new GeneratorPanel();
//        new URLPanel();
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new ShowPanel().run() );
    }
    
    public void run()
    {
        JFrame  frame   = new JFrame( "Panel Shower" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.getContentPane().add( panelToShow );
        frame.pack();
        frame.setVisible( true );
    }

}
