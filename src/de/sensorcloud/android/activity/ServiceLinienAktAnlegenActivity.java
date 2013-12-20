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
import de.sensorcloud.android.entitaet.AktorService;
import de.sensorcloud.android.entitaet.AktorServiceList;
import de.sensorcloud.android.entitaet.AktorServiceMitServiceLinie;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.ServiceLinien;
import de.sensorcloud.android.entitaet.ServiceLinienList;
import de.sensorcloud.android.helpertools.Helper;

public class ServiceLinienAktAnlegenActivity extends Activity {

	Spinner spinnerAktoren;
	Spinner spinnerServices;
	Spinner spinnerServiceLinien;
	EditText servLinieName;
	
	String nutStaID;
	
	AktorList aktList;
	AktorServiceList aktServList;
	
	Aktor aktor;
	AktorService aktorService;
	
	ServiceLinienList servLinList;
	ServiceLinien servLin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_linien_akt_anlegen_activity);
		
		servLinieName = (EditText) findViewById(R.id.neue_servlinie_akt);

		spinnerAktoren = (Spinner) findViewById(R.id.spinnerSLAktor);
		spinnerServices = (Spinner) findViewById(R.id.spinnerSLAktServices);
		spinnerServiceLinien = (Spinner) findViewById(R.id.spinnerSLAktServLinien);
		getDatensatzAktoren();
		
	}
	
	public void getDatensatzAktoren(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ServiceLinienAktAnlegenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Aktor/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        aktList = gson.fromJson(response, AktorList.class);
		        setDataToSpinnerAktor();
		    }
		});
	}
	
	public void setDataToSpinnerAktor() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (Aktor akt : aktList.getList()) {
        	list.add(akt.getAktBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAktAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerAktoren.postInvalidate();
		spinnerAktoren.setAdapter(dAdapter);
		
		spinnerAktoren.setOnItemSelectedListener(new AuswahlAktorListener());
	}
	
	public class AuswahlAktorListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			aktor = aktList.getList().get(position);
			getDatensatzServices();
			getDatensatzServiceLinien();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void getDatensatzServices(){
		AsyncHttpClient client = new AsyncHttpClient();
		String senID = aktor.getAktID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/AktorService/AktID/"+senID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        aktServList = gson.fromJson(response, AktorServiceList.class);
		        setDataToSpinnerServices();
		    }
		});
	}
	
	public void setDataToSpinnerServices() {
		List<String> list = new ArrayList<String>();
		list.clear();
		for (AktorService serv : aktServList.getList()) {
        	list.add(serv.getAktSerBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAktAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerServices.postInvalidate();
		spinnerServices.setAdapter(dAdapter);
		
		spinnerServices.setOnItemSelectedListener(new AuswahlServiceListener());
	}
	
	public class AuswahlServiceListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			aktorService = aktServList.getList().get(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	
	public void getDatensatzServiceLinien(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ServiceLinienAktAnlegenActivity.this); 
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
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ServiceLinienAktAnlegenActivity.this, android.R.layout.simple_spinner_item, list);
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
	
	public void updateServiceLinieAkt(View view) {
		AktorServiceMitServiceLinie servLinServ = new AktorServiceMitServiceLinie();
		servLinServ.setService(aktorService);
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
		client.post(null, Helper.BASE_URL+"/SensorCloudRest/crud/ServiceLinien/Aktor", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(ServiceLinienAktAnlegenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
		});
	}
	
	public void insertServiceInLinieAkt(View view) {
		String name = servLinieName.getText().toString();
		
		AktorServiceMitServiceLinie servLinServ = new AktorServiceMitServiceLinie();
		servLinServ.setService(aktorService);
		
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
		client.put(null, Helper.BASE_URL+"/SensorCloudRest/crud/ServiceLinien/Aktor", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			 public void onSuccess(String response) {
		        Toast.makeText(ServiceLinienAktAnlegenActivity.this, response, Toast.LENGTH_LONG).show();
			 }
		});
	}

}
