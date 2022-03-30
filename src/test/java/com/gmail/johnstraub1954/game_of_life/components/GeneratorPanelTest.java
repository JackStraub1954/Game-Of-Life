package com.gmail.johnstraub1954.game_of_life.components;

import static org.junit.jupiter.api.Assertions.fail;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gmail.johnstraub1954.game_of_life.main.GOLConstants;

import test_utils.ComponentUtils;

class GeneratorPanelTest
{
    private static JButton          nextGenerationButton;
    private static JButton          rewindButton;
    private static JComponent       genPerSecondFeedback;
    private static JSlider          getPerSecondSlider;
    private static JToggleButton    animateToggleButton;
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GridFrame   frame   = new GridFrame();
        SwingUtilities.invokeAndWait( () -> frame.run() );
        
        nextGenerationButton = 
            ComponentUtils.getJButton( GOLConstants.GEN_NEXT_BUTTON_CN );
        rewindButton = 
            ComponentUtils.getJButton( GOLConstants.GEN_REWIND_BUTTON_CN );
        genPerSecondFeedback = 
            ComponentUtils.getJComponent( GOLConstants.GEN_FEEDBACK_COMP_CN );
        getPerSecondSlider = 
            ComponentUtils.getJSlider( GOLConstants.GEN_PER_SEC_COMP_CN );
    }

    @BeforeEach
    void setUp() throws Exception
    {
    }

    @Test
    void test()
    {
        fail("Not yet implemented");
    }

}
