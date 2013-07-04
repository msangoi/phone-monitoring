package makeit.phonemonitoring;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * The activity for the monitoring application
 */
public class MonitoringActivity extends Activity {

    private String uniqueId;
    private LastDataReceiver lastDataReceiver = new LastDataReceiver(this);

    private AlarmManager alarmManager;
    private Intent pushDataIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // phone identifier
        uniqueId = getUniqueId();
        ((TextView) findViewById(R.id.uniqueIdText)).setText(uniqueId);

        // server host from preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String serverHost = sharedPrefs.getString(SettingsActivity.KEY_PREF_SERVER_HOST, "");

        pushDataIntent = new Intent(this, LastDataService.class);
        pushDataIntent.putExtra(LastDataService.DEVICE_ID_EXTRA, uniqueId);
        pushDataIntent.putExtra(LastDataService.SERVER_HOST_EXTRA, serverHost);

        System.out.println("server host : " + serverHost);

        // check if the service is already running
        ((ToggleButton) findViewById(R.id.startStopButton)).setChecked(isServiceRunning());

        // register a new receiver to receive the last data from the background service
        IntentFilter intentFilter = new IntentFilter(LastDataIntent.REFRESH_LAST_DATA);
        LocalBroadcastManager.getInstance(this).registerReceiver(lastDataReceiver, intentFilter);
    }

    @SuppressLint("NewApi")
    private String getUniqueId() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            return Build.SERIAL;
        } else {
            return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        }
    }

    private boolean isServiceRunning() {
        PendingIntent pending = PendingIntent.getService(this, 0, pushDataIntent, PendingIntent.FLAG_NO_CREATE);

        return pending != null;
    }

    /**
     * onClick method of start/stop button
     */
    public void startStopService(View view) {

        if (((ToggleButton) findViewById(R.id.startStopButton)).isChecked()) {
            // start
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            int period = Integer.valueOf(sharedPrefs.getString(SettingsActivity.KEY_PREF_REFRESH_PERIOD, "10"));

            PendingIntent pending = PendingIntent
                    .getService(this, 0, pushDataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC, 0, period * 60 * 1000, pending);
        } else {
            // stop
            PendingIntent pending = PendingIntent.getService(this, 0, pushDataIntent, PendingIntent.FLAG_NO_CREATE);
            if (pending != null) {
                System.err.println("cancelling intent");
                alarmManager.cancel(pending);
                pending.cancel();
            }
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case R.id.action_settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            break;
        }

        return true;
    }

}
