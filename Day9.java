public class Day9 {

  static int part1(String input) {
    int l = 0; // decompressed length
    int i = 0;
    while (i < input.length()) {
      char c = input.charAt(i);
      if (c == '(') {
        int indexOfX = input.indexOf('x', i);
        int endIndex = input.indexOf(')', indexOfX);

        int chars = Integer.parseInt(input.substring(i + 1, indexOfX));
        int repts = Integer.parseInt(input.substring(indexOfX + 1, endIndex));

        i = endIndex + 1 + chars;
        l += chars * repts;
      }
      else {
        l++;
        i++;
      }
    }

    return l;
  }

  static long part2(String input, int i, int end) {
    long l = 0L; // decompressed length
    while (i < end) {
      char c = input.charAt(i);
      if (c == '(') {
        int indexOfX = input.indexOf('x', i);
        int endIndex = input.indexOf(')', indexOfX);

        int chars = Integer.parseInt(input.substring(i + 1, indexOfX));
        int repts = Integer.parseInt(input.substring(indexOfX + 1, endIndex));

        i = endIndex + 1 + chars;

        long lengthOfSub = part2(input, endIndex + 1, i);
        l += lengthOfSub * repts;
      }
      else {
        l++;
        i++;
      }
    }

    return l;
  }

  public static void main(String[] args) {
    String input = Util.readInput("day9.input").iterator().next();

    // Part 1 : 98135
    System.out.println(part1(input));

    // Part 2 : 10964557606
    System.out.println(part2(input, 0, input.length()));
  }
}
