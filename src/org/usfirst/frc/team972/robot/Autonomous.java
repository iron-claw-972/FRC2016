package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	public static void autonomousDoNothing() {
		
	}
	
	public static void autonomousLowBar() {
//		if (Robot.leftDriveEncoder.get() > RobotMap.LOW_BAR_LEFT_DRIVE_DISTANCE) {
//			Robot.botDrive.tankDrive(0, RobotMap.LOW_BAR_RIGHT_DRIVE_SPEED);
//		} else // TODO put the other if statement here
		if (Robot.rightDriveEncoder.get() > RobotMap.LOW_BAR_RIGHT_DRIVE_DISTANCE) {
			Robot.botDrive.tankDrive(RobotMap.LOW_BAR_LEFT_DRIVE_SPEED, 0);
		} else if (Robot.rightDriveEncoder.get() <= RobotMap.LOW_BAR_RIGHT_DRIVE_DISTANCE /*&& Robot.leftDriveEncoder.get() != RobotMap.LOW_BAR_LEFT_DRIVE_DISTANCE*/) {	// TODO remove comment		
			Robot.botDrive.tankDrive(RobotMap.LOW_BAR_LEFT_DRIVE_SPEED, RobotMap.LOW_BAR_RIGHT_DRIVE_SPEED);
		}
		SmartDashboard.putNumber("Autonomous Encoder Value", Robot.rightDriveEncoder.get());
	}
	
	public static void autonomousDriveOverDefense() { // TODO remove

	}

//	public static void autonomousChevalDeFrise() {
//		 = System.currentTimeMillis();
//		if (
//)
//	}
	
	public static void autonomousPortcullis() {
		
	}
	
	public static boolean autonomousDelay(long start, int millis) {
		return System.currentTimeMillis() >= start + millis;
	}
}
