package app;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;

import com.gmail.johnstraub1954.game_of_life.main.Cell;
import com.gmail.johnstraub1954.game_of_life.main.GOLTimer;
import com.gmail.johnstraub1954.game_of_life.main.GridMap;
import com.gmail.johnstraub1954.game_of_life.main.Neighborhood;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;
import com.gmail.johnstraub1954.game_of_life.main.Utils;

public class LayoutExerciser
{
    private static final Parameters params  = Parameters.INSTANCE;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new LayoutExerciser().run() );

    }

    public void run()
    {
        JFrame      frame       = new JFrame( "Layout Exerciser" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel     contentPane = new JPanel();
        BoxLayout   layout      =
            new BoxLayout( contentPane, BoxLayout.Y_AXIS );
        contentPane.setLayout( layout );
        contentPane.setAlignmentX( Component.CENTER_ALIGNMENT );
        contentPane.add( new GeneratorPanel() );
        contentPane.add( new JSeparator() );
        contentPane.add( new ControlPanel() );
        frame.setContentPane( contentPane );
        
        frame.pack();
        frame.setVisible( true );
    }
    
    private class ControlPanel extends JPanel
    {
        private final JLabel    cellSizeLabel           = 
            new JLabel( "Cell Size" );
        private final JSpinner  cellSizeSpinner         =
            new JSpinner( new SpinnerNumberModel( 4, 1, 128, 1 ) );
        private final JCheckBox gridOnCheckBox          = 
            new JCheckBox( "Grid On" ); 
        private final JCheckBox keepCenteredCheckBox    =
            new JCheckBox( "Keep Grid Centered" );
        private final JButton   centerGridButton        =
            new JButton( "Center Grid" );
        private final JPanel    checkBoxPanel           = new JPanel();
        private final JPanel    cellSizePanel           = new JPanel();
        public ControlPanel()
        {
            setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
            this.setAlignmentX( Component.CENTER_ALIGNMENT );
            
            Border  border          =
                BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
            this.setBorder( border );
            
            cellSizeLabel.setAlignmentX( Component.CENTER_ALIGNMENT );
            cellSizeLabel.setHorizontalAlignment( JLabel.CENTER );
            cellSizeSpinner.setAlignmentX( Component.CENTER_ALIGNMENT );
            cellSizeSpinner.setMaximumSize( cellSizeSpinner.getPreferredSize() );
            centerGridButton.setAlignmentX( Component.CENTER_ALIGNMENT );
            
            cellSizePanel.setLayout( new BoxLayout( cellSizePanel, BoxLayout.X_AXIS ) );
            cellSizePanel.setAlignmentX( Component.CENTER_ALIGNMENT );
            cellSizePanel.add( cellSizeLabel );
            cellSizePanel.add( cellSizeSpinner );
            add( cellSizePanel );
            
            checkBoxPanel.setLayout( new BoxLayout( checkBoxPanel, BoxLayout.Y_AXIS ));
            checkBoxPanel.setAlignmentX( Component.CENTER_ALIGNMENT );
            checkBoxPanel.add( gridOnCheckBox );
            checkBoxPanel.add( keepCenteredCheckBox );
            gridOnCheckBox.addChangeListener( e -> tweakGridOn( e ) );
            centerGridButton.addActionListener( e -> params.centerGrid() );
            add( checkBoxPanel );
            add( centerGridButton );
        }
        
        private void tweakGridOn( ChangeEvent evt )
        {
            Object  src = evt.getSource();
            if ( !(src instanceof JToggleButton) )
                return;
            JToggleButton   button  = (JToggleButton)src;
            params.setGridLineShow( button.isSelected() );
        }
        
        private void tweakKeepGridCentered( ChangeEvent evt )
        {
            Object  src = evt.getSource();
            if ( !(src instanceof JToggleButton) )
                return;
            JToggleButton   button  = (JToggleButton)src;
            params.setGridKeepCentered( button.isSelected() );
        }
}
    
    private class GeneratorPanel extends JPanel 
    {
        private static final float      sliderUnits     = 1000;
        private static final int        millisPerSecond = 1000;
        
        private final Parameters        params          = Parameters.INSTANCE;
        private final GOLTimer          golTimer        = GOLTimer.INSTANCE;
        private final DecimalFormat     gpsFormatter    = 
            new DecimalFormat( "#0.00" );

        private final JButton       nextGenButton   =
            new JButton( "Next Generation" );
        private final JTextField    sliderFeedback  = new JTextField( 25 );
        private final JSlider       slider          = getSlider();
        private final JToggleButton animateToggle   =
            new JToggleButton( "Animate", false );
        
        private TimerTask       task                = null;
        
        public GeneratorPanel()
        {
            setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
            this.setAlignmentX( Component.CENTER_ALIGNMENT );
            
            Border  border          =
                BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
            this.setBorder( border );
            
            nextGenButton.setAlignmentX( Component.CENTER_ALIGNMENT );
            add( nextGenButton );
            
            sliderFeedback.setAlignmentX( Component.CENTER_ALIGNMENT );
            sliderFeedback.setHorizontalAlignment( JLabel.CENTER);
            sliderFeedback.setEditable( false );
            sliderFeedback.setMaximumSize( sliderFeedback.getPreferredSize() );

            sliderFeedback.setBorder( null );
            add( sliderFeedback );
            
            slider.setAlignmentX( Component.CENTER_ALIGNMENT );
            add( slider );
            
            animateToggle.setAlignmentX( Component.CENTER_ALIGNMENT );
            animateToggle.addChangeListener( e -> tweakAutoRegenOn( e ));
            add( animateToggle );
        }
        
        private void tweakAutoRegenOn( ChangeEvent evt )
        {
            Object  src = evt.getSource();
            if ( !(src instanceof JToggleButton ) )
                return;
            JToggleButton   toggle      = (JToggleButton)src;
            params.setAutoRegenerationOn( toggle.isSelected() );
        }
        
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

            if ( task != null )
            {
                task.cancel();
                task = null;
            }
            
            if ( params.isAutoRegenerationOn() )
            {
                long    interval    = 
                    (int)( 1 / genPerSec * millisPerSecond );
                task = golTimer
                    .addTask( interval, () -> Utils.INSTANCE.propagate() );
            }
        }
        
    }
    
    private class URLPanel extends JPanel
    {
        private final JLabel        enterURLLabel       = 
            new JLabel( "Enter URL or Drag/Drop" );
        private final JTextField    urlTextBox          = new JTextField( 25 );
        private final JButton       openFileButton      =
            new JButton( "Open File" );
        
        public URLPanel()
        {
            setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        }
    }
}
