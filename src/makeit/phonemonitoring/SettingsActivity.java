package makeit.phonemonitoring;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

@SuppressLint("NewApi")
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_REFRESH_PERIOD = "pref_refresh_period";
    public static final String KEY_PREF_SERVER_HOST = "pref_server_host";

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Display the fragment as the main content.
            getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        } else {
            this.addPreferencesFromResource(R.layout.preferences);
        }

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // TODO
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.layout.preferences);
        }
    }

}
