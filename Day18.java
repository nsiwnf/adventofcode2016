
public class Day18 {
  private final static char TRAP = '^';

  /**
   * Trap conditions: <br>
   * - Its left and center tiles are traps, but its right tile is not. 110 <br>
   * - Its center and right tiles are traps, but its left tile is not. 011 <br>
   * - Only its left tile is a trap. 100 <br>
   * - Only its right tile is a trap. 001 <br>
   *
   * @return number of safe tiles
   */
  static int getNumSafe(String firstRow, final int nRows) {
    int[] prevRow = new int[firstRow.length()];
    for (int i = 0; i < prevRow.length; i++) {
      prevRow[i] = (firstRow.charAt(i) == TRAP ? 0 : 1);
    }

    int[] row = new int[prevRow.length];
    int rLen = prevRow.length - 1;

    int safe = 0;
    for (int r = 0; r < nRows; r++) {
      // first and last
      row[0] = prevRow[1];
      row[rLen] = prevRow[rLen - 1];
      safe += prevRow[0] + prevRow[rLen];

      // in between
      for (int i = 1; i < rLen; i++) {
        safe += prevRow[i];
        row[i] = prevRow[i - 1] == prevRow[i + 1] ? 1 : 0;
      }

      // swap
      int[] tmp = prevRow;
      prevRow = row;
      row = tmp;
    }

    return safe;
  }

  public static void main(String[] args) {
    String input = "^..^^.^^^..^^.^...^^^^^....^.^..^^^.^.^.^^...^.^.^.^.^^.....^.^^.^.^.^.^.^.^^..^^^^^...^.....^....^.";

    System.out.println(getNumSafe(input, 40));
    System.out.println(getNumSafe(input, 400000));
  }
}
