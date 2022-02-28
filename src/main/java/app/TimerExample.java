package app;

import java.util.TimerTask;

import com.gmail.johnstraub1954.game_of_life.main.GOLTimer;

public class TimerExample
{
    public static void main(String[] args)
    {
        final String    message = "To be printed every .5 second";
        TimerTask   task    = GOLTimer.INSTANCE.addTask( 
            500, 
            () -> System.out.println( message )
        );
        try
        { 
            Thread.sleep( 2000 ); 
        } 
        catch ( InterruptedException exc) {}
        task.cancel();
        GOLTimer.INSTANCE.dispose();
    }

}
