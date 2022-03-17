package com.gmail.johnstraub1954.game_of_life.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gmail.johnstraub1954.game_of_life.main.Parameters;

public class GridPropertiesPanel extends JPanel
{
    /** Parameter collector for this application */
    private static final Parameters params  = Parameters.INSTANCE;
    
    /** for creating empty border around this panel */
    private static final int        margin              = 5;
    
    /** minimum grid line width */
    private static final int        gridLineWidthMin     = 0;
    /** maximum grid line width (arbitrarily chosen) */
    private static final int        gridLineWidthMax     = 128;
    /** size of a step when manipulating cell-size spinner */
    private static final int        gridLineWidthStep    = 1;
    /** initial value for grid line width */
    private final int   gridLineWidth   = params.getGridLineWidth();
    
    /** minimum cell size */
    private static final int        cellSizeMin     = 0;
    /** maximum cell size (arbitrarily chosen) */
    private static final int        cellSizeMax     = 128;
    /** size of a step when manipulating cell-size spinner */
    private static final int        cellSizeStep    = 1;
    /** initial value for cell size */
    private final int cellSize  = params.getGridCellSize();
    
    /** spinner model for setting cell size */
    private final SpinnerNumberModel    cellSizeModel   =
        new SpinnerNumberModel( 
            cellSize, cellSizeMin, cellSizeMax, cellSizeStep
        );
    /** spinner for setting cell size */
    private final JSpinner              cellSizeSpinner =
        new JSpinner( cellSizeModel );
    
    /** spinner model for setting cell size */
    private final SpinnerNumberModel    gridLineWidthModel   =
        new SpinnerNumberModel( 
            gridLineWidth, 
            gridLineWidthMin,
            gridLineWidthMax,
            gridLineWidthStep
        );
    /** spinner for setting cell size */
    private final JSpinner              gridLineWidthSpinner =
        new JSpinner( gridLineWidthModel );

    public GridPropertiesPanel()
    {
        super( new GridBagLayout() );
        Border  border  = BorderFactory
            .createEmptyBorder( margin, margin, margin, margin );
        setBorder( border );
        
        GridBagConstraints  gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        SpinnerPanel    spinnerPanel    = null;
        ColorPanel      colorPanel      = null;
        add( new ColorPanel( 
            "Grid Color",
            params.getGridColor(),
            c -> params.setGridColor( c )
            ),
            gbc
        );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        colorPanel = new ColorPanel( 
            "Cell Color", 
            params.getGridColor(),
            c -> params.setGridCellColor( c ) 
        );
        add( colorPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        colorPanel = new ColorPanel( 
            "Grid Line Color", 
            params.getGridLineColor(),
            c -> params.setGridLineColor( c ) 
        );
        add( colorPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        spinnerPanel = new SpinnerPanel(
            gridLineWidthSpinner,
            "Grid Line Width",
            i -> params.setGridLineWidth( i )
        );
        add( spinnerPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        spinnerPanel = new SpinnerPanel( 
            cellSizeSpinner, 
            "Cell Size",
            i -> params.setGridCellSize( i )
        );
        add( spinnerPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        CheckBox    checkBox    = 
            new CheckBox( "Show Grid", b -> params.setGridLineShow( b ) );
        checkBox.setSelected( params.isGridLineShow() );
        add( checkBox, gbc );
    }

    private static class ColorPanel 
        extends JPanel 
        implements ActionListener
    {
        private final JLabel            label;
        private final Consumer<Color>   consumer;
        
        public ColorPanel( 
            String  text, 
            Color   color,
            Consumer<Color> consumer
        )
        {
            super( new GridLayout( 1, 2, 2, 2 ) );
            this.consumer = consumer;
            label = new JLabel( " " );
            JButton button  = new JButton( text );
            Border  border  = 
                BorderFactory.createLineBorder( Color.BLACK, 1 );
            label.setBackground( color );
            label.setBorder( border );
            label.setOpaque( true );

            button.addActionListener( this );
            
            add( button );
            add( label );
        }
        
        /**
         * Allow operator to choose a new color for some property.
         * 
         * @param evt   event object associated with this event
         */
        @Override
        public void actionPerformed( ActionEvent evt )
        {
            Object  source      = evt.getSource();
            if ( !(source instanceof JButton) )
                return;
            JButton button      = (JButton)source;
            String  title       = button.getText();
            Color   oldColor    = label.getBackground();
            Color   color   = 
                JColorChooser.showDialog( this, title, oldColor );
            if ( color != null )
            {
                label.setBackground( color );
                consumer.accept( color );
                params.reset();
            }
        }
    }
    
    private static class SpinnerPanel 
        extends JPanel
        implements ChangeListener
    {
        private final IntConsumer   consumer;
        
        public SpinnerPanel(
            JSpinner    spinner, 
            String      text, 
            IntConsumer consumer
        )
        {
            super( new GridLayout( 1, 2, 2, 2 ) );
            this.consumer = consumer;
            JLabel  label   = new JLabel( text );
            label.setHorizontalAlignment( JLabel.RIGHT );
            spinner.addChangeListener( this );
            add( label );
            add( spinner );
        }
        
        @Override
        public void stateChanged( ChangeEvent evt )
        {
            Object  source  = evt.getSource();
            if ( !( source instanceof JSpinner) )
                return;
            
            JSpinner            spinner     = (JSpinner)source;
            SpinnerModel        model       = spinner.getModel();
            if ( !(model instanceof SpinnerNumberModel) )
                return;
            
            SpinnerNumberModel  numberModel = (SpinnerNumberModel)model;
            int             val             = (int)numberModel.getNumber();
            consumer.accept( val );
            params.reset();
        }
    }
    
    private static class CheckBox 
        extends JCheckBox
        implements ChangeListener
    {
        private final Consumer<Boolean> consumer;
        
        public CheckBox( String text, Consumer<Boolean> consumer )
        {
            super( text );
            this.consumer = consumer;
            addChangeListener( this );
        }

        @Override
        public void stateChanged( ChangeEvent evt )
        {
            Object  source  = evt.getSource();
            if ( !(source instanceof JCheckBox) )
                return;
            JCheckBox   checkBox    = (JCheckBox)source;
            consumer.accept( checkBox.isSelected() );
            params.reset();
        }
    }
}
