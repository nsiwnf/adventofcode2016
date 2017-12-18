
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day20 {

  static long part1(List<String> input) {
    long lowest = 0;
    input.sort(Comparator.comparingLong(o -> Long.parseLong(o.substring(0, o.indexOf('-')))));
    for (String l : input) {
      int hyphen = l.indexOf('-');
      Integer a = Integer.parseInt(l.substring(0, hyphen));
      Integer b = Integer.parseInt(l.substring(hyphen + 1));
      if (lowest >= a) {
        if(lowest <= b) {
          lowest = b + 1;
        }
      }
      else {
        return lowest;
      }
    }
    return lowest;
  }

  static class IPGroup {
    long lowest;
    long highest;

    public IPGroup(long lowest, long highest) {
      this.lowest = lowest;
      this.highest = highest;
    }
  }

  static long part2(List<String> input) {
    List<IPGroup> blacklist = new ArrayList<>();
    input.sort(Comparator.comparingLong(o -> Long.parseLong(o.substring(0, o.indexOf('-')))));
    for (String l : input) {
      int hyphen = l.indexOf('-');
      Long a = Long.parseLong(l.substring(0, hyphen));
      Long b = Long.parseLong(l.substring(hyphen + 1));

      boolean added = false;
      for(IPGroup r : blacklist) {
        if(a >= r.lowest-1 && a <= r.highest+1) {
          r.highest = Math.max(r.highest, b);
          added = true;
        }
        if(b >= r.lowest-1 && b <= r.highest+1) {
          r.lowest = Math.min(r.lowest, a);
          added = true;
        }
      }

      if(!added) {
        blacklist.add(new IPGroup(a, b));
      }
    }

    long ipCount = 0;
    for (int i = 0; i < blacklist.size()-1; i++) {
      IPGroup rLow = blacklist.get(i);
      IPGroup rHigh = blacklist.get(i+1);

      ipCount += (rHigh.lowest - rLow.highest) - 1;
    }

    return ipCount;
  }

  public static void main(String[] args) {
    List<String> input = Util.readInput("day20.input");
//    input = Arrays.asList("0-2", "4-7", "5-8");
//    System.out.println(part1(input));
    System.out.println(part2(input)); // 202 too high
  }
}
