package de.sensorcloud.android.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.sensorcloud.android.R;

public class AuswahlseiteActivity extends ListActivity {

	static final String[] ACTIVITIES = new String[] { "Adressdaten anzeigen/bearbeiten", "Profildaten anzeigen/bearbeiten", "Telefondaten anzeigen/bearbeiten", "E-Maildaten anzeigen/bearbeiten", "Sicherheitsdaten anzeigen/bearbeiten", "Event anzeigen/bearbeiten",
		"Gruppe erstellen", "Mitglied in Gruppe einladen", "Gruppen anzeigen", "Aktor/Verbund anzeigen ", "Sensor/Verbund anzeigen",
		"Chart anzeigen", "Aktor mit Aktorverbund verbinden", "Sensor mit Sensorverbund verbinden",
		"ServiceLinie (Sensor) anlegen","ServiceLinie (Aktor) anlegen", "ServiceLinie anzeigen"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_list, ACTIVITIES));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent;
			   switch (position) {
			case 0:
				intent  = new Intent(AuswahlseiteActivity.this, AdresseActivity.class);
				startActivity(intent);
				break;
			case 1:
				intent  = new Intent(AuswahlseiteActivity.this, StammdatenActivity.class);
				startActivity(intent);
				break;
			case 2:
				intent  = new Intent(AuswahlseiteActivity.this, TelefonActivity.class);
				startActivity(intent);
				break;
			case 3:
				intent  = new Intent(AuswahlseiteActivity.this, EmailActivity.class);
				startActivity(intent);
				break;
			case 4:
				intent  = new Intent(AuswahlseiteActivity.this, SicherheitActivity.class);
				startActivity(intent);
				break;
			case 5:
				intent  = new Intent(AuswahlseiteActivity.this, EventRegelActivity.class);
				startActivity(intent);
				break;
			case 6:
				intent  = new Intent(AuswahlseiteActivity.this, GruppeErstellenActivity.class);
				startActivity(intent);
				break;
			case 7:
				intent  = new Intent(AuswahlseiteActivity.this, GruppenMitEinladenActivity.class);
				startActivity(intent);
				break;
			case 8:
				intent  = new Intent(AuswahlseiteActivity.this, GruppenAnzeigenActivity.class);
				startActivity(intent);
				break;
			case 9:
				intent  = new Intent(AuswahlseiteActivity.this, AktorVerbundActivity.class);
				startActivity(intent);
				break;
			case 10:
				intent  = new Intent(AuswahlseiteActivity.this, SensorVerbundActivity.class);
				startActivity(intent);
				break;
			case 11:
				intent  = new Intent(AuswahlseiteActivity.this, ChartActivity.class);
				startActivity(intent);
				break;
			case 12:
				intent  = new Intent(AuswahlseiteActivity.this, AktorVerbundAnlegenActivity.class);
				startActivity(intent);
				break;
			case 13:
				intent  = new Intent(AuswahlseiteActivity.this, SensorVerbundAnlegenActivity.class);
				startActivity(intent);
				break;
			case 14:
				intent  = new Intent(AuswahlseiteActivity.this, ServiceLinieSenAnlegenActivity.class);
				startActivity(intent);
				break;
			case 15:
				intent  = new Intent(AuswahlseiteActivity.this, ServiceLinienAktAnlegenActivity.class);
				startActivity(intent);
				break;
			case 16:
				intent  = new Intent(AuswahlseiteActivity.this, ServiceLinienAnzeigenActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
			}
		});
	}
}