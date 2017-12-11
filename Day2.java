import java.util.List;

public class Day2 {

  static char[][] GRID_PART1 = {
      { '1', '2', '3' },
      { '4', '5', '6' },
      { '7', '8', '9' } };

  static char[][] GRID_PART2 = {
      { '\0', '\0', '1', '\0', '\0' },
      { '\0', '2', '3', '4', '\0' },
      { '5', '6', '7', '8', '9' },
      { '\0', 'A', 'B', 'C', '\0' },
      { '\0', '\0', 'D', '\0', '\0' } };

  static String day2(List<String> rows, char[][] grid, int xStart, int yStart) {
    int x = xStart;
    int y = yStart;
    int bound = grid.length - 1; // assume NxN grid

    StringBuilder result = new StringBuilder(rows.size());

    for (String r : rows) {
      for (byte c : r.getBytes()) {
        switch (c) {
        case 'U':
          if (x != 0 && '\0' != grid[x - 1][y])
            x--;
          break;
        case 'D':
          if (x != bound && '\0' != grid[x + 1][y])
            x++;
          break;
        case 'L':
          if (y != 0 && '\0' != grid[x][y - 1])
            y--;
          break;
        case 'R':
          if (y != bound && '\0' != grid[x][y + 1])
            y++;
          break;
        }
      }

      result.append(grid[x][y]);
    }

    return result.toString();
  }

  public static void main(String[] args) {
    List<String> input = Util.readInput("day2.input");

    // Part 1 : 98575
    System.out.println(day2(input, GRID_PART1, 1, 1));

    // Part 2 : CD8D4
    System.out.println(day2(input, GRID_PART2, 2, 0));
  }
}
