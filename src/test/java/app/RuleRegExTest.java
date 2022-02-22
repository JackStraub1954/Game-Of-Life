package app;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleRegExTest
{
    private static final String  ruleRegEx  = 
        "x\\s*=\\s*(\\d+)\\s*,*\\s*"
        + "y\\s*=\\s*(\\d+)\\s*,*"
        + "\\s*rule\\s*=\\s*"
        + "(\\d+)\\s*/\\s*(\\d+)";
    
    /**
     * Compiled pattern for parsing rules
     * 
     * @see #ruleRegEx
     * @see #parseHeader(String, BufferedReader)
     */
    private static final Pattern    rulePattern = 
        Pattern.compile( ruleRegEx, Pattern.CASE_INSENSITIVE );
    
    public static void main(String[] args)
    {
        test( "x = 36, y = 9, rule = 23/3" );
        test( "x = 36 , y = 9 , rule = 23/3" );
        test( "x = 5 y = 10 rule = 23/3" );
        test( "x=5 y=10 rule=23/3" );
        test( "x  =  5  y  =  10  rule  =  23/3" );
        test( "x  =  5  y  =  10  rule  =  23/3   " );
    }

    public static void test( String rule )
    {
        Matcher matcher     = rulePattern.matcher( rule );
        boolean found       = matcher.find();
        System.out.println( rule );
        System.out.println( "found = " + found );
        if ( found )
        {
            int groupCount  = matcher.groupCount();
            for ( int inx = 1 ; inx <= groupCount ; ++inx )
                System.out.printf( "%d. %s%n", inx, matcher.group( inx ) );
        }
    }
}
