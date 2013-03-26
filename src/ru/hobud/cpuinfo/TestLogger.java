package ru.hobud.cpuinfo;

import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;


public class TestLogger {

  private final String logfileName;
  private final boolean timestamp;

  public TestLogger(String logfileName, boolean timestamp) {
    if (logfileName.startsWith(Environment.getExternalStorageDirectory().getPath()))
      this.logfileName = logfileName;
    else
      this.logfileName = Environment.getExternalStorageDirectory().getPath() + "/" + logfileName;
    this.timestamp = timestamp;
  }

  // проверяет доступно ли внешнее хранилище на чтение
  private boolean externalStorageAvailable() {
    boolean mExternalStorageAvailable = false;
    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED.equals(state)) {
      mExternalStorageAvailable = true;
    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      mExternalStorageAvailable = true;
    } else {
      mExternalStorageAvailable = false;
    }
    return mExternalStorageAvailable;
  }

  // проверяет доступно ли внешнее хранилище на запись
  private boolean externalStorageWritable() {
    if (!externalStorageAvailable())
      return false;
    boolean mExternalStorageWriteable = false;
    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      mExternalStorageWriteable = false;
    } else {
      mExternalStorageWriteable = true;
    }
    return mExternalStorageWriteable;
  }

  private RandomAccessFile openFileForWriteToEnd(String FileName) {
    RandomAccessFile random_file = null;

    if (externalStorageWritable()) {
      if (!FileName.isEmpty()) {
        try {
          random_file = new RandomAccessFile(FileName, "rw");
          random_file.seek(random_file.length());
        } catch (Exception e) {
          random_file = null;
        }
      }
    }
    return random_file;
  }

  public void log(String msg) {
    if(timestamp) {
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      msg = String.format("%s %s", df.format(new Date()), msg); 
    }
    try {
      RandomAccessFile file = null;
      file = openFileForWriteToEnd(logfileName);
      if (file != null) {
        file.write(msg.getBytes());
        file.close();
      }
    } catch (Exception e) {
    }
  }
}