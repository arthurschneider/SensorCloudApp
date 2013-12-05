package de.sensorcloud.android.entitaet;

import java.util.ArrayList;

public class DatasetMitSemantik {

	private ArrayList<MesswertTime> messwertTime;
	private ParVor parVor;

	
	public ArrayList<MesswertTime> getMesswertTime() {
		return messwertTime;
	}

	public void setMesswertTime(ArrayList<MesswertTime> messwertTime) {
		this.messwertTime = messwertTime;
	}

	public ParVor getParVor() {
		return parVor;
	}

	public void setParVor(ParVor parVor) {
		this.parVor = parVor;
	}

}
