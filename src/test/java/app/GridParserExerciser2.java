package app;

import java.text.ParseException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.johnstraub1954.game_of_life.main.GOLException;

public class GridParserExerciser2
{
    private static final String     endl    = System.lineSeparator();
    
    private static final String  ruleRegEx1 = 
        "x\\s*=\\s*(\\d+)\\s*,*\\s*"
        + "y\\s*=\\s*(\\d+)\\s*,*"
        + "\\s*rule\\s*=\\s*"
        + "(\\d+)\\s*/\\s*(\\d+)";
    private static final Pattern    pattern1 = Pattern.compile( ruleRegEx1 );
    
    private static final String  ruleRegEx2  = 
        "x\\s*=\\s*(\\d+)\\s*,*\\s*"
        + "y\\s*=\\s*(\\d+)\\s*,*"
        + "\\s*rule\\s*=\\s*"
        + "([sb]){0,1}(\\d+)\\s*/\\s*([sb]){0,1}(\\d+)";
    private static final Pattern    pattern2 = Pattern.compile( ruleRegEx2 );
    
    private static int count = 0;

    public static void main(String[] args)
    {
        String[]    lines1  =
        {
            "x = 5   y = 10   rule = 23/3",
            "x = 5   y = 10   rule = 23 / 3",
            "x = 5,   y = 10   rule = 23/3",
            "x = 5,   y = 10,   rule = 23/3",
            "x=5y=10rule=23/3",
            "x=5,y=10rule=23/3",
            "x=5,y=10,rule=23/3",
        };
        for ( String line : lines1 )
            testRegExp2( line );
    
        System.out.println( endl + "***********************" );
        
        String[]    lines2  =
        {
            "x = 5   y = 10   rule = 23/3",
            "x = 5   y = 10   rule = 23 / 3",
            "x = 5   y = 10   rule = s23/b3",
            "x = 5   y = 10   rule = b23/s3",
        };
        for ( String line : lines2 )
            testRegExp2( line );
    }
    
    private static void testRegExp1( String line )
    {
        Matcher matcher     = pattern1.matcher( line );
        
        System.out.printf( "%2d. Input: %s%n", ++count, line );
        if ( !matcher.find() )
            System.out.println( "no match" );
        else
        {
            int groupCount  = matcher.groupCount();
            for ( int inx = 1 ; inx <= groupCount ; ++inx )
            {
                String  group   = matcher.group( inx );
                System.out.printf( "(%s)", group );
            }
            System.out.println();
        }
    }
        
    private static void testRegExp2( String line )
    {
        Matcher matcher     = pattern2.matcher( line );
        
        System.out.printf( "%2d. Input: %s%n", ++count, line );
        if ( !matcher.find() )
            System.out.println( "no match" );
        else
        {
            int groupCount  = matcher.groupCount();
            for ( int inx = 1 ; inx <= groupCount ; ++inx )
            {
                String  group   = matcher.group( inx );
                System.out.printf( "(%s)", group );
            }
            System.out.println();
        }
    }
}
