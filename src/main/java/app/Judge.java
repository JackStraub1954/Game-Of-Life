package app;

import com.gmail.johnstraub1954.game_of_life.main.Parameters;

public class Judge
{
    public static void main(String[] args)
    {
        Parameters params = Parameters.INSTANCE;
        System.out.println( params.getGridCellSize() );
    }

}
