package com.kitever.pos.fragment.PosComparators;

/**
 * Created by android on 24/2/17.
 */

public class SortMethod {
    public static int sortNumber(Double a, int b)
    {
        if(a==b)
            return 0;
        else if(a>b)
            return 1;
        else
            return -1;
    }

    public static int sortNumber(Double a, Double b)
    {
        if(a==b)
            return 0;
        else if(a>b)
            return 1;
        else
            return -1;
    }
}
