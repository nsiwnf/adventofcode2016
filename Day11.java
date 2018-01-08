import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class Day11 {

  // Promethium
  private static final byte Pm = 0x01;

  // Cobalt
  private static final byte Co = 0x02;

  // Curium
  private static final byte Cm = 0x04;

  // Ruthenium
  private static final byte Ru = 0x08;

  // Plutonium
  private static final byte Pu = 0x10;

  static final byte[] ELEMS = { Pm, Co, Cm, Ru, Pu };
  static final String[] ELEM_STR = { "Pm", "Co", "Cm", "Ru", "Pu" };

  static final byte ZERO = (byte) 0;

  static class Point {

    int level;

    byte[] generators;

    byte[] microchips;

    int steps;

    Point(int level, byte[] generators, byte[] microchips, int steps) {
      this.level = level;
      this.generators = generators;
      this.microchips = microchips;
      this.steps = steps;
    }

    Point getNext(int newLevel, byte chip1, byte chip2, byte rtg1, byte rtg2) {
      if (newLevel > 3 || newLevel < 0) {
        return null;
      }

      byte newGen = (byte) (generators[newLevel] | rtg1 | rtg2);
      byte newMic = (byte) (microchips[newLevel] | chip1 | chip2);
      if (newGen != 0 && (newGen & newMic) == 0) {
        // none of the RTGs have corresponding microchips, you are dead
        return null;
      }

      if (newMic != 0) {
        // there are microchips on this level
        if (newGen != 0) {
          // there are generators on this level
          if ((newMic & newGen) != newMic) {
            // there are microchips exposed to incompatible RTGs and are fried
            return null;
          }
        }
      }

      byte microchip = (byte) (microchips[level] & ~chip1 & ~chip2);
      byte generator = (byte) (generators[level] & ~rtg1 & ~rtg2);
      if (microchip != 0) {
        // there are microchips on this level
        if (generator != 0) {
          // there are generators on this level
          if ((microchip & generator) != microchip) {
            // there are microchips exposed to incompatible RTGs
            return null;
          }
        }
      }

      byte[] newGenerators = Arrays.copyOf(generators, generators.length);
      newGenerators[newLevel] = newGen;
      newGenerators[level] = generator;
      byte[] newMicrochips = Arrays.copyOf(microchips, microchips.length);
      newMicrochips[newLevel] = newMic;
      newMicrochips[level] = microchip;
      return (new Point(newLevel, newGenerators, newMicrochips, steps + 1));
    }

    @Override
    public String toString() {
      String genbin = Integer.toBinaryString(generators[0]) + ", " + Integer.toBinaryString(generators[1]) + ", "
          + Integer.toBinaryString(generators[2]) + ", " + Integer.toBinaryString(generators[3]);
      String micbin = Integer.toBinaryString(microchips[0]) + ", " + Integer.toBinaryString(microchips[1]) + ", "
          + Integer.toBinaryString(microchips[2]) + ", " + Integer.toBinaryString(microchips[3]);
      return level + "[" + genbin + "][" + micbin + "]";
    }
  }

  private static int part1() {
    final byte all = 0b11111;
    Queue<Point> points = new ArrayBlockingQueue<Point>(100000);
    Point start = new Point(0, new byte[] { Pm, Co | Cm | Ru | Pu, 0, 0 }, new byte[] { Pm, 0, Co | Cm | Ru | Pu, 0 }, 0);
    points.add(start);
    Set<String> visited = new HashSet<>();
     visited.add(start.toString());

    while (!points.isEmpty()) {
      Point p = points.poll();

        System.out.println(printPoint(p));
        if (p.generators[3] == all && p.microchips[3] == all) {
          return p.steps;
        }
        else if (minSteps > p.steps) {
          for (int i = 0; i < ELEMS.length; i++) {
            if ((p.microchips[p.level] & ELEMS[i]) != 0) {
              // take just this chip
              Point next = p.getNext(p.level - 1, ELEMS[i], ZERO, ZERO, ZERO);
              if (next != null && visited.add(next.toString())) {
                points.add(next);
              }
              next = p.getNext(p.level + 1, ELEMS[i], ZERO, ZERO, ZERO);
              if (next != null) {
                points.add(next);
              }

              // take this chip and another chip
              for (int j = i + 1; j < ELEMS.length; j++) {
                if ((p.microchips[p.level] & ELEMS[j]) != 0) {
                  next = p.getNext(p.level - 1, ELEMS[i], ELEMS[j], ZERO, ZERO);
                  if (next != null && visited.add(next.toString())) {
                    points.add(next);
                  }
                  next = p.getNext(p.level + 1, ELEMS[i], ELEMS[j], ZERO, ZERO);
                  if (next != null && visited.add(next.toString())) {
                    points.add(next);
                  }
                }
              }

              // take this chip and its gen
              if ((p.generators[p.level] & ELEMS[i]) != 0) {
                next = p.getNext(p.level - 1, ELEMS[i], ZERO, ELEMS[i], ZERO);
                if (next != null && visited.add(next.toString())) {
                  points.add(next);
                }
                next = p.getNext(p.level + 1, ELEMS[i], ZERO, ELEMS[i], ZERO);
                if (next != null && visited.add(next.toString())) {
                  points.add(next);
                }
              }
            }

            if ((p.generators[p.level] & ELEMS[i]) != 0) {
              // take just this generator
              Point next = p.getNext(p.level - 1, ZERO, ZERO, ELEMS[i], ZERO);
              if (next != null && visited.add(next.toString())) {
                points.add(next);
              }
              next = p.getNext(p.level + 1, ZERO, ZERO, ELEMS[i], ZERO);
              if (next != null && visited.add(next.toString())) {
                points.add(next);
              }

              // take this generator and another generator
              for (int j = i + 1; j < ELEMS.length; j++) {
                if ((p.generators[p.level] & ELEMS[j]) != 0) {
                  next = p.getNext(p.level - 1, ZERO, ZERO, ELEMS[i], ELEMS[j]);
                  if (next != null && visited.add(next.toString())) {
                    points.add(next);
                  }
                  next = p.getNext(p.level + 1, ZERO, ZERO, ELEMS[i], ELEMS[j]);
                  if (next != null && visited.add(next.toString())) {
                    points.add(next);
                  }
                }
              }
            }
          }
        }
      }

    return -1;
  }

  static String printPoint(Point p) {
    StringBuilder b = new StringBuilder();
    for (int i = 3; i >= 0; i--) {
      b.append('F').append(i + 1).append(' ');
      b.append(p.level == i ? 'E' : '.');
      b.append("   ");

      for (int i1 = 0; i1 < ELEMS.length; i1++) {
        byte e = ELEMS[i1];
        if ((p.generators[i] & e) == e) {
          b.append(ELEM_STR[i1]).append("G ");
        }
        else {
          b.append(".   ");
        }
        if ((p.microchips[i] & e) == e) {
          b.append(ELEM_STR[i1]).append("M ");
        }
        else {
          b.append(".   ");
        }
      }

      b.append('\n');
    }
    return b.toString();
  }


  public static void main(String[] args) {
    /**
     * The first floor contains a PmG and a PmM <br/>
     * The second floor contains a CoG, a CmG, a RuG, and a PuG. <br/>
     * The third floor contains a CoM, a CmM, a RuM, and a PuM. <br/>
     * The fourth floor contains nothing relevant. <br/>
     */
    System.out.println(part1());
  }
}
