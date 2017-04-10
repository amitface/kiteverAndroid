package com.kitever.pos.model.data;

import java.util.List;
import com.kitever.pos.model.CustomerList;

public class CustomerObject {
	private List<CustomerList> CustomerList;

    public List<CustomerList> getCustomerList ()
    {
        return CustomerList;
    }

    public void setCustomerList (List<CustomerList> CustomerList)
    {
        this.CustomerList = CustomerList;
    }

    @Override
    public String toString()
    {
//        return "ClassPojo [CustomerList = "+CustomerList.(CustomerList[]) collection.toArray(new CustomerList[collection.size()])+"]";
        return null;
    }
}
