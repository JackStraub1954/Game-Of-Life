package com.gmail.johnstraub1954.game_of_life.main;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.AUTO_REGEN_MAX_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.AUTO_REGEN_MIN_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.AUTO_REGEN_ON_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.AUTO_REGEN_PACE_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.CTRL_BIRTH_STATES_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.CTRL_CENTER_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.CTRL_GRID_URL_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.CTRL_SURVIVAL_STATES_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_ORIGIN_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_SIZE_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_HEIGHT_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_KEEP_CENTERED_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_SHOW_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_WIDTH_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MAP_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_BOTTOM_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_LEFT_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_RIGHT_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_TOP_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_WIDTH_PN;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a single point of access to parameters
 * that may change dynamically during the course of Game of Life execution.
 * For example: 
 * automated regeneration on/off,
 * speed of automated regeneration, etc.
 * Clients can register for property change notification
 * when a parameter changes.
 * 
 * @author Jack Straub
 *
 */
public enum Parameters
{
    INSTANCE;
    
    /** Automatic regeneration on/off */
    private boolean             autoRegenerationOn;
    /** Pace of automatic regeneration, in generations per second */
    private float               autoRegenerationPace;
    
    /** Auto-regeneration minimum value (generations per second) */
    private float               autoRegenerationPaceMin;
    
    /** Auto-regeneration maximum value (generations per second) */
    private float               autoRegenerationPaceMax;

    /** Background color of the grid */
    private Color               gridColor;
    /** Top margin of grid */
    private int                 gridMarginTop;
    /** Left margin of grid */
    private int                 gridMarginLeft;
    /** Bottom margin of grid */
    private int                 gridMarginBottom;
    /** Right margin of grid */
    private int                 gridMarginRight;
    /** Grid width <em>in cells</em> */
    private int                 gridWidth;
    /** Grid height <em>in cells</em> */
    private int                 gridHeight;
    
    /** Grid lines on/off */
    private boolean             gridLineShow;
    /** Grid line width; pixels */
    private int                 gridLineWidth;
    /** Grid line color */
    private Color               gridLineColor;
    
    /** Grid cell size (width/height); pixels */
    private int                 gridCellSize;
    /** Grid cell color (for live cells) */
    private Color               gridCellColor;
    /** 
     * The coordinates of the cell to place in the upper-left corner
     * of the physical grid.
     */
    private Point               gridCellOrigin;
    /** Indicates whether the grid should attempt to center its live cells. */
    private boolean             gridCenter;
    /** 
     * Indicates whether the application should try to center the grid
     * with each new generation.
     */
    private boolean             gridKeepCentered;

    /** 
     * Grid map
     */
    private GridMap             gridMap;
    
    /** The location (URL) of the date to parse and display in the grid. */
    private URL                 gridURL;
    
    /** 
     * Conditions under which a live cell may continue to live.
     */
    private List<Integer>       survivalStates;
    /** 
     * Conditions under which a dead cell may come to life.
     */
    private List<Integer>       birthStates;
    
    /** Support for PropertyChangeListeners */
    private final PropertyChangeSupport propChangeSupport   = 
        new PropertyChangeSupport( this );
    
    /** List of ActionListeners */
    private final List<ActionListener>  actionListeners = new ArrayList<>();

    /**
     * Constructor.
     */
    private Parameters()
    {
        GOLProperties   props   = new GOLProperties();
        autoRegenerationOn = props.getAutoRegenOn();
        autoRegenerationPace = props.getAutoRegenPace();
        autoRegenerationPaceMin = props.getAutoRegenPaceMin();
        autoRegenerationPaceMax = props.getAutoRegenPaceMax();
        
        gridColor = props.getGridColor();
        gridMarginTop = props.getGridMarginTop();
        gridMarginLeft = props.getGridMarginLeft();
        gridMarginBottom = props.getGridMarginBottom();
        gridMarginRight = props.getGridMarginRight();
        gridWidth = props.getGridWidth();
        gridHeight = props.getGridHeight();
        gridLineShow = props.getGridLineShow();
        gridLineWidth = props.getGridLineWidth();
        gridLineColor = props.getGridLineColor();
        gridCellSize = props.getGridCellSize();
        gridCellColor = props.getGridCellColor();
        gridCellOrigin = props.getGridCellOrigin();
        gridCenter = props.getCenterGrid();
        gridKeepCentered = props.getGridKeepCentered();
        gridURL = props.getGridURL();
        
        survivalStates = props.getSurvivalStates();
        birthStates = props.getBirthStates();
        
        gridMap = null;
    }
    
