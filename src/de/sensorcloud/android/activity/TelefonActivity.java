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
import de.sensorcloud.android.entitaet.NutzerTelefon;
import de.sensorcloud.android.entitaet.NutzerTelefonList;

public class TelefonActivity extends Activity implements OnItemSelectedListener {

	Spinner spinnerTelStmmdtn;
	EditText telNumTxt;
	EditText telBezTxt;
	String nutStaID;
	static List<String> list = new ArrayList<String>();
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telefon_activity);
		telNumTxt = (EditText) findViewById(R.id.tel_num);
		telBezTxt = (EditText) findViewById(R.id.tel_bez);

		 
		spinnerTelStmmdtn = (Spinner) findViewById(R.id.spinnerTelStmmdtn);
		
		
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get("http://babeauf.nt.fh-koeln.de:8080/SensorCloudRest/crud/NutzerTelefon/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        Gson gson = new Gson();
		        
		        list.clear();
		        NutzerTelefonList telefonListe = gson.fromJson(response, NutzerTelefonList.class);
		        
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		        SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("TelefonListe", response);
				editor.commit();
		        for (NutzerTelefon tel : telefonListe.getList()) {
		        	TelefonActivity.list.add(tel.getNutTelNum());
					//Log.i("innen", tel.getNutTelNum());
				}
		    }
		    
		});

		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTelStmmdtn.setAdapter(dAdapter);
		Log.i("Test", "----------------------\nHALeluja : ");
		spinnerTelStmmdtn.setOnItemSelectedListener(this);
			

	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Log.i("Select", "endlich getriggert");
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String json = mPrefs.getString("TelefonListe", null);
			NutzerTelefon telObj  = gson.fromJson(json, NutzerTelefonList.class).getList().get(arg2);
		 
			telBezTxt.setText(telObj.getNutTelBez());
			telNumTxt.setText(telObj.getNutTelNum());
			
			
			String data = spinnerTelStmmdtn.getItemAtPosition(arg2).toString();
	        Toast.makeText(TelefonActivity.this, data, Toast.LENGTH_SHORT).show();
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public void fuegeEin(String data){
		list.add(data);
	}
	
}
