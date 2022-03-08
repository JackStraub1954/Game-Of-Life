package com.gmail.johnstraub1954.game_of_life.components;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_SIZE_PN;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;

import com.gmail.johnstraub1954.game_of_life.main.Parameters;

public class ControlsPanel extends JPanel
{
    /**  Generated serial version UID */
    private static final long serialVersionUID = 4696307862593824588L;
    /** Parameters singleton */
    private static final Parameters params          = Parameters.INSTANCE;
    /** minimum cell size */
    private static final int        cellSizeMin     = 0;
    /** maximum cell size (arbitrarily chosen) */
    private static final int        cellSizeMax     = 128;
    /** size of a step when manipulating cell-size spinner */
    private static final int        cellSizeStep    = 1;
    /** initial value for cell size */
    private int cellSize    = params.getGridCellSize();

    /** Identifies the cell-size field */
    private final JLabel                cellSizeLabel   = 
        new JLabel( "Cell Size" );
    /** spinner model for setting cell size */
    private final SpinnerNumberModel    cellSizeModel   =
        new SpinnerNumberModel( 
            cellSize, cellSizeMin, cellSizeMax, cellSizeStep
        );
    /** spinner for setting cell size */
    private final JSpinner              cellSizeSpinner =
        new JSpinner( cellSizeModel );
    
    /** toggle for "grid-on" property */
    private final JCheckBox showGridCheckBox            = 
        new JCheckBox( "Show Grid" ); 
    /** toggle for "keep centered" property */
    private final JCheckBox keepCenteredCheckBox        =
        new JCheckBox( "Keep Grid Centered" );
    /** button to launch "center grid" notification */
    private final JButton   centerGridButton            =
        new JButton( "Center Grid" );
    
    /** Layout adjustment: cell-size components are added to a sub-panel */
    private final JPanel    cellSizePanel               = new JPanel();
    /** Layout adjustment: check boxes are added to a sub-panel */
    private final JPanel    checkBoxPanel               = new JPanel();
    
    /**
     * Constructor.
     */
    public ControlsPanel()
    {
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        this.setAlignmentX( Component.CENTER_ALIGNMENT );
        
        Border  innerBorder     =
            BorderFactory.createLineBorder( Color.BLACK );
        Border  outerBorder     =
            BorderFactory.createEmptyBorder( 8, 4, 8, 4 );
        Border  border          =
            BorderFactory.createCompoundBorder( innerBorder, outerBorder );
//        this.setBorder( border );
        
        cellSizeLabel.setAlignmentX( Component.CENTER_ALIGNMENT );
        cellSizeLabel.setHorizontalAlignment( JLabel.CENTER );
        cellSizeSpinner.setAlignmentX( Component.CENTER_ALIGNMENT );
        cellSizeSpinner.setMaximumSize( 
            cellSizeSpinner.getPreferredSize() );
        cellSizeSpinner.addChangeListener( e -> tweakCellSize( e ) );
        
        cellSizePanel.setLayout(
            new BoxLayout( cellSizePanel, BoxLayout.X_AXIS ) );
        cellSizePanel.setAlignmentX( Component.CENTER_ALIGNMENT );
        cellSizePanel.add( cellSizeLabel );
        cellSizePanel.add( cellSizeSpinner );
        add( cellSizePanel );
        
        checkBoxPanel.setLayout( 
            new BoxLayout( checkBoxPanel, BoxLayout.Y_AXIS ));
        checkBoxPanel.setAlignmentX( Component.CENTER_ALIGNMENT );
        checkBoxPanel.add( showGridCheckBox );
        checkBoxPanel.add( keepCenteredCheckBox );
        showGridCheckBox.setSelected( params.isGridLineShow() );
        showGridCheckBox.addChangeListener( e -> tweakGridOn( e ) );
        keepCenteredCheckBox.
            addChangeListener( e -> tweakKeepGridCentered( e ) );
        add( checkBoxPanel );
        
        centerGridButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        centerGridButton.addActionListener( e -> params.centerGrid() );
        add( centerGridButton );
    }
    
    /**
     * Monitors state changes for the cell-size property.
     * Triggers property change event for GOLConstants.GRID_CELL_SIZE_PN.
     * Triggers a notification event for GOLConstants.ACTION_RESET_PN.
     * 
     * @param evt   event that encapsulates changes
     *              to the cell-size property
     */
    private void tweakCellSize( ChangeEvent evt )
    {
        Object  source  = evt.getSource();
        if ( !(source instanceof JSpinner) )
            return;
        Object  value   = ((JSpinner)source).getValue();
        if ( value instanceof Number )
        {
            int size    = ((Number)value).intValue();
            params.setGridCellSize( size );
            params.reset();
        }
    }
    
    /**
     * Monitors changes to the state of the show-grid property.
     * Triggers property change event for GOLConstants.GRID_LINE_SHOW_PN.
     * Triggers a notification event for GOLConstants.ACTION_RESET_PN.
     * 
     * @param evt   encapsulates the state of the grid-on control
     */
    private void tweakGridOn( ChangeEvent evt )
    {
        Object  src = evt.getSource();
        if ( !(src instanceof JToggleButton) )
            return;
        JToggleButton   button  = (JToggleButton)src;
        params.setGridLineShow( button.isSelected() );
        params.reset();
    }
    
    /**
     * Monitors state changes for the keep-grid-centered property.
     * Triggers property change event for GOLConstants.GRID_KEEP_CENTERED_PN.
     * Triggers a notification event for GOLConstants.ACTION_RESET_PN.
     * 
     * @param evt   event that encapsulates changes
     *              to the keep-grid-centered control
     */
    private void tweakKeepGridCentered( ChangeEvent evt )
    {
        Object  src = evt.getSource();
        if ( !(src instanceof JToggleButton) )
            return;
        JToggleButton   button  = (JToggleButton)src;
        params.setGridKeepCentered( button.isSelected() );
        params.reset();
    }
}