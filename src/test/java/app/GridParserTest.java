package app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GridParserTest
{
    private static final String endl    = System.lineSeparator();
    
    private static final String  regExp      = "(.*?)[\\$!]";

    private static final String  regExp2     = "";
    
    private static final Pattern pattern     = 
        Pattern.compile( regExp, Pattern.CASE_INSENSITIVE );

    public static void main(String[] args)
    {
        String[]    lines   =
        {
            "ooo$bbb$obob$",
            "ooo$bbb$$",
            "ooo$bbb",
            "obobo",
            "bb"
        };
        for ( String line : lines )
            testRegExp( line );
    }
    
    private static void testRegExp( String line )
    {
        Matcher matcher     = pattern.matcher( line );
        
        System.out.println( "In: " + line + "; " );
        int count   = 1;
        while ( matcher.find() )
        {
            String  group   = matcher.group( 1 );
            System.out.print( "    " + count++ + ". " + group );
            System.out.println( " (" + matcher.end() + ")" );
        }
    }
    
    private static void testSplit( String line )
    {
        String[]    strs    = line.split( "b" );
        int         count   = strs.length;
        System.out.println( "In: " + line );
        System.out.println( "Out: " + count );
        for ( int inx = 0 ; inx < count ; ++inx )
        {
            String  fmt = "%6d. %s";
            String  str = String.format( fmt, inx, strs[inx] );
            System.out.println( str );
        }
    }

}
