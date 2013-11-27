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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auswahlseite_activity);
		addItemsOnSpinnerAuswahl();
		addListenerOnSpinnerItemSelection();
	}

	
	public void addItemsOnSpinnerAuswahl() {
		 
		spinnerNutStmmdtn = (Spinner) findViewById(R.id.spinnerNutStmmdtn);
		
		List<String> list = new ArrayList<String>();
		list.add("--------");
		list.add("Adresse");
		list.add("Stammdaten");
		list.add("Telefon");
		list.add("Email");
		list.add("Sicherheit");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerNutStmmdtn.setAdapter(dataAdapter);
		spinnerNutStmmdtn.setSelection(0);
	}
	
	public void addListenerOnSpinnerItemSelection() {
		
		spinnerNutStmmdtn = (Spinner) findViewById(R.id.spinnerNutStmmdtn);
		spinnerNutStmmdtn.setOnItemSelectedListener(new AuswahlNutStmmdtnListener());
	}	
	
	public class AuswahlNutStmmdtnListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			
			if (position == 1){
				Intent intent  = new Intent(getApplicationContext(), AdresseActivity.class);
				startActivity(intent);
			}
			if (position == 2){
				Intent intent  = new Intent(getApplicationContext(), StammdatenActivity.class);
				startActivity(intent);
			}
			if (position == 3){
				Intent intent  = new Intent(getApplicationContext(), TelefonActivity.class);
				startActivity(intent);
			}
			if (position == 4){
				Intent intent  = new Intent(getApplicationContext(), EmailActivity.class);
				startActivity(intent);
			}
			if (position == 5){
				Intent intent  = new Intent(getApplicationContext(), SicherheitActivity.class);
				startActivity(intent);
			}
			
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	public void goToSenAktVerbund(View view){
		Intent intent  = new Intent(getApplicationContext(), SensorVerbundActivity.class);
		startActivity(intent);
	}
}