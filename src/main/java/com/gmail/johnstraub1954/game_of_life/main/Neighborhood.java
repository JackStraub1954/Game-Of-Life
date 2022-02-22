package com.gmail.johnstraub1954.game_of_life.main;

import java.util.Collection;
import java.util.HashMap;

/**
 * Encapsulates the logic to assemble and evaluate neighborhoods.
 * 
 * A <em>neighborhood</em> if a cell <em>self</em>
 * consists of <em>self's</em> eight neighbors.
 * The specific neighbor of a cell can be accessed using
 * <em>get(Direction&nbsp;dir)</em>
 * where <em>dir</em> is one of the first eight standard directions
 * as indicated by <em>enum Direction.</em>.
 * 
 * @author Jack Straub
 * 
 * @see HashMap#get(Object)
 */
public class Neighborhood extends HashMap<Direction, Cell>
{
    /** Generated serial version UID */
    private static final long serialVersionUID = -8161806609146596688L;
    
    /** The Cell whose neighbors can be accessed via this Neighborhood */
    private final Cell      self;
    
    /**
     * Constructor.
     * 
     * @param self  the cell from which this neighborhood is extrapolated
     * @param map   map of all cells in this game
     */
    public Neighborhood( Cell self, GridMap map )
    {
        this.self = self;
        for ( Direction dir : Direction.values() )
        {
            Cell    cell    = self.getNeighbor( dir, map );
            put( dir, cell );
        }
    }
    
    /**
     * Gets the number of live cells in the neighborhood,
     * not including self.
     * 
     * @return  the number of live cells in the neighborhood,
     *          not including self
     */
    public int  getLivingNeighborCount()
    {
        int count   = 0;
        for ( Cell cell : values() )
        {
            if ( cell.isAlive() )
                ++count;
        }
        
        return count;
    }
    
    /**
     * Returns the base cell for this neighborhood.
     * 
     * @return  the base cell for this neighborhood
     */
    public Cell getSelf()
    {
        return self;
    }
    
    /**
     * Determines whether the next state of the base cell
     * of this neighborhood should be <em>alive</em>
     * or <em>dead.</em>
     * The new state is determined by a
     * given set of live neighbor counts that indicates 
     * a live cell should survive,
     * and a given set of live neighbor counts that indicates
     * a dead cell should be born
     * 
     * @param survivalStates    the given set of live neighbor counts 
     *                          that indicates a live cell should survive
     * @param birthStates       the given set of live neighbor counts 
     *                          that indicates a dead cell should be born
     *                          
     * @return  a copy of <em>self</em> in the new state
     *          as dictated by the above
     */
    public Cell getNextState( 
        Collection<Integer> survivalStates, 
        Collection<Integer> birthStates
    )
    {
        int     count       = getLivingNeighborCount();
        Cell    nextState   = new Cell( self );
        if ( nextState.isAlive() )
        {
            nextState.setAlive( survivalStates.contains( count ) );
        }
        else
        {
            nextState.setAlive( birthStates.contains( count ) );
        }
        return nextState;
    }
}
