package app;

import java.util.Timer;
import java.util.TimerTask;

public class TimerExerciser
{
    private static Timer    timer   = new Timer();

    public static void main(String[] args)
    {
        schedule( new MyTimerTask( 1000 ) );
        schedule( new MyTimerTask( 500 ) );
        schedule( new MyTimerTask( 250 ) );
    }
    
    private static void schedule( MyTimerTask task )
    {
        long    millis  = task.getMillis();
        timer.schedule( task, 0, millis );
    }

    private static class MyTimerTask extends TimerTask
    {
        private final long millis;
        
        public MyTimerTask( long millis )
        {
            this.millis = millis;
        }
        
        @Override
        public void run()
        {
            double  dMillis = millis / 1000.;
            System.out.printf( "%.2f seconds%n", dMillis );
            
        }
        
        public long getMillis()
        {
            return millis;
        }
    }
}
