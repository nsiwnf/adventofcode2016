import java.util.Arrays;
import java.util.List;

public class Day6 {

  // Find most common occuring letter in each col
  static String day1(List<String> rows) {
    int nCols = rows.iterator().next().length();
    byte[][] grid = new byte[nCols][26];
    for (String row : rows) {
      for (int col = 0; col < nCols; col++) {
        grid[col][row.charAt(col) - 'a']++;
      }
    }

    if (nCols != -1) {
      StringBuilder part1 = new StringBuilder(nCols);
      StringBuilder part2 = new StringBuilder(nCols);
      for (byte[] col : grid) {
        int maxIdx = 0;
        int minIdx = -1;
        for (int l = 0; l < 26; l++) {
          if (col[maxIdx] < col[l]) {
            maxIdx = l;
          }
          if (minIdx == -1 || col[minIdx] > col[l] && col[l] > 0) {
            minIdx = l;
          }
        }
        part1.append((char) (maxIdx + 'a'));
        part2.append((char) (minIdx + 'a'));
      }

      return part1.toString();
//      return part2.toString();
    }

    return null;
  }

  public static void main(String[] args) {
    // Part 1 : easter, Part 2 : advent
    List<String> input1 = Arrays.asList(
        "eedadn",
        "drvtee",
        "eandsr",
        "raavrd",
        "atevrs",
        "tsrnev",
        "sdttsa",
        "rasrtv",
        "nssdts",
        "ntnada",
        "svetve",
        "tesnvt",
        "vntsnd",
        "vrdear",
        "dvrsen",
        "enarar");
    List<String> input = Util.readInput("day6.input");
    // Part 1 : qtbjqiuq
    // Part 2 : akothqli
    System.out.println(day1(input));
  }
}
