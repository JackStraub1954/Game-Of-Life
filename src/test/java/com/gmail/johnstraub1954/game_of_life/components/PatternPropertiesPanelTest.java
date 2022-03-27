package com.gmail.johnstraub1954.game_of_life.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.text.JTextComponent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.gmail.johnstraub1954.game_of_life.main.GOLConstants;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;

import test_utils.ComponentUtils;
import test_utils.TestUtils;

class PatternPropertiesPanelTest
{
    /** Time zone ID for translating between Date and LocalDate */
    private static final ZoneId    ZONE_ID     = ZoneId.systemDefault();
    
    /** Central parameter store */
    private static final Parameters params  = Parameters.INSTANCE;
    /** Preferences dialog (parent of PatterPropertiesPanel) */
    private PreferencesDialog   prefDlg;
    /** Preferences dialog apply button */
    private JButton             applyButton;
    /** Preferences dialog OK button */
    private JButton             okButton;
    /** Preferences dialog cancel button */
    private JButton             cancelButton;

    /** The text box used to configure the pattern name property */
    private JTextComponent      patternNameTextBox;
    /** The text box used to configure the author name property */
    private JTextComponent      authorNameTextBox;
    /** The text box used to configure the author email property */
    private JTextComponent      authorEmailTextBox;
    /** The text box used to configure the pattern filename property */
    private JTextComponent      patternFileNameTextBox;
    
    /** The spinner used to configure the author date property */
    private JSpinner            authorDateSpinner;
    /** The spinner model used to configure the author date property */
    private SpinnerDateModel    authorDateSpinnerModel;

    /** The text box used to configure the birth rules property */
    private JTextComponent      birthRulesTextBox;
    /** The text box used to configure the survival rules property */
    private JTextComponent      survivalRulesTextBox;

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
    
        patternNameTextBox =
            ComponentUtils.getJTextField( GOLConstants.PREF_PATTERN_NAME_CN );
        authorNameTextBox =
            ComponentUtils.getJTextField( GOLConstants.PREF_AUTHOR_NAME_CN );
        authorEmailTextBox =
            ComponentUtils.getJTextField( GOLConstants.PREF_AUTHOR_EMAIL_CN );
        patternFileNameTextBox =
            ComponentUtils.getJTextField( GOLConstants.PREF_FILE_NAME_CN );
        
        authorDateSpinner =
            ComponentUtils.getJSpinner( GOLConstants.PREF_AUTHOR_TIME_CN );
        authorDateSpinnerModel = 
            (SpinnerDateModel)authorDateSpinner.getModel();

