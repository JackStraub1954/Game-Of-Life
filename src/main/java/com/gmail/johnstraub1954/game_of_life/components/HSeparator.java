package com.gmail.johnstraub1954.game_of_life.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

public class HSeparator extends JComponent
{
    private static final Stroke stroke1     = new BasicStroke( 1 );
    private static final float  defWeight   = 1;
    private static final int    defHeight   = 10;
    
    private final boolean   invisible;
    private final float     percentWidth;
    private final int       height;
    
    public HSeparator()
    {
        this( defWeight, defHeight, false );
    }
    
    public HSeparator( float percentWidth )
    {
        this( percentWidth, defHeight, false );
    }
    
    public HSeparator( int height, boolean invisible )
    {
        this( defWeight, height, invisible );
    }
    
    public HSeparator( boolean invisible )
    {
        this( defWeight, defHeight, invisible );
    }
    
    public HSeparator( float percentWidth, int height, boolean invisible )
    {
        this.percentWidth = percentWidth;
        this.invisible = invisible;
        this.height = height;
        
        // have to start out with non-null preferred size,
        // otherwise initial dimensions will be 0/0
        int         width   = 0;
        Dimension   size    = new Dimension( width, height );
        setPreferredSize( size );
    }
    
    /**
     * Copy constructor.
     * Useful for making many separators with the same properties.
     * 
     * @param toCopy    the separator to copy
     */
    public HSeparator( HSeparator toCopy )
    {
        this.percentWidth = toCopy.percentWidth;
        this.height = toCopy.height;
        this.invisible = toCopy.invisible;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        
        Graphics2D  gtx     = (Graphics2D)graphics.create();
        Container   parent  = getParent();
        int         width   = getWidth();
        Dimension   size    = new Dimension( width, height );
        this.setSize( size );
        
        gtx.setColor( parent.getBackground() );
        gtx.fillRect( 0,  0,  (int)(width * percentWidth),  height );
        
        if ( !invisible )
        {
            float       verticalMidpoint    = height / 2f;
            float       lineLength          = width * percentWidth;
            float       xStart              = (width - lineLength) / 2f;
            float       xEnd                = xStart + lineLength;
            Point2D     start               = 
                new Point2D.Float( xStart, verticalMidpoint );
            Point2D     end                 = 
                new Point.Float( xEnd, verticalMidpoint );
            Line2D      line                = new Line2D.Float( start, end );
            
            gtx.setColor( Color.BLACK );
            gtx.setStroke( stroke1 );
            gtx.draw( line );
        }
    }
}
