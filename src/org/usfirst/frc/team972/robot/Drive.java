package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	RobotDrive botDrive;
	CANTalon frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
	boolean gearboxPistonForward = false;
	
	public Drive(RobotDrive botDrive, CANTalon frontLeftMotor, CANTalon frontRightMotor, CANTalon backLeftMotor, CANTalon backRightMotor) {
		this.botDrive = botDrive;
		this.frontLeftMotor = frontLeftMotor;
		this.frontRightMotor = frontRightMotor;
		this.backLeftMotor = backLeftMotor;
		this.backRightMotor = backRightMotor;
	}
	
	public double setDriveMultiplier(double originalDriveMultiplier) {
		if (Robot.joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_1_BUTTON)) {
			return RobotMap.DRIVE_MODE_1;
		} else if (Robot.joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_2_BUTTON)) {
			return RobotMap.DRIVE_MODE_2;
		} else if (Robot.joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_3_BUTTON)) {
			return RobotMap.DRIVE_MODE_3;
		} else if (Robot.joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_4_BUTTON)) {
			return RobotMap.DRIVE_MODE_4;
		} else if (Robot.joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_5_BUTTON)) {
			return RobotMap.DRIVE_MODE_5;
		} else {
			return originalDriveMultiplier;
		}
	}
	
	public void pidBrake(boolean pidMode, PIDController pidLeftDrive, PIDController pidRightDrive, Encoder leftDriveEncoder, Encoder rightDriveEncoder, double kP, double kI, double kD) {
		if (!(pidMode)) {
			botDrive.setSafetyEnabled(false); // stops "Robot Drive not updated enough" error during PID
			pidMode = true;

			pidLeftDrive.reset(); // resets past error
			pidRightDrive.reset();

			leftDriveEncoder.reset();
			rightDriveEncoder.reset();

			pidLeftDrive.setSetpoint(0);
			pidRightDrive.setSetpoint(0);

			SmartDashboard.putNumber("Left Error", pidLeftDrive.getError());
			SmartDashboard.putNumber("Right Error", pidRightDrive.getError());

			// this sets all the motors except front left to be followers
			// this way they will do the same thing that the front left
			// motor does
			// the front left motor is controlled by the PID Controller
			// object
			backRightMotor.changeControlMode(TalonControlMode.Follower);
			backLeftMotor.changeControlMode(TalonControlMode.Follower);
		}
		// has the other motors follow the PID controlled motor
		backRightMotor.set(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
		backLeftMotor.set(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
		pidLeftDrive.setPID(kP, kI, kD);
		pidRightDrive.setPID(kP, kI, kD);
		pidLeftDrive.enable();
		pidRightDrive.enable();
	}
	
	public void stopPIDBrake(boolean pidMode, PIDController pidLeftDrive, PIDController pidRightDrive) {
		pidMode = false;
		botDrive.setSafetyEnabled(true);
		backRightMotor.changeControlMode(TalonControlMode.PercentVbus);
		backLeftMotor.changeControlMode(TalonControlMode.PercentVbus);
		pidLeftDrive.disable();
		pidRightDrive.disable();
	}
	
	public void tankDrive(double leftDriveSpeed, double rightDriveSpeed) {
		botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
	}

	public void arcadeDrive(double moveDriveSpeed, double rotateDriveSpeed) {
		botDrive.arcadeDrive(moveDriveSpeed, rotateDriveSpeed);
	}
	
	public void switchModes(DoubleSolenoid gearboxPistonLeft/*, DoubleSolenoid gearboxPistonRight*/) {
		if (gearboxPistonForward == false) {
			gearboxPistonLeft.set(DoubleSolenoid.Value.kForward);
//			gearboxPistonRight.set(DoubleSolenoid.Value.kReverse);
			gearboxPistonForward = true;
			SmartDashboard.putNumber("Current Gear", 1);
		} else {
			gearboxPistonLeft.set(DoubleSolenoid.Value.kReverse);
//			gearboxPistonRight.set(DoubleSolenoid.Value.kForward);
			gearboxPistonForward = false;
			SmartDashboard.putNumber("Current Gear", 2);
		}
	}
}