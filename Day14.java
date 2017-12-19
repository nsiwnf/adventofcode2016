
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Day14 {
  private static MessageDigest digest;

  private static byte[] HEX_VALUES = {
      (byte) 0x00, (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x77,
      (byte) 0x88, (byte) 0x99, (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff };

  private static List<String> HEX_STRINGS1 = Arrays.asList(
      "000", "111", "222", "333", "444", "555", "666", "777",
      "888", "999", "aaa", "bbb", "ccc", "ddd", "eee", "fff");

  private static List<String> HEX_STRINGS2 = Arrays.asList(
      "00000", "11111", "22222", "33333", "44444", "55555", "66666", "77777",
      "88888", "99999", "aaaaa", "bbbbb", "ccccc", "ddddd", "eeeee", "fffff");

  static int part1(String input) {
    int keyCount = 0;
    Queue<String> hashQueue = new ArrayBlockingQueue<>(1000);
    Queue<Integer> indexQueue = new ArrayBlockingQueue<>(1000);

    // Add the first possible key
    int index = 0;
    while (hashQueue.isEmpty()) {
      byte[] hash = digest.digest((input + index).getBytes());
      queueHash(hashQueue, hash, indexQueue, index);
      index++;
    }

    int lastIdx = -1;
    while (keyCount < 67) {
      String hashString = hashQueue.poll();
      int idx = indexQueue.poll();

      // Skip to next possible key (skip over 5-repeats)
      while (hashString.length() != 3) {
        hashString = hashQueue.poll();
        idx = indexQueue.poll();
      }

      // Queue up 3/5 hashes for the next 1000 indices
      while (index - idx < 1000) {
        byte[] hash = digest.digest((input + index).getBytes());
        queueHash(hashQueue, hash, indexQueue, index);
        index++;
      }

      // If the queue contains the 5-repeat for this 3-repeat hash, increment
      // keyCount and store the index to return
      if (hashQueue.contains(HEX_STRINGS2.get(HEX_STRINGS1.indexOf(hashString)))) {
        System.out.println(idx);
        keyCount++;
        lastIdx = idx;
      }
    }

    return lastIdx;
  }

  private static void queueHash(Queue<String> hashQueue, byte[] hash, Queue<Integer> indexQueue, int index) {
    int prev = -1;
    for (int i = 0; i < hash.length - 1; i++) {
      int a = hash[i] & 0x0f;
      if (HEX_VALUES[a] == hash[i]) {
        // 5
        if (hash[i] == hash[i + 1]) {
          if (i > 0 && prev == a || i < hash.length - 2 && (hash[i + 2] & 0xf0) == a << 4) {
            hashQueue.add(HEX_STRINGS2.get(a));
            indexQueue.add(index);
          }
        }
        // 3
        else if ((hash[i + 1] & 0xf0) == a << 4 || prev == a) {
          hashQueue.add(HEX_STRINGS1.get(a));
          indexQueue.add(index);
        }
      }
      prev = a;
    }
  }

  private static final char[] HEX_MAP = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
      'f' };
  static String hTS(byte[] hash) {
    StringBuilder b = new StringBuilder(32);
    for (byte aHash : hash) {
      b.append(HEX_MAP[aHash >> 4 & 0x0f]);
      b.append(HEX_MAP[aHash & 0x0f]);
    }
    return b.toString();
  }

  public static void main(String[] args) {
    try {
      digest = MessageDigest.getInstance("MD5");

      System.out.println(part1("abc")); // 22728
      // System.out.println(part1("cuanljph"));

    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
}
