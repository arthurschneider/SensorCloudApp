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
import de.sensorcloud.android.entitaet.SensorService;
import de.sensorcloud.android.entitaet.SensorServiceList;
import de.sensorcloud.android.entitaet.ServiceLinien;
import de.sensorcloud.android.entitaet.ServiceLinienList;
import de.sensorcloud.android.entitaet.SensorServiceMitServiceLinie;
import de.sensorcloud.android.helpertools.Helper;

public class ServiceLinieSenAnlegenActivity extends Activity {
	
	Spinner spinnerSensoren;
	Spinner spinnerServices;
	Spinner spinnerServiceLinien;
	EditText servLinieName;
	
	String nutStaID;
	
	SensorList senList;
	SensorServiceList senServList;
	
	Sensor sensor;
	SensorService sensorService;
	
	ServiceLinienList servLinList;
	ServiceLinien servLin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_linie_sen_anlegen_activity);
		
		servLinieName = (EditText) findViewById(R.id.neue_servlinie);

		spinnerSensoren = (Spinner) findViewById(R.id.spinnerSLSensor);
		spinnerServices = (Spinner) findViewById(R.id.spinnerSLServices);
		spinnerServiceLinien = (Spinner) findViewById(R.id.spinnerSLServLinien);
		getDatensatzSensoren();
		
	}
	
	public void getDatensatzSensoren(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ServiceLinieSenAnlegenActivity.this); 
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
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinieSenAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSensoren.postInvalidate();
		spinnerSensoren.setAdapter(dAdapter);
		
		spinnerSensoren.setOnItemSelectedListener(new AuswahlSensorListener());
	}
	
	public class AuswahlSensorListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			sensor = senList.getSensorList().get(position);
			getDatensatzServices();
			getDatensatzServiceLinien();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void getDatensatzServices(){
		AsyncHttpClient client = new AsyncHttpClient();
		String senID = sensor.getSenID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorService/SenID/"+senID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        senServList = gson.fromJson(response, SensorServiceList.class);
		        setDataToSpinnerServices();
		    }
		});
	}
	
	public void setDataToSpinnerServices() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (SensorService serv : senServList.getList()) {
        	list.add(serv.getSenSerBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinieSenAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerServices.postInvalidate();
		spinnerServices.setAdapter(dAdapter);
		
		spinnerServices.setOnItemSelectedListener(new AuswahlServiceListener());
	}
	
	public class AuswahlServiceListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			sensorService = senServList.getList().get(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	
	public void getDatensatzServiceLinien(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ServiceLinieSenAnlegenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/ServiceLinien/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        servLinList = gson.fromJson(response, ServiceLinienList.class);
		        setDataToSpinnerServiceLinien();
		    }
		});
	}
	
	public void setDataToSpinnerServiceLinien() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (ServiceLinien servLin : servLinList.getList()) {
        	list.add(servLin.getSerLinBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinieSenAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerServiceLinien.postInvalidate();
		spinnerServiceLinien.setAdapter(dAdapter);
		
		spinnerServiceLinien.setOnItemSelectedListener(new AuswahlServiceLinienListener());
	}
	
	public class AuswahlServiceLinienListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			servLin = servLinList.getList().get(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void updateServiceLinie(View view) {
		SensorServiceMitServiceLinie servLinServ = new SensorServiceMitServiceLinie();
		servLinServ.setService(sensorService);
		servLinServ.setServiceLinie(servLin);
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(servLinServ);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null, Helper.BASE_URL+"/SensorCloudRest/crud/ServiceLinien", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(ServiceLinieSenAnlegenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
		});
	}
	
	public void insertServiceInLinie(View view) {
		String name = servLinieName.getText().toString();
		SensorServiceMitServiceLinie servLinServ = new SensorServiceMitServiceLinie();
		servLinServ.setService(sensorService);
		
		ServiceLinien linie = new ServiceLinien();
		linie.setSerLinBez(name);
		linie.setSerLinNutStaID(nutStaID);
		linie.setSerLinID(Helper.generateUUID());
		servLinServ.setServiceLinie(linie);
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(servLinServ);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.put(null, Helper.BASE_URL+"/SensorCloudRest/crud/ServiceLinien", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(ServiceLinieSenAnlegenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
		});
	}
}
