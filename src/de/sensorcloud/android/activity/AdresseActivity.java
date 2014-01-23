package de.sensorcloud.android.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.Adresse;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class AdresseActivity extends Activity {

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
		
		getDatensatz();
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
		        Gson gson = new Gson();
		        adrObj = gson.fromJson(response, Adresse.class);
		        setDataToView();
		    }	    
		});
	}
	
	public void setDataToView() {
		adrBezTxt.setText(adrObj.getAdrBez());
		adrStrTxt.setText(adrObj.getAdrStr());
		adrOrtTxt.setText(adrObj.getAdrOrt());
		adrPlzTxt.setText(adrObj.getAdrPlz());
		adrLanTxt.setText(adrObj.getAdrLan());
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
			        Toast.makeText(AdresseActivity.this, response, Toast.LENGTH_LONG).show();
			        getDatensatz();
		 }
		});
	}
}
