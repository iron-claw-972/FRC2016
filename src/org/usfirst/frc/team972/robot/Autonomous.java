package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {

	public static boolean autonomousDelay(long start, int millis) {
		return ((System.currentTimeMillis() >= start + millis));
	}
	
	public static boolean autonomousDriveOverDefense(int distance) {
		double leftDriveSpeed, rightDriveSpeed;
		if (Robot.leftDriveEncoder.get() < distance) {
			leftDriveSpeed = 0.5;
		} else {
			leftDriveSpeed = 0;
		}
		if (Robot.rightDriveEncoder.get() < distance) {
			rightDriveSpeed = 0.5;
		} else {
			rightDriveSpeed = 0;
		}
		Robot.botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
		return Robot.leftDriveEncoder.get() >= distance && Robot.rightDriveEncoder.get() >= distance;
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
		
//		botDrive.setSafetyEnabled(true); // Prevents "output not updated enough" error message
		System.out.println("finished autonomous delay");

		Robot.leftDriveEncoder.reset();
		Robot.rightDriveEncoder.reset();
		
		switch (RobotMap.autonomousDefenseMode) {
			case RobotMap.LOW_BAR_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.LOW_BAR_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.PORTCULLIS_MODE:
				Autonomous.autonomousFlippyThing(true);
				break;
			case RobotMap.CHEVAL_DE_FRISE_MODE:
				Autonomous.autonomousFlippyThing(false);
				break;
			case RobotMap.MOAT_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.MOAT_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.RAMPARTS_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.RAMPARTS_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.DRAWBRIDGE_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.DRAWBRIDGE_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.SALLY_PORT_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.SALLY_PORT_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.ROCK_WALL_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.ROCK_WALL_DEFENSE_DRIVE_DISTANCE); // May need to be tuned
				break;
			case RobotMap.ROUGH_TERRAIN_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.ROUGH_TERRAIN_DEFENSE_DRIVE_DISTANCE); // May need to be tuned
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Defense Mode", "Default error!!!");
				System.out.println("Default Autonomous Defense Mode Error -- Actually driving!!!");
				break;
		} // switch brace
	}	
}
