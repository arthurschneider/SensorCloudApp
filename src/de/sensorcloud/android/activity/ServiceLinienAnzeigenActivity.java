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
import de.sensorcloud.android.entitaet.Aktor;
import de.sensorcloud.android.entitaet.AktorList;
import de.sensorcloud.android.entitaet.AktorServiceFunktion;
import de.sensorcloud.android.entitaet.AktorServiceFunktionList;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.Sensor;
import de.sensorcloud.android.entitaet.SensorList;
import de.sensorcloud.android.entitaet.SensorServiceFunktion;
import de.sensorcloud.android.entitaet.SensorServiceFunktionList;
import de.sensorcloud.android.entitaet.ServiceLinien;
import de.sensorcloud.android.entitaet.ServiceLinienList;
import de.sensorcloud.android.entitaet.ServiceMitTyp;
import de.sensorcloud.android.entitaet.ServiceMitTypList;
import de.sensorcloud.android.helpertools.Helper;

public class ServiceLinienAnzeigenActivity extends Activity {

	Spinner spinnerSensoren;
	Spinner spinnerServices;
	Spinner spinnerFunktionen;
	Spinner spinnerServiceLinien;
	
	String nutStaID;
	
	SensorList senList;
	AktorList aktList;
	ServiceMitTypList servList;
	
	SensorServiceFunktionList senfunktionList;
	AktorServiceFunktionList aktfunktionList;
	
	Sensor sensor;
	ServiceMitTyp service;
	
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
		        servList = gson.fromJson(response, ServiceMitTypList.class);
		        setDataToSpinnerServices();
		    }
		});
	}
	
	public void setDataToSpinnerServices() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (ServiceMitTyp serv : servList.getList()) {
        	list.add(serv.getSerBez());
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
			service = servList.getList().get(position);
			getDatensatzFunktionen();
			getDatensatzSensorenAktoren();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void getDatensatzFunktionen(){
		AsyncHttpClient client = new AsyncHttpClient();
		
		if (service.getSerTyp().equals("SensorService")) {
			String senSerID = service.getSerID();
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorServiceFunktion/SenSerID/"+senSerID, new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			        Gson gson = new Gson();
			        senfunktionList = gson.fromJson(response, SensorServiceFunktionList.class);
			        setDataToSpinnerFunktionenSensor();
			    }
			});
		}
		
		if (service.getSerTyp().equals("AktorService")) {
			String aktSerID = service.getSerID();
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/AktorServiceFunktion/AktSerID/"+aktSerID, new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			        Gson gson = new Gson();
			        aktfunktionList = gson.fromJson(response, AktorServiceFunktionList.class);
			        setDataToSpinnerFunktionenAktor();
			    }
			});
		}
		
	}
	
	public void setDataToSpinnerFunktionenSensor() {
		List<String> list = new ArrayList<String>();
		list.clear();
		
		for (SensorServiceFunktion funk : senfunktionList.getList()) {
        	list.add(funk.getSenSerFunNam());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerFunktionen.postInvalidate();
		spinnerFunktionen.setAdapter(dAdapter);

	}
	
	public void setDataToSpinnerFunktionenAktor() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (AktorServiceFunktion funk : aktfunktionList.getList()) {
        	list.add(funk.getAktSerFunNam());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerFunktionen.postInvalidate();
		spinnerFunktionen.setAdapter(dAdapter);
	}
	

	
	public void getDatensatzSensorenAktoren(){
		AsyncHttpClient client = new AsyncHttpClient();
		
		if (service.getSerTyp().equals("SensorService")) {
			String senSerID = service.getSerID();
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Sensor/SenSerID/"+senSerID, new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			        Gson gson = new Gson();
			        senList = gson.fromJson(response, SensorList.class);
			        setDataToSpinnerSensoren();
			    }
			});
		}
		
		if (service.getSerTyp().equals("AktorService")) {
			String senSerID = service.getSerID();
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Aktor/AktSerID/"+senSerID, new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			        Gson gson = new Gson();
			        aktList = gson.fromJson(response, AktorList.class);
			        setDataToSpinnerAktoren();
			    }
			});
		}
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
		
	}
	
	public void setDataToSpinnerAktoren() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (Aktor akt : aktList.getList()) {
        	list.add(akt.getAktBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSensoren.postInvalidate();
		spinnerSensoren.setAdapter(dAdapter);
		
	}
}
