package ru.hobud.cpuinfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CpuInfoGrabber {
  private final String kernelMaxFilename = "/sys/devices/system/cpu/kernel_max";
  private final String statFilename = "/proc/stat";
  private final int kernel_max;
  private int cpuload[];
  private ArrayList<HashMap<String, Long>> cpuloadPrev;
  private ArrayList<HashMap<String, Long>> cpuloadCur;
  Pattern pattern = Pattern.compile("^cpu([0-9])\\s([0-9]+)\\s([0-9]+)\\s([0-9]+)\\s([0-9]+)\\s([0-9]+)\\s([0-9]+)\\s([0-9]+).*");

  
  public CpuInfoGrabber() {
    // let's find out how many cpu cores do we have
    int km;
    try {
      km = Integer.parseInt(new BufferedReader(new InputStreamReader(new FileInputStream(kernelMaxFilename)), 10).readLine());
    } catch (Exception e) {
      km = 0;
    }
    kernel_max = km;
    cpuload = new int[kernel_max + 1];
    for(int i = 0; i < cpuload.length; i++) {
      cpuload[i] = -1;
    }
    cpuloadCur = new ArrayList<HashMap<String,Long>>(kernel_max + 1);
    cpuloadPrev = new ArrayList<HashMap<String,Long>>(kernel_max + 1);
    for(int i = 0; i < kernel_max+1; i++) {
      cpuloadCur.add(null);
      cpuloadPrev.add(null);
    }
    resetPrev();
  }

  @SuppressWarnings("unchecked")
  private void resetPrev() {
    HashMap<String, Long> cpuInfo = new HashMap<String, Long>();
    cpuInfo.put("user", 0L);
    cpuInfo.put("nice", 0L);
    cpuInfo.put("system", 0L);
    cpuInfo.put("idle", 0L);
    cpuInfo.put("iowait", 0L);
    cpuInfo.put("irq", 0L);
    cpuInfo.put("softirq", 0L);
    for(int i = 0; i < cpuloadPrev.size(); i++) {
      cpuloadPrev.set(i, (HashMap<String, Long>) cpuInfo.clone());
    }
  }
  
  
  @SuppressWarnings("unchecked")
  private void clearCur() {
    HashMap<String, Long> cpuInfo = new HashMap<String, Long>();
    cpuInfo.put("user", -1L);
    cpuInfo.put("nice", -1L);
    cpuInfo.put("system", -1L);
    cpuInfo.put("idle", -1L);
    cpuInfo.put("iowait", -1L);
    cpuInfo.put("irq", -1L);
    cpuInfo.put("softirq", -1L);
    for(int i = 0; i < cpuloadCur.size(); i++) {
      cpuloadCur.set(i, (HashMap<String, Long>) cpuInfo.clone());
    }
  }
  
  public String showCurCpuInfo() {
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < cpuloadCur.size(); i++) {
      HashMap<String, Long> cpuInfo = cpuloadCur.get(i);
      sb.append(String.format("\n%d) user: %d, system: %d, idel: %d (%d)", i, cpuInfo.get("user"), cpuInfo.get("system"), cpuInfo.get("idle"), cpuload[i]));
    }
    return sb.toString();
  }
  
  public void processCpuInfo() {
    for(int i = 0; i < cpuloadCur.size(); i++) {
      int load = -1;
      HashMap<String, Long> cpuInfoCur = cpuloadCur.get(i);
      HashMap<String, Long> cpuInfoPrev = cpuloadPrev.get(i);
      long user = cpuInfoCur.get("user");
      long user_prev = cpuInfoPrev.get("user");
      long nice = cpuInfoCur.get("nice");
      long nice_prev = cpuInfoPrev.get("nice");
      long system = cpuInfoCur.get("system");
      long system_prev = cpuInfoPrev.get("system");
      long idle = cpuInfoCur.get("idle");
      long idle_prev = cpuInfoPrev.get("idle");
      long iowait = cpuInfoCur.get("iowait");
      long iowait_prev = cpuInfoPrev.get("iowait");
      long irq = cpuInfoCur.get("irq");
      long irq_prev = cpuInfoPrev.get("irq");
      long softirq = cpuInfoCur.get("softirq");
      long softirq_prev = cpuInfoPrev.get("softirq");
      if(user<0 || nice <0 || system < 0|| idle < 0 || iowait < 0 || irq < 0|| softirq < 0) {
      } else {
        long sum = user + nice + system + idle + iowait + irq + softirq;
        long sum_prev = user_prev + nice_prev + system_prev + idle_prev + iowait_prev + irq_prev + softirq_prev;
        long all = sum - sum_prev;
        long dIdle = idle - idle_prev;
        if(all > 0) {
          load = (int) ((all - dIdle)*100/all);
        }
      }
      cpuload[i] = load;
    }
  }
  
  @SuppressWarnings("unchecked")
  public int[] grabCpuInfo() {
    BufferedReader statFile = null;
    try {
      clearCur();
      statFile = new BufferedReader(new FileReader((statFilename)), 4096);
      String line = statFile.readLine();
      while(line != null) {
        Matcher m = pattern.matcher(line);
        if(m.find()) {
          HashMap<String, Long> cpuInfo = new HashMap<String, Long>();
          int cpuNo = Integer.parseInt(m.group(1));
          cpuInfo.put("user", Long.parseLong(m.group(2)));
          cpuInfo.put("nice", Long.parseLong(m.group(3)));
          cpuInfo.put("system", Long.parseLong(m.group(4)));
          cpuInfo.put("idle", Long.parseLong(m.group(5)));
          cpuInfo.put("iowait", Long.parseLong(m.group(6)));
          cpuInfo.put("irq", Long.parseLong(m.group(7)));
          cpuInfo.put("softirq", Long.parseLong(m.group(8)));
          cpuloadCur.set(cpuNo, cpuInfo);
        }
        line = statFile.readLine();
      }
      processCpuInfo();
      for(int i = 0; i < cpuloadCur.size(); i++) {
        HashMap<String, Long> cpuInfo = cpuloadCur.get(i);
        cpuloadPrev.set(i, (HashMap<String, Long>) cpuInfo.clone());
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if(statFile != null)
        try {
          statFile.close();
        } catch (IOException e) {
        }
    }
    return cpuload;
  }
}
