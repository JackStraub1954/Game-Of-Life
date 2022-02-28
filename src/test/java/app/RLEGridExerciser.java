package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.game_of_life.main.Cell;
import com.gmail.johnstraub1954.game_of_life.main.GOLException;
import com.gmail.johnstraub1954.game_of_life.main.Grid;
import com.gmail.johnstraub1954.game_of_life.main.GridMap;
import com.gmail.johnstraub1954.game_of_life.main.Neighborhood;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;
import com.gmail.johnstraub1954.game_of_life.main.RLEGridDecoder;
import com.gmail.johnstraub1954.game_of_life.main.RLEInput;

public class RLEGridExerciser
{
    private static String          name;
    private static String          author;
    private static Point           origin;
    private static List<Integer>   survivalStates;
    private static List<Integer>   birthStates;
    private static int             gameStates;
    private static GridMap          gridMap;
    
    private static final Parameters params  = Parameters.INSTANCE;
    private Grid    gridPanel;

    public RLEGridExerciser()
    {
    }

    public static void main(String[] args)
    {
        String      resourceName    = "GosperGlider.rle";
        ClassLoader loader          = RLEInput.class.getClassLoader();
        InputStream inStream        = 
            loader.getResourceAsStream( resourceName );
        if ( inStream == null )
        {
            String  message = 
                "Failed to load rle file: " + resourceName;
            throw new GOLException( message );
        }
        
        RLEInput        input           = new RLEInput( inStream );
        name            = input.getName();
        author          = input.getAuthor();
        origin          = input.getUpperLeft();
        survivalStates  = input.getSurvivalRules();
        birthStates     = input.getBirthRules();
        gameStates      = input.getGameStates();
        System.out.println( "name=" + name );
        System.out.println( "author=" + author );
        System.out.println( "origin=" + origin );
        System.out.println( "survivalStates=" + survivalStates );
        System.out.println( "birthStates=" + birthStates );
        System.out.println( "gameStates=" + gameStates );
        
        RLEGridDecoder      decoder = input.getGridDecoder();
        Iterator<Character> iter    = decoder.iterator();
        gridMap = new GridMap();
        
        int currX   = origin.x;
        int currY   = origin.y;
        while ( iter.hasNext() )
        {
            char    nextChar    = iter.next();
            if ( nextChar == '$' )
            {
                ++currY;
                currX = origin.x;
            }
            else
            {
                boolean state   = nextChar == 'o';
                Cell    cell    = new Cell( currX++, currY, state );
                gridMap.put( cell );
            }
        }
        SwingUtilities.invokeLater( () -> new RLEGridExerciser().run() );
    }

    public void run()
    {
        JFrame  frame   = new JFrame( "Show Grid" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        params.setGridMap(gridMap);
        gridPanel = new Grid();
        params.setGridCellOrigin( origin );
        for ( Cell cell : gridMap )
        {
            if ( cell.isAlive() )
                System.out.println( cell );
        }
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( new OuterPanel(), BorderLayout.CENTER );
        contentPane.add( new Controls(), BorderLayout.WEST );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    private class OuterPanel extends JPanel
    {
        private final Parameters    params  = Parameters.INSTANCE;
        
        private static final int    gridMargin  = 16;
        
        public OuterPanel()
        {
            //super( new BorderLayout() );

            gridPanel.setLayout( null );
            add( gridPanel );//, BorderLayout.CENTER );
            
            Dimension   prefSize    = new Dimension( 800, 800 );
            setPreferredSize( prefSize );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            int         cellSize    = params.getGridCellSize();
            int         lineWidth   = params.getGridLineWidth();
            int         width       = getWidth();
            int         height      = getHeight();
            
            int         childWidth  = width - 2 * gridMargin;
            childWidth = (childWidth / cellSize) * cellSize + lineWidth;
            int         childHeight = height - 2 * gridMargin;
            childHeight = (childHeight / cellSize) * cellSize + lineWidth;
            
            Dimension   childDim    = new Dimension( childWidth, childHeight );
            gridPanel.setPreferredSize( childDim );
            gridPanel.setLocation( gridMargin, gridMargin );
        }
    }
    
    private class Controls extends JPanel
    {
        public Controls()
        {
            setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
            JButton next    = new JButton( "Next Generation" );
            JButton run     = new JButton( "run" );
            add( next );
            add( run );
            next.addActionListener( e -> propagate() );
            run.addActionListener( e -> startAnimation() );
        }
        
        private void propagate()
        {
            List<Cell>      cellsToModify   = new ArrayList<>();
            Iterator<Cell>  cellIterator    = gridMap.iterator();
            while ( cellIterator.hasNext() )
            {
                Cell            cell    = cellIterator.next();
                Neighborhood    hood    = new Neighborhood( cell, gridMap );
                Cell            newCell = 
                    hood.getNextState( survivalStates, birthStates );
                if ( cell.isAlive() != newCell.isAlive() )
                    cellsToModify.add( newCell );
            }
            
            for ( Cell modifiedCell : cellsToModify )
                gridMap.put( modifiedCell );
            gridPanel.repaint();
        }
        
        private void startAnimation()
        {
            Thread  thread  = new Thread( () -> animate() );
            thread.start();
        }
        
        private void animate()
        {
            long    interval    = 64;
            while ( true )
            {
                try
                {
                    Thread.sleep( interval );
                    propagate();
                }
                catch ( InterruptedException exc )
                {
                    
                }
            }
        }
    }
}
