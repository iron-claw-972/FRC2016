package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.*;

public class AutonomousChooser {

	SendableChooser autonomousDefenseChooser = new SendableChooser();
	SendableChooser autonomousDelayChooser = new SendableChooser();
	SendableChooser autonomousPositionChooser = new SendableChooser();
	SendableChooser autonomousShooterChooser = new SendableChooser();
	int autonomousDelay; // How long we delay
	long chevalDeFriseStartTime = -1; // This means the timer has not been set
	
	public void createChooser() {
		autonomousDefenseChooser.addObject("Low Bar", new Integer(RobotMap.LOW_BAR_MODE));
		autonomousDefenseChooser.addObject("Portcullis", new Integer(RobotMap.PORTCULLIS_MODE));
		autonomousDefenseChooser.addObject("Cheval de Frise", new Integer(RobotMap.CHEVAL_DE_FRISE_MODE));
		autonomousDefenseChooser.addObject("Moat", new Integer(RobotMap.MOAT_MODE));
		autonomousDefenseChooser.addObject("Ramparts", new Integer(RobotMap.RAMPARTS_MODE));
		autonomousDefenseChooser.addObject("Drawbridge", new Integer(RobotMap.DRAWBRIDGE_MODE));
		autonomousDefenseChooser.addObject("Sally Port", new Integer(RobotMap.SALLY_PORT_MODE));
		autonomousDefenseChooser.addObject("Rock Wall", new Integer(RobotMap.ROCK_WALL_MODE));
		autonomousDefenseChooser.addObject("Rough Terrain", new Integer(RobotMap.ROUGH_TERRAIN_MODE));
		autonomousDefenseChooser.addDefault("Do Nothing", new Integer(RobotMap.DO_NOTHING_MODE));
		SmartDashboard.putData("Autonomous Defense Chooser", autonomousDefenseChooser);

		autonomousPositionChooser.addObject("Spy Position", new Integer(RobotMap.POSITION_SPY));
		autonomousPositionChooser.addDefault("Position 1", new Integer(RobotMap.POSITION_1));
		autonomousPositionChooser.addObject("Position 2", new Integer(RobotMap.POSITION_2));
		autonomousPositionChooser.addObject("Position 3", new Integer(RobotMap.POSITION_3));
		autonomousPositionChooser.addObject("Position 4", new Integer(RobotMap.POSITION_4));
		autonomousPositionChooser.addObject("Position 5", new Integer(RobotMap.POSITION_5));
		SmartDashboard.putData("Autonomous Position Chooser", autonomousPositionChooser);

		autonomousDelayChooser.addDefault("No Delay", new Integer(RobotMap.NO_DELAY));
		autonomousDelayChooser.addObject("2 Sec Delay", new Integer(RobotMap.TWO_SECOND_DELAY));
		autonomousDelayChooser.addObject("4 Sec Delay", new Integer(RobotMap.FOUR_SECOND_DELAY));
		autonomousDelayChooser.addObject("6 Sec Delay", new Integer(RobotMap.SIX_SECOND_DELAY));
		SmartDashboard.putData("Autonomous Delay Chooser", autonomousDelayChooser);

		autonomousShooterChooser.addDefault("Do Not Shoot", new Integer(RobotMap.DO_NOT_SHOOT));
		autonomousShooterChooser.addObject("Left High Goal", new Integer(RobotMap.SHOOTER_LEFT_HIGH_GOAL));
		autonomousShooterChooser.addObject("Center High Goal", new Integer(RobotMap.SHOOTER_CENTER_HIGH_GOAL));
		autonomousShooterChooser.addObject("Right High Goal", new Integer(RobotMap.SHOOTER_RIGHT_HIGH_GOAL));
		autonomousShooterChooser.addObject("Assist", new Integer(RobotMap.ASSIST_SHOOT));
		SmartDashboard.putData("Autonomous Shooting Chooser", autonomousShooterChooser);
	}
	
