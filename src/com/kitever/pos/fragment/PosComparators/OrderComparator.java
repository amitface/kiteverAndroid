package com.kitever.pos.fragment.PosComparators;

import com.kitever.pos.model.data.OrderDetailModelData;

import java.util.Comparator;

/**
 * Created by android on 24/2/17.
 */


public class OrderComparator implements Comparator<OrderDetailModelData> {
    private int field;
    private boolean asc;

    public OrderComparator(int field, boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    @Override
    public int compare(OrderDetailModelData s1, OrderDetailModelData s2) {
        String name1="";
        String name2="";

        if(field==1) {
             name1 = s1.getOrderDate();
             name2 = s2.getOrderDate();
        }
        else if(field==2) {
            name1 = s1.getOrderCode();
            name2 = s2.getOrderCode();
        }
        else if(field==3) {
            name1 = s1.getContactName();
            name2 = s2.getContactName();
        }
        else if(field==4) {
          Double   bill11 = s1.getBillAmount();
          Double   bill12 = s2.getBillAmount();
           int ret_bill=0;
           if(asc) ret_bill=(int)(bill11 -bill12);
           else   ret_bill=(int)(bill12 -bill11);
           return ret_bill;
        }
        else if(field==5) {
            Double   paid1 = Double.parseDouble(s1.getPaidAmount());
            Double   paid2 = Double.parseDouble(s2.getPaidAmount());
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
