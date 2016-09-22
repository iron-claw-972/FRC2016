package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Autonomous {

	static SendableChooser autonomousModeChooser = new SendableChooser();

	// creates the autonomous chooser on SmartDashboard
	public static void createChooser() {
		autonomousModeChooser.addObject("Drive Forward", new Integer(RobotMap.AUTO_CROSS_OBSTACLE_MODE));
		autonomousModeChooser.addObject("Low Bar Shoot", new Integer(RobotMap.LOW_BAR_SHOOT_MODE));
		autonomousModeChooser.addDefault("Do Nothing", new Integer(RobotMap.DO_NOTHING_MODE));
		SmartDashboard.putData("Autonomous Mode Chooser", autonomousModeChooser);
	}

	// runs the autonomous routine selected in SmartDashboard
	public static void runAutonomous(Robot r) {
		RobotMap.autonomousMode = ((Integer) (autonomousModeChooser.getSelected())).intValue();
		// getSelected() returns an object and we need to convert it to an int
		// so we cast it into an Integer and use its intValue() method
		switch (RobotMap.autonomousMode) {
		case RobotMap.AUTO_CROSS_OBSTACLE_MODE:
			autoCrossObstacle(r);
			SmartDashboard.putString("Autonomous Mode", "Drive Forward Mode");
			break;
		case RobotMap.LOW_BAR_SHOOT_MODE:
			lowBarShoot(r);
			SmartDashboard.putString("Autonomous Mode", "Low Bar Shoot Mode");
			break;
		case RobotMap.DO_NOTHING_MODE:
			SmartDashboard.putString("Autonomous Mode", "Do Nothing Mode");
		default:
			// This should never happen
			SmartDashboard.putString("Autonomous Mode", "Default error!!!");
			System.out.println("Default Autonomous Error!!!");
			break;
		}
	}

	// crosses the obstacle and then stops
	public static void autoCrossObstacle(Robot r) {
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		// drive until we drive the designated distance, then stop
		while (Robot.leftDriveEncoder.get() <= RobotMap.CROSS_OBSTACLE_DRIVE_DISTANCE && r.isAutonomous()) {
			// WE ONLY HAVE 1 ENCODER - CHANGE CODE WHEN WE FIX SECOND DRIVE ENCODER
			Robot.botDrive.tankDrive(RobotMap.CROSS_OBSTACLE_DRIVE_SPEED, RobotMap.CROSS_OBSTACLE_DRIVE_SPEED);
		}
		Robot.botDrive.tankDrive(0, 0);
	}

	// goes under the low bar, goes to low goal and shoots low
	public static void lowBarShoot(Robot r) {
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		
		// drive under low bar
		while (Robot.leftDriveEncoder.get() <= RobotMap.CROSS_OBSTACLE_DRIVE_DISTANCE && r.isAutonomous()) {
			Robot.botDrive.tankDrive(RobotMap.CROSS_OBSTACLE_DRIVE_SPEED, RobotMap.CROSS_OBSTACLE_DRIVE_SPEED);
		}
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		
		// turn to the right towards the goal
		while (Robot.leftDriveEncoder.get() <= RobotMap.LOW_BAR_TURN_TO_GOAL_DISTANCE && r.isAutonomous()) {
			Robot.botDrive.tankDrive(RobotMap.CROSS_OBSTACLE_DRIVE_SPEED, -RobotMap.CROSS_OBSTACLE_DRIVE_SPEED);
		}
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		
		// drive to goal
		while (Robot.leftDriveEncoder.get() <= RobotMap.DRIVE_TO_GOAL_DISTANCE && r.isAutonomous()) {
			Robot.botDrive.tankDrive(RobotMap.CROSS_OBSTACLE_DRIVE_SPEED, RobotMap.CROSS_OBSTACLE_DRIVE_SPEED);
		}
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		
		// stop
		Robot.botDrive.tankDrive(0, 0);
		
		long startTime = System.currentTimeMillis();
		System.out.println("Reverse Intake");
		
		// outtake ball
		Robot.spoonPiston.set(DoubleSolenoid.Value.kForward); // spoon down
		Robot.outtakePiston.set(DoubleSolenoid.Value.kForward); // outtake ball for _ seconds
		while (startTime - System.currentTimeMillis() <= RobotMap.OUTTAKE_MOTOR_AUTO_RUN_TIME && r.isAutonomous()) {
			Robot.intakeMotor.set(RobotMap.INTAKE_REVERSE_MOTOR_SPEED);
			Robot.spoonPiston.set(DoubleSolenoid.Value.kReverse);
			Robot.outtakePiston.set(DoubleSolenoid.Value.kReverse);
			Robot.intakeMotor.set(0);
		}
	}
}
