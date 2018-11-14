package edu.neu.ccs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * FileUtil.
 * @author create by Xiao Han 11/10/18
 * @version 1.0
 * @since jdk 1.8
 */
public class FileUtil {

  private static final int CACHE_SIZE = 1024;

  /**
   * allow file to be read in all os.
   * @param filePath file path
   * @return string
   */
  public static String fileOperatorFomatter(String filePath) {
    return filePath.replaceAll("[\\\\|/]", File.separator);
  }

  /**
   * locate position at the closest beginning of the line.
   *
   * @param file     file
   * @param position start position
   * @return position
   * @throws IOException ioException.
   */
  public static long locator(File file, long position) throws IOException {
    long startNum = position;
    FileChannel fcin = new RandomAccessFile(file, "r").getChannel();
    fcin.position(position);
    try {
      int cache = CACHE_SIZE;
      ByteBuffer byteBuffer = ByteBuffer.allocate(cache);

      byte[] bytes = new byte[cache];

      byte[] tempBs = new byte[0];

      while (fcin.read(byteBuffer) != -1) {
        int right = byteBuffer.position();
        byteBuffer.rewind();
        byteBuffer.get(bytes);
        byteBuffer.clear();
        byte[] newStrByte = bytes;
        //
        if (tempBs.length != 0) {
          int tempLength = tempBs.length;
          newStrByte = new byte[right + tempLength];
          System.arraycopy(tempBs, 0, newStrByte, 0, tempLength);
          System.arraycopy(bytes, 0, newStrByte, tempLength, right);
        }
        //
        int endIndex = StringUtil.indexOf(newStrByte, 0);
        if (endIndex != -1) {
          return startNum + endIndex;
        }
        tempBs = StringUtil.substring(newStrByte, 0, newStrByte.length);
        startNum += 1024;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      fcin.close();
    }
    return position;
  }

  /**
   * write.
   * @param file file to writes
   * @param lines lines
   */
  public static void write(File file, List<String> lines) throws IOException {
    if (!file.exists()) {
       file.createNewFile();
    }

    try(OutputStreamWriter outputStreamWriter =  new OutputStreamWriter(
            new FileOutputStream(file), StandardCharsets.UTF_8);) {
      for (String str : lines) {
        outputStreamWriter.write(str);
      }
      outputStreamWriter.flush();
    }
  }


}
