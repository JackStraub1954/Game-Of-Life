package com.gmail.johnstraub1954.game_of_life.main;

import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.AUTO_REGEN_ON_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.AUTO_REGEN_ON_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.AUTO_REGEN_PACE_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.AUTO_REGEN_PACE_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_COLOR_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_SIZE_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_CELL_SIZE_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_COLOR_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_HEIGHT_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_HEIGHT_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_COLOR_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_COLOR_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_SHOW_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_SHOW_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_WIDTH_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_LINE_WIDTH_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_BOTTOM_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_BOTTOM_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_LEFT_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_LEFT_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_RIGHT_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_RIGHT_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_TOP_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_MARGIN_TOP_PN;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_WIDTH_DV;
import static com.gmail.johnstraub1954.game_of_life.main.GOLConstants.GRID_WIDTH_PN;

import java.awt.Color;
import java.util.Properties;

/**
 * @author Jack Straub
 *
 */
public class GOLProperties extends Properties
{
    /** Generated serial version UID */
    private static final long serialVersionUID = 998230253608692257L;

    /**
     * Default constructor.
     */
    public GOLProperties()
    {
        super();
    }

    /**
     * Constructor providing the initial capacity of this object.
     * 
     * @param initialCapacity   initial capacity of this object
     */
    public GOLProperties( int initialCapacity )
    {
        super( initialCapacity );
    }

    /**
     * Constructor providing a set of default values for properties.
     * 
     * @param defaults  the set of default values for properties
     */
    public GOLProperties( Properties defaults )
    {
        super( defaults );
    }
    
    /**
     * Obtains the value of the GRID COLOR property.
     * 
     * @return the value of the GRID COLOR property.
     */
    public Color getGridColor()
    {
        Color   color   = getColor( GRID_COLOR_PN, GRID_COLOR_DV );
        return color;
    }
    
    /**
     * Obtains the value of the GRID MARGIN-TOP property.
     * 
     * @return the value of the GRID MARGIN-TOP property
     */
    public int getGridMarginTop()
    {
        int size    = getSize( GRID_MARGIN_TOP_PN, GRID_MARGIN_TOP_DV );
        return size;
    }
    
    /**
     * Obtains the value of the GRID MARGIN-LEFT property.
     * 
     * @return the value of the GRID MARGIN-LEFT property
     */
    public int getGridMarginLeft()
    {
        int size    = getSize( GRID_MARGIN_LEFT_PN, GRID_MARGIN_LEFT_DV );
        return size;
    }
    
    /**
     * Obtains the value of the GRID MARGIN-BOTTOM property.
     * 
     * @return the value of the GRID MARGIN-BOTTOM property
     */
    public int getGridMarginBottom()
    {
        int size    = getSize( GRID_MARGIN_BOTTOM_PN, GRID_MARGIN_BOTTOM_DV );
        return size;
    }
    
    /**
     * Obtains the value of the GRID MARGIN-RIGHT property.
     * 
     * @return the value of the GRID MARGIN-RIGHT property
     */
    public int getGridMarginRight()
    {
        int size    = getSize( GRID_MARGIN_RIGHT_PN, GRID_MARGIN_RIGHT_DV );
        return size;
    }
    
    /**
     * Obtains the value of the GRID WIDTH property.
     * 
     * @return the value of the GRID WIDTH property
     */
    public int getGridWidth()
    {
        int size    = getInt( GRID_WIDTH_PN, GRID_WIDTH_DV );
        return size;
    }
    
    /**
     * Obtains the value of the GRID HEIGHT property.
     * 
     * @return the value of the GRID HEIGHT property
     */
    public int getGridHeight()
    {
        int size    = getInt( GRID_HEIGHT_PN, GRID_HEIGHT_DV );
        return size;
    }
    
    /**
     * Obtains the value of the GRID-LINE-SHOW property.
     * 
     * @return the value of the GRID-LINE-SHOW property
     */
    public boolean getGridLineShow()
    {
        boolean show    = getBoolean( GRID_LINE_SHOW_PN, GRID_LINE_SHOW_DV );
        return show;
    }
    
