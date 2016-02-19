package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Intake {
	CANTalon intakeMotor;
	long startTime = -1;
	boolean lastTimePressed = false;
	
	public Intake(CANTalon intakeMotor) {
		this.intakeMotor = intakeMotor;
	}
	
//	public void spinForward() {
//		intakeMotor.set(RobotMap.INTAKE_MOTOR_SPEED); 		
//	}
//	
//	public void spinBackwards() {
//		intakeMotor.set(RobotMap.INTAKE_REVERSE_MOTOR_SPEED);
//	}
//	
//	public void stop() {
//		intakeMotor.set(0);
//	}
	
	public void intakeStateMachine(DoubleSolenoid spoonPiston, DigitalInput ballSensor) {
		if (!Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_INTAKE_BUTTON)) {
			RobotMap.intakeState = RobotMap.INTAKE_WAIT_STATE;
		}
		switch (RobotMap.intakeState) {
			case RobotMap.INTAKE_WAIT_STATE :
				intakeMotor.set(0);
				boolean buttonPressed = Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_INTAKE_BUTTON);
				if (buttonPressed && !lastTimePressed) {
					RobotMap.intakeState = RobotMap.INTAKE_SPOON_DOWN_STATE;
				}
				lastTimePressed = buttonPressed;
				SmartDashboard.putString("Intake State", "Wait");
				break;
			case RobotMap.INTAKE_SPOON_DOWN_STATE :
				spoonPiston.set(DoubleSolenoid.Value.kForward);
				RobotMap.intakeState = RobotMap.INTAKE_START_INTAKE_STATE;
				SmartDashboard.putString("Intake State", "Spoon Down");
				break;
			case RobotMap.INTAKE_START_INTAKE_STATE :
				intakeMotor.set(RobotMap.INTAKE_MOTOR_SPEED);
				if (!ballSensor.get()) {
					if (startTime == -1) {
						startTime = System.currentTimeMillis();
					} else {
						if (System.currentTimeMillis() >= startTime + RobotMap.BALL_OPTICAL_DELAY_TIME) {
							RobotMap.intakeState = RobotMap.INTAKE_SPOON_UP_STATE;
							startTime = -1;
						}
					}
				}
				SmartDashboard.putString("Intake State", "Start Intake Motor");
				break;
			case RobotMap.INTAKE_SPOON_UP_STATE :
				intakeMotor.set(0);
				spoonPiston.set(DoubleSolenoid.Value.kReverse);
				RobotMap.intakeState = RobotMap.INTAKE_WAIT_STATE;
				SmartDashboard.putString("Intake State", "Spoon Up State");
				break;
			default:
				System.out.println("Intake State Machine Error!");
				SmartDashboard.putString("Intake State", "Error");
				break;
		}

	} // intake state machine
	
}
