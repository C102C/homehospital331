package com.health.heathtools;

import android.graphics.drawable.Drawable;

public class APPinfo {
	private String APPname;
	private Drawable APPicon;
	private String APPpackagename;

	public APPinfo(String APPname, Drawable APPicon, String APPpackagename) {
		this.APPname = APPname;
		this.APPicon = APPicon;
		this.APPpackagename = APPpackagename;
	}

	public void setAPPname(String APPname) {
		this.APPname = APPname;
	}

	public String getAPPname() {
		return APPname;
	}

	public void setAPPicon(Drawable APPicon) {
		this.APPicon = APPicon;
	}

	public Drawable getAPPicon() {
		return APPicon;
	}

	public void setAPPpackagename(String APPpackagename) {
		this.APPpackagename = APPpackagename;
	}

	public String getAPPpackagename() {
		return APPpackagename;
	}

}