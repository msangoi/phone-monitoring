package makeit.phonemonitoring;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class StateListener extends PhoneStateListener {

	private SignalStrength signalStrength;
	
	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		this.signalStrength = signalStrength;
	}

	
	public Integer getRSSI() {
		if(signalStrength == null) {
			return null;
		}
		return signalStrength.getGsmSignalStrength();
	}
	
}
