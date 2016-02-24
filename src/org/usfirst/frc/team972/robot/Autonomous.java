package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {

	public static boolean autonomousDelay(long start, int millis) {
		return ((System.currentTimeMillis() >= start + millis));
	}

	public static boolean lowerObstacleMotor(long startTime) {
		// If the lower limit switch is not pressed, lower the obstacle motor
		if (Robot.obstacleMotorLowerLimitSwitch.get()) {
			Robot.obstacleMotor.set(0.2);
		} else {
			Robot.obstacleMotor.set(0.0);
		}
		return System.currentTimeMillis() >= startTime + RobotMap.LOWER_OBSTACLE_MOTOR_TIME;
	}

	public static boolean autonomousDrive(int distance, double speed) {
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

	// Autonomous Drive Backward should never be used
	// Distance and speed should both be negative
	// public static boolean autonomousDriveBackward(int distance, double speed)
	// {
	// double leftDriveSpeed, rightDriveSpeed;
	// if (Robot.leftDriveEncoder.get() > distance) {
	// leftDriveSpeed = speed;
	// } else {
	// leftDriveSpeed = 0;
	// }
	// if (Robot.rightDriveEncoder.get() > distance) {
	// rightDriveSpeed = speed;
	// } else {
	// rightDriveSpeed = 0;
	// }
	// Robot.botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
	// return Robot.leftDriveEncoder.get() <= distance &&
	// Robot.rightDriveEncoder.get() <= distance;
	// }

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

	// distance and speed should be positive
	public static boolean autonomousTurnCounterclockwise(int distance, double speed) {
		double leftDriveSpeed, rightDriveSpeed;
		if (Robot.leftDriveEncoder.get() > -distance) {
			leftDriveSpeed = -speed;
		} else {
			leftDriveSpeed = 0;
		}
		if (Robot.rightDriveEncoder.get() < distance) {
			rightDriveSpeed = speed;
		} else {
			rightDriveSpeed = 0;
		}
		Robot.botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
		return Robot.leftDriveEncoder.get() <= distance && Robot.rightDriveEncoder.get() >= -distance;
	}

	public static boolean autonomousFlippyThing(boolean flipUp) {
		return true;
	}

	public static void startAutonomous(Robot r, AutonomousChooser autonomousChooserSystem) {

		// Currently no autonomous delay
		// Robot.autonomousDelayStartTime = System.currentTimeMillis();
		// boolean finishedDelaying = false;
		// while (r.isAutonomous() && r.isEnabled() && !finishedDelaying) {
		// finishedDelaying =
		// Autonomous.autonomousDelay(Robot.autonomousDelayStartTime,
		// autonomousChooserSystem.getAutonomousDelay() * 1000);
		// // Converting from seconds to milliseconds
		// }

		// botDrive.setSafetyEnabled(true); // Prevents "output not updated
		// enough" error message

		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();

		int distance;
		double speed;
		long startTime = System.currentTimeMillis();

		while (r.isEnabled() && r.isAutonomous()) { // will automatically return
													// out of method when
													// finished with state
													// machine
			switch (RobotMap.autonomousMode) {
				case RobotMap.LOWER_OBSTACLE_MOTOR_MODE:
					SmartDashboard.putString("Autonomous Mode", "Lower Obstacle Motor");
					if (lowerObstacleMotor(startTime)) {
						Robot.obstacleMotor.set(0);
						RobotMap.autonomousMode = RobotMap.FIRST_DRIVE_FORWARD_MODE;
					}
					break;
				case RobotMap.FIRST_DRIVE_FORWARD_MODE:
					SmartDashboard.putString("Autonomous Mode", "First Drive Forward");
					distance = AutonomousChooser.getDefenseDistance(RobotMap.autonomousFirstDefenseMode);
					speed = AutonomousChooser.getDefenseSpeed(RobotMap.autonomousFirstDefenseMode);
					if (Autonomous.autonomousDrive(distance, speed)) {
						Robot.botDrive.tankDrive(0, 0);
						Robot.leftDriveEncoder.reset();
						Robot.rightDriveEncoder.reset();
						RobotMap.autonomousMode = RobotMap.TURN_AROUND_MODE;
					}
					break;
				case RobotMap.TURN_AROUND_MODE:
					SmartDashboard.putString("Autonomous Mode", "Turn Around");
					// Twice the distance because we want two 90 degree turns
					if (autonomousTurnClockwise(2 * RobotMap.LOW_BAR_TURN_DISTANCE, RobotMap.AUTONOMOUS_TURN_SPEED)) {
						Robot.botDrive.tankDrive(0, 0);
						Robot.leftDriveEncoder.reset();
						Robot.rightDriveEncoder.reset();
						RobotMap.autonomousMode = RobotMap.FIRST_DRIVE_BACKWARD_MODE;
					}
				case RobotMap.FIRST_DRIVE_BACKWARD_MODE:
					SmartDashboard.putString("Autonomous Mode", "First Drive Backward");
					distance = AutonomousChooser.getDefenseDistance(RobotMap.autonomousFirstDefenseMode);
					speed = AutonomousChooser.getDefenseSpeed(RobotMap.autonomousFirstDefenseMode);
					if (Autonomous.autonomousDrive(distance, speed)) {
						Robot.botDrive.tankDrive(0, 0);
						Robot.leftDriveEncoder.reset();
						Robot.rightDriveEncoder.reset();
						RobotMap.autonomousMode = RobotMap.TURN_MODE;
					}
					break;
				case RobotMap.TURN_MODE:
					SmartDashboard.putString("Autonomous Mode", "Turn");
					if (AutonomousChooser.getDifferenceInPosition() > 0) {
						if (autonomousTurnCounterclockwise(RobotMap.LOW_BAR_TURN_DISTANCE, RobotMap.AUTONOMOUS_TURN_SPEED)) {
							Robot.botDrive.tankDrive(0, 0);
							Robot.leftDriveEncoder.reset();
							Robot.rightDriveEncoder.reset();
							RobotMap.autonomousMode = RobotMap.GO_TO_NEXT_DEFENSE_MODE;
						}
					} else if (AutonomousChooser.getDifferenceInPosition() < 0) {
						if (autonomousTurnClockwise(RobotMap.LOW_BAR_TURN_DISTANCE, RobotMap.AUTONOMOUS_TURN_SPEED)) {
							Robot.botDrive.tankDrive(0, 0);
							Robot.leftDriveEncoder.reset();
							Robot.rightDriveEncoder.reset();
							RobotMap.autonomousMode = RobotMap.GO_TO_NEXT_DEFENSE_MODE;
						}
					} else {
						Robot.botDrive.tankDrive(0, 0);
						Robot.leftDriveEncoder.reset();
						Robot.rightDriveEncoder.reset();
						RobotMap.autonomousMode = RobotMap.GO_TO_NEXT_DEFENSE_MODE;
					}
					break;
				case RobotMap.GO_TO_NEXT_DEFENSE_MODE:
					SmartDashboard.putString("Autonomous Mode", "Go To Next Defense");
					distance = RobotMap.AUTONOMOUS_DISTANCE_BETWEEN_DEFENSES * AutonomousChooser.getDifferenceInPosition();
					speed = RobotMap.AUTONOMOUS_SPEED_BETWEEN_DISTANCES;
					if (Autonomous.autonomousDrive(distance, speed)) {
						Robot.botDrive.tankDrive(0, 0);
						Robot.leftDriveEncoder.reset();
						Robot.rightDriveEncoder.reset();
						RobotMap.autonomousMode = RobotMap.TURN_TOWARD_DEFENSE_MODE;
					}
					break;
				case RobotMap.TURN_TOWARD_DEFENSE_MODE:
					SmartDashboard.putString("Autonomous Mode", "Turn Toward Defense");
					if (AutonomousChooser.getDifferenceInPosition() > 0) {
						if (autonomousTurnCounterclockwise(RobotMap.LOW_BAR_TURN_DISTANCE, RobotMap.AUTONOMOUS_TURN_SPEED)) {
							Robot.botDrive.tankDrive(0, 0);
							Robot.leftDriveEncoder.reset();
							Robot.rightDriveEncoder.reset();
							RobotMap.autonomousMode = RobotMap.SECOND_DRIVE_FORWARD_MODE;
						}
					} else if (AutonomousChooser.getDifferenceInPosition() < 0) {
						if (autonomousTurnClockwise(RobotMap.LOW_BAR_TURN_DISTANCE, RobotMap.AUTONOMOUS_TURN_SPEED)) {
							Robot.botDrive.tankDrive(0, 0);
							Robot.leftDriveEncoder.reset();
							Robot.rightDriveEncoder.reset();
							RobotMap.autonomousMode = RobotMap.SECOND_DRIVE_FORWARD_MODE;
						}
					}
					break;
				case RobotMap.SECOND_DRIVE_FORWARD_MODE:
					SmartDashboard.putString("Autonomous Mode", "Second Drive Forward");
					distance = AutonomousChooser.getDefenseDistance(RobotMap.autonomousSecondDefenseMode);
					speed = AutonomousChooser.getDefenseSpeed(RobotMap.autonomousSecondDefenseMode);
					if (Autonomous.autonomousDrive(distance, speed)) {
						Robot.botDrive.tankDrive(0, 0);
						Robot.leftDriveEncoder.reset();
						Robot.rightDriveEncoder.reset();
						return;
					}
					SmartDashboard.putString("Autonomous Mode", "First Drive Forward");
					break;
			}
		}
	}
}
