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
import de.sensorcloud.android.entitaet.SensorVerbund;
import de.sensorcloud.android.entitaet.SensorVerbundSet;
import de.sensorcloud.android.helpertools.Helper;

public class SensorVerbundActivity extends Activity implements OnItemSelectedListener{
	
	Spinner spinnerSenVerbBez;
	Spinner spinnerSenVerbSensoren;
	EditText senBezeichnungTxt;
	EditText senPositionTxt;
	EditText senRaumTxt;
	EditText senDatumTxt;
	
	String nutStaID;
	SensorVerbundSet verbundSet;

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
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		        SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("VerbundListe", response);
				editor.commit();
		        
		    }
		    
		});
	}
	
	public void setDataToSpinner() {
		
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		list.clear();
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String json = mPrefs.getString("VerbundListe", null);
		verbundSet = gson.fromJson(json, SensorVerbundSet.class);
		
		for (SensorVerbund verb : verbundSet.getSenVerbundSet()) {
        	list.add(verb.getSenVerBez());
		}
		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSenVerbBez.postInvalidate();
		spinnerSenVerbBez.setAdapter(dAdapter);
		
		spinnerSenVerbBez.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Log.i("Select", "endlich getriggert");
		
			
			String data = spinnerSenVerbBez.getItemAtPosition(position).toString();
	        Toast.makeText(SensorVerbundActivity.this, data, Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
}
