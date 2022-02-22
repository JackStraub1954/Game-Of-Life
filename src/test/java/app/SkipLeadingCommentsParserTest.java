package app;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkipLeadingCommentsParserTest
{
    private static final String endl    = System.lineSeparator();
    
    private static final String  skipLeadingRegEx   = "(?:.*?)([$!ob]*.*)";
    private static final String  skipCommentRegEx   = "(?^#.*$)";
    private static final int     skipLeadingFlags   = 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL;
    private static final Pattern skipLeadingPattern = 
        Pattern.compile( skipLeadingRegEx, skipLeadingFlags );
    private static final int     nabCommentFlags    = 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL;
    private static final Pattern nabCommentPattern  = 
        Pattern.compile( skipLeadingRegEx, skipLeadingFlags );
    
    private static int  counter = 0;

    public static void main(String[] args)
    {
        String  buff1   =
            endl + endl + "#" +"#"
            + "!$ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb!";
        testNab( buff1 );
        testNab( "" );
        testNab( "#" + endl + "#" + endl );
    }
    
    private static void testRegExp( String buf )
    {
        Matcher matcher     = skipLeadingPattern.matcher( buf );
        if ( matcher.find() )
        {
            int     groupCount  = matcher.groupCount();
            System.out.println( "group count = " + groupCount );
            for ( int inx = 1 ; inx <= groupCount ; ++inx )
            {
                String  group   = matcher.group( inx );
                System.out.printf( "%d. %s%n", inx, group );
            }
        }
        else
        {
            System.out.println( "not found" );
        }
    }
    
    private static void testNab( String buf )
    {
        try (
            ByteArrayInputStream    inStream        = 
                new ByteArrayInputStream( buf.getBytes() );
            InputStreamReader       streamReader    =
                new InputStreamReader( inStream );
            BufferedReader          bufReader   =
                new BufferedReader( streamReader );
        )
        {
            StringBuilder   bldr    = new StringBuilder();
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
    }
}
