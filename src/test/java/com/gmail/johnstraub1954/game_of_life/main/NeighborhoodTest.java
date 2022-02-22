package com.gmail.johnstraub1954.game_of_life.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Specific tests
 * 
 * Remains alive:
 * 
 * 
 * @author Jack Straub
 */
class NeighborhoodTest
{
    private static final List<Integer>  survivalStates  =
        new ArrayList<>( Arrays.asList( 2, 3 ) );
    private static final List<Integer>  birthStates  =
        new ArrayList<>( Arrays.asList( 3 ) );
    
    private GridMap defGridMap;
    
    @BeforeEach
    public void beforeEach()
    {
        defGridMap = new GridMap();
    }

    @ParameterizedTest
    @ValueSource( booleans = { true, false } )
    void testNeighborhood( boolean state )
    {
        int             selfX       = 10;
        int             selfY       = 20;
        Cell            self        = new Cell( selfX, selfY, state );
        
        Neighborhood    hood        = new Neighborhood( self, defGridMap );
        for ( Direction dir : Direction.values() )
        {
            Cell    neighbor    = hood.get( dir );
            assertFalse( neighbor.isAlive() );
            Point   point       = neighbor.getPoint();
            int     expX        = selfX + dir.getHorizontal();
            int     expY        = selfY + dir.getVertical();
            assertEquals( expX, point.x );
            assertEquals( expY, point.y );
        }
    }
    
    @Test
    void testGetLivingNeighborCount()
    {
        int     selfX       = 10;
        int     selfY       = 20;
        Cell    self        = new Cell( selfX, selfY );
        int     expCount    = 0;
        for ( Direction dir : Direction.values() )
        {
            Neighborhood    hood    = new Neighborhood( self, defGridMap );
            assertEquals( expCount, hood.getLivingNeighborCount() );
            Point           point   = self.getRelativePoint( dir );
            defGridMap.put( point, true );
            ++expCount;
        }
    }

    @Test
    void testGetSelf()
    {
        int             selfX       = 10;
        int             selfY       = 20;
        Cell            self        = new Cell( selfX, selfY );
        Neighborhood    hood        = new Neighborhood( self, defGridMap );
        assertEquals( self, hood.getSelf() );
    }

    @Test
    void testGetNextStateSurvive()
    {
        int         selfX       = 10;
        int         selfY       = 20;
        Cell        self        = new Cell( selfX, selfY, true );
        Direction[] dirs        = Direction.values();
        for ( int count = 0 ; count <= 8 ; ++count )
        {
            Neighborhood    hood        = new Neighborhood( self, defGridMap );
            Cell            cell        = 
                hood.getNextState( survivalStates, birthStates );
            boolean         expAlive    = survivalStates.contains( count );
            assertEquals( expAlive, cell.isAlive() );
            if ( count < 8 )
            {
                Direction   dir     = dirs[count];
                Point       point   = cell.getRelativePoint( dir );
                defGridMap.put( point, true );
            }
        }
    }

    @Test
    void testGetNextStateBirth()
    {
        int         selfX       = 10;
        int         selfY       = 20;
        Cell        self        = new Cell( selfX, selfY, false );
        Direction[] dirs        = Direction.values();
        for ( int count = 0 ; count <= 8 ; ++count )
        {
            Neighborhood    hood        = new Neighborhood( self, defGridMap );
            Cell            cell        = 
                hood.getNextState( survivalStates, birthStates );
            boolean         expAlive    = birthStates.contains( count );
            assertEquals( expAlive, cell.isAlive() );
            if ( count < 8 )
            {
                Direction   dir     = dirs[count];
                Point       point   = cell.getRelativePoint( dir );
                defGridMap.put( point, true );
            }
        }
    }
}
