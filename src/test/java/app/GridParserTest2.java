package app;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GridParserTest2
{
    private static final String endl    = System.lineSeparator();
    
    private static final String  regExp      = "(.*?)[\\$!]";

    private static final String  regExp2     = "";
    
    private static final Pattern pattern     = 
        Pattern.compile( regExp, Pattern.CASE_INSENSITIVE );

    public static void main(String[] args)
    {
        String  buff1   =
            "ooo$bbb$obob$" + endl
            + "ooobbb" + endl
            + "ooo$" + endl
            + "bbb!";
        String  buff2   =
            "3o$3b$obob$" + endl
            + "3o3b" + endl
            + "5o$" + endl
            + "5b!";
        try (
            ByteArrayInputStream    inStream        = 
                new ByteArrayInputStream( buff2.getBytes() );
            InputStreamReader       streamReader    =
                new InputStreamReader( inStream );
            BufferedReader          bufReader   =
                new BufferedReader( streamReader );
        )
        {
            testRegExp( bufReader );
        }
        catch ( IOException exc )
        {
            System.err.println( exc.getMessage() );
            exc.printStackTrace();
            System.exit( 1 );
        }
            
    }
    
    private static void testRegExp( BufferedReader reader )
        throws IOException
    {
        StringBuilder   bldr        = new StringBuilder();
        List<String>    parsedLines = new ArrayList<>();
        boolean         done        = false;
        String          line        = null;
        while ( !done && (line = reader.readLine()) != null )
        {
            int inx = -1;
            if ( (inx = line.indexOf( '!' )) != -1 )
            {
                line = line.substring( 0, inx );
                done = true;
            }
            bldr.append( line );
        }
        String  input       = bldr.toString();
        Matcher matcher     = pattern.matcher( input );
        int end = 0;
        while ( matcher.find() )
        {
            parsedLines.add( matcher.group( 1 ) );
            end = matcher.end();
        }
        
        System.out.println( "Input: " + input );
        String  last    = input.substring( end );
        if ( !last.isEmpty() )
            parsedLines.add( last );
        
        for ( String str : parsedLines )
            System.out.println( "    " + str );
    }
}
