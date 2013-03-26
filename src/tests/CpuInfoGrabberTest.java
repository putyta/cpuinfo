package tests;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class CpuInfoGrabberTest extends TestCase {

  public void testCpuInfoGrabber() {
    fail("Not yet implemented"); // TODO
  }

  private String[] statFile = new String[] { 
      "cpu  495725 23007 357249 10464236 41593 67 7992 0 0 0\n",
      "cpu0 310414 14439 290192 2201344 22380 46 7876 0 0 0\n",
      "cpu1 62596 2824 23224 2752569 6486 8 11 0 0 0\n",
      "cpu2 44001 2273 13392 2783799 4322 4 1 0 0 0\n",
      "cpu3 78713 3470 30439 2726523 8405 9 103 0 0 0\n",
      "intr 25525962 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 0 15 0 105825 0 60413 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 213 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 2047316 0 1392017 375043 1643 0 3627311 0 0 0 0 205136 42261 0 0 0 205344 546530 5953874 0 3125 1039 0 0 0 773 980 0 0 0 40278 576 0 0 0 0 0 280 29086 0 0 0 0 0 0 0 0 72 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 146679 147863 148719 149445 182888 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 43771 0 0 0 0 0 113636 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 7 59 10 0 0 0 0 2721 0 23073 0 0 0 1438 2 81 0 0 40 159 0 101334 16 216 0 0 0 51 21 0 0 1 0 0 0 89568 0 0 0 0 0 0 0 388 0 0 0 25257 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 1399 0 0 0 0 13 0 0 0 13 0 0 0 0 0 0 0 90 90 0 0 8 8 0 0 0 0 1579 2721 0 0 2721 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n",
      "ctxt 67654975\n",
      "btime 1364151256\n",
      "processes 158956\n",
      "procs_running 2\n",
      "procs_blocked 0\n",
      "softirq 16349477 154 7143535 23748 83764 154 154 6218391 782795 10895 2085887\n",
      };

  Map<String, Integer> cpuloadPrev;
  Map<String, Integer> cpuloadCur;
  
  public void testGrabCpuInfo() {
    cpuloadCur = new HashMap<String, Integer>();
    cpuloadCur.put("cpuno", 0);
    String str = "cpu  463430 20870 335153 8898686 38459 67 7507 0 0 0";
    String[] values = str.split(" ");
    for(int i = 0; i < values.length; i++) {
      System.out.println(String.format("%d) '%s'", i, values[i]));
    }
    int arr[][]=new int [3][5];
    for(int i = 0; i < 3; i++)
      for(int j=0; j < 5; j++)
        arr[i][j] = i*10 + j;
    for(int i = 0; i < 3; i++)
      for(int j=0; j < 5; j++)
        System.out.println(String.format("arr[%d][%d] = %02d", i, j, arr[i][j]));
    Pattern ptn = Pattern.compile("^cpu(?<cpuno>[0-9])\\s(?<user>[0-9]+)\\s(?<nice>[0-9]+)\\s(?<system>[0-9]+)\\s(?<idle>[0-9]+)\\s(?<iowait>[0-9]+)\\s(?<irq>[0-9]+)\\s(?<softirq>[0-9]+).*");
    for(String s : statFile) {
      Matcher m = ptn.matcher(s);
      if(m.find()) {
        System.out.println(String.format("'%s'", m.group()));
        System.out.println(String.format("Found groups: %d", m.groupCount()));
        System.out.println(String.format("cpuno: '%s', user: '%s', nice: '%s', system: '%s', idel: '%s', iowait: '%s', irq: '%s', softirq: '%s'", m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6), m.group(7), m.group(8)));
      }
    }
  }

}
