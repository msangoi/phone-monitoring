package makeit.phonemonitoring;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PhoneMonitoring extends Activity {
	
	private String uniqueId;
    private LastDataReceiver lastDataReceiver = new LastDataReceiver(this);
    
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRENCH);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);
		
		uniqueId = getUniqueId();
		((TextView) findViewById(R.id.uniqueIdText)).setText(uniqueId);
			
		// check if the service is already running 
		RunningServiceInfo serviceInfo = getRunningServiceInfo();
		String startDate = "-";
		if(serviceInfo != null) {
			((ToggleButton) findViewById(R.id.startStopButton)).setChecked(true);
			long started = System.currentTimeMillis() - serviceInfo.activeSince;
			startDate = dateFormat.format(new Date(started));
		}
		else {
			((ToggleButton) findViewById(R.id.startStopButton)).setChecked(false);
		}
		((TextView) findViewById(R.id.startedSinceText)).setText(startDate);

		
		// register a new receiver to receive data from the background service
        IntentFilter intentFilter = new IntentFilter(PushDataService.BROADCAST_DATA_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(lastDataReceiver, intentFilter);
	}

	@SuppressLint("NewApi")
	private String getUniqueId() {
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			return Build.SERIAL;
		}
		else {
			return Secure.getString(getContentResolver(),
	                Secure.ANDROID_ID); 
		}
	}
	
	private RunningServiceInfo getRunningServiceInfo() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (PushDataService.class.getName().equals(service.service.getClassName())) {
	            return service;
	        }
	    }
	    return null;
	}
	
	/**
	 * onClick method of start/stop button 
	 */
	public void startStopService(View view) {
		
		if(((ToggleButton) findViewById(R.id.startStopButton)).isChecked()) {
			// start
			System.err.println("start");
			this.startService(new Intent(this, PushDataService.class));
		}
		else {
			// stop
			System.err.println("stop");
			boolean stopped = this.stopService(new Intent(this, PushDataService.class));
			System.err.println("stopped : " + stopped);
		}
	}
	
	public void displayRssi(Integer rssi) {
		((TextView) findViewById(R.id.lastRefreshText)).setText(dateFormat.format(new Date()));
		((TextView) findViewById(R.id.rssiText)).setText(rssi.toString());
	}
	
	@Override
	public void onStop() {
		super.onStop();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lastDataReceiver);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
