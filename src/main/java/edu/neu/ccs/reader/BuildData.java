package edu.neu.ccs.reader;


import edu.neu.ccs.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author create by Xiao Han 11/9/18
 * @version 1.0
 * @since jdk 1.8
 */
public class BuildData {
  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();
    File file = new File("/Users/xiaohan/Desktop/CS5010/assignment/assignment7/src/main/resource/WearableWorkloadDefault-1024-1000i-CPUrelaxed-long-Python-POSTraw.csv");
    //File file = new File("/Users/xiaohan/Desktop/CS5010/Student_repo_samhan0616/src/main/resources/assignment6/WearableWorkloadDefault-Javatest-GETraw.csv");
    FileInputStream fis = null;
    int maxThreadNum = Runtime.getRuntime().availableProcessors();
    PriorityBlockingQueue queue = new PriorityBlockingQueue();
    List<Future> futures = new ArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(maxThreadNum);
    try {

      ReadFile readFile = new ReadFile();
      fis = new FileInputStream(file);
      int available = fis.available();


      int i = available / maxThreadNum + 1;
      System.out.println("Max threads: " + maxThreadNum);
      for (int j = 0; j < maxThreadNum; j++) {

        long startNum = j == 0 ? 0 : FileUtil.locator(file, i * j) + 1;
        //System.out.println(j + " start " +startNum);
        long endNum = j + 1 < maxThreadNum ? FileUtil.locator(file, i * (j + 1)) : available;
        //System.out.println(j + " end " + endNum);
        System.out.printf("Start: %d, End: %d\n", startNum, endNum);
        //
        futures.add(executorService.submit(new ReadFileThread(new ReadFileListener() {
          @Override
          public void output(List<String> stringList) throws Exception {
            stringList.forEach(queue::offer);
          }
        }, startNum, endNum, file.getPath())));

      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    while(futures.size() != 0)
      futures.removeIf(Future::isDone);

    System.out.println(queue.size());

    System.out.println(System.currentTimeMillis() - start);

    executorService.shutdown();

  }
}
