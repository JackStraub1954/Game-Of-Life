package app;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.game_of_life.components.GridFrame;
import com.gmail.johnstraub1954.game_of_life.components.PreferencesDialog;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;

import test_utils.TestUtils;

public class AdHocExerciser
{
    public static void main(String[] args)
    {
        PreferencesDialog   prefDialog  = new PreferencesDialog();
        GridFrame           frame       = new GridFrame();
        try
        {
            SwingUtilities.invokeAndWait( () -> frame.run() );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
        }
        
        Parameters.INSTANCE.setGridColor( Color.RED );
        prefDialog.setVisible( true );
        TestUtils.pause( 2000 );
        Parameters.INSTANCE.setGridColor( Color.BLUE );
        TestUtils.pause( 2000 );
        prefDialog.setVisible( true );
    }
}
