package com.gmail.johnstraub1954.game_of_life.main;

import java.awt.Component;
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.text.JTextComponent;

public class URLManager implements DropTargetListener
{
    private static final JFileChooser   fileChooser = new JFileChooser();
    private static final Parameters     params      = Parameters.INSTANCE;
    private final JTextComponent        textComponent;
    private URL                         url         = null;
    
    public URLManager( JTextComponent textComponent )
    {
        this.textComponent = textComponent;
        textComponent.setDragEnabled( true );
        new DropTarget( textComponent, this );
    }
    
    public URL getURL() throws GOLException
    {
        return url;
    }
    
    public int selectFile( Component parent )
    {
        int rcode   = 
            fileChooser.showDialog( parent, JFileChooser.APPROVE_SELECTION );
        if ( rcode == JFileChooser.APPROVE_OPTION )
        {
            File    file    = fileChooser.getSelectedFile();
            try
            {
                url = file.toURI().toURL();
            }
            catch ( MalformedURLException exc )
            {
                String  message = 
                    "Failed to convert file to URL: "+ file.getName();
                throw new GOLException( message, exc );
            }
            textComponent.setText( url.toString() );
            params.setGridURL( url );
        }
        return rcode;
    }
    
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
                url = getLocation( file );
                String  text    = url == null ? "none" : url.toString();
                textComponent.setText( text );
                params.setGridURL( url );
            }
            else
                textComponent.setText( "??? ERROR ???" );
        }
        catch ( IOException | UnsupportedFlavorException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
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
                // Note: file.toURL is deprecated
                URI uri = file.toURI();
                url = uri.toURL();
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
}
