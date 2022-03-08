package com.gmail.johnstraub1954.game_of_life.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Encapsulates all cells in a grid. 
 * Theoretically, the grid has an infinite extent,
 * and the grid map is infinitely large.
 * Only live cells are physically stored in the map; 
 * any location not represented in the map
 * is assumed to be a valid, but dead, cell.
 * Every valid get invocation returns a physical cell.
 * 
 * To facilitate traversal of a grid,
 * this class can generate a rectangle that contains
 * all live cells.
 * 
 * Note about using a rectangle for a range:
 * 
 * Given a Rectangle with:<br>
 * (x,y) = (5,4), width = 5, height = 5
 * <blockquote>
 * Horizontal line segment (5,4) - (5,8) (inclusive)
 * is in-bounds, (5,9) is not.
 * <br>
 * Vertical line segment (5,4) - (9,4) (inclusive) 
 * is in-bounds, (10,4)) is not
 * </blockquote>
 * 
 * 
 * @author Jack Straub
 *
 */
public class GridMap implements Iterable<Cell>
{
    /** 
     * The map that defines the contents of the map.
     * Virtually speaking, the grid is infinite, and every cell in
     * the grid exists; however, only live cells are physically
     * stored in the map. 
     * The user can assume that any successful <em>get</em> operation
     * returns a live cell; an unsuccessful <em>get</em> operation
     * represents a dead cell.
     */
    private final Map<Point,Boolean>    grid    = new HashMap<>();
    
    /** 
     * Changed to true every time the map is modified.
     * Used to indicate when a grid needs to be saved.
     */
    private boolean modified        = false;
    
    /**
     * Returns the cell corresponding to a given coordinate pair.
     * 
     * @param   xco     the x-coordinate of the target cell
     * @param   yco     the y-coordinate of the target cell
     * 
     * @return the cell corresponding to the given coordinates
     */
    public Cell get( int xco, int yco )
    {
        Point   point   = new Point( xco, yco );
        Cell    cell    = get( point );
        return cell;
    }
    
    /**
     * Returns the cell corresponding to a given point.
     * 
     * @param   point   the location of the target cell
     *  
     * @return the cell corresponding to the given point
     */
    public Cell get( Point point )
    {
        Boolean isAlive = grid.get( point );
        if ( isAlive == null )
            isAlive = false;
        Cell    cell    = new Cell( point, isAlive );
        return cell;
    }
    
    /**
     * Specifies the value of a cell at a given location.
     * The previous value of the cell is returned.
     * 
     * @param xco       the x-coordinate of the given location
     * @param yco       the y-coordinate of the given location
     * @param isAlive   the value of the cell
     * 
     * @return  the previous value of the cell
     */
    public Cell put( int xco, int yco, boolean isAlive )
    {
//        Point   point   = new Point( xco, yco );
//        Cell    cell    = get( point );
//        if ( isAlive )
//            grid.put( point, true );
//        else
//            grid.remove( point );
        Cell    cell    = put( new Point( xco, yco ), isAlive );
        return cell;
    }
    
    /**
     * Adds a cell to the grid.
     * 
     * @param cell  the cell to add
     * 
     * @return  the previous value of the cell at the given location
     */
    public Cell put( Cell cell )
    {
        Cell    result  = put( cell.getPoint(), cell.isAlive() );

        return result;
    }
    
    /**
     * Specifies the value of a cell at a given point.
     * The previous value of the cell is returned.
     * 
     * @param point     the given point
     * @param isAlive   the value of the cell
     * 
     * @return  the previous value of the cell
     */
    public Cell put( Point point, boolean isAlive )
    {
        Cell    cell    = get( point );
        if ( !isAlive )
            grid.remove( point );
        else
            grid.put( point, true );
        modified = true;
        
        return cell;
    }
    
    /**
     * Return a rectangle that encloses all live cells in the grid.
     * 
     * @return a rectangle that encloses all live cells in the grid
     */
    public Rectangle getLiveRectangle()
    {
        int minX    = Integer.MAX_VALUE;
        int maxX    = Integer.MIN_VALUE;
        int minY    = Integer.MAX_VALUE;
        int maxY    = Integer.MIN_VALUE;
        
        for ( Point point : grid.keySet() )
        {
            int xco = point.x;
            if ( xco < minX )
                minX = xco;
            if ( xco > maxX )
                maxX = xco;

            int yco = point.y;
            if ( yco < minY )
                minY = yco;
            if ( yco > maxY )
                maxY = yco;
        }
        
        int xExtent = maxX - minX + 1;
        int yExtent = maxY - minY + 1;
        Rectangle   rect    = new Rectangle( minX, minY, xExtent, yExtent );
        return rect;
    }
    
