package com.kitever.pos.fragment.PosComparators;

import com.kitever.pos.model.CustomerList;

import java.util.Comparator;

/**
 * Created by android on 24/2/17.
 */

public class CustomerComparator implements Comparator<CustomerList> {
    private int field;
    private boolean asc;

    public CustomerComparator(int field, boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    @Override
    public int compare(CustomerList first, CustomerList sec) {
         if (field==1 && asc)
        {
            return first.getContact_Name().compareToIgnoreCase(sec.getContact_Name());
        }
        else if( field==1 && !asc)// for desc
        {
            return sec.getContact_Name().compareToIgnoreCase(first.getContact_Name());
        }
        else if( field== 2 && asc)
        {
            return first.getTotalOrder().compareToIgnoreCase(sec.getTotalOrder());
        }
        else if( field== 2 && !asc)
        {
            return sec.getTotalOrder().compareToIgnoreCase(first.getTotalOrder());
        }
        else if( field== 3 && asc)
        {
            return SortMethod.sortNumber(Double.parseDouble(first.getTotalAmount()),Double.parseDouble(sec.getTotalAmount()));
        }
        else if( field== 3 && !asc)
        {
            return SortMethod.sortNumber(Double.parseDouble(sec.getTotalAmount()),Double.parseDouble(first.getTotalAmount()));
        }
        else if( field== 4 && asc)
        {
            return SortMethod.sortNumber(Double.parseDouble(first.getTotalCredit()),Double.parseDouble(sec.getTotalCredit()));
        }
        else if( field== 4 && !asc)
        {
            return SortMethod.sortNumber(Double.parseDouble(sec.getTotalCredit()),Double.parseDouble(first.getTotalCredit()));
        }else if(field == 5 && asc)// for asc
        {
            return first.getLastOrderDate().compareTo(sec.getLastOrderDate());
        }else if(field == 5 && !asc)// for desc
        {
            return sec.getLastOrderDate().compareTo(first.getLastOrderDate());
        }
            return 0;
    }
}
