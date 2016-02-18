package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class Intake {
	CANTalon intakeMotor;
	
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
}
