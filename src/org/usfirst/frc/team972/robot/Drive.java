package org.usfirst.frc.team972.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	RobotDrive botDrive;
	CANTalon frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
	boolean gearboxPistonForward = false;
	double driveP, driveI, driveD;

	public Drive(RobotDrive botDrive, CANTalon frontLeftMotor, CANTalon frontRightMotor, CANTalon backLeftMotor,
			CANTalon backRightMotor) {
		this.botDrive = botDrive;
		this.frontLeftMotor = frontLeftMotor;
		this.frontRightMotor = frontRightMotor;
		this.backLeftMotor = backLeftMotor;
		this.backRightMotor = backRightMotor;
	}

	// TODO Take out PID
	public void straightDrive(PIDController leftPID, PIDController rightPID, double leftSpeed, double rightSpeed) {
		// frontLeftMotor.changeControlMode(TalonControlMode.PercentVbus);
		// frontRightMotor.changeControlMode(TalonControlMode.PercentVbus);
		// backLeftMotor.changeControlMode(TalonControlMode.Follower);
		// backRightMotor.changeControlMode(TalonControlMode.Follower);
		// backLeftMotor.set(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
		// backRightMotor.set(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
		// frontLeftMotor.set((leftSpeed + rightSpeed) / 2);
		// frontRightMotor.set((leftSpeed + rightSpeed) / 2);
		double speed = (leftSpeed + rightSpeed) / 2;
		Robot.botDrive.tankDrive(speed, speed);

		// below code is frickin stupid
		// leftPID.setSetpoint((rightSpeed + leftSpeed)/2);
		// rightPID.setSetpoint((rightSpeed + leftSpeed)/2);
		// driveP = (((Robot.joystickLeft.getZ() * -1) + 1) / 2.0) * 0.01;
		// driveI = (((Robot.joystickRight.getZ() * -1) + 1) / 2.0) * 0.01;
		// driveD = (((Robot.joystickOp.getThrottle() * -1) + 1) / 2.0) * 0.01;
		// rightPID.setPID(driveP, driveI, driveD);
		// leftPID.setPID(driveP, driveI, driveD);
		// rightPID.enable();
		// leftPID.enable();
		// SmartDashboard.putNumber("Drive P", driveP);
		// SmartDashboard.putNumber("Drive I", driveI);
		// SmartDashboard.putNumber("Drive D", driveD);
	}

	public void reverse() {
		Robot.rearCam = !Robot.rearCam;
	}

	public void pidBrake(boolean pidMode, PIDController pidLeftDrive, PIDController pidRightDrive,
			Encoder leftDriveEncoder, Encoder rightDriveEncoder, double kP, double kI, double kD) {
		if (!(pidMode)) {
			botDrive.setSafetyEnabled(false); // stops "Robot Drive not updated
												// enough" error during PID
			pidMode = true;

			pidLeftDrive.reset(); // resets past error
			pidRightDrive.reset();

			leftDriveEncoder.reset();
			rightDriveEncoder.reset();

			pidLeftDrive.setSetpoint(0);
			pidRightDrive.setSetpoint(0);

			// SmartDashboard.putNumber("Left Error", pidLeftDrive.getError());
			// //Not used by Drive Team.
			// SmartDashboard.putNumber("Right Error",
			// pidRightDrive.getError());

			// this sets all the motors except front left to be followers
			// this way they will do the same thing that the front left motor
			// does
			// the front left motor is controlled by the PID Controller object

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

	public void switchModes(
			DoubleSolenoid gearboxPiston/*
										 * , DoubleSolenoid gearboxPistonRight
										 */) { // we used to have two solenoids;
												// now we have one
		if (gearboxPistonForward == false) {
			gearboxPiston.set(DoubleSolenoid.Value.kForward);
			gearboxPistonForward = true;
			SmartDashboard.putString("Gear", "High");
		} else {
			gearboxPiston.set(DoubleSolenoid.Value.kReverse);
			gearboxPistonForward = false;
			SmartDashboard.putString("Gear", "Low");
		}
	}

	public void switchToLowGear(DoubleSolenoid gearboxPiston) {
		gearboxPiston.set(DoubleSolenoid.Value.kReverse);
		gearboxPistonForward = true;
		SmartDashboard.putString("Gear", "Low");
	}
}