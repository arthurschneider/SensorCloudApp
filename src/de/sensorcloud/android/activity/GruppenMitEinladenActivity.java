package de.sensorcloud.android.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.Gruppen;
import de.sensorcloud.android.entitaet.GruppenList;
import de.sensorcloud.android.entitaet.GruppenMitglied;
import de.sensorcloud.android.entitaet.NeueGruppeMitNutzer;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class GruppenMitEinladenActivity extends Activity {
	
	Spinner spinnerGruppen;
	EditText grpMitgEmail;
	EditText grpNameTxt;
	
	int gruppePosition;
	String nutStaID;
	GruppenList grpList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gruppen_mit_einladen_activity);
		grpMitgEmail = (EditText) findViewById(R.id.grp_mitg_email_adr);
		grpNameTxt = (EditText) findViewById(R.id.grp_erstellen_name);
		spinnerGruppen = (Spinner) findViewById(R.id.spinnerGruppenMitEinladen);
		
		getDatensatzGruppen();
	}
	
	public void getDatensatzGruppen(){

		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(GruppenMitEinladenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Gruppen/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        grpList = gson.fromJson(response, GruppenList.class);
		        setDataToSpinner();
		    }	    
		});
	}
	
	public void setDataToSpinner() {
		
		List<String> list = new ArrayList<String>();
		list.clear();
		spinnerGruppen.setAdapter(null);
		spinnerGruppen.invalidate();
		
		for (Gruppen grp : grpList.getGrpList()) {
        	list.add(grp.getGruBez());
		}
		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(GruppenMitEinladenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.notifyDataSetChanged();
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGruppen.setAdapter(dAdapter);
		spinnerGruppen.setOnItemSelectedListener(new AuswahlGruppeListener());
	}

	public class AuswahlGruppeListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			gruppePosition = position;
		}
	
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	public void einladenMitglied(View view) {
		String gruID = grpList.getGrpList().get(gruppePosition).getGruID();
		String email = grpMitgEmail.getText().toString();
		
		GruppenMitglied mitg = new GruppenMitglied();
		mitg.setEmail(email);
		mitg.setId(gruID);
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(mitg);
		
		StringEntity se = null;
		
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.put(null,  Helper.BASE_URL+"/SensorCloudRest/crud/Gruppen/inviteMitglied", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(GruppenMitEinladenActivity.this, response, Toast.LENGTH_LONG).show();
		 }
		});
	}
	
	
	public void erstelleGruppe(View view){
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(GruppenMitEinladenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		String gruBez = grpNameTxt.getText().toString();
		NeueGruppeMitNutzer gruppeNutzer = new NeueGruppeMitNutzer();
		gruppeNutzer.setGruBez(gruBez);
		gruppeNutzer.setNutStaID(nutStaID);
		insertGruppe(gruppeNutzer);
	}
	
	public void insertGruppe(NeueGruppeMitNutzer gruppeNutzer){
		
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(gruppeNutzer);
		
		StringEntity se = null;
		
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.put(null,  Helper.BASE_URL+"/SensorCloudRest/crud/Gruppen/createGruppe", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(GruppenMitEinladenActivity.this, response, Toast.LENGTH_LONG).show();
		        getDatensatzGruppen();
		 }
		});
	}
}
