package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Autonomous {
		
	static SendableChooser autonomousModeChooser = new SendableChooser();
	
	public static void createChooser() {
		autonomousModeChooser.addObject("Drive Forward", new Integer(RobotMap.AUTO_DRIVE_FORWARD_MODE));
		autonomousModeChooser.addObject("Low Bar Shoot", new Integer(RobotMap.LOW_BAR_SHOOT_MODE));
		autonomousModeChooser.addDefault("Do Nothing", new Integer(RobotMap.DO_NOTHING_MODE));
		SmartDashboard.putData("Autonomous Mode Chooser", autonomousModeChooser);
	}
	
	public static void checkChoices(Robot r) {
		RobotMap.autonomousMode = ((Integer) (autonomousModeChooser.getSelected())).intValue();
		
		switch (RobotMap.autonomousMode) {
			case RobotMap.AUTO_DRIVE_FORWARD_MODE:
				lowBarDrive(r);
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
	
	public static void lowBarDrive(Robot r) {
		// Reset encoders in AutonomousInit
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		while (Robot.leftDriveEncoder.get() <= RobotMap.LOW_BAR_DEFENSE_DRIVE_DISTANCE && r.isAutonomous() && r.isEnabled()) { // WE ONLY HAVE 1 ENCODER
			Robot.botDrive.tankDrive(RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED, RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED);
		}
		Robot.botDrive.tankDrive(0, 0);
	}
	
	public static void lowBarShoot(Robot r) {
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		lowBarDrive(r);
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		while (Robot.leftDriveEncoder.get() <= RobotMap.LOW_BAR_TURN_TO_GOAL && r.isAutonomous() && r.isEnabled()) {
			Robot.botDrive.tankDrive(RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED, -RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED);
		}
		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		while (Robot.leftDriveEncoder.get() <= RobotMap.LOW_BAR_TO_GOAL_DRIVE_DISTANCE && r.isAutonomous() && r.isEnabled()) { // WE ONLY HAVE 1 ENCODER
			Robot.botDrive.tankDrive(RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED, RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED);
		}
		Robot.botDrive.tankDrive(0, 0);
		long startTime = System.currentTimeMillis();
		System.out.println("Reverse Intake");
		Robot.spoonPiston.set(DoubleSolenoid.Value.kForward);
		Robot.outtakePiston.set(DoubleSolenoid.Value.kForward);
		while (startTime - System.currentTimeMillis() <= RobotMap.OUTTAKE_MOTOR_AUTO_RUN_TIME && r.isAutonomous() && r.isEnabled()) {
			Robot.intakeMotor.set(RobotMap.INTAKE_REVERSE_MOTOR_SPEED);
			Robot.spoonPiston.set(DoubleSolenoid.Value.kReverse);
			// Puts the spoon back to its original state
			Robot.outtakePiston.set(DoubleSolenoid.Value.kReverse);			// Brings the outtake piston back in
			Robot.intakeMotor.set(0);
		}
	}
}
