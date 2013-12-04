package de.sensorcloud.android.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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





import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.SensorVerbund;
import de.sensorcloud.android.entitaet.SensorVerbundList;
import de.sensorcloud.android.helpertools.AsyncResponse;
import de.sensorcloud.android.helpertools.Helper;

public class SensorVerbundActivity extends Activity implements OnItemSelectedListener{
	
	Spinner spinnerSenVerbBez;
	Spinner spinnerSenVerbSensoren;
	EditText senBezeichnungTxt;
	EditText senPositionTxt;
	EditText senRaumTxt;
	EditText senDatumTxt;
	
	public String ausgabe = new String();
	SensorVerbundList verbundList;
	WebServiceTask wst = new WebServiceTask();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensorverbund_activity);
		
		senBezeichnungTxt = (EditText) findViewById(R.id.senBezeichnung);
		senPositionTxt = (EditText) findViewById(R.id.senPosition);
		senRaumTxt = (EditText) findViewById(R.id.senRaum);
		senDatumTxt = (EditText) findViewById(R.id.senDatum);
		spinnerSenVerbBez = (Spinner) findViewById(R.id.spinnerSenVerbBez);
		spinnerSenVerbSensoren = (Spinner) findViewById(R.id.spinnerSenVerbSensoren);

		
		getDatensatzVerbund();
		
		
	}

	public void getDatensatzVerbund(){
		WebServiceTask wst = new WebServiceTask();
         
         wst.execute();
	}

	private String readStream(InputStream in) {
	  BufferedReader reader = null;
	  String line = "httttt";
	  try {
	    reader = new BufferedReader(new InputStreamReader(in));
	    
	    while ((line = reader.readLine()) != null) {
	      System.out.println(line);
	      
	    }
	    return line;
	  } catch (IOException e) {
	    e.printStackTrace();
	  } finally {
	    if (reader != null) {
	      try {
	        reader.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	        }
	    }
	  }
	return line;
	} 
	
	public void setDataToSpinner() {
		
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		list.clear();
		
		Log.i("AusgabeString", ausgabe);
//		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this); 
//		String json = mPrefs.getString("SVerbundListe", null);
//		verbundList = gson.fromJson(json, SensorVerbundList.class);
		
		for (SensorVerbund verb : verbundList.getSenVerbundList()) {
        	list.add(verb.getSenVerBez());
		}
//		 list.add("m������1111111��h");
//	     list.add("l�����111111111��");
//	     list.add("cffffff1111111111fffff");
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(SensorVerbundActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerSenVerbBez.postInvalidate();
		spinnerSenVerbBez.setAdapter(dAdapter);
		
		spinnerSenVerbBez.setOnItemSelectedListener(new AuswahlSensor());
	}

	
	public void setSpinnerSenVerbSensor(int position){
//		AsyncHttpClient client = new AsyncHttpClient();
//		Gson gson = new Gson();
//		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this); 
//		String json = mPrefs.getString("SVerbundListe", null);
//		verbundList = gson.fromJson(json, SensorVerbundList.class);
//	
//		SensorVerbund verb = verbundList.getSenVerbundList().get(position);
//        	
//		
//		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund/SenVerID/"+verb.getSenVerID(), new AsyncHttpResponseHandler() {
//		    @Override
//		    public void onSuccess(String response) {
//		        Log.i("Test", response);
//		        
//		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this);
//		        SharedPreferences.Editor editor = sharedPreferences.edit();
////		        editor.remove("SVerbundListe");
//				editor.putString("SensorVListe", response);
//				editor.commit();
//		        
//		    }
//		    
//		});
//		
//	
		List<String> list2 = new ArrayList<String>();
		list2.clear();
//		
//		
//		json = mPrefs.getString("SensorVListe", null);
//		SensorList senList = gson.fromJson(json, SensorList.class);
//		
//		for (Sensor sen : senList.getSensorList()) {
//        	list2.add(sen.getSenID());
//		}
     list2.add("m��������h");
     list2.add("l�������");
     list2.add("cfffffffffff");
     
		
		ArrayAdapter<String> senAdapter = new ArrayAdapter<String>(SensorVerbundActivity.this, android.R.layout.simple_spinner_item, list2);
		senAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		senAdapter.notifyDataSetChanged();
		spinnerSenVerbSensoren.postInvalidate();
		spinnerSenVerbSensoren.setAdapter(senAdapter);
		
		spinnerSenVerbSensoren.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		Gson gson = new Gson();
//		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this); 
//		String json = mPrefs.getString("SensorVListe", null);
//		SensorList senList = gson.fromJson(json, SensorList.class);
//		
//		Sensor sen = senList.getSensorList().get(position);
//        	
//				
//				senBezeichnungTxt.setText(sen.getSenBez());
//				senPositionTxt.setText(sen.getSenPos());
//				senRaumTxt.setText(sen.getSenRauID());
//				senDatumTxt.setText(sen.getSenDatEin());

		String data = spinnerSenVerbSensoren.getItemAtPosition(position).toString();
        Toast.makeText(SensorVerbundActivity.this, data, Toast.LENGTH_SHORT).show();
			
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public class AuswahlSensor implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			
			setSpinnerSenVerbSensor(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	
	 private class WebServiceTask  extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... arg0) {
				Gson gson = new Gson();
				SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SensorVerbundActivity.this); 
				String json = mPrefs.getString("NutzerObj", null);
				String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
				String response = new String();
				try {
					  URL url = new URL(Helper.BASE_URL+"/SensorCloudRest/crud/SensorVerbund/NutStaID/"+nutStaID);
					  HttpURLConnection con = (HttpURLConnection) url
					    .openConnection();
					  BufferedReader buffer = new BufferedReader(new InputStreamReader(con.getInputStream()));
			          String s = "";
			          while ((s = buffer.readLine()) != null) {
			            response += s;
			          }
					  } catch (Exception e) {
					  e.printStackTrace();
					}
				return response;
			}
			
			@Override
	        protected void onPostExecute(String response) {
				super.onPostExecute(response);
				Gson gson = new Gson();
				ausgabe = response;
				
				Log.d("Debug", response);
			    verbundList = gson.fromJson(response, SensorVerbundList.class);
				setDataToSpinner();
			}
		 }


	
}
