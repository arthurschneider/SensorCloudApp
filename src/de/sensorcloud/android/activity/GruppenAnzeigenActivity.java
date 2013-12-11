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
import de.sensorcloud.android.entitaet.GruIDNutStaID;
import de.sensorcloud.android.entitaet.Gruppen;
import de.sensorcloud.android.entitaet.GruppenList;
import de.sensorcloud.android.entitaet.Mitglied;
import de.sensorcloud.android.entitaet.MitgliederList;
import de.sensorcloud.android.entitaet.NutzerEmail;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class GruppenAnzeigenActivity extends Activity {
	
	Spinner spinnerGruppe;
	Spinner spinnerGruppenMitglied;
	Spinner spinnerGrpMitgldEmail;
	EditText grpMitgName;
	
	String nutStaID, nutEmaAdr;
	GruppenList grpList;
	MitgliederList mitgldList;
	int gruppePosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gruppen_anzeigen_activity);
		
		grpMitgName = (EditText) findViewById(R.id.grp_mitg_name);
		spinnerGruppe = (Spinner) findViewById(R.id.spinnerGruppe);
		spinnerGruppenMitglied = (Spinner) findViewById(R.id.spinnerGruppenMitglied);
		spinnerGrpMitgldEmail = (Spinner) findViewById(R.id.spinnerGrpMitgldEmail);
		
		getDatensatzGruppen();
	}

	
	public void getDatensatzGruppen(){

		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(GruppenAnzeigenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Gruppen/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        Gson gson = new Gson();
		        grpList = gson.fromJson(response, GruppenList.class);
		        
		        setDataToSpinner();
		    }	    
		});
	}
	
	public void setDataToSpinner() {
		
		List<String> list = new ArrayList<String>();
		list.clear();
		spinnerGruppe.setAdapter(null);
		spinnerGruppe.invalidate();
		
		for (Gruppen grp : grpList.getGrpList()) {
        	list.add(grp.getGruBez());
		}
		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(GruppenAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.notifyDataSetChanged();
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGruppe.setAdapter(dAdapter);
		spinnerGruppe.setOnItemSelectedListener(new AuswahlGruppeListener());
	}
	
	public void getDatensatzGrpMitglied(int position){

		AsyncHttpClient client = new AsyncHttpClient();
		String gruID = grpList.getGrpList().get(position).getGruID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Gruppen/GruID/"+gruID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        Gson gson = new Gson();
		        mitgldList = gson.fromJson(response, MitgliederList.class);
		        
		        setMitgliedDataToSpinner();
		    }	    
		});
	}
	
	public void setMitgliedDataToSpinner() {
		
		List<String> list = new ArrayList<String>();
		list.clear();
		spinnerGruppenMitglied.setAdapter(null);
		spinnerGruppenMitglied.invalidate();
		
		for (Mitglied mitgld : mitgldList.getList()) {
        	list.add(mitgld.getNutzer().getNutStaNam());
		}
		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(GruppenAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.notifyDataSetChanged();
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGruppenMitglied.setAdapter(dAdapter);
		spinnerGruppenMitglied.setOnItemSelectedListener(new AuswahlGruppenMitgliedListener());
	}

	public class AuswahlGruppeListener implements OnItemSelectedListener {
	
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			gruppePosition = position;
			getDatensatzGrpMitglied(position);
		}
	
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	public class AuswahlGruppenMitgliedListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			Mitglied mitgld = mitgldList.getList().get(position);
			grpMitgName.setText(mitgld.getNutzer().getNutStaVor() + ", "+ mitgld.getNutzer().getNutStaNam());
			
			List<String> list = new ArrayList<String>();
			list.clear();
			spinnerGrpMitgldEmail.setAdapter(null);
			spinnerGrpMitgldEmail.invalidate();
			
			for (NutzerEmail mail : mitgld.getMailList()) {
	        	list.add(mail.getNutEmaAdr());
			}
			
			ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(GruppenAnzeigenActivity.this, android.R.layout.simple_spinner_item, list);
			dAdapter.notifyDataSetChanged();
			dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerGrpMitgldEmail.setAdapter(dAdapter);
			spinnerGrpMitgldEmail.setOnItemSelectedListener(new AuswahlGruppenMitgliedEmailListener());
		}
	
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	public class AuswahlGruppenMitgliedEmailListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			nutEmaAdr = parent.getItemAtPosition(position).toString();
		}
	
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	
	public void verlasseGruppe(View view){
		Gson gson = new Gson();
		GruIDNutStaID gru = new GruIDNutStaID();
		gru.setGruID(grpList.getGrpList().get(gruppePosition).getGruID());
		gru.setNutStaID(nutStaID);
		JsonElement jsonElement = gson.toJsonTree(gru);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null,  Helper.BASE_URL+"/SensorCloudRest/crud/Gruppen", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Toast.makeText(GruppenAnzeigenActivity.this, response, Toast.LENGTH_LONG).show();
		 }
	    
		});
	}
}
