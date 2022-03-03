package com.gmail.johnstraub1954.game_of_life.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

public class GridFrame implements Runnable
{
    private final Parameters    params  = Parameters.INSTANCE;
    private final JFrame        frame;
    private List<Integer>       survivalStates  = null;
    private List<Integer>       birthStates     = null;

    public GridFrame()
    {
        frame = new JFrame( "Conway's Game of Life" );
    }

    public void run()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( new OuterPanel(), BorderLayout.CENTER );
        contentPane.add( new Controls(), BorderLayout.WEST );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
    }
    
    private void propagate()
    {
        GridMap         gridMap         = params.getGridMap();
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
        params.reset();
    }

    private class OuterPanel extends JPanel
    {
        private final Parameters    params  = Parameters.INSTANCE;
        private final Grid          gridPanel   = new Grid();
        
        private static final int    gridMargin  = 16;
        
        public OuterPanel()
        {
            gridPanel.setLayout( null );
            add( gridPanel );
            
            Dimension   prefSize    = new Dimension( 800, 800 );
            setPreferredSize( prefSize );
            setBackground( new Color( 0x00bfff ) );
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
        private final Parameters    parms       = Parameters.INSTANCE;
        private final JButton       next        = 
            new JButton( "Next Generation" );
        private final JCheckBox     center      = 
            new JCheckBox( "Center Cells" );
        private final JTextField    urlField    = new JTextField( 25 );
        public Controls()
        {
            Dimension   minFiller   = new Dimension( 1, 1 );
            Dimension   prefFiller  = new Dimension( 16, 16 );
            survivalStates = parms.getSurvivalStates();
            birthStates = parms.getBirthStates();
            
            setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
            this.setAlignmentX( Component.CENTER_ALIGNMENT );
            add( new Box.Filler( minFiller, prefFiller, prefFiller ) );
            add( next );
//            next.setAlignmentX( Component.CENTER_ALIGNMENT );
            add( new Box.Filler( minFiller, prefFiller, prefFiller ) );

            next.addActionListener( e -> propagate() );
            
            add( new AnimationPanel() );
            params.addPropertyChangeListener(
                GOLConstants.AUTO_REGEN_ON_PN, 
                e -> next.setEnabled( !(boolean)e.getNewValue() )
            );
            
            add( new Box.Filler( minFiller, prefFiller, prefFiller ) );
            add( center );
            center.addChangeListener( e -> centerGrid()  );
            
            JPanel  dragDropPanel   = new JPanel();
            BoxLayout   dragAndDropLayout   =
                new BoxLayout( dragDropPanel, BoxLayout.LINE_AXIS );
            dragDropPanel.setLayout( dragAndDropLayout );
            JLabel  urlLabel    = new JLabel( "Enter URL or Drag/Drop" );
            urlLabel.setMaximumSize( urlLabel.getPreferredSize() );
            urlField.setMaximumSize( urlField.getPreferredSize() );
            this.add( urlLabel );
            this.add( urlField );
            urlLabel.setAlignmentX( Component.CENTER_ALIGNMENT );
            urlLabel.setHorizontalAlignment( JLabel.CENTER );
            urlField.setAlignmentX( Component.CENTER_ALIGNMENT );
//            add( dragDropPanel );
            
            new URLManager( urlField );
        }
        
        private void centerGrid()
        {
            params.setGridCenter( center.isSelected() ); 
            params.reset();
        }
    }
    
    private class AnimationPanel extends JPanel
    {
        private static final float  sliderUnits     = 1000;
        private static final int    millisPerSecond = 1000;
        
        private final Parameters    params          = Parameters.INSTANCE;
        private final GOLTimer      golTimer        = GOLTimer.INSTANCE;
        private final AnimationMgr  animationMgr    = new AnimationMgr();
        
        private final JSlider       slider;
        private final JTextField    feedback;
        private final JToggleButton animateToggle;
        
        private float   fMax;
        
        public AnimationPanel()
        {
            Dimension   minFiller   = new Dimension( 1, 1 );
            Dimension   prefFiller  = new Dimension( 4, 4 );

            setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
            fMax    = params.getAutoRegenerationPaceMax();
            float   fVal    = params.getAutoRegenerationPace();
            int     iVal    = (int)((fVal / (fMax)) * sliderUnits );
            slider = new JSlider( 0, (int)sliderUnits );
            String  str = String.format( "%.2f Gen / second", fVal );
            feedback = new JTextField( 22 );
            feedback.setBorder( null );
            
            // For some reason, BoxLayout stretches JTextField 
            // to fit available space (not a problem, for example,
            // with JLabel). To fix JText field, set its maximum
            // dimensions.
            feedback.setMaximumSize( feedback.getPreferredSize() );
            feedback.setEditable( false );
            feedback.setHorizontalAlignment( JTextField.RIGHT );
            feedback.setAlignmentX( Component.CENTER_ALIGNMENT );
            feedback.setHorizontalAlignment( JLabel.HORIZONTAL );
            slider.addChangeListener( e -> animationMgr.adjustTimer() );
            slider.setAlignmentX( Component.CENTER_ALIGNMENT );
            
            add( feedback );
            add( slider );
            
            animateToggle = new JToggleButton( "Animate", false );
            animateToggle.addActionListener( e -> animationMgr.adjustTimer() );
            add( new Box.Filler( minFiller, prefFiller, prefFiller ) );
            add( animateToggle );
            animateToggle.setAlignmentX( Component.CENTER_ALIGNMENT );
            slider.setValue( iVal );
            
            // ctor( startValue, min, max, stepSize
            int                 cellSize        = params.getGridCellSize();
            SpinnerNumberModel  cellSizeModel   = 
                new SpinnerNumberModel( cellSize, 1, 128, 1 );
            JSpinner            cellSizeSpinner =
                new JSpinner( cellSizeModel );
            JPanel  cellSizePanel   = new JPanel( new GridLayout( 2, 1 ) );            
            cellSizePanel.add( new JLabel( "Cell Size" ) );
            cellSizePanel.add( cellSizeSpinner );
            cellSizePanel.setMaximumSize( cellSizePanel.getPreferredSize() );
            cellSizeSpinner.addChangeListener( e -> changeCellSize( e ) );
            cellSizeSpinner.setAlignmentX( Component.CENTER_ALIGNMENT );
            add( cellSizePanel );
            
            boolean     isGridOn    = params.isGridLineShow();
            JCheckBox   gridOn      = new JCheckBox( "Grid On", isGridOn );
            gridOn.addChangeListener( e -> changeGridOn( gridOn.isSelected() ) );
            gridOn.setAlignmentX( Component.CENTER_ALIGNMENT );
            add( gridOn );
            
            params.addPropertyChangeListener(str, null);
        }
        
        private void changeGridOn( boolean isOn )
        {
            params.setGridLineShow( isOn );
            params.reset();
        }
        
        private void changeCellSize( ChangeEvent evt )
        {
            Object  source  = evt.getSource();
            if ( source instanceof JSpinner )
            {
                Object  value   = ((JSpinner)source).getValue();
                if ( value instanceof Number )
                {
                    int size    = ((Number)value).intValue();
                    params.setGridCellSize( size );
                    params.reset();
                }
            }
        }
        
        private class AnimationMgr
        {
            private TimerTask       task            = null;
            private DecimalFormat   gpsFormatter    = 
                new DecimalFormat( "#0.00" );
            
            public void adjustTimer()
            {
                float   percent     = slider.getValue() / sliderUnits;
                float   genPerSec   = percent * fMax;
                String  text        = 
                    gpsFormatter.format( genPerSec ) + " Gen / second";
                feedback.setText( text );
                if ( task != null )
                {
                    task.cancel();
                    task = null;
                }
                
                boolean selected    = animateToggle.isSelected();
                params.setAutoRegenerationOn( selected );
                if ( selected )
                {
                    long    interval    = 
                        (int)( 1 / genPerSec * millisPerSecond );
                    task = golTimer.addTask(interval, () -> propagate() );
                }
            }
        }
    }
}
