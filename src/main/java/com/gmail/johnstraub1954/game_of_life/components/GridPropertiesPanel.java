package com.gmail.johnstraub1954.game_of_life.components;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_SIZE_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_SHOW_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_WIDTH_PN;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

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

import com.gmail.johnstraub1954.game_of_life.main.ActionRegistrar;
import com.gmail.johnstraub1954.game_of_life.main.GOLConstants;
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
    
    /** minimum cell size */
    private static final int        cellSizeMin     = 0;
    /** maximum cell size (arbitrarily chosen) */
    private static final int        cellSizeMax     = 128;
    /** size of a step when manipulating cell-size spinner */
    private static final int        cellSizeStep    = 1;
    
    /** Object that notifies NotificationListeners of apply actions */
    private final ActionRegistrar   actionRegistrar;

    public GridPropertiesPanel( ActionRegistrar actionRegistrar)
    {
        super( new GridBagLayout() );
        this.actionRegistrar = actionRegistrar;
        
        Border  emptyBorder = BorderFactory
            .createEmptyBorder( margin, margin, margin, margin );
        Border  lineBorder  = BorderFactory
            .createLineBorder( Color.BLACK, 2 );
        Border  border      = BorderFactory
            .createCompoundBorder( lineBorder, emptyBorder );
        setBorder( border );

        GridBagConstraints  gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        SpinnerNumberPanel    spinnerPanel    = null;
        ColorPanel      colorPanel      = null;
        add( new ColorPanel( 
            "Grid Color",
            "GRID_COLOR_PN",
            GOLConstants.PREF_GRID_COLOR_CN,
            GOLConstants.PREF_GRID_COLOR_FB_CN,
            c -> params.setGridColor( c ),
            () -> params.getGridColor()
            ),
            gbc
        );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        colorPanel = new ColorPanel( 
            "Cell Color", 
            GRID_CELL_COLOR_PN,
            GOLConstants.PREF_CELL_COLOR_CN,
            GOLConstants.PREF_CELL_COLOR_FB_CN,
            c -> params.setGridCellColor( c ),
            () -> params.getGridCellColor() 
        );
        add( colorPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        colorPanel = new ColorPanel( 
            "Grid Line Color", 
            GRID_LINE_COLOR_PN,
            GOLConstants.PREF_LINE_COLOR_CN,
            GOLConstants.PREF_LINE_COLOR_FB_CN,
            c -> params.setGridLineColor( c ),
            () -> params.getGridLineColor() 
        );
        add( colorPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        spinnerPanel = new SpinnerNumberPanel(
            gridLineWidthMin,
            gridLineWidthMax,
            gridLineWidthStep,
            "Grid Line Width",
            GRID_LINE_WIDTH_PN,
            GOLConstants.PREF_LINE_WIDTH_CN,
            i -> params.setGridLineWidth( i ),
            () -> params.getGridLineWidth()
        );
        add( spinnerPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        spinnerPanel = new SpinnerNumberPanel( 
            cellSizeMin,
            cellSizeMax,
            cellSizeStep,
            "Cell Size",
            GRID_CELL_SIZE_PN,
            GOLConstants.PREF_CELL_SIZE_CN,
            i -> params.setGridCellSize( i ),
            () -> params.getGridCellSize()
        );
        add( spinnerPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        CheckBox    checkBox    = new CheckBox(
            "Show Grid",
            GRID_LINE_SHOW_PN,
            GOLConstants.PREF_SHOW_GRID_CN,
            b -> params.setGridLineShow( b ),
            () -> params.isGridLineShow()
        );
        checkBox.setSelected( params.isGridLineShow() );
        add( checkBox, gbc );
}

    private class ColorPanel 
        extends JPanel 
        implements ActionListener
    {
        private final JLabel            label;
        
        public ColorPanel( 
            String          text, 
            String          propertyName,
            String          buttonCN,
            String          feedbackCN,
            Consumer<Color> consumer,
            Supplier<Color> supplier
        )
        {
            super( new GridLayout( 1, 2, 2, 2 ) );
            label = new JLabel( " " );
            JButton button  = new JButton( text );
            button.setName( buttonCN );
            Border  border  = 
                BorderFactory.createLineBorder( Color.BLACK, 1 );
            label.setBackground( supplier.get() );
            label.setBorder( border );
            label.setOpaque( true );
            label.setName( feedbackCN );

            button.addActionListener( this );
            
            add( button );
            add( label );
            actionRegistrar.addNotificationListener(
                GOLConstants.ACTION_APPLY_PN,
                e -> consumer.accept(label.getBackground() )
            );
            
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
            }
        }
    }
    
    private class SpinnerNumberPanel 
        extends JPanel
    {
        private final IntConsumer   consumer;
        
        public SpinnerNumberPanel(
            int         min,
            int         max,
            int         step,
            String      text, 
            String      propertyName,
            String      spinnerName,
            IntConsumer consumer,
            IntSupplier supplier
        )
        {
            super( new GridLayout( 1, 2, 2, 2 ) );
            this.consumer = consumer;
            JLabel  label   = new JLabel( text );
            label.setHorizontalAlignment( JLabel.RIGHT );
            
            int val = supplier.getAsInt();
            SpinnerNumberModel  model   = 
                new SpinnerNumberModel( val, min, max, step );
            JSpinner    spinner = new JSpinner( model );
            spinner.setName( spinnerName );
            add( label );
            add( spinner );
            actionRegistrar.addNotificationListener(
                GOLConstants.ACTION_APPLY_PN,
                e -> consumer.accept( model.getNumber().intValue() )
            );
        }
    }
    
    private class CheckBox 
        extends JCheckBox
//        implements ChangeListener
    {
        private final Consumer<Boolean> consumer;
        
        public CheckBox( 
            String              text, 
            String              propertyName,
            String              checkBoxName,
            Consumer<Boolean>   consumer,
            Supplier<Boolean>   supplier
        )
        {
            super( text );
            this.consumer = consumer;
            setName( checkBoxName );
            setSelected( supplier.get() );
            addComponentListener( 
                new ComponentAdapter()
                {
                    @Override
                    public void componentShown( ComponentEvent evt )
                    {
                        System.out.println( "check box" );
                        JCheckBox   checkBox    = (JCheckBox)evt.getSource();
                        checkBox.setSelected( supplier.get() );
                    }
                }
            );
            actionRegistrar.addNotificationListener(
                GOLConstants.ACTION_APPLY_PN,
                e -> consumer.accept( isSelected() )
            );
//            params.addPropertyChangeListener(
//                propertyName, e -> setSelected( supplier.get() ) 
//            );
        }

//        @Override
//        public void stateChanged( ChangeEvent evt )
//        {
//            Object  source  = evt.getSource();
//            if ( !(source instanceof JCheckBox) )
//                return;
//            JCheckBox   checkBox    = (JCheckBox)source;
//            consumer.accept( checkBox.isSelected() );
//            params.reset();
//        }
    }
}
