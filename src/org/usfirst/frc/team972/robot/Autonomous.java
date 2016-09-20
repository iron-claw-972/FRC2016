package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Autonomous {
		
	static SendableChooser autonomousModeChooser = new SendableChooser();
	
	public static void createChooser() {
		autonomousModeChooser.addObject("Low Bar", new Integer(RobotMap.LOW_BAR_DRIVE_FORWARD_MODE));
		autonomousModeChooser.addObject("Low Bar Forward Back Mode", new Integer(RobotMap.LOW_BAR_SHOOT_MODE));
		autonomousModeChooser.addObject("Moat", new Integer(RobotMap.MOAT_MODE));
		autonomousModeChooser.addObject("Ramparts", new Integer(RobotMap.RAMPARTS_MODE));
		autonomousModeChooser.addObject("Rock Wall", new Integer(RobotMap.ROCK_WALL_MODE));
		autonomousModeChooser.addObject("Rough Terrain", new Integer(RobotMap.ROUGH_TERRAIN_MODE));
		autonomousModeChooser.addDefault("Do Nothing", new Integer(RobotMap.DO_NOTHING_MODE));
		SmartDashboard.putData("Autonomous Mode Chooser", autonomousModeChooser);
	}
	
	public static void checkChoices(Robot r) {
		RobotMap.autonomousMode = ((Integer) (autonomousModeChooser.getSelected())).intValue();
		
		switch (RobotMap.autonomousMode) {
			case RobotMap.LOW_BAR_DRIVE_FORWARD_MODE:
				lowBarDrive(r);
				SmartDashboard.putString("Autonomous Mode", "Low Bar");
				break;
			case RobotMap.LOW_BAR_SHOOT_MODE:
				lowBarShoot(r);
				SmartDashboard.putString("Autonomous Mode", "Low Bar Forward Back");
				break;
			case RobotMap.MOAT_MODE:
				SmartDashboard.putString("Autonomous Mode", "Moat");
				break;
			case RobotMap.RAMPARTS_MODE:
				SmartDashboard.putString("Autonomous Mode", "Ramparts");
				break;
			case RobotMap.ROCK_WALL_MODE:
				SmartDashboard.putString("Autonomous Mode", "Rock Wall");
				break;
			case RobotMap.ROUGH_TERRAIN_MODE:
				SmartDashboard.putString("Autonomous Mode", "Rough Terrain");
				break;
			case RobotMap.DO_NOTHING_MODE:
				SmartDashboard.putString("Autonomous Mode", "Do Nothing Mode");
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Mode", "Default error!!!");
				System.out.println("Default Autonomous First Defense Mode Error!!!");
				break;
		}
	}
	
	public static void lowBarDrive(Robot r) {
		// Reset encoders in AutonomousInit
		while (Robot.leftDriveEncoder.get() <= RobotMap.LOW_BAR_DEFENSE_DRIVE_DISTANCE && r.isAutonomous() && r.isEnabled()) { // WE ONLY HAVE 1 ENCODER
			Robot.botDrive.tankDrive(RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED, RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED);
		}
		Robot.botDrive.tankDrive(0, 0);
	}
	
	public static void lowBarShoot(Robot r) {
		lowBarDrive(r);
		Robot.leftDriveEncoder.reset();
		while (Robot.leftDriveEncoder.get() <= RobotMap.LOW_BAR_TURN_TO_GOAL && r.isAutonomous() && r.isEnabled()) {
			Robot.botDrive.tankDrive(RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED, -RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED);
		}
		Robot.leftDriveEncoder.reset();
		while (Robot.leftDriveEncoder.get() <= RobotMap.LOW_BAR_TO_GOAL_DRIVE_DISTANCE && r.isAutonomous() && r.isEnabled()) { // WE ONLY HAVE 1 ENCODER
			Robot.botDrive.tankDrive(RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED, RobotMap.LOW_BAR_DEFENSE_DRIVE_SPEED);
		}
		Robot.botDrive.tankDrive(0, 0);
	}
	
}