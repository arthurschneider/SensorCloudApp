package de.sensorcloud.android.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.Event;
import de.sensorcloud.android.entitaet.EventAktion;
import de.sensorcloud.android.entitaet.EventList;
import de.sensorcloud.android.entitaet.EventRegel;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.SensorEvent;
import de.sensorcloud.android.helpertools.Helper;

public class EventRegelActivity extends Activity {
	
	Spinner spinnerEventBez;
	Spinner spinnerSensorEvent;
	Spinner spinnerEveAktion;
	
	EditText eve_seneve_bez;
	EditText eve_seneve_parameter;
	EditText eve_seneve_operator;
	EditText eve_seneve_schwwert;
	
	EditText eve_eveakt_akt;
	EditText eve_eveakt_bez;
	EditText eve_eveakt_funkt;
	EditText eve_eveakt_wert;
	
	EventList eventList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventregel_activity);
		eve_seneve_bez = (EditText) findViewById(R.id.eve_seneve_bez);
		eve_seneve_parameter = (EditText) findViewById(R.id.eve_seneve_parameter);
		eve_seneve_operator = (EditText) findViewById(R.id.eve_seneve_operator);
		eve_seneve_schwwert = (EditText) findViewById(R.id.eve_seneve_schwwert);
		
		eve_eveakt_akt = (EditText) findViewById(R.id.eve_eveakt_akt);
		eve_eveakt_bez = (EditText) findViewById(R.id.eve_eveakt_bez);
		eve_eveakt_funkt = (EditText) findViewById(R.id.eve_eveakt_funkt);
		eve_eveakt_wert = (EditText) findViewById(R.id.eve_eveakt_wert);
		spinnerEventBez = (Spinner) findViewById(R.id.spinnerEventBez);
		spinnerSensorEvent = (Spinner) findViewById(R.id.spinnerSensorEvent);
		spinnerEveAktion = (Spinner) findViewById(R.id.spinnerEveAktion);
		
		getDatensatzVerbund();
		setDataToSpinner();
		
	}
	public void getDatensatzVerbund(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Event/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this);
		        SharedPreferences.Editor editor = sharedPreferences.edit();
		        
				editor.putString("EventBez", response);
				editor.commit();
		    }
		});
	}
	
	public void setDataToSpinner() {
		
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		list.clear();
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this); 
		String json = mPrefs.getString("EventBez", null);
		EventList eventList = gson.fromJson(json, EventList.class);
		
		for (Event event : eventList.getList()) {
        	list.add(event.getEveBez());
		}
//		 list.add("müüüüüü1111111üüh");
//	     list.add("lööööö111111111öö");
//	     list.add("cffffff1111111111fffff");
		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(EventRegelActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerEventBez.postInvalidate();
		spinnerEventBez.setAdapter(dAdapter);
		
		spinnerEventBez.setOnItemSelectedListener(new AuswahlSensorListener());
	}
	
	
	public void setSenorEvent(int position){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this); 
		String json = mPrefs.getString("EventBez", null);
		eventList = gson.fromJson(json, EventList.class);
	
		Event event = eventList.getList().get(position);
        	
		
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Event/EveID/"+event.getEveID(), new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this);
		        SharedPreferences.Editor editor = sharedPreferences.edit();
//		        editor.remove("SVerbundListe");
				editor.putString("EventRegel", response);
				editor.commit();
		        
		    }
		    
		});
		
	
		List<String> list2 = new ArrayList<String>();
		list2.clear();
		
		
		json = mPrefs.getString("EventRegel", null);
		EventRegel eventRegel = gson.fromJson(json, EventRegel.class);
		
		for (SensorEvent senEve : eventRegel.getSensorEvent()) {
        	list2.add(senEve.getSenEveQueID());
		}
