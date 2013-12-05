package de.sensorcloud.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import de.sensorcloud.android.R;

public class AuswahlseiteActivity extends Activity {

	Spinner spinnerNutStmmdtn;
	Spinner spinnerVerbund;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auswahlseite_activity);
		addItemsOnSpinnerAuswahl();
		addListenerOnSpinnerAuswahlItemSelection();
		addItemsOnSpinnerVerbund();
		addListenerOnSpinnerVerbundItemSelection();
	}

	
	public void addItemsOnSpinnerAuswahl() {
		 
		spinnerNutStmmdtn = (Spinner) findViewById(R.id.spinnerNutStmmdtn);
		spinnerVerbund = (Spinner) findViewById(R.id.spinnerVerbund);
		List<String> list = new ArrayList<String>();
		list.add("--------");
		list.add("Adresse");
		list.add("Stammdaten");
		list.add("Telefon");
		list.add("Email");
		list.add("Sicherheit");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AuswahlseiteActivity.this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerNutStmmdtn.setAdapter(dataAdapter);
		spinnerNutStmmdtn.setSelection(0);
	}
	
	public void addListenerOnSpinnerAuswahlItemSelection() {
		
		spinnerNutStmmdtn = (Spinner) findViewById(R.id.spinnerNutStmmdtn);
		spinnerNutStmmdtn.setOnItemSelectedListener(new AuswahlNutStmmdtnListener());
	}	
	
	
	public void addItemsOnSpinnerVerbund() {
		 
		spinnerVerbund = (Spinner) findViewById(R.id.spinnerVerbund);
		
		List<String> list = new ArrayList<String>();
		list.add("--------");
		list.add("Aktor/Verbund");
		list.add("Sensor/Verbund");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AuswahlseiteActivity.this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerVerbund.setAdapter(dataAdapter);
		spinnerVerbund.setSelection(0);
	}
	
	public void addListenerOnSpinnerVerbundItemSelection() {
		
		spinnerVerbund = (Spinner) findViewById(R.id.spinnerVerbund);
		spinnerVerbund.setOnItemSelectedListener(new AuswahlVerbundListener());
	}	
	
	public class AuswahlNutStmmdtnListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			
			if (position == 1){
				Intent intent  = new Intent(AuswahlseiteActivity.this, AdresseActivity.class);
				startActivity(intent);
			}
			if (position == 2){
				Intent intent  = new Intent(AuswahlseiteActivity.this, StammdatenActivity.class);
				startActivity(intent);
			}
			if (position == 3){
				Intent intent  = new Intent(AuswahlseiteActivity.this, TelefonActivity.class);
				startActivity(intent);
			}
			if (position == 4){
				Intent intent  = new Intent(AuswahlseiteActivity.this, EmailActivity.class);
				startActivity(intent);
			}
			if (position == 5){
				Intent intent  = new Intent(AuswahlseiteActivity.this, SicherheitActivity.class);
				startActivity(intent);
			}
			
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	public class AuswahlVerbundListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			
			if (position == 1){
				Intent intent  = new Intent(AuswahlseiteActivity.this, AktorVerbundActivity.class);
				startActivity(intent);
			}
			if (position == 2){
				Intent intent  = new Intent(AuswahlseiteActivity.this, SensorVerbundActivity.class);
				startActivity(intent);
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	public void goToEvent(View view){
		Intent intent  = new Intent(AuswahlseiteActivity.this, EventRegelActivity.class);
		startActivity(intent);
	}
	
	public void goToChart(View view){
		Intent intent  = new Intent(AuswahlseiteActivity.this, ChartActivity.class);
		startActivity(intent);
	}
}