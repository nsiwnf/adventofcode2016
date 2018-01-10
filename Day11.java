
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class Day112 {

  private static final byte ZERO = (byte) 0;

  private static final byte FLOOR = (byte) 8;

  private static final byte FOURTH_FLOOR = FLOOR * 3;

  static class Point {
    int level;

    int generators;

    int microchips;

    byte n;

    int steps;

    // List<Long> prev = new ArrayList<>();

    Point() {
    }

    Point(int level, int generators, int microchips, byte n, int steps) {
      this.level = level;
      this.generators = generators;
      this.microchips = microchips;
      this.n = n;
      this.steps = steps;
    }

    boolean getNext(Point next, int newLevel, byte chips, byte rtgs) {
      // valid floor, chips and generators
      if (newLevel > FOURTH_FLOOR || newLevel < 0
          || (microchips >> level & chips) != chips || (generators >> level & rtgs) != rtgs
          // nothing below, no point in taking it further down
          || newLevel < level && ((generators >> level) | (microchips >> level)) == 0) {
        return false;
      }

      byte genNL = (byte) (generators >> newLevel & n | rtgs);
      byte micNL = (byte) (microchips >> newLevel & n | chips);
      if (genNL != 0 && (genNL & micNL) == 0) {
        // none of the RTGs have corresponding microchips, you are dead
        return false;
      }

      if (micNL != 0 && genNL != 0 && (micNL & genNL) != micNL) {
        // there are microchips exposed to incompatible RTGs and are fried
        return false;
      }

      byte genCL = (byte) (generators >> level & n & ~rtgs);
      byte micCL = (byte) (microchips >> level & n & ~chips);
      if (micCL != 0 && genCL != 0 && (micCL & genCL) != micCL) {
        // there are microchips exposed to incompatible RTGs
        return false;
      }

      int floorMask = ~(n << level | n << newLevel);
      int newGenerators = genNL << newLevel | genCL << level | floorMask & generators;
      int newMicrochips = micNL << newLevel | micCL << level | floorMask & microchips;

      next.level = newLevel;
      next.generators = newGenerators;
      next.microchips = newMicrochips;
      next.n = n;
      next.steps = steps + 1;
      // next.prev.clear();
      // next.prev.addAll(prev);
      // next.prev.add((long) generators << 32 | microchips);

      return true;
    }

    @Override
    public String toString() {
      String genbin = Integer.toBinaryString(generators);
      String micbin = Integer.toBinaryString(microchips);
      return level / FLOOR + "[" + genbin + "][" + micbin + "]";
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + level / FLOOR;
      result = 31 * result + generators;
      result = 31 * result + microchips;
      return result;
    }
  }

  private static int part1(byte[] elements, String[] elementStrings, int generators, int microchips) {
    int all = 0;
    for (byte e : elements) {
      all |= e;
    }
    Queue<Point> points = new ArrayBlockingQueue<>(500000);
    Point start = new Point(0, generators, microchips, (byte) all, 0);
    points.add(start);

    Set<Integer> visited = new HashSet<>();
    visited.add(start.hashCode());

    all = all << FOURTH_FLOOR;

    while (!points.isEmpty()) {
      Point p = points.poll();

      // System.out.println(printPoint(elements, elementStrings, p));
      if (p.generators == all && p.microchips == all) {
        // printSteps(elements, elementStrings, p.prev);
        return p.steps;
      }

      for (int i = 0; i < elements.length; i++) {
        Point next = new Point();
        // take just this chip
        next = addNext(p, next, elements[i], ZERO, visited, points);

        // take this chip and its gen
        next = addNext(p, next, elements[i], elements[i], visited, points);

        // take just this generator
        next = addNext(p, next, ZERO, elements[i], visited, points);

        for (int j = i + 1; j < elements.length; j++) {
          // take this chip and another chip
          next = addNext(p, next, (byte) (elements[i] | elements[j]), ZERO, visited, points);

          // take this gen and another gen
          next = addNext(p, next, ZERO, (byte) (elements[i] | elements[j]), visited, points);

          // take this chip and another gen
          next = addNext(p, next, elements[i], elements[j], visited, points);

          // take this gen and another chip
          next = addNext(p, next, elements[j], elements[i], visited, points);
        }
      }
    }

    return -1;
  }

  private static Point addNext(Point current, Point next, byte chips, byte rtgs, Set<Integer> visited,
      Queue<Point> points) {
    if (current.getNext(next, current.level - FLOOR, chips, rtgs) && visited.add(next.hashCode())) {
      points.add(next);
      next = new Point();
    }
    if (current.getNext(next, current.level + FLOOR, chips, rtgs) && visited.add(next.hashCode())) {
      points.add(next);
      next = new Point();
    }
    return next;
  }

  private static String printPoint(byte[] elements, String[] elementStrings, Point p) {
    StringBuilder b = new StringBuilder();
    for (int i = 3; i >= 0; i--) {
      b.append('F').append(i + 1).append(' ');
      b.append(p.level == i ? 'E' : '.');
      b.append("   ");

      for (int i1 = 0; i1 < elements.length; i1++) {
        byte e = elements[i1];
        if ((p.generators >> FLOOR * i & e) == e) {
          b.append(elementStrings[i1]).append("G ");
        }
        else {
          b.append(".   ");
        }
        if ((p.microchips >> FLOOR * i & e) == e) {
          b.append(elementStrings[i1]).append("M ");
        }
        else {
          b.append(".   ");
        }
      }

      b.append('\n');
    }
    return b.toString();
  }

  private static void printSteps(byte[] elements, String[] elementStrings, List<Long> steps) {
    StringBuilder b = new StringBuilder();
    for (long step : steps) {
      b.setLength(0);
      int generators = (int) (step >> 32);
      int microchips = (int) step;
      for (int i = FOURTH_FLOOR; i >= 0; i -= FLOOR) {
        b.append('F').append(i / FLOOR + 1).append(' ');
        // b.append(p.level == i ? 'E' : '.');
        b.append(". ");

        for (int i1 = 0; i1 < elements.length; i1++) {
          byte e = elements[i1];
          if ((generators >> i & e) == e) {
            b.append(elementStrings[i1]).append("G ");
          }
          else {
            b.append(".   ");
          }
          if ((microchips >> i & e) == e) {
            b.append(elementStrings[i1]).append("M ");
          }
          else {
            b.append(".   ");
          }
        }

        b.append('\n');
      }
      System.out.println(b.toString());
    }
  }

  public static void main(String[] args) {
    System.out
        .println(part1(new byte[] { 0b1, 0b10 }, new String[] { "H ", "L " }, 0b0_10_00000001_00000000, 0b0_0_0_11));

    /**
     * The first floor contains a PmG and a PmM <br/>
     * The second floor contains a CoG, a CmG, a RuG, and a PuG. <br/>
     * The third floor contains a CoM, a CmM, a RuM, and a PuM. <br/>
     * The fourth floor contains nothing relevant. <br/>
     */
    // Promethium
    final byte Pm = 0x01;

    // Cobalt
    final byte Co = 0x02;

    // Curium
    final byte Cm = 0x04;

    // Ruthenium
    final byte Ru = 0x08;

    // Plutonium
    final byte Pu = 0x10;

    byte[] elements = { Pm, Co, Cm, Ru, Pu };
    String[] elementStrings = { "Pm", "Co", "Cm", "Ru", "Pu" };
    System.out.println(part1(elements, elementStrings, 0b0_0_11110_00000001, 0b0_11110_00000000_00000001));

    // Elerium
    final byte El = 0x20;

    // Dilithium
    final byte Di = 0x40;
    elements = new byte[] { Pm, Co, Cm, Ru, Pu, El, Di };
    elementStrings = new String[] { "Pm", "Co", "Cm", "Ru", "Pu", "El", "Di" };
    long start = System.currentTimeMillis();
    System.out.println(part1(elements, elementStrings, 0b0_0_11110_01100001,
        0b0_11110_00000000_01100001));
    System.out.println(System.currentTimeMillis() - start);
  }
}
