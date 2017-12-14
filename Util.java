
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {

  static List<String> readInput(String fileName)  {
    List<String> result = new ArrayList<>();

    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(fileName));
      String line;
      while((line = in.readLine()) != null) {
        result.add(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if(in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return result;
  }
}
