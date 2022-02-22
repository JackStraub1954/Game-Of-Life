package app;

import java.io.InputStream;

import com.gmail.johnstraub1954.game_of_life.main.GOLException;
import com.gmail.johnstraub1954.game_of_life.main.RLEGridDecoder;
import com.gmail.johnstraub1954.game_of_life.main.RLEInput;

public class RLEParserTest
{
    private static final String resourceName    = "GosperGlider.rle";
    public static void main(String[] args)
    {
        RLEInput    rleInput    = null;
        ClassLoader loader      = RLEParserTest.class.getClassLoader();
        InputStream inStream    = 
            loader.getResourceAsStream( resourceName );
        if ( inStream == null )
        {
            String  message = 
                "Failed to load audio clip file: " + resourceName;
            throw new GOLException( message );
        }
        rleInput = new RLEInput( inStream );
        System.out.println( "author=" + rleInput.getAuthor() );
        System.out.println( "name=" + rleInput.getName() );
        System.out.println( "survival rules=" + rleInput.getSurvivalRules() );
        System.out.println( "birth rules=" + rleInput.getBirthRules() );
        System.out.println( "origin=" + rleInput.getUpperLeft() );
        RLEGridDecoder  decoder = rleInput.getGridDecoder();
        for ( Character ccc : decoder )
            System.out.print( ccc );
        System.out.println();
    }

}
