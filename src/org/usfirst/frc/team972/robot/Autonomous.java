package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {

	public static void autonomousLowBar() {
		double leftDriveSpeed, rightDriveSpeed;

		if (Robot.rightDriveEncoder.get() >= RobotMap.LOW_BAR_RIGHT_DRIVE_DISTANCE) {
			rightDriveSpeed = 0;
		} else {
			rightDriveSpeed = RobotMap.LOW_BAR_RIGHT_DRIVE_SPEED;
		}
		if (Robot.leftDriveEncoder.get() >= RobotMap.LOW_BAR_LEFT_DRIVE_DISTANCE) {
			leftDriveSpeed = 0;
		} else {
			leftDriveSpeed = RobotMap.LOW_BAR_LEFT_DRIVE_SPEED;
		}
		Robot.botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
	}

	public static void autonomousDriveOverDefense() { // TODO remove

	}

	// public static void autonomousChevalDeFrise() {
	// = System.currentTimeMillis();
	// if (
	// )
	// }

	public static void autonomousPortcullis() {

	}

	public static void autonomousDelay(int millis) {
		long start = System.currentTimeMillis();
		while (!(System.currentTimeMillis() >= start + millis))
			;
		// While the time is not complete, do nothing
		// Therefore, there should be a semicolon at the end of the statement
	}
}
