package app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParserTest
{
    static final String  regExp      = 
        "x\\s*=\\s*(\\d+)"
        + "\\s*,\\s*"
        + "y\\s*=\\s*(\\d+)"
        + "\\s*rule\\s*=\\s"
        + "(\\d+)\\s*/\\s(\\d+)";

    static final String  regExp2     = 
        "x\\s*=\\s*(\\d+)"
        + "\\s*,\\s*"
        + "y\\s*=\\s*(\\d+)"
        + "\\s*rule\\s*=\\s*"
        + "(\\d+)\\s*/\\s*(\\d+)"
        + "(?:\\s*/\\s*(\\d+)){0,1}";

    static final Pattern pattern     = 
        Pattern.compile( regExp2, Pattern.CASE_INSENSITIVE );

    public static void main(String[] args)
    {
        String[]    lines   =
        {
            "x = 5 , y = 6 rule = 23 / 3",
            "x=5,y=6  rule  = 23/3",
            "x = 5 , y = 6 rule = 23 / 3 / 1",
            "x=5,y=6 rule=23/3/1",
        };
        for ( String line : lines )
            test( line );
    }
    
    private static void test( String line )
    {
        Matcher matcher     = pattern.matcher( line );
        boolean found       = matcher.find();
        
        System.out.print( "In: " + line + "; " );
        if ( !found )
            System.out.println( "not found" );
        else
        {
            System.out.println( "found" );
            int     matchCount  = matcher.groupCount();
            if ( matchCount < 5 || matchCount > 6 )
                throw new Error( "invalid match count: " + matchCount );
            for ( int inx = 1 ; inx <= matchCount ; ++inx )
            {
                String  group   = matcher.group( inx );
                System.out.println( "    " + inx + ". " + group );
            }
        }
    }

}
