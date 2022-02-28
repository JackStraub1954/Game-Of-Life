package com.gmail.johnstraub1954.game_of_life.main;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.CTRL_CENTER_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_ORIGIN_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_SIZE_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_SHOW_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_WIDTH_PN;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JPanel;

/**
 * Encapsulates the physical grid that displays the state
 * of live and dead cells.
 * 
 * @author Jack Straub
 *
 */
public class Grid extends JPanel implements PropertyChangeListener
{
    
    private final   Parameters  params              = Parameters.INSTANCE;
    
    private GridMap         gridMap             = params.getGridMap(); 
    
    private Color           gridColor           = params.getGridColor();
    private boolean         gridLineShow        = params.isGridLineShow();
    private int             gridLineWidth       = params.getGridLineWidth();
    private Color           gridLineColor       = params.getGridLineColor();
    private int             gridCellSize        = params.getGridCellSize();
    private Color           gridCellColor       = params.getGridCellColor();
    
    private boolean         gridCellCenter      = params.isGridCenter();
    private Point           gridCellTop         = params.getGridCellOrigin();
    private Point           gridCellULC         = calculateULC();
//    private int             gridCellTopX        = 10;
//    private int             gridCellTopY        = 20;
    
    /**
     * Graphics context for use during painting.
     * Must be refreshed each time paintComponent is invoked.
     * Made global just to facilitate painting in multiple sub-methods.
     */
    private Graphics2D  gtx                 = null;
    
    /**
     * Default constructor.
     */
    public Grid()
    {
        params.addPropertyChangeListener( this );
        params.addActionListener( e -> repaint() );
    }

    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        String  propName    = evt.getPropertyName();
        Object  newValue    = evt.getNewValue();
        switch ( propName )
        {
        case GRID_COLOR_PN:
            gridColor = (Color)newValue;
            break;
        case GRID_LINE_SHOW_PN:
            gridLineShow = (Boolean)newValue;
            break;
        case GRID_LINE_WIDTH_PN:
            gridLineWidth = (Integer)newValue;
            break;
        case GRID_LINE_COLOR_PN:
            gridLineColor = (Color)newValue;
            break;
        case GRID_CELL_SIZE_PN:
            gridCellSize = (Integer)newValue;
            break;
        case GRID_CELL_COLOR_PN:
            gridCellColor = (Color)newValue;
            break;
        case GRID_CELL_ORIGIN_PN:
            gridCellULC = (Point)newValue;
            break;
        case CTRL_CENTER_PN:
            gridCellCenter = (boolean)newValue;
            gridCellULC = calculateULC();
            break;
        default:
            break;
        }
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        gridCellULC = calculateULC();
        gtx = (Graphics2D)graphics.create();
        gtx.setRenderingHint( 
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        
        int width   = getWidth();
        int height  = getHeight();
        gtx.setColor( gridColor );
        gtx.fillRect( 0,  0, width, height );
        
        if ( gridLineShow )
        {
            Stroke  stroke  = new BasicStroke( gridLineWidth );
            gtx.setStroke( stroke );
            gtx.setColor( gridLineColor );
            GridLineIterator    iter    = new GridLineIterator();
            while ( iter.hasNext() )
                gtx.draw( iter.next() );
        }
        
        gtx.setColor( gridCellColor );
        LiveCellIterator    cellIter    = 
            new LiveCellIterator( gridCellULC );
        while ( cellIter.hasNext() )
            gtx.fill( cellIter.next() );
        
        gtx.dispose();
        gtx = null;
    }
    
    private Point calculateULC()
    {
        Point   topLeft = gridCellTop;
        if ( gridCellCenter )
        {
            Rectangle   rect    = gridMap.getLiveRectangle();
            int         centerX = rect.x + rect.width / 2;
            int         centerY = rect.y + rect.height / 2;
            int         width   = getWidth() / gridCellSize;
            int         height  = getHeight() / gridCellSize;
            int         xco     = centerX - width / 2;
            int         yco     = centerY - height / 2;
            topLeft = new Point( xco, yco );
        }
        
        return topLeft;
    }
    
    private class GridLineIterator implements Iterator<Line2D>
    {
        private final Line2D.Float      line;
        
        private final Point2D.Float     ulCorner;   // upper left corner
        private final Point2D.Float     lrCorner;   // lower right corner
        
        private Point2D.Float   nextStart;
        private Point2D.Float   nextEnd;
        private Direction       dir;
        
        public GridLineIterator()
        {
            int     width   = getWidth();
            int     height  = getHeight();
            
            float   ulcX    = 0;//gridMarginLeft;
            float   ulcY    = 0;//gridMarginTop;
            ulCorner = new Point2D.Float( ulcX, ulcY );
            
            float   lrcX    = 
                ulcX + width;// - gridMarginRight - gridMarginLeft;
            float   lrcY    =
                ulcY + height;// - gridMarginBottom - gridMarginTop;
            
            lrCorner = new Point2D.Float( lrcX, lrcY );
            nextStart = new Point2D.Float( ulCorner.x, ulCorner.y );
            nextEnd = new Point2D.Float( ulCorner.x, lrCorner.y );
            
            line = new Line2D.Float();
            dir = Direction.SOUTH;
        }
        
        @Override
        public boolean hasNext()
        {
            boolean result  = nextStart.y <= lrCorner.y;
            return result;
        }

        @Override
        public Line2D next()
        {
            if ( nextStart.y > lrCorner.y )
            {
                String  message = "(" + nextStart + ") : (" + nextEnd + ")";
                throw new NoSuchElementException( message );
            }
            
            line.x1 = nextStart.x;
            line.y1 = nextStart.y;
            line.x2 = nextEnd.x;
            line.y2 = nextEnd.y;
            
            if ( dir == Direction.SOUTH )
            {
                nextStart.x += gridCellSize;
                nextEnd.x = nextStart.x;
                if ( nextStart.x > lrCorner.x )
                {
                    dir = Direction.EAST;
                    nextStart.x = ulCorner.x;
                    nextStart.y = ulCorner.y;
                    nextEnd.x = lrCorner.x;
                    nextEnd.y = nextStart.y;
                }
            }
            else
            {
                nextStart.y += gridCellSize;
                nextEnd.y = nextStart.y;
            }
            return line;
        }
        
    }
    
    private class LiveCellIterator implements Iterator<Rectangle>
    {
        private final   Iterator<Cell>  cellIterator;
        private final   Point           startCell;
        
        public LiveCellIterator( Point ulcCell )
        {
            startCell = ulcCell;
            
            int     widthPixels     = getWidth();
            int     widthCells      = widthPixels / gridCellSize;
            int     heightPixels    = getHeight();
            int     heightCells     = heightPixels / gridCellSize;
            int     xco             = ulcCell.x;
            int     yco             = ulcCell.y;
            
            // Rectangle enclosing all visible cells.
            Rectangle rect    = 
                new Rectangle( xco, yco, widthCells, heightCells );
            cellIterator = gridMap.iterator( rect );
        }

        @Override
        public boolean hasNext()
        {
            return cellIterator.hasNext();
        }

        @Override
        public Rectangle next() throws NoSuchElementException
        {
            if ( !cellIterator.hasNext() )
            {
                String  message = "Iterator exhausted";
                throw new NoSuchElementException( message );
            }
            
            Cell        next    = cellIterator.next();
            Point       point   = next.getPoint();
            int         xco     = (point.x - startCell.x) * gridCellSize;
            int         yco     = (point.y - startCell.y) * gridCellSize;
            Rectangle   rect    = 
                new Rectangle( xco, yco, gridCellSize, gridCellSize );
            return rect;
        }        
    }
}
