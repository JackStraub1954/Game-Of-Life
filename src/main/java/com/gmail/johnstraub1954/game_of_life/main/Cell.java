/**
 * 
 */
package com.gmail.johnstraub1954.game_of_life.main;

import java.awt.Point;
import java.util.Objects;

/**
 * Encapsulation of a cell in the Game of Life.
 * Properties:
 * <ol>
 *      <li>xco - x coordinate of cell</li>
 *      <li>yco - y coordinate of cell</li>
 *      <li>isAlive - true if the cell is alive</li>
 * </ol>
 * @author Jack Straub
 *
 */
public class Cell
{
    /** 
     * Grid coordinates of this cell. 
     * This object is not modifiable by the user.
     * 
     * @see #getPoint
     */
    private final Point point;
    
    /** true if cell is alive */
    private boolean isAlive;

    /**
     * Default constructor. X- and y-coordinates 
     * are explicitly set to 0;
     * isAlive is explicitly set to false;
     * 
     * @param xco   x-coordinate of this cell
     * @param yco   y-coordinate of this cell
     */
    public Cell()
    {
        this( 0, 0, false );
    }
    
    /**
     * Constructor to set the x- and y-coordinates of this cell.
     * isAlive is explicitly set to false.
     * 
     * @param xco   x-coordinate of this cell
     * @param yco   y-coordinate of this cell
     */
    public Cell( int xco, int yco )
    {
        this( xco, yco, false );
    }
    
    /**
     * Constructor to set the x- and y-coordinates of a cell
     * to the x- and y-coordinates of a given Point.
     * isAlive is explicitly set to false.
     * 
     * @param point the given point
     */
    public Cell( Point point )
    {
        this( point.x, point.y, false );
    }
    
    /**
     * Constructor to set the x- and y-coordinates of a cell
     * to the x- and y-coordinates of a given Point,
     * and isAlive to a given value.
     * 
     * @param point     the given point
     * @param isAlive   the given value for isAlive
     */
    public Cell( Point point, boolean isAlive )
    {
        this( point.x, point.y, isAlive );
    }
    
    /**
     * Copy constructor.
     * 
     * @param cell  the cell to copy
     */
    public Cell( Cell cell )
    {
        this( cell.point.x, cell.point.y, cell.isAlive );
    }
    
    /**
     * Constructor to set all properties of this cell.
     * 
     * @param xco       the x-coordinate of this cell
     * @param yco       the y-coordinate of this cell
     * @param alive     true if this cell is alive
     */
    public Cell( int xco, int yco, boolean alive )
    {
        point = new Point( xco, yco );
        setAlive( alive );
    }
    
    /**
     * Get the neighbor of a cell in a given direction.
     * 
     * @param dir   the given direction
     * 
     * @return the neighbor of a cell in a given direction.
     */
    public Cell getNeighbor( Direction dir, GridMap map )
    {
        Point   point   = getRelativePoint( dir );
        Cell    cell    = map.get( point );
        return cell;
    }
    
    /**
     * Returns a point, relative to this Cell, in a given direction.
     * 
     * @param dir   the given direction
     * 
     * @return  a point, relative to this Cell, in the given direction
     */
    public Point getRelativePoint( Direction dir )
    {
        int     xco = getXco() + dir.getHorizontal();
        int     yco = getYco() + dir.getVertical();
        Point   point   = new Point( xco, yco );
        return point;
    }
    
    /**
     * Returns true if this cell is alive.
     * 
     * @return true if this cell is alive.
     */
    public boolean isAlive()
    {
        return isAlive;
    }

    /**
     * Sets this cell to alive or dead
     * 
     * @param isAlive   true, to make the cell alive
     */
    public void setAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }
    
    /**
     * Getter for the x-coordinate of this cell's grid location.
     * @return  the x-coordinate of this cell
     */
    public int getXco()
    {
        return point.x;
    }
    
    /**
     * Getter for the y-coordinate of this cell
     * @return  the y-coordinate of this cell
     */
    public int getYco()
    {
        return point.y;
    }
    
    /**
     * Gets a copy of the grid coordinates of this cell.
     * Modifying the returned object
     * will <em>not</em> modify the gird location
     * of this cell.
     * 
     * @return a copy of the grid coordinates of this cell
     */
    public Point getPoint()
    {
        Point   point   = new Point( this.point );
        return point;
    }
    
    /**
     * Returns a string representation of this Cell.
     * 
     * @return  a string representation of this Cell
     */
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder( point.toString() );
        bldr.append( ",isAlive=" ).append( isAlive );
        return bldr.toString();
    }
    
    /**
     * Tests this Cell against a given object for equality.
     * Equality pertains if the given object is a Cell,
     * and its x and y coordinates
     * and living status are all equal.
     * 
     * @param   obj the given Object
     * 
     * @returns true, if this Cell is equal to the given object
     */
    @Override
    public boolean equals( Object obj )
    {
        boolean result = false;
        if ( obj != null && obj instanceof Cell )
        {
            Cell    that    = (Cell)obj;
            result  = this.point.equals( that.point )
                && this.isAlive == that.isAlive;
        }
        return result;
    }
    
    /**
     * Produces a hashcode for this Cell.
     * Required because <em>equals</em> is overridden.
     * 
     * @return  a hashcode for this Cell
     * 
     * @see Objects#hash(Object...)
     */
    @Override
    public int hashCode()
    {
        int hash    = Objects.hash( point, isAlive );
        return hash;
    }
}
