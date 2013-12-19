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
import de.sensorcloud.android.entitaet.Aktor;
import de.sensorcloud.android.entitaet.AktorList;
import de.sensorcloud.android.entitaet.AktorVerbund;
import de.sensorcloud.android.entitaet.AktorVerbundList;
import de.sensorcloud.android.entitaet.AktorVerbundMitAktor;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class AktorVerbundAnlegenActivity extends Activity {
	
	Spinner spinnerAktoren;
	Spinner spinnerVerbund;
	EditText verbundName;
	
	String nutStaID;
	
	AktorVerbundList verbundList;
	AktorList aktList;
	
	Aktor aktor;
	AktorVerbund aktorVerbund;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aktor_verbund_anlegen_activity);
		
		verbundName = (EditText) findViewById(R.id.neuer_akrverb);
		spinnerAktoren = (Spinner) findViewById(R.id.spinner_angelegte_aktoren);
		spinnerVerbund = (Spinner) findViewById(R.id.spinner_angelegte_aktverb);
		getDatensatzAktor();
		getDatensatzVerbund();
	}

	public void getDatensatzAktor(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AktorVerbundAnlegenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Aktor/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        aktList = gson.fromJson(response, AktorList.class);
		        setDataToSpinnerAktorBez();
		        
		    }
		    
		});
	}
	
	public void setDataToSpinnerAktorBez() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (Aktor akt : aktList.getList()) {
        	list.add(akt.getAktBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(AktorVerbundAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerAktoren.postInvalidate();
		spinnerAktoren.setAdapter(dAdapter);
		
		spinnerAktoren.setOnItemSelectedListener(new AuswahlAktorListener());
	}
	
	public class AuswahlAktorListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			aktor = aktList.getList().get(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void getDatensatzVerbund(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AktorVerbundAnlegenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/AktorVerbund/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        verbundList = gson.fromJson(response, AktorVerbundList.class);
		        setDataToSpinnerAktorVerbBez();
		        
		    }
		    
		});
	}
	
	public void setDataToSpinnerAktorVerbBez() {
		List<String> list = new ArrayList<String>();
		list.clear();
		
		for (AktorVerbund verb : verbundList.getAktVerbundList()) {
        	list.add(verb.getAktVerBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(AktorVerbundAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerVerbund.postInvalidate();
		spinnerVerbund.setAdapter(dAdapter);
		
		spinnerVerbund.setOnItemSelectedListener(new AuswahlAktorVerbundListener());
	}
	
	public class AuswahlAktorVerbundListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			aktorVerbund = verbundList.getAktVerbundList().get(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void insertAktorVerbund(View view) {
		AktorVerbundMitAktor aktVerbAkt = new AktorVerbundMitAktor();
		aktVerbAkt.setAktor(aktor);
		aktVerbAkt.setVerb(aktorVerbund);
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(aktVerbAkt);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null, Helper.BASE_URL+"/SensorCloudRest/crud/AktorVerbund", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(AktorVerbundAnlegenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
		});
	}
	
	public void anlegenNeuVerb(View view) {
		String name = verbundName.getText().toString();
		
		Log.i("Debug"," Aktor = "+ aktor.getAktBez()+" Verbundname = "+name);
		AktorVerbundMitAktor aktVerbAkt = new AktorVerbundMitAktor();
		aktVerbAkt.setAktor(aktor);
		
		AktorVerbund verb = new AktorVerbund();
		verb.setAktVerBez(name);
		verb.setAktVerID(Helper.generateUUID());
		aktVerbAkt.setVerb(verb);
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(aktVerbAkt);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.put(null, Helper.BASE_URL+"/SensorCloudRest/crud/AktorVerbund", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(AktorVerbundAnlegenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
		});
	}
	
}
