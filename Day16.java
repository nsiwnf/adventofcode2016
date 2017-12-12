
import java.util.Arrays;

public class Day16 {

  private static final byte[] FLIP = {'1', '0'};

  private static byte[] part1(byte[] input, int size) {
    int aSize = input.length;
    byte[] result = Arrays.copyOf(input, size);
    while(aSize < size) {
      result[aSize] = '0';
      for(int i = 0; i <aSize; i++) {
        int bIdx = 2 * aSize - i;
        if(bIdx < size) {
          result[bIdx] = FLIP[result[i] - '0'];
        }
      }
      aSize = aSize*2 + 1;
    }

    return getChecksum(result);
  }

  private static byte[] getChecksum(byte[] input) {
    int resultLen = input.length;
    while(resultLen%2 == 0) {
      for(int i=0; i<resultLen/2; i++) {
        input[i] = (byte) (input[i*2] == input[i*2+1] ? '1' : '0');
      }
      resultLen /= 2;
    }
    return Arrays.copyOf(input, resultLen);
  }

  public static void main(String[] args) {
    /*
      - Call the data you have at this point "a".
      - Make a copy of "a"; call this copy "b".
      - Reverse the order of the characters in "b".
      - In "b", replace all instances of 0 with 1 and all 1s with 0.
      - The resulting data is "a", then a single 0, then "b".
     */
    
    // Part 1
    System.out.println(new String(part1("01111001100111011".getBytes(), 272)));
    // Part 2
    System.out.println(new String(part1("01111001100111011".getBytes(), 35651584)));
  }
}
