import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Day13 {

  static int part1(int initial, int endX, int endY) {
    // System.out.println(printGrid(initial, endX + 10, endY + 10));
    boolean[][] visited = new boolean[endX + 10][endY + 10];
    return part1(initial, 1, 1, endX, endY, visited);
  }

  static class Point {
    int x;

    int y;

    int dist;

    public Point(int x, int y, int dist) {
      this.x = x;
      this.y = y;
      this.dist = dist;
    }
  }

  static int part1(int initial, final int startX, final int startY, int endX, int endY, boolean[][] visited) {
    Queue<Point> pointQueue = new ArrayBlockingQueue<>(100);
    Point start = new Point(startX, startY, 0);
    pointQueue.add(start);

    int numVisited = 0;
    while (!pointQueue.isEmpty()) {
      Point p = pointQueue.poll();

      int x = p.x;
      int y = p.y;

      // Part 1
      // if (p.x == endX && p.y == endY) {
      // return p.dist;
      // }

      if (!visited[x][y]) {
        numVisited++;
      }

      visited[x][y] = true;
      if (p.dist < 50) {
        if (x < (endX + 10) && !visited[x + 1][y] && isValid(initial, x + 1, y)) {
          pointQueue.add(new Point(x + 1, y, p.dist + 1));
        }

        if (x > 0 && !visited[x - 1][y] && isValid(initial, x - 1, y)) {
          pointQueue.add(new Point(x - 1, y, p.dist + 1));
        }

        if (y < (endY + 10) && !visited[x][y + 1] && isValid(initial, x, y + 1)) {
          pointQueue.add(new Point(x, y + 1, p.dist + 1));
        }

        if (y > 0 && !visited[x][y - 1] && isValid(initial, x, y - 1)) {
          pointQueue.add(new Point(x, y - 1, p.dist + 1));
        }
      }
    }

    return numVisited;
  }

  static boolean isValid(int initial, int x, int y) {
    initial += x * x + 3 * x + 2 * x * y + y + y * y;
    byte result = 0;
    for (int i = 0; i < 32; i++) {
      int i2 = initial >> i;
      result ^= i2 & 0b0001;
      if (i2 == 0) {
        return result == 0;
      }
    }

    return result == 0;
  }

  static String printGrid(int initial, int xLen, int yLen) {
    StringBuilder b = new StringBuilder();
    for (int x = 0; x < xLen; x++) {
      b.append(x);
      for (int y = 0; y < yLen; y++) {
        b.append(isValid(initial, x, y) ? '.' : '#');
      }
      b.append('\n');
    }

    return b.toString();
  }

  public static void main(String[] args) {

    int input = 1364;
    // System.out.println(part1(10, 7, 4));
    System.out.println(part1(input, 31, 39));
  }

}
