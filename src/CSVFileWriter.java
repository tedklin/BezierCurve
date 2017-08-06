import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * File writer for CSV data 1 x-axis array and 2 y-axis arrays
 * 
 * @author Isaac Addis
 * @author Ronan Konishi
 * 
 */

public class CSVFileWriter {

    private String m_name;
    private static ArrayList<Double> m_xAxisArray;
    private static ArrayList<Double> m_yAxisArray;
    private static String m_title1;
    private static String m_title2;

    /**
     * File writer constructor
     * 
     * @param maxIterations
     * @param xAxisArray
     * @param yAxisArray1
     * @param yAxisArray2
     * @param finalValue
     * @param dist
     */
    public CSVFileWriter(String name, String title1, ArrayList<Double> xAxisArray, String title2,
	    ArrayList<Double> yAxisArray) {
	m_name = name;
	m_xAxisArray = xAxisArray;
	m_yAxisArray = yAxisArray;
	m_title1 = title1;
	m_title2 = title2;
    }

    public void writeToFile() {
	int maxIterations = m_xAxisArray.size();
	try {
	    FileWriter writer = new FileWriter("generated_bezier/" + m_name);

	    // Headings
	    writer.append(m_title1);
	    writer.append(",");
	    writer.append(m_title2);
	    writer.append("\r");

	    // Start for loop
	    for (int j = 0; j <= maxIterations - 1; j++) {
		// Rows
		writer.append(String.valueOf(m_xAxisArray.get(j)));
		writer.append(",");
		writer.append(String.valueOf(m_yAxisArray.get(j)));
		writer.append("\r");
	    }
	    writer.flush();
	    writer.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
