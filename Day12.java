import java.util.List;

public class Day12 {

  static int part1(int[] registers, List<String> instructions) {
    int i = 0;
    while (i < instructions.size()) {
      String[] instruction = instructions.get(i).split(" ");
      String command = instruction[0];
      int x = instruction[1].charAt(0);
      if (command.equals("cpy")) {
        if (x >= '0' && x <= '9') {
          x = Integer.parseInt(instruction[1]);
        }
        else {
          x = registers[x - 'a'];
        }
        int r = instruction[2].charAt(0);
        registers[r - 'a'] = x;
      }
      else if (command.equals("inc")) {
        registers[x - 'a']++;
      }
      else if (command.equals("dec")) {
        registers[x - 'a']--;
      }
      else if (x >= '0' && x <= '9' || x >= 'a' && x <= 'd' && registers[x - 'a'] != 0) { // jnz
        int jump = Integer.parseInt(instruction[2]) - 1;
        i += jump;
      }

      i++;
    }

    return registers[0];
  }

  public static void main(String[] args) {

    List<String> input = Util.readInput("day12.input");
    System.out.println(part1(new int[] { 0, 0, 1, 0 }, input));
  }
}
