package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.game_of_life.main.Cell;
import com.gmail.johnstraub1954.game_of_life.main.Grid;
import com.gmail.johnstraub1954.game_of_life.main.GridMap;
import com.gmail.johnstraub1954.game_of_life.main.Neighborhood;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;

public class ShowGrid
{
    private static final List<Integer>  survivalStates  =
        new ArrayList<>( Arrays.asList( 2, 3 ) );
    private static final List<Integer>  birthStates  =
        new ArrayList<>( Arrays.asList( 3 ) );
    
    private final GridMap   gridMap;
    private final Grid      gridPanel;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new ShowGrid().run() );
    }
    
    public ShowGrid()
    {
        gridMap = new GridMap();
        gridPanel = new Grid( gridMap );
    }

    public void run()
    {
        int startx  = 11;
        int starty  = 21;
        int width   = 15;
        int height  = 20;
        int lastrow = starty + height;
        int lastcol = startx + width;
        
        for ( int col = startx, row = starty ; col <= lastcol ; ++col )
        {
            gridMap.put( col, row, true );
            gridMap.put( col, lastrow, true );
        }
        
        for ( int col = startx, row = starty ; row <= lastrow ; ++row )
        {
            gridMap.put( col, row, true );
            gridMap.put( lastcol, row, true );
        }
//        gridMap.put( new Point( 50, 51 ), true );
//        gridMap.put( new Point( 50, 52 ), true );
//        gridMap.put( new Point( 50, 53 ), true );
//        
//        gridMap.put( new Point( 60, 50 ), true );
//        gridMap.put( new Point( 61, 50 ), true );
//        gridMap.put( new Point( 62, 50 ), true );
//        gridMap.put( new Point( 59, 51 ), true );
//        gridMap.put( new Point( 60, 51 ), true );
//        gridMap.put( new Point( 61, 51 ), true );
        
        JFrame  frame   = new JFrame( "Show Grid" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
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
            add( next );
            next.addActionListener( e -> propagate() );
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
    }
}
