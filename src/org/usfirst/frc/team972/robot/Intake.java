package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Intake {
	CANTalon intakeMotor;
	DoubleSolenoid spoonPiston, outtakePiston;
	DigitalInput ballSensor;
	long startTime = -1, reverseStartTime = -1;
	boolean lastTimePressed = false;

	public Intake(CANTalon intakeMotor, DoubleSolenoid spoonPiston, DoubleSolenoid outtakePiston,
			DigitalInput ballSensor) {
		this.intakeMotor = intakeMotor;
		this.spoonPiston = spoonPiston;
		this.outtakePiston = outtakePiston;
		this.ballSensor = ballSensor;
	}

	public void spoonUp() {
		spoonPiston.set(DoubleSolenoid.Value.kReverse);
	}

	public void intakeStateMachine() {
		switch (RobotMap.intakeState) {
			case RobotMap.INTAKE_WAIT_STATE:
				intakeMotor.set(0);
				// Reverse means the outtake piston is in
				boolean buttonPressed = Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_INTAKE_BUTTON);
				boolean backButtonPressed = Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_REVERSE_INTAKE_BUTTON);
				if (buttonPressed && !lastTimePressed) {
					RobotMap.intakeState = RobotMap.INTAKE_SPOON_DOWN_STATE;
				}
				if (backButtonPressed) {
					RobotMap.intakeState = RobotMap.INTAKE_REVERSE_SPOON_DOWN_STATE;
				}
				lastTimePressed = buttonPressed;
//				SmartDashboard.putString("Intake State", "Wait"); //Not used by Drive Team.
				break;
			case RobotMap.INTAKE_SPOON_DOWN_STATE:
				spoonPiston.set(DoubleSolenoid.Value.kForward);
				// Spoon is down
				RobotMap.intakeState = RobotMap.INTAKE_START_INTAKE_STATE;
//				SmartDashboard.putString("Intake State", "Spoon Down"); //Not used by Drive Team.
				break;
			case RobotMap.INTAKE_START_INTAKE_STATE:
				intakeMotor.set(RobotMap.INTAKE_MOTOR_SPEED);
				// if (!ballSensor.get()) {
				if (!Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_INTAKE_BUTTON)) {
					// if (startTime < 0) {
					// startTime = System.currentTimeMillis();
					// }
					// if (System.currentTimeMillis() >= startTime +
					// RobotMap.BALL_OPTICAL_DELAY_TIME) {
					// RobotMap.intakeState = RobotMap.INTAKE_SPOON_UP_STATE;
					// startTime = -1;
					// }
					System.out.println("Leaving start intake");
					RobotMap.intakeState = RobotMap.INTAKE_SPOON_UP_STATE;
				}
//				SmartDashboard.putString("Intake State", "Start Intake Motor"); //Not used by Drive Team.
				break;
			case RobotMap.INTAKE_SPOON_UP_STATE:
				intakeMotor.set(0);
				spoonPiston.set(DoubleSolenoid.Value.kReverse);
				// Spoon is up
				RobotMap.intakeState = RobotMap.INTAKE_WAIT_STATE;
//				SmartDashboard.putString("Intake State", "Spoon Up"); //Not used by Drive Team.
				break;
			case RobotMap.INTAKE_REVERSE_SPOON_DOWN_STATE:
//				SmartDashboard.putString("Intake State", "Reverse Spoon"); //Not used by Drive Team.
				System.out.println("Reverse Intake");
				spoonPiston.set(DoubleSolenoid.Value.kForward);
				// Spoon is down to allow the ball to reach the intake motor
				if (reverseStartTime < 0) {
					reverseStartTime = System.currentTimeMillis();
				}
				if (System.currentTimeMillis() >= reverseStartTime + RobotMap.REVERSE_OUTTAKE_DELAY_TIME) {
					RobotMap.intakeState = RobotMap.INTAKE_REVERSE_OUTTAKE_STATE;
					reverseStartTime = -1;
				}
				// This case adds a delay in order to allow the outtake to work
				// after the spoon is down
				break;
			case RobotMap.INTAKE_REVERSE_OUTTAKE_STATE:
//				SmartDashboard.putString("Intake State", "Reverse Outtake"); //Not used by Drive Team.
				outtakePiston.set(DoubleSolenoid.Value.kForward);
				intakeMotor.set(RobotMap.INTAKE_REVERSE_MOTOR_SPEED);
				if (!Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_REVERSE_INTAKE_BUTTON)) {
					spoonPiston.set(DoubleSolenoid.Value.kReverse);
					// Puts the spoon back to its original state
					outtakePiston.set(DoubleSolenoid.Value.kReverse);
					// Brings the outtake piston back in
					RobotMap.intakeState = RobotMap.INTAKE_WAIT_STATE;
					intakeMotor.set(0);
				}
				break;
			default:
				System.out.println("Intake State Machine Error!");
//				SmartDashboard.putString("Intake State", "Error"); //Not used by Drive Team.
				break;
		}

	} // intake state machine

}
