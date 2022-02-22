package com.gmail.johnstraub1954.game_of_life.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParametersTest
{
    private Object  propUnderTestNewVal;
    private Object  propUnderTestOldVal;
    private String  propUnderTestName;
    private int     propUnderTestCount;
    
    private Parameters  params;
    private List<PropChangeTester>  allPropChangeTesters    = new LinkedList<>();
    
    @BeforeEach
    public void beforeEach()
    {
        params = Parameters.INSTANCE;
        propUnderTestNewVal = null;
        propUnderTestOldVal = null;
        propUnderTestName = null;
        propUnderTestCount = 0;
        
        while ( !allPropChangeTesters.isEmpty() )
        {
            PropChangeTester    tester  = allPropChangeTesters.remove( 0 );
            params.removePropertyChangeListener( tester );
        }
    }
    
    @Test
    void testAddPropertyChangeListenerPropertyChangeListener()
    {
        int     oldVal  = 15;
        int     newVal  = oldVal + 5;
        String  name    = GOLConstants.GRID_CELL_SIZE_PN;
        
        params.setGridCellSize( oldVal );
        assertNull( propUnderTestName );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( tester );
        params.setGridCellSize( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( 1, propUnderTestCount );
        
        propUnderTestCount = 0;
        tester = getPropChangeTester();
        params.addPropertyChangeListener( tester );
        params.setGridCellSize( oldVal );
        assertEquals( 2, propUnderTestCount );
    }

    @Test
    void testAddPropertyChangeListenerStringPropertyChangeListener()
    {
        int     oldVal  = 15;
        int     newVal  = oldVal + 5;
        String  name1   = GOLConstants.GRID_WIDTH_PN;
        // name2 must be different from name1
        String  name2   = GOLConstants.GRID_HEIGHT_PN;
        
        params.setGridWidth( oldVal );
        assertNull( propUnderTestName );
        
        PropChangeTester    tester1  = getPropChangeTester();
        params.addPropertyChangeListener( name1, tester1 );

        // tester2 shouldn't ever be invoked;
        // it's here to prove that when I invoke the gridWidth-specific
        // property change listener, only the gridWidth-specific property
        // change listener is invoked.
        PropChangeTester    tester2  = getPropChangeTester();
        params.addPropertyChangeListener( name2, tester2 );

        params.setGridWidth( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name1, propUnderTestName );
        assertEquals( 1, propUnderTestCount );
        
        propUnderTestCount = 0;
        tester1 = getPropChangeTester();
        params.addPropertyChangeListener( name1, tester1 );
        params.setGridWidth( oldVal );
        assertEquals( 2, propUnderTestCount );
    }

    @Test
    void testRemovePropertyChangeListenerPropertyChangeListener()
    {
        int     oldVal  = 15;
        int     newVal  = oldVal + 5;
        String  name    = GOLConstants.GRID_CELL_SIZE_PN;
        
        params.setGridCellSize( oldVal );
        assertNull( propUnderTestName );
        
        // Add one PropChangeTester, verify that only 1 
        // PropChangeTester is invoked.
        PropChangeTester    tester1 = getPropChangeTester();
        params.addPropertyChangeListener( tester1 );
        params.setGridCellSize( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( 1, propUnderTestCount );

        // Add a second prop change tester,
        // verify that both testers are invoked.
        propUnderTestCount = 0;
        PropChangeTester    tester2 = getPropChangeTester();
        params.addPropertyChangeListener( tester2 );
        params.setGridCellSize( oldVal );
        assertEquals( 2, propUnderTestCount );

        // Remove a prop change tester,
        // verify that only one tester is invoked.
        propUnderTestCount = 0;
        params.removePropertyChangeListener( tester2 );
        params.setGridCellSize( newVal );
        assertEquals( 1, propUnderTestCount );
        
        // Remove the remaining prop change tester,
        // verify that no testers are invoked.
        propUnderTestCount = 0;
        params.removePropertyChangeListener( tester1 );
        params.setGridCellSize( oldVal );
        assertEquals( 0, propUnderTestCount );
    }

    @Test
    void testRemovePropertyChangeListenerStringPropertyChangeListener()
    {
        int     oldVal  = 15;
        int     newVal  = oldVal + 5;
        String  name    = GOLConstants.GRID_CELL_SIZE_PN;
        
        params.setGridCellSize( oldVal );
        assertNull( propUnderTestName );
        
        // Add one PropChangeTester, verify that only 1 
        // PropChangeTester is invoked.
        PropChangeTester    tester1 = getPropChangeTester();
        params.addPropertyChangeListener( name, tester1 );
        params.setGridCellSize( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( 1, propUnderTestCount );

        // Add a second prop change tester,
        // verify that both testers are invoked.
        propUnderTestCount = 0;
        PropChangeTester    tester2 = getPropChangeTester();
        params.addPropertyChangeListener( name, tester2 );
        params.setGridCellSize( oldVal );
        assertEquals( 2, propUnderTestCount );

        // Remove a prop change tester,
        // verify that only one tester is invoked.
        propUnderTestCount = 0;
        params.removePropertyChangeListener( name, tester2 );
        params.setGridCellSize( newVal );
        assertEquals( 1, propUnderTestCount );
        
        // Remove the remaining prop change tester,
        // verify that no testers are invoked.
        propUnderTestCount = 0;
        params.removePropertyChangeListener( name, tester1 );
        params.setGridCellSize( oldVal );
        assertEquals( 0, propUnderTestCount );
    }

    @Test
    void testSetAutoRegenerationOn()
    {
        boolean oldVal  = true;
        boolean newVal  = !oldVal;
        String  name    = GOLConstants.AUTO_REGEN_ON_PN;
        
        params.setAutoRegenerationOn( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.isAutoRegenerationOn() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setAutoRegenerationOn( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Boolean );
        assertTrue( propUnderTestNewVal instanceof Boolean );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.isAutoRegenerationOn(), newVal );
    }

    @Test
    void testSetAutoRegenerationPace()
    {
        float   oldVal  = 1.5f;
        float   newVal  = oldVal + 1;
        String  name    = GOLConstants.AUTO_REGEN_PACE_PN;
        
        params.setAutoRegenerationPace( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getAutoRegenerationPace() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setAutoRegenerationPace( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Float );
        assertTrue( propUnderTestNewVal instanceof Float );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getAutoRegenerationPace(), newVal );
    }

    @Test
    void testSetGridColor()
    {
        Color   oldVal  = Color.RED;
        Color   newVal  = Color.BLACK;
        String  name    = GOLConstants.GRID_COLOR_PN;
        
        params.setGridColor( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridColor() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridColor( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Color );
        assertTrue( propUnderTestNewVal instanceof Color );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridColor(), newVal );
    }

    @Test
    void testSetGridMarginTop()
    {
        int     oldVal  = 5;
        int     newVal  = 2 * oldVal;
        String  name    = GOLConstants.GRID_MARGIN_TOP_PN;
        
        params.setGridMarginTop( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridMarginTop() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridMarginTop( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridMarginTop(), newVal );
    }

    @Test
    void testSetGridMarginLeft()
    {
        int     oldVal  = 5;
        int     newVal  = 2 * oldVal;
        String  name    = GOLConstants.GRID_MARGIN_LEFT_PN;
        
        params.setGridMarginLeft( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridMarginLeft() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridMarginLeft( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridMarginLeft(), newVal );
    }

    @Test
    void testSetGridMarginBottom()
    {
        int     oldVal  = 5;
        int     newVal  = 2 * oldVal;
        String  name    = GOLConstants.GRID_MARGIN_BOTTOM_PN;
        
        params.setGridMarginBottom( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridMarginBottom() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridMarginBottom( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridMarginBottom(), newVal );
    }

    @Test
    void testSetGridMarginRight()
    {
        int     oldVal  = 5;
        int     newVal  = 2 * oldVal;
        String  name    = GOLConstants.GRID_MARGIN_RIGHT_PN;
        
        params.setGridMarginRight( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridMarginRight() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridMarginRight( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridMarginRight(), newVal );
    }

    @Test
    void testSetGridWidth()
    {
        int     oldVal  = 5;
        int     newVal  = 2 * oldVal;
        String  name    = GOLConstants.GRID_WIDTH_PN;
        
        params.setGridWidth( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridWidth() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridWidth( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridWidth(), newVal );
    }

    @Test
    void testSetGridHeight()
    {
        int     oldVal  = 5;
        int     newVal  = 2 * oldVal;
        String  name    = GOLConstants.GRID_HEIGHT_PN;
        
        params.setGridHeight( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridHeight() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridHeight( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridHeight(), newVal );
    }

    @Test
    void testSetGridLineShow()
    {
        boolean oldVal  = true;
        boolean newVal  = !oldVal;
        String  name    = GOLConstants.GRID_LINE_SHOW_PN;
        
        params.setGridLineShow( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.isGridLineShow() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridLineShow( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Boolean );
        assertTrue( propUnderTestNewVal instanceof Boolean );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.isGridLineShow(), newVal );
    }

    @Test
    void testSetGridLineWidth()
    {
        int     oldVal  = 5;
        int     newVal  = 2 * oldVal;
        String  name    = GOLConstants.GRID_LINE_WIDTH_PN;
        
        params.setGridLineWidth( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridLineWidth() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridLineWidth( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridLineWidth(), newVal );
    }

    @Test
    void testSetGridLineColor()
    {
        Color   oldVal  = Color.RED;
        Color   newVal  = Color.BLACK;
        String  name    = GOLConstants.GRID_LINE_COLOR_PN;
        
        params.setGridLineColor( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridLineColor() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridLineColor( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Color );
        assertTrue( propUnderTestNewVal instanceof Color );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridLineColor(), newVal );
    }

    @Test
    void testSetGridCellSize()
    {
        int     oldVal  = 5;
        int     newVal  = 2 * oldVal;
        String  name    = GOLConstants.GRID_CELL_SIZE_PN;
        
        params.setGridCellSize( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridCellSize() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridCellSize( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Integer );
        assertTrue( propUnderTestNewVal instanceof Integer );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridCellSize(), newVal );
    }

    @Test
    void testSetGridCellColor()
    {
        Color   oldVal  = Color.RED;
        Color   newVal  = Color.BLACK;
        String  name    = GOLConstants.GRID_CELL_COLOR_PN;
        
        params.setGridCellColor( oldVal );
        assertNull( propUnderTestName );
        assertEquals( oldVal, params.getGridCellColor() );
        
        PropChangeTester    tester  = getPropChangeTester();
        params.addPropertyChangeListener( name, tester );
        params.setGridCellColor( newVal );
        assertNotNull( propUnderTestOldVal );
        assertNotNull( propUnderTestNewVal );
        assertTrue( propUnderTestOldVal instanceof Color );
        assertTrue( propUnderTestNewVal instanceof Color );
        assertEquals( oldVal, propUnderTestOldVal );
        assertEquals( newVal, propUnderTestNewVal );
        assertEquals( name, propUnderTestName );
        assertEquals( params.getGridCellColor(), newVal );
    }
    
    private PropChangeTester getPropChangeTester()
    {
        PropChangeTester    tester  = new PropChangeTester();
        allPropChangeTesters.add( tester );
        return tester;
    }

    private class PropChangeTester implements PropertyChangeListener
    {
        @Override
        public void propertyChange( PropertyChangeEvent evt )
        {
            propUnderTestNewVal = evt.getNewValue();
            propUnderTestOldVal = evt.getOldValue();
            propUnderTestName = evt.getPropertyName();
            ++propUnderTestCount;
        }
        
    }
}
