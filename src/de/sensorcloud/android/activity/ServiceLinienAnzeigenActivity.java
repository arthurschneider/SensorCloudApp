package de.sensorcloud.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.Sensor;
import de.sensorcloud.android.entitaet.SensorList;
import de.sensorcloud.android.entitaet.SensorService;
import de.sensorcloud.android.entitaet.SensorServiceFunktion;
import de.sensorcloud.android.entitaet.SensorServiceFunktionList;
import de.sensorcloud.android.entitaet.SensorServiceList;
import de.sensorcloud.android.entitaet.ServiceLinien;
import de.sensorcloud.android.entitaet.ServiceLinienList;
import de.sensorcloud.android.helpertools.Helper;

public class ServiceLinienAnzeigenActivity extends Activity {

	Spinner spinnerSensoren;
	Spinner spinnerServices;
	Spinner spinnerFunktionen;
	Spinner spinnerServiceLinien;
	
	String nutStaID;
	
	SensorList senList;
	SensorServiceList senServList;
	
	SensorServiceFunktionList funktionList;
	
	
	Sensor sensor;
	SensorService sensorService;
	
	ServiceLinienList servLinList;
	ServiceLinien servLin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_linien_anzeigen_activity);
		
		spinnerSensoren = (Spinner) findViewById(R.id.spinnerAnzSensoren);
		spinnerServices = (Spinner) findViewById(R.id.spinnerAnzService);
		spinnerFunktionen = (Spinner) findViewById(R.id.spinnerAnzFunktion);
		spinnerServiceLinien = (Spinner) findViewById(R.id.spinnerAnzServLin);
		getDatensatzServiceLinien();
	}

	public void getDatensatzServiceLinien(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ServiceLinienAnzeigenActivity.this); 
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
		for (ServiceLinien linie : servLinList.getList()) {
        	list.add(linie.getSerLinBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerServiceLinien.postInvalidate();
		spinnerServiceLinien.setAdapter(dAdapter);
		
		spinnerServiceLinien.setOnItemSelectedListener(new AuswahlSensorListener());
	}
	
	public class AuswahlSensorListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			servLin = servLinList.getList().get(position);
			getDatensatzServices();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void getDatensatzServices(){
		AsyncHttpClient client = new AsyncHttpClient();
		String serLinID = servLin.getSerLinID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorService/SerLinID/"+serLinID, new AsyncHttpResponseHandler() {
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
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerServices.postInvalidate();
		spinnerServices.setAdapter(dAdapter);
		
		spinnerServices.setOnItemSelectedListener(new AuswahlServiceListener());
	}
	
	public class AuswahlServiceListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			sensorService = senServList.getList().get(position);
			getDatensatzFunktionen();
			getDatensatzSensoren();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void getDatensatzFunktionen(){
		AsyncHttpClient client = new AsyncHttpClient();
		String senSerID = sensorService.getSenSerID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorServiceFunktion/SenSerID/"+senSerID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        funktionList = gson.fromJson(response, SensorServiceFunktionList.class);
		        setDataToSpinnerFunktionen();
		    }
		});
	}
	
	public void setDataToSpinnerFunktionen() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (SensorServiceFunktion funk : funktionList.getList()) {
        	list.add(funk.getSenSerFunNam());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerFunktionen.postInvalidate();
		spinnerFunktionen.setAdapter(dAdapter);
		
//		spinnerServices.setOnItemSelectedListener(new AuswahlServiceListener());
	}
	
//	public class AuswahlServiceListener implements OnItemSelectedListener {
//		
//		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
//			sensorService = senServList.getList().get(position);
//			getDatensatzFunktionen();
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> arg0) {}
//	}
	
	public void getDatensatzSensoren(){
		AsyncHttpClient client = new AsyncHttpClient();
		String senSerID = sensorService.getSenSerID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Sensor/SenSerID/"+senSerID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        senList = gson.fromJson(response, SensorList.class);
		        setDataToSpinnerSensoren();
		    }
		});
	}
	
	public void setDataToSpinnerSensoren() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (Sensor sen : senList.getSensorList()) {
        	list.add(sen.getSenBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSensoren.postInvalidate();
		spinnerSensoren.setAdapter(dAdapter);
		
//		spinnerServices.setOnItemSelectedListener(new AuswahlServiceListener());
	}
}
