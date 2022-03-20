package com.gmail.johnstraub1954.game_of_life.components;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gmail.johnstraub1954.game_of_life.main.GOLConstants;

import test_utils.ComponentUtils;

class GridPropertiesPanelTest
{
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
    private JSpinner            cellSizeSpinner;
    
    private JCheckBox           showGridCheckBox;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
    }

    @BeforeEach
    void setUp() throws Exception
    {
        prefDlg = new PreferencesDialog();
        showDialog();
        
        applyButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_APPLY_BUTTON_CN );
        cancelButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_CANCEL_BUTTON_CN );
        okButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_OK_BUTTON_CN );
        gridColorButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_GRID_COLOR_CN );
        cellColorButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_CELL_COLOR_CN );
        lineColorButton = 
            ComponentUtils.getJButton( GOLConstants.PREF_CELL_COLOR_CN );

        gridColorFeedback = 
            ComponentUtils.getJLabel( GOLConstants.PREF_GRID_COLOR_FB_CN );
        cellColorFeedback = 
            ComponentUtils.getJLabel( GOLConstants.PREF_CELL_COLOR_FB_CN );
        gridColorFeedback = 
            ComponentUtils.getJLabel( GOLConstants.PREF_LINE_COLOR_FB_CN );

        lineWidthSpinner = 
            ComponentUtils.getJSpinner( GOLConstants.PREF_LINE_WIDTH_CN );
        cellSizeSpinner = 
            ComponentUtils.getJSpinner( GOLConstants.PREF_CELL_SIZE_CN );
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
    void test()
    {
        System.out.println( applyButton.getText() );
    }
    
    /**
     * Start the preferences dialog on a new event thread.
     * Pause to give the thread time to spin up.
     */
    private void showDialog()
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> prefDlg.setVisible( true ) );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            fail( "Unexpected exception", exc );
        }
    }
}
