package app;

import java.text.ParseException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.johnstraub1954.game_of_life.main.GOLException;

public class GridParserExeerciser
{
    private static final String     endl    = System.lineSeparator();
    
    private static final String     regExp  = "(\\d*?)([$!\\D])";
    private static final Pattern    pattern = Pattern.compile( regExp );

    public static void main(String[] args)
    {
        String[]    lines   =
        {
            "oo10o$9bbb$obob5$",
            "oo10o$9bbb$obob5$o",
            "3ooo5$bbb5$!",
            "3$",
            "$",
            "$!",
            "!",
            ""
        };
        for ( String line : lines )
            testRegExp2( line );
    }
    
    private static void testRegExp( String line )
    {
        Matcher matcher     = pattern.matcher( line );
        
        System.out.println( "Input: " + line + "; " );
        while ( matcher.find() )
        {
            int groupCount  = matcher.groupCount();
            for ( int inx = 1 ; inx <= groupCount ; ++inx )
            {
                String  group   = matcher.group( inx );
                System.out.printf( "(%s)", group );
            }
            System.out.print( " " );
        }
        System.out.println( endl );
    }
    
    private static void testRegExp2( String line )
    {
        System.out.println( "Input: " + line );
        CodeIterator    iter    = new CodeIterator( line );
        while ( iter.hasNext() )
            System.out.print( iter.next() );
        
        System.out.println( endl );
    }
    
    private static class CodeIterator implements Iterator<Character>
    {
        private final String    toParse;
        private final Matcher   matcher;
        private int             charCount;
        private char            nextChar;
        
        public CodeIterator( String toParse )
        {
            this.toParse = toParse;
            matcher = pattern.matcher( toParse );
            parseNext();
        }
        
        @Override
        public boolean hasNext()
        {
            boolean result  = charCount != 0;
            return result;
        }

        @Override
        public Character next()
        {
            if ( charCount == 0 )
            {
                String  message = "Iterator exhausted";
                throw new NoSuchElementException( message );
            }
            
            Character   next    = nextChar;
            if ( --charCount == 0 )
                parseNext();
            
            return next;
        }
        
        private void parseNext()
        {
            System.out.print( ".." );
            if ( !matcher.find() )
                charCount = 0;
            else
            {
                charCount = 1;
                String  strCount    = matcher.group( 1 );
                if ( !strCount.isEmpty() )
                    charCount = Integer.parseInt( strCount );
                
                String  strChar     = matcher.group( 2 );
                if ( strChar.isEmpty() )
                {
                    int             offset  = matcher.end();
                    String          message = 
                        "parse error at offset " + offset;
                    ParseException  exc     = new 
                        ParseException( toParse, offset );
                    throw new GOLException( message, exc );
                }
                nextChar = matcher.group( 2 ).charAt( 0 );
                if ( nextChar == '!' )
                    charCount = 0;
            }
        }
    }
}