    public Iterator<Cell> iterator()
    {
        Iterator<Cell>  iter    = new GenerationIterator();
        return iter;
    }
    
    /**
     * Returns an iterator that traverses every live cell
     * in the map.
     * 
     * @return  an iterator that traverses every live cell
     *          in the map
     */
    public Iterator<Cell> liveIiterator()
    {
        Rectangle       rect    = getLiveRectangle();
        Iterator<Cell>  iter    = new CellIterator( rect );
        return iter;
    }
    
    /**
     * Returns an iterator that traverses every live cell
     * in a given rectangle.
     * 
     * @param rect  the given rectangle
     * 
     * @return  an iterator that traverses every live cell
     *          in the given rectangle
     */
    public Iterator<Cell> iterator( Rectangle rect )
    {
        Iterator<Cell>  iter    = new CellIterator( rect );
        return iter;
    }
    
    /**
     * Resets the state of the grid to "unmodified."
     * Two common uses for this method are:
     * <ul>
     * <li>Immediately after a fresh game has been loaded</li>
     * <li>Immediately after saving the game</li>
     * </ul>
     * 
     * @see #isModified()
     */
    public void resetModified()
    {
        modified = false;
    }
    
    /**
     * Gets a value that indicates whether the grid has been modified
     * since the last reset.
     * 
     * @return  true if the grid has been modified since the last reset
     * 
     * @see #resetModified()
     */
    public boolean isModified()
    {
        return modified;
    }
    
    /**
     * This class is used to iterate over every live cell
     * in some rectangle.
     * 
     * @author Jack Straub
     *
     */
    private class CellIterator implements Iterator<Cell>
    {
        /** The source Rectangle */
        private final Rectangle rect;
        
        /** 
         * Iterator over all keys in the map.
         * Note that only live cells are stored in the map,
         * so every key maps to a live cell.
         */
        private Iterator<Point> pointIter;
        
        /** Next cell to return; null if none. */
        private Cell            next;
        
        public CellIterator( Rectangle rect )
        {
            this.rect = rect;
            Set<Point>   keys    = grid.keySet();
            pointIter = keys.iterator();
            next = nextCell();
        }
        
        /**
         * Returns true if this iterator can return another element.
         * 
         * @return true, if this iterator can return another element
         */
        @Override
        public boolean hasNext()
        {
            boolean result  = next != null;
            return result;
        }

        /**
         * Returns the next Cell associated with this iterator.
         * Throws NoSuchElement exception if there is no next Cell.
         * 
         * @return  the next Cell associated with this iterator
         * 
         * @throws NoSuchElementException if there is no next Cell
         */
        @Override
        public Cell next() throws NoSuchElementException
        {
            if ( next == null )
            {
                String  message = "Iterator exhausted";
                throw new NoSuchElementException( message );
            }
            
            Cell    cell    = next;
            next = nextCell();
            
            return cell;
        }
        
        /**
         * Locates the "next" live cell that falls within the range
         * of the given rectangle.
         * Cells are not returned in any particular order.
         * 
         * @return the "next" live cell in the rectangle
         */
        private Cell nextCell()
        {
            Cell    nextCell = null;
            while ( nextCell == null && pointIter.hasNext() )
            {
                Point   point   = pointIter.next();
                
                if ( rect.contains( point) )
                    nextCell = new Cell( point, true );
            }
            return nextCell;
        }
    }
    
    private class GenerationIterator implements Iterator<Cell>
    {
        private static final int    extendRectBy    = 2;
        
        private final int   firstRow;
        private final int   firstCol;
        private final int   lastRow;
        private final int   lastCol;
        
        private Point   nextLoc;
        
        public GenerationIterator()
        {
            Rectangle   rect    = getLiveRectangle();
            firstRow = rect.y - extendRectBy;
            firstCol = rect.x - extendRectBy;
            lastRow = firstRow + rect.height + 2 * extendRectBy;
            lastCol = firstCol + rect.width + 2 * extendRectBy;
            
            nextLoc = new Point( firstCol, firstRow );
        }

        @Override
        public boolean hasNext()
        {
            return nextLoc != null;
        }

        @Override
        public Cell next() throws NoSuchElementException
        {
            if ( nextLoc == null )
            {
                String  message = "Iterator bounds exceeded";
                throw new NoSuchElementException( message );
            }
            
            Cell    nextCell    = get( nextLoc );
            if ( ++nextLoc.x > lastCol )
            {
                nextLoc.x = firstCol;
                if ( ++nextLoc.y > lastRow )
                    nextLoc = null;;
            }

            return nextCell;
        }
    }
}
