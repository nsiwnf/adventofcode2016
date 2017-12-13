import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day5 {
  private static char[] HEX_MAP = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  static String part1(String input) {
    final int capacity = 8;
    StringBuilder passcode = new StringBuilder("--------");
    try {
      MessageDigest myDigest = MessageDigest.getInstance("MD5");
      int i = 0;
      byte charCount = 0;
      while (charCount < capacity) {
        byte[] result = myDigest.digest((input + i).getBytes());
        if (result[0] == 0 && result[1] == 0 && (result[2] & 0xF0) == 0 && (result[2] & 0x0F) < capacity) {
//           passcode.setCharAt(charCount, HEX_MAP[result[2] & 0x0f]); // Part 1
          int index = result[2] & 0x0f;
          if(passcode.charAt(index) == '-') {
            charCount++;
            passcode.setCharAt(index, HEX_MAP[(result[3] >> 4) & 0x0f]);
          }
        }
        i++;
      }
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return passcode.toString();
  }

  public static void main(String[] args) {
    System.out.println(part1("wtnhxymk"));
  }
}
