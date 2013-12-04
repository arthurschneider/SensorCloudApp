package de.sensorcloud.android.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.Sensor;
import de.sensorcloud.android.entitaet.SensorList;
import de.sensorcloud.android.entitaet.SensorVerbund;
import de.sensorcloud.android.entitaet.SensorVerbundList;
import de.sensorcloud.android.helpertools.Helper;

public class SensorVerbundActivity extends Activity implements OnItemSelectedListener {
	
	Spinner spinnerSenVerbBez;
	Spinner spinnerSenVerbSensoren;
	EditText senBezeichnungTxt;
	EditText senPositionTxt;
	EditText senRaumTxt;
	EditText senDatumTxt;
	
	int verbundPosition; 
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
		setDataToSpinner();
		
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
		        Log.i("Test", response);
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this);
		        SharedPreferences.Editor editor = sharedPreferences.edit();
		        
				editor.putString("SVerbundListe", response);
				editor.commit();
		    
		    }
		    
		});
	}
	
	public void setDataToSpinner() {
		
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		list.clear();
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this); 
		String json = mPrefs.getString("SVerbundListe", null);
		verbundList = gson.fromJson(json, SensorVerbundList.class);
		list.add("--Alle Sensoren anzeigen--");
		for (SensorVerbund verb : verbundList.getSenVerbundList()) {
        	list.add(verb.getSenVerBez());
		}
//		 list.add("müüüüüü1111111üüh");
//	     list.add("lööööö111111111öö");
//	     list.add("cffffff1111111111fffff");
//		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(SensorVerbundActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSenVerbBez.postInvalidate();
		spinnerSenVerbBez.setAdapter(dAdapter);
		
		spinnerSenVerbBez.setOnItemSelectedListener(new AuswahlSensor());
	}

	
	public void setSpinnerSenVerbSensor(int position){
		Gson gson = new Gson();
		AsyncHttpClient client = new AsyncHttpClient();

		Log.i("Debug", "Position : "+position);
		if (position == 0) {
			
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Sensor/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			        Log.i("Debug", response);
			        
			        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this);
			        SharedPreferences.Editor editor = sharedPreferences.edit();
//			        editor.remove("SVerbundListe");
					editor.putString("SensorV1Liste", response);
					editor.commit();
			       
			    }
			    
			});
			
		} else {
			
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this); 
			String json = mPrefs.getString("SVerbundListe", null);
			verbundList = gson.fromJson(json, SensorVerbundList.class);
			SensorVerbund verb = verbundList.getSenVerbundList().get(position-1);

			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund/SenVerID/"+verb.getSenVerID(), new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			        Log.i("Debug", response);
			        
			        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this);
			        SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("SensorV2Liste", response);
					editor.commit();
			      
			    }
			    
			});
		}
	
	
		List<String> list2 = new ArrayList<String>();
		list2.clear();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this);
		String json; 
		if (position == 0) {
			json = mPrefs.getString("SensorV1Liste", null);
		} else {
			json = mPrefs.getString("SensorV2Liste", null);
		}
		
		senList = gson.fromJson(json, SensorList.class);
		
		for (Sensor sen : senList.getSensorList()) {
        	list2.add(sen.getSenID());
		}
//     list2.add("müüüüüüüüh");
//     list2.add("lööööööö");
//     list2.add("cfffffffffff");
     
		
		ArrayAdapter<String> senAdapter = new ArrayAdapter<String>(SensorVerbundActivity.this, android.R.layout.simple_spinner_item, list2);
		senAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		senAdapter.notifyDataSetChanged();
		spinnerSenVerbSensoren.postInvalidate();
		spinnerSenVerbSensoren.setAdapter(senAdapter);
		
		spinnerSenVerbSensoren.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this); 
		String json = mPrefs.getString("SensorVListe", null);
		SensorList senList = gson.fromJson(json, SensorList.class);

		Sensor sen = senList.getSensorList().get(position);
    
				senBezeichnungTxt.setText(sen.getSenBez());
				senPositionTxt.setText(sen.getSenPos());
				senRaumTxt.setText(sen.getSenRauID());
				senDatumTxt.setText(sen.getSenDatEin());
//		String data = spinnerSenVerbSensoren.getItemAtPosition(position).toString();
//        Toast.makeText(SensorVerbundActivity.this, data, Toast.LENGTH_SHORT).show();
        
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	
	
	public class AuswahlSensor implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			verbundPosition = position;
			setSpinnerSenVerbSensor(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
