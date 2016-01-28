package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.Encoder;

public class RobotMap {
	/*
	 * Constants style guide:
	 * 
	 * Define all constants in capital letters with underscores between each word.
	 * End the name of the constant with a general word describing what it is, like CHANNEL.
	 * 
	 * Always define a constant as a public static final int or double
	 * public means that any class can access the constant, not just RobotMap
	 * static means that you don't have to make an instance of RobotMap to access the constant
	 * final means that it cannot be changed
	 * int means that your constant is an integer and double means that your constant is a decimal
	 */
	
	 // JOYSTICKS
	
	 public static final int JOYSTICK_LEFT_USB_PORT = 0;
	 public static final int JOYSTICK_RIGHT_USB_PORT = 1;
	 public static final int JOYSTICK_OP_USB_PORT = 2;
	 
	 // MOTORS
	 
	 public static final int FRONT_LEFT_MOTOR_CAN_ID = 1;
	 public static final int FRONT_RIGHT_MOTOR_CAN_ID = 2;
	 public static final int BACK_LEFT_MOTOR_CAN_ID = 3;
	 public static final int BACK_RIGHT_MOTOR_CAN_ID = 4;
	 public static final int SHOOTER_BOTTOM_MOTOR_CAN_ID = 5;
	 public static final int SHOOTER_TOP_MOTOR_CAN_ID = 6;
	 public static final int INTAKE_MOTOR_CAN_ID = 7;
	 public static final int OBSTACLE_MOTOR_CAN_ID = 8;
	 
	 // ENCODERS
	 
	 public static final int LEFT_DRIVE_ENCODER_DIO_A_PORT = 0;
	 public static final int LEFT_DRIVE_ENCODER_DIO_B_PORT = 1;
	 public static final int RIGHT_DRIVE_ENCODER_DIO_A_PORT = 2;
	 public static final int RIGHT_DRIVE_ENCODER_DIO_B_PORT = 3;
	 public static final int SHOOTER_BOTTOM_ENCODER_DIO_A_PORT = 4;
	 public static final int SHOOTER_BOTTOM_ENCODER_DIO_B_PORT = 5;
	 public static final int SHOOTER_TOP_ENCODER_DIO_A_PORT = 6;
	 public static final int SHOOTER_TOP_ENCODER_DIO_B_PORT = 7;
	 
	 // BUTTONS
	 
	 public static final int JOYSTICK_SPEED_TOGGLE_BUTTON = 1;
	 
	 // VALUES
	 
	 public static final double STARTING_DRIVE_MULTIPLIER = 0.5;
	 public static final double FAST_MODE_DRIVE_MULTIPLIER = 0.8;
}
