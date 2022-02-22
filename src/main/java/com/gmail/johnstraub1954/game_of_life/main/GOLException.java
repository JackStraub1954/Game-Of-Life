package com.gmail.johnstraub1954.game_of_life.main;

public class GOLException extends Error
{

    public GOLException()
    {
        // TODO Auto-generated constructor stub
    }

    public GOLException(String message)
    {
        super(message);
    }

    public GOLException(Throwable cause)
    {
        super(cause);
    }

    public GOLException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public GOLException(String message, Throwable cause,
        boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
