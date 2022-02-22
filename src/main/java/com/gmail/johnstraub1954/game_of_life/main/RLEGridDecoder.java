package com.gmail.johnstraub1954.game_of_life.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class RLEGridDecoder implements Iterable<Character>
{
    private final List<String>      codes   = new ArrayList<>();
    
    /**
     * Constructor.
     * Skips all blank lines and initial comments in the stream
     * indicated by a given stream reader.
     * Parses the input sequence contained in the stream
     * (lines consisting of exclusively b, o, $ or !).
     * The reader's cursor is left positioned at either
     * the first or second line after the end of the input sequence,
     * or at the end of the stream if there are no lines
     * after the input sequence.
     * 
     * @param reader    the given stream reader
     * 
     * @throws GOLException if the input sequence could not be parsed
     */
    public RLEGridDecoder( BufferedReader reader ) throws GOLException
    {
        try
        {
            // Eliminate blank INITIAL blank lines and comments
            String  line    = reader.readLine();
            while (
                line != null 
                && (line.isEmpty() || line.charAt( 0 ) == '#')
            )
                line = reader.readLine();
            
            // Get all the SIGNIFICANT lines
            // Stop at:
            // ... EOF
            // ... line containing !
            // ... blank line
            // ... comment
            boolean done    = false;
            int     bangInx = -1;
            while ( !done )
            {
                if ( line == null )
                    done = true;
                else if ( line.isEmpty() )
                    done = true;
                else if ( line.charAt( 0 ) == '#' )
                    done = true;
                else if ( (bangInx = line.indexOf(  '!' )) >= 0 )
                {
                    done = true;
                    String  fragment    = line.substring( 0, bangInx );
                    
                    // Add the start of the line ending with the !
                    // if the line starts with a !, don't add anything
                    if ( !fragment.isEmpty() )
                        codes.add( fragment );
                }
                else
                {
                    codes.add( line );
                    line = reader.readLine();
                }
            }
        }
        catch ( IOException exc )
        {
            throw new GOLException( "I/O error", exc );
        }
        
        // Make sure the list of lines contains at least one line,
        // and that the last line ends with a !.
        int count   = codes.size();
        if ( count == 0 )
        {
            codes.add( "!" );
        }
        else
        {
            String  last    = codes.get( count - 1 );
            codes.set( count - 1, last + "!" );
        }
    }

    /**
     * Returns an iterator that sequentially traverses
     * the input sequence for a grid spec.
     * 
     * @return  an iterator that sequentially traverses
     *          the input sequence for a grid spec
     */
    @Override
    public Iterator<Character> iterator()
    {
        CharIterator    iter    = new CharIterator();
        return iter;
    }
    
    private class CharIterator implements Iterator<Character>
    {
        // The codes list contains at least one line,
        // so codes.get( 0 ) is always in bounds.
        // The last line of the codes list ends with !
        
        private int         lineInx     = 0;
        private String      currLine    = null;
        private int         charCount   = 0;
        private int         charInx     = 0;
        private Character   nextChar    = null;
        
        /**
         * Constructor.
         * 
         * Precondition: the <em>codes</em> contains at least one element
         * 
         * Precondition: the last line in the <em>codes</em> 
         *               contains a '!'
         */
        public CharIterator()
        {
            nextChar = nextChar();
        }

        /**
         * Returns true if the input sequence has not been exhausted.
         * 
         * @return true if the input sequence has not been exhausted
         */
        @Override
        public boolean hasNext()
        {
            boolean rval    = nextChar != null;
            return rval;
        }

        /**
         * Returns the next character in the input sequence.
         * 
         * @return  the next character in the input sequence
         * 
         * @throws  NoSuchElementException
         *          if the input sequence is exhausted
         */
        @Override
        public Character next() throws NoSuchElementException
        {
            if ( nextChar == null )
            {
                String  message =
                    "Iterator exhausted at line " + lineInx
                    + ", character " + charInx;
                throw new NoSuchElementException( message );
            }
            
            Character   next    = nextChar;
            nextChar = nextChar();
            
            return next;
        }
        
        /**
         * Returns the next character in the input sequence.
         * If the end of the input sequence is reached;
         * i.e., if the next character in the sequence is '!';
         * null is returned.
         * 
         * @return  the next character in the input sequence,
         *          or null if none
         */
        private Character nextChar()
        {
            if ( charInx >= charCount )
            {
                currLine = codes.get( lineInx++ );
                charCount = currLine.length();
                charInx = 0;
                // The list of lines is never empty;
                // no line in the list is ever empty;
                // iteration is over when ! is detected;
                // last line of list always ends with !;
                // ... so currLine is never null; and
                // ... currLine.charAt( 0 ) is always in bounds
            }
            
            Character   result  = null;
            if ( (result = currLine.charAt( charInx++ )) == '!' )
                result = null;
            return result;
        }
    }
}
