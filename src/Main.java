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
    public static double y1 = 100300;

    public static double x2 = 0;
    public static double y2 = 100300;

    public static double x3 = -48000;
    public static double y3 = 100300;

    // public static double x4 = 65.31;
    // public static double y4 = 118.5;
    //
    // public static double x5 = 10;
    // public static double y5 = 87.3;
    //
    // public static double x6 = 11;
    // public static double y6 = 166.8;
    //
    // public static double x7 = -29.7;
    // public static double y7 = 103.24;

    public static double m_cx;
    public static double m_bx;
    public static double m_ax;
    public static double m_cy;
    public static double m_by;
    public static double m_ay;

    public static ArrayList<Double> xPoints;
    public static ArrayList<Double> yPoints;
    public static ArrayList<Double> headings;
    public static ArrayList<Double> arcLength;

    public static double m_distance = 0;

    public static void main(String[] args) {
	BezierCurve bezier = new BezierCurve(x0, y0, x1, y1, x2, y2, x3, y3);
	bezier.calculateBezier();
	xPoints = bezier.getXPoints();
	yPoints = bezier.getYPoints();
	headings = bezier.getHeading();
	arcLength = bezier.getArcLength();

	CSVFileWriter pathGrapher = new CSVFileWriter("path.csv", "X Points", xPoints, "Y Points", yPoints);
	pathGrapher.writeToFile();
	System.out.println("path graphed");

	CSVFileWriter robotValues = new CSVFileWriter("robotValues.csv", "Heading", headings, "Arc Length", arcLength);
	robotValues.writeToFile();
	System.out.println("robot values found");

	System.out.println("total arc length: " + arcLength.get(arcLength.size() - 1));

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
	//
	// System.out.println("total arc length: " + arcLength.get(arcLength.size() -
	// 1));
    }

}