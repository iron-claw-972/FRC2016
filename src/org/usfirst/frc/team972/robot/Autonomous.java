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
		case RobotMap.AUTO_CROSS_OBSTACLE_MODE: // Drives straight over obstacle
			autoCrossObstacle(r);
			SmartDashboard.putString("Autonomous Mode", "Drive Forward Mode");
			break;
		case RobotMap.LOW_BAR_SHOOT_MODE: // Drives over low bar, positions itself in front of the low goal, and shoots
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
	
	public static void printEncoders() {
		SmartDashboard.putNumber("Auto Left Encoder", Robot.leftDriveEncoder.get());
		SmartDashboard.putNumber("Auto Right Encoder", Robot.rightDriveEncoder.get());
		System.out.println(Robot.leftDriveEncoder.get());
		System.out.println(Robot.rightDriveEncoder.get());
	}

	// crosses the obstacle and then stops
	public static void autoCrossObstacle(Robot r) {
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		// drive until we drive the designated distance, then stop
		while (Robot.rightDriveEncoder.get() <= RobotMap.CROSS_OBSTACLE_DRIVE_DISTANCE && r.isAutonomous()) {
			// TODO: WE ONLY HAVE 1 ENCODER - CHANGE CODE WHEN WE FIX SECOND DRIVE ENCODER
			printEncoders();
			Robot.botDrive.tankDrive(RobotMap.CROSS_OBSTACLE_DRIVE_SPEED, RobotMap.CROSS_OBSTACLE_DRIVE_SPEED);
		}
		Robot.botDrive.tankDrive(0, 0);
	}

	// goes under the low bar, goes to low goal and shoots low
	public static void lowBarShoot(Robot r) {
		// Encoders reset so that the values aren't carried over from previous movement
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset(); 
		
		// drive under low bar
		while (Robot.rightDriveEncoder.get() <= RobotMap.CROSS_OBSTACLE_DRIVE_DISTANCE && r.isAutonomous()) {
			printEncoders();
			Robot.botDrive.tankDrive(RobotMap.CROSS_OBSTACLE_DRIVE_SPEED, RobotMap.CROSS_OBSTACLE_DRIVE_SPEED);
		}
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		
		// turn to the right towards the low goal
		while (Robot.rightDriveEncoder.get() <= RobotMap.LOW_BAR_TURN_TO_GOAL_DISTANCE && r.isAutonomous()) {
			printEncoders();
			Robot.botDrive.tankDrive(RobotMap.CROSS_OBSTACLE_DRIVE_SPEED, -RobotMap.CROSS_OBSTACLE_DRIVE_SPEED);
		}
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		
		// drive to low goal
		while (Robot.rightDriveEncoder.get() <= RobotMap.DRIVE_TO_GOAL_DISTANCE && r.isAutonomous()) {
			printEncoders();
			Robot.botDrive.tankDrive(RobotMap.CROSS_OBSTACLE_DRIVE_SPEED, RobotMap.CROSS_OBSTACLE_DRIVE_SPEED);
		}
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		
		// stops drive motors
		Robot.botDrive.tankDrive(0, 0);
		
		long startTime = System.currentTimeMillis();
		System.out.println("Reverse Intake");
		
		// outtake ball
		Robot.spoonPiston.set(DoubleSolenoid.Value.kForward); // spoon down
		Robot.outtakePiston.set(DoubleSolenoid.Value.kForward); // outtake ball for _ seconds
		while (startTime - System.currentTimeMillis() <= RobotMap.OUTTAKE_MOTOR_AUTO_RUN_TIME && r.isAutonomous()) {
			Robot.intakeMotor.set(RobotMap.INTAKE_REVERSE_MOTOR_SPEED); // spins pin reverse so ball is shot out
			Robot.spoonPiston.set(DoubleSolenoid.Value.kReverse); // Drops spoon down for ball to be released
			Robot.outtakePiston.set(DoubleSolenoid.Value.kReverse); // Pushes ball out 
			Robot.intakeMotor.set(0); // Stops intake motor once completed
		}
	}
}
