package com.gmail.johnstraub1954.game_of_life.main;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RLEInput
{
    /** 
     * Regular expression for parsing rules, such as:
     * <br>&nbsp;&nbsp;&nbsp;&nbsp;
     * x=100,y=200,rule=23/3
     * <br>&nbsp;&nbsp;&nbsp;&nbsp;
     * x = 100 , y = 200 , rule = 23/3
     * <br>&nbsp;&nbsp;&nbsp;&nbsp;
     * 
     * @see #rulePattern
     * @see #parseHeader(String, BufferedReader)
     */
    private static final String  ruleRegEx  = 
        "x\\s*=\\s*(\\d+)\\s*,*\\s*"
        + "y\\s*=\\s*(\\d+)\\s*,*"
        + "\\s*rule\\s*=\\s*"
        + "(\\d+)\\s*/\\s*(\\d+)";
    
    /**
     * Compiled pattern for parsing rules
     * 
     * @see #ruleRegEx
     * @see #parseHeader(String, BufferedReader)
     */
    private static final Pattern    rulePattern = 
        Pattern.compile( ruleRegEx, Pattern.CASE_INSENSITIVE );
    
    /** Used for reading input file */
    private BufferedReader reader  = null;
    
    /** true if the header line was present in the input */
    private boolean     headerPresent   = false;
    
    private final List<String>  comments        = new ArrayList<>();
    private final List<Integer> survivalRules   = new ArrayList<>();
    private final List<Integer> birthRules      = new ArrayList<>();
    private int                 gameStates      = -1;
    private String              name            = "Unnamed";
    private String              author          = "Unknown";
    private Point               upperLeft       = new Point( 0, 0 );
    private RLEGridDecoder      gridDecoder;
    private GridMap             gridMap         = null;

    public RLEInput( String path )
    {
        this( new File( path ) );
    }
    
    public RLEInput( File path )
    {
        try
        {
            if ( !path.exists() )
            {
                String  message = path.getPath() + ": not found";
                throw new GOLException( message );
            };
            URL url = path.toURI().toURL();
            parse( url );
        }
        catch ( IOException exc )
        {
            throw new GOLException( exc );
        }
    }
    
    public RLEInput( URL url )
    {
        try
        {
            parse( url );
        }
        catch ( IOException exc )
        {
            throw new GOLException( exc );
        }
    }
    
    public RLEInput( InputStream inStream )
    {
        try
        {
            parse( inStream );
        }
        catch ( IOException exc )
        {
            throw new GOLException( exc );
        }
    }

    /**
     * @return the gameStates
     */
    public int getGameStates()
    {
        return gameStates;
    }

    /**
     * @param gameStates the gameStates to set
     */
    public void setGameStates(int gameStates)
    {
        this.gameStates = gameStates;
    }

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
    public String getAuthor()
    {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }

    /**
     * @return the upperLeft
     */
    public Point getUpperLeft()
    {
        return upperLeft;
    }

    /**
     * @param upperLeft the upperLeft to set
     */
    public void setUpperLeft(Point upperLeft)
    {
        this.upperLeft = upperLeft;
    }

    /**
     * @return the gridMap
     */
    public GridMap getGridMap()
    {
        return gridMap;
    }

    /**
     * @param gridMap the gridMap to set
     */
    public void setGridMap(GridMap gridMap)
    {
        this.gridMap = gridMap;
    }

    /**
     * @return the comments
     */
    public List<String> getComments()
    {
        return comments;
    }

    /**
     * @return the survivalRules
     */
    public List<Integer> getSurvivalRules()
    {
        return survivalRules;
    }

    /**
     * @return the birthRules
     */
    public List<Integer> getBirthRules()
    {
        return birthRules;
    }
    
    public RLEGridDecoder getGridDecoder()
    {
        return gridDecoder;
    }
    
    private void parse( URL url ) throws IOException
    {
        try ( InputStream inStream = url.openStream(); )
        {
            parse( inStream );
        }
    }
        
    private void parse( InputStream inStream ) throws IOException
    {
        try ( 
            InputStreamReader inReader = new InputStreamReader( inStream );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            reader = bufReader;
            String  line    = parseComments();
            if ( line != null )
            {
                bufReader.mark( 10000 );
                line = parseHeader( line );
                // there's a good chance that parsing
                // the front matter will read
                // the first line of the input sequence,
                // so put stream back to where it was before
                // parsing the header.
                if ( headerPresent )
                    bufReader.reset();
            }
            gridDecoder = new RLEGridDecoder( bufReader );
        }
    }
    
    /**
     * Parses all comments from the header of the RLE source.
     * Returns the first non-comment line from the source, 
     * or null if the source is exhausted.
     * In particular, if a non-null string is returned
     * the user should be prepared for that line
     * to be the header line
     * (<em>x = m, y = n, rule = abc</em>).
     * @param reader
     * @return
     * @throws IOException
     */
    private String parseComments() throws IOException
    {
        boolean done    = false;
        String  line    = null;
        while ( !done && (line = nextLine( reader )) != null )
        {
            // note: nextLine never returns an empty line
            if ( line.charAt( 0 ) == '#' )
                parseComment( line );
            else
                done = true;
        }
        
        return line;
    }
    
    /**
     * Attempts to parse a given line as a header line
     * (<em>x = m, y = n, rule = abc</em>).
     * @param line
     * @param reader
     * @return
     * @throws IOException
     * 
     * @see #ruleRegEx
     * @see #rulePattern
     */
    private String parseHeader( String line )
        throws IOException
    {
        String  next        = line;
        Matcher matcher     = rulePattern.matcher( line );
        boolean found       = matcher.find();
        
        if ( found )
        {
            headerPresent = true;
            int matchCount  = matcher.groupCount();
            if ( matchCount < 4 || matchCount > 5 )
            {
                String  message =
                    "for RLE header string \"" + line
                    + "\": invalid match count ("
                    + matchCount + ")";
                throw new GOLException( message );
            }

            int     xco     = Integer.parseInt( matcher.group( 1 ) );
            int     yco     = Integer.parseInt( matcher.group( 2 ) );
            upperLeft = new Point( xco, yco );
            
            String  str = matcher.group( 3 );
            int     len = str.length();
            for ( int inx = 0 ; inx < len ; ++inx )
            {
                String  rule    =  "" + str.charAt( inx );
                survivalRules.add( Integer.parseInt( rule ) );
            }
            
            str = matcher.group( 4 );
            len  = str.length();
            for ( int inx = 0 ; inx < len ; ++inx )
            {
                String  rule    =  "" + str.charAt( inx );
                birthRules.add( Integer.parseInt( rule ) );
            }
            
            if ( matchCount > 4 )
            {
                str = matcher.group( 5 );
                if ( str != null )
                    gameStates = Integer.parseInt( str );
            }

            next = nextLine( reader );
        }
        
        return next;
    }
    
    private void parseComment( String line )
    {
        if ( line.length() < 2 )
            return;
        
        String  comment = line.substring( 1 );
        switch ( comment.charAt( 0 ) )
        {
        case 'C':
        case 'c':
            if ( comment.length() > 1 )
                comments.add( comment.substring( 1 ) );
            break;
        case 'N':
            if ( comment.length() > 1 )
                name = comment;
            break;
        case 'O':
            if ( comment.length() > 1 )
                author = comment.substring( 1 );
            break;
        case 'P':
        case 'R':
            parseUpperLeft( comment );
            break;
        case 'r':
            parseRules( comment );
            break;
        default:
            String  message =
            "Invalid RLE comment: " + comment;
            throw new GOLException( message );
        }
    }
    
    private void parseUpperLeft( String comment )
    {
        String[]    nums    = 
            comment.substring( 1 ).strip().split( "\\S" );
        if ( nums.length != 2 )
        {
            String  message = "Invalid comment in input stream:" + comment;
            throw new GOLException( message );
        }
        try
        {
            int xco = Integer.parseInt( nums[0] );
            int yco = Integer.parseInt( nums[1] );
            upperLeft = new Point( xco, yco );
        }
        catch ( NumberFormatException exc )
        {
            String  message = "Invalid ulc coordinates: " + comment;
            throw new GOLException( message );
        }
    }
    
    private void parseRules( String comment )
    {
        String[]    rules   = 
            comment.substring( 1 ).strip().split( "/" );
        if ( rules.length < 2 || rules.length > 3 )
        {
            String  message = "Invalid comment in input stream:" + comment;
            throw new GOLException( message );
        }
        try
        {
            String  rule    = rules[0];
            int     len     = rule.length();
            for ( int inx = 0 ; inx < len ; ++inx )
                survivalRules.add( Integer.parseInt( rule ) );
            
            rule = rules[1];
            len = rule.length();
            for ( int inx = 0 ; inx < len ; ++inx )
            {
                String  count   = "" + rule.charAt( inx );
                birthRules.add( Integer.parseInt( count ) );
            }
            
            if ( rules.length > 2 )
            {
                gameStates = Integer.parseInt( rules[2] );
            }
        }
        catch ( NumberFormatException exc )
        {
            String  message = "Invalid ulc coordinates: " + comment;
            throw new GOLException( message );
        }
    }
    
    private void parseGrid( String line )
    {
        
    }
    
    /**
     * Reads and returns the next line from a given BufferedReader.
     * Lines are stripped;
     * Blank lines are skipped; 
     * lines consisting of only "#" (after stripping) are skipped.
     * Null is returned if the reader is exhausted.
     *  
     * @param reader    the given BufferedReader
     * 
     * @return  the next non-blank line from the reader,
     *          or null if the reader is exhausted
     *          
     * @throws IOException  if an IO error occurs
     * 
     * @see #nextLineRaw(BufferedReader)
     */
    private String nextLine( BufferedReader reader) throws IOException
    {
        String  line    = nextLineRaw( reader );
        while ( line != null && line.isEmpty() )
            line = nextLineRaw( reader );
        return line;
    }
    
    /**
     * This is a helper method for <em>nextLine.</em>
     * Reads and returns the next line from a given BufferedReader.
     * Lines are stripped;
     * lines consisting of only "#" (after stripping)
     * are converted to empty strings.
     * Null is returned if the reader is exhausted.
     *  
     * @param reader    the given BufferedReader
     * 
     * @return  the next non-blank line from the reader,
     *          or null if the reader is exhausted
     *          
     * @throws IOException  if an IO error occurs
     * 
     * @see #nextLine(BufferedReader)
     */
    private String nextLineRaw( BufferedReader reader ) throws IOException
    {
        String  line    = reader.readLine();
        if ( line != null )
        {
            line = line.strip();
            if ( line.equals( "#" ) )
                line = "";
        }
        return line;
    }
}
