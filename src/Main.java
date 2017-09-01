import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Mirror of Bezier Curve generation in NerdyDrive. A quick tool to check Bezier
 * Curve values in Excel before uploading to Robot.
 * 
 * @author tedlin
 *
 */

public class Main {

    public static double x0 = 0;
    public static double y0 = 0;

    public static double x1 = 0;
    public static double y1 = 81;

    public static double x2 = 0;
    public static double y2 = 81;

    public static double x3 = 65.31;
    public static double y3 = 118.5;

    public static double x4 = 65.31;
    public static double y4 = 118.5;

    public static double x5 = 0;
    public static double y5 = 76;

    public static double x6 = 0;
    public static double y6 = 174;

    public static double x7 = -29.7;
    public static double y7 = 103.24;

    public static double kRotPBezier = 0.03;
    public static double kStraightPowerAdjuster = 0.5;
    public static double kMaxStraightPower = 0.75;

    public static double m_cx;
    public static double m_bx;
    public static double m_ax;
    public static double m_cy;
    public static double m_by;
    public static double m_ay;

    // public static ArrayList<Double> xPoints;
    // public static ArrayList<Double> yPoints;
    // public static ArrayList<Double> headings;
    // public static ArrayList<Double> arcLength;

    public static double m_distance = 0;

    public static void main(String[] args) {
	BezierCurve bezier1 = new BezierCurve(x0, y0, x1, y1, x2, y2, x3, y3);
	BezierCurve bezier2 = new BezierCurve(x4, y4, x5, y5, x6, y6, x7, y7);
	System.out.println("bezier generated");

	try {
	    sampleRobotValues(bezier1, "samplePath1.csv", 0.687);
	    sampleRobotValues(bezier2, "samplePath2.csv", -0.687);

	    System.out.println("path generated");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// bezier.calculateBezier();
	// xPoints = bezier.getXPoints();
	// yPoints = bezier.getYPoints();
	// headings = bezier.getHeading();
	// arcLength = bezier.getArcLength();
	//
	// CSVFileWriter pathGrapher = new CSVFileWriter("path.csv", "X Points",
	// xPoints, "Y Points", yPoints);
	// pathGrapher.writeToFile();
	// System.out.println("path graphed");
	//
	// CSVFileWriter robotValues = new CSVFileWriter("robotValues.csv", "Heading",
	// headings, "Arc Length", arcLength);
	// robotValues.writeToFile();
	// System.out.println("robot values found");
	//
	// try {
	// sampleRobotValues(bezier, "sampleRobot.csv");
	// System.out.println("sample robot values found");
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// System.out.println("total arc length: " + arcLength.get(arcLength.size() -
	// 1));

	// BezierCurve bezier2 = new BezierCurve(x4, y4, x5, y5, x6, y6, x7, y7);
	// bezier2.calculateBezier();
	// xPoints = bezier2.getXPoints();
	// yPoints = bezier2.getYPoints();
	// headings = bezier2.getHeading();
	// arcLength = bezier2.getArcLength();
	//
	// CSVFileWriter pathGrapher2 = new CSVFileWriter("path2.csv", "X Points",
	// xPoints, "Y Points", yPoints);
	// pathGrapher2.writeToFile();
	// System.out.println("path graphed");
	//
	// CSVFileWriter robotValues2 = new CSVFileWriter("robotValues2.csv", "Heading",
	// headings, "Arc Length",
	// arcLength);
	// robotValues2.writeToFile();
	// System.out.println("robot values found");

	// try {
	// sampleRobotValues(bezier, "sampleRobot2.csv");
	// System.out.println("sample robot values found");
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// System.out.println("total arc length: " + arcLength.get(arcLength.size() -
	// 1));
    }

    /**
     * sample robot values; TODO: cleanup, this is bad practice
     * 
     * @param bezierCurve
     * @param name
     * @throws IOException
     */
    public static void sampleRobotValues(BezierCurve bezierCurve, String name, double straightPower)
	    throws IOException {
	bezierCurve.calculateBezier();

	ArrayList<Double> xPoints = bezierCurve.getXPoints();
	ArrayList<Double> yPoints = bezierCurve.getYPoints();
	ArrayList<Double> heading = bezierCurve.getHeading();
	ArrayList<Double> arcLengths = bezierCurve.getArcLength();

	System.out.println("initial heading: " + heading.get(1));
	System.out.println("final heading: " + heading.get(heading.size() - 1));

	int counter = 1;
	double basePower = 1; // always equal to 1

	FileWriter writer = new FileWriter("generated_bezier/" + name);
	// CSV file headings
	writer.append("X Points");
	writer.append(",");
	writer.append("Y Points");
	writer.append(",");
	writer.append("Heading");
	writer.append(",");
	writer.append("Arc Length");
	writer.append(",");
	writer.append("Left Power");
	writer.append(",");
	writer.append("Right Power");
	writer.append("\r");

	for (counter = 1; counter < heading.size(); counter++) {
	    double headingError = heading.get(counter) - heading.get(counter - 1);
	    double rotPower = kRotPBezier * headingError;
	    double sign = Math.signum(straightPower);
	    // comment out next line to disable straight power adjuster
	    straightPower = sign * basePower / (Math.abs(headingError) * kStraightPowerAdjuster);

	    if (Math.abs(straightPower) > kMaxStraightPower) {
		straightPower = kMaxStraightPower * sign;
	    }

	    double leftPower = straightPower + rotPower;
	    double rightPower = straightPower - rotPower;

	    writer.append(String.valueOf(xPoints.get(counter)));
	    writer.append(",");
	    writer.append(String.valueOf(yPoints.get(counter)));
	    writer.append(",");
	    writer.append(String.valueOf(heading.get(counter)));
	    writer.append(",");
	    writer.append(String.valueOf(arcLengths.get(counter)));
	    writer.append(",");
	    writer.append(String.valueOf(leftPower));
	    writer.append(",");
	    writer.append(String.valueOf(rightPower));
	    writer.append("\r");
	}

	writer.flush();
	writer.close();
    }

}