package ru.hobud.cpuinfo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

public class CpuMonitorService extends Service {

  private BroadcastReceiver mReceiver;
  private Runnable runner;
  private long runnerDelay = 2000;
  private boolean stopFlag = false;
  private TestLogger logger;
  PowerManager powerManager;
  CpuInfoGrabber grabber;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private Runnable getRunner() {
    return new Runnable() {
      Handler h2 = new Handler();

      @Override
      public void run() {
        if (!stopFlag && powerManager.isScreenOn()) {
          grabCpuInfo();
          h2.postDelayed(this, runnerDelay);
        }
      }

    };
  }

  @Override
  public void onCreate() {
    super.onCreate();

    powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    logger = new TestLogger("cpuinfo.log", true);
    
    grabber = new CpuInfoGrabber();

    runner = getRunner();
    runner.run();

    IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
    intentFilter.addAction("android.intent.action.SCREEN_ON");
    mReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Toast.makeText(context, String.format("SCREEN: %s", action), Toast.LENGTH_SHORT).show();
        if (action == "android.intent.action.SCREEN_OFF") {
          // stop grabbing
          stopFlag = true;
          logger.log(String.format("RUNNER stoped now\n"));
        } else if (action == "android.intent.action.SCREEN_ON") {
          // start grabbing
          stopFlag = false;
          runner.run();
          logger.log(String.format("RUNNER started now\n"));
        }

      }
    };
    // registering our receiver
    this.registerReceiver(mReceiver, intentFilter);

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  private void grabCpuInfo() {
    grabber.grabCpuInfo();
    logger.log(String.format("%s\n", grabber.showCurCpuInfo()));
  }
}
