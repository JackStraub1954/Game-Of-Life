package com.gmail.johnstraub1954.game_of_life.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridmapTest
{
    private GridMap gridMap;
    
    @BeforeEach
    public void beforeEach()
    {
        gridMap = new GridMap();
    }
    
    @Test
    void testGetIntInt()
    {
        // Test a range of cells in an empty GridMap;
        // make sure they all come back correctly.
        for ( int row = -100 ; row < 100 ; ++row )
            for ( int col = -100 ; col < 100 ; ++col )
            {
                Cell    cell    = gridMap.get( col, row );
                Point   point   = cell.getPoint();
                assertFalse( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        
        // Set some cells in a range, and make sure they can be gotten;
        // make sure non-set cells can be correctly gotten, too.
        int setStartXco = -50;
        int setEndXco   = 50;
        int setStartYco = -50;
        int setEndYco   = 50;
        
        for ( int row = setStartYco ; row < setEndYco ; ++row )
            for ( int col = setStartXco ; col < setEndXco ; ++col )
            {
                gridMap.put( col, row, true );
            }
        
        // cells below the range must be false
        for ( int row = setStartYco - 50 ; row < setStartYco ; ++row )
            for ( int col = setStartXco - 50 ; col < setStartXco ; ++col )
            {
                Cell    cell    = gridMap.get( col, row );
                Point   point   = cell.getPoint();
                assertFalse( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        // cells within the range must be true
        for ( int row = setStartYco ; row < setEndYco ; ++row )
            for ( int col = setStartXco ; col < setEndXco ; ++col )
            {
                Cell    cell    = gridMap.get( col, row );
                Point   point   = cell.getPoint();
                assertTrue( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        // cells above the range must be false
        for ( int row = setEndYco ; row < setStartYco + 50 ; ++row )
            for ( int col = setStartXco ; col < setStartXco + 50 ; ++col )
            {
                Cell    cell    = gridMap.get( col, row );
                Point   point   = cell.getPoint();
                assertFalse( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
    }

    @Test
    void testGetPoint()
    {
        // Set some cells in a range, and make sure they can be gotten;
        // make sure non-set cells can be correctly gotten, too.
        int setStartXco = -50;
        int setEndXco   = 50;
        int setStartYco = -50;
        int setEndYco   = 50;
        
        for ( int row = setStartYco ; row < setEndYco ; ++row )
            for ( int col = setStartXco ; col < setEndXco ; ++col )
            {
                gridMap.put( col, row, true );
            }
        
        // cells below the range must be false
        for ( int row = setStartYco - 50 ; row < setStartYco ; ++row )
            for ( int col = setStartXco - 50 ; col < setStartXco ; ++col )
            {
                Point   point   = new Point( col, row );
                Cell    cell    = gridMap.get( point );
                assertFalse( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        // cells within the range must be true
        for ( int row = setStartYco ; row < setEndYco ; ++row )
            for ( int col = setStartXco ; col < setEndXco ; ++col )
            {
                Point   point   = new Point( col, row );
                Cell    cell    = gridMap.get( point );
                point  = cell.getPoint();
                assertTrue( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        // cells above the range must be false
        for ( int row = setEndYco ; row < setStartYco + 50 ; ++row )
            for ( int col = setStartXco ; col < setStartXco + 50 ; ++col )
            {
                Point   point   = new Point( col, row );
                Cell    cell    = gridMap.get( point );
                point   = cell.getPoint();
                assertFalse( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
    }

    @Test
    void testPutIntIntBoolean()
    {
        // Set a range of unset cells; verify a dead cell is returned
        int startXco    = -50;
        int endXco      = startXco + 100;
        int startYco    = -50;
        int endYco      = startYco + 100;
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Cell    cell    = gridMap.put( col, row, true );
                Point   point   = cell.getPoint();
                assertFalse( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        
        // Set the same range again; all returned cells should be alive
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Cell    cell    = gridMap.put( col, row, true );
                Point   point   = cell.getPoint();
                assertTrue( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        
        // Reset the range; all returned cells should be alive
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Cell    cell    = gridMap.put( col, row, false );
                Point   point   = cell.getPoint();
                assertTrue( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
    }
    
    void testPutCell()
    {
        // Mainly a copy of putPointBoolean
        // Set a range of reset cells; verify a dead cell is returned
        int startXco    = -50;
        int endXco      = startXco + 5;
        int startYco    = -50;
        int endYco      = startYco + 5;
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Cell    cellIn  = new Cell( col, row, true );
                Cell    cellOut = gridMap.put( cellIn );
                Point   point   = cellOut.getPoint();
                assertFalse( cellOut.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        
        // Set the same range again; all returned cells should be alive
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Cell    cellIn  = new Cell( col, row, true );
                Cell    cellOut = gridMap.put( cellIn );
                Point   point   = cellOut.getPoint();
                assertTrue( cellOut.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        
        // Reset the range; all returned cells should be alive
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Cell    cellIn  = new Cell( col, row, false );
                Cell    cellOut = gridMap.put( cellIn );
                Point   point   = cellOut.getPoint();
                assertTrue( cellOut.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
    }

    @Test
    void testPutPointBoolean()
    {
        // Basically, a copy of putIntIntBoolean
        // Set a range of unset cells; verify a dead cell is returned
        int startXco    = -50;
        int endXco      = 50;
        int startYco    = -50;
        int endYco      = 50;
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Point   point   = new Point( col, row );
                Cell    cell    = gridMap.put( point, true );
                assertFalse( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        
        // Set the same range again; all returned cells should be alive
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Point   point   = new Point( col, row );
                Cell    cell    = gridMap.put( point, true );
                assertTrue( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
        
        // Reset the range; all returned cells should be alive
        for ( int row = startYco ; row < endYco ; ++row )
            for ( int col = startXco ; col < endXco ; ++col )
            {
                Point   point   = new Point( col, row );
                Cell    cell    = gridMap.put( point, false );
                assertTrue( cell.isAlive() );
                assertEquals( col, point.x );
                assertEquals( row, point.y );
            }
    }

    @Test
    void testGetLiveRectangle()
    {
        int leftX   = -100;
        int rightX  = 100;
        int topY    = -200;
        int bottomY = 200;
        
        gridMap.put( leftX, topY, true );
        gridMap.put( rightX, topY, true );
        gridMap.put( leftX, bottomY, true );
        gridMap.put( rightX, bottomY, true );
        for ( int row = topY + 20 ; row < bottomY ; row += 10 )
            for ( int col = leftX ; col < rightX ; col += 10 )
                gridMap.put( col, row, true );
        
        int         expWidth    = rightX - leftX + 1;
        int         expHeight   = bottomY - topY + 1;
        Rectangle   rect        = gridMap.getLiveRectangle();
        assertEquals( leftX, rect.x );
        assertEquals( topY, rect.y );
        assertEquals( expWidth, rect.width );
        assertEquals( expHeight, rect.height );
        
    }

    @Test
    void testIterator()
    {
        Point[]     allPoints   =
        {
            new Point( -10, -10 ),
            new Point( 10, -10 ),
            new Point( -10, 10 ),
            new Point( 10, 10 ),
            new Point( 0, 0 )
        };
        List<Cell>  liveCells   = new ArrayList<>();
        
        for ( Point point : allPoints)
        {
            Cell    cell    = new Cell( point, true );
            gridMap.put( cell );
            liveCells.add( cell );
        }
        
        Iterator<Cell>  iter    = gridMap.iterator();
        while ( iter.hasNext() )
        {
            Cell    cell    = iter.next();
            assertTrue( liveCells.remove( cell ) );
        }
        assertTrue( liveCells.isEmpty() );
    }

    @Test
    void testIteratorRectangle()
    {
        Point       ulc     = new Point( -10, -10 ); // upper left corner
        Point       urc     = new Point( 10, -10 );  // upper right corner
        Point       llc     = new Point( -10, 10 );  // lower left corner
        Point       lrc     = new Point( 10, 10 );   // lower right corner
        Point       center  = new Point( 0, 0 );     // center
        List<Point> livePoints   = new ArrayList<>();
        
        for ( Point point : new Point[]{ ulc, urc, llc, lrc, center } )
        {
            Cell    cell    = new Cell( point, true );
            gridMap.put( cell );
            livePoints.add( point );
        }
        
        // Create a rectangle that includes the upper-left corner
        // and center point, but not the lower corners.
        int         width   = urc.x - ulc.x;
        int         height  = lrc.y - urc.y;
        Rectangle   rect    = new Rectangle( ulc.x, ulc.y, width, height );
        
        Iterator<Cell>  iter    = gridMap.iterator( rect );
        while ( iter.hasNext() )
        {
            Cell    cell    = iter.next();
            assertTrue( livePoints.remove( cell.getPoint() ) );
        }
        assertEquals( 3, livePoints.size() );
        assertTrue( livePoints.contains( urc ) );
        assertTrue( livePoints.contains( llc ) );
        assertTrue( livePoints.contains( lrc ) );
    }

    /**
     *  Verify that GridMap.iterator().next()
     *  throws a NoSuchElementException when necessary.
     */
    @Test
    public void testGoWrong()
    {
        gridMap.put( new Point( 0, 0 ), true );
        Iterator<Cell>  iter    = gridMap.iterator();
        
        // Exhaust the iterator
        while ( iter.hasNext() )
            iter.next();
        
        Class<NoSuchElementException>   clazz   = 
            NoSuchElementException.class;
        assertThrows( clazz, ()->iter.next() );
    }
}
