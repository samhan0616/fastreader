package edu.neu.ccs.reader;

import java.io.IOException;

/**
 * FileReader interface.
 * @author create by Xiao Han 11/7/18
 * @version 1.0
 * @since jdk 1.8
 */
public interface IFileReader {

  /**
   * read one file.
   * @param filePath file path
   * @throws IOException if read file occurs io exception
   */
  void readFile(String filePath) throws IOException;
}
