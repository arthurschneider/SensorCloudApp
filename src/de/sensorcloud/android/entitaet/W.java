package de.sensorcloud.android.entitaet;


import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class W {

	@SerializedName("enum")
	private ArrayList<String> wenum = new ArrayList<String>();
	private String min;
	private String max;

	
	public ArrayList<String> getWenum() {
		return wenum;
	}

	public void setWenum(ArrayList<String> wenum) {
		this.wenum = wenum;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

}
