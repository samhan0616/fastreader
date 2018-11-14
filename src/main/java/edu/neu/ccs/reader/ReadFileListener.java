package edu.neu.ccs.reader;

import java.util.ArrayList;
import java.util.List;

/**
 * ReadFileListener.
 *
 * @author create by Xiao Han 11/9/18
 * @version 1.0
 * @since jdk 1.8
 */

public abstract class ReadFileListener {

  // current lines
  private int cahcheSize = 2000;

  private List<String> list = new ArrayList<>();

  protected boolean done;

  /**
   * read lines.
   *
   * @param cahcheSize size of line caching
   */
  protected void setReadColNum(int cahcheSize) {
    this.cahcheSize = cahcheSize;
  }

  /**
   * outLine to output.
   *
   * @param lineStr string of line
   * @param lineNum line num
   * @param done if done
   * @throws Exception if out occurs exception
   */
  public void outLine(String lineStr, long lineNum, boolean done) throws Exception {
    if (null != lineStr) {
      list.add(lineStr);
    }
    if (!done && (lineNum % cahcheSize == 0)) {
      output(list);
      list.clear();
    } else if (done) {
      this.done = done;
      output(list);
      list.clear();
    }
  }

  /**
   * output to source.
   *
   * @param stringList stringList
   * @throws Exception if exception occurs for implements class.
   */
  public abstract void output(List<String> stringList) throws Exception;

}

