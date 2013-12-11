package de.sensorcloud.android.entitaet;

import java.util.ArrayList;

public class Mitglied {

	private NutzerStammdaten nutzer;
	private ArrayList<NutzerEmail> mailList;
	

	public NutzerStammdaten getNutzer() {
		return nutzer;
	}

	public void setNutzer(NutzerStammdaten nutzer) {
		this.nutzer = nutzer;
	}

	public ArrayList<NutzerEmail> getMailList() {
		return mailList;
	}

	public void setMailList(ArrayList<NutzerEmail> mailList) {
		this.mailList = mailList;
	}

}
