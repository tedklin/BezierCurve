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

    public static double x0 = 1.367;
    public static double y0 = 2.82;

    public static double x1 = 1.35;
    public static double y1 = 1.837;

    public static double x2 = 2.007;
    public static double y2 = 2.36;

    public static double x3 = 2.01;
    public static double y3 = 1.316;

    public static double x4 = 74403.7644;
    public static double y4 = 134999.94;

    public static double x5 = 0;
    public static double y5 = 86582.24;

    public static double x6 = 0;
    public static double y6 = 198227.76;

    public static double x7 = -33835.428;
    public static double y7 = 117615.1376;

    // public static double kRotPBezier = 0.05;
    public static double kMaxStraightPower = 0.8;
    public static double kMinStraightPower = 0.25;
    // public static double kDistPBezier = 0.03; // inches
    // public static double kDistPBezier = 0.0005; // ticks
    public static double kCurvatureFunction = 85; // keep this under 150

    public static double m_cx;
    public static double m_bx;
    public static double m_ax;
    public static double m_cy;
    public static double m_by;
    public static double m_ay;

    public static double m_distance = 0;

    public static void main(String[] args) {
	// BezierCurve bezier1 = new BezierCurve(x0, y0, x1, y1, x2, y2, x3, y3);
	// BezierCurve bezier2 = new BezierCurve(x4, y4, x5, y5, x6, y6, x7, y7);
	// System.out.println("bezier generated");

	try {
	    // sampleRobotValues(bezier1, "samplePath1.csv", -0.687);
	    // sampleRobotValues(bezier2, "samplePath2.csv", 0.687);
	    sampleRobotValues(AutoConstants.kRedCenterToRightSwitchPath, "_center_to_right_switch.csv", 0.7, 0.05,
		    0.0005, true);
	    sampleRobotValues(AutoConstants.kRedCenterToRightMidPath, "_center_to_right_mid.csv", -0.7, 0.05, 0.005,
		    false);
	    sampleRobotValues(AutoConstants.kRedRightMidToRightScalePath, "_right_mid_right_scale.csv", -0.7, 0.05,
		    0.0001, true);
	    sampleRobotValues(AutoConstants.kRedRightScaleToCubePath, "_right_scale_to_cube.csv", 0.5, 0.05, 0.005,
		    true);

	    System.out.println("path generated");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * sample robot values; TODO: cleanup, this is bad practice
     * 
     * @param bezierCurve
     * @param name
     * @throws IOException
     */
    public static void sampleRobotValues(BezierCurve bezierCurve, String name, double straightPower, double kRotPBezier,
	    double kDistPBezier, boolean softStop) throws IOException {
	bezierCurve.calculateBezier();

	ArrayList<Double> xPoints = bezierCurve.getXPoints();
	ArrayList<Double> yPoints = bezierCurve.getYPoints();
	ArrayList<Double> heading = bezierCurve.getHeading();
	ArrayList<Double> arcLengths = bezierCurve.getArcLength();

	System.out.println("initial heading: " + heading.get(1));
	System.out.println("final heading: " + heading.get(heading.size() - 1));

	int counter = 1;

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
	writer.append("Delta Heading");
	writer.append(",");
	writer.append("Delta Arc Length");
	writer.append(",");
	writer.append("Curvature");
	writer.append(",");
	writer.append("Left Power");
	writer.append(",");
	writer.append("Right Power");
	writer.append(",");
	writer.append("Left Adjusted Power");
	writer.append(",");
	writer.append("Right Adjusted Power");
	writer.append("\r");

	for (counter = 1; counter < heading.size(); counter++) {
	    double direction = Math.signum(straightPower);
	    double robotAngle = (360 - heading.get(counter - 1)) % 360;
	    double desiredHeading = heading.get(counter);

	    // remember this goes into the robot code
	    // if (direction > 0) {
	    // desiredHeading += 180;
	    // }

	    desiredHeading = -desiredHeading; // This is always necessary because of how our rotational PID is
					      // structured.
	    double rotError = desiredHeading - robotAngle;
	    rotError = (rotError > 180) ? rotError - 360 : rotError;
	    rotError = (rotError < -180) ? rotError + 360 : rotError;
	    double rotPower = kRotPBezier * rotError;

	    double straightError = arcLengths.get(arcLengths.size() - 1) - arcLengths.get(counter); // theoretical
	    double maxStraightPower = kMaxStraightPower; // default

	    if (softStop) {
		double newMaxStraightPower = kDistPBezier * straightError;
		maxStraightPower = Math.min(Math.abs(maxStraightPower), Math.abs(newMaxStraightPower));
	    }

	    if (Math.abs(straightPower) > maxStraightPower) {
		straightPower = maxStraightPower * direction;
	    }
	    if (Math.abs(straightPower) < kMinStraightPower) {
		straightPower = kMinStraightPower * direction;
	    }

	    double leftPower = straightPower + rotPower;
	    double rightPower = straightPower - rotPower;

	    double deltaHeading = heading.get(counter) - heading.get(counter - 1);
	    double deltaSegmentLength = arcLengths.get(counter) - arcLengths.get(counter - 1);
	    double curvature = Math.abs(rotError / deltaSegmentLength);
	    double adjustedStraightPower = (kMaxStraightPower - kCurvatureFunction * curvature) * direction;

	    if (Math.abs(adjustedStraightPower) > maxStraightPower) {
		adjustedStraightPower = maxStraightPower * direction;
	    }
	    if (Math.abs(adjustedStraightPower) < kMinStraightPower) {
		adjustedStraightPower = kMinStraightPower * direction;
	    }

	    double adjustedLeftPower = adjustedStraightPower + rotPower;
	    double adjustedRightPower = adjustedStraightPower - rotPower;

	    writer.append(String.valueOf(xPoints.get(counter)));
	    writer.append(",");
	    writer.append(String.valueOf(yPoints.get(counter)));
	    writer.append(",");
	    writer.append(String.valueOf(heading.get(counter)));
	    writer.append(",");
	    writer.append(String.valueOf(arcLengths.get(counter)));
	    writer.append(",");
	    writer.append(String.valueOf(deltaHeading));
	    writer.append(",");
	    writer.append(String.valueOf(deltaSegmentLength));
	    writer.append(",");
	    writer.append(String.valueOf(curvature));
	    writer.append(",");
	    writer.append(String.valueOf(leftPower));
	    writer.append(",");
	    writer.append(String.valueOf(rightPower));
	    writer.append(",");
	    writer.append(String.valueOf(adjustedLeftPower));
	    writer.append(",");
	    writer.append(String.valueOf(adjustedRightPower));
	    writer.append("\r");
	}

	writer.flush();
	writer.close();
    }

}