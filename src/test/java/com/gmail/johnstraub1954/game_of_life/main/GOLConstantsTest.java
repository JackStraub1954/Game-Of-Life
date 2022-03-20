package com.gmail.johnstraub1954.game_of_life.main;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * At the moment, this is all about verifying that property names
 * are unique.
 * 
 * @author Jack Straub
 *
 */
class GOLConstantsTest
{
    private List<String>    fieldValues     = new ArrayList<>();
    
    @Test
    void test()
    {
        Class<GOLConstants> clazz           = GOLConstants.class;
        Field[]             publicFields    = clazz.getFields();
        for ( Field field : publicFields )
        {
            String  fieldName   = field.getName();
            if ( fieldName.endsWith( "_PN" ) || fieldName.endsWith( "_CN" ) )
                testPropertyName( field );
        }
    }
    
    private void testPropertyName( Field field )
    {
        try
        {
            assertTrue( (field.getModifiers() & Modifier.STATIC) != 0 );
            
            Object      fieldValue  = field.get( null );
            assertTrue( fieldValue instanceof String );
            
            String      strValue    = (String)fieldValue;
            System.out.println( strValue );
            String      name        = field.getName();
            String      errMessage  = name + "=" + strValue;
            if ( fieldValues.contains( strValue ) )
            {
                System.out.println( strValue );
            }
            assertFalse( fieldValues.contains( strValue ),  errMessage );
            fieldValues.add( strValue );
        }
        catch ( IllegalAccessException exc )
        {
            String  message = "Unexpected IllegalAccessException";
            fail( message, exc );
        }
    }
}
