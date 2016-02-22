package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	CANTalon topMotor, bottomMotor;
	DoubleSolenoid spoonPiston;
	double shooterBottomSpeed, shooterTopSpeed;
	long startTime = -1;

	public Shooter(CANTalon topMotor, CANTalon bottomMotor, DoubleSolenoid spoonPiston) {
		this.topMotor = topMotor;
		this.bottomMotor = bottomMotor;
		this.spoonPiston = spoonPiston;
	}

	// @return whether shooter is done or not
	void shooterStop() {
		System.out.println("Stop Shooter");
		topMotor.set(0);
		bottomMotor.set(0);
		topMotor.disable();
		bottomMotor.disable();

		shooterTopSpeed = 0;
		shooterBottomSpeed = 0;
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
	}
	
	public void runShooter() {
		topMotor.set(shooterTopSpeed);
		bottomMotor.set(shooterBottomSpeed);
	}

	public void shooterStateMachine() {
		setShooterSpeeds();
		switch (RobotMap.shooterState) {
			case RobotMap.SHOOTER_WAIT_STATE:
				SmartDashboard.putString("Shooter State", "Wait");
				shooterStop();
				if (Robot.joystickOp.getRawButton(RobotMap.JOYSTICK_USE_SHOOTER_BUTTON)) {
					RobotMap.shooterState = RobotMap.SHOOTER_SPOON_DOWN_STATE;
				}
				break;
			case RobotMap.SHOOTER_SPOON_DOWN_STATE:
				SmartDashboard.putString("Shooter State", "Spoon Down");
				shooterStop();
				spoonPiston.set(DoubleSolenoid.Value.kForward);
				break;
			case RobotMap.SHOOTER_SPIN_MOTORS_STATE:
				SmartDashboard.putString("Shooter State", "Spin");
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
			case RobotMap.SHOOTER_STOP_SHOOTER_STATE:
				SmartDashboard.putString("Shooter State", "Stop Shooter");
				shooterStop();
				RobotMap.shooterState = RobotMap.SHOOTER_WAIT_STATE;
				shooterBottomSpeed = 0;
				shooterTopSpeed = 0;
				break;
		} // end shooter state machine
	} // end method
}
