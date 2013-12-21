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
import android.util.Log;
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
import de.sensorcloud.android.entitaet.SensorProduktSemantik;
import de.sensorcloud.android.helpertools.Helper;

public class ChartActivity extends Activity implements OnItemSelectedListener {

	Button btnSelectDate;
	Spinner spinnerAuswahlSensor;
	Spinner spinnerAuswahlSenWer;
	 
	static final int DATE_DIALOG_ID = 0;
	public  int year,month,day;
	private int mYear, mMonth, mDay;
	
	int senPosition, senWerPosition;
	SensorProduktSemantik semantik;
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
		
		btnSelectDate.setText("Datum : "+mDay+"-"+mMonth+"-"+mYear);
		spinnerAuswahlSensor = (Spinner) findViewById(R.id.spinnerAuswahlSensor);
		spinnerAuswahlSenWer = (Spinner) findViewById(R.id.spinnerAuswahlSenWer);
		getDatensatzSensor();
	}

	
	public void getDatensatzSensor(){
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
				getDatensatzSensorSemantik();
				
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
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			senPosition = position;
			getDatensatzSensorSemantik();
			senID  = sensorList.getSensorList().get(position).getSenID();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	
	
	public void getDatensatzSensorSemantik(){
		AsyncHttpClient client = new AsyncHttpClient();
		
		client.get(Helper.BASE_URL+"/SensorCloudRest/crud/SensorProdukt/SenProID/"+sensorList.getSensorList().get(senPosition).getSenSenProID(), new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		    	Gson gson = new Gson();
		    	Log.i("Chart", response);
				semantik = gson.fromJson(response, SensorProduktSemantik.class);
				
				setDataToSenWerSpinner();
		    }
		    
		});
	}
	
	
	public void setDataToSenWerSpinner() {
		List<String> list2 = new ArrayList<String>();
		list2.clear();
		spinnerAuswahlSenWer.setAdapter(null);
		spinnerAuswahlSenWer.invalidate();
	
		for (int i = 0; i < semantik.getN(); i++) {
		    list2.add(semantik.getParvor().get(i).getPn());
		}

		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ChartActivity.this, android.R.layout.simple_spinner_item, list2);
		dAdapter.notifyDataSetChanged();
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAuswahlSenWer.setAdapter(dAdapter);
		spinnerAuswahlSenWer.setOnItemSelectedListener(new AuswahlSenWerListener());
	}
	
	
	
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
                    btnSelectDate.setText("Datum : "+day+"-"+month+"-"+year);
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
    	 Toast.makeText(getBaseContext(),"zeichne Chart",Toast.LENGTH_SHORT).show();
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
    	if (datenSatz.getParVor().getUfkt().equals("hex2dec($W)/100")) {
    		for(MesswertTime mTData : datenSatz.getMesswertTime()){
        		messWertSeries.add(new Date(Long.parseLong(mTData.getMesWerTimSta())), Long.parseLong(mTData.getMesWerWer(), 16)/100.0);
            }
		} else if (datenSatz.getParVor().getUfkt().equals("hex2dec($W)")) {
			for(MesswertTime mTData : datenSatz.getMesswertTime()){
        		messWertSeries.add(new Date(Long.parseLong(mTData.getMesWerTimSta())), Long.parseLong(mTData.getMesWerWer(), 16));
            }
		} else if (datenSatz.getParVor().getUfkt().equals("(bool)($W)")) {
			for(MesswertTime mTData : datenSatz.getMesswertTime()){
        		messWertSeries.add(new Date(Long.parseLong(mTData.getMesWerTimSta())), Integer.parseInt(mTData.getMesWerWer().substring(1, 2)));
            }
		}
    
    	
    	 XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
         dataset.addSeries(messWertSeries);
  
         XYSeriesRenderer renderer = new XYSeriesRenderer();
         renderer.setColor(Color.RED);
         renderer.setPointStyle(PointStyle.CIRCLE);
         renderer.setFillPoints(true);
         renderer.setLineWidth(0);
         renderer.setDisplayChartValues(false);
  
         XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
         multiRenderer.setChartTitle(senWer+" des Tages "+day+"/"+month+"/"+year);
         multiRenderer.setXTitle("Stunden");
         multiRenderer.setYTitle(senWer+" in " + datenSatz.getParVor().getEh());
         multiRenderer.setZoomButtonsVisible(true);
         multiRenderer.addSeriesRenderer(renderer);
  
         LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
  
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
                         selectedSeries + " um "  + strDate + " Uhr  = " + amount + " "+datenSatz.getParVor().getEh(),
                         Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         	Toast.makeText(getBaseContext(),"Chart gezeichnet",Toast.LENGTH_SHORT).show();
             // Adding the Line Chart to the LinearLayout
         	 chartContainer.removeAllViews();
             chartContainer.addView(mChart);
             
    }
    	  
    public class AuswahlSenWerListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			senWerPosition = position;
			senWer = (String)parent.getItemAtPosition(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}	  
  
}
