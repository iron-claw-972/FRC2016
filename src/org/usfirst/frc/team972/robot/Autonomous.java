package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {

	public static boolean autonomousDelay(long start, int millis) {
		return ((System.currentTimeMillis() >= start + millis));
	}

	public static boolean autonomousDriveOverDefense(int distance, double speed) {
		double leftDriveSpeed, rightDriveSpeed;
		if (Robot.leftDriveEncoder.get() < distance) {
			leftDriveSpeed = speed;
		} else {
			leftDriveSpeed = 0;
		}
		if (Robot.rightDriveEncoder.get() < distance) {
			rightDriveSpeed = speed;
		} else {
			rightDriveSpeed = 0;
		}
		Robot.botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
		return Robot.leftDriveEncoder.get() >= distance && Robot.rightDriveEncoder.get() >= distance;
	}

	public static boolean autonomousTurnClockwise(int distance, double speed) {
		double leftDriveSpeed, rightDriveSpeed;
		if (Robot.leftDriveEncoder.get() < distance) {
			leftDriveSpeed = speed;
		} else {
			leftDriveSpeed = 0;
		}
		if (Robot.rightDriveEncoder.get() > -distance) {
			rightDriveSpeed = -speed;
		} else {
			rightDriveSpeed = 0;
		}
		Robot.botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
		return Robot.leftDriveEncoder.get() >= distance && Robot.rightDriveEncoder.get() <= -distance;
	}

	public static boolean autonomousDriveBackwardOverDefense(int distance, double speed) {
		double leftDriveSpeed, rightDriveSpeed;
		if (Robot.leftDriveEncoder.get() > distance) {
			leftDriveSpeed = speed;
		} else {
			leftDriveSpeed = 0;
		}
		if (Robot.rightDriveEncoder.get() > distance) {
			rightDriveSpeed = speed;
		} else {
			rightDriveSpeed = 0;
		}
		Robot.botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
		return Robot.leftDriveEncoder.get() <= distance && Robot.rightDriveEncoder.get() <= distance;
	}

	public static boolean autonomousFlippyThing(boolean flipUp) {
		return true;
	}

	public static void startAutonomous(Robot r, AutonomousChooser autonomousChooserSystem) {

		Robot.autonomousDelayStartTime = System.currentTimeMillis();
		boolean finishedDelaying = false;
		while (r.isAutonomous() && r.isEnabled() && !finishedDelaying) {
			finishedDelaying = Autonomous.autonomousDelay(Robot.autonomousDelayStartTime, autonomousChooserSystem.getAutonomousDelay() * 1000);
			// Converting from seconds to milliseconds
		}

		// botDrive.setSafetyEnabled(true); // Prevents "output not updated
		// enough" error message
		System.out.println("finished autonomous delay");

		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();

		int section = 1;
		while (r.isEnabled() && r.isAutonomous()) {
			switch (RobotMap.autonomousFirstDefenseMode) {
				case RobotMap.LOW_BAR_MODE:
					switch (RobotMap.autonomousTwoDefenseMode) {
						case RobotMap.WAIT_MODE:
							break;
						case RobotMap.FIRST_DRIVE_FORWARD_MODE:
							if (Autonomous.autonomousDriveOverDefense(RobotMap.LOW_BAR_DEFENSE_DRIVE_DISTANCE,
									RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED)) {
							}
							SmartDashboard.putNumber("Low Bar Section", 1);
							break;
						case RobotMap.FIRST_DRIVE_BACKWARD_MODE:
							break;
						case RobotMap.TURN_MODE:
							break;
						case RobotMap.DRIVE_LEFT_RIGHT_MODE:
							break;
						case RobotMap.TURN_TOWARD_DEFENSE_MODE:
							break;
						case RobotMap.SECOND_DRIVE_FORWARD_MODE:
							break;
							// Robot.leftDriveEncoder.reset();
							// Robot.rightDriveEncoder.reset();
							//
							// } else if (section == 3) {
							// if
							// (autonomousDriveBackwardOverDefense(-RobotMap.LOW_BAR_DEFENSE_DRIVE_DISTANCE,
							// -RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED)) {
							//
							// }
							// } else if (section == 4) {
							// Robot.leftDriveEncoder.reset();
							// Robot.rightDriveEncoder.reset();
							//
							// } else if (section == 5) {
							// if
							// (autonomousTurnClockwise(RobotMap.LOW_BAR_TURN_DISTANCE,
							// RobotMap.LOW_BAR_TURN_SPEED)) {
							//
							// }
							// }
//							break;
						// case RobotMap.MOAT_MODE:
						// break;
						// case RobotMap.RAMPARTS_MODE:
						// break;
						// case RobotMap.ROCK_WALL_MODE:
						// break;
						// case RobotMap.ROUGH_TERRAIN_MODE:
						// break;
						// case RobotMap.DO_NOTHING_MODE:
						// break;
						// TODO fix the cases and put the if statement stuff
						// into the new cases above them.
						default:
							// This should never happen
							SmartDashboard.putString("Autonomous First Defense Mode", "Default error!!!");
							System.out.println("Default Autonomous First Defense Mode Error -- Actually driving!!!");
							break;
					} // switch brace
			}
		}
	}
}
