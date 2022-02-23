package com.gmail.johnstraub1954.game_of_life.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

class RLEGridDecoderTest
{
    private static final String endl    = System.lineSeparator();
        
    private int counter = 0;

    @Test
    void testRLEGridDecoder()
    {
        testIterator( "obob" );
    }

    /**
     * Parse a buffer with multiple chars 
     * ending in !.
     */
    @Test
    void testIteratorMiscBang()
    {
        String  buff    =
            "b" + endl
            + "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb!";
        testIterator( buff );
    }
    
    @Test
    void testIteratorMiscNoBang()
    {
        String  buff    =
            "b" + endl
            + "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb";
        testIterator( buff );
    }
    
    @Test
    void testIteratorBangOnly()
    {
        String  buff    = "!";
        testIterator( buff );
    }
    
    @Test
    void testIteratorEmptyBuffer()
    {
        String  buff    = "";
        testIterator( buff );
    }
    
    @Test
    public void testIteratorCommentsBefore()
    {
        String  buff    =
            "#" + endl
            + "#5 comments" + endl
            + "b" + endl
            + "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb";
        testIterator( buff );
    }
    
    @Test
    public void testIteratorBlankLinesBefore()
    {
        String  buff    =
            endl + endl + endl
            + "#" + endl
            + "#" + endl
            + "b" + endl
            + "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb";
        testIterator( buff );
    }
    
    @Test
    public void testIteratorCommentsAfterWithBang()
    {
        String  buff    =
            "b" + endl
            + "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb!" + endl
            + "#" + endl
            + "#" + endl;
        testIterator( buff );
    }
    
    @Test
    public void testIteratorCommentsAfterWithoutBang()
    {
        String  buff    =
            "b" + endl
            + "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb" + endl
            + "#" + endl
            + "# this is a comment" + endl;
        testIterator( buff );
    }
    
    @Test
    public void testIteratorBlankLinesAfterWithoutBang()
    {
        String  buff    =
            "b" + endl
            + "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb" + endl
            + endl
            + "# this is a comment" + endl;
        testIterator( buff );
    }
    
    @Test
    public void testIteratorCommentsOnly()
    {
        String  buff    =
            "#" + endl
            + "#" + endl;
        testIterator( buff );
    }
    
    @Test
    public void testIteratorBangFollowedByCruft()
    {
        String  buff    =
            "b" + endl
            + "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb!ooo";
        testIterator( buff );
    }

    @Test
    public void testCountCharacterPairs()
    {
        String          input       = "3ob5obo3b3$!";
        String          expOutput   = "ooobooooobobbb$$$";
        StringBuilder   actOutput   = new StringBuilder();
        try (
            ByteArrayInputStream    inStream        = 
                new ByteArrayInputStream( input.getBytes() );
            InputStreamReader       streamReader    =
                new InputStreamReader( inStream );
            BufferedReader          bufReader   =
                new BufferedReader( streamReader );
        )
        {
            RLEGridDecoder      decoder     = new RLEGridDecoder( bufReader );
            Iterator<Character> iter        = decoder.iterator();
            while ( iter.hasNext() )
                actOutput.append( iter.next() );
            
            assertEquals( expOutput, actOutput.toString() );
        }
        catch ( IOException exc )
        {
            fail( "Unexpected IOException", exc );
        }
    }

    @Test
    public void testAbuseIterator()
    {
        try (
            ByteArrayInputStream    inStream        = 
                new ByteArrayInputStream( "ooo".getBytes() );
            InputStreamReader       streamReader    =
                new InputStreamReader( inStream );
            BufferedReader          bufReader   =
                new BufferedReader( streamReader );
        )
        {
            RLEGridDecoder      decoder     = new RLEGridDecoder( bufReader );
            Iterator<Character> iter        = decoder.iterator();
            while ( iter.hasNext() )
                iter.next();
            
            assertThrows( NoSuchElementException.class, () -> iter.next() );
        }
        catch ( IOException exc )
        {
            fail( "Unexpected IOException", exc );
        }
    }

