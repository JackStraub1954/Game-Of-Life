package com.gmail.johnstraub1954.game_of_life.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

public class HSeparator extends JComponent
{
    private final float     percentWidth;
    public HSeparator()
    {
        this( 1 );
    }
    
    public HSeparator( float percentWidth )
    {
        this.percentWidth = percentWidth;
        
        // have to start out with non-null preferred size,
        // otherwise initial dimensions will be 0/0
        int         width   = 0;
        int         height  = 10;
        Dimension   size    = new Dimension( width, height );
        setPreferredSize( size );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        
        Graphics2D  gtx     = (Graphics2D)graphics.create();
        Container   parent  = getParent();
        int         width   = parent.getWidth();
        int         height  = 10;
        Dimension   size    = new Dimension( width, height );
        this.setSize( size );
        
        gtx.setColor( parent.getBackground() );
        gtx.fillRect( 0,  0,  width,  height );
        
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
        gtx.setStroke( new BasicStroke( 1 ) );
        gtx.draw( line );
    }
}
