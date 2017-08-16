import java.util.ArrayList;

/**
 * Mirror of Bezier Curve generation in NerdyDrive. A quick tool to check Bezier
 * Curve values in Excel before uploading to Robot.
 * 
 * @author tedfoodlin
 *
 */

public class Main {

    public static double x0 = 0;
    public static double y0 = 0;

    public static double x1 = 0;
    public static double y1 = 4;

    public static double x2 = 0;
    public static double y2 = 4;

    public static double x3 = 3;
    public static double y3 = 4;

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
    }

}