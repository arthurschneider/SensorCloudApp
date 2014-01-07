package de.sensorcloud.android.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.Login;
import de.sensorcloud.android.helpertools.Helper;


public class MainActivity extends Activity {
	
	EditText eMailTxt;
	EditText passwortTxt;
	TextView infoLbl;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		eMailTxt = (EditText) findViewById(R.id.email);
		passwortTxt = (EditText) findViewById(R.id.passwort);
		infoLbl = (TextView) findViewById(R.id.txtView_info);
	}
	
	public void anmelden(View view) {
		infoLbl.setText("");
		
		final Intent intent = new Intent(this, AuswahlseiteActivity.class);
		
		Login login = new Login();
		login.setEmail(eMailTxt.getText().toString());
		login.setPasswort(passwortTxt.getText().toString());
		
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(login);
		StringEntity se = null;
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null, Helper.BASE_URL+"/SensorCloudRest/crud/Login/authetifizieren", se, "application/json", new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		    	Log.i("Debug", response);
		        if (response.equals("Denied")) {
		        	infoLbl.setTextColor(Color.RED);
		        	infoLbl.setText("Anmeldung nicht gelungen");
				}else {
					SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			        SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("NutzerObj", response);
					editor.commit();
					startActivity(intent);
				} 
		    }
		});
	}	
	
	public void registrieren(View view){
		Intent intent  = new Intent(this, RegistrierenActivity.class);
		startActivity(intent);
	}
}
