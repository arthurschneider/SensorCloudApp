package de.sensorcloud.android.entitaet;

import java.util.ArrayList;

public class SensorProduktSemantik {

	private int n;
	private ArrayList<ParVor> parvor = new ArrayList<ParVor>();

	
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public ArrayList<ParVor> getParvor() {
		return parvor;
	}

	public void setParvor(ArrayList<ParVor> parvor) {
		this.parvor = parvor;
	}

}
