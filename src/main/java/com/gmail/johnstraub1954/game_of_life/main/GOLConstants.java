package com.gmail.johnstraub1954.game_of_life.main;

/**
 * Encapsulates the constants used throughout 
 * the Game of Life code.
 * 
 * @author Jack Straub
 *
 */
public class GOLConstants
{
    ////////////////////////////////////////
    //
    // GRID PROPERTIES
    //
    ////////////////////////////////////////
    
    /** 
     * Grid color property name. 
     * The grid color is the background color
     * of the grid.
     * This may be a color name or an integer (usually in hexadecimal format).
     * Color names must begin with an alphabetic character
     * and be present in the system properties.
     */
    public static final String  GRID_COLOR_PN       = "gridColor";
    /** Grid width default value. */
    public static final String  GRID_COLOR_DV       = "0";

    /** 
     * Grid margin top property name. 
     * The top margin is the blank space above the grid.
     * May be specified in pixels (px) or EMs (em);
     * pixels is the default.
     */
    public static final String  GRID_MARGIN_TOP_PN   = "gridMarginTop";
    /** Grid margin top default value. */
    public static final String  GRID_MARGIN_TOP_DV   = "1em";

    /** 
     * Grid margin left property name. 
     * The left margin is the blank space to the left
     * of the the grid.
     * May be specified in pixels (px) or EMs (em);
     * pixels is the default.
     */
    public static final String  GRID_MARGIN_LEFT_PN  = "gridMarginLeft";
    /** Grid margin left default value. */
    public static final String  GRID_MARGIN_LEFT_DV  = "1em";

    /** 
     * Grid margin bottom property name. 
     * The bottom margin is the blank space below the grid.
     * May be specified in pixels (px) or EMs (em);
     * pixels is the default.
     */
    public static final String  GRID_MARGIN_BOTTOM_PN   = "gridMarginBottom";
    /** Grid margin top default value. */
    public static final String  GRID_MARGIN_BOTTOM_DV   = "1em";

    /** 
     * Grid margin right property name. 
     * The right margin is the blank space to the right
     * of the the grid.
     * May be specified in pixels (px) or EMs (em);
     * pixels is the default.
     */
    public static final String  GRID_MARGIN_RIGHT_PN = "gridMarginRight";
    /** Grid margin left default value. */
    public static final String  GRID_MARGIN_RIGHT_DV ="1em";
    
    /** 
     * Grid width property name. 
     * The grid width is number of physical cells
     * that are drawn along the x-axis 
     * when the grid is refreshed.
     */
    public static final String  GRID_WIDTH_PN       = "gridWidth";
    /** Grid width default value. */
    public static final String  GRID_WIDTH_DV       = "100";
    
    /** 
     * Grid height property name. 
     * The grid height is number of physical cells
     * that are drawn along the y-axis 
     * when the grid is refreshed.
     */
    public static final String  GRID_HEIGHT_PN      = "gridHeight";
    /** Grid width default value. */
    public static final String  GRID_HEIGHT_DV      = "100";

    /** 
     * Show-grid-line property name. 
     * A Boolean value that indicates 
     * whether grid lines are to be draw around each cell.
     */
    public static final String  GRID_LINE_SHOW_PN   = "gridLineShow";
    /** Show-grid-line default value. */
    public static final String  GRID_LINE_SHOW_DV   = "true";

    /** 
     * Grid line width property name. 
     * The grid width is width of the lines
     * drawn around a cell.
     * May be specified in pixels (px) or EMs (em);
     * pixels is the default.
     */
    public static final String  GRID_LINE_WIDTH_PN   = "gridLineWidth";
    /** Grid line width default value. */
    public static final String  GRID_LINE_WIDTH_DV   = "1px";

    /** 
     * Grid line color property name. 
     * The grid color is color used to draw the grid lines.
     * This may be a color name or an integer (usually in hexadecimal format).
     * Color names must begin with an alphabetic character
     * and be present in the system properties.
     */
    public static final String  GRID_LINE_COLOR_PN   = "gridLineColor";
    /** Grid line color default value. */
    public static final String  GRID_LINE_COLOR_DV   = "0";

    /** 
     * Grid cell width property name. 
     * The grid cell width is width of a cell
     * <em>not</em> including the grid lines.
     * May be specified in pixels (px) or EMs (em);
     * pixels is the default.
     */
    public static final String  GRID_CELL_SIZE_PN   = "gridCellSize";
    /** Grid line width default value. */
    public static final String  GRID_CELL_SIZE_DV   = "10";

    /** 
     * Grid cell color property name. 
     * The grid cell color is the color used to draw live cells
     * (dead cells are drawn in the background color of the grid).
     * This may be a color name or an integer (usually in hexadecimal format).
     * Color names must begin with an alphabetic character
     * and be present in the system properties.
     */
    public static final String  GRID_CELL_COLOR_PN   = "gridCellColor";
    /** Grid line color default value. */
    public static final String  GRID_CELL_COLOR_DV   = "0";
    
    ////////////////////////////////////////
    //
    // AUTOREGENERATION PROPERTIES
    //
    ////////////////////////////////////////
    
    /** 
     * Auto-regeneration-on property name.
     * May be <em>true</em> or <em>false</em>.
     */
    public static final String  AUTO_REGEN_ON_PN    = "autoRegenerationOn";
    /** Auto-regeneration default value */
    public static final String  AUTO_REGEN_ON_DV    = "false";
    /**
     * Auto-regeneration pace, property name.
     * Specified in generations per second.
     */
    public static final String  AUTO_REGEN_PACE_PN  = "autoRegenerationPace";
    /** Auto-regeneration pace, default value */
    public static final String  AUTO_REGEN_PACE_DV  = "1";
}
