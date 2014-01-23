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

public class HauptmenueActivity extends ListActivity {

	static final String[] ACTIVITIES = new String[] { "Adressdaten anzeigen/bearbeiten", "Profildaten anzeigen/bearbeiten", "Telefondaten anzeigen/bearbeiten", "E-Maildaten anzeigen/bearbeiten", "Sicherheitsdaten anzeigen/bearbeiten", "Event anzeigen/bearbeiten",
		"Mitglied in Gruppe einladen", "Gruppen anzeigen", "Aktor/Verbund anzeigen ", "Sensor/Verbund anzeigen",
		"Diagramm erstellen", "Aktor zum Verbund(Aktor) einfügen", "Sensor zum Verbund(Sensor) einfügen",
		"ServiceLinie (Sensor) anlegen","ServiceLinie (Aktor) anlegen", "ServiceLinie anzeigen"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.hauptmenue_activity, ACTIVITIES));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent;
			   switch (position) {
			case 0:
				intent  = new Intent(HauptmenueActivity.this, AdresseActivity.class);
				startActivity(intent);
				break;
			case 1:
				intent  = new Intent(HauptmenueActivity.this, StammdatenActivity.class);
				startActivity(intent);
				break;
			case 2:
				intent  = new Intent(HauptmenueActivity.this, TelefonActivity.class);
				startActivity(intent);
				break;
			case 3:
				intent  = new Intent(HauptmenueActivity.this, EmailActivity.class);
				startActivity(intent);
				break;
			case 4:
				intent  = new Intent(HauptmenueActivity.this, SicherheitActivity.class);
				startActivity(intent);
				break;
			case 5:
				intent  = new Intent(HauptmenueActivity.this, EventRegelActivity.class);
				startActivity(intent);
				break;
			case 6:
				intent  = new Intent(HauptmenueActivity.this, GruppenMitEinladenActivity.class);
				startActivity(intent);
				break;
			case 7:
				intent  = new Intent(HauptmenueActivity.this, GruppenAnzeigenActivity.class);
				startActivity(intent);
				break;
			case 8:
				intent  = new Intent(HauptmenueActivity.this, AktorVerbundActivity.class);
				startActivity(intent);
				break;
			case 9:
				intent  = new Intent(HauptmenueActivity.this, SensorVerbundActivity.class);
				startActivity(intent);
				break;
			case 10:
				intent  = new Intent(HauptmenueActivity.this, ChartActivity.class);
				startActivity(intent);
				break;
			case 11:
				intent  = new Intent(HauptmenueActivity.this, AktorVerbundAnlegenActivity.class);
				startActivity(intent);
				break;
			case 12:
				intent  = new Intent(HauptmenueActivity.this, SensorVerbundAnlegenActivity.class);
				startActivity(intent);
				break;
			case 13:
				intent  = new Intent(HauptmenueActivity.this, ServiceLinieSenAnlegenActivity.class);
				startActivity(intent);
				break;
			case 14:
				intent  = new Intent(HauptmenueActivity.this, ServiceLinienAktAnlegenActivity.class);
				startActivity(intent);
				break;
			case 15:
				intent  = new Intent(HauptmenueActivity.this, ServiceLinienAnzeigenActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
			}
		});
	}
}