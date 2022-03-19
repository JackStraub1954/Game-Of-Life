package com.gmail.johnstraub1954.game_of_life.components;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.MISC_AUTHOR_EMAIL_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.MISC_AUTHOR_NAME_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.MISC_PATTERN_NAME_PN;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.Border;

import com.gmail.johnstraub1954.game_of_life.main.GOLConstants;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;

public class PatternPropertiesPanel extends JPanel
{
    /** Parameter collector for this application */
    private static final Parameters params  = Parameters.INSTANCE;
    
    /** for creating empty border around this panel */
    private static final int        margin              = 5;
    
    /** Component that notifies ActionListeners of apply actions */
    private final AbstractButton    applyButton;
    /** Component that notifies ActionListeners of cancel actions */
    private final AbstractButton    cancelButton;
    
    
    public PatternPropertiesPanel( 
        AbstractButton applyButton, 
        AbstractButton cancelButton 
    )
    {
        super( new GridBagLayout() );
        this.applyButton = applyButton;
        this.cancelButton = cancelButton;
        
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
        gbc.anchor = GridBagConstraints.PAGE_START;
        
        TextFieldPanel  textPanel   = null;
        textPanel = new TextFieldPanel( 
            "Pattern Name",
            MISC_PATTERN_NAME_PN,
            s -> params.setPatternName( s ),
            () -> params.getPatternName()
        );
        add( textPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        textPanel = new TextFieldPanel( 
            "Author Name",
            MISC_AUTHOR_NAME_PN,
            s -> params.setAuthorName( s ),
            () -> params.getAuthorName()
        );
        add( textPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        textPanel = new TextFieldPanel( 
            "Author Email",
            MISC_AUTHOR_EMAIL_PN,
            s -> params.setAuthorEmail( s ),
            () -> params.getAuthorEmail()
        );
        add( textPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        SpinnerDatePanel    datePanel   = new SpinnerDatePanel(
            "Author Date",
            GOLConstants.MISC_AUTHOR_TIME_PN,
            d -> params.setAuthorTime( d ),
            () -> params.getAuthorTime()
        );
        add( datePanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        IntegerListPanel    listPanel   = new IntegerListPanel(
            "Birth Rules",
            GOLConstants.CTRL_BIRTH_STATES_PN,
            l -> params.setBirthStates( l ),
            () -> params.getBirthStates()
        );
        add( listPanel, gbc );
        gbc.gridy++;
        
        add( new HSeparator( 3, true ), gbc );
        gbc.gridy++;
        
        listPanel = new IntegerListPanel(
            "Survival Rules",
            GOLConstants.CTRL_SURVIVAL_STATES_PN,
            l -> params.setSurvivalStates( l ),
            () -> params.getSurvivalStates()
        );
        add( listPanel, gbc );
        gbc.gridy++;
    }
    
    private static String cvtListToCSV( List<?> list )
    {
        StringBuilder   bldr    = 
            list.stream()
            .distinct()
            .sorted()
            .map( e -> e + "," )
            .collect( 
                StringBuilder::new, 
                StringBuilder::append, 
                StringBuilder::append
            );
        int len = bldr.length();
        if ( len > 0 )
            bldr.deleteCharAt( len - 1 );
        return bldr.toString();
    }
    
    private static List<Integer> cvtCSVToList( String csv )
    {
        List<Integer>   list    = new ArrayList<>();
        String[]        strs    = csv.split( "[\\s,]*" );
        try
        {
            for ( String str : strs )
                list.add( Integer.parseInt( str ) );
        }
        catch ( NumberFormatException exc )
        {
            String  msg = "\"" + csv + "\" is not a list of integers";
            JOptionPane.showMessageDialog(
                null,
                msg,
                "Invalid input", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return list;
    }
    
    private class TextFieldPanel extends JPanel
    {
        public TextFieldPanel( 
            String           text, 
            String           propertyName,
            Consumer<String> consumer,
            Supplier<String> supplier
        )
        {
            super( new GridLayout( 1, 2, 2, 2 ) );
            JLabel      label       = new JLabel( text );
            JTextField  textField   = new JTextField( supplier.get(), 10 );
            label.setHorizontalAlignment( JLabel.RIGHT );
            add( label );
            add( textField );
            params.addPropertyChangeListener(
                propertyName, 
                e -> textField.setText( (String)e.getNewValue() )
            );
            applyButton.addActionListener( 
                e -> consumer.accept( textField.getText() )
            );
            
        }
    }
    
    private class IntegerListPanel extends JPanel
    {
        public IntegerListPanel( 
            String                  text, 
            String                  propertyName,
            Consumer<List<Integer>> consumer,
            Supplier<List<Integer>> supplier
        )
        {
            super( new GridLayout( 1, 2, 2, 2 ) );
            JLabel          label       = new JLabel( text );
            JTextField      textField   = new JTextField( 10 );
            textField.setText( cvtListToCSV( supplier.get() ) );
            label.setHorizontalAlignment( JLabel.RIGHT );
            add( label );
            add( textField );
            
            applyButton.addActionListener(
                e -> consumer.accept( cvtCSVToList( textField.getText() ) ) 
            );
            
        }
    }
    
    private class SpinnerDatePanel extends JPanel
    {
        public SpinnerDatePanel(
            String          text, 
            String          propertyName,
            Consumer<LocalDateTime>  consumer,
            Supplier<LocalDateTime>  supplier
        )
        {
            super( new GridLayout( 1, 2, 2, 2 ) );
            SpinnerDateModel    model   = new SpinnerDateModel();
            JLabel              label   = new JLabel( text );
            JSpinner            spinner = new JSpinner( model );
            
            label.setHorizontalAlignment( JLabel.RIGHT );
            add( label );
            add( spinner );
        }
    }
}
