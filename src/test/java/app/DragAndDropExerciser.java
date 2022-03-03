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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

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
                    URL     url     = getLocation( file );
                    String  text    = url == null ? "null" : url.toString();
                    textField.setText( text );
                    dumpFileToo( file );
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
    
    private URL getLocation( File file )
    {
        URL url = null;
        try
        {
            if ( file.getName().toLowerCase().endsWith( ".url" ) )
                url = parseURL( file );
            else
            {
                URI uri = file.toURI();
                url = uri.toURL();
                System.out.println( "########## " + uri + ", " + url );
                // Note: file.URL is deprecated
            }
        }
        catch ( MalformedURLException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        return url;
    }
    
    private URL parseURL( File file )
    {
        List<String>    lines   = null;
        URL url         = null;
        try ( 
            FileReader  fileReader  = new FileReader( file );
            BufferedReader bufReader = new BufferedReader( fileReader );
        )
        {
            lines = bufReader.lines().collect( Collectors.toList() );
            int size    = lines.size();
            for ( int inx = 0 ; url == null && inx < size ; ++inx )
            {
                String[]    arr = lines.get( inx ).split( "=" );
                if ( arr.length == 2 && arr[0].equalsIgnoreCase( "url" ) )
                    url = new URL( arr[1] );
            }
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        return url;
    }
    
    private void dumpFile( File file )
    {
        List<String>    lines   = null;
        try ( 
            FileReader  fileReader  = new FileReader( file );
            BufferedReader bufReader = new BufferedReader( fileReader );
        )
        {
                lines = bufReader.lines().collect( Collectors.toList() );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        lines.forEach( System.out::println );
    }
    
    private void dumpFileToo( File file )
    {
        List<String>    lines   = null;
        URL             url     = getLocation( file );
        if ( url == null )
        {
            System.err.println( "url == null" );
            System.exit( 1 );
        }
        
        try ( 
            InputStream inStream = url.openStream();
            InputStreamReader inReader = new InputStreamReader( inStream );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
                lines = bufReader.lines().collect( Collectors.toList() );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        lines.forEach( System.out::println );
    }
}
