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
import de.sensorcloud.android.entitaet.NutzerEmail;
import de.sensorcloud.android.entitaet.NutzerEmailList;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class EmailActivity extends Activity implements OnItemSelectedListener {

	Spinner spinnerEmaStmmdtn;
	EditText emaAdrTxt;
	EditText emaBezTxt;

	NutzerEmail mailObj;
	NutzerEmailList emailListe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_activity);
		
		emaAdrTxt = (EditText) findViewById(R.id.ema_adr);
		emaBezTxt = (EditText) findViewById(R.id.ema_bez);
		spinnerEmaStmmdtn = (Spinner) findViewById(R.id.spinnerEmaStmmdtn);
		
		getDatensatz();
	}	
	
	public void getDatensatz(){
		mailObj = null;
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EmailActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/NutzerEmail/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		    	Gson gson = new Gson();
				emailListe = gson.fromJson(response, NutzerEmailList.class);
				setDataToSpinner();
		    }
		    
		});
	}
	
	public void setDataToSpinner() {
		List<String> list = new ArrayList<String>();
		list.clear();
		
		for (NutzerEmail mail : emailListe.getList()) {
        	list.add(mail.getNutEmaAdr());
		}
		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(EmailActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.notifyDataSetChanged();
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEmaStmmdtn.setAdapter(dAdapter);
		spinnerEmaStmmdtn.setOnItemSelectedListener(EmailActivity.this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		mailObj  = emailListe.getList().get(position);
	 
		emaBezTxt.setText(mailObj.getNutEmaBez());
		emaAdrTxt.setText(mailObj.getNutEmaAdr());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	
	public void updateEmail(View view){
		mailObj.setNutEmaBez(emaBezTxt.getText().toString());
		mailObj.setNutEmaAdr(emaAdrTxt.getText().toString());
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(mailObj);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null, Helper.BASE_URL+"/SensorCloudRest/crud/NutzerEmail", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Log.i("Test", response);
			        Toast.makeText(EmailActivity.this, response, Toast.LENGTH_LONG).show();
			        getDatensatz();
		 }
		});
	}
	
	public void insertEmail(View view){
		mailObj.setNutEmaBez(emaBezTxt.getText().toString());
		mailObj.setNutEmaAdr(emaAdrTxt.getText().toString());
		Gson gson = new Gson();
		// Java Objekt wird in einen JSON formatierten String umgewandelt.
		JsonElement jsonElement = gson.toJsonTree(mailObj);
		
		StringEntity se = null;
		
		try {
			// Erstellung des Parametrs aus dem JSON String
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		//Einfügen des parametrs in das Body der HTTP Anfrage
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.put(null,  Helper.BASE_URL+"/SensorCloudRest/crud/NutzerEmail", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Toast.makeText(EmailActivity.this, response, Toast.LENGTH_LONG).show();
					getDatensatz();
		 }
		});
	}
	
	
	public void deleteEmail(View view){
		if (emailListe.getList().size() > 1) {
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(mailObj.getNutEmaID());
			
			StringEntity se = null;
			
			try {
			    se = new StringEntity(jsonElement.toString());
			} catch (UnsupportedEncodingException e) {
				Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
			}		
			
			AsyncHttpClient client = new AsyncHttpClient();
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			client.post(null,  Helper.BASE_URL+"/SensorCloudRest/crud/NutzerEmail/delete", se, "application/json", new AsyncHttpResponseHandler() {
				 @Override
				    public void onSuccess(String response) {
				        Toast.makeText(EmailActivity.this, response, Toast.LENGTH_LONG).show();
				        getDatensatz();
			 }
			});
		} else {
			Toast.makeText(EmailActivity.this, "Das letzte Element darf nicht geloescht werden", Toast.LENGTH_LONG).show();
		}
		
	}

}
