package com.gmail.johnstraub1954.game_of_life.components;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.gmail.johnstraub1954.game_of_life.main.ActionRegistrar;
import com.gmail.johnstraub1954.game_of_life.main.GOLConstants;

/**
 * Encapsulates the buttons used to dismiss a dialog:
 * OK, Apply or Cancel.
 * Typically displayed at the bottom of a frame.
 * 
 * @author Jack Straub
 * 
 * @see PreferencesDialog
 * @see SaveDialog
 */
public class ButtonPanel extends JPanel
{
    private final JButton   applyButton     = new JButton( "Apply" );
    private final JButton   cancelButton    = new JButton( "Cancel" );
    private final JButton   okayButton      = new JButton( "OK" );
    
    /**
     * Constructor.
     * The parent component (typically a dialog) passes an object
     * (the actionRegistrar) through which the buttons in this panel
     * can notify other components of their selection.
     * 
     * @param actionRegistrar   an object through which the buttons in this
     *                          panel can notify other components 
     *                          of their selection
     */
    public ButtonPanel( ActionRegistrar actionRegistrar )
    {
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        okayButton.addActionListener(
            e -> actionRegistrar.
                fireNotificationEvent( GOLConstants.ACTION_APPLY_PN )
        );
        okayButton.addActionListener(
            e -> actionRegistrar.
                fireNotificationEvent( GOLConstants.ACTION_OKAY_PN )
        );
        applyButton.addActionListener(
            e -> actionRegistrar.
                fireNotificationEvent( GOLConstants.ACTION_APPLY_PN )
        );
        cancelButton.addActionListener(
            e -> actionRegistrar.
                fireNotificationEvent( GOLConstants.ACTION_CANCEL_PN )
        );
        
        applyButton.setName( GOLConstants.BP_APPLY_BUTTON_CN );
        cancelButton.setName( GOLConstants.BP_CANCEL_BUTTON_CN );
        okayButton.setName( GOLConstants.BP_OK_BUTTON_CN );
        
        add( okayButton );
        add( applyButton );
        add( cancelButton );
    }
}
