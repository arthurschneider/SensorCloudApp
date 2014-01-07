package de.sensorcloud.android.activity;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.Adresse;
import de.sensorcloud.android.entitaet.NutzerEmail;
import de.sensorcloud.android.entitaet.NutzerSicherheit;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.NutzerTelefon;
import de.sensorcloud.android.entitaet.Registrieren;
import de.sensorcloud.android.helpertools.Helper;

public class RegistrierenActivity extends Activity {
	
	EditText anrede;
	EditText name;
	EditText vorname;
	EditText firma;
	
	EditText telNr;
	EditText telBez;
	
	EditText emailAdr;
	EditText emailBez;
	
	EditText passwort;
	EditText pubKey;
	EditText priKey;
	
	EditText bez;
	EditText str;
	EditText plz;
	EditText ort;
	EditText land;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registrieren_activity);
		
		anrede = (EditText) findViewById(R.id.nutzer_anlegen_anrede);
		name = (EditText) findViewById(R.id.nutzer_anlegen_name);
		vorname = (EditText) findViewById(R.id.nutzer_anlegen_vorname);
		firma = (EditText) findViewById(R.id.nutzer_anlegen_firma);
		
		telNr = (EditText) findViewById(R.id.nutzer_anlegen_telnr);
		telBez = (EditText) findViewById(R.id.nutzer_anlegen_telbez);
		
		emailAdr = (EditText) findViewById(R.id.nutzer_anlegen_emaadr);
		emailBez = (EditText) findViewById(R.id.nutzer_anlegen_emabez);
		
		passwort = (EditText) findViewById(R.id.nutzer_anlegen_pass);
		pubKey = (EditText) findViewById(R.id.nutzer_anlegen_pubKey);
		priKey = (EditText) findViewById(R.id.nutzer_anlegen_priKey);
		
		bez = (EditText) findViewById(R.id.nutzer_anlegen_adrBez);
		str = (EditText) findViewById(R.id.nutzer_anlegen_strasse);
		plz = (EditText) findViewById(R.id.nutzer_anlegen_plz);
		ort = (EditText) findViewById(R.id.nutzer_anlegen_ort);
		land  = (EditText) findViewById(R.id.nutzer_anlegen_land);
	}

	
	public void insertNutzer(View view) {
		Registrieren regist = new Registrieren();
		String adrID = null;
		String nutStaID = null;
		boolean stammdatenVoll = false;
		boolean telefonVoll = false;
		boolean emailVoll = false;
		boolean sicherVoll = false;
		boolean adresseVoll = false;
		
		/*--------------- Stammdaten anlegen ----------------------*/
		
		if (!anrede.getText().toString().isEmpty() && !firma.getText().toString().isEmpty()
			&& !name.getText().toString().isEmpty() && !vorname.getText().toString().isEmpty()) {
			
			NutzerStammdaten stammdaten = new NutzerStammdaten();
			
			adrID = Helper.generateUUID();
			stammdaten.setNutStaAdrID(adrID);
			
			stammdaten.setNutStaAnr(anrede.getText().toString());
			
			Date datum = new Date();
			long timestamp = datum.getTime();
			stammdaten.setNutStaDatEin(timestamp);
			
			stammdaten.setNutStaFir(firma.getText().toString());
			
			nutStaID = Helper.generateUUID();
			stammdaten.setNutStaID(nutStaID);
			
			stammdaten.setNutStaNam(name.getText().toString());
			stammdaten.setNutStaVor(vorname.getText().toString());
			regist.setStammdaten(stammdaten);
			stammdatenVoll = true;
		} else {
			Toast.makeText(RegistrierenActivity.this, "NutzerStammdaten sind unvollstaendig!", Toast.LENGTH_LONG).show();
		}
		
		
		/*--------------- Email anlegen ----------------------*/
		if (!emailBez.getText().toString().isEmpty() && !emailAdr.getText().toString().isEmpty()) {
			NutzerEmail mailObj = new NutzerEmail();
			mailObj.setNutEmaNutStaID(nutStaID);
			mailObj.setNutEmaID(Helper.generateUUID());
			mailObj.setNutEmaBez(emailBez.getText().toString());
			mailObj.setNutEmaAdr(emailAdr.getText().toString());
			regist.setEmail(mailObj);
			emailVoll = true;
		} else {
			Toast.makeText(RegistrierenActivity.this, "Emaildaten sind unvollstaendig!", Toast.LENGTH_LONG).show();
		}
		
		
		/*--------------- Telefon anlegen ----------------------*/
		if (!telBez.getText().toString().isEmpty() && !telNr.getText().toString().isEmpty()) {
			NutzerTelefon telObj = new NutzerTelefon();
			telObj.setNutTelNutStaID(nutStaID);
			telObj.setNutTelID(Helper.generateUUID());
			telObj.setNutTelBez(telBez.getText().toString());
			telObj.setNutTelNum(telNr.getText().toString());
			regist.setTelefon(telObj);
			telefonVoll = true;
		} else {
			Toast.makeText(RegistrierenActivity.this, "Telefondaten sind unvollstaendig!", Toast.LENGTH_LONG).show();
		}
		
		
		/*--------------- Sicherheit anlegen ----------------------*/
		if (!passwort.getText().toString().isEmpty() && !pubKey.getText().toString().isEmpty()
			&& !priKey.getText().toString().isEmpty()) {
			
			NutzerSicherheit sichObj = new NutzerSicherheit();
			sichObj.setNutSicID(Helper.generateUUID());
			sichObj.setNutSicNutStaID(nutStaID);
			sichObj.setNutSicPas(passwort.getText().toString());
			sichObj.setNutSicPubKey(pubKey.getText().toString());
			sichObj.setNutSicPriKey(priKey.getText().toString());
			regist.setSicherheit(sichObj);
			sicherVoll = true;
		} else {
			Toast.makeText(RegistrierenActivity.this, "Sicherheitsdaten sind unvollstaendig!", Toast.LENGTH_LONG).show();
		}
		
		
		/*--------------- Adresse anlegen ----------------------*/
		if (!bez.getText().toString().isEmpty() && !str.getText().toString().isEmpty()
			&& !plz.getText().toString().isEmpty() && !ort.getText().toString().isEmpty()
			&& !land.getText().toString().isEmpty()) {
			Adresse adresse = new Adresse();
			adresse.setAdrID(adrID);
			adresse.setAdrBez(bez.getText().toString());
			adresse.setAdrStr(str.getText().toString());
			adresse.setAdrPlz(plz.getText().toString());
			adresse.setAdrOrt(ort.getText().toString());
			adresse.setAdrLan(land.getText().toString());
			regist.setAdresse(adresse);
			adresseVoll = true;
		} else {
			Toast.makeText(RegistrierenActivity.this, "Adressdaten sind unvollstaendig!", Toast.LENGTH_LONG).show();
		}
		
		if (stammdatenVoll && telefonVoll && emailVoll && sicherVoll && adresseVoll ) {
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(regist);
			
			StringEntity se = null;
			
			try {
				se = new StringEntity(jsonElement.toString());
			} catch (UnsupportedEncodingException e) {
				Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
			}		
			
			AsyncHttpClient client = new AsyncHttpClient();
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			client.put(null,  Helper.BASE_URL+"/SensorCloudRest/crud/NutzerStammdaten", se, "application/json", new AsyncHttpResponseHandler() {
				 @Override
				 public void onSuccess(String response) {
				 Toast.makeText(RegistrierenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
			});
		} else {
			Toast.makeText(RegistrierenActivity.this, "Insert wurde nicht ausgefuehrt!", Toast.LENGTH_LONG).show();
		}
		
		
	}
}
