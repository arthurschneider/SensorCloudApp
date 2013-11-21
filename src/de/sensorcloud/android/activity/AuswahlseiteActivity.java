package de.sensorcloud.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.NutzerStammdaten;

public class AuswahlseiteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auswahlseite_activity);
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String json = mPrefs.getString("NutzerObj", null);
	    NutzerStammdaten obj = gson.fromJson(json, NutzerStammdaten.class);
	    Log.i("Test", "string : "+json);
	}

	
	public void goToNutzerStammdaten(View vw) {

		Intent intent  = new Intent(this, NutzerStammdatenActivity.class);
		startActivity(intent);
	}

}
