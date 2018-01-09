
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Day22 {

  static long part1(List<String> input) {
    int viableNodes = 0;
    for (int i = 0; i < input.size() - 1; i++) {
      String[] a = input.get(i).split("\\s+");
      int availA = Integer.parseInt(a[3].substring(0, a[3].length() - 1));
      int usedA = Integer.parseInt(a[2].substring(0, a[2].length() - 1));
      for (int j = i + 1; j < input.size(); j++) {
        String[] b = input.get(j).split("\\s+");
        int availB = Integer.parseInt(b[3].substring(0, b[3].length() - 1));
        if (usedA != 0 && usedA <= availB) {
          viableNodes++;
        }
        if (!b[2].equals("0T")) {
          int usedB = Integer.parseInt(b[2].substring(0, b[2].length() - 1));
          if (usedB < availA) {
            viableNodes++;
          }
        }
      }
    }
    return viableNodes;
  }

  static long part2(List<String> input, int endX, int endY) {
    // Build grid
    int[][] used = new int[endY + 1][endX + 1];
    int[][] avai = new int[endY + 1][endX + 1];
    int eX = -1;
    int eY = -1;
    for (int i = 0; i < input.size() - 1; i++) {
      String parsed = input.get(i).replaceAll("/dev/grid/node-x(\\d+)|-y(\\d+)|(\\d+)T|\\d+%", "$1 $2$3");
      int[] a = Arrays.stream(parsed.split("\\s+")).mapToInt(Integer::parseInt).toArray();
      int x = a[0];
      int y = a[1];
      int usedA = a[3];
      int avaiA = a[4];
      used[y][x] = usedA;
      avai[y][x] = avaiA;

      if (usedA == 0 && avaiA > 0) {
        eX = x;
        eY = y;
      }
    }

    int steps = 0;
    int dataSize = used[0][endX];
    Queue<Point> points = new ArrayBlockingQueue<>(100);
    boolean[][] visited = new boolean[used.length][used[0].length];
    for (int i = endX - 1; i >= 0; i--) {
      // reset
      points.clear();
      for (boolean[] v : visited) {
        Arrays.fill(v, false);
      }
      // Dont pass through goal
      visited[0][i + 1] = true;

      // empty to pos before goal
      points.add(new Point(eX, eY, avai[eY][eX], null));
      steps += shortestPath(points, visited, i, used, avai, dataSize);

      // goal to empty
      avai[0][i + 1] += used[0][i + 1];
      avai[0][i] -= used[0][i + 1];
      used[0][i] += used[0][i + 1];
      used[0][i + 1] = 0;
      steps++;
      eX = i + 1;
      eY = 0;
    }

    return steps;
  }

  static class Point {
    int x;

    int y;

    int avai;

    List<Point> prev = new ArrayList<>();

    public Point(int x, int y, int avai, Point p) {
      this.x = x;
      this.y = y;
      this.avai = avai;
      if (p != null) {
        prev.addAll(p.prev);
        prev.add(p);
      }
    }

    @Override
    public String toString() {
      return x + "," + y;
    }
  }

  static int shortestPath(Queue<Point> points, boolean[][] visited, int goalX, int[][] used, int[][] avai,
      int dataSize) {

    while (!points.isEmpty()) {
      Point p = points.poll();

      if (p.x == goalX && p.y == 0) {
        List<Point> prev = p.prev;
        prev.add(p);
        for (int i = 0; i < prev.size() - 1; i++) {
          Point c = prev.get(i);
          Point n = prev.get(i + 1);

          used[c.y][c.x] += used[n.y][n.x];
          avai[c.y][c.x] -= used[n.y][n.x];

          avai[n.y][n.x] += used[n.y][n.x];
          used[n.y][n.x] = 0;
        }

        return p.prev.size() - 1;
      }

      if (!visited[p.y][p.x]) {
        visited[p.y][p.x] = true;
        // to be able to move data, there must be enough space in the next node
        // to accommodate dataSize minus the available in the current node

        // Move empty data up
        int newX = p.x - 1;
        int newY = p.y;
        if (p.x > 0 && !visited[newY][newX] && p.avai >= used[newY][newX]) {
          int newAvail = avai[newY][newX] + Math.min(used[newY][newX], p.avai);
          if (newAvail >= dataSize) {
            points.add(new Point(newX, newY, newAvail, p));
          }
        }

        newX = p.x;
        newY = p.y + 1;
        if (p.y < avai.length - 1 && !visited[newY][newX] && p.avai >= used[newY][newX]) {
          int newAvail = avai[newY][newX] + Math.min(used[newY][newX], p.avai);
          if (newAvail >= dataSize) {
            points.add(new Point(newX, newY, newAvail, p));
          }
        }

        newX = p.x + 1;
        newY = p.y;
        if (p.x < avai[newY].length - 1 && !visited[newY][newX] && p.avai >= used[newY][newX]) {
          int newAvail = avai[newY][newX] + Math.min(used[newY][newX], p.avai);
          if (newAvail >= dataSize) {
            points.add(new Point(newX, newY, newAvail, p));
          }
        }

        newX = p.x;
        newY = p.y - 1;
        if (p.y > 0 && !visited[newY][newX] && p.avai >= used[newY][newX]) {
          int newAvail = avai[newY][newX] + Math.min(used[newY][newX], p.avai);
          if (newAvail >= dataSize) {
            points.add(new Point(newX, newY, newAvail, p));
          }
        }
      }
    }

    return -1;
  }

  static String printGrid(int[][] used, int[][] avai) {
    StringBuilder b = new StringBuilder();
    for (int y = 0; y < used.length; y++) {
      for (int x = 0; x < used[y].length; x++) {
        b.append(used[y][x]).append('/').append(avai[y][x] + used[y][x]).append(' ');
      }
      b.append('\n');
    }
    return b.toString();
  }

  public static void main(String[] args) {
    List<String> input = Util.readInput("day22.input");

    // input = Arrays.asList(
    // "/dev/grid/node-x0-y0 10T 8T 2T 80%",
    // "/dev/grid/node-x0-y1 11T 6T 5T 54%",
    // "/dev/grid/node-x0-y2 32T 28T 4T 87%",
    // "/dev/grid/node-x1-y0 9T 7T 2T 77%",
    // "/dev/grid/node-x1-y1 8T 0T 8T 0%",
    // "/dev/grid/node-x1-y2 11T 7T 4T 63%",
    // "/dev/grid/node-x2-y0 10T 6T 4T 60%",
    // "/dev/grid/node-x2-y1 9T 8T 1T 88%",
    // "/dev/grid/node-x2-y2 9T 6T 3T 66%");
    // System.out.println(part2(input, 2, 2));

    System.out.println(part2(input, 37, 27)); // 261
  }
}
