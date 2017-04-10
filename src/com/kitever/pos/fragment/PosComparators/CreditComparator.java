package com.kitever.pos.fragment.PosComparators;

import com.kitever.pos.model.data.CreditDetails;

import java.util.Comparator;

/**
 * Created by android on 24/2/17.
 */

public class CreditComparator implements Comparator<CreditDetails> {
    private int field;
    private boolean asc;

    public CreditComparator(int field, boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    @Override
    public int compare(CreditDetails s1, CreditDetails s2) {

        String name1="";
        String name2="";

        if(field==1) {
            name1 = s1.getOrderDate();
            name2 = s2.getOrderDate();
        }
        else if(field==2) {
            name1 = s1.getContact_Name();
            name2 = s2.getContact_Name();
        }
        else if(field==3) {
            name1 = s1.getOrderCode();
            name2 = s2.getOrderCode();
        }

        else if(field==4) {
            Double   bill11 =  Double.parseDouble(s1.getBillAmount());
            Double   bill12 =  Double.parseDouble(s2.getBillAmount());
            int ret_bill=0;
            if(asc) ret_bill=(int)(bill11 -bill12);
            else   ret_bill=(int)(bill12 -bill11);
            return ret_bill;
        }
        else if(field==5) {
            Double   paid1 = Double.parseDouble(s1.getBalanceAmount());
            Double   paid2 = Double.parseDouble(s2.getBalanceAmount());
            int ret_bill=0;
            if(asc) ret_bill=(int)(paid1 -paid2);
            else   ret_bill=(int)(paid2 -paid1);
            return ret_bill;
        }
        else
        {

        }

        if(asc) return name1.compareToIgnoreCase(name2);
        else return name2.compareToIgnoreCase(name1);
    }
}
