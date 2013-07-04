package makeit.phonemonitoring;

import android.content.Intent;

public class LastDataIntent extends Intent {

    public static final String REFRESH_LAST_DATA = "makeit.phonemonitoring.lastdata.refresh";

    // keys used for broadcasting last data events
    private static final String LAST_DATA_PREFIX = "makeit.phonemonitoring.lastdata.";
    private static final String RSSI_KEY = LAST_DATA_PREFIX + "rssi";
    private static final String BATTERY_KEY = LAST_DATA_PREFIX + "battery";
    private static final String OPERATOR_KEY = LAST_DATA_PREFIX + "operator";
    private static final String NETWORK_TYPE_KEY = LAST_DATA_PREFIX + "networktype";
    private static final String ROAMING_KEY = LAST_DATA_PREFIX + "roaming";
    private static final String LATITUDE_KEY = LAST_DATA_PREFIX + "latitude";
    private static final String LONGITUDE_KEY = LAST_DATA_PREFIX + "longitude";

    private static final String ERROR_KEY = LAST_DATA_PREFIX + "error";

    public LastDataIntent() {
        super(REFRESH_LAST_DATA);
    }

    public void setLastData(LastData data) {

        if (data.getRssi() != null) {
            this.putExtra(RSSI_KEY, data.getRssi().intValue());
        }
        if (data.getOperator() != null) {
            this.putExtra(OPERATOR_KEY, data.getOperator());
        }
        if (data.getNetworkType() != null) {
            this.putExtra(NETWORK_TYPE_KEY, data.getNetworkType());
        }
        if (data.getRoaming() != null) {
            this.putExtra(ROAMING_KEY, data.getRoaming().booleanValue());
        }
        if (data.getBatteryLevel() != null) {
            this.putExtra(BATTERY_KEY, data.getBatteryLevel());
        }
        if (data.getLatitude() != null) {
            this.putExtra(LATITUDE_KEY, data.getLatitude().doubleValue());
        }
        if (data.getLongitude() != null) {
            this.putExtra(LONGITUDE_KEY, data.getLongitude().doubleValue());
        }
    }

    public LastData getLastData() {
        LastData data = new LastData();

        data.setRssi((Integer) this.getExtras().get(RSSI_KEY));
        data.setOperator((String) this.getExtras().get(OPERATOR_KEY));
        data.setNetworkType((String) this.getExtras().get(NETWORK_TYPE_KEY));
        data.setRoaming((Boolean) this.getExtras().get(ROAMING_KEY));
        data.setBatteryLevel((Float) this.getExtras().get(BATTERY_KEY));
        data.setLatitude((Double) this.getExtras().get(LATITUDE_KEY));
        data.setLongitude((Double) this.getExtras().get(LONGITUDE_KEY));

        return data;
    }

    public void setError(String error) {
        if (error != null && error != "") {
            this.putExtra(ERROR_KEY, error);
        }
    }

    public String getError() {
        return this.getExtras().getString(ERROR_KEY);
    }

}
