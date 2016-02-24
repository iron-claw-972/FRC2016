package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	CANTalon topMotor, bottomMotor;
	DoubleSolenoid spoonPiston;
	double shooterBottomSpeed = RobotMap.SHOOTER_BOTTOM_HIGH_SPEED, shooterTopSpeed = RobotMap.SHOOTER_TOP_HIGH_SPEED;
	long startTime = -1;

	public Shooter(CANTalon topMotor, CANTalon bottomMotor, DoubleSolenoid spoonPiston) {
		this.topMotor = topMotor;
		this.bottomMotor = bottomMotor;
		this.spoonPiston = spoonPiston;
	}

	void shooterStop() {
		topMotor.reset();
		bottomMotor.reset();
		topMotor.set(0);
		bottomMotor.set(0);
	}

	// boolean setShooterPID(double setpoint) {
	// double kP = (((Robot.joystickLeft.getZ() * -1) + 1) / 2.0) * 0.01;
	// double kI = (((Robot.joystickRight.getZ() * -1) + 1) / 2.0) * 0.01;
	// double kD = (((Robot.joystickOp.getThrottle() * -1) + 1) / 2.0) * 0.01;
	//
	// SmartDashboard.putNumber("Shooter P", kP);
	// SmartDashboard.putNumber("Shooter I", kI);
	// SmartDashboard.putNumber("Shooter D", kD);
	//
	// double topSetpoint = -setpoint;
	// double bottomSetpoint = setpoint;
	//
	// topMotor.changeControlMode(TalonControlMode.Speed);
	// bottomMotor.changeControlMode(TalonControlMode.Speed);
	//
	// topMotor.set(topSetpoint);
	// bottomMotor.set(bottomSetpoint);
	//
	// topMotor.setSetpoint(topSetpoint);
	// bottomMotor.setSetpoint(bottomSetpoint);
	//
	// topMotor.setPID(kP, kI, kD);
	// bottomMotor.setPID(kP, kI, kD);
	//
	// SmartDashboard.putNumber("Top Error", topMotor.get() - topSetpoint);
	// SmartDashboard.putNumber("Bottom Error", bottomMotor.get() -
	// bottomSetpoint);
	//
	// boolean topMotorCorrect = topMotor.get() <= topSetpoint +
	// RobotMap.SHOOTER_DEADZONE
	// && topMotor.get() >= topSetpoint - RobotMap.SHOOTER_DEADZONE;
	// boolean bottomMotorCorrect = bottomMotor.get() <= bottomSetpoint +
	// RobotMap.SHOOTER_DEADZONE
	// && bottomMotor.get() >= bottomSetpoint - RobotMap.SHOOTER_DEADZONE;
	// boolean doneSettingSpeed = topMotorCorrect && bottomMotorCorrect;
	// if (doneSettingSpeed) {
	// shooterStop();
	// }
	// return doneSettingSpeed;
	// }
	//
	// void setShooterNoPID(double speed) {
	// topMotor.changeControlMode(TalonControlMode.PercentVbus);
	// bottomMotor.changeControlMode(TalonControlMode.PercentVbus);
	//
	// topMotor.set(-speed);
	// bottomMotor.set(speed);
	// }

	// @return whether shooter speed is changed
	public void setShooterSpeeds() {
		if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_LOW_SPEED_SHOOTER_BUTTON)) {
			shooterBottomSpeed = RobotMap.SHOOTER_BOTTOM_LOW_SPEED;
			shooterTopSpeed = RobotMap.SHOOTER_TOP_LOW_SPEED;
			SmartDashboard.putString("Shooter Speed", "Low");
		}
		if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_MEDIUM_SPEED_SHOOTER_BUTTON)) {
			shooterBottomSpeed = RobotMap.SHOOTER_BOTTOM_MEDIUM_SPEED;
			shooterTopSpeed = RobotMap.SHOOTER_TOP_MEDIUM_SPEED;
			SmartDashboard.putString("Shooter Speed", "Medium");
		}
		if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_START_HIGH_SPEED_SHOOTER_BUTTON)) {
			shooterBottomSpeed = RobotMap.SHOOTER_BOTTOM_HIGH_SPEED;
			shooterTopSpeed = RobotMap.SHOOTER_TOP_HIGH_SPEED;
			SmartDashboard.putString("Shooter Speed", "High");
		}
		if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_STOP_SHOOTER_BUTTON)) {
			shooterTopSpeed = 0;
			shooterBottomSpeed = 0;
			SmartDashboard.putString("Shooter Speed", "Stopped");
		}
	}
	
	public void runShooter() {
		topMotor.enable();
		bottomMotor.enable();
//		topMotor.set(-1*shooterTopSpeed);
//		bottomMotor.set(shooterBottomSpeed);
		double kP = (((Robot.joystickLeft.getZ() * -1) + 1) / 2.0) * 0.5;
		double kI = (((Robot.joystickRight.getZ() * -1) + 1) / 2.0) * 0.01;
		double kD = (((Robot.joystickOp.getThrottle() * -1) + 1) / 2.0) * 10;
		if (Robot.joystickOp.getRawButton(11)) {
			topMotor.setP(kP);
			topMotor.setI(kI);
			topMotor.setD(kD);
		}
		if (Robot.joystickOp.getRawButton(12)) {
			bottomMotor.setP(kP);
			bottomMotor.setI(kI);
			bottomMotor.setD(kD);
		}
		SmartDashboard.putNumber("Shooter Top P", topMotor.getP());
		SmartDashboard.putNumber("Shooter Bottom P", bottomMotor.getP());
		SmartDashboard.putNumber("Shooter Top I", topMotor.getI());
		SmartDashboard.putNumber("Shooter Bottom I", bottomMotor.getI());
		SmartDashboard.putNumber("Shooter Top D", topMotor.getD());
		SmartDashboard.putNumber("Shooter Bottom D", bottomMotor.getD());
//		topMotor.set(shooterTopSpeed);
//		bottomMotor.set(shooterBottomSpeed);
		topMotor.set(shooterTopSpeed);
		bottomMotor.set(shooterBottomSpeed);
	}
	
	public void reverseShooter() {
		topMotor.enable();
		bottomMotor.enable();
		double kP = (((Robot.joystickLeft.getZ() * -1) + 1) / 2.0);
		double kI = (((Robot.joystickRight.getZ() * -1) + 1) / 2.0);
		double kD = (((Robot.joystickOp.getThrottle() * -1) + 1) / 2.0);
		if (Robot.joystickOp.getRawButton(11)) {
			topMotor.setP(kP);
			topMotor.setI(kI);
			topMotor.setD(kD);
		}
		if (Robot.joystickOp.getRawButton(12)) {
			bottomMotor.setP(kP);
			bottomMotor.setI(kI);
		}
		SmartDashboard.putNumber("Shooter Top P", topMotor.getP());
		SmartDashboard.putNumber("Shooter Bottom P", bottomMotor.getP());
		SmartDashboard.putNumber("Shooter Top I", topMotor.getI());
		SmartDashboard.putNumber("Shooter Bottom I", bottomMotor.getI());
		SmartDashboard.putNumber("Shooter Top D", topMotor.getD());
		SmartDashboard.putNumber("Shooter Bottom D", bottomMotor.getD());
		topMotor.setPID(kP, kI, kD);
		bottomMotor.setPID(kP, kI, kD);
		topMotor.set(RobotMap.SHOOTER_TOP_REVERSE_SPEED);
		bottomMotor.set(RobotMap.SHOOTER_BOTTOM_REVERSE_SPEED);
	}

	public void shooterStateMachine() {
		setShooterSpeeds();
		switch (RobotMap.shooterState) {
			case RobotMap.SHOOTER_WAIT_STATE:
				SmartDashboard.putString("Shooter State", "Wait");
				shooterStop();
				if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_USE_SHOOTER_BUTTON)) {
					RobotMap.shooterState = RobotMap.SHOOTER_SPIN_MOTORS_STATE;
					spoonPiston.set(DoubleSolenoid.Value.kForward);
					shooterStop();
				} else if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_REVERSE_SHOOTER_BUTTON)) {
					RobotMap.shooterState = RobotMap.SHOOTER_REVERSE_SHOOTER_STATE;
					spoonPiston.set(DoubleSolenoid.Value.kForward);
					shooterStop();
				}
				break;
			case RobotMap.SHOOTER_SPIN_MOTORS_STATE:
				SmartDashboard.putString("Shooter State", "Shoot");
				runShooter();
				if (!Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_USE_SHOOTER_BUTTON)) {
					RobotMap.shooterState = RobotMap.SHOOTER_DELAY_AFTER_SHOOTING_STATE;
					spoonPiston.set(DoubleSolenoid.Value.kReverse); //spoon up
				}
				break;
			case RobotMap.SHOOTER_DELAY_AFTER_SHOOTING_STATE:
				SmartDashboard.putString("Shooter State", "Spoon Up");
				runShooter();
				if (startTime == -1) {
					startTime = System.currentTimeMillis();
				} else {
					if (System.currentTimeMillis() >= startTime + RobotMap.SHOOTER_DELAY_TIME) {
						RobotMap.shooterState = RobotMap.SHOOTER_STOP_SHOOTER_STATE;
						startTime = -1;
					}
				}
				break;
			case RobotMap.SHOOTER_REVERSE_SHOOTER_STATE:
				SmartDashboard.putString("Shooter State", "Reverse Shooter");
				reverseShooter();
				if (!Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_REVERSE_SHOOTER_BUTTON)) {
					RobotMap.shooterState = RobotMap.SHOOTER_STOP_SHOOTER_STATE;
					spoonPiston.set(DoubleSolenoid.Value.kReverse); //spoon up
				}
				break;
			case RobotMap.SHOOTER_STOP_SHOOTER_STATE:
				SmartDashboard.putString("Shooter State", "Stop Shooter");
				shooterStop();
				RobotMap.shooterState = RobotMap.SHOOTER_WAIT_STATE;
				break;
		} // end shooter state machine
	} // end method
}
