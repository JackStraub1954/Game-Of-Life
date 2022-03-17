package com.gmail.johnstraub1954.game_of_life.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RLEDescriptorTest
{
    private static final String endl            = System.lineSeparator();
    private static final String headerFormat    = 
        "x = %d, y = %d, rule = B%s/S%s";
    private RLEDescriptor   rleDescrip;
    
    @BeforeEach
    public void init()
    {
        rleDescrip = new RLEDescriptor();
    }

    @Test
    void testSetGetName()
    {
        assertNull( rleDescrip.getName() );
        String  patternName = "New Pattern Name";
        rleDescrip.setName( patternName );
        assertEquals( patternName, rleDescrip.getName() );
    }

    @Test
    void testSetGetAuthorName()
    {
        assertNull( rleDescrip.getAuthorName() );
        String  authorName = "New Author Name";
        rleDescrip.setAuthorName( authorName );
        assertEquals( authorName, rleDescrip.getAuthorName() );
    }

    @Test
    void testSetGetAuthorEmail()
    {
        assertNull( rleDescrip.getAuthorEmail() );
        String  newValue    = "New Author Email";
        rleDescrip.setAuthorEmail( newValue );
        assertEquals( newValue, rleDescrip.getAuthorEmail() );
    }

    @Test
    void testSetGetAuthorTime()
    {
        assertNull( rleDescrip.getAuthorTime() );
        LocalDateTime   newValue    = LocalDateTime.now();
        rleDescrip.setAuthorTime( newValue );
        assertEquals( newValue, rleDescrip.getAuthorTime() );
    }
    
    @Test
    public void testSetGetGridMap()
    {
        assertNull( rleDescrip.getGridMap() );
        GridMap newValue    = new GridMap();
        rleDescrip.setGridMap( newValue );
        assertEquals( newValue, rleDescrip.getGridMap() );
    }

    @Test
    void testSetGetUpperLeftCorner()
    {
        // corner not explicitly set, no grid map, should return (0, 0)
        assertEquals( rleDescrip.getUpperLeftCorner(), new Point( 0, 0 ) );
        
        // corner not explicitly set, grid map explicitly set,
        // should return the upper left corner of the grid map's
        // live rectangle.
        int     testX1  = 100;
        int     testY1  = 150;
        Point   point1  = new Point( testX1, testY1 );
        int     testX2  = 2 * testX1;
        int     testY2  = 2 * testY1;
        Point   point2  = new Point( testX2, testY2 );
        GridMap gridMap = new GridMap();
        gridMap.put( point1, true );
        gridMap.put( point2, true );
        rleDescrip.setGridMap( gridMap );
        assertEquals( point1, rleDescrip.getUpperLeftCorner() );
        
        // corner explicitly set, should return explicitly set corner
        rleDescrip.setUpperLeftCorner( point2 );
        assertEquals( point2, rleDescrip.getUpperLeftCorner() );
    }

    @Test
    void testSetGetBirthRules()
    {
        List<Integer>   rules   = rleDescrip.getBirthRules();
        assertNotNull( rules );
        assertTrue( rules.isEmpty() );
        List<Integer>   newValue    = List.of( 2, 3, 4 );
        rleDescrip.setBirthRules( newValue );
        assertEquals( newValue, rleDescrip.getBirthRules() );
    }

    @Test
    void testAddBirthRule()
    {
        List<Integer>   expRules    = new ArrayList<>();
        assertEquals( expRules, rleDescrip.getBirthRules() );
        for ( Integer inx : new Integer[] { 1, 2, 3 } )
        {
            expRules.add( inx );
            rleDescrip.addBirthRule( inx );
            assertEquals( expRules, rleDescrip.getBirthRules() );
        }
    }

    @Test
    void testSetGetSurvivalRules()
    {
        List<Integer>   rules   = rleDescrip.getSurvivalRules();
        assertNotNull( rules );
        assertTrue( rules.isEmpty() );
        List<Integer>   newValue    = List.of( 2, 3, 4 );
        rleDescrip.setSurvivalRules( newValue );
        assertEquals( newValue, rleDescrip.getSurvivalRules() );
    }

    @Test
    void testAddSurvivalRule()
    {
        List<Integer>   expRules    = new ArrayList<>();
        assertEquals( expRules, rleDescrip.getSurvivalRules() );
        for ( Integer inx : new Integer[] { 1, 2, 3 } )
        {
            expRules.add( inx );
            rleDescrip.addSurvivalRule( inx );
            assertEquals( expRules, rleDescrip.getSurvivalRules() );
        }
    }

    @Test
    void testSetGetComments()
    {
        List<String>    comments    = rleDescrip.getComments();
        assertNotNull( comments );
        assertTrue( comments.isEmpty() );
        List<String>    newValue    = List.of( "A", "B", "C" );
        rleDescrip.setComments( newValue );
        assertEquals( newValue, rleDescrip.getComments() );
    }

    @Test
    void testAddCommentsOnePerTime()
    {
        List<String>    expComments = new ArrayList<>();
        assertEquals( expComments, rleDescrip.getComments() );
        for ( int inx = 0 ; inx < 3 ; ++inx )
        {
            String  nextComment = "next comment " + inx;
            expComments.add( nextComment );
            rleDescrip.addComments( nextComment );
            assertEquals( expComments, rleDescrip.getComments() );
        }
    }

    @Test
    void testAddCommentsInMultiples()
    {
        List<String>    expComments = new ArrayList<>();
        int             inx         = 0;
        String          commentA    = "Comment " + inx++;
        String          commentB    = "Comment " + inx++;
        String          comments    = commentA + endl + commentB;
        expComments.add( commentA );
        expComments.add( commentB );
        rleDescrip.addComments( comments );
        assertEquals( expComments, rleDescrip.getComments() );

        commentA    = "Comment " + inx++;
        commentB    = "Comment " + inx++;
        comments    = commentA + endl + commentB;
        expComments.add( commentA );
        expComments.add( commentB );
        rleDescrip.addComments( comments );
        assertEquals( expComments, rleDescrip.getComments() );
    }

    @Test
    void testRemoveComment()
    {
        List<String>    expComments = new ArrayList<>();
        assertEquals( expComments, rleDescrip.getComments() );
        for ( int inx = 0 ; inx < 3 ; ++inx )
        {
            String  nextComment = "next comment " + inx;
            expComments.add( nextComment );
            rleDescrip.addComments( nextComment );
            assertEquals( expComments, rleDescrip.getComments() );
        }
        
        while ( !expComments.isEmpty() )
        {
            int     inx     = expComments.size() / 2;
            String  comment = expComments.remove( inx );
            rleDescrip.removeComment( comment );
            assertEquals( expComments, rleDescrip.getComments() );
        }
    }

    @Test
    void testGetHeaderComments()
    {
        List<String>    actComments = rleDescrip.getHeaderComments();
        assertTrue( actComments.isEmpty() );
        
        List<String>    expComments = new ArrayList<>();
        for ( int inx = 0 ; inx < 3 ; ++inx )
        {
            String  comment = "comment " + inx;
            expComments.add( "#C " + comment );
            rleDescrip.addComments( comment );
            assertEquals( expComments, rleDescrip.getHeaderComments() );
        }
    }
    
    @Test
    public void testGetPatternNameHeader()
    {
        rleDescrip.setName( null );
        String  nameComment = getComment( "#N" );
        assertNull( nameComment );
        
        rleDescrip.setName( "" );
        nameComment = getComment( "#N" );
        assertNull( nameComment );
        
        String  name    = "this name";
        rleDescrip.setName( name );
        nameComment = getComment( "#N" );
        assertNotNull( nameComment );
        assertTrue( nameComment.endsWith( name ) );
    }
    
    @Test
    public void testGetAuthorHeader()
    {
        String          testName    = "Author, The";
        String          testEmail   = "author@email.com";
        LocalDateTime   testTime    = LocalDateTime.now();
        String          testYear    = testTime.getYear() + "";
        
        // email and timestamp are present but author name is null,
        // so there shouldn't be an author comment.
        rleDescrip.setAuthorName( null );
        rleDescrip.setAuthorEmail( testEmail );
        rleDescrip.setAuthorTime( testTime );
        String  nameComment = getComment( "#O" );
        assertNull( nameComment );
        
        // email and timestamp are present but author name is empty,
        // so there shouldn't be an author comment.
        rleDescrip.setAuthorName( "" );
        nameComment = getComment( "#O" );
        assertNull( nameComment );
        
        // email, timestamp and author name are present,
        // so there should be an author comment with all three fields present.
        rleDescrip.setAuthorName( testName );
        nameComment = getComment( "#O" );
        assertNotNull( nameComment );
        assertTrue( nameComment.contains( testName ) );
        assertTrue( nameComment.contains( testEmail ) );
        assertTrue( nameComment.contains( testYear ) );
        
        // Remove the timestamp; author comment should be present,
        // with only name and email fields identified.
        rleDescrip.setAuthorTime( null );
        nameComment = getComment( "#O" );
        assertNotNull( nameComment );
        assertTrue( nameComment.contains( testName ) );
        assertTrue( nameComment.contains( testEmail ) );
        assertFalse( nameComment.contains( testYear ) );
        
        // Author name is present, timestamp is null and email is empty;
        // author comment should be present, and contain only the author name
        rleDescrip.setAuthorEmail( "" );
        nameComment = getComment( "#O" );
        assertNotNull( nameComment );
        assertTrue( nameComment.contains( testName ) );
        assertFalse( nameComment.contains( testEmail ) );
        assertFalse( nameComment.contains( testYear ) );
        
        // Author name is present, timestamp and email are null;
        // author comment should be present, and contain only the author name
        rleDescrip.setAuthorEmail( null );
        nameComment = getComment( "#O" );
        assertNotNull( nameComment );
        assertTrue( nameComment.contains( testName ) );
        assertFalse( nameComment.contains( testEmail ) );
        assertFalse( nameComment.contains( testYear ) );
    }

    @Test
    void testGetHeaderLine()
    {
        int     xco     = 100;
        int     yco     = 200;
        Point   ulc     = new Point( xco, yco );
        rleDescrip.setUpperLeftCorner( ulc );
        
        Integer[]       birthRuleInts       = { 2, 3, 4 };
        Integer[]       survivalRuleInts    = { 4, 5, 6, 7 };
        List<Integer>   birthRules          = List.of( birthRuleInts );
        List<Integer>   survivalRules       = List.of( survivalRuleInts );
        rleDescrip.setBirthRules( birthRules );
        rleDescrip.setSurvivalRules( survivalRules );
        
        StringBuilder   birthBldr           = new StringBuilder();
        birthRules.forEach( i -> birthBldr.append( i.toString() ) );
        
        StringBuilder   survivalBldr        = new StringBuilder();
        survivalRules.forEach( i -> survivalBldr.append( i.toString() ) );
        
        String  expHeader   = 
            String.format( headerFormat, xco, yco, birthBldr, survivalBldr )
            .toUpperCase();
        String  actHeader   = rleDescrip.getHeaderLine().toUpperCase();
        assertEquals( expHeader, actHeader );
    }
    
    /**
     * Exercise the upper-left corner component of the header line.
     * <ul>
     * <li>
     *      If the ULC is explicitly set, the header line should
     *      reflect that specific value.
     * <li>
     * <li>
     *      If the ULC is not explicitly set, 
     *      and the grid map is set, the header line should
     *      reflect the upper-left corner of the grid map
     *      live rectangle.
     * <li>
     * <li>
     *      If the ULC is not explicitly set, 
     *      and the grid map is not present, the header line should
     *      reflect an upper-left corner of (0,0).
     * <li>
     * </ul>
     */
    @Test
    public void testULCHeaderLine()
    {
        String  format          = "x = %d, y = %d";
        
        int     defXco          = 0;
        int     defYco          = 0;
        Point   defULCPoint     = new Point( defXco, defYco );
        int     gridXco         = defXco + 100;
        int     gridYco         = defYco + 200;
        Point   testGridPoint   = new Point( gridXco, gridYco );
        int     ulcXco          = 2 * gridXco;
        int     ulcYco          = 2 * gridYco;
        Point   testULCPoint    = new Point( ulcXco, ulcYco );
        GridMap gridMap         = new GridMap();
        gridMap.put( testGridPoint, true );
        
        rleDescrip.setGridMap( gridMap );
        rleDescrip.setUpperLeftCorner( testULCPoint );
        String  expULC  = String.format( format, ulcXco, ulcYco );
        String  actULC  = rleDescrip.getHeaderLine();
        assertTrue( actULC.contains( expULC ) );
        
        rleDescrip.setUpperLeftCorner( null );
        expULC = String.format( format, gridXco, gridYco );
        actULC = rleDescrip.getHeaderLine();
        assertTrue( actULC.contains( expULC ) );
        
        rleDescrip.setUpperLeftCorner( null );
        rleDescrip.setGridMap( null );
        expULC = String.format( format, defXco, defYco );
        actULC = rleDescrip.getHeaderLine();
        assertTrue( actULC.contains( expULC ) );
        
        rleDescrip.setUpperLeftCorner( null );
        rleDescrip.setGridMap( new GridMap() );
        expULC = String.format( format, defXco, defYco );
        actULC = rleDescrip.getHeaderLine();
        assertTrue( actULC.contains( expULC ) );
    }
    
    /**
     * Validate the birth state in the header line.
     * If birth state list is empty, birth state should be { 3 },
     * otherwise it should reflect the explicit birth state.
     */
    @Test
    public void testBirthStateHeaderLine()
    {
        String          defRuleState        = "B3/S23";
        
        String          headerLine          = 
            rleDescrip.getHeaderLine().toUpperCase();
        assertTrue( headerLine.contains( defRuleState ) );
        
        rleDescrip.addBirthRule( 4 );
        String          expRuleState        = "B34/S23";
        headerLine = rleDescrip.getHeaderLine().toUpperCase();
        assertTrue( headerLine.contains( expRuleState ) );
    }
    
    /**
     * Validate the survival state in the header line.
     * If survival state list is empty, survival state should be { 2, 3 },
     * otherwise it should reflect the explicit survival state.
     */
    @Test
    public void testSurvivalStateHeaderLine()
    {
        String          defRuleState        = "B3/S23";
        
        String          headerLine          = 
            rleDescrip.getHeaderLine().toUpperCase();
        assertTrue( headerLine.contains( defRuleState ) );
        
        rleDescrip.addSurvivalRule( 4 );
        String          expRuleState        = "B3/S234";
        headerLine = rleDescrip.getHeaderLine().toUpperCase();
        assertTrue( headerLine.contains( expRuleState ) );
    }

    @Test
    void testIterator()
    {
        int         startX      = 100;
        int         startY      = 200;
        String[]    cells       =
        {
            "ooo",
            "bbooooooo",
            "oboboboboboboo",
            "oobbbo"
        };
        
        GridMap     gridMap     = new GridMap();
        int         row         = startY;
        for ( String str : cells )
        {
            for ( int col = 0 ; col < str.length() ; ++col )
            {
                if ( str.charAt( col ) == 'o' )
                    gridMap.put( startX + col, row, true );
            }
            ++row;
        }
        
        rleDescrip.setGridMap( gridMap );
        Iterator<Character> iter    = rleDescrip.iterator();
        int limit   = cells.length;
        
        for ( int inx = 0 ; inx < limit ; ++inx )
        {
            String  str         = cells[inx];
            char[]  strChars    = str.toCharArray();
            for ( char expChar : strChars )
            {
                assertTrue( iter.hasNext() );
                char    actChar = iter.next();
                assertEquals( expChar, actChar );
            }
            assertTrue( iter.hasNext() );
            if ( inx < limit - 1 )
                assertEquals( '$', iter.next() );
            else
                assertEquals( '!', iter.next() );
        }
        assertFalse( iter.hasNext() );
    }

    @Test
    void testIteratorGoWrong()
    {
        GridMap              gridMap    = new GridMap();
        gridMap.put( 0, 0, true );
        rleDescrip.setGridMap( gridMap );
        Iterator<Character> iter        = rleDescrip.iterator();
        
        assertTrue( iter.hasNext() );
        assertEquals( 'o', iter.next() );
        assertTrue( iter.hasNext() );
        assertEquals( '!', iter.next() );
        
        assertFalse( iter.hasNext() );
        Class<NoSuchElementException>   clazz   = 
            NoSuchElementException.class;
        assertThrows( clazz, () -> iter.next() );
    }
    
    /**
     * Locate a specific comment in the RLEDescriptors list of comments.
     * The comment to locate is indicated by a given prefix.
     * For example, to get the author comment, specify the "#O" prefix.
     * 
     * @param prefix    the given prefix
     * 
     * @return the target comment, or null if not found.
     */
    private String getComment( String prefix )
    {
        List<String>    comments    = rleDescrip.getHeaderComments();
        String          result      = null;
        for ( String comment : comments )
        {
            if ( comment.startsWith( prefix ) )
            {
                result = comment;
                break;
            }
        }
        
        return result;
    }
}
