package de.sensorcloud.android.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.helpertools.Helper;

public class StammdatenActivity extends Activity {

	EditText nutAnrTxt;
	EditText nutStaNamTxt;
	EditText nutStaVorTxt;
	EditText nutStaFirTxt;
	EditText nutStaDatEinTxt;
	
	String nutStaID;
	NutzerStammdaten stammObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stammdaten_activity);
		
		nutAnrTxt = (EditText) findViewById(R.id.stmmdtn_anrede);
		nutStaNamTxt = (EditText) findViewById(R.id.stmmdtn_name);
		nutStaVorTxt = (EditText) findViewById(R.id.stmmdtn_vorname);
		nutStaFirTxt = (EditText) findViewById(R.id.stmmdtn_firma);
		nutStaDatEinTxt = (EditText) findViewById(R.id.stmmdtn_datumeintritt);
		
		getDatensatz();
		
		
	}	
		public void getDatensatz(){
			AsyncHttpClient client = new AsyncHttpClient();
			Gson gson = new Gson();
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(StammdatenActivity.this); 
			String json = mPrefs.getString("NutzerObj", null);
			nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
			client.get(Helper.BASE_URL+"/SensorCloudRest/crud/NutzerStammdaten/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
			    @Override
			    public void onSuccess(String response) {
			    	Gson gson = new Gson();
			    	stammObj = gson.fromJson(response, NutzerStammdaten.class);
			    	setDataToView();
			    }
			    
			});
			
			
		}
		
		public void setDataToView() {
			nutAnrTxt.setText(stammObj.getNutStaAnr());
			nutStaNamTxt.setText(stammObj.getNutStaNam());
			nutStaVorTxt.setText(stammObj.getNutStaVor());
			nutStaFirTxt.setText(stammObj.getNutStaFir());
			
			Date datum = new Date(stammObj.getNutStaDatEin());
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy  HH:mm:SS");
			nutStaDatEinTxt.setText(DATE_FORMAT.format(datum));
		}
		
		public void updateStammdaten(View view){
			stammObj.setNutStaAnr(nutAnrTxt.getText().toString());
			stammObj.setNutStaNam(nutStaNamTxt.getText().toString());
			stammObj.setNutStaVor(nutStaVorTxt.getText().toString());
			stammObj.setNutStaFir(nutStaFirTxt.getText().toString());
			
			
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(stammObj);
			
			StringEntity se = null;
		
			try {
			    se = new StringEntity(jsonElement.toString());
			} catch (UnsupportedEncodingException e) {
				Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
			}		
			
			AsyncHttpClient client = new AsyncHttpClient();
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			client.post(null,  Helper.BASE_URL+"/SensorCloudRest/crud/NutzerStammdaten", se, "application/json", new AsyncHttpResponseHandler() {
				 @Override
				    public void onSuccess(String response) {
				        Log.i("Test", response);
				        Toast.makeText(StammdatenActivity.this, response, Toast.LENGTH_LONG).show();
				        getDatensatz();
			 }
			});
		}
}
	

