package com.gmail.johnstraub1954.game_of_life.components;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;

import com.gmail.johnstraub1954.game_of_life.main.CheckpointStack;
import com.gmail.johnstraub1954.game_of_life.main.GOLConstants;
import com.gmail.johnstraub1954.game_of_life.main.GOLTimer;
import com.gmail.johnstraub1954.game_of_life.main.GridMap;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;
import com.gmail.johnstraub1954.game_of_life.main.RLEInput;
import com.gmail.johnstraub1954.game_of_life.main.Utils;

/**
 * Encapsulates the components used to control generation propagation:
 * <ol>
 * <li>Pushbutton to single-step generation propagation</li>
 * <li>
 * Feedback area to display current "generations per second" propagation
 * </li>
 * <li>Slider to adjust "generations per second" propagation</li>
 * <li>Toggle button to enable/disable auto-propagation (animation)</li>
 * </ol>
 * 
 * Also in charge of managing timer tasks when animation is enabled.
 * <br>
 * 
 * <img
 *      style="margin: 2em;"
 *      src="doc-files/GeneratorPanel.png"
 *      alt="GeneratorPanel.png"
 * >
 * 
 * @author Jack Straub
 */
public class GeneratorPanel extends JPanel
{
    private static final float          sliderUnits     = 1000;
    private static final int            millisPerSecond = 1000;
    private static final DecimalFormat gpsFormatter     = 
        new DecimalFormat( "#0.00" );
    
    private final Parameters    params          = Parameters.INSTANCE;
    private final GOLTimer      golTimer        = GOLTimer.INSTANCE;

    private final JButton       nextGenButton   =
        new JButton( "Next Generation" );
    private final JButton       rewindButton    = new JButton( "Rewind" );
    private final JTextField    sliderFeedback  = new JTextField( 25 );
    private final JSlider       slider          = getSlider();
    private final JToggleButton animateToggle   =
        new JToggleButton( "Animate", false );
    
    private TimerTask       task                = null;
    
    /**
     * Constructor.
     */
    public GeneratorPanel()
    {
        Dimension   minFiller   = new Dimension( 1, 1 );
        Dimension   prefFiller  = new Dimension( 4, 4 );

        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        this.setAlignmentX( Component.CENTER_ALIGNMENT );
        
        nextGenButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        nextGenButton.addActionListener( e -> Utils.INSTANCE.propagate() );
        nextGenButton.setName( GOLConstants.GEN_NEXT_BUTTON_CN );
        add( nextGenButton );
        add( new HSeparator( 3, true ) );

        rewindButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        rewindButton.addActionListener( e -> rewind() );
        boolean rewindButtonStatus  = params.getGridLatestData() != null;
        params.addPropertyChangeListener( e -> tweakRewind( e ) );

        rewindButton.setEnabled( rewindButtonStatus );
        rewindButton.setName( GOLConstants.GEN_REWIND_BUTTON_CN );
        add( rewindButton );
        
        // a bit more space between the next-gen button and the 
        // feedback box
        add( new Box.Filler( minFiller, prefFiller, prefFiller ) );
        
        sliderFeedback.setAlignmentX( Component.CENTER_ALIGNMENT );
        sliderFeedback.setHorizontalAlignment( JLabel.CENTER);
        sliderFeedback.setEditable( false );
        sliderFeedback.setMaximumSize( sliderFeedback.getPreferredSize() );

        sliderFeedback.setBorder( null );
        sliderFeedback.setName( GOLConstants.GEN_FEEDBACK_COMP_CN );
        add( sliderFeedback );
        
        slider.setAlignmentX( Component.CENTER_ALIGNMENT );
        slider.setName( GOLConstants.GEN_ANIMATE_TOGGLE_CN );
        add( slider );

        // a bit more space between the slider and the 
        // animate button
        add( new Box.Filler( minFiller, prefFiller, prefFiller ) );
        
        animateToggle.setAlignmentX( Component.CENTER_ALIGNMENT );
        animateToggle.addChangeListener( e -> tweakAutoRegenOn( e ));
        add( animateToggle );
    }
    
    /**
     * Catch ChangeEvents for the Animate toggle.
     * Trigger a change event for property GOLConstants.AUTO_REGEN_ON_PN.
     * 
     * @param evt   event describing the changed state of the Animate toggle
     * 
     * @see Parameters#setAutoRegenerationOn(boolean)
     */
    private void tweakAutoRegenOn( ChangeEvent evt )
    {
        Object  src = evt.getSource();
        if ( !(src instanceof JToggleButton ) )
            return;
        JToggleButton   toggle      = (JToggleButton)src;
        params.setAutoRegenerationOn( toggle.isSelected() );
        tweakTask();
    }
    
    private void tweakTask()
    {
        if ( task != null )
        {
            task.cancel();
            task = null;
        }
        
        if ( params.isAutoRegenerationOn() )
        {
            float   genPerSec   = params.getAutoRegenerationPace();
            long    interval    = 
                (int)( 1 / genPerSec * millisPerSecond );
            task = golTimer
                .addTask( interval, () -> Utils.INSTANCE.propagate() );
        }
    }
    
    /**
     * Create and configure the slider used to set the
     * pace of animation (when enabled).
     * 
     * @return the newly created slider
     */
    private JSlider getSlider()
    {
        float   maxPace = params.getAutoRegenerationPaceMax();
        float   fVal    = params.getAutoRegenerationPace();
        int     iVal    = (int)((fVal / (maxPace)) * sliderUnits );
        JSlider slider  = new JSlider( 0, (int)sliderUnits );
        slider.addChangeListener( e -> adjustFeedback( e ) );
        slider.setValue( iVal );
        return slider;
    }
    
    private void adjustFeedback( ChangeEvent evt )
    {
        Object  src = evt.getSource();
        if ( !(src instanceof JSlider) )
            return;
        JSlider slider      = (JSlider)src;
        float   sliderUnits = slider.getMaximum();
        float   maxPace     = params.getAutoRegenerationPaceMax();
        float   percent     = slider.getValue() / sliderUnits;
        float   genPerSec   = percent * maxPace;
        String  text        = 
            gpsFormatter.format( genPerSec ) + " Gen / second";
        sliderFeedback.setText( text );
        params.setAutoRegenerationPace( genPerSec );
        tweakTask();
        params.reset();
    }
    
    private void tweakRewind(  PropertyChangeEvent evt )
    {
        Object      newValue        = evt.getNewValue();
        boolean     rewindStatus    =
            newValue == null || !(newValue instanceof RLEInput);
        rewindButton.setEnabled( rewindStatus );
    }
    
    /**
     * Resets the current pattern (if any) to its initial state.
     */
    private void rewind()
    {
        GridMap map = CheckpointStack.INSTANCE.rewind();
        params.setGridMap( map );
        params.reset();
        
        RLEInput    input   = params.getGridLatestData();
        if ( input != null )
            URLManager.open( input );
    }
}
