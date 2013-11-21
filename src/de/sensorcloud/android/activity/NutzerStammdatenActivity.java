package de.sensorcloud.android.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import de.sensorcloud.android.R;
import de.sensorcloud.android.fragment.FragmentNutzerUebersicht;

public class NutzerStammdatenActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nutzerstammdaten_activity);
		
		if (savedInstanceState == null) {
			FragmentNutzerUebersicht fragNutzU = new FragmentNutzerUebersicht();
			FragmentManager fragManager = getFragmentManager();
			FragmentTransaction transaction = fragManager.beginTransaction();
			transaction.add(R.id.frameLinks, fragNutzU, FragmentNutzerUebersicht.TAG);
			View v = this.findViewById(R.id.linearLayout_large_land);
			
			if (v != null) {
				
			}
		}
	}

}
