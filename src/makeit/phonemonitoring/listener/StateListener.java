package makeit.phonemonitoring.listener;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class StateListener extends PhoneStateListener {

    private SignalStrength signalStrength;
    private ServiceState serviceState;
    private String networkType;

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        this.signalStrength = signalStrength;
    }

    @Override
    public void onDataConnectionStateChanged(int state, int networkType) {

        switch (networkType) {
        case TelephonyManager.NETWORK_TYPE_GPRS:
            this.networkType = "GPRS";
            break;
        case TelephonyManager.NETWORK_TYPE_EDGE:
            this.networkType = "EDGE";
            break;
        case TelephonyManager.NETWORK_TYPE_UMTS:
            this.networkType = "UMTS";
            break;
        case TelephonyManager.NETWORK_TYPE_HSDPA:
            this.networkType = "HSDPA";
            break;
        // to be continued
        default:
            System.err.println("unmapped network type : " + networkType);
            this.networkType = null;
        }
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        this.serviceState = serviceState;
    }

    @Override
    public void onCellLocationChanged(CellLocation location) {
        // System.out.println("cell location : " + location);
    }

    public Integer getRSSI() {
        Integer rssiInDb = null;

        if (signalStrength != null) {
            int rssi = signalStrength.getGsmSignalStrength();
            if (rssi >= 0 && rssi <= 31) {
                return rssi * 2 - 113;
            }
        }
        return rssiInDb;
    }

    public String getOperator() {
        return serviceState != null ? serviceState.getOperatorAlphaLong() : null;
    }

    public Boolean isRoaming() {
        return serviceState != null ? serviceState.getRoaming() : null;
    }

    public String getNetworkType() {
        return networkType;
    }

}
