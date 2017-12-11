import java.util.Arrays;
import java.util.List;

public class Day4 {

  static long day4(List<String> rooms) {
    long idSum = 0;
    int northPoleId = -1;
    byte[] NORTH_POLE = "northpole".getBytes();
    int[] count = new int[26]; // 0=a, 1=b, 2=c etc..
    byte[] expctCS = new byte[5];
    for (String room : rooms) {
      int id = 0;
      boolean isValid = true;
      int csOffset = -1;
      Arrays.fill(count, 0);
      Arrays.fill(expctCS, (byte) -1);
      byte[] bytes = room.getBytes();
      for (int i = 0; i < bytes.length - 1; i++) {
        byte c = bytes[i];
        int k = c - 'a';
        if (c == '[') {
          csOffset = i + 1;
        }
        else if (csOffset != -1) {
          // validate
          isValid &= (expctCS[i - csOffset] == k);
        }
        else if (c >= 'a' && c <= 'z') {
          int v = ++count[k];
          for (int j = 0; j < expctCS.length; j++) {
            byte k1 = expctCS[j];
            if (k1 == -1 || v > count[k1] || (v == count[k1] && k <= k1) || k1 == c - 'a') {
              // replace and swap
              expctCS[j] = (byte) k;
              k = k1;

              if (k1 == -1 || k1 == c - 'a') {
                // wasnt set or found the original location of k
                break;
              }
            }
          }
        }
        else if (c >= '0' && c <= '9') {
          id = id * 10 + (c - '0');
        }
      }
      if (isValid) {
        idSum += id;
        boolean isEqual = compareBytes(NORTH_POLE, bytes, id);
        if (isEqual) {
          northPoleId = id;
        }
      }
    }

    // Part 1
//     return idSum;

    // Part 2
    return northPoleId;
  }

  /**
   * shiftedBytes.startsWith(expected)
   */
  private static boolean compareBytes(byte[] expected, byte[] bytes, int shift) {
    for (int i = 0; i < expected.length; i++) {
      byte b = bytes[i];
      if (expected[i] != (b - 'a' + shift) % 26 + 'a') {
        return false;
      }
    }

    return true;
  }

  public static void main(String[] args) {
    List<String> input = Util.readInput("day4.input");

    // Part 1 : 137896
    // Part 2 : 501
    System.out.println(day4(input));
  }
}
