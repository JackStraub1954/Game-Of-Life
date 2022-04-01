package com.gmail.johnstraub1954.game_of_life.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.function.BiPredicate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.gmail.johnstraub1954.game_of_life.main.GOLConstants;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;

import test_utils.ComponentLocator;
import test_utils.ComponentUtils;
import test_utils.TestUtils;

class GridPropertiesPanelTest
{
    /** Central parameter store */
    private static final Parameters params  = Parameters.INSTANCE;
    
    /** 
     * How long to wait for the color chooser to appear before 
     * declaring a test failure
     */
    private static final long   waitForChooserTimeout   = 1000;
    /** 
     * How long to wait after pushing the down arrow key
     * in the color chooser
     */
    private static final long   waitForChooserDown      = 0;
    /** How long to wait after dismissing the color chooser */
    private static final long   waitForChooserAfter     = 250;
    /** Predicate to establish color chooser is instantiated and visible */
    private static final BiPredicate<Component,Object> chooserPred = 
        (c,o) -> (c instanceof JColorChooser) && c.isVisible();
        
    /** 
     * How long to wait after select a dialog action button
     * (OK, Apply, Cancel)
     */
    private static final long   waitAfterDialogAction   = 250;
    
    /** The swatch to select from the color chooser for the grid color */
    private static final int    gridColorSwatchCount    = 1;
    /** The swatch to select from the color chooser for the grid cell color */
    private static final int    cellColorSwatchCount    = 
        gridColorSwatchCount + 1;
    /** The swatch to select from the color chooser for the grid line color */
    private static final int    lineColorSwatchCount    = 
        cellColorSwatchCount + 3;
    
    private static Robot        robbie;
    private PreferencesDialog   prefDlg;
    private JButton             applyButton;
    private JButton             okButton;
    private JButton             cancelButton;
    
    private JButton             gridColorButton;
    private JButton             cellColorButton;
    private JButton             lineColorButton;
    
    private JLabel              gridColorFeedback;          
    private JLabel              cellColorFeedback;          
    private JLabel              lineColorFeedback;
    
    private JSpinner            lineWidthSpinner;
    private SpinnerNumberModel  lineWidthModel;
    private JSpinner            cellSizeSpinner;
    private SpinnerNumberModel  cellSizeModel;
    
