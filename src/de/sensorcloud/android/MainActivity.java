package de.sensorcloud.android;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.entitaet.NutzerTelefon;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}
	
	public void postData(View vw) {
		AsyncHttpClient client = new AsyncHttpClient();
//		NutzerStammdaten nutzer = new NutzerStammdaten();
//        nutzer.setNutStaAdrID("b79c662a-c718-4fb7-b3a0-a60af1084eca");
//        nutzer.setNutStaAnr("Herr");
//        nutzer.setNutStaDatEin("1377700816");
//        nutzer.setNutStaFir("FH Koeln");
//        nutzer.setNutStaID("c24d959d-9aa1-47e1-92b4-4ebdd89583f0");
//        nutzer.setNutStaNam("Schneider");
//        nutzer.setNutStaVor("Arthur");
        
//		NutzerEmail nutEma = new NutzerEmail();
//		nutEma.setNutEmaAdr("nc-schneiar15@netcologne.de");
//		nutEma.setNutEmaBez("privat");
//		nutEma.setNutEmaID("8bdcdd8f-5129-4bba-9a72-ce24ddb6a27f");
//		nutEma.setNutEmaNutStaID("c24d959d-9aa1-47e1-92b4-4ebdd89583f0");
		
		NutzerTelefon nutTel = new NutzerTelefon();
		nutTel.setNutTelBez("privat");
		nutTel.setNutTelID("ac4b3cae-fec5-4c32-b61d-447aaf7b6c31");
		nutTel.setNutTelNum("022339856145");
		nutTel.setNutTelNutStaID("c24d959d-9aa1-47e1-92b4-4ebdd89583f0");
        Gson gson = new Gson();
        JsonElement jsonElement = null;
        jsonElement = gson.toJsonTree(nutTel);
		StringEntity se = null;
		
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
		    // handle exceptions properly!
		}
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null, "http://10.0.2.2:8080/SensorCloudRest/update/nutTel", se, "application/json", new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		    }
		});

	}	
}
