package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	CANTalon topMotor, bottomMotor;
	final int LOW_SPEED = 0;
	final int MED_SPEED = 1;
	final int HIGH_SPEED = 2;
	int shooterSpeed = MED_SPEED;
	long startTime = -1;

	public Shooter(CANTalon topMotor, CANTalon bottomMotor) {
		this.topMotor = topMotor;
		this.bottomMotor = bottomMotor;
	}

	// @return whether shooter is done or not
	boolean shooterSlow() {
		System.out.println("Slow Shooter");
		return setShooter(RobotMap.LOW_SPEED_SHOOTER_MOTOR_SETPOINT);
	}

	boolean shooterMedium() {
		System.out.println("Med Shooter");
		return setShooter(RobotMap.MEDIUM_SPEED_SHOOTER_MOTOR_SETPOINT);
	}

	boolean shooterHigh() {
		System.out.println("High Shooter");
		return setShooter(RobotMap.HIGH_SPEED_SHOOTER_MOTOR_SETPOINT);
	}

	void shooterStop() {
		System.out.println("Stop Shooter");
		topMotor.set(0);
		bottomMotor.set(0);
		topMotor.disable();
		bottomMotor.disable();
	}

	boolean setShooter(double speed) {
		double kP = (((Robot.joystickLeft.getZ() * -1) + 1) / 2.0) * 0.01;
		double kI = (((Robot.joystickRight.getZ() * -1) + 1) / 2.0) * 0.01;
		double kD = (((Robot.joystickOp.getThrottle() * -1) + 1) / 2.0) * 0.01;

		SmartDashboard.putNumber("Shooter P", kP);
		SmartDashboard.putNumber("Shooter I", kI);
		SmartDashboard.putNumber("Shooter D", kD);
		
		double topSetpoint = speed;
		double bottomSetpoint = -speed;
		double deadzone = 0.01;
		
		topMotor.setSetpoint(topSetpoint);
		bottomMotor.setSetpoint(bottomSetpoint);
		
		topMotor.setPID(kP, kI, kD);
		topMotor.enable();
		bottomMotor.setPID(kP, kI, kD);
		bottomMotor.enable();
		
		SmartDashboard.putNumber("Top Error", topMotor.get() - topSetpoint);
		SmartDashboard.putNumber("Bottom Error", bottomMotor.get() - bottomSetpoint);

		boolean topMotorCorrect = topMotor.get() <= topSetpoint + deadzone && topMotor.get() >= topSetpoint - deadzone;
		boolean bottomMotorCorrect = bottomMotor.get() <= bottomSetpoint + deadzone && bottomMotor.get() >= bottomSetpoint - deadzone;
		boolean doneSettingSpeed = topMotorCorrect && bottomMotorCorrect;
		if (doneSettingSpeed) {
			shooterStop();
		}
		return doneSettingSpeed;
	}

	// @return whether shooter speed is changed
	public boolean checkShooterSpeedButton() {
		int oldSpeed = shooterSpeed;
		if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_LOW_SPEED_SHOOTER_BUTTON)) {
			shooterSpeed = LOW_SPEED;
			SmartDashboard.putString("Shooter Speed", "Low");
		}
		if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_MEDIUM_SPEED_SHOOTER_BUTTON)) {
			shooterSpeed = MED_SPEED;
			SmartDashboard.putString("Shooter Speed", "Medium");
		}
		if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_HIGH_SPEED_SHOOTER_BUTTON)) {
			shooterSpeed = HIGH_SPEED;
			SmartDashboard.putString("Shooter Speed", "High");
		}
		return !(oldSpeed == shooterSpeed);
	}

	public void goToCorrectSpeed() {
		switch (shooterSpeed) {
		case LOW_SPEED:
			RobotMap.shooterState = RobotMap.SHOOTER_LOW_SPEED_STATE;
			break;
		case MED_SPEED:
			RobotMap.shooterState = RobotMap.SHOOTER_MEDIUM_SPEED_STATE;
			break;
		case HIGH_SPEED:
			RobotMap.shooterState = RobotMap.SHOOTER_HIGH_SPEED_STATE;
			break;
		default:
			System.out.println("shooter speed switch statement error");
		}
	}

	public void shooterStateMachine(DoubleSolenoid spoonPiston) {
		switch (RobotMap.shooterState) {
		case RobotMap.SHOOTER_WAIT_STATE:
			SmartDashboard.putString("Shooter State", "Wait");
			checkShooterSpeedButton();
			if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_USE_SHOOTER_BUTTON)) {
				RobotMap.shooterState = RobotMap.SHOOTER_SPOON_DOWN_STATE;
			}
			break;
		case RobotMap.SHOOTER_SPOON_DOWN_STATE:
			SmartDashboard.putString("Shooter State", "Spoon Down");
			spoonPiston.set(DoubleSolenoid.Value.kForward);
			checkShooterSpeedButton();
			goToCorrectSpeed(); // this helper method will change the state
			break;
		case RobotMap.SHOOTER_LOW_SPEED_STATE:
			SmartDashboard.putString("Shooter State", "Low Speed");
			if (checkShooterSpeedButton()) { // if you change the speed
				goToCorrectSpeed(); // go to the correct state
			} else {
				if (shooterSlow() && !Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_USE_SHOOTER_BUTTON)) {
					RobotMap.shooterState = RobotMap.SHOOTER_SPOON_UP_STATE;
				}
			}
			break;
		case RobotMap.SHOOTER_MEDIUM_SPEED_STATE:
			SmartDashboard.putString("Shooter State", "Medium Speed");
			if (checkShooterSpeedButton()) { // if you change the speed
				goToCorrectSpeed(); // go to the correct state
			} else {
				if (shooterMedium() && !Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_USE_SHOOTER_BUTTON)) {
					RobotMap.shooterState = RobotMap.SHOOTER_SPOON_UP_STATE;
				}
			}
			break;
		case RobotMap.SHOOTER_HIGH_SPEED_STATE:
			SmartDashboard.putString("Shooter State", "High Speed");
			if (checkShooterSpeedButton()) { // if you change the speed
				goToCorrectSpeed(); // go to the correct state
			} else {
				if (shooterHigh() && !Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_USE_SHOOTER_BUTTON)) {
					RobotMap.shooterState = RobotMap.SHOOTER_SPOON_UP_STATE;
				}
			}
			break;
		case RobotMap.SHOOTER_SPOON_UP_STATE:
			SmartDashboard.putString("Shooter State", "Spoon Up");
			spoonPiston.set(DoubleSolenoid.Value.kReverse);
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			} else {
				if (System.currentTimeMillis() >= startTime + RobotMap.SHOOTER_DELAY_TIME) {
					RobotMap.shooterState = RobotMap.SHOOTER_STOP_SHOOTER_STATE;
					startTime = -1;
				}
			}
			break;
		case RobotMap.SHOOTER_STOP_SHOOTER_STATE:
			SmartDashboard.putString("Shooter State", "Stop Shooter");
			shooterStop();
			RobotMap.shooterState = RobotMap.SHOOTER_WAIT_STATE;
			shooterSpeed = MED_SPEED;
			break;
		} // end shooter state machine
	} // end method

	// OLD SHOOTER CODE FOR REFERENCE
	// shooterHighSpeedMotorButtonPressed =
	// joystickOp.getRawButton(RobotMap.JOYSTICK_START_HIGH_SPEED_SHOOTER_BUTTON);
	// shooterMediumSpeedMotorButtonPressed =
	// joystickOp.getRawButton(RobotMap.JOYSTICK_START_MEDIUM_SPEED_SHOOTER_BUTTON);
	// shooterSlowSpeedMotorButtonPressed =
	// joystickOp.getRawButton(RobotMap.JOYSTICK_START_LOW_SPEED_SHOOTER_BUTTON);
	// shooterStopMotorButtonPressed =
	// joystickOp.getRawButton(RobotMap.JOYSTICK_STOP_SHOOTER_BUTTON);
	// if (shooterHighSpeedMotorButtonPressed) { // 2800 Hz
	//// shooterSpeed = RobotMap.SHOOTER_FAST_SPEED;
	// pidBottomShooter.setSetpoint(RobotMap.HIGH_SPEED_SHOOTER_MOTOR_SETPOINT);
	// pidTopShooter.setSetpoint(RobotMap.HIGH_SPEED_SHOOTER_MOTOR_SETPOINT);
	// pidBottomShooter.setPID(RobotMap.P_HIGH_SPEED_SHOOTER,
	// RobotMap.I_HIGH_SPEED_SHOOTER, RobotMap.D_HIGH_SPEED_SHOOTER);
	// pidTopShooter.setPID(RobotMap.P_HIGH_SPEED_SHOOTER,
	// RobotMap.I_HIGH_SPEED_SHOOTER, RobotMap.D_HIGH_SPEED_SHOOTER);
	// pidBottomShooter.enable();
	// pidTopShooter.enable();
	//
	// } else if (shooterMediumSpeedMotorButtonPressed) { // 1600 Hz
	//// shooterSpeed = RobotMap.SHOOTER_MEDIUM_SPEED;
	// pidBottomShooter.setSetpoint(RobotMap.MEDIUM_SPEED_SHOOTER_MOTOR_SETPOINT);
	// pidTopShooter.setSetpoint(RobotMap.MEDIUM_SPEED_SHOOTER_MOTOR_SETPOINT);
	// pidBottomShooter.setPID(RobotMap.P_MEDIUM_SPEED_SHOOTER,
	// RobotMap.I_MEDIUM_SPEED_SHOOTER, RobotMap.D_MEDIUM_SPEED_SHOOTER);
	// pidTopShooter.setPID(RobotMap.P_MEDIUM_SPEED_SHOOTER,
	// RobotMap.I_MEDIUM_SPEED_SHOOTER, RobotMap.D_MEDIUM_SPEED_SHOOTER);
	// pidBottomShooter.enable();
	// pidTopShooter.enable();
	// } else if (shooterSlowSpeedMotorButtonPressed) { // 700 Hz
	//// shooterSpeed = RobotMap.SHOOTER_SLOW_SPEED;
	// pidBottomShooter.setSetpoint(RobotMap.LOW_SPEED_SHOOTER_MOTOR_SETPOINT);
	// pidTopShooter.setSetpoint(RobotMap.LOW_SPEED_SHOOTER_MOTOR_SETPOINT);
	// pidBottomShooter.setPID(RobotMap.P_LOW_SPEED_SHOOTER,
	// RobotMap.I_LOW_SPEED_SHOOTER, RobotMap.D_LOW_SPEED_SHOOTER);
	// pidTopShooter.setPID(RobotMap.P_LOW_SPEED_SHOOTER,
	// RobotMap.I_LOW_SPEED_SHOOTER, RobotMap.D_LOW_SPEED_SHOOTER);
	// pidBottomShooter.enable();
	// pidTopShooter.enable();
	//
	// } else if (shooterStopMotorButtonPressed) {
	//// shooterSpeed = 0;
	// pidTopShooter.disable();
	// pidBottomShooter.disable();
	//// backLeftMotor.set(0);
	//// backRightMotor.set(0);
	// frontLeftMotor.set(0);
	// frontRightMotor.set(0);
	// }
	// shooterBottomMotor.set(shooterSpeed);
	// shooterTopMotor.set(shooterSpeed);
	// frontLeftMotor.set(shooterSpeed);
	// frontRightMotor.set(shooterSpeed);
	// backLeftMotor.set(shooterSpeed);
	// backRightMotor.set(shooterSpeed);

	// backLeftMotor.set(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
	// backRightMotor.set(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
}