    private JCheckBox           showGridCheckBox;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        try
        {
            robbie = new Robot();
        }
        catch ( AWTException exc )
        {
            String  message = "Instantiate robot failure";
            fail( message, exc );
        }
    }

    @BeforeEach
    void setUp() throws Exception
    {
        prefDlg = new PreferencesDialog();
        showDialog();
        
        applyButton = 
            ComponentUtils.getJButton( GOLConstants.BP_APPLY_BUTTON_CN );
        cancelButton = 
            ComponentUtils.getJButton( GOLConstants.BP_CANCEL_BUTTON_CN );
        okButton = 
            ComponentUtils.getJButton( GOLConstants.BP_OK_BUTTON_CN );
        gridColorButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_GRID_COLOR_CN );
        cellColorButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_CELL_COLOR_CN );
        lineColorButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_LINE_COLOR_CN );

        gridColorFeedback = 
            ComponentUtils.getJLabel( GOLConstants.PREF_GRID_COLOR_FB_CN );
        cellColorFeedback = 
            ComponentUtils.getJLabel( GOLConstants.PREF_CELL_COLOR_FB_CN );
        lineColorFeedback = 
            ComponentUtils.getJLabel( GOLConstants.PREF_LINE_COLOR_FB_CN );

        lineWidthSpinner = 
            ComponentUtils.getJSpinner( GOLConstants.PREF_LINE_WIDTH_CN );
        lineWidthModel = (SpinnerNumberModel)lineWidthSpinner.getModel();
        cellSizeSpinner = 
            ComponentUtils.getJSpinner( GOLConstants.PREF_CELL_SIZE_CN );
        cellSizeModel = (SpinnerNumberModel)cellSizeSpinner.getModel();
        showGridCheckBox = 
            ComponentUtils.getJCheckBox( GOLConstants.PREF_SHOW_GRID_CN );
    }
    
    @AfterEach
    public void afterEach()
    {
        if ( prefDlg.isVisible() )
            prefDlg.setVisible( false );
        prefDlg.dispose();
    }

    @Test
    void testSelectGridColor()
    {
        testSelectColor( 
            gridColorButton, 
            gridColorFeedback, 
            gridColorSwatchCount,
            () -> params.getGridColor()
        );
    }

    @Test
    void testSelectCellColor()
    {
        testSelectColor( 
            cellColorButton, 
            cellColorFeedback, 
            cellColorSwatchCount,
            () -> params.getGridCellColor()
        );
    }

    @Test
    void testSelectLineColor()
    {
        testSelectColor( 
            lineColorButton, 
            lineColorFeedback, 
            lineColorSwatchCount,
            () -> params.getGridLineColor()
        );
    }
    
    @Test
    public void testSelectLineWidth()
    {
        SpinnerNumberModel  model   = 
            (SpinnerNumberModel)lineWidthSpinner.getModel();
        testSelectNumber( model, () -> params.getGridLineWidth() );
    }
    
    @Test
    public void testSelectCellSize()
    {
        SpinnerNumberModel  model   = 
            (SpinnerNumberModel)cellSizeSpinner.getModel();
        testSelectNumber( model, () -> params.getGridCellSize() );
    }
    
    @Test
    public void testShowGrid()
    {
        boolean oldVal  = params.isGridLineShow();
        boolean newVal  = !oldVal;
        showGridCheckBox.setSelected( newVal );
        assertEquals( oldVal, params.isGridLineShow() );
        apply();
        assertEquals( newVal, params.isGridLineShow() );
    }
    
    @Test
    public void testColorChooserCanceled()
    {
        // Get the original color
        Color   origColor   = gridColorFeedback.getBackground();
        
        // start the color chooser from a separate thread
        new Thread( ()-> gridColorButton.doClick() ).start();
        
        // wait for the color chooser to become visible
        waitForChooser();
        
        // cancel chooser
        robbie.keyPress( KeyEvent.VK_ESCAPE );
        
        // give chooser thread a chance to settle down
        TestUtils.pause( waitAfterDialogAction );
        
        // verify that, since the chooser was canceled,
        // the color isn't changed
        assertEquals( origColor, gridColorFeedback.getBackground() );
    }
    
    /**
     * Verify property values are updated when the parent dialog's
     * OK button is pressed.
     */
    @Test
    public void testOKAction()
    {
        Color   gridColorOrig       = params.getGridColor();
        Color   gridColorNew        = getDifferentColor( gridColorOrig );
        assertNotEquals( gridColorNew, gridColorFeedback.getBackground() );
        gridColorFeedback.setBackground( gridColorNew );

        Color   gridCellColorOrig   = params.getGridCellColor();
        Color   gridCellColorNew    = getDifferentColor( gridCellColorOrig );
        assertNotEquals( gridCellColorNew, cellColorFeedback.getBackground() );
        cellColorFeedback.setBackground( gridCellColorNew );

        Color   gridLineColorOrig   = params.getGridLineColor();
        Color   gridLineColorNew    = getDifferentColor( gridLineColorOrig );
        assertNotEquals( gridLineColorNew, lineColorFeedback.getBackground() );
        lineColorFeedback.setBackground( gridLineColorNew );
        
        int     lineWidthOrig       = params.getGridLineWidth();
        int     lineWidthNew        = 
            getDifferentNumber( lineWidthModel ).intValue();
        assertNotEquals( lineWidthOrig, lineWidthNew );
        lineWidthModel.setValue( lineWidthNew );

        int     cellSizeOrig        = params.getGridCellSize();
        int     cellSizeNew         = 
            getDifferentNumber( cellSizeModel ).intValue();
        assertNotEquals( cellSizeOrig, cellSizeNew );
        cellSizeModel.setValue( cellSizeNew );
        
        boolean showGridOrig        = params.isGridLineShow();
        boolean showGridNew         = !showGridOrig;
        assertNotEquals( showGridNew, showGridCheckBox.isSelected() );
        showGridCheckBox.setSelected( showGridNew );
        
        okButton.doClick();
        // give dialog thread a chance to settle
        TestUtils.pause( waitAfterDialogAction );

        assertEquals( gridColorNew, params.getGridColor() );
        assertEquals( gridCellColorNew, params.getGridCellColor() );
        assertEquals( gridLineColorNew, params.getGridLineColor() );
        
        assertEquals( lineWidthNew, params.getGridLineWidth() );
        assertEquals( cellSizeNew, params.getGridCellSize() );
        
        assertEquals( showGridNew, params.isGridLineShow() );
    }
    
    /**
     * Verify property values are NOT updated when the parent dialog's
     * Cancel button is pressed.
     */
    @ParameterizedTest
    @ValueSource( booleans= {true, false} )
    public void testOKCancelActions( boolean param )
    {
        Color   gridColorOrig       = params.getGridColor();
        Color   gridColorNew        = getDifferentColor( gridColorOrig );
        assertNotEquals( gridColorNew, gridColorFeedback.getBackground() );
        gridColorFeedback.setBackground( gridColorNew );

        Color   gridCellColorOrig   = params.getGridCellColor();
        Color   gridCellColorNew    = getDifferentColor( gridCellColorOrig );
        assertNotEquals( gridCellColorNew, cellColorFeedback.getBackground() );
        cellColorFeedback.setBackground( gridCellColorNew );

        Color   gridLineColorOrig   = params.getGridLineColor();
        Color   gridLineColorNew    = getDifferentColor( gridLineColorOrig );
        assertNotEquals( gridLineColorNew, lineColorFeedback.getBackground() );
        lineColorFeedback.setBackground( gridLineColorNew );
        
        int     lineWidthOrig       = params.getGridLineWidth();
        int     lineWidthNew        = 
            getDifferentNumber( lineWidthModel ).intValue();
        assertNotEquals( lineWidthOrig, lineWidthNew );
        lineWidthModel.setValue( lineWidthNew );

        int     cellSizeOrig        = params.getGridCellSize();
        int     cellSizeNew         = 
            getDifferentNumber( cellSizeModel ).intValue();
        assertNotEquals( cellSizeOrig, cellSizeNew );
        cellSizeModel.setValue( cellSizeNew );
        
        boolean showGridOrig        = params.isGridLineShow();
        boolean showGridNew         = !showGridOrig;
        assertNotEquals( showGridNew, showGridCheckBox.isSelected() );
        showGridCheckBox.setSelected( showGridNew );
        
        if ( param )
        {
            okButton.doClick();
            // give dialog thread a chance to settle
            TestUtils.pause( waitAfterDialogAction );

            assertEquals( gridColorNew, params.getGridColor() );
            assertEquals( gridCellColorNew, params.getGridCellColor() );
            assertEquals( gridLineColorNew, params.getGridLineColor() );
            
            assertEquals( lineWidthNew, params.getGridLineWidth() );
            assertEquals( cellSizeNew, params.getGridCellSize() );
            
            assertEquals( showGridNew, params.isGridLineShow() );
        }
        else
        {
            cancelButton.doClick();
            // give dialog thread a chance to settle
            TestUtils.pause( waitAfterDialogAction );
    
            assertEquals( gridColorOrig, params.getGridColor() );
            assertEquals( gridCellColorOrig, params.getGridCellColor() );
            assertEquals( gridLineColorOrig, params.getGridLineColor() );
            
            assertEquals( lineWidthOrig, params.getGridLineWidth() );
            assertEquals( cellSizeOrig, params.getGridCellSize() );
            
            assertEquals( showGridOrig, params.isGridLineShow() );
        }
    }
    
    /**
     * Verify that the dialog fields are initialized with the latest 
     * parameter values with the dialog is opened.
     */
    @Test
    public void testInit()
    {
        if ( prefDlg.isVisible() )
            prefDlg.setVisible( false );
        Color   gridColor   = Color.BLUE;
        assertNotEquals( gridColor, params.getGridColor() );
        params.setGridColor( gridColor );
        
        Color   gridCellColor   = Color.RED;
        assertNotEquals( gridCellColor, params.getGridCellColor() );
        params.setGridCellColor( gridCellColor );
        
        Color   gridLineColor   = Color.RED;
        assertNotEquals( gridLineColor, params.getGridLineColor() );
        params.setGridLineColor( gridLineColor );
        
        int     gridLineWidth   = (int)lineWidthModel.getMaximum();
        assertNotEquals( gridLineWidth, params.getGridLineWidth() );
        params.setGridLineWidth( gridLineWidth );
        
        int     gridCellSize    = (int)cellSizeModel.getMaximum();
        assertNotEquals( gridCellSize, params.getGridCellSize() );
        params.setGridCellSize( gridCellSize );
        
        boolean showGrid    = !params.isGridLineShow();
        params.setGridLineShow( showGrid );
                
        showDialog();
        assertEquals( gridColor, gridColorFeedback.getBackground() );
        assertEquals( gridCellColor, cellColorFeedback.getBackground() );
        assertEquals( gridLineColor, lineColorFeedback.getBackground() );
        assertEquals( gridLineWidth, lineWidthModel.getNumber() );
        assertEquals( gridCellSize, cellSizeModel.getNumber() );
        assertEquals( showGrid, showGridCheckBox.isSelected() );
    }

    private void testSelectNumber(
        SpinnerNumberModel model,
        IntSupplier        supplier
    )
    {
        int origVal = (int)model.getValue();
        int newVal  = origVal + 1;
        model.setValue( newVal );
        
        // newVal should not be saved until after apply button is pushed
        assertEquals( origVal, supplier.getAsInt() );
        apply();
        assertEquals( newVal, supplier.getAsInt() );
    }
    
    private void testSelectColor(
        JButton         actionButton, 
        JLabel          actionFeedback,
        int             swatchCount,
        Supplier<Color> paramReader
    )
    {
        Color   origColor   = paramReader.get();
        Color   newColor    = getColorFromAction(
            actionButton,
            actionFeedback,
            swatchCount
        );
        
        // sanity check; don't want to accidentally select the original color
        assertNotEquals( origColor, newColor );
        
        // the new color has been selected, but shouldn't be saved
        // until the dialog's apply button is pushed.
        assertEquals( origColor, paramReader.get() );
        apply();
        assertEquals( newColor, paramReader.get() );
    }
    
    /**
     * Initiate a color selection, and return the selected color.
     * The color selection is initiated by pushing a button,
     * which launches a color chooser that displays a matrix
     * of color swatches;
     * the button is linked to a feedback label which displays 
     * the selected color. The color is selected from the <em>n + 1th</em>
     * swatch in the first column, as indicated by <em>swatchCount</em>.
     * 
     * @param actionButton      button to push to initiate selection
     * @param actionFeedback    label used to display feedback.
     * @param swatchCount       the number of the swatch to choose from
     *                          the color chooser
     *                          
     * @return  the color displayed in the feedback label
     */
    private Color getColorFromAction( 
        JButton actionButton, 
        JLabel  actionFeedback,
        int     swatchCount
    )
    {
        // start the color chooser from a separate thread
        new Thread( ()-> actionButton.doClick() ).start();
        
        // wait for the color chooser to become visible
        waitForChooser();
        
        // color chooser should now be present and have keyboard focus
        // choose a swatch for the new color:
        // ... push tab to go to the swatch matrix
        // ... push down arrow count times to reach a swatch:
        // ....... never choose the first swatch; the first and second
        // ....... swatches seem to be the same, and the whole point
        // ....... of this is to choose a unique color.
        // ... push spacebar to select swatch
        // ... push enter to activate dialog OK button
        robbie.keyPress( KeyEvent.VK_TAB );
        robbie.delay( 100 );
        
        // seriously, how is this better than a traditional for loop?
        // swatchCount + 1 is to always omit the first swatch
        Stream
            .iterate( 0 , n -> n + 1 )
            .limit( swatchCount + 1 )
            .map( n -> addDelay( n, waitForChooserDown ) )
            .forEach( n -> robbie.keyPress( KeyEvent.VK_DOWN ) );

        robbie.keyPress( KeyEvent.VK_SPACE );
        robbie.keyPress( KeyEvent.VK_ENTER );
        TestUtils.pause( waitForChooserAfter );
        
        Color   color   = actionFeedback.getBackground();
        System.out.println( "***" + color );
        return color;
    }
    
    /**
     * This method is to be used as part of a stream. 
     * It accepts a number (which is mostly ignored) from a stream, 
     * pauses for some length of time, then returns the number
     * to the stream.
     * 
     * @param num   the number to accept/return
     * 
     * @return  the accepted number
     */
    private int addDelay( int num, long delay )
    {
        TestUtils.pause( waitForChooserDown );
        return num;
    }
    
    private void waitForChooser()
    {
        long    startMillis = System.currentTimeMillis();
        while ( ComponentLocator.getComponent( chooserPred, null) == null )
        {
            long    deltaMillis = System.currentTimeMillis() - startMillis;
            if ( deltaMillis > waitForChooserTimeout )
                fail( "Timeout waiting for color chooser to appear" );
            robbie.delay( 10 );
        }
        robbie.delay( 250 );

    }
    
    /**
     * Push the dialog's apply button, then wait for some interval
     * before returning.
     */
    private void apply()
    {
        applyButton.doClick();
        TestUtils.pause( waitAfterDialogAction );
    }
    
    /**
     * Start the preferences dialog on a separate thread.
     * Pause to give the thread time to spin up.
     */
    private void showDialog()
    {
        new Thread( () -> prefDlg.setVisible( true ) ).start();
        TestUtils.pause( 250 );
    }
    
    /**
     * Given a color, return a different color.
     * It is not specified how the new color will differ from the original,
     * only that it is guaranteed that origColor.equals(newColor) 
     * will be false.
     * 
     * @param origColor the given color
     * @return  a color which is different from the original color
     */
    private Color getDifferentColor( Color origColor )
    {
        int     origColorRGB    = origColor.getRGB();
        int     newColorRGB     = origColorRGB + 1;// == 0xFF ? 0x00 : 0xFF;
        Color   newColor        = new Color( newColorRGB );
        assertNotEquals( newColor, origColor );
        return newColor;
    }
    
    /**
     * Given a number spinner, return a number 
     * that is different from the value of the given spinner.
     * It is not specified how the new value will differ from the original,
     * only that it is guaranteed that the two values will be unequal. 
     * 
     * @param model the model associated with the number spinner
     * 
     * @return  a number which is different from the value of the spinner
     */
    private Number 
    getDifferentNumber( SpinnerNumberModel model )
    {
        Number  origNumber  = model.getNumber();
        Number  newNumber   = (Number)model.getNextValue();
        if ( newNumber == null )
            newNumber = (Number)model.getPreviousValue();
        assertNotEquals( newNumber, origNumber );
        return newNumber;
    }
}
