package app;

import java.util.ArrayList;
import java.util.List;

public class AdHocExerciser
{
    public static void main(String[] args)
    {
        List<Integer>   list1   = List.of( 5, 4, 3, 0, 1, 2 );
        List<Integer>   list2   = List.of( 5, 4, 5, 3, 4, 2 );
        List<Integer>   list3   = List.of( 3 );
        List<Integer>   list4   = new ArrayList<>();
        System.out.println( "#" + cvtListToCSV2( list1) );
        System.out.println( "#" + cvtListToCSV( list2) );
        System.out.println( "#" + cvtListToCSV( list3) );
        System.out.println( "#" + cvtListToCSV( list4) );
    }
    
    private static String cvtListToCSV( List<?> list )
    {
        StringBuilder   bldr    = 
            list.stream()
            .distinct()
            .sorted()
            .map( i -> i + "," )
            .collect( 
                StringBuilder::new, 
                StringBuilder::append, 
                StringBuilder::append
            );
        int len = bldr.length();
        if ( len > 0 )
            bldr.deleteCharAt( len - 1 );
        return bldr.toString();
    }
    
    private static String cvtListToCSV2( List<?> list )
    {
        StringBuilder result = 
            list.stream()
                .collect(
                    StringBuilder::new, 
                    (x, y) -> x.append(y),
                    (a, b) -> a.append(",").append(b)
                );
        return result.toString();
    }
}