    /**
     * Adds a property change listener to this object.
     * 
     * @param listener  property change listener to add
     */
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        propChangeSupport.addPropertyChangeListener( listener );
    }
    
    /**
     * Adds a per-property property change listener to this object.
     * 
     * @param propName  target property name
     * @param listener  property change listener to add
     */
    public void addPropertyChangeListener( 
        String propName,
        PropertyChangeListener listener 
    )
    {
        propChangeSupport.addPropertyChangeListener( propName, listener );
    }
    
    /**
     * Removes a property change listener from this object.
     * Property names are listed in the GOLConstants class.
     * 
     * @param listener  property change listener to remove
     * 
     * @see GOLConstants
     */
    public void 
    removePropertyChangeListener( PropertyChangeListener listener )
    {
        propChangeSupport.removePropertyChangeListener( listener );
    }

    /**
     * Removes a per-property property change listener from this object.
     * Property names are listed in the GOLConstants class.
     * 
     * @param propName  target property name
     * @param listener  property change listener to remove

     * @see GOLConstants
     */
    public void removePropertyChangeListener( 
        String propName,
        PropertyChangeListener listener 
    )
    {
        propChangeSupport.removePropertyChangeListener( propName, listener );
    }
    
    /**
     * Add a given ActionListener to the list of ActionListeners.
     * 
     * @param listener  the given ActionListener
     */
    public void addActionListener( ActionListener listener )
    {
        actionListeners.add( listener );
    }
    
    /**
     * Removes a given ActionListener from the list of ActionListener.
     * If the ActionListener is not in the list
     * no action is taken.
     * 
     * @param listener  the given ActionListener
     */
    public void removeActionListener( ActionListener listener )
    {
        actionListeners.remove( listener );
    }

    /**
     * Gets the autoRegenerationOn parameter.
     * 
     * @return the autoRegenerationOn
     */
    public boolean isAutoRegenerationOn()
    {
        return autoRegenerationOn;
    }

    /**
     * Sets the autoRegenerationOn parameter.
     * 
     * @param autoRegenerationOn the autoRegenerationOn to set
     */
    public void setAutoRegenerationOn(boolean autoRegenerationOn)
    {
        boolean oldVal      = this.autoRegenerationOn;
        String  propName    = AUTO_REGEN_ON_PN;
        this.autoRegenerationOn = autoRegenerationOn;
        propChangeSupport.
        firePropertyChange( propName, oldVal, autoRegenerationOn );
    }

    /**
     * Gets the autoRegenerationPace parameter.
     * 
     * @return the autoRegenerationPace
     */
    public float getAutoRegenerationPace()
    {
        return autoRegenerationPace;
    }

    /**
     * Sets the autoRegenerationPace parameter.
     * 
     * @param autoRegenerationPace the autoRegenerationPace to set
     */
    public void setAutoRegenerationPace(float autoRegenerationPace)
    {
        float   oldVal      = this.autoRegenerationPace;
        String  propName    = AUTO_REGEN_PACE_PN;
        this.autoRegenerationPace = autoRegenerationPace;
        propChangeSupport.
            firePropertyChange( propName, oldVal, autoRegenerationPace );
    }

    /**
     * Gets the auto-regeneration minimum pace parameter.
     * 
     * @return the auto-regeneration minimum pace 
     */
    public float getAutoRegenerationPaceMin()
    {
        return autoRegenerationPaceMin;
    }

    /**
     * Sets the auto-regeneration minimum pace parameter.
     * 
     * @param autoRegenerationPaceMin the autoRegenerationPace to set
     */
    public void setAutoRegenerationPaceMin( float pace )
    {
        float   oldVal      = this.autoRegenerationPaceMin;
        float   newVal      = pace;
        String  propName    = AUTO_REGEN_MIN_PN;
        this.autoRegenerationPaceMin = pace;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the auto-regeneration maximum pace parameter.
     * 
     * @return the auto-regeneration maximum pace 
     */
    public float getAutoRegenerationPaceMax()
    {
        return autoRegenerationPaceMax;
    }

    /**
     * Sets the auto-regeneration maximum pace parameter.
     * 
     * @param autoRegenerationPaceMin the auto-regeneration maximum pace
     */
    public void setAutoRegenerationPaceMax(float pace )
    {
        float   oldVal      = this.autoRegenerationPaceMax;
        float   newVal      = pace;
        String  propName    = AUTO_REGEN_MAX_PN;
        this.autoRegenerationPaceMax = pace;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridColor parameter.
     * 
     * @return the gridColor
     */
    public Color getGridColor()
    {
        return gridColor;
    }

    /**
     * Sets the gridColor parameter.
     * 
     * @param gridColor the gridColor to set
     */
    public void setGridColor(Color gridColor)
    {
        Color   oldVal      = this.gridColor;
        String  propName    = GRID_COLOR_PN;
        this.gridColor = gridColor;
        propChangeSupport.
            firePropertyChange( propName, oldVal, gridColor );
    }

    /**
     * Gets the gridMarginTop parameter.
     * 
     * @return the gridMarginTop
     */
    public int getGridMarginTop()
    {
        return gridMarginTop;
    }

    /**
     * Sets the gridMarginTop parameter.
     * 
     * @param gridMarginTop the gridMarginTop to set
     */
    public void setGridMarginTop(int gridMarginTop)
    {
        int     oldVal      = this.gridMarginTop;
        String  propName    = GRID_MARGIN_TOP_PN;
        this.gridMarginTop = gridMarginTop;
        propChangeSupport.
            firePropertyChange( propName, oldVal, gridMarginTop );
}

    /**
     * Gets the gridMarginLeft parameter.
     * 
     * @return the gridMarginLeft
     */
    public int getGridMarginLeft()
    {
        return gridMarginLeft;
    }

    /**
     * Sets the gridMarginLeft parameter.
     * 
     * @param gridMarginLeft the gridMarginLeft to set
     */
    public void setGridMarginLeft(int gridMarginLeft)
    {
        int     oldVal      = this.gridMarginLeft;
        int     newVal      = gridMarginLeft;
        String  propName    = GRID_MARGIN_LEFT_PN;
        this.gridMarginLeft = gridMarginLeft;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridMarginBottom parameter.
     * 
     * @return the gridMarginBottom
     */
    public int getGridMarginBottom()
    {
        return gridMarginBottom;
    }

    /**
     * Sets the gridMarginBottom parameter.
     * 
     * @param gridMarginBottom the gridMarginBottom to set
     */
    public void setGridMarginBottom(int gridMarginBottom)
    {
        int     oldVal      = this.gridMarginBottom;
        int     newVal      = gridMarginBottom;
        String  propName    = GRID_MARGIN_BOTTOM_PN;
        this.gridMarginBottom = gridMarginBottom;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridMarginRigth parameter.
     * 
     * @return the gridMarginRight
     */
    public int getGridMarginRight()
    {
        return gridMarginRight;
    }

    /**
     * Sets the gridMarginRight parameter.
     * 
     * @param gridMarginRight the gridMarginRight to set
     */
    public void setGridMarginRight(int gridMarginRight)
    {
        int     oldVal      = this.gridMarginRight;
        int     newVal      = gridMarginRight;
        String  propName    = GRID_MARGIN_RIGHT_PN;
        this.gridMarginRight = gridMarginRight;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridWidth parameter.
     * 
     * @return the gridWidth
     */
    public int getGridWidth()
    {
        return gridWidth;
    }

    /**
     * Sets the gridWidth parameter.
     * 
     * @param gridWidth the gridWidth to set
     */
    public void setGridWidth(int gridWidth)
    {
        int     oldVal      = this.gridWidth;
        int     newVal      = gridWidth;
        String  propName    = GRID_WIDTH_PN;
        this.gridWidth = gridWidth;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridHeight parameter.
     * 
     * @return the gridHeight
     */
    public int getGridHeight()
    {
        return gridHeight;
    }

    /**
     * Sets the gridHeight parameter.
     * 
     * @param gridHeight the gridHeight to set
     */
    public void setGridHeight(int gridHeight)
    {
        int     oldVal      = this.gridHeight;
        int     newVal      = gridHeight;
        String  propName    = GRID_HEIGHT_PN;
        this.gridHeight = gridHeight;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridLineShow parameter.
     * 
     * @return the gridLineShow
     */
    public boolean isGridLineShow()
    {
        return gridLineShow;
    }

    /**
     * Sets the gridLineShow parameter.
     * 
     * @param gridLineShow the gridLineShow to set
     */
    public void setGridLineShow(boolean gridLineShow)
    {
        boolean oldVal      = this.gridLineShow;
        boolean newVal      = gridLineShow;
        String  propName    = GRID_LINE_SHOW_PN;
        this.gridLineShow = gridLineShow;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridLineWidth parameter.
     * 
     * @return the gridLineWidth
     */
    public int getGridLineWidth()
    {
        return gridLineWidth;
    }

    /**
     * Sets the gridLineWidth parameter.
     * 
     * @param gridLineWidth the gridLineWidth to set
     */
    public void setGridLineWidth(int gridLineWidth)
    {
        int     oldVal      = this.gridLineWidth;
        int     newVal      = gridLineWidth;
        String  propName    = GRID_LINE_WIDTH_PN;
        this.gridLineWidth = gridLineWidth;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridLineColor parameter.
     * 
     * @return the gridLineColor
     */
    public Color getGridLineColor()
    {
        return gridLineColor;
    }

    /**
     * Sets the gridLineColor parameter.
     * 
     * @param gridLineColor the gridLineColor to set
     */
    public void setGridLineColor(Color gridLineColor)
    {
        Color   oldVal      = this.gridLineColor;
        Color   newVal      = gridLineColor;
        String  propName    = GRID_LINE_COLOR_PN;
        this.gridLineColor = gridLineColor;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridCellSize parameter.
     * 
     * @return the gridCellSize
     */
    public int getGridCellSize()
    {
        return gridCellSize;
    }

    /**
     * Sets the gridCellSize parameter.
     * 
     * @param gridCellSize the gridCellSize to set
     */
    public void setGridCellSize(int gridCellSize)
    {
        int     oldVal      = this.gridCellSize;
        int     newVal      = gridCellSize;
        String  propName    = GRID_CELL_SIZE_PN;
        this.gridCellSize = gridCellSize;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }

    /**
     * Gets the gridCellColor parameter.
     * 
     * @return the gridCellColor
     */
    public Color getGridCellColor()
    {
        return gridCellColor;
    }

    /**
     * Sets the gridCellColor parameter.
     * 
     * @param gridCellColor the gridCellColor to set
     */
    public void setGridCellColor(Color gridCellColor)
    {
        Color   oldVal      = this.gridCellColor;
        Color   newVal      = gridCellColor;
        String  propName    = GRID_CELL_COLOR_PN;
        this.gridCellColor = gridCellColor;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }
    
    /**
     * Gets the GridMap managed by this Parameters object.
     * If the parameter has not been set by the user,
     * it is initialized to an empty map.
     * 
     * @return the GridMap managed by this Parameters object
     */
    public GridMap getGridMap()
    {
        if ( gridMap == null )
            gridMap = new GridMap();
        return gridMap;
    }
    
    /**
     * Sets the GridMap managed by this Parameters object.
     * 
     * @return the GridMap managed by this Parameters object
     */
    public void setGridMap( GridMap gridMap )
    {
        GridMap oldVal      = this.gridMap;
        GridMap newVal      = gridMap;
        String  propName    = GRID_MAP_PN;
        this.gridMap = gridMap;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }
    
    /**
     * Gets the list of survival states that controls the state
     * of cells in the next generation.
     * 
     * @return list of survival states
     */
    public List<Integer> getSurvivalStates()
    {
        return survivalStates;
    }
    
    /**
     * Gets the list of survival states that controls the state
     * of cells in the next generation.
     * 
     * @param   list of survival states
     */
    public void setSurvivalStates( List<Integer> survivalStates )
    {
        List<Integer>   oldVal      = this.survivalStates;
        List<Integer>   newVal      = survivalStates;
        String          propName    = CTRL_SURVIVAL_STATES_PN;
        this.survivalStates = survivalStates;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }
    
    /**
     * Gets the list of birth states that controls the state
     * of cells in the next generation.
     * 
     * @return list of birth states
     */
    public List<Integer> getBirthStates()
    {
        return birthStates;
    }
    
    /**
     * Sets the list of birth states that controls the state
     * of cells in the next generation.
     * 
     * @param   list of birth states
     */
    public void setBirthStates( List<Integer> birthStates )
    {
        List<Integer>   oldVal      = this.birthStates;
        List<Integer>   newVal      = birthStates;
        String          propName    = CTRL_BIRTH_STATES_PN;
        this.birthStates = birthStates;
        propChangeSupport.
            firePropertyChange( propName, oldVal, newVal );
    }
    
    public Point getGridCellOrigin()
    {
        return gridCellOrigin;
    }
    
    public void setGridCellOrigin( Point gridCellOrigin )
    {
        Point   oldValue    = this.gridCellOrigin;
        Point   newValue    = gridCellOrigin;
        String  propName    = GRID_CELL_ORIGIN_PN;
        this.gridCellOrigin = gridCellOrigin;
        propChangeSupport.
            firePropertyChange( propName, oldValue, newValue );
    }
    
    /**
     * Gets a value that indicates if the the grid should attempt
     * to center its live cells.
     * 
     * @return the value described above
     */
    public boolean isGridCenter()
    {
        return gridCenter;
    }

    /**
     * Sets a value that indicates if the the grid should attempt
     * to center its live cells.
     * This method propagates a PropertyChange event
     * for property GOLConstants.CTRL_CENTER_PN.
     * 
     * @param gridCenter    true to center the grid
     */
    public void setGridCenter( boolean gridCenter )
    {
        Boolean oldValue    = this.gridCenter;
        Boolean newValue    = gridCenter;
        String  propName    = CTRL_CENTER_PN;
        this.gridCenter = gridCenter;
        propChangeSupport.
            firePropertyChange( propName, oldValue, newValue );
    }
    
    /**
     * Returns the value of a URL for using to populate a grid.
     * Will return null if there is no such URL.
     * 
     * @return  the value of a URL for using to populate a grid,
     *          or null if none
     */
    public URL getGridURL()
    {
        return gridURL;
    }
    
    /**
     * Sets the URL of data which may be used to populate the grid.
     * May be null.
     * This method propagates a PropertyChange event
     * for property GOLConstants.CTRL_GRID_URL_PN.
     * 
     * @param gridURL
     */
    public void setGridURL( URL gridURL )
    {
        URL     oldValue    = this.gridURL;
        URL     newValue    = gridURL;
        String  propName    = CTRL_GRID_URL_PN;
        this.gridURL = gridURL;
        propChangeSupport.
            firePropertyChange( propName, oldValue, newValue );
    }
    
    /**
     * Returns a value indicating whether the application
     * should attempt to keep the grid centered
     * with each new generation.
     * 
     * @return  a value indicating whether the application
     *          should attempt to keep the grid centered
     *          with each new generation
     */
    public boolean getGridKeepCentered()
    {
        return gridKeepCentered;
    }
    
    /**
     * Sets a value indicating whether the application
     * should attempt to keep the grid centered
     * with each new generation.
     * This method propagates a PropertyChange event
     * for property GOLConstants.GRID_KEEP_CENTERED_PN.
     * 
     * @param keepCentered
     */
    public void setGridKeepCentered( boolean keepCentered )
    {
        boolean oldValue    = this.gridKeepCentered;
        boolean newValue    = keepCentered;
        String  propName    = GRID_KEEP_CENTERED_PN;
        this.gridKeepCentered = keepCentered;
        propChangeSupport.
            firePropertyChange( propName, oldValue, newValue );
    }
    
    /**
     * Fires an ActionEvent to ActionListeners.
     */
    public void reset()
    {
        ActionEvent event   =
            new ActionEvent( this, ActionEvent.ACTION_PERFORMED, null );
        for ( ActionListener listener : actionListeners )
            listener.actionPerformed( event );
    }
}
