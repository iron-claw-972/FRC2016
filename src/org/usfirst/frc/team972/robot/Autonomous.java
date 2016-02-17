package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {

	public static boolean autonomousDelay(long start, int millis) {
		return ((System.currentTimeMillis() >= start + millis));
	}
	
	public static boolean autonomousDriveOverDefense(int distance) {
		return true;
	}
	
	public static boolean autonomousFlippyThing(boolean flipUp) {
		return true;
	}
	
	
	
}
