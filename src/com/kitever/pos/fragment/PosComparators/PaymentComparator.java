package com.kitever.pos.fragment.PosComparators;

import com.kitever.pos.model.data.CreditBalnce;

import java.util.Comparator;

/**
 * Created by android on 24/2/17.
 */

public class PaymentComparator implements Comparator<CreditBalnce> {
    private int field;
    private boolean asc;

    public PaymentComparator(int field, boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    @Override
    public int compare(CreditBalnce  first, CreditBalnce  sec) {
        if(field == 1 && asc)// for asc
        {
            return first.getPaymentDate().compareTo(sec.getPaymentDate());
        }
        else if(field == 1 && !asc)// for desc
        {
            return sec.getPaymentDate().compareTo(first.getPaymentDate());
        }
        else if (field==2 && asc)
        {
            return first.getInvoiceID().compareTo(sec.getInvoiceID());
        }
        else if( field==2 && !asc)// for desc
        {
            return sec.getInvoiceID().compareTo(first.getInvoiceID());
        }
        else if( field== 3 && asc)
        {
            return first.getReceiptNo().compareTo(sec.getReceiptNo());
        }
        else if( field== 3 && !asc)
        {
            return sec.getReceiptNo().compareTo(first.getReceiptNo());
        }
        else if( field== 4 && asc)
        {
            return first.getContact_Name().trim().compareToIgnoreCase(sec.getContact_Name().trim());
        }
        else if( field== 4 && !asc)
        {
            return sec.getContact_Name().trim().compareToIgnoreCase(first.getContact_Name().trim());
        }
        else if( field== 5 && asc)
        {
            return SortMethod.sortNumber(Double.parseDouble(first.getPaidAmount()),Double.parseDouble(sec.getPaidAmount()));
        }
        else if( field== 5 && !asc)
        {
            return SortMethod.sortNumber(Double.parseDouble(sec.getPaidAmount()),Double.parseDouble(first.getPaidAmount()));
        }
        return 0;
    }
}

