package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
	CANTalon intakeMotor;
	long startTime;

	public Intake(CANTalon intakeMotor) {
		this.intakeMotor = intakeMotor;
	}

	public void spinForward() {
		intakeMotor.set(RobotMap.INTAKE_MOTOR_SPEED);
	}

	public void spinBackwards() {
		intakeMotor.set(RobotMap.INTAKE_REVERSE_MOTOR_SPEED);
	}

	public void stop() {
		intakeMotor.set(0);
	}

	public void intakeStateMachine(DoubleSolenoid spoonPiston,
			CANTalon intakeMotor, DigitalInput ballSensor) {
		switch (RobotMap.intakeState) {
			case RobotMap.INTAKE_WAIT_STATE:
				if (Robot.joystickOp
						.getRawButton(RobotMap.JOYSTICK_START_INTAKE_BUTTON)) {
					RobotMap.intakeState = RobotMap.INTAKE_SPOON_DOWN_STATE;
				}
				SmartDashboard.putString("Intake State", "Wait");
				break;
			case RobotMap.INTAKE_SPOON_DOWN_STATE:
				spoonPiston.set(DoubleSolenoid.Value.kForward);
				RobotMap.intakeState = RobotMap.INTAKE_START_INTAKE_STATE;
				SmartDashboard.putString("Intake State", "Spoon Down");
				break;

			case RobotMap.INTAKE_START_INTAKE_STATE:
				intakeMotor.set(RobotMap.INTAKE_MOTOR_SPEED);
				startTime = System.currentTimeMillis();
				RobotMap.intakeState = RobotMap.INTAKE_WAIT_FOR_OPTICAL_STATE;
				SmartDashboard.putString("Intake State", "Start Intake Motor");
				break;
			case RobotMap.INTAKE_WAIT_FOR_OPTICAL_STATE:
				if (System.currentTimeMillis() > startTime + RobotMap.BALL_OPTICAL_DELAY_TIME && ballSensor.get()) {
					RobotMap.intakeState = RobotMap.INTAKE_SPOON_UP_STATE;
				}
				SmartDashboard.putString("Intake State", "Waiting for Optical");
				break;
			case RobotMap.INTAKE_SPOON_UP_STATE:
				intakeMotor.set(0);
				spoonPiston.set(DoubleSolenoid.Value.kReverse);
				RobotMap.intakeState = RobotMap.INTAKE_WAIT_STATE;
				SmartDashboard.putString("Intake State", "Spoon Up State");
				break;
			default:
				System.out.println("Intake State Machine Error!");
				SmartDashboard.putString("Intake State", "Error");
		}

	} // intake state machine
}
