package ru.hobud.cpuinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Autostarter extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    Toast.makeText(context, String.format("ACTION: %s", action == null?"NULL":action), Toast.LENGTH_LONG).show();
    boolean kickstarter = intent.getAction().equals(context.getString(R.string.starter_intent_name));
    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || kickstarter) {
      SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
      boolean startOnBoot = mySharedPreferences.getBoolean(context.getString(R.string.start_on_boot_pref_name), false);
      if (startOnBoot || kickstarter)
        context.startService(new Intent(context, CpuMonitorService.class));
    }
  }

}
