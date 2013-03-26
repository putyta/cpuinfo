package ru.hobud.cpuinfo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class Preferences extends PreferenceFragment implements OnSharedPreferenceChangeListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Load the preferences from an XML resource
    addPreferencesFromResource(R.xml.preferences);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(getString(R.string.start_on_boot_pref_name))) {
      // Set summary to be the user-description for the selected value
      Preference exercisesPref = findPreference(key);
      String summery = Boolean.toString(sharedPreferences.getBoolean(key, false));
      exercisesPref.setSummary(summery);
      Toast.makeText(this.getActivity(), String.format("Start on boot: %s", summery), Toast.LENGTH_SHORT).show();
    }

  }

  @Override
  public void onResume() {
    super.onResume();
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

  }

  @Override
  public void onPause() {
    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    super.onPause();
  }
}
