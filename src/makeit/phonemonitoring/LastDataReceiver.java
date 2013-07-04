package makeit.phonemonitoring;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * A component in charge of listening the "refresh data" events and updating the values in the current activity UI.
 */
public class LastDataReceiver extends BroadcastReceiver {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);

    private final String NO_VALUE = "-";

    private final MonitoringActivity activity;

    public LastDataReceiver(MonitoringActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // last data
        LastData data = ((LastDataIntent) intent).getLastData();

        findView(R.id.lastRefreshText).setText(dateFormat.format(new Date()));

        findView(R.id.rssiText).setText(data.getRssi() != null ? data.getRssi() + " dBm" : NO_VALUE);

        findView(R.id.batteryText).setText(
                data.getBatteryLevel() != null ? ((int) (data.getBatteryLevel() * 100) + "%") : NO_VALUE);

        findView(R.id.operatorText).setText(data.getOperator() != null ? data.getOperator() : NO_VALUE);

        findView(R.id.networdTypeText).setText(data.getNetworkType() != null ? data.getNetworkType() : NO_VALUE);

        findView(R.id.roamingText).setText(data.getRoaming() != null ? data.getRoaming().toString() : NO_VALUE);

        String location = NO_VALUE;
        if (data.getLatitude() != null && data.getLongitude() != null) {
            location = data.getLatitude() + "/" + data.getLongitude();
        }
        findView(R.id.locationText).setText(location);

        // error message
        String pushError = ((LastDataIntent) intent).getError();
        activity.findViewById(R.id.errorLayout).setVisibility(pushError != null ? View.VISIBLE : View.INVISIBLE);
        findView(R.id.errorMessage).setText(pushError != null ? pushError : "");
    }

    private TextView findView(int viewId) {
        return (TextView) activity.findViewById(viewId);
    }

}
