import java.util.Arrays;
import java.util.List;

public class Day3 {

  public static int part2(List<String> input) {
    int[][] triangles = new int[3][3];
    int count = 0;
    for (int i = 0; i < input.size(); i += 3) {
      for (int k = 0; k < 3; k++) {
        String row = input.get(i+k);
        int[] tris = Arrays.stream(row.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        for (int j = 0; j < 3; j++) {
          triangles[j][k] = tris[j];
        }
      }

      for (int k = 0; k < 3; k++) {
        Arrays.sort(triangles[k]);
        if (triangles[k][0] + triangles[k][1] > triangles[k][2]) {
          count++;
        }
      }
    }
    return count;
  }

  public static void main(String[] args) {
    List<String> input = Util.readInput("day3.input");
    System.out.println(part2(input));
  }
}
