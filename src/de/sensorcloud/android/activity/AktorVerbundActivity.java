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

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.Aktor;
import de.sensorcloud.android.entitaet.AktorList;
import de.sensorcloud.android.entitaet.AktorVerbund;
import de.sensorcloud.android.entitaet.AktorVerbundList;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class AktorVerbundActivity extends Activity implements OnItemSelectedListener{

	Spinner spinnerAktVerbBez;
	Spinner spinnerAktVerbAktoren;
	EditText aktBezeichnungTxt;
	EditText aktPositionTxt;
	EditText aktRaumTxt;
	EditText aktDatumTxt;
	
	AktorVerbundList verbundList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aktorverbund_activity);
		
		aktBezeichnungTxt = (EditText) findViewById(R.id.aktBezeichnung);
		aktPositionTxt = (EditText) findViewById(R.id.aktPosition);
		aktRaumTxt = (EditText) findViewById(R.id.aktRaum);
		aktDatumTxt = (EditText) findViewById(R.id.aktDatum);
		spinnerAktVerbBez = (Spinner) findViewById(R.id.spinnerAktVerbBez);
		spinnerAktVerbAktoren = (Spinner) findViewById(R.id.spinnerAktVerbAktoren);
		
		getDatensatzVerbund();
		setDataToSpinner();
	}

	public void getDatensatzVerbund(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AktorVerbundActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/AktorVerbund/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AktorVerbundActivity.this);
		        SharedPreferences.Editor editor = sharedPreferences.edit();
		        
				editor.putString("AVerbundListe", response);
				editor.commit();
		        
		    }
		    
		});
	}
	
	public void setDataToSpinner() {
		
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		list.clear();
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AktorVerbundActivity.this); 
		String json = mPrefs.getString("AVerbundListe", null);
		AktorVerbundList verbundList = gson.fromJson(json, AktorVerbundList.class);
		
		for (AktorVerbund verb : verbundList.getAktVerbundList()) {
        	list.add(verb.getAktVerBez());
		}
//		 list.add("müüüüüü1111111üüh");
//	     list.add("lööööö111111111öö");
//	     list.add("cffffff1111111111fffff");
		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(AktorVerbundActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerAktVerbBez.postInvalidate();
		spinnerAktVerbBez.setAdapter(dAdapter);
		
		spinnerAktVerbBez.setOnItemSelectedListener(new AuswahlAktor());
	}
	
	public void setSpinnerAktVerbAktor(int position){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AktorVerbundActivity.this); 
		String json = mPrefs.getString("AVerbundListe", null);
		verbundList = gson.fromJson(json, AktorVerbundList.class);
	
		AktorVerbund verb = verbundList.getAktVerbundList().get(position);
        	
		
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/AktorVerbund/AktVerID/"+verb.getAktVerID(), new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AktorVerbundActivity.this);
		        SharedPreferences.Editor editor = sharedPreferences.edit();
//		        editor.remove("SVerbundListe");
				editor.putString("AktorVListe", response);
				editor.commit();
		        
		    }
		    
		});

		List<String> list2 = new ArrayList<String>();
		list2.clear();
		
		
		json = mPrefs.getString("AktorVListe", null);
		AktorList aktList = gson.fromJson(json, AktorList.class);
		
		for (Aktor akt : aktList.getList()) {
        	list2.add(akt.getAktID());
		}
//     list2.add("müüüüüüüüh");
//     list2.add("lööööööö");
//     list2.add("cfffffffffff");
     
		
		ArrayAdapter<String> senAdapter = new ArrayAdapter<String>(AktorVerbundActivity.this, android.R.layout.simple_spinner_item, list2);
		senAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		senAdapter.notifyDataSetChanged();
		spinnerAktVerbAktoren.postInvalidate();
		spinnerAktVerbAktoren.setAdapter(senAdapter);
		
		spinnerAktVerbAktoren.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AktorVerbundActivity.this); 
		String json = mPrefs.getString("AktorVListe", null);
		AktorList aktList = gson.fromJson(json, AktorList.class);
		
		Aktor akt = aktList.getList().get(position);
        	
				
				aktBezeichnungTxt.setText(akt.getAktBez());
				aktPositionTxt.setText(akt.getAktPos());
				aktRaumTxt.setText(akt.getAktRauID());
				aktDatumTxt.setText(akt.getAktDatEin());

//		String data = spinnerAktVerbAktoren.getItemAtPosition(position).toString();
//        Toast.makeText(AktorVerbundActivity.this, data, Toast.LENGTH_SHORT).show();
			
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	
	public class AuswahlAktor implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			
			setSpinnerAktVerbAktor(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
