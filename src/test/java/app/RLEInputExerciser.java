package app;

import java.awt.Point;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.gmail.johnstraub1954.game_of_life.main.GOLException;
import com.gmail.johnstraub1954.game_of_life.main.RLEGridDecoder;
import com.gmail.johnstraub1954.game_of_life.main.RLEInput;

public class RLEInputExerciser
{

    public RLEInputExerciser()
    {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        String      resourceName    = "GosperGlider.rle";
        ClassLoader loader          = RLEInput.class.getClassLoader();
        InputStream inStream        = 
            loader.getResourceAsStream( resourceName );
        if ( inStream == null )
        {
            String  message = 
                "Failed to load rle file: " + resourceName;
            throw new GOLException( message );
        }
        
        RLEInput        input           = new RLEInput( inStream );
        String          name            = input.getName();
        String          author          = input.getAuthor();
        Point           origin          = input.getUpperLeft();
        List<Integer>   survivalStates  = input.getSurvivalRules();
        List<Integer>   birthStates     = input.getBirthRules();
        int             gameStates      = input.getGameStates();
        System.out.println( "name=" + name );
        System.out.println( "author=" + author );
        System.out.println( "origin=" + origin );
        System.out.println( "survivalStates=" + survivalStates );
        System.out.println( "birthStates=" + birthStates );
        System.out.println( "gameStates=" + gameStates );
        
        RLEGridDecoder      decoder = input.getGridDecoder();
        Iterator<Character> iter    = decoder.iterator();
        while ( iter.hasNext() )
            System.out.print( iter.next() );
    }

}
