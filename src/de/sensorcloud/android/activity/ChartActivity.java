package de.sensorcloud.android.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.sensorcloud.android.R;
import de.sensorcloud.android.entitaet.DatasetMitSemantik;
import de.sensorcloud.android.entitaet.MesswertTime;
import de.sensorcloud.android.entitaet.NutzerStammdaten;
import de.sensorcloud.android.entitaet.Sensor;
import de.sensorcloud.android.entitaet.SensorList;
import de.sensorcloud.android.helpertools.Helper;

public class ChartActivity extends Activity implements OnItemSelectedListener {

	Button btnSelectDate;
	Spinner spinnerAuswahlSensor;
	Spinner spinnerAuswahlSenWer;
	 
	static final int DATE_DIALOG_ID = 0;
	public  int year,month,day;
	private int mYear, mMonth, mDay;
	
	String senID, senWer;
	SensorList sensorList;
	DatasetMitSemantik datenSatz;
	private GraphicalView mChart;
	
	public ChartActivity(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_activity);
		btnSelectDate = (Button)findViewById(R.id.bn_datepicker);
		spinnerAuswahlSensor = (Spinner) findViewById(R.id.spinnerAuswahlSensor);
		spinnerAuswahlSenWer = (Spinner) findViewById(R.id.spinnerAuswahlSenWer);
		getDatensatz();
	}

	
	public void getDatensatz(){
		AsyncHttpClient client = new AsyncHttpClient();
		Gson gson = new Gson();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ChartActivity.this); 
		String json = mPrefs.getString("NutzerObj", null);
		String nutStaID = gson.fromJson(json, NutzerStammdaten.class).getNutStaID();
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Sensor/NutStaID/"+nutStaID, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		    	Gson gson = new Gson();
				sensorList = gson.fromJson(response, SensorList.class);
				setDataToSensorSpinner();
				setDataToSenWerSpinner();
		    }
		    
		});
	}
	
	public void setDataToSensorSpinner() {
		List<String> list = new ArrayList<String>();
		list.clear();
		spinnerAuswahlSensor.setAdapter(null);
		spinnerAuswahlSensor.invalidate();
		
		for (Sensor sen : sensorList.getSensorList()) {
        	list.add(sen.getSenID()+" : "+sen.getSenBez());
		}
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ChartActivity.this, android.R.layout.simple_spinner_item, list);
		dAdapter.notifyDataSetChanged();
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAuswahlSensor.setAdapter(dAdapter);
		spinnerAuswahlSensor.setOnItemSelectedListener(ChartActivity.this);
	}
	
	
	public void setDataToSenWerSpinner() {
		
//		Gson gson = new Gson();
		List<String> list2 = new ArrayList<String>();
		list2.clear();
		spinnerAuswahlSenWer.setAdapter(null);
		spinnerAuswahlSenWer.invalidate();
//		List emailListe = gson.fromJson(json, NutzerEmailList.class);
//	
//		for (Sensor sen : sensorList.getSensorList()) {
//        	list2.add(sen.getSenID()+" : "+sen.getSenBez());
//		}
		list2.add("Temperatur");
		list2.add("Luftfeuchte");
		list2.add("Luftqualitaet");
		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ChartActivity.this, android.R.layout.simple_spinner_item, list2);
		dAdapter.notifyDataSetChanged();
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAuswahlSenWer.setAdapter(dAdapter);
		spinnerAuswahlSenWer.setOnItemSelectedListener(new AuswahlSenWerListener());
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
	
			senID  = sensorList.getSensorList().get(position).getSenID();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	public void showDatePicker(View view){
		showDialog(DATE_DIALOG_ID);
	}
	
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
             new DatePickerDialog.OnDateSetListener() {
                 public void onDateSet(DatePicker view, int yearSelected,
                                       int monthOfYear, int dayOfMonth) {
                    year = yearSelected;
                    month = monthOfYear+1;
                    day = dayOfMonth;
                    btnSelectDate.setText("Date selected : "+day+"-"+month+"-"+year);
                 }
             };
             
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }
    
    
    
    
    
    
    public void startChart(View view){
    	  AsyncHttpClient client = new AsyncHttpClient();
    	  client.get(Helper.BASE_URL+"/SensorCloudRest/crud/Messwert/SenID/"+senID+"/MesWerNam/"+senWer+"/MesWerTimYea/"+year+"/MesWerTimMon/"+month+"/MesWerTimDay/"+day, new AsyncHttpResponseHandler() {
  		    @Override
  		    public void onSuccess(String response) {
  		    	Gson gson = new Gson();
  		    	datenSatz = gson.fromJson(response, DatasetMitSemantik.class);
  		    	drawChart();
  		    }
  		});
    }	  
    
    
    public void drawChart(){
    	TimeSeries messWertSeries = new TimeSeries("Messwerte");

    	for(MesswertTime mTData : datenSatz.getMesswertTime()){
    		messWertSeries.add(new Date(Long.parseLong(mTData.getMesWerTimSta())), Long.parseLong(mTData.getMesWerWer(), 16)/100.0);
        }

    	
    	 XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    	 
   
         dataset.addSeries(messWertSeries);
  
  
         XYSeriesRenderer visitsRenderer = new XYSeriesRenderer();
         visitsRenderer.setColor(Color.RED);
         visitsRenderer.setPointStyle(PointStyle.CIRCLE);
         visitsRenderer.setFillPoints(true);
         visitsRenderer.setLineWidth(2);
         visitsRenderer.setDisplayChartValues(false);
  
        
  
         // Creating a XYMultipleSeriesRenderer to customize the whole chart
         XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
  
         multiRenderer.setChartTitle(senWer+" des Tages "+day+"/"+month+"/"+year);
         multiRenderer.setXTitle("Stunden");
         multiRenderer.setYTitle(senWer);
         multiRenderer.setZoomButtonsVisible(true);
  
         // Adding visitsRenderer and viewsRenderer to multipleRenderer
         // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
         // should be same
         multiRenderer.addSeriesRenderer(visitsRenderer);
  
         // Getting a reference to LinearLayout of the MainActivity Layout
         LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
  
         // Creating a Time Chart
         mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"H:mm");

         multiRenderer.setClickEnabled(true);
         multiRenderer.setSelectableBuffer(10);
  
         // Setting a click event listener for the graph
         mChart.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SimpleDateFormat formatter = new SimpleDateFormat("H:mm:ss");
  
                 SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();
  
                 if (seriesSelection != null) {
                     int seriesIndex = seriesSelection.getSeriesIndex();
                     String selectedSeries="Fehler";
                     if(seriesIndex==0)
                         selectedSeries = "Temperatur";
                     else
                         selectedSeries = "falsch";
  
                     // Getting the clicked Date ( x value )
                     long clickedDateSeconds = (long) seriesSelection.getXValue();
                     Date clickedDate = new Date(clickedDateSeconds);
                     String strDate = formatter.format(clickedDate);
  
                     // Getting the y value
                     double amount = (double) seriesSelection.getValue();
  
                     // Displaying Toast Message
                     Toast.makeText(
                         getBaseContext(),
                         selectedSeries + " um "  + strDate + " Uhr  = " + amount + " °C",
                         Toast.LENGTH_SHORT).show();
                     }
                 }
             });
  
             // Adding the Line Chart to the LinearLayout
         	 chartContainer.removeAllViews();
             chartContainer.addView(mChart);
    }
    	  
    public class AuswahlSenWerListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		
			senWer = (String)parent.getItemAtPosition(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}	  
  
}
