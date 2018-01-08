
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class Day11 {

  static final byte ZERO = (byte) 0;

  static class Point {

    int level;

    byte[] generators;

    byte[] microchips;

    int steps;

//    List<String> prev = new ArrayList<>();

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

      Point next = new Point(newLevel, newGenerators, newMicrochips, steps + 1);
//      next.prev.addAll(prev);
//      next.prev.add(prev.toString());

      return next;
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

  private static int part1(byte[] elements, String[] elementStrings, byte[] generators, byte[] microchips) {
    byte all = 0;
    for (byte e : elements) {
      all |= e;
    }
    Queue<Point> points = new ArrayBlockingQueue<Point>(5000000);
    Point start = new Point(0, generators, microchips, 0);
    points.add(start);
    Set<String> visited = new HashSet<>();
    visited.add(start.toString());

    while (!points.isEmpty()) {
      Point p = points.poll();

//      System.out.println(printPoint(elements, elementStrings, p));
      if (p.generators[3] == all && p.microchips[3] == all) {
        return p.steps;
      }

      for (int i = 0; i < elements.length; i++) {
        if ((p.microchips[p.level] & elements[i]) != 0) {
          // take just this chip
          Point next = p.getNext(p.level - 1, elements[i], ZERO, ZERO, ZERO);
          if (next != null && visited.add(next.toString())) {
            points.add(next);
          }
          next = p.getNext(p.level + 1, elements[i], ZERO, ZERO, ZERO);
          if (next != null) {
            points.add(next);
          }

          // take this chip and another chip
          for (int j = i + 1; j < elements.length; j++) {
            if ((p.microchips[p.level] & elements[j]) != 0) {
              next = p.getNext(p.level - 1, elements[i], elements[j], ZERO, ZERO);
              if (next != null && visited.add(next.toString())) {
                points.add(next);
              }
              next = p.getNext(p.level + 1, elements[i], elements[j], ZERO, ZERO);
              if (next != null && visited.add(next.toString())) {
                points.add(next);
              }
            }
          }

          // take this chip and its gen
          if ((p.generators[p.level] & elements[i]) != 0) {
            next = p.getNext(p.level - 1, elements[i], ZERO, elements[i], ZERO);
            if (next != null && visited.add(next.toString())) {
              points.add(next);
            }
            next = p.getNext(p.level + 1, elements[i], ZERO, elements[i], ZERO);
            if (next != null && visited.add(next.toString())) {
              points.add(next);
            }
          }
        }

        if ((p.generators[p.level] & elements[i]) != 0) {
          // take just this generator
          Point next = p.getNext(p.level - 1, ZERO, ZERO, elements[i], ZERO);
          if (next != null && visited.add(next.toString())) {
            points.add(next);
          }
          next = p.getNext(p.level + 1, ZERO, ZERO, elements[i], ZERO);
          if (next != null && visited.add(next.toString())) {
            points.add(next);
          }

          // take this generator and another generator
          for (int j = i + 1; j < elements.length; j++) {
            if ((p.generators[p.level] & elements[j]) != 0) {
              next = p.getNext(p.level - 1, ZERO, ZERO, elements[i], elements[j]);
              if (next != null && visited.add(next.toString())) {
                points.add(next);
              }
              next = p.getNext(p.level + 1, ZERO, ZERO, elements[i], elements[j]);
              if (next != null && visited.add(next.toString())) {
                points.add(next);
              }
            }
          }
        }
      }
    }

    return -1;
  }

  private static String printPoint(byte[] elements, String[] elementStrings, Point p) {
    StringBuilder b = new StringBuilder();
    for (int i = 3; i >= 0; i--) {
      b.append('F').append(i + 1).append(' ');
      b.append(p.level == i ? 'E' : '.');
      b.append("   ");

      for (int i1 = 0; i1 < elements.length; i1++) {
        byte e = elements[i1];
        if ((p.generators[i] & e) == e) {
          b.append(elementStrings[i1]).append("G ");
        }
        else {
          b.append(".   ");
        }
        if ((p.microchips[i] & e) == e) {
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

  public static void main(String[] args) {
//    System.out.println(part1(new byte[]{0b1, 0b10}, new String[]{"H ", "L "}, new byte[] { 0, 0b0001, 0b0010, 0 }, new byte[] { 0b0011, 0, 0, 0 }));

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
    System.out.println(part1(elements, elementStrings, new byte[] { Pm, Co | Cm | Ru | Pu, 0, 0 },
        new byte[] { Pm, 0, Co | Cm | Ru | Pu, 0 }));

    // Elerium
    final byte El = 0x20;

    // Dilithium
    final byte Di = 0x40;
    elements = new byte[] { Pm, Co, Cm, Ru, Pu, El, Di };
    elementStrings = new String[] { "Pm", "Co", "Cm", "Ru", "Pu", "El", "Di" };
    System.out.println(part1(elements, elementStrings, new byte[] { Pm | El | Di, Co | Cm | Ru | Pu, 0, 0 },
        new byte[] { Pm | El | Di, 0, Co | Cm | Ru | Pu, 0 }));
  }
}
