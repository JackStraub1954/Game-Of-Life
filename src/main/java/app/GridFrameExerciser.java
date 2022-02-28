package app;

import java.awt.Point;
import java.io.InputStream;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.game_of_life.main.Cell;
import com.gmail.johnstraub1954.game_of_life.main.GOLException;
import com.gmail.johnstraub1954.game_of_life.main.GridFrame;
import com.gmail.johnstraub1954.game_of_life.main.GridMap;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;
import com.gmail.johnstraub1954.game_of_life.main.RLEGridDecoder;
import com.gmail.johnstraub1954.game_of_life.main.RLEInput;

public class GridFrameExerciser
{
    private static final Parameters params  = Parameters.INSTANCE;
    
    public static void main(String[] args)
    {
//        String      resourceName    = "GosperGlider.rle";
//        String      resourceName    = "HerschelClimber.rle";
        String      resourceName    = "pp8primecalculator.rle";
//        String      resourceName    = "rats.rle";
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
        params.setGridCellOrigin( input.getUpperLeft() );
        params.setSurvivalStates( input.getSurvivalRules() );
        params.setBirthStates( input.getBirthRules() );
        params.setGridMap( getGridMap( input ) );

        SwingUtilities.invokeLater( () -> new GridFrame().run() );
    }
    
    private static GridMap getGridMap( RLEInput input )
    {
        Point               origin  = input.getUpperLeft();
        RLEGridDecoder      decoder = input.getGridDecoder();
        Iterator<Character> iter    = decoder.iterator();
        GridMap             gridMap = new GridMap();
        int currX   = origin.x;
        int currY   = origin.y;
        while ( iter.hasNext() )
        {
            char    nextChar    = iter.next();
            if ( nextChar == '$' )
            {
                ++currY;
                currX = origin.x;
            }
            else
            {
                boolean state   = nextChar == 'o';
                Cell    cell    = new Cell( currX++, currY, state );
                gridMap.put( cell );
            }
        }
        
        return gridMap;
    }
}
