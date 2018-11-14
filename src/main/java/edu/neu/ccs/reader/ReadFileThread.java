package edu.neu.ccs.reader;

/**
 * @author create by Xiao Han 11/9/18
 * @version 1.0
 * @since jdk 1.8
 */
public class ReadFileThread implements Runnable {

  private ReadFileListener processPoiDataListeners;
  private String filePath;
  private long start;
  private long end;

  public ReadFileThread(ReadFileListener processPoiDataListeners, long start, long end, String file) {
    this.start = start;
    this.end = end;
    this.filePath = file;
    this.processPoiDataListeners = processPoiDataListeners;
  }

  @Override
  public void run() {
    ReadFile readFile = new ReadFile();
    readFile.setReaderListener(processPoiDataListeners);
//    readFile.addObserver(readerObserver);
    try {
      readFile.readFileByLine(filePath, start, end);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

