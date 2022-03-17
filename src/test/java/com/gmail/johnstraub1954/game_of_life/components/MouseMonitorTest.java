package com.gmail.johnstraub1954.game_of_life.components;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_CLICKED_PN;
import static org.junit.jupiter.api.Assertions.fail;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gmail.johnstraub1954.game_of_life.main.Cell;
import com.gmail.johnstraub1954.game_of_life.main.GridMap;
import com.gmail.johnstraub1954.game_of_life.main.Parameters;

import test_utils.TestUtils;

class MouseMonitorTest
{
    private static Parameters   params  = Parameters.INSTANCE;
    
    private GridMap     gridMap = params.getGridMap();
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GridFrame   frame   = new GridFrame();
        SwingUtilities.invokeLater( () -> frame.run() );
        TestUtils.pause( 125 );
    }

    @BeforeEach
    void setUp() throws Exception
    {
    }

    @Test
    void test()
    {
        params.addPropertyChangeListener( 
            GRID_CELL_CLICKED_PN, 
            ( v ) -> {
                Cell cell = (Cell)v.getNewValue();
                cell.setAlive( !cell.isAlive() );
                System.out.println( cell );
                gridMap.put( cell );
            });
    }

}
