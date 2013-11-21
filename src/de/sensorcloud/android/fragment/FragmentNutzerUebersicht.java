package de.sensorcloud.android.fragment;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.Adresse;
import de.sensorcloud.android.entitaet.NutzerEmail;
import de.sensorcloud.android.entitaet.NutzerEmailList;
import de.sensorcloud.android.entitaet.NutzerSicherheit;
import de.sensorcloud.android.entitaet.NutzerSicherheitList;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.NutzerTelefon;
import de.sensorcloud.android.entitaet.NutzerTelefonList;

public class FragmentNutzerUebersicht extends Fragment {
	
	public static String TAG = "NutzerUebersicht";
	
	private Spinner spinnerTelefon, spinnerEmail, spinnerSicherheit, spinnerAdresse, spinnerStammdaten;

	private NutzerTelefonList telefonListe;
	private NutzerEmailList eMailListe;
	private NutzerSicherheitList sicherheitListe;
	private Adresse adresseObj;
	
	NutzerStammdaten stammdatenObj = null;
	

	private List<String> stammdaten;
	private List<String> telefon;
	private List<String> email;
	private List<String> sicherheit;
	private List<String> adresse;

	public FragmentNutzerUebersicht() {
		
		stammdaten.add(stammdatenObj.getNutStaVor()+", " + stammdatenObj.getNutStaNam());
		
		AsyncHttpClient client = new AsyncHttpClient();
		
		
		client.get("http://10.0.2.2:8080/SensorCloudRest/crud/NutzerEmail/NutStaID/"+stammdatenObj.getNutStaID(), new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        Gson gson = new Gson();
		        
		        eMailListe = gson.fromJson(response, NutzerEmailList.class);
		        for (NutzerEmail mail : eMailListe.getList()) {
		        	email.add(mail.getNutEmaAdr());
				}
		    }
		    
		});
		
		client.get("http://10.0.2.2:8080/SensorCloudRest/crud/NutzerTelefon/NutStaID/"+stammdatenObj.getNutStaID(), new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        Gson gson = new Gson();
		        
		        telefonListe = gson.fromJson(response, NutzerTelefonList.class);
		        for (NutzerTelefon tel : telefonListe.getList()) {
					telefon.add(tel.getNutTelNum());
				}
		    }
		    
		});
		
		client.get("http://10.0.2.2:8080/SensorCloudRest/crud/NutzerSicherheit/NutStaID/"+stammdatenObj.getNutStaID(), new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        Gson gson = new Gson();
		        
		        sicherheitListe = gson.fromJson(response, NutzerSicherheitList.class);
		        for (NutzerSicherheit sich : sicherheitListe.getList()) {
					sicherheit.add(sich.getNutSicPas());
				}
		    }
		    
		});
		
		client.get("http://10.0.2.2:8080/SensorCloudRest/crud/Adresse/AdrID/"+stammdatenObj.getNutStaAdrID(), new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("Test", response);
		        Gson gson = new Gson();
		        
		        adresseObj = gson.fromJson(response, Adresse.class);
		        adresse.add(adresseObj.getAdrStr());
		    }
		    
		});
		
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	    View v = inflater.inflate(R.layout.nutzeruebersicht_fragment, container, false);
 
	    spinnerTelefon = (Spinner) v.findViewById(R.id.spinnerTelefon);
	    ArrayAdapter<String> Teladapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, telefon);
	    Teladapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    spinnerTelefon.setAdapter(Teladapter);
	    
	    spinnerEmail = (Spinner) v.findViewById(R.id.spinnerEmail);
	    ArrayAdapter<String> Mailadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, email);
	    Mailadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    spinnerEmail.setAdapter(Mailadapter);
	    
	    spinnerSicherheit = (Spinner) v.findViewById(R.id.spinnerSicherheit);
	    ArrayAdapter<String> Sichadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, sicherheit);
	    Sichadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    spinnerSicherheit.setAdapter(Sichadapter);
	    
	    spinnerAdresse = (Spinner) v.findViewById(R.id.spinnerAdresse);
	    ArrayAdapter<String> Adradapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, adresse);
	    Adradapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    spinnerAdresse.setAdapter(Adradapter);
	    
	    spinnerStammdaten = (Spinner) v.findViewById(R.id.spinnerStammdaten);
	    ArrayAdapter<String> Stmadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, stammdaten);
	    Stmadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    spinnerStammdaten.setAdapter(Stmadapter);
	    return v;
	}


	 
	  public void addListenerOnSpinnerItemSelection() {
		  spinnerTelefon.setOnItemSelectedListener((OnItemSelectedListener) new CustomOnItemSelectedListener());
		  spinnerEmail.setOnItemSelectedListener((OnItemSelectedListener) new CustomOnItemSelectedListener());
		  spinnerAdresse.setOnItemSelectedListener((OnItemSelectedListener) new CustomOnItemSelectedListener());
		  spinnerStammdaten.setOnItemSelectedListener((OnItemSelectedListener) new CustomOnItemSelectedListener());
		  spinnerSicherheit.setOnItemSelectedListener((OnItemSelectedListener) new CustomOnItemSelectedListener());
	  }
	  
	  public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		  
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			Toast.makeText(parent.getContext(), 
				"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
		  }
		 
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }

	
	}
}
