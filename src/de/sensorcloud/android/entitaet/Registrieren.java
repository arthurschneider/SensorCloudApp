package de.sensorcloud.android.entitaet;

public class Registrieren {

	private NutzerStammdaten stammdaten;
	private NutzerTelefon telefon;
	private NutzerEmail email;
	private NutzerSicherheit sicherheit;
	private Adresse adresse;

	
	public NutzerStammdaten getStammdaten() {
		return stammdaten;
	}

	public void setStammdaten(NutzerStammdaten stammdaten) {
		this.stammdaten = stammdaten;
	}

	public NutzerTelefon getTelefon() {
		return telefon;
	}

	public void setTelefon(NutzerTelefon telefon) {
		this.telefon = telefon;
	}

	public NutzerEmail getEmail() {
		return email;
	}

	public void setEmail(NutzerEmail email) {
		this.email = email;
	}

	public NutzerSicherheit getSicherheit() {
		return sicherheit;
	}

	public void setSicherheit(NutzerSicherheit sicherheit) {
		this.sicherheit = sicherheit;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

}
