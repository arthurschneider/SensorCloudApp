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
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.NutzerTelefon;
import de.sensorcloud.android.entitaet.NutzerTelefonList;
import de.sensorcloud.android.helpertools.Helper;

public class TelefonActivity extends Activity implements OnItemSelectedListener {

	Spinner spinnerTelStmmdtn;
	EditText telNumTxt;
	EditText telBezTxt;
	
	
	NutzerTelefon telObj;
	NutzerTelefonList telefonListe;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telefon_activity);
		
		telNumTxt = (EditText) findViewById(R.id.tel_num);
		telBezTxt = (EditText) findViewById(R.id.tel_bez);
		spinnerTelStmmdtn = (Spinner) findViewById(R.id.spinnerTelStmmdtn);
		
		getDatensatz();
		setDataToSpinner();
	}	
	
	public void getDatensatz(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(TelefonActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get( Helper.BASE_URL+"/SensorCloudRest/crud/NutzerTelefon/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TelefonActivity.this);
		        SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("TelefonListe", response);
				editor.commit();
		    }
		    
		});
		
	}
	
	public void setDataToSpinner(){
		
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		list.clear();
		
		spinnerTelStmmdtn.setAdapter(null);
		spinnerTelStmmdtn.postInvalidate();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(TelefonActivity.this); 
		String json = mPrefs.getString("TelefonListe", null);
		telefonListe = gson.fromJson(json, NutzerTelefonList.class);
		
		for (NutzerTelefon tel : telefonListe.getList()) {
        	list.add(tel.getNutTelNum());
        }
	
//		list.add("müüüüüüggggggggg");
//		list.add("zuoooooooooooo");list.add("ffffffffffffff");
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(TelefonActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.notifyDataSetChanged();
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	
		
		spinnerTelStmmdtn.setAdapter(dAdapter);
		
		spinnerTelStmmdtn.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			
			Gson gson = new Gson();
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(TelefonActivity.this); 
			String json = mPrefs.getString("TelefonListe", null);
			telefonListe = gson.fromJson(json, NutzerTelefonList.class);
			telObj  = telefonListe.getList().get(position);
	
			
			telBezTxt.setText(telObj.getNutTelBez());
			telNumTxt.setText(telObj.getNutTelNum());
//			String data = spinnerTelStmmdtn.getItemAtPosition(position).toString();
//	        Toast.makeText(TelefonActivity.this, data, Toast.LENGTH_SHORT).show();
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public void updateTel(View view){
		
		telObj.setNutTelBez(telBezTxt.getText().toString());
		telObj.setNutTelNum(telNumTxt.getText().toString());
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(telObj);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null,  Helper.BASE_URL+"/SensorCloudRest/crud/NutzerTelefon", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Toast.makeText(TelefonActivity.this, response, Toast.LENGTH_LONG).show();
		 }
	    
		});
		
		getDatensatz();
		setDataToSpinner();
	}
	
	public void insertTel(View view){
		
		telObj.setNutTelBez(telBezTxt.getText().toString());
		telObj.setNutTelNum(telNumTxt.getText().toString());
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(telObj);
		
		StringEntity se = null;
		
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.put(null,  Helper.BASE_URL+"/SensorCloudRest/crud/NutzerTelefon", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Toast.makeText(TelefonActivity.this, response, Toast.LENGTH_LONG).show();
		 }
	    
		});
		
		getDatensatz();
		setDataToSpinner();
	}
	
	
	public void deleteTel(View view){
		
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(telObj.getNutTelID());
		
		StringEntity se = null;
		
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null,  Helper.BASE_URL+"/SensorCloudRest/crud/NutzerTelefon/delete", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Toast.makeText(TelefonActivity.this, response, Toast.LENGTH_LONG).show();
		 }
	    
		});
		getDatensatz();
		setDataToSpinner();
	}
}
