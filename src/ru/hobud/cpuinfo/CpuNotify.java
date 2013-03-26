package ru.hobud.cpuinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CpuNotify extends Activity {

//  boolean startOnBoot;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sendBroadcast(new Intent(getString(R.string.starter_intent_name)));
    getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
//    loadPreferences();
  }

//  private void loadPreferences(){
//    SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//    startOnBoot = mySharedPreferences.getBoolean(getString(R.string.start_on_boot_pref_name), false);
//    Toast.makeText(this, String.format("%start on boot", startOnBoot ? "S":"DO NOT s"), Toast.LENGTH_LONG).show();
//  }

}