    @Test
    public void testIOException()
    {
        try (
            ByteArrayInputStream    inStream        = 
                new ByteArrayInputStream( "ooo".getBytes() );
            InputStreamReader       streamReader    =
                new InputStreamReader( inStream );
            BufferedReader          bufReader   =
                new BufferedReaderTester( streamReader );
        )
        {
            assertThrows( 
                GOLException.class, 
                () -> new RLEGridDecoder( bufReader ) 
            );

        }
        catch ( IOException exc )
        {
            fail( "Unexpected IOException", exc );
        }
    }

    /**
     * This method does not work if the input contains counts.
     * Eg., "bobobo$o!" is allowed but "3ob" is not.
     * 
     * @param buff  test input
     */
    private void testIterator( String buff )
    {
        try (
            ByteArrayInputStream    inStream        = 
                new ByteArrayInputStream( buff.getBytes() );
            InputStreamReader       streamReader    =
                new InputStreamReader( inStream );
            BufferedReader          bufReader   =
                new BufferedReader( streamReader );
        )
        {
            testIterator( buff, bufReader );
        }
        catch ( IOException exc )
        {
            fail( "Unexpected IOException", exc );
        }
    }
    
    private void testIterator( String buff, BufferedReader reader )
    {
        // expected input is the same as buff, but without the 
        // line endings.
        
        String          expInput   = getExpectedOutput( buff );
        System.out.println( ">>> " + expInput );
        RLEGridDecoder  decoder     = new RLEGridDecoder( reader );
        int             charInx     = 0;
        System.out.print( "!!! " );
        for ( Character ccc : decoder )
        {
            System.out.print( expInput.charAt( charInx ) );
            assertEquals( expInput.charAt( charInx++ ), ccc );
        }
        System.out.println();
        int inputLen    = expInput.length();
        
        // iterator must never go past end of buffer
        assertFalse( charInx > inputLen );
        
        // iterator must end when:
        // ... it reaches end-of-string
        // ... it reaches !
        // ... it reaches a whitespace character
        String  message = 
            "inputLen == " + inputLen
            + ", charInx == " + charInx;
        assertTrue( 
            charInx == inputLen 
            || expInput.charAt( charInx ) == '!'
            || Character.isWhitespace( expInput.charAt( charInx ) )
            , message
        );
        System.out.println();
    }
        
    /**
     * Removes comments and line endings from a given string.
     * 
     * @param buff the given string
     * 
     * @return  a string equivalent to the given string,
     *          but with all line endings removed
     */
    private String getExpectedOutput( String buf )
    {
        StringBuilder   bldr    = new StringBuilder();
        try (
            ByteArrayInputStream    inStream        = 
                new ByteArrayInputStream( buf.getBytes() );
            InputStreamReader       streamReader    =
                new InputStreamReader( inStream );
            BufferedReader          bufReader   =
                new BufferedReader( streamReader );
        )
        {
            String          line    = null;
            while ( (line = bufReader.readLine()) != null )
            {
                if ( line.isEmpty() )
                    ; // skip empty lines
                else if ( line.startsWith( "#" ) )
                    ; //skip comments
                else
                    bldr.append( line );
            }
            System.out.printf( "%d. %s%n", ++counter, bldr );
        }
        catch ( IOException exc )
        {
            fail( "Unexpected IOException", exc );
        }
        return bldr.toString();
    }

    /**
     * This class is used to simulate IO errors when the decoder
     * is attempting to read input.
     * 
     * @author Jack Straub
     */
    private class BufferedReaderTester extends BufferedReader
    {
        /**
         * Constructor required because BufferedReader
         * doesn't have a default constructor.
         * 
         * @param inStream  InputStreamReader to construct
         *                  BufferedReader superclass
         */
        public BufferedReaderTester( InputStreamReader inStream )
        {
            super( inStream );
        }
        
        /**
         * Overrides BufferedReader.readLine().
         * Immediately throws IOException.
         */
        @Override
        public String readLine() throws IOException
        {
            throw new IOException( "this is a test" );
        }
    }
}
