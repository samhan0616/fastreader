package edu.neu.ccs.reader;


import edu.neu.ccs.util.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Observable;
import java.util.Observer;

/**
 * ReadFile.
 * @author create by Xiao Han 11/9/18
 * @version 1.0
 * @since jdk 1.8
 */
public class ReadFile extends Observable {

  private int bufSize = 1024;
  // current line
  private long lineNum = 0;

  // listener
  private ReadFileListener readerListener;
  //jobid
  private static final Charset encode = StandardCharsets.UTF_8;


  public void setReaderListener(ReadFileListener readerListener) {
    this.readerListener = readerListener;
  }



  /**
   * read from start to end.
   * @param fullPath fullpath of file
   * @param start start position
   * @param end end position
   * @throws Exception if exception occur then throw it in FileOperationException
   */
  public void readFileByLine(String fullPath, long start, long end) throws Exception {

    File fin = new File(fullPath);
    if (fin.exists()) {
      FileChannel fcin = new RandomAccessFile(fin, "r").getChannel();
      fcin.position(start);
      try {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufSize);
        // buffered
        byte[] bytes = new byte[bufSize];
        // cache not completed line
        byte[] tempBs = new byte[0];

        // current posi
        long nowCur = start;
        while (fcin.read(byteBuffer) != -1) {

          int right = byteBuffer.position();
          nowCur += right;

          if (nowCur > end) {
            right = (int)(right - (nowCur - end));
          }

          byteBuffer.rewind();
          byteBuffer.get(bytes);
          byteBuffer.clear();
          byte[] newStrByte = bytes;

          // add remained to front
          if (tempBs.length != 0) {
            int tempLength = tempBs.length;
            newStrByte = new byte[right + tempLength];
            System.arraycopy(tempBs, 0, newStrByte, 0, tempLength);
            System.arraycopy(bytes, 0, newStrByte, tempLength, right);
          } else {
            newStrByte = StringUtil.substring(newStrByte, 0, right);
          }

          // check is end
          boolean isEnd = false;

          int fromIndex = 0;
          int endIndex = 0;
          String line = "";

          // read by line
          while ((endIndex = StringUtil.indexOf(newStrByte, fromIndex)) != -1) {
            byte[] lineBytes = StringUtil.substring(newStrByte, fromIndex, endIndex);
            line = new String(lineBytes, 0, lineBytes.length, encode);
            lineNum++;
            // output one line
            readerListener.outLine(line.trim(), lineNum, false);
            fromIndex = endIndex + 1;
          }

          if (nowCur >= end) {
            isEnd = true;
          }
          // cache remained data
          tempBs = StringUtil.substring(newStrByte, fromIndex, newStrByte.length);
          if (isEnd) {
            break;
          }
        }
        // output last line or buff all line.
        if (tempBs.length > 0) {
          String lineStr = new String(tempBs, 0, tempBs.length, encode);
          lineNum++;
          readerListener.outLine(lineStr.trim(), lineNum, true);
        } else {
          readerListener.outLine(null, lineNum, true);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        fcin.close();
      }

    } else {
      throw new FileNotFoundException("File not found");
    }

  }



}

