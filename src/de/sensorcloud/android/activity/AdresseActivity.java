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
import de.sensorcloud.android.entitaet.Adresse;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class AdresseActivity extends Activity implements OnItemSelectedListener {

	Spinner spinnerAdrStmmdtn;
	EditText adrBezTxt;
	EditText adrStrTxt;
	EditText adrOrtTxt;
	EditText adrPlzTxt;
	EditText adrLanTxt;
	
	String nutStaAdrID;
	Adresse adrObj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adresse_activity);
		
		adrBezTxt = (EditText) findViewById(R.id.adr_bez);
		adrStrTxt = (EditText) findViewById(R.id.adr_str);
		adrOrtTxt = (EditText) findViewById(R.id.adr_ort);
		adrPlzTxt = (EditText) findViewById(R.id.adr_plz);
		adrLanTxt = (EditText) findViewById(R.id.adr_lan);
		spinnerAdrStmmdtn = (Spinner) findViewById(R.id.spinnerAdrStmmdtn);
		
		getDatensatz();
		setDataToSpinner();
	}

	public void getDatensatz(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AdresseActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		nutStaAdrID = gson.fromJson(json, NutzerStammdaten.class).getNutStaAdrID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Adresse/AdrID/"+nutStaAdrID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AdresseActivity.this);
		        SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("AdressObj", response);
				editor.commit();
		    }
		    
		});
	}
	
	public void setDataToSpinner() {
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		list.clear();
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AdresseActivity.this); 
		String json = mPrefs.getString("AdressObj", null);
		Adresse adrObj = gson.fromJson(json, Adresse.class);
		
     	list.add(adrObj.getAdrBez());
//     	list.add("maaaaaaä");
//     	list.add("maadadadadaaaaaä");
//     	list.add("maaadadadadadadadaaaaä");
     	
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(AdresseActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerAdrStmmdtn.postInvalidate();
		spinnerAdrStmmdtn.setAdapter(dAdapter);
		
		spinnerAdrStmmdtn.setOnItemSelectedListener(this);


	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AdresseActivity.this); 
		String json = mPrefs.getString("AdressObj", null);
		Adresse adrObj = gson.fromJson(json, Adresse.class);
		
			adrBezTxt.setText(adrObj.getAdrBez());
			adrStrTxt.setText(adrObj.getAdrStr());
			adrOrtTxt.setText(adrObj.getAdrOrt());
			adrPlzTxt.setText(adrObj.getAdrPlz());
			adrLanTxt.setText(adrObj.getAdrLan());
		
			
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public void updateAdresse(View view){
		
		adrObj.setAdrBez(adrBezTxt.getText().toString());
		adrObj.setAdrStr(adrStrTxt.getText().toString());
		adrObj.setAdrOrt(adrOrtTxt.getText().toString());
		adrObj.setAdrPlz(adrPlzTxt.getText().toString());
		adrObj.setAdrLan(adrLanTxt.getText().toString());
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(adrObj);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null, Helper.BASE_URL+"/SensorCloudRest/crud/Adresse", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Log.i("Test", response);
			        Toast.makeText(AdresseActivity.this, response, Toast.LENGTH_LONG).show();
		 }
	    
		});
		
		getDatensatz();
		setDataToSpinner();
	}
}