//     list2.add("müüüüüüüüh");
//     list2.add("lööööööö");
//     list2.add("cfffffffffff");
//     
		
		ArrayAdapter<String> senAdapter = new ArrayAdapter<String>(EventRegelActivity.this, android.R.layout.simple_spinner_item, list2);
		senAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		senAdapter.notifyDataSetChanged();
		spinnerSensorEvent.postInvalidate();
		spinnerSensorEvent.setAdapter(senAdapter);
		
		spinnerSensorEvent.setOnItemSelectedListener(new SensorEventListener());
	}
	
	
	public void setEventAktion(int position){
//		AsyncHttpClient client = new AsyncHttpClient();
//		Gson gson = new Gson();
//		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this); 
//		String json = mPrefs.getString("EventBez", null);
//		eventList = gson.fromJson(json, EventList.class);
//	
//		Event event = eventList.getList().get(position);
//        	
//		
//		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Event/EveID/"+event.getEveID(), new AsyncHttpResponseHandler() {
//		    @Override
//		    public void onSuccess(String response) {
//		        Log.i("Test", response);
//		        
//		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this);
//		        SharedPreferences.Editor editor = sharedPreferences.edit();
////		        editor.remove("SVerbundListe");
//				editor.putString("EventRegel", response);
//				editor.commit();
//		        
//		    }
//		    
//		});
		Gson gson = new Gson();
		
		List<String> list3 = new ArrayList<String>();
		list3.clear();
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this); 
		String json = mPrefs.getString("EventRegel", null);
		EventRegel eventRegel = gson.fromJson(json, EventRegel.class);
		
		for (EventAktion eveAkt : eventRegel.getEventAktion()) {
        	list3.add(eveAkt.getEveAktiZieID());
		}
//     list3.add("müüüüüüüüh");
//     list3.add("lööööööö");
//     list3.add("cfffffffffff");
     
		
		ArrayAdapter<String> aktAdapter = new ArrayAdapter<String>(EventRegelActivity.this, android.R.layout.simple_spinner_item, list3);
		aktAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aktAdapter.notifyDataSetChanged();
		spinnerEveAktion.postInvalidate();
		spinnerEveAktion.setAdapter(aktAdapter);
		
		spinnerEveAktion.setOnItemSelectedListener(new EventAktionListener());
	}
	
	
	

	public class AuswahlSensorListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			
			setSenorEvent(position);
			setEventAktion(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	public class SensorEventListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			Gson gson =new Gson();
		
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this); 
			String json = mPrefs.getString("EventRegel", null);
			EventRegel eventRegel = gson.fromJson(json, EventRegel.class);
			
			SensorEvent senEve =  eventRegel.getSensorEvent().get(position);
			eve_seneve_bez.setText(senEve.getSenEveQue()+":"+senEve.getSenEveQueID());
			eve_seneve_parameter.setText(senEve.getSenEvePhyNam());
			eve_seneve_operator.setText(senEve.getSenEveVop());
			eve_seneve_schwwert.setText(senEve.getSenEveWer());
			
			
//			String data = spinnerSensorEvent.getItemAtPosition(position).toString();
//	        Toast.makeText(EventRegelActivity.this, data, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	
	public class EventAktionListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			
			Gson gson =new Gson();
			
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this); 
			String json = mPrefs.getString("EventRegel", null);
			EventRegel eventRegel = gson.fromJson(json, EventRegel.class);
			
			EventAktion eveAkt =  eventRegel.getEventAktion().get(position);
			eve_eveakt_akt.setText(eveAkt.getEveAktiZie()+":"+eveAkt.getEveAktiZieID());
			eve_eveakt_bez.setText(eveAkt.getEveAktiBez());
			eve_eveakt_funkt.setText(eveAkt.getEveAktiZiePar());
			eve_eveakt_wert.setText(eveAkt.getEveAktiZieWer());
			
			
//			String data = spinnerEveAktion.getItemAtPosition(position).toString();
//	        Toast.makeText(EventRegelActivity.this, data, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