    /**
     * Obtains the value of the GRID LINE WIDTH property.
     * 
     * @return the value of the GRID LINE WIDTH property
     */
    public int getGridLineWidth()
    {
        int size    = getSize( GRID_LINE_WIDTH_PN, GRID_LINE_WIDTH_DV );
        return size;
    }
    
    /**
     * Obtains the value of the GRID LINE COLOR property.
     * 
     * @return the value of the GRID LINE COLOR property.
     */
    public Color getGridLineColor()
    {
        Color   color   = getColor( GRID_LINE_COLOR_PN, GRID_LINE_COLOR_DV );
        return color;
    }
    
    /**
     * Obtains the value of the GRID CELL WIDTH property.
     * 
     * @return the value of the GRID CELL WIDTH property
     */
    public int getGridCellSize()
    {
        int size    = getSize( GRID_CELL_SIZE_PN, GRID_CELL_SIZE_DV );
        return size;
    }
    
    /**
     * Obtains the value of the GRID CELL COLOR property.
     * 
     * @return the value of the GRID CELL COLOR property.
     */
    public Color getGridCellColor()
    {
        Color   color   = getColor( GRID_CELL_COLOR_PN, GRID_CELL_COLOR_DV );
        return color;
    }
    
    /**
     * Obtains the value of the AUTO-REGENERATION ON property.
     * 
     * @return the value of the AUTO-REGENERATION ON property.
     */
    public boolean getAutoRegenOn()
    {
        boolean isOn    = getBoolean( AUTO_REGEN_ON_PN, AUTO_REGEN_ON_DV );
        return isOn;
    }
    
    /**
     * Obtains the value of the AUTO-REGENERATION PACE property.
     * 
     * @return the value of the AUTO-REGENERATION PACE property.
     */
    public float getAutoRegenPace()
    {
        float   pace    = getFloat( AUTO_REGEN_PACE_PN, AUTO_REGEN_PACE_DV );
        return pace;
    }

    /**
     * Retrieves a property value and converts it to a Color.
     * If the value of the property begins with an alphabetic character
     * it will be treated as a color name,
     * otherwise it will be treated as an integer.
     * If the value is expected to be an integer,
     * but cannot be converted to an integer,
     * an IllegalStateException is thrown.
     * If the property name cannot be found
     * <em>defValue</em> will be returned.
     * 
     * @param name      the name of the property
     * @param defValue  the default value, 
     *                  to be returned if the property name is not found
     *                  in this property set
     *                  
     * @return  the value of the named property,
     *          or <em>defValue</em> if the property name is not found
     *          
     * @throws IllegalArgumentException if <em>defValue</em> is null
     *          
     * @throws  IllegalStateException if the property value
     *          is expected to be an integer
     *          but cannot be converted to an integer 
     */
    public Color getColor( String name, String defValue )
        throws IllegalArgumentException, IllegalStateException
    {
        if ( defValue == null )
        {
            String  message = "Default value may not be null";
            throw new IllegalArgumentException( message );
        }
        
        Color   result  = null;
        String  value   = getProperty( name, defValue );
        
        if ( Character.isAlphabetic( value.charAt( 0 ) ) )
        {
            result = Color.getColor( value );
            if ( result == null )
            {
                String  message = "\"" + value + "\" is not a valid color";
                throw new IllegalStateException( message );
            }
        }
        else
        {
            try
            {
                int     radix   = 10;
                String  strNum  = value;
                if ( value.startsWith( "0x" ) )
                {
                    radix = 16;
                    strNum = value.substring( 2 );
                }
                else if ( value.startsWith( "0b" ) )
                {
                    radix = 2;
                    strNum = value.substring( 2 );
                }
                else if ( !value.equals( "0" ) &&value.startsWith( "0" ) )
                {
                    radix = 8;
                    strNum = value.substring( 1 );
                }
                else
                    ;
                
                int intColor    = Integer.parseInt( strNum, radix );
                result = new Color( intColor );
            }
            catch ( NumberFormatException exc )
            {
                String  message = "\"" + value + "\" is not a valid color";
                throw new IllegalStateException( message, exc );
            }
        }   
        return result;
    }
    
