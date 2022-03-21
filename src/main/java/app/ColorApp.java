package app;

import java.awt.Color;

import javax.swing.JColorChooser;

public class ColorApp
{
    public static void main(String[] args)
    {
        for ( int inx = 0 ; inx < 4 ; ++inx )
        {
            Color   color   = 
                JColorChooser.showDialog(null, "Mockito Demo", Color.RED );
            System.out.println( "***** " + color );
        }   
    }
}
