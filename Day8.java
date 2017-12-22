
import java.util.ArrayList;
import java.util.List;

public class Day8 {

  private static final char ON = '#';

  private static final char OFF = ' ';

  private static final char RECT = 'T';

  private static final char ROTX = 'X';

  private static final char ROTY = 'Y';

  static class Command {
    char op;

    int a;

    int b;

    Command(char op, int a, int b) {
      this.op = op;
      this.a = a;
      this.b = b;
    }
  }

  static int part1(List<String> input) {
    List<Command> commands = convert(input);
    char[][] grid = createGrid(50, 6);
    char[] _tmp = new char[grid[0].length];

    for (Command command : commands) {
      if (command.op == RECT) {
        for (int y = 0; y < command.b; y++) {
          for (int x = 0; x < command.a; x++) {
            grid[y][x] = ON;
          }
        }
      }
      else if (command.op == ROTX) {
        int x = command.a;
        int offset = command.b;

        for (int y = 0; y < grid.length; y++) {
          _tmp[y] = grid[y][x];
        }

        for (int y = 0; y < grid.length; y++) {
          grid[(y + offset) % grid.length][x] = _tmp[y];
        }

      }
      else if (command.op == ROTY) {
        int y = command.a;
        int offset = command.b;

        System.arraycopy(grid[y], grid[y].length - offset, _tmp, 0, offset);
        System.arraycopy(grid[y], 0, grid[y], offset, grid[y].length - offset);
        System.arraycopy(_tmp, 0, grid[y], 0, offset);
      }
    }

    int countOn = 0;
    for (char[] y : grid) {
      for (char x : y) {
        if (x == ON) {
          countOn++;
        }
      }
    }

    System.out.println(printGrid(grid));

    return countOn;
  }

  private static String printGrid(char[][] grid) {
    StringBuilder b = new StringBuilder();
    for (char[] y : grid) {
      for (char x : y) {
        b.append(x);
      }
      b.append('\n');
    }
    return b.toString();
  }

  private static char[][] createGrid(int xSize, int ySize) {
    char[][] grid = new char[ySize][xSize];

    for (int y = 0; y < ySize; y++) {
      for (int x = 0; x < xSize; x++) {
        grid[y][x] = OFF;
      }
    }

    return grid;
  }

  private static List<Command> convert(List<String> input) {
    List<Command> commands = new ArrayList<>(input.size());
    for (String instruction : input) {
      String[] inst = instruction.split(" ");
      String command = inst[0];
      if (command.equals("rect")) {
        int indexX = inst[1].indexOf('x');
        commands.add(new Command(RECT, Integer.parseInt(inst[1].substring(0, indexX)),
            Integer.parseInt(inst[1].substring(indexX + 1))));
      }
      else if (inst[1].equals("row")) {
        int row = Integer.parseInt(inst[2].substring(2));
        int by = Integer.parseInt(inst[4]);
        commands.add(new Command(ROTY, row, by));
      }
      else if (inst[1].equals("column")) {
        int col = Integer.parseInt(inst[2].substring(2));
        int by = Integer.parseInt(inst[4]);
        commands.add(new Command(ROTX, col, by));
      }
    }

    return commands;
  }

  public static void main(String[] args) {
    List<String> input = Util.readInput("day8.input");
    System.out.println(part1(input));
  }
}
