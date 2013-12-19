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
import de.sensorcloud.android.entitaet.Sensor;
import de.sensorcloud.android.entitaet.SensorList;
import de.sensorcloud.android.entitaet.SensorVerbund;
import de.sensorcloud.android.entitaet.SensorVerbundList;
import de.sensorcloud.android.entitaet.SensorVerbundMitSensor;
import de.sensorcloud.android.helpertools.Helper;

public class SensorVerbundAnlegenActivity extends Activity {

	Spinner spinnerSensoren;
	Spinner spinnerVerbund;
	EditText verbundName;
	
	String nutStaID;
	
	SensorVerbundList verbundList;
	SensorList senList;
	
	Sensor sensor;
	SensorVerbund sensorVerbund;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_verbund_anlegen_activity);
		
		verbundName = (EditText) findViewById(R.id.neuer_senverb);
		spinnerSensoren = (Spinner) findViewById(R.id.spinner_angelegte_sensoren);
		spinnerVerbund = (Spinner) findViewById(R.id.spinner_angelegte_senverb);
		getDatensatzSensor();
		getDatensatzVerbund();
	}

	public void getDatensatzSensor(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundAnlegenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Sensor/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        senList = gson.fromJson(response, SensorList.class);
		        setDataToSpinnerSensorBez();
		        
		    }
		    
		});
	}
	
	public void setDataToSpinnerSensorBez() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (Sensor sen : senList.getSensorList()) {
        	list.add(sen.getSenBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(SensorVerbundAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSensoren.postInvalidate();
		spinnerSensoren.setAdapter(dAdapter);
		
		spinnerSensoren.setOnItemSelectedListener(new AuswahlSensorListener());
	}
	
	public class AuswahlSensorListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			sensor = senList.getSensorList().get(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	
	public void getDatensatzVerbund(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundAnlegenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        verbundList = gson.fromJson(response, SensorVerbundList.class);
		        setDataToSpinnerSensorVerbBez();
		        
		    }
		    
		});
	}
	
	public void setDataToSpinnerSensorVerbBez() {
		List<String> list = new ArrayList<String>();
		list.clear();
		
		for (SensorVerbund verb : verbundList.getSenVerbundList()) {
        	list.add(verb.getSenVerBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(SensorVerbundAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerVerbund.postInvalidate();
		spinnerVerbund.setAdapter(dAdapter);
		
		spinnerVerbund.setOnItemSelectedListener(new AuswahlSensorVerbundListener());
	}
	
	public class AuswahlSensorVerbundListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			sensorVerbund = verbundList.getSenVerbundList().get(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void insertSensorVerbund(View view) {
		SensorVerbundMitSensor senVerbSen = new SensorVerbundMitSensor();
		senVerbSen.setSensor(sensor);
		senVerbSen.setVerb(sensorVerbund);
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(senVerbSen);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null, Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(SensorVerbundAnlegenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
		});
	}
	
	public void anlegenNeuVerb(View view) {
		String name = verbundName.getText().toString();
		
		Log.i("Debug"," Sensor = "+ sensor.getSenBez()+" Verbundname = "+name);
		SensorVerbundMitSensor senVerbSen = new SensorVerbundMitSensor();
		senVerbSen.setSensor(sensor);
		
		SensorVerbund verb = new SensorVerbund();
		verb.setSenVerBez(name);
		verb.setSenVerID(Helper.generateUUID());
		senVerbSen.setVerb(verb);
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(senVerbSen);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.put(null, Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(SensorVerbundAnlegenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
		});
	}

}
