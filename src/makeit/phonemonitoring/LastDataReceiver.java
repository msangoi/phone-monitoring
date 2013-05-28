package makeit.phonemonitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LastDataReceiver extends BroadcastReceiver {
	
	private PhoneMonitoring activity;
	
	public LastDataReceiver(PhoneMonitoring activity) {
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Integer rssi = (Integer) intent.getExtras().get(PushDataService.RSSI_KEY);

		System.err.println("new rssi value : " + rssi);
		if(rssi != null) {
			activity.displayRssi(rssi);
		}
		
	}

}
