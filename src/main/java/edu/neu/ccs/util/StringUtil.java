package edu.neu.ccs.util;

import java.nio.charset.StandardCharsets;

/**
 * StringUtil.
 * @author create by Xiao Han 11/10/18
 * @version 1.0
 * @since jdk 1.8
 */
public class StringUtil {

  private static final int KEY = "\n".getBytes(StandardCharsets.UTF_8)[0];

  /**
   * substring.
   * @param src src
   * @param fromIndex substring from
   * @param endIndex substring to
   * @return bytes of string
   */
  public static byte[] substring(byte[] src, int fromIndex, int endIndex) {
    int size = endIndex - fromIndex;
    byte[] ret = new byte[size];
    System.arraycopy(src, fromIndex, ret, 0, size);
    return ret;
  }


  /**
   * find line changer.
   * @param src src
   * @param fromIndex from index
   * @return the index of line mark \n
   */
  public static int indexOf(byte[] src, int fromIndex) {

    for (int i = fromIndex; i < src.length; i++) {
      if (src[i] == KEY) {
        return i;
      }
    }
    return -1;
  }

  /**
   * formatLeftS.
   * @param str target str
   * @param minLength min length
   * @return formatted string
   */
  public static String formatLeftS(String str, int minLength) {
    String format = "%-" + (minLength < 1 ? 1 : minLength) + "s";
    return String.format(format, str);
  }


}