	public void checkChoices() {
		RobotMap.autonomousDefenseMode = ((Integer) (autonomousDefenseChooser.getSelected())).intValue();
		RobotMap.autonomousDelayMode = ((Integer) (autonomousDelayChooser.getSelected())).intValue();
		RobotMap.autonomousPositionMode = ((Integer) (autonomousPositionChooser.getSelected())).intValue();
		RobotMap.autonomousShooterMode = ((Integer) (autonomousShooterChooser.getSelected())).intValue();
		// These lines store the value of the Autonomous Chooser as an int

		switch (RobotMap.autonomousDelayMode) {
			case RobotMap.NO_DELAY:
				SmartDashboard.putString("Autonomous Delay Mode", "No Delay");
				autonomousDelay = 0;
				break;
			case RobotMap.TWO_SECOND_DELAY:
				SmartDashboard.putString("Autonomous Delay Mode", "Two Second Delay");
				autonomousDelay = 2;
				break;
			case RobotMap.FOUR_SECOND_DELAY:
				SmartDashboard.putString("Autonomous Delay Mode", "Four Second Delay");
				autonomousDelay = 4;
				break;
			case RobotMap.SIX_SECOND_DELAY:
				SmartDashboard.putString("Autonomous Delay Mode", "Six Second Delay");
				autonomousDelay = 6;
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Delay Mode", "Default error!!!");
				System.out.println("Default Autonomous Delay Mode Error!!!");
				break;
		}

		switch (RobotMap.autonomousPositionMode) {
			case RobotMap.POSITION_SPY:
				SmartDashboard.putString("Autonomous Position Mode", "Spy Position");
				break;
			case RobotMap.POSITION_1:
				SmartDashboard.putString("Autonomous Position Mode", "Position 1");
				break;
			case RobotMap.POSITION_2:
				SmartDashboard.putString("Autonomous Position Mode", "Position 2");
				break;
			case RobotMap.POSITION_3:
				SmartDashboard.putString("Autonomous Position Mode", "Position 3");
				break;
			case RobotMap.POSITION_4:
				SmartDashboard.putString("Autonomous Position Mode", "Position 4");
				break;
			case RobotMap.POSITION_5:
				SmartDashboard.putString("Autonomous Position Mode", "Position 5");
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Position Mode", "Default error!!!");
				System.out.println("Default Autonomous Position Mode Error!!!");
				break;
		}

		switch (RobotMap.autonomousShooterMode) {
			case RobotMap.DO_NOT_SHOOT:
				SmartDashboard.putString("Autonomous Shooter Mode", "Do Not Shoot");
				break;
			case RobotMap.SHOOTER_LEFT_HIGH_GOAL:
				SmartDashboard.putString("Autonomous Shooter Mode", "Left High Goal");
				break;
			case RobotMap.SHOOTER_CENTER_HIGH_GOAL:
				SmartDashboard.putString("Autonomous Shooter Mode", "Center High Goal");
				break;
			case RobotMap.SHOOTER_RIGHT_HIGH_GOAL:
				SmartDashboard.putString("Autonomous Shooter Mode", "Right High Goal");
				break;
			case RobotMap.ASSIST_SHOOT:
				SmartDashboard.putString("Autonomous Shooter Mode", "Assist For Shooting");
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Shooter Mode", "Default error!!!");
				System.out.println("Default Autonomous Shooter Mode Error!!!");
				break;
		}

		switch (RobotMap.autonomousDefenseMode) {
			case RobotMap.LOW_BAR_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Low Bar");
				break;
			case RobotMap.PORTCULLIS_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Portcullis");
				break;
			case RobotMap.CHEVAL_DE_FRISE_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Cheval de Frise");
				break;
			case RobotMap.MOAT_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Moat");
				break;
			case RobotMap.RAMPARTS_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Ramparts");
				break;
			case RobotMap.DRAWBRIDGE_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Drawbridge");
				break;
			case RobotMap.SALLY_PORT_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Sally Port");
				break;
			case RobotMap.ROCK_WALL_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Rock Wall");
				break;
			case RobotMap.ROUGH_TERRAIN_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Rough Terrain");
				break;
			case RobotMap.DO_NOTHING_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Do Nothing");
				return;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Defense Mode", "Default error!!!");
				System.out.println("Default Autonomous Defense Mode Error!!!");
				break;
		} // switch brace
	}
	
	public int getAutonomousDelay() {
		return autonomousDelay;
	}
}
