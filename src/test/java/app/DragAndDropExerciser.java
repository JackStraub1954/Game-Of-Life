package app;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class DragAndDropExerciser
{
    private final JTextField    textField   = new JTextField( 25 );
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new DragAndDropExerciser().run() );
    }
    
    public void run()
    {
        JFrame  frame   = new JFrame( "Drag and Drop Exerciser" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( new DropPanel() );
        frame.pack();
        frame.setVisible( true );
    }

    private class DropPanel extends JPanel
    {
        public DropPanel()
        {
            Dimension   dim = new Dimension( 500, 500 );
            setPreferredSize( dim );
            
            textField.setDragEnabled( true );
            add( textField );
            DropTarget  target  = 
                new DropTarget( textField, new TextDropTarget() );
        }
    }
    
    private class TextDropTarget implements DropTargetListener
    {
        @Override
        public void dragEnter(DropTargetDragEvent e)
        {
        }
        
        @Override
        public void dragExit(DropTargetEvent e)
        {
        }
        
        @Override
        public void dragOver(DropTargetDragEvent e)
        {
        }
        
        @Override
        public void dropActionChanged(DropTargetDragEvent e)
        {
        
        }
        
        @Override
        public void drop(DropTargetDropEvent evt )
        {
            try
            {
                evt.acceptDrop(DnDConstants.ACTION_COPY );
                DataFlavor      flavor          =
                    DataFlavor.javaFileListFlavor;
                Transferable    transferable    = evt.getTransferable();
                Object          obj             =
                    transferable.getTransferData( flavor );
                List<?>         list            = (List<?>)obj;
                if ( list.size() > 0 && list.get( 0 ) instanceof File )
                {
                    File    file    = (File)list.get( 0 );
                    textField.setText( file.toString() );
                    dumpFile ( file );
                    Object  src = evt.getSource();
                    System.out.println( src );
                    for ( Object item : list )
                        System.out.println( item );
                }
                else
                    textField.setText( "??? ERROR ???" );
            }
            catch ( IOException | UnsupportedFlavorException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
        }
    }
    
    private void dumpFile( File file )
    {
        try ( 
            FileReader  fileReader  = new FileReader( file );
            BufferedReader bufReader = new BufferedReader( fileReader );
        )
        {
            bufReader.lines().forEach( System.out::println );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
}
