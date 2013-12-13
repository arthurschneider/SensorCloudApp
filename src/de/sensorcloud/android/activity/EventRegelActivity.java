package de.sensorcloud.android.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
	
	Button showDialog;
	Dialog custom;
	
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
	EventRegel eventRegel;
	int senEvePosition, eveAktPosition, eventPosition;
	
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
		
		showDialog = (Button)findViewById(R.id.eve_dialog_show);
		
		getDatensatzEventBez();
	}
	
	public void getDatensatzEventBez(){
		eventRegel = null;
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(EventRegelActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Event/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		    	Gson gson = new Gson();
		    	eventList = gson.fromJson(response, EventList.class);
		    	setDataToSpinner();
		    }
		});
	}
	
	public void setDataToSpinner() {
		List<String> list = new ArrayList<String>();
		list.clear();

		for (Event event : eventList.getList()) {
        	list.add(event.getEveBez());
		}
		
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(EventRegelActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.notifyDataSetChanged();
		spinnerEventBez.postInvalidate();
		spinnerEventBez.setAdapter(dAdapter);
		
		spinnerEventBez.setOnItemSelectedListener(new AuswahlEventListener());
	}
	
	
	public void getEventRegel(int position){
		AsyncHttpClient client = new AsyncHttpClient();
		Event event = eventList.getList().get(position);
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Event/EveID/"+event.getEveID(), new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		    	Gson gson = new Gson();
		    	eventRegel = gson.fromJson(response, EventRegel.class);
				setDataToSensorEventSpinner();
				setDataToEventAktionSpinner();
		    } 
		});
	}
	
	public void setDataToSensorEventSpinner(){
		List<String> list2 = new ArrayList<String>();
		list2.clear();
		
		for (SensorEvent senEve : eventRegel.getSensorEvent()) {
        	list2.add(senEve.getSenEveQueID());
		}
		ArrayAdapter<String> senAdapter = new ArrayAdapter<String>(EventRegelActivity.this, android.R.layout.simple_spinner_item, list2);
		senAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		senAdapter.notifyDataSetChanged();
		spinnerSensorEvent.postInvalidate();
		spinnerSensorEvent.setAdapter(senAdapter);
		
		spinnerSensorEvent.setOnItemSelectedListener(new SensorEventListener());
	}
	
	
	public void setDataToEventAktionSpinner(){
		List<String> list3 = new ArrayList<String>();
		list3.clear();
		for (EventAktion eveAkt : eventRegel.getEventAktion()) {
        	list3.add(eveAkt.getEveAktiZieID());
		}
		
		ArrayAdapter<String> aktAdapter = new ArrayAdapter<String>(EventRegelActivity.this, android.R.layout.simple_spinner_item, list3);
		aktAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aktAdapter.notifyDataSetChanged();
		spinnerEveAktion.postInvalidate();
		spinnerEveAktion.setAdapter(aktAdapter);
		spinnerEveAktion.setOnItemSelectedListener(new EventAktionListener());
	}
	
	public class OnShowDialogListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			custom = new Dialog(EventRegelActivity.this);
			custom.setContentView(R.layout.event_dialog);
			
			EditText nachricht = (EditText)custom.findViewById(R.id.evedia_nachricht);
			EditText art = (EditText)custom.findViewById(R.id.evedia_art);
			EditText nachrichtWeg = (EditText)custom.findViewById(R.id.evedia_weg);
			EditText timestmp = (EditText)custom.findViewById(R.id.evedia_time);
			Button canbtn = (Button)custom.findViewById(R.id.dialog_close);
			
			nachricht.setText(eventRegel.getEvent().getEveNac());
			art.setText(eventRegel.getEvent().getEveArt());
			nachrichtWeg.setText(eventRegel.getEventBen().getEveBenWeg());
			timestmp.setText(eventRegel.getEvent().getEveTimSta());
			
			canbtn.setOnClickListener(new View.OnClickListener() {
				
                @Override
                public void onClick(View view) {
                    custom.dismiss();
                }
            });
            custom.show();
		}
		
	}

	public class AuswahlEventListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			showDialog.setOnClickListener(new OnShowDialogListener());
			eventPosition = position;
			getEventRegel(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	public class SensorEventListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			SensorEvent senEve =  eventRegel.getSensorEvent().get(position);
			eve_seneve_bez.setText(senEve.getSenEveQue()+":"+senEve.getSenEveQueID());
			eve_seneve_parameter.setText(senEve.getSenEvePhyNam());
			eve_seneve_operator.setText(senEve.getSenEveVop());
			eve_seneve_schwwert.setText(senEve.getSenEveWer());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public class EventAktionListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			EventAktion eveAkt =  eventRegel.getEventAktion().get(position);
			eve_eveakt_akt.setText(eveAkt.getEveAktiZie()+":"+eveAkt.getEveAktiZieID());
			eve_eveakt_bez.setText(eveAkt.getEveAktiBez());
			eve_eveakt_funkt.setText(eveAkt.getEveAktiZiePar());
			eve_eveakt_wert.setText(eveAkt.getEveAktiZieWer());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
	
	public void saveSenEveContent(View view) {
		SensorEvent senEve =  eventRegel.getSensorEvent().get(senEvePosition);
		
		senEve.setSenEveVop(eve_seneve_operator.getText().toString());
		senEve.setSenEveWer(eve_seneve_schwwert.getText().toString());
		
		eventRegel.getSensorEvent().set(senEvePosition, senEve);
	}
	
	
	public void saveEveAktContent(View view) {
		EventAktion eveAkt =  eventRegel.getEventAktion().get(eveAktPosition);
		
		eveAkt.setEveAktiZiePar(eve_eveakt_funkt.getText().toString());
		eveAkt.setEveAktiZieWer(eve_eveakt_wert.getText().toString());
		
		eventRegel.getEventAktion().set(eveAktPosition, eveAkt);
	}
	
	
	public void updateEventRegel(View view){
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(eventRegel);
		
		StringEntity se = null;
	
		try {
		    se = new StringEntity(jsonElement.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e("Fehler", "Json-String konnte nicht verarbeitet werden!");
		}		
		
		AsyncHttpClient client = new AsyncHttpClient();
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		client.post(null,  Helper.BASE_URL+"/SensorCloudRest/crud/Event", se, "application/json", new AsyncHttpResponseHandler() {
			 @Override
			    public void onSuccess(String response) {
			        Toast.makeText(EventRegelActivity.this, response, Toast.LENGTH_LONG).show();
			        getDatensatzEventBez();
		 }
		});
	}
}
