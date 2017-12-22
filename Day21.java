
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day21 {

  static String part1(List<String> input, String letterString) {
    List<Command> commands = convert(input);
    char[] letters = letterString.toCharArray();
    char[] _tmp = new char[letters.length];
    for (Command cmd : commands) {
      // System.out.println(String.valueOf(letters));
      if (cmd.op == 'S') {
        char tmp = letters[cmd.x];
        letters[cmd.x] = letters[cmd.y];
        letters[cmd.y] = tmp;
      }
      else if (cmd.op == 'E') {
        char a = (char) cmd.x;
        char b = (char) cmd.y;

        int t = -1;
        int i = 0;
        while (i < 16 && t != -2) {
          if (letters[i] == a || letters[i] == b) {
            if (t == -1) {
              t = i;
            }
            else {
              char tmp = letters[i];
              letters[i] = letters[t];
              letters[t] = tmp;
              t = -2;
            }
          }
          i++;
        }
      }
      else if (cmd.op == 'V') {
        for (int i = 0; i < (cmd.y - cmd.x + 1) / 2; i++) {
          char tmp = letters[i + cmd.x];
          letters[i + cmd.x] = letters[cmd.y - i];
          letters[cmd.y - i] = tmp;
        }
      }
      else if (cmd.op == 'R') {
        int offset = (cmd.x + letters.length) % letters.length;
        rotate(letters, _tmp, offset);
      }
      else if (cmd.op == 'P') {
        int i = 0;
        while (letters[i] != cmd.x && i < letters.length) {
          i++;
        }
        i += i >= 4 ? 2 : 1;
        rotate(letters, _tmp, i % letters.length);
      }
      else if (cmd.op == 'M') {
        int idx = cmd.x;
        int newIdx = cmd.y;
        char tmp = letters[idx];

        if (idx < newIdx) {
          System.arraycopy(letters, idx + 1, letters, idx, newIdx - idx);
        }
        else {
          System.arraycopy(letters, newIdx, letters, newIdx + 1, idx - newIdx);
        }
        letters[newIdx] = tmp;
      }
    }

    return String.valueOf(letters);
  }

  static String part2(List<String> input, String letterString) {
    List<Command> commands = convert(input);
    Collections.reverse(commands);
    char[] letters = letterString.toCharArray();
    char[] _tmp = new char[letters.length];
    for (Command cmd : commands) {
      // System.out.println(String.valueOf(letters));
      if (cmd.op == 'S') {
        char tmp = letters[cmd.x];
        letters[cmd.x] = letters[cmd.y];
        letters[cmd.y] = tmp;
      }
      else if (cmd.op == 'E') {
        char a = (char) cmd.x;
        char b = (char) cmd.y;

        int t = -1;
        int i = 0;
        while (i < 16 && t != -2) {
          if (letters[i] == a || letters[i] == b) {
            if (t == -1) {
              t = i;
            }
            else {
              char tmp = letters[i];
              letters[i] = letters[t];
              letters[t] = tmp;
              t = -2;
            }
          }
          i++;
        }
      }
      else if (cmd.op == 'V') {
        for (int i = 0; i < (cmd.y - cmd.x + 1) / 2; i++) {
          char tmp = letters[i + cmd.x];
          letters[i + cmd.x] = letters[cmd.y - i];
          letters[cmd.y - i] = tmp;
        }
      }
      else if (cmd.op == 'R') {
        int offset = (2 * letters.length - cmd.x) % letters.length;
        rotate(letters, _tmp, offset);
      }
      else if (cmd.op == 'P') {
        int j = 0;
        while (letters[j] != cmd.x && j < letters.length) {
          j++;
        }

        // 1,3,5,7,2,4,6,0
        // 7,6,5,4,2,1,0,7
        // 0,1,2,3,4,5,6,7

        int i = reverseOffset(letters.length, j);
        rotate(letters, _tmp, (letters.length + i - j) % letters.length);
      }
      else if (cmd.op == 'M') {
        int newIdx = cmd.x;
        int idx = cmd.y;
        char tmp = letters[idx];

        if (idx < newIdx) {
          System.arraycopy(letters, idx + 1, letters, idx, newIdx - idx);
        }
        else {
          System.arraycopy(letters, newIdx, letters, newIdx + 1, idx - newIdx);
        }
        letters[newIdx] = tmp;
      }
    }

    return String.valueOf(letters);
  }

  private static int reverseOffset(int n, int i) {
    int j = 0;
    boolean foundJ = false;
    while (!foundJ) {
      if (j < 4 && (2 * j + 1) % n == i || j >= 4 && (2 * j + 2) % n == i) {
        foundJ = true;
      }
      else {
        j++;
      }
    }
    return j;
  }

  private static void rotate(char[] letters, char[] _tmp, int offset) {
    System.arraycopy(letters, letters.length - offset, _tmp, 0, offset);
    System.arraycopy(letters, 0, letters, offset, letters.length - offset);
    System.arraycopy(_tmp, 0, letters, 0, offset);
  }

  static class Command {
    char op;

    int x;

    int y;

    Command(char op, int x, int y) {
      this.op = op;
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
      if (op == 'S') {
        return "swap " + x + " with " + y;
      }
      else if (op == 'E') {
        return "swap " + (char) x + " with " + (char) y;
      }
      else if (op == 'V') {
        return "reverse " + x + " and " + y;
      }
      else if (op == 'R') {
        return "rotate " + x;
      }
      else if (op == 'P') {
        return "rotate " + (char) x;
      }
      else if (op == 'M') {
        return "move " + x + " to " + y;
      }
      return null;
    }
  }

  private static List<Command> convert(List<String> input) {
    List<Command> result = new ArrayList<>();

    for (String i : input) {
      String[] instruction = i.split("\\s+");
      switch (instruction[0]) {
      case "swap": {
        String x = instruction[2];
        String y = instruction[5];
        if (instruction[1].equals("position")) {
          result.add(new Command('S', Integer.parseInt(x), Integer.parseInt(y)));
        }
        else if (instruction[1].equals("letter")) {
          result.add(new Command('E', x.charAt(0), y.charAt(0)));
        }
        break;
      }
      case "reverse": {
        String x = instruction[2];
        String y = instruction[4];
        result.add(new Command('V', Integer.parseInt(x), Integer.parseInt(y)));
        break;
      }
      case "rotate":
        switch (instruction[1]) {
        case "left":
          result.add(new Command('R', -Integer.parseInt(instruction[2]), -1));
          break;
        case "right":
          result.add(new Command('R', Integer.parseInt(instruction[2]), -1));
          break;
        case "based":
          result.add(new Command('P', instruction[6].charAt(0), -1));
          break;
        }
        break;
      case "move":
        result.add(new Command('M', Integer.parseInt(instruction[2]), Integer.parseInt(instruction[5])));
        break;
      }
    }
    return result;
  }

  public static void main(String[] args) {
    List<String> input = Util.readInput("day21.input");

    // System.out.println(part1(input, "abcdefgh")); // gbhcefad

    System.out.println(part1(input, "gahedfcb"));
    System.out.println(part2(input, "fbgdceah")); // not hefbgacd
  }
}
