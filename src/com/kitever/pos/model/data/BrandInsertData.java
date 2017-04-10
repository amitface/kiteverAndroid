package com.kitever.pos.model.data;

public class BrandInsertData {
	 public BrandInsertData(String name) {
		super();
		Name = name;
	}

	private String Name;

	    public String getName ()
	    {
	        return Name;
	    }

	    public void setName (String Name)
	    {
	        this.Name = Name;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [Name = "+Name+"]";
	    }
}
