package com.kitever.pos.fragment.PosComparators;

import com.kitever.pos.model.data.FetchInvoice;

import java.util.Comparator;

/**
 * Created by android on 24/2/17.
 */

public class InvoiceComparator implements Comparator<FetchInvoice>{
    private int field;
    private boolean asc;

    public InvoiceComparator(int field, boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    @Override
    public int compare(FetchInvoice s1, FetchInvoice s2) {
        String name1="";
        String name2="";

        if(field==1) {
            name1 = String.valueOf(s1.getOrderDate());
            name2 = String.valueOf(s2.getOrderDate());
        }
        else if(field==2) {
            name1 = s1.getContactName();
            name2 = s2.getContactName();
        }
        else if(field==3) {
            name1 = s1.getInvoiceCode();
            name2 = s2.getInvoiceCode();
        }

        else if(field==4) {
            Double   bill11 =  Double.parseDouble(s1.getBillAmount());
            Double   bill12 =  Double.parseDouble(s2.getBillAmount());
            int ret_bill=0;
            if(asc) ret_bill=(int)(bill11 -bill12);
            else   ret_bill=(int)(bill12 -bill11);
            return ret_bill;
        }

        else
        {

        }

        if(asc) return name1.compareToIgnoreCase(name2);
        else return name2.compareToIgnoreCase(name1);
    }
}
