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
import de.sensorcloud.android.entitaet.NutzerSicherheit;
import de.sensorcloud.android.entitaet.NutzerSicherheitList;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;


public class SicherheitActivity extends Activity implements OnItemSelectedListener {

	Spinner spinnerSicherheit;
	EditText nutSicPasTxt;
	EditText nutSicPubKeyTxt;
	EditText nutSicPriKeyTxt;
	
	NutzerSicherheit sicherObj;
	NutzerSicherheitList sicherList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sicherheit_activity);
		
		nutSicPasTxt = (EditText) findViewById(R.id.sicherheit_pas);
		nutSicPubKeyTxt = (EditText) findViewById(R.id.sicherheit_pubKey);
		nutSicPriKeyTxt = (EditText) findViewById(R.id.sicherheit_priKey);
		spinnerSicherheit = (Spinner) findViewById(R.id.spinnerSicherheit);
		
		getDatensatz();
		setDataToSpinner();
	}
	
	public void getDatensatz(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get( Helper.BASE_URL+"/SensorCloudRest/crud/NutzerSicherheit/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		        SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("NSicherheitList", response);
				editor.commit();
		    }
		    
		});
	}
	
	public void setDataToSpinner() {
		
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		list.clear();
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String json = mPrefs.getString("NSicherheitList", null);
		sicherList = gson.fromJson(json, NutzerSicherheitList.class);
		
		for (NutzerSicherheit sicher : sicherList.getList()) {
			list.add(sicher.getNutSicPas());
		}
     	
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSicherheit.postInvalidate();
		spinnerSicherheit.setAdapter(dAdapter);
		
		spinnerSicherheit.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Log.i("Select", "endlich getriggert");
		
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String json = mPrefs.getString("NSicherheitList", null);
		sicherList = gson.fromJson(json, NutzerSicherheitList.class);
		sicherObj = sicherList.getList().get(position);
		
		nutSicPasTxt.setText(sicherObj.getNutSicPas());
		nutSicPubKeyTxt.setText(sicherObj.getNutSicPubKey());
		nutSicPriKeyTxt.setText(sicherObj.getNutSicPriKey());
		
		String data = spinnerSicherheit.getItemAtPosition(position).toString();
        Toast.makeText(SicherheitActivity.this, data, Toast.LENGTH_SHORT).show();
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public void updateSicherheit(View view){
		
		sicherObj.setNutSicPas(nutSicPasTxt.getText().toString());
		sicherObj.setNutSicPubKey(nutSicPubKeyTxt.getText().toString());
		sicherObj.setNutSicPriKey(nutSicPriKeyTxt.getText().toString());
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(sicherObj);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null,  Helper.BASE_URL+"/SensorCloudRest/crud/NutzerSicherheit", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Log.i("Test", response);
			        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
		 }
	    
		});
		
		getDatensatz();
		setDataToSpinner();
	}

}
