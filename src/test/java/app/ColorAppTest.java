package app;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import javax.swing.JColorChooser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import test_utils.ComponentLocator;

@ExtendWith(MockitoExtension.class)
class ColorAppTest
{
    private static Color    color;
    private static Robot    robbie;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        robbie = new Robot();
    }

    @BeforeEach
    void setUp() throws Exception
    {
    }

    @Test
    void testMain() throws AWTException
    {
        test( 0 );
        test( 1 );
        test( 2 );
        test( 3 );
    }
    
    private static void test( int count )
    {
        startChooser();
        robbie.keyPress( KeyEvent.VK_TAB );
        robbie.delay( 100 );
        robbie.keyPress( KeyEvent.VK_RIGHT );
        robbie.delay( 100 );
        
        // seriously, how is this better than a traditional for loop?
        Stream
            .iterate( 0 , n -> n + 1 )
            .limit( count )
//            .map( n -> tweak( n ) )
            .forEach( n -> robbie.keyPress( KeyEvent.VK_DOWN ) );

        robbie.keyPress( KeyEvent.VK_SPACE );
        robbie.keyPress( KeyEvent.VK_ENTER );
        robbie.delay( 100 );
        System.out.println( "from app: " + color );
    }
    
    private static void startChooser()
    {
        Thread  thread  = new Thread(
            () -> 
            color = JColorChooser.showDialog( null, "Test", Color.GREEN ) 
        );
        thread.start();
        
        // wait for color chooser to become visible,
        // then wait a little longer
        // (why wait longer? how to eliminate random wait?)
        BiPredicate<Component,Object> pred    = 
            (c,o) -> (c instanceof JColorChooser) && c.isVisible();
        while ( ComponentLocator.getComponent( pred, null) == null )
            robbie.delay( 10 );
        robbie.delay( 250 );
    }
    
    private static int tweak( int n )
    {
//        robbie.delay( 100 );
        return n;
    }
}
