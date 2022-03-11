package com.gmail.johnstraub1954.game_of_life.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class RLEDescriptor
{
    /** For formatting the date/time for use in header comment. */
    private static final DateTimeFormatter  timeFormatString    =
        DateTimeFormatter.ofPattern( "uuuu/MMM/dd HH:mm:ss" );
    
    /** For formatting the header line. */
    private static final String headerLineFormat    =
        "x = %d, y = %d, rule = %s/%s";
    
    private final List<String>  comments        = new ArrayList<>();
    private List<Integer>       birthRules      = new ArrayList<>();
    private List<Integer>       survivalRules   = new ArrayList<>();
    private String              name            = null;
    private String              authorName      = null;
    private String              authorEmail     = null;
    private LocalDateTime       authorTime      = null;
    private Point               upperLeftCorner = null;
    private GridMap             gridMap         = null;
    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return the author
     */
    public String getAuthorName()
    {
        return authorName;
    }
    /**
     * @param authorName the author to set
     */
    public void setAuthor(String authorName )
    {
        this.authorName = authorName;
    }
    /**
     * @return the authorEmail
     */
    public String getAuthorEmail()
    {
        return authorEmail;
    }
    /**
     * @param authorEmail the authorEmail to set
     */
    public void setAuthorEmail(String authorEmail)
    {
        this.authorEmail = authorEmail;
    }
    /**
     * @return the authorTime
     */
    public LocalDateTime getAuthorTime()
    {
        return authorTime;
    }
    /**
     * @param authorTime the authorTime to set
     */
    public void setAuthorTime(LocalDateTime authorTime)
    {
        this.authorTime = authorTime;
    }
    /**
     * @return the upperLeftCorner
     */
    public Point getUpperLeftCorner()
    {
        return upperLeftCorner;
    }
    /**
     * @param upperLeftCorner the upperLeftCorner to set
     */
    public void setUpperLeftCorner(Point upperLeftCorner)
    {
        this.upperLeftCorner = upperLeftCorner;
    }
    /**
     * @return the comments
     */
    public List<String> getComments()
    {
        return comments;
    }
    
    /**
     * Sets the entire list of birth rules. 
     * If the input is null, it is converted to an empty list.
     * 
     * @param birthRules    the new list of birth rules
     */
    public void setBirthRules( List<Integer> birthRules )
    {
        if ( birthRules == null )
            this.birthRules = new ArrayList<>();
        else
            this.birthRules = birthRules;
    }
    
    /**
     * Adds a single rule to the list of birth rules.
     * 
     * @param rule  the rule to add to the list
     */
    public void addBirthRule( int rule )
    {
        this.birthRules.add( rule );
    }
    
    /**
     * Get the list of birth rules.
     * 
     * @return the list of birthRules
     */
    public List<Integer> getBirthRules()
    {
        return birthRules;
    }
    
    /**
     * Sets the entire list of survival rules. 
     * If the input is null, it is converted to an empty list.
     * 
     * @param survivalRules    the new list of survival rules
     */
    public void setSurvivalRules( List<Integer> survivalRules )
    {
        if ( survivalRules == null )
            this.survivalRules = new ArrayList<>();
        else
            this.survivalRules = survivalRules;
    }
    
    /**
     * Adds a single rule to the list of survival rules.
     * 
     * @param rule  the rule to add to the list
     */
    public void addSurvivalRule( int rule )
    {
        this.survivalRules.add( rule );
    }
    
    /**
     * Gets the list of survival rules.
     * 
     * @return the list of survivalRules
     */
    public List<Integer> getSurvivalRules()
    {
        return survivalRules;
    }
    
    /**
     * Add a comment or comments to the list of comments.
     * The comments are passed as a single string, 
     * but each line gets parsed, trimmed and added as 
     * an individual comment.
     * 
     * @param comments  the comments to add, as a single string
     * 
     * @throws GOLException if an exception occurs while reading
     *         lines from the input string
     */
    public void addComments( String comments )
    {
        try ( StringReader    strReader   = new StringReader( comments );
              BufferedReader  bufReader   = new BufferedReader( strReader );
        )
        {
            List<String>    lines   =
                bufReader.lines()
                .map( s -> s.trim() )
                .collect( Collectors.toList() );
            this.comments.addAll( lines );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            String  message = 
                "Unexpected I/O error when reading from string source";
            throw new GOLException( message, exc );
        }
    }
    
    /**
     * Removes a single comment from the list of comments.
     * The comment will be trimmed prior to comparing to the comments
     * in the list.
     * 
     * @param comment   the comment to remove.
     */
    public void removeComment( String comment )
    {
        comments.remove( comment.trim() );
    }
    
    /**
     * @return
     */
    public List<String> getHeaderComments()
    {
        List<String>    headerComments  = 
            comments
            .stream()
            .map( s -> "#C " + s )
            .collect( Collectors.toCollection( ArrayList::new ) );
        if ( name != null && !name.isEmpty() )
            headerComments.add( "N " + name );
        
        // Only add author data if author name is present.
        // If author name is not present, author email and date
        // are ignored.
        if ( authorName != null && !authorName.isEmpty() )
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( "#O " ).append( authorName );
            if ( authorEmail != null && !authorEmail.isEmpty() )
                bldr.append( ' ' ).append( authorEmail );
            if ( authorTime != null )
                bldr.append( ' ' )
                .append( authorTime.format( timeFormatString ) );
            headerComments.add( bldr.toString() );
        }
        
        return headerComments;
    }
    
    /**
     * Returns the formatted header line of an RLE file, for example:
     * <pre>
     *     x = 25, y = 35, rule = B3/S23</pre>
     *     
     * If the upper left corner has not been specified 
     * an attempt will be made to obtain it from the encapsulated gridMap;
     * if there is no grid, the upper left corner will default to (0,0).
     * 
     * If one or both rules have not been specified
     * the missing rule will be replaced with the original
     * Conway rules (B3/S23).
     * The order in which the birth and survival rules are returned
     * is not specified.
     * 
     * @return the formatted header line of RLE file
     */
    public String getHeaderLine()
    {
        if ( birthRules.isEmpty() )
            birthRules.add( 3 );
        if ( survivalRules.isEmpty() )
            survivalRules.addAll( List.of( 2, 3 ) );
        
        if ( upperLeftCorner == null )
        {
            if ( gridMap == null )
                upperLeftCorner = new Point( 0, 0 );
            else
                upperLeftCorner = gridMap.getUpperLeftCorner();
        }
        
        StringBuilder   bBldr   = new StringBuilder( "B" );
        birthRules.forEach( i -> bBldr.append( i ) );
        StringBuilder   sBldr   = new StringBuilder( "S" );
        birthRules.forEach( i -> sBldr.append( i ) );
        String  line    = String.format(
            headerLineFormat,
            upperLeftCorner.x, 
            upperLeftCorner.y,
            bBldr,
            sBldr
        );
        
        return line;
    }
    
    private static class CellIterator implements Iterator<Character>
    {
        private final GridMap   gridMap;
        private final Rectangle liveRect;
        private final int       firstX;
        private final int       lastX;
        private final int       firstY;
        private final int       lastY;
        private final int       lastRow;
        private final int       lastRowX;
        
        private int             nextX;
        private int             nextY;
        // initial value of lastChar should be anything OTHER than:
        // 'o', 'b', '$', '!'
        private char            lastChar    = ' ';
        
        public CellIterator( GridMap map )
        {
            gridMap = map;
            liveRect = gridMap.getLiveRectangle();
            firstX = liveRect.x;
            lastX = firstX + liveRect.width;
            firstY = liveRect.y;
            lastY = firstY + liveRect.height;
            nextX = firstX;
            nextY = firstY;
            
            // find the last live cell in the last row
            lastRow = lastY - 1;
            int lastCol = 0;
            for ( int inx = firstX ; inx < lastX ; ++inx )
            {
                Cell    cell    = gridMap.get( inx, lastRow );
                if ( cell.isAlive() )
                    lastCol = cell.getXco();
            }
            lastRowX = lastCol;
        }
        
        @Override
        public boolean hasNext()
        {
            boolean result  = lastChar == '!';
            return result;
        }
        @Override
        public Character next()
        {
            if ( lastChar == '!' )
                throw new NoSuchElementException( "iterator overflow" );

            if ( nextY == lastRow && nextX > lastRowX )
                lastChar = '!';
            else if ( nextX >= lastX )
            {
                nextX = firstX;
                ++nextY;
                lastChar = '$';
            }
            else
            {
                Cell    cell    = gridMap.get( nextX++, nextY );
                lastChar = cell.isAlive() ? 'o' : 'b';
            }
            
            return lastChar;
        }
    }
}
