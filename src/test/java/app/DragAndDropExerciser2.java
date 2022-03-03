package app;

import java.io.File;

import javax.swing.JFileChooser;

public class DragAndDropExerciser2
{
    private final static    JFileChooser    fileChooser = new JFileChooser();
    
    public static void main(String[] args)
    {
        File    dir = new File( 
            "D:\\Users\\jstra\\Workspaces\\MiscellaneousJavaProjects\\"
            + "GameOfLife\\src\\test\\resources\\"
            );
        fileChooser.setCurrentDirectory( dir );
        
        int     rcode       = fileChooser.showDialog( null, null );
        String  strRcode    = null;
        switch ( rcode )
        {
        case JFileChooser.CANCEL_OPTION:
            strRcode = "cancel ";
            break;
        case JFileChooser.APPROVE_OPTION:
            strRcode = "approve ";
            break;
        default:
            strRcode = "??? ";
            break;
        }
        System.out.println( strRcode + rcode );
        System.out.println( fileChooser.getSelectedFile() );
        
        fileChooser.showDialog( null, null );
    }
    
}
