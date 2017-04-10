package com.kitever.models;

import android.graphics.Bitmap;

public class FtpListModel {
	Bitmap image;
	String name;
	public FtpListModel(Bitmap image,String name) {
		// TODO Auto-generated constructor stub
		this.image=image;
		this.name=name;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
