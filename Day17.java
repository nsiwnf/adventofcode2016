import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Day17 {

  private static MessageDigest digest;

  private static final boolean[] OPEN = { false, false, false, false, false, false, false, false, false, false, false, true,
      true, true, true, true };

  static class Point {
    int x;

    int y;

    String code;

    Point(int x, int y, String code) {
      this.x = x;
      this.y = y;
      this.code = code;
    }
  }

  public static String part1(String input, final int endX, final int endY) {
    Queue<Point> positions = new ArrayBlockingQueue<>(50);
    positions.add(new Point(0, 0, input));

    while (!positions.isEmpty()) {
      Point p = positions.poll();

      if (p.x == endX && p.y == endY) {
        return p.code.substring(input.length());
      }

      addPoints(endX, endY, positions, p);
    }

    // no path
    return null;
  }

  public static int part2(String input, final int endX, final int endY) {
    Queue<Point> positions = new ArrayBlockingQueue<>(500);
    positions.add(new Point(0, 0, input));

    int max = -1;
    while (!positions.isEmpty()) {
      Point p = positions.poll();

      if (p.x == endX && p.y == endY) {
        max = Math.max(max, p.code.length());
      }
      else {
        addPoints(endX, endY, positions, p);
      }

    }

    return max - input.length();
  }

  private static void addPoints(int endX, int endY, Queue<Point> positions, Point p) {
    byte[] hash = digest.digest(p.code.getBytes());

    // up
    if (p.x > 0 && OPEN[(hash[0] >> 4) & 0xf]) {
      positions.add(new Point(p.x - 1, p.y, p.code + 'U'));
    }

    // down
    if (p.x < endX && OPEN[hash[0] & 0x0f]) {
      positions.add(new Point(p.x + 1, p.y, p.code + 'D'));
    }

    // left
    if (p.y > 0 && OPEN[(hash[1] >> 4) & 0xf]) {
      positions.add(new Point(p.x, p.y - 1, p.code + 'L'));
    }

    // right
    if (p.y < endY && OPEN[hash[1] & 0x0f]) {
      positions.add(new Point(p.x, p.y + 1, p.code + 'R'));
    }
  }

  public static void main(String[] args) {
    try {
      digest = MessageDigest.getInstance("MD5");

      System.out.println(part2("pslxynzg", 3, 3));
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
}
