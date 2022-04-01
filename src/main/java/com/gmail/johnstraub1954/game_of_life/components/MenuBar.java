package com.gmail.johnstraub1954.game_of_life.components;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar
{
    private final PreferencesDialog preferencesDialog   = 
        new PreferencesDialog();
    
    private final SaveDialog        saveDialog          = new SaveDialog();
    private final JFileChooser      fileChooser         = new JFileChooser();
    
    private final JMenu fileMenu;
    private final JMenu windowMenu;
    
    public MenuBar()
    {
        final int   ctrlMask    = ActionEvent.CTRL_MASK;
        KeyStroke   keyStroke   = null;
        
        fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic( KeyEvent.VK_F );
        
        JMenuItem   newItem     = new JMenuItem( "New", KeyEvent.VK_N );
        keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_N, ctrlMask );
        newItem.setAccelerator( keyStroke );
        fileMenu.add( newItem );
        
        JMenuItem   openItem    = new JMenuItem( "Open", KeyEvent.VK_O );
        keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_O, ctrlMask );
        openItem.setAccelerator( keyStroke );
        fileMenu.add( openItem );

        JMenuItem   saveItem    = new JMenuItem( "Save", KeyEvent.VK_S );
        keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_S, ctrlMask );
        saveItem.setAccelerator( keyStroke );
        fileMenu.add( saveItem );

        JMenuItem   saveAsItem  = new JMenuItem( "Save As", KeyEvent.VK_A );
        keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_A, ctrlMask );
        saveAsItem.setAccelerator( keyStroke );
        saveAsItem.addActionListener( e -> saveDialog.setVisible( true ) );
        fileMenu.add( saveAsItem );

        add( fileMenu );
        
        windowMenu = new JMenu( "Window" );
        windowMenu.setMnemonic( KeyEvent.VK_W );
        JMenuItem   preferencesItem     = 
            new JMenuItem( "Preferences", KeyEvent.VK_P );
        keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_P, ctrlMask );
        preferencesItem.setAccelerator( keyStroke );
        preferencesItem.addActionListener( 
            e -> preferencesDialog.setVisible( true ) );
        windowMenu.add( preferencesItem );
        add( windowMenu );
    }
    
    private void saveAs()
    {
        int result  = fileChooser.showSaveDialog( null );
        if ( result == JFileChooser.APPROVE_OPTION )
        {
            
        }
    }
}
