package makeit.phonemonitoring;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PushDataService extends IntentService {

	public static final String SERVICE_NAME = "PushDataService";

	public static final String BROADCAST_DATA_ACTION = "makeit.phonemonitoring.lastdata.BROADCAST";
   
	private static final String LAST_DATA_PREFIX = "makeit.phonemonitoring.lastdata.";
    public static final String RSSI_KEY = LAST_DATA_PREFIX + "rssi";
	
	boolean stopRequested = false;
	
	private StateListener stateListener = new StateListener();
	
	public PushDataService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("starting service");
		
		((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(stateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		
		while(!stopRequested) {
			
			try {
				Bundle lastData = new Bundle();
				
				Integer rssi = stateListener.getRSSI();
				
				System.out.println("RSSI : " + rssi);
				if(rssi != null) {
					lastData.putInt(RSSI_KEY, rssi.intValue());	
				}
				
			    Intent localIntent = new Intent(BROADCAST_DATA_ACTION).putExtras(lastData);
			    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

			    Thread.sleep(10000);
			    
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("stopping service");
	}
	
	@Override
	public void onDestroy() {
		stopRequested = true;

		((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(stateListener, PhoneStateListener.LISTEN_NONE);
	}
	
}
