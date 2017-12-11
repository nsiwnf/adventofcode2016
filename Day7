import java.util.List;

public class Day7 {
  // find valid TLS, ex abba and baab
  static int part1(List<String> input) {
    int count = 0;
    for (String row : input) {
      boolean found = false;
      int bracketCount = 0;
      for (int i = 0; i < row.length() - 3; i++) {
        if (row.charAt(i) == '[' || row.charAt(i) == ']') {
          bracketCount++;
        }
        else if (row.charAt(i) == row.charAt(i + 3) && row.charAt(i + 1) == row.charAt(i + 2)
            && row.charAt(i) != row.charAt(i + 1)) {
          if (bracketCount % 2 == 1) {
            found = false;
            break;
          }
          else {
            found = true;
          }
        }
      }
      if (found) {
        count++;
      }
    }
    return count;
  }

  // find valid SSL, ex aba and bab
  static int part2(List<String> input) {
    int count = 0;
    for (String row : input) {
      int i = 0;
      int j = row.indexOf('[') + 1;
      boolean ssl = isSSL(i, j, row);
      if (ssl) {
        count++;
      }
    }
    return count;
  }

  private static boolean isSSL(int i, int j, String row) {
    if (i > row.length() - 3 || j > row.length() - 3) {
      return false;
    }

    boolean validI = row.charAt(i) == row.charAt(i + 2) && row.charAt(i) != row.charAt(i + 1);
    while (!validI) {
      if (i >= row.length() - 3) {
        return false;
      }
      else if (row.charAt(i + 2) == '[') {
        i = row.indexOf(']', i + 2);
        if (i == -1) {
          return false;
        }
      }
      i++;
      validI = row.charAt(i) == row.charAt(i + 2) && row.charAt(i) != row.charAt(i + 1);
    }

    boolean validJ = row.charAt(j) == row.charAt(j + 2) && row.charAt(j) != row.charAt(j + 1);
    while (!validJ) {
      if (j >= row.length() - 3) {
        return false;
      }
      else if (row.charAt(j + 2) == ']') {
        j = row.indexOf('[', j + 2);
        if (j == -1)
          return false;
      }
      j++;
      validJ = row.charAt(j) == row.charAt(j + 2) && row.charAt(j) != row.charAt(j + 1);
    }

    boolean isSSL = row.charAt(i) == row.charAt(j + 1) && row.charAt(i + 1) == row.charAt(j);
    return isSSL || isSSL(i + 1, j, row) || isSSL(i, j + 1, row);
  }
}
