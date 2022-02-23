package app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GridParserIterExerciser
{
    private static final String     endl    = System.lineSeparator();
    
    private static final String     regExp  = "(\\d*?)([$!\\D])";
    private static final Pattern    pattern = Pattern.compile( regExp );

    public static void main(String[] args)
    {
        String[]    lines   =
        {
            "oo10o$9bbb$obob555$",
            "oo10o$9bbb$obob555$o",
            "3ooo5$bbb5$!",
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
        Matcher matcher     = pattern.matcher( line );
        
        System.out.println( "Input: " + line + "; " );
        while ( matcher.find() )
        {
            int     count       = 1;
            String  sym         = null;
            String  strCount    = matcher.group( 1 );
            if ( !strCount.isEmpty() )
                count = Integer.parseInt( strCount );
            sym = matcher.group( 2 );
            for ( int inx = 0 ; inx < count ; ++inx )
            {
                System.out.print( sym );
            }
            System.out.print( "   " );
        }
        System.out.println( endl );
    }
}