        birthRulesTextBox =
            ComponentUtils.getJTextField( GOLConstants.PREF_BIRTH_RULES_CN );
        survivalRulesTextBox =
            ComponentUtils.
                getJTextField( GOLConstants.PREF_SURVIVAL_RULES_CN );
    }
    
    @AfterEach
    public void afterEach()
    {
        if ( prefDlg.isVisible() )
            prefDlg.setVisible( false );
        prefDlg.dispose();
    }

    @Test
    void testPatternNameProperty()
    {
        testText( patternNameTextBox, () -> params.getPatternName(), true );
        testText( patternNameTextBox, () -> params.getPatternName(), false );
    }

    @Test
    void testAuthorNameProperty()
    {
        testText( authorNameTextBox, () -> params.getAuthorName(), true );
        testText( authorNameTextBox, () -> params.getAuthorName(), false );
    }

    @Test
    void testAuthorEmailProperty()
    {
        testText( authorEmailTextBox, () -> params.getAuthorEmail(), true );
        testText( authorEmailTextBox, () -> params.getAuthorEmail(), false );
    }
    
    @Test
    public void testBirthRules()
    {
        testList( birthRulesTextBox, () -> params.getBirthStates(), true );
        testList( birthRulesTextBox, () -> params.getBirthStates(), false );
    }
    
    @Test
    public void testSurvivalRules()
    {
        testList( 
            survivalRulesTextBox,
            () -> params.getSurvivalStates(),
            true
        );
        testList(
            survivalRulesTextBox,
            () -> params.getSurvivalStates(),
            false
        );
    }
    
    /**
     * Get a tiny bit more code coverage.
     * See what happens when the Panel code has to convert and empty list
     * to a CSV String and vice versa.
     */
    @Test
    public void testEmptyRules()
    {
        if ( prefDlg.isVisible() )
            prefDlg.setVisible( false );
        params.setBirthStates( new ArrayList<Integer>() );
        showDialog();
        assertTrue( birthRulesTextBox.getText().isEmpty() );
        
        params.setBirthStates( List.of( 1 ) );
        applyButton.doClick();
        assertTrue( params.getBirthStates().isEmpty() );
        
        // restore birth rules to a non-empty state
        params.setBirthStates( List.of( 2, 3 ) );
    }
    
    @ParameterizedTest
    @ValueSource( booleans= {true, false} )
    public void testAuthorDateProperty( boolean selectApply )
    {
        LocalDateTime   origTime    = params.getAuthorTime();
        LocalDateTime   newTime     = origTime.plusDays( 1 );
        LocalDateTime   dialogTime  = getLocalDate( authorDateSpinnerModel );
        
        // text in the component should be initialized from the 
        // value stored in the Parameters instance
        assertEquals( origTime, dialogTime );
        setDate( newTime, authorDateSpinnerModel );
        
        // setting the text in the component should not change the
        // associated parameter until an apply action is propagated
        assertEquals( origTime, params.getAuthorTime() );
        
        if ( selectApply )
        {
            applyButton.doClick();
            assertEquals( newTime, params.getAuthorTime() );
            
            newTime = newTime.plusDays( 1 );
            setDate( newTime, authorDateSpinnerModel );
            okButton.doClick();
            assertEquals( newTime, params.getAuthorTime() );
        }
        else
        {
            cancelButton.doClick();
            assertEquals( origTime, params.getAuthorTime() );
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
        String  patternName = "test pattern name";
        assertNotEquals( patternName, params.getPatternName() );
        params.setPatternName( patternName );
        
        String  authorName  = "test author name";
        assertNotEquals( authorName, params.getAuthorName() );
        params.setAuthorName( authorName );
        
        String  authorEmail = "test author email";
        assertNotEquals( authorEmail, params.getAuthorEmail() );
        params.setAuthorEmail( authorEmail );
        
        String  patternFileName = "test pattern file name";
        assertNotEquals( patternFileName, params.getPatternFileName() );
        params.setPatternFileName( patternFileName );
        
        // When converting LocalDateTime -> Date -> LocalDateTime
        // (which is required when working with SpinnerDateModel)
        // precision is lost. For testing, start with a LocalDateTime
        // that corresponds to a whole second.
        String          testTime    = "1999-12-31T11:59:59";
        LocalDateTime   authorTime  = LocalDateTime.parse( testTime );
        assertNotEquals( authorTime, params.getAuthorTime() );
        params.setAuthorTime( authorTime );
        
        List<Integer>   birthRules  = List.of( 0, 8 );
        assertNotEquals( birthRules, params.getBirthStates() );
        params.setBirthStates( birthRules );
        
        List<Integer>   survivalRules  = List.of( 1, 7 );
        assertNotEquals( survivalRules, params.getSurvivalStates() );
        params.setSurvivalStates( survivalRules );
        
        showDialog();
        assertEquals( patternName, patternNameTextBox.getText() );
        assertEquals( authorName, authorNameTextBox.getText() );
        assertEquals( authorEmail, authorEmailTextBox.getText() );
        assertEquals( patternFileName, patternFileNameTextBox.getText() );
        assertEquals( authorTime, getLocalDate( authorDateSpinnerModel ) );
        List<Integer>   temp    = 
            getIntegerList( birthRulesTextBox.getText() );
        assertEquals( birthRules, temp );
        temp = getIntegerList( survivalRulesTextBox.getText() );
        assertEquals( survivalRules, temp );
    }

    private void testText(
        JTextComponent      textBox,
        Supplier<String>    supplier,
        boolean             selectApply
    )
    {
        final String suffix = "_test";
        String  origText    = supplier.get();
        String  newText     = origText + suffix;
        String  dialogText  = textBox.getText();
        
        // text in the component should be initialized from the 
        // value stored in the Parameters instance
        assertEquals( origText, dialogText );
        textBox.setText( newText );
        
        // setting the text in the component should not change the
        // associated parameter until an apply action is propagated
        assertEquals( origText, supplier.get() );
        
        if ( selectApply )
        {
            applyButton.doClick();
            assertEquals( newText, supplier.get() );
            
            newText = newText + suffix;
            textBox.setText( newText );
            okButton.doClick();
            assertEquals( newText, supplier.get() );
        }
        else
        {
            cancelButton.doClick();
            assertEquals( origText, supplier.get() );
        }
    }

    private void testList(
        JTextComponent          textBox,
        Supplier<List<Integer>> supplier,
        boolean                 selectApply
    )
    {
        List<Integer>   origList    = supplier.get();
        List<Integer>   newList     = getDifferentList( origList );
        List<Integer>   dialogList  = getIntegerList( textBox.getText() );
        
        // text in the component should be initialized from the 
        // value stored in the Parameters instance
        assertEquals( origList, dialogList );
        String          csvString   = getCSVString( newList );
        textBox.setText( csvString );
        
        // setting the text in the component should not change the
        // associated parameter until an apply action is propagated
        assertEquals( origList, supplier.get() );
        
        if ( selectApply )
        {
            applyButton.doClick();
            assertEquals( newList, supplier.get() );
            
            newList = getDifferentList( newList );
            csvString = getCSVString( newList );
            textBox.setText( csvString );
            okButton.doClick();
            assertEquals( newList, supplier.get() );
        }
        else
        {
            cancelButton.doClick();
            assertEquals( origList, supplier.get() );
        }
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
     * Translates a java.util.Date object into a LocalDate object.
     * An exception is thrown if the input is not a Date object.
     * 
     * @param obj   The object to translate
     * 
     * @return  The translated value
     */
    private static LocalDateTime getLocalDate( SpinnerDateModel model )
    {
        Date            dateIn  = model.getDate();
        LocalDateTime   dateOut =
            dateIn.toInstant().atZone( ZONE_ID ).toLocalDateTime();
        return dateOut;
    }
    
    /**
     * Translates a LocalDate object into a java.util.Date object.
     * An exception is thrown if the input is not a LocalDate object.
     * 
     * @param obj   The object to translate
     * 
     * @return  The translated value
     */
    private static void setDate( LocalDateTime dateIn, SpinnerDateModel model )
    {
        ZonedDateTime   zTime       = dateIn.atZone( ZONE_ID );
        Instant         instant     = zTime.toInstant();
        Date            dateOut     = Date.from( instant );
        model.setValue( dateOut );
    }
    
    private static List<Integer> getIntegerList( String text )
    {
        String[]        strs    = text.split( "," );
        List<Integer>   list    = new ArrayList<>();
        if ( !text.isEmpty() )
            for ( String str : strs )
                list.add( Integer.parseInt( str ) );
        return list;
    }
    
    /**
     * Given a list of integers, return a different list of integers.
     * There is no guarantee as to how the returned list differs from
     * the input list, only that:
     * <ul>
     * <li>the returned list will be non-empty</li>
     * <li>the returned list will not be equal to the input list</li>
     * </ul>
     * @param listIn
     * @return
     */
    private static List<Integer> getDifferentList( List<Integer> listIn )
    {
        List<Integer>   listOut = new ArrayList<>( listIn );
        int             size    = listOut.size();
        if ( size > 1 )
            listOut.remove( size - 1 );
        else if ( size == 0 )
            listOut.add( 1 );
        else if ( !listOut.contains( 1 ) )// listOut.size == 1
            listOut.add( 1 );
        else
            listOut.add( 2 );
        return listOut;
    }
    
    /**
     * Convert a list of integers into a CSV string 
     * consisting of those integers.
     * 
     * @param list  the given list of integers
     * 
     * @return  a CSV string consisting of the integers in the given list
     */
    private static String getCSVString( List<Integer> list )
    {
        StringBuilder   bldr    = new StringBuilder();
        for ( Integer obj : list )
            bldr.append( obj ).append( "," );
        int             len     = bldr.length();
        if (len > 0 )
            bldr.deleteCharAt( len - 1 );
        return bldr.toString();
    }
}
