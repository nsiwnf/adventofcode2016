
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

    static boolean dead(int level, byte[] generators, byte[] microchips) {
      if (level > 3 || level < 0) {
        return true;
      }

      if (generators[level] != 0 && (generators[level] & microchips[level]) == 0) {
        // none of the RTGs have corresponding microchips
        return true;
      }

      for (int i = 0; i < generators.length; i++) {
        if (microchips[i] != 0) {
          // there are microchips on this level
          if (generators[i] != 0) {
            // there are generators on this level
            if ((microchips[i] & generators[i]) != microchips[i]) {
              // there are microchips exposed to incompatible RTGs
              return true;
            }
          }
        }
      }

      return false;
    }

    static boolean dead(int level, int newLevel, byte[] generators, byte[] microchips, byte chip1, byte chip2,
        byte rtg) {
      if (newLevel > 3 || newLevel < 0) {
        return true;
      }

      byte generator = (byte) (generators[newLevel] | rtg);
      byte microchip = (byte) (microchips[newLevel] | chip1 | chip2);
      if (generator != 0 && (generator & microchip) == 0) {
        // none of the RTGs have corresponding microchips
        return true;
      }

      if (microchip != 0) {
        // there are microchips on this level
        if (generator != 0) {
          // there are generators on this level
          if ((microchip & generator) != microchip) {
            // there are microchips exposed to incompatible RTGs
            return true;
          }
        }
      }

      microchip = (byte) (microchips[level] & ~chip1 & ~chip2);
      generator = (byte) (generators[level] & ~rtg);
      if (microchip != 0) {
        // there are microchips on this level
        if (generator != 0) {
          // there are generators on this level
          if ((microchip & generator) != microchip) {
            // there are microchips exposed to incompatible RTGs
            return true;
          }
        }
      }

      return false;
    }

    Point getNext(int newLevel, byte chip1, byte chip2, byte rtg1, byte rtg2) {
      if (newLevel > 3 || newLevel < 0) {
        return null;
      }

      // If theres nothing on lower levels, theres no point in moving anything
      // there
      // if(newLevel < level) {
      // boolean nothing = true;
      // for(int i = 0; i < level; i++) {
      // nothing = nothing && generators[i] == 0 && microchips[i] == 0;
      // }
      // if(nothing) {
      // return null;
      // }
      // }

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

  /**
   * The experimental RTGs have poor radiation containment, so they're
   * dangerously radioactive. The chips are prototypes and don't have normal
   * radiation shielding, but they do have the ability to generate an
   * electromagnetic radiation shield when powered. Unfortunately, they can only
   * be powered by their corresponding RTG. An RTG powering a microchip is still
   * dangerous to other microchips.
   * 
   * In other words, if a chip is ever left in the same area as another RTG, and
   * it's not connected to its own RTG, the chip will be fried. Therefore, it is
   * assumed that you will follow procedure and keep chips connected to their
   * corresponding RTG when they're in the same room, and away from other RTGs
   * otherwise.
   * 
   * These microchips sound very interesting and useful to your current
   * activities, and you'd like to try to retrieve them. The fourth floor of the
   * facility has an assembling machine which can make a self-contained,
   * shielded computer for you to take with you - that is, if you can bring it
   * all of the RTGs and microchips.
   * 
   * Within the radiation-shielded part of the facility (in which it's safe to
   * have these pre-assembly RTGs), there is an elevator that can move between
   * the four floors. Its capacity rating means it can carry at most yourself
   * and two RTGs or microchips in any combination. (They're rigged to some
   * heavy diagnostic equipment - the assembling machine will detach it for
   * you.) As a security measure, the elevator will only function if it contains
   * at least one RTG or microchip. The elevator always stops on each floor to
   * recharge, and this takes long enough that the items within it and the items
   * on that floor can irradiate each other. (You can prevent this if a
   * Microchip and its Generator end up on the same floor in this way, as they
   * can be connected while the elevator is recharging.)
   * 
   * You make some notes of the locations of each component of interest (your
   * puzzle input). Before you don a hazmat suit and start moving things around,
   * you'd like to have an idea of what you need to do.
   * 
   * When you enter the containment area, you and the elevator will start on the
   * first floor.
   * 
   * @return
   */
  private static int part1() {
    int minSteps = Integer.MAX_VALUE;
    final byte all = 0b11111;
    Queue<Point> points = new ArrayBlockingQueue<Point>(100000);
    Point start = new Point(0, new byte[] { Pm, Co | Cm | Ru, 0, 0 }, new byte[] { Pm, 0, Co | Cm | Ru, 0 }, 0);
    points.add(start);
    Set<String> visited = new HashSet<>();
    visited.add(start.toString());

    Point last = null;
    while (!points.isEmpty()) {
      Point p = points.poll();

      if (p.generators[3] == all && p.microchips[3] == all) {
        minSteps = Math.min(minSteps, p.steps);
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
            if (next != null && visited.add(next.toString())) {
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
        last = p;
      }
    }

    return minSteps;
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