    /**
     * Obtains the value of a property and converts it to a size.
     * The value may end with "px" (for pixels) or "e" (for EMs).
     * 
     * @param name      the name of the property to obtain
     * @param defValue  the default value to use if the property name
     *                  can't be found
     *                  
     * @return the value of the property converted to a size
     * 
     * @throws IllegalArgumentException if <em>defValue</em> is null
     * @throws IllegalStateException if the value of the property
     *         cannot be converted to a size
     */
    public int getSize( String name, String defValue )
        throws IllegalArgumentException, IllegalStateException
    {
        if ( defValue == null )
        {
            String  message = "Default value may not be null";
            throw new IllegalArgumentException( message );
        }
        
        int     size        = 0;
        String  value       = getProperty( name, defValue );
        String  strNum      = value;
        int     xier        = 1;
        int     len         = value.length();
        
        if ( value.endsWith( "px" ) )
            strNum = value.substring( 0, len - 2 );
        else if ( value.endsWith( "em" ) )
        {
            strNum = value.substring( 0, len - 2 );
            xier = 16;
        }
        else
            ;
        
        try
        {
            size = Integer.parseInt( strNum ) * xier;
        }
        catch ( NumberFormatException exc )
        {
            String  message = "\"" + value + "\" is not a valid color";
            throw new IllegalStateException( message, exc );
        }
        
        return size;
    }   
    
    /**
     * Obtains the value of a property and converts it to an int.
     * 
     * @param name      the name of the property to obtain
     * @param defValue  the default value to use if the property name
     *                  can't be found
     *                  
     * @return the named property converted to an int
     * 
     * @throws IllegalArgumentException if <em>defValue</em> is null
     * @throws IllegalStateException if the value of the property
     *         cannot be converted to a size
     */
    public int getInt( String name, String defValue )
        throws IllegalArgumentException, IllegalStateException
    {
        if ( defValue == null )
        {
            String  message = "Default value may not be null";
            throw new IllegalArgumentException( message );
        }
        
        int     size        = 0;
        String  value       = getProperty( name, defValue );
        try
        {
            size = Integer.parseInt( value );
        }
        catch ( NumberFormatException exc )
        {
            String  message = "\"" + value + "\" is not a valid size";
            throw new IllegalStateException( message, exc );
        }
        
        return size;
    }   
    
    /**
     * Obtains the value of a property and converts it to an boolean.
     * 
     * @param name      the name of the property to obtain
     * @param defValue  the default value to use if the property name
     *                  can't be found
     *                  
     * @return the named property converted to a boolean.
     * 
     * @throws IllegalArgumentException if <em>defValue</em> is null
     */
    public boolean getBoolean( String name, String defValue )
        throws IllegalArgumentException
    {
        if ( defValue == null )
        {
            String  message = "Default value may not be null";
            throw new IllegalArgumentException( message );
        }
        
        boolean show        = false;
        String  value       = getProperty( name, defValue );
        show = Boolean.parseBoolean( value );
        
        return show;
    }   
    
    /**
     * Obtains the value of a property and converts it to a float.
     * 
     * @param name      the name of the property to obtain
     * @param defValue  the default value to use if the property name
     *                  can't be found
     *                  
     * @return the named property converted to an float
     * 
     * @throws IllegalArgumentException if <em>defValue</em> is null
     * @throws IllegalStateException if the value of the property
     *         cannot be converted to a size
     */
    public float getFloat( String name, String defValue )
        throws IllegalArgumentException, IllegalStateException
    {
        if ( defValue == null )
        {
            String  message = "Default value may not be null";
            throw new IllegalArgumentException( message );
        }
        
        float   num         = 0;
        String  value       = getProperty( name, defValue );
        try
        {
            num = Float.parseFloat( value );
        }
        catch ( NumberFormatException exc )
        {
            String  message = "\"" + value + "\" is not a valid float";
            throw new IllegalStateException( message, exc );
        }
        
        return num;
    }   
}
