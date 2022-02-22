package com.gmail.johnstraub1954.game_of_life.main;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.*;

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
    
    /** Support for PropertyChangeListeners */
    private final PropertyChangeSupport propChangeSupport   = 
        new PropertyChangeSupport( this );

    /**
     * Constructor.
     */
    private Parameters()
    {
        GOLProperties   props   = new GOLProperties();
        autoRegenerationOn = props.getAutoRegenOn();
        autoRegenerationPace = props.getAutoRegenPace();
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
    public double getAutoRegenerationPace()
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
}
