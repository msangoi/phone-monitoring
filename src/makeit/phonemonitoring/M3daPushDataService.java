package makeit.phonemonitoring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import m3da.client.M3daClient;
import m3da.client.M3daServerException;
import m3da.client.M3daTcpClient;
import m3da.codec.dto.M3daBodyMessage;
import m3da.codec.dto.M3daMessage;

public class M3daPushDataService {

    public void pushData(String deviceId, LastData data, String serverHost) throws IOException, M3daServerException {

        // push data to the server
        M3daClient client = new M3daTcpClient(serverHost, 44900, deviceId);

        client.connect();

        Map<Object, Object> body = new HashMap<Object, Object>();

        for (Entry<String, Object> d : this.getDataMap(data).entrySet()) {
            body.put(d.getKey(), d.getValue());
        }

        M3daBodyMessage message = new M3daMessage("@sys", System.currentTimeMillis(), body);

        // we don't care about the response
        client.sendEnvelope(new M3daBodyMessage[] { message });

        client.close();
    }

    private Map<String, Object> getDataMap(LastData data) {
        Map<String, Object> dataMap = new HashMap<String, Object>();

        if (data.getRssi() != null) {
            dataMap.put("system.cellular.link.rssi", data.getRssi());
        }
        if (data.getNetworkType() != null) {
            dataMap.put("system.cellular.link.service", data.getNetworkType());
        }
        if (data.getOperator() != null) {
            dataMap.put("system.cellular.gsm.link.operator", data.getOperator());
        }
        if (data.getLatitude() != null && data.getLongitude() != null) {
            dataMap.put("system.gps.latitude", data.getLatitude());
            dataMap.put("system.gps.longitude", data.getLongitude());
        }
        if (data.getBatteryLevel() != null) {
            dataMap.put("system.android.phone.batterylevel", data.getBatteryLevel());
        }
        if (data.getRoaming() != null) {
            dataMap.put("system.android.phone.roaming", data.getRoaming());
        }
        return dataMap;
    }

}
