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
	AktorList aktList;
	String nutStaID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aktor_verbund_activity);
		
		aktBezeichnungTxt = (EditText) findViewById(R.id.aktBezeichnung);
		aktPositionTxt = (EditText) findViewById(R.id.aktPosition);
		aktRaumTxt = (EditText) findViewById(R.id.aktRaum);
		aktDatumTxt = (EditText) findViewById(R.id.aktDatum);
		spinnerAktVerbBez = (Spinner) findViewById(R.id.spinnerAktVerbBez);
		spinnerAktVerbAktoren = (Spinner) findViewById(R.id.spinnerAktVerbAktoren);
		
		getDatensatzVerbund();
		
	}

	public void getDatensatzVerbund(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AktorVerbundActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/AktorVerbund/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Gson gson = new Gson();
		        verbundList = gson.fromJson(response, AktorVerbundList.class);
		        setDataToSpinnerAktorVerbBez();
		        
		    }
		    
		});
	}
	
	public void setDataToSpinnerAktorVerbBez() {
		List<String> list = new ArrayList<String>();
		list.clear();
		
		list.add("--Alle Aktoren anzeigen--");
		for (AktorVerbund verb : verbundList.getAktVerbundList()) {
        	list.add(verb.getAktVerBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(AktorVerbundActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerAktVerbBez.postInvalidate();
		spinnerAktVerbBez.setAdapter(dAdapter);
		
		spinnerAktVerbBez.setOnItemSelectedListener(new AuswahlAktorVerbundListener());
	}
	
	public void getDatensatzAktoren(int position){
		if (position == 0) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Aktor/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			    	Gson gson = new Gson();
			    	aktList = gson.fromJson(response, AktorList.class);
			    	setDataToSpinnerAktorVerbAktoren();
			    }
			    
			});
			
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			AktorVerbund verb = verbundList.getAktVerbundList().get(position-1);
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/AktorVerbund/AktVerID/"+verb.getAktVerID(), new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			        Gson gson = new Gson();
			        aktList = gson.fromJson(response, AktorList.class);
			        setDataToSpinnerAktorVerbAktoren();
			    }    
			});
		}
	}
	
	public void setDataToSpinnerAktorVerbAktoren(){
		List<String> list = new ArrayList<String>();
		list.clear();
		
		for (Aktor akt : aktList.getList()) {
        	list.add(akt.getAktID());
		}
		
		ArrayAdapter<String> senAdapter = new ArrayAdapter<String>(AktorVerbundActivity.this, android.R.layout.simple_spinner_item, list);
		senAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		senAdapter.notifyDataSetChanged();
		spinnerAktVerbAktoren.postInvalidate();
		spinnerAktVerbAktoren.setAdapter(senAdapter);
		spinnerAktVerbAktoren.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Aktor akt = aktList.getList().get(position);
		aktBezeichnungTxt.setText(akt.getAktBez());
		aktPositionTxt.setText(akt.getAktPos());
		aktRaumTxt.setText(akt.getAktRauID());
		
		Date datum = new Date(Long.parseLong(akt.getAktDatEin().trim()));
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy  HH:mm:SS");
		aktDatumTxt.setText(DATE_FORMAT.format(datum));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	
	public class AuswahlAktorVerbundListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			getDatensatzAktoren(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
}
