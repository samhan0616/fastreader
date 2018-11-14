package edu.neu.ccs.reader;


import edu.neu.ccs.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author create by Xiao Han 11/9/18
 * @version 1.0
 * @since jdk 1.8
 */
public class BuildData {
  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();
    File file = new File("/Users/xiaohan/Desktop/CS5010/Student_repo_samhan0616/src/main/resources/assignment6/WearableWorkloadDefault-1024-1000i-CPUrelaxed-long-Python-POSTraw.csv");
    //File file = new File("/Users/xiaohan/Desktop/CS5010/Student_repo_samhan0616/src/main/resources/assignment6/WearableWorkloadDefault-Javatest-GETraw.csv");
    FileInputStream fis = null;
    int maxThreadNum = Runtime.getRuntime().availableProcessors();
    System.out.println(maxThreadNum);
    LinkedBlockingQueue<String> pool = new LinkedBlockingQueue<>(500);

    try {

      ReadFile readFile = new ReadFile();
      fis = new FileInputStream(file);
      int available = fis.available();

      System.out.println(available);


      int i = available / maxThreadNum + 1;
      System.out.println("Max threads: " + maxThreadNum);
      for (int j = 0; j < maxThreadNum; j++) {

        long startNum = j == 0 ? 0 : FileUtil.locator(file, i * j) + 1;
        //System.out.println(j + " start " +startNum);
        long endNum = j + 1 < maxThreadNum ? FileUtil.locator(file, i * (j + 1)) : available;
        //System.out.println(j + " end " + endNum);
        System.out.printf("Start: %d, End: %d\n", startNum, endNum);
        //
        new ReadFileThread(new ReadFileListener() {
          @Override
          public void output(List<String> stringList) throws Exception {
            long start2 = System.currentTimeMillis();
            for (String s: stringList)
              pool.offer(s);



          }
        }, startNum, endNum, file.getPath()).start();

      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    System.out.println(System.currentTimeMillis() - start);


  }
}
