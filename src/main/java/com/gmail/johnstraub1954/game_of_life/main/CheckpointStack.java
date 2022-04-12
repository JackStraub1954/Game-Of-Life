package com.gmail.johnstraub1954.game_of_life.main;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * This class encapsulates a stack for managing checkpoints.
 * A checkpoint is considered to be the state of the active grid map.
 * Implemented as a singleton.
 * All operations are thread-safe.
 * 
 * @author Jack Straub
 */
public enum CheckpointStack
{
    INSTANCE;

    /** The checkpoint stack */
    private Stack<GridMap>  stack   = new Stack<>();
    
    /**
     * Traditional push operation. 
     * Adds a grid map to the stack, and returns the value pushed.
     * This operation is thread-safe.
     * 
     * @param gridMap   the grid map to push
     * 
     * @return the given grid map
     */
    public GridMap push( GridMap gridMap )
    {
        stack.push( gridMap );
        return gridMap;
    }
    
    /**
     * Traditional pop operation.
     * Removes the most recently pushed grid map from the stack
     * and returns it.
     * This operation is thread-safe.
     * 
     * @return  the most recently pushed grid map
     * 
     * @throws EmptyStackException  if the stack is empty
     */
    public GridMap pop() throws EmptyStackException
    {
        GridMap gridMap = stack.pop();
        return gridMap;
    }
    
    /**
     * Traditional peek operation.
     * Returns the most recently pushed grid map 
     * without removing it from the stack.
     * This operation is thread-safe.
     * 
     * @return  the most recently pushed grid map
     * 
     * @throws EmptyStackException  if the stack is empty
     */
    public GridMap peek() throws EmptyStackException
    {
        GridMap gridMap = stack.peek();
        return gridMap;
    }
    
    /**
     * Clears the checkpoint stack.
     * This operation is thread-safe.
     */
    public void clear()
    {
        stack.clear();
    }
    
    /**
     * Indicates whether or not the checkpoint stack
     * is currently empty.
     * This operation is thread-safe.
     * 
     * @return true if the checkpoint stack is currently empty
     * 
     * @see #empty()
     */
    public boolean isEmpty()
    {
        boolean result = stack.isEmpty();
        return result;
    }
    
    /**
     * Equivalent to the <em>isEmpty</em> method.
     * Included for consistency with the java.util.Stack API.
     * This operation is thread-safe.
     * 
     * @return true if the checkpoint stack is currently empty
     * 
     * @see #isEmpty()
     */
    public boolean empty()
    {
        return isEmpty();
    }
}
