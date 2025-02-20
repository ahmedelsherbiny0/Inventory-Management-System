import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
  public static List<String[]> readCSV(String filePath) throws IOException {
    List<String[]> data = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        data.add(line.split(","));
      }
    }
    return data;
  }

  public static void writeCSV(String filePath, List<String[]> data) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (String[] row : data) {
        writer.write(String.join(",", row));
        writer.newLine();
      }
    }
  }
}
