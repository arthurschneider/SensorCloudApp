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
import de.sensorcloud.android.entitaet.NeueGruppeMitNutzer;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class GruppeErstellenActivity extends Activity {
	EditText grpNameTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gruppe_erstellen_activity);
		grpNameTxt = (EditText) findViewById(R.id.grp_erstellen_name);
	}

	public void erstelleGruppe(View view){
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(GruppeErstellenActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		String gruBez = grpNameTxt.getText().toString();
		NeueGruppeMitNutzer gruppeNutzer = new NeueGruppeMitNutzer();
		gruppeNutzer.setGruBez(gruBez);
		gruppeNutzer.setNutStaID(nutStaID);
		insertGruppe(gruppeNutzer);
	}
	
	public void insertGruppe(NeueGruppeMitNutzer gruppeNutzer){
		
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(gruppeNutzer);
		
		StringEntity se = null;
		
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.put(null,  Helper.BASE_URL+"/SensorCloudRest/crud/Gruppen/createGruppe", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Toast.makeText(GruppeErstellenActivity.this, response, Toast.LENGTH_LONG).show();
		 }
	    
		});
		
	}
}
