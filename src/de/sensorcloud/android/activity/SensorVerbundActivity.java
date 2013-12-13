package de.sensorcloud.android.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.Sensor;
import de.sensorcloud.android.entitaet.SensorList;
import de.sensorcloud.android.entitaet.SensorVerbund;
import de.sensorcloud.android.entitaet.SensorVerbundList;
import de.sensorcloud.android.helpertools.Helper;

public class SensorVerbundActivity extends Activity implements OnItemSelectedListener{
	
	Spinner spinnerSenVerbBez;
	Spinner spinnerSenVerbSensoren;
	EditText senBezeichnungTxt;
	EditText senPositionTxt;
	EditText senRaumTxt;
	EditText senDatumTxt;
	
	String nutStaID;
	SensorVerbundList verbundList;
	SensorList senList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensorverbund_activity);
		
		senBezeichnungTxt = (EditText) findViewById(R.id.senBezeichnung);
		senPositionTxt = (EditText) findViewById(R.id.senPosition);
		senRaumTxt = (EditText) findViewById(R.id.senRaum);
		senDatumTxt = (EditText) findViewById(R.id.senDatum);
		spinnerSenVerbBez = (Spinner) findViewById(R.id.spinnerSenVerbBez);
		spinnerSenVerbSensoren = (Spinner) findViewById(R.id.spinnerSenVerbSensoren);
		
		getDatensatzVerbund();
	}

	public void getDatensatzVerbund(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
				Gson gson = new Gson();
		    	verbundList = gson.fromJson(response, SensorVerbundList.class);
		    	setDataToSpinner();
		    }
		});
	}

	public void setDataToSpinner() {
		List<String> list = new ArrayList<String>();
		list.clear();
		
		list.add("--Alle Sensoren anzeigen--");
		for (SensorVerbund verb : verbundList.getSenVerbundList()) {
        	list.add(verb.getSenVerBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(SensorVerbundActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSenVerbBez.postInvalidate();
		spinnerSenVerbBez.setAdapter(dAdapter);
		
		spinnerSenVerbBez.setOnItemSelectedListener(new AuswahlSensorListener());
	}

	
	public void getDatensatzSensor(int position){
		if (position == 0) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Sensor/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			    	Gson gson = new Gson();
			    	senList = gson.fromJson(response, SensorList.class);
			    	setDataToSpinnerSenVerbSensoren();
			    }
			    
			});
			
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			SensorVerbund verb = verbundList.getSenVerbundList().get(position-1);
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund/SenVerID/"+verb.getSenVerID(), new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			    	Gson gson = new Gson();
			    	senList = gson.fromJson(response, SensorList.class);
			    	setDataToSpinnerSenVerbSensoren();
			    } 
			});
		}
	}
		
		
	public void setDataToSpinnerSenVerbSensoren(){
		List<String> list2 = new ArrayList<String>();
		list2.clear();
		
		for (Sensor sen : senList.getSensorList()) {
        	list2.add(sen.getSenID());
		}
     
		ArrayAdapter<String> senAdapter = new ArrayAdapter<String>(SensorVerbundActivity.this, android.R.layout.simple_spinner_item, list2);
		senAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		senAdapter.notifyDataSetChanged();
		spinnerSenVerbSensoren.postInvalidate();
		spinnerSenVerbSensoren.setAdapter(senAdapter);
		spinnerSenVerbSensoren.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Sensor sen = senList.getSensorList().get(position);
    
		senBezeichnungTxt.setText(sen.getSenBez());
		senPositionTxt.setText(sen.getSenPos());
		senRaumTxt.setText(sen.getSenRauID());
		
		Date datum = new Date(Long.parseLong(sen.getSenDatEin().trim()));
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy  HH:mm:SS");
		senDatumTxt.setText(DATE_FORMAT.format(datum));
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	
	public class AuswahlSensorListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			getDatensatzSensor(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
}
