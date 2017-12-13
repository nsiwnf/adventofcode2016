public class Day11 {

  public static int part1(int[] positions, int[] indices) {
    int time = 0;

    boolean canPass = false;
    while (!canPass) {
      canPass = true;
      for (int i = 0; i < positions.length; i++) {
        indices[i] = (indices[i] + 1) % positions[i];
        canPass &= (indices[i] + i) % positions[i] == 0;
      }
      time++;
    }

    return time - 1;
  }


  public static void main(String[] args) {
    /*
     Disc #1 has 17 positions; at time=0, it is at position 5.
     Disc #2 has 19 positions; at time=0, it is at position 8.
     Disc #3 has 7 positions; at time=0, it is at position 1.
     Disc #4 has 13 positions; at time=0, it is at position 7.
     Disc #5 has 5 positions; at time=0, it is at position 1.
     Disc #6 has 3 positions; at time=0, it is at position 0.
     */
    //16824, 3543984
    System.out.println(part1(
        new int[]{17, 19, 7, 13, 5, 3, 11},
        new int[]{5, 8, 1, 7, 1, 0, 0}
        ));
  }
}
