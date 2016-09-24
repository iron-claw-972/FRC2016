package org.usfirst.frc.team972.robot;

public class RobotMap {
	/*
	 * Constants style guide:
	 * 
	 * Define all constants in capital letters with underscores between each
	 * word. End the name of the constant with a general word describing what it
	 * is, like CHANNEL.
	 * 
	 * 
	 * Always define a constant as a public static final int or double public
	 * means that any class can access the constant, not just RobotMap static
	 * means that you don't have to make an instance of RobotMap to access the
	 * constant final means that it cannot be changed int means that your
	 * constant is an integer and double means that your constant is a decimal
	 */

	// JOYSTICKS

	public static final int JOYSTICK_LEFT_USB_PORT = 0;
	public static final int JOYSTICK_RIGHT_USB_PORT = 1;
	public static final int JOYSTICK_OP_USB_PORT = 2;

	// CAN IDs

	public static final int FRONT_LEFT_MOTOR_CAN_ID = 1;
	public static final int FRONT_RIGHT_MOTOR_CAN_ID = 2;
	public static final int BACK_LEFT_MOTOR_CAN_ID = 3;
	public static final int BACK_RIGHT_MOTOR_CAN_ID = 4;
	public static final int SHOOTER_BOTTOM_MOTOR_CAN_ID = 5;
	public static final int SHOOTER_TOP_MOTOR_CAN_ID = 6;
	public static final int INTAKE_MOTOR_CAN_ID = 7;
	public static final int FLIPPY_MOTOR_CAN_ID = 8;
	public static final int PCM_CAN_ID = 10;

	// DIO

	public static final int LEFT_DRIVE_ENCODER_DIO_A_PORT = 0;
	public static final int LEFT_DRIVE_ENCODER_DIO_B_PORT = 1;
	public static final int RIGHT_DRIVE_ENCODER_DIO_A_PORT = 2;
	public static final int RIGHT_DRIVE_ENCODER_DIO_B_PORT = 3;
	// NOTHING IN 4
	public static final int FLIPPY_THING_UPPER_LIMIT_SWITCH = 5;
	public static final int FLIPPY_THING_LOWER_LIMIT_SWITCH = 6;
	public static final int BALL_OPTICAL_SENSOR_PORT = 7;

	// BUTTONS

	// LEFT JOYSTICK

	public static final int JOYSTICK_CAMERA_TOGGLE_BUTTON = 1;
	public static final int JOYSTICK_BRAKE_MODE_BUTTON = 2;

	// RIGHT JOYSTICK

	public static final int JOYSTICK_GEARSHIFT_BUTTON = 1;
	public static final int JOYSTICK_TOGGLE_BRAKE_COAST_BUTTON = 2;
	public static final int JOYSTICK_BRAKE_BUTTON = 3;
	public static final int JOYSTICK_COAST_BUTTON = 5;

	// OPERATOR JOYSTICK

	public static final int JOYSTICK_USE_SHOOTER_BUTTON = 1;
	public static final int JOYSTICK_START_INTAKE_BUTTON = 2;
	public static final int JOYSTICK_REVERSE_INTAKE_BUTTON = 7;
	public static final int JOYSTICK_START_HIGH_SPEED_SHOOTER_BUTTON = 4;
	public static final int JOYSTICK_START_MEDIUM_SPEED_SHOOTER_BUTTON = 3;
	public static final int JOYSTICK_START_LOW_SPEED_SHOOTER_BUTTON = 5;
	public static final int JOYSTICK_STOP_SHOOTER_BUTTON = 6;
	public static final int JOYSTICK_REVERSE_SHOOTER_BUTTON = 9;
	public static final int JOYSTICK_FLIPPY_MOTOR_MANUAL_OVERRIDE_BUTTON = 11;
	public static final int JOYSTICK_KEEP_FLIPPY_UP_BUTTON = 12;

	// SPEEDS

	public static final double SHOOTER_TOP_HIGH_SPEED = -0.8;
	public static final double SHOOTER_BOTTOM_HIGH_SPEED = 0.75;
	public static final double SHOOTER_TOP_MEDIUM_SPEED = -0.625;
	public static final double SHOOTER_BOTTOM_MEDIUM_SPEED = 0.575;
	public static final double SHOOTER_TOP_LOW_SPEED = -0.4;
	public static final double SHOOTER_BOTTOM_LOW_SPEED = 0.35;
	public static final double SHOOTER_TOP_REVERSE_SPEED = 0.3;
	public static final double SHOOTER_BOTTOM_REVERSE_SPEED = -0.3;

	// shooter speeds will not happen in actual bot because of PID
	public static final double INTAKE_MOTOR_SPEED = 0.8;
	public static final double INTAKE_REVERSE_MOTOR_SPEED = -1.0;
	public static final double OBSTACLE_MOTOR_SPEED = 0.2;

	// DRIVE MULTIPLIERS
	public static final double DEFAULT_DRIVE_MODE = 1.0; //was previously DRIVE_MODE_5;

	// PISTONS

	public static final int PISTON_GEARBOX_SHIFTING_FORWARD_CHANNEL = 0;
	public static final int PISTON_GEARBOX_SHIFTING_REVERSE_CHANNEL = 1;
	public static final int PISTON_BALL_PUSHER_FORWARD_CHANNEL = 2;
	public static final int PISTON_BALL_PUSHER_REVERSE_CHANNEL = 3;
	public static final int PISTON_OUTTAKE_FORWARD_CHANNEL = 5;
	public static final int PISTON_OUTTAKE_REVERSE_CHANNEL = 4;

	// SHOOTER PISTON STATE MACHINE
	public static final int DO_NOTHING_STATE = 1;
	public static final int SHOOTER_PISTON_UP_STATE = 2;
	public static final int SHOOTER_PISTON_DOWN_STATE = 3;
	public static int currentState = DO_NOTHING_STATE;

	// AUTONOMOUS MODE STATES
	public static final int DO_NOTHING_MODE = 0;
	public static final int AUTO_CROSS_OBSTACLE_MODE = 1;
	public static final int LOW_BAR_SHOOT_MODE = 2;
	public static final int NO_ENCODER_CROSS_OBSTACLE_MODE = 3;
	public static int autonomousMode = AUTO_CROSS_OBSTACLE_MODE; // default mode

	// AUTONOMOUS DRIVE SPEED
	public static final double CROSS_OBSTACLE_DRIVE_SPEED = 0.7;
	public static final double TURN_DRIVE_SPEED = 0.4;
	public static final double DRIVE_TO_GOAL_SPEED = 0.7;
	
	// AUTONOMOUS DEFENSE DRIVE DISTANCE
	public static final int CROSS_OBSTACLE_DRIVE_DISTANCE = 2000;
	public static final int LOW_BAR_DRIVE_DISTANCE = 2000;
	public static final int LOW_BAR_TURN_TO_GOAL_DISTANCE = 700;
	public static final int DRIVE_TO_GOAL_DISTANCE = 3000;

	// SHOOTER MOTOR PID

	public static final double HIGH_SPEED_SHOOTER_MOTOR_SETPOINT = 2800.0;
	// P=0.001, I=0.009, D=0.000
	public static final double MEDIUM_SPEED_SHOOTER_MOTOR_SETPOINT = 1600.0;
	// P=0.001, I=0.024, D=0.000
	public static final double LOW_SPEED_SHOOTER_MOTOR_SETPOINT = 700.0;
	// P=0.001, I=0.024, D=0.000

	// P is very low; increasing causes violent oscillation
	// I increases as setpoint decreases; this is to prevent the rate from
	// reversing and making robot oscillate between directions
	// D is not necessary and does not help oscillation

	public static final double P_LOW_SPEED_SHOOTER = 0.001;
	public static final double I_LOW_SPEED_SHOOTER = 0.024;
	public static final double D_LOW_SPEED_SHOOTER = 0.000;

	public static final double P_MEDIUM_SPEED_SHOOTER = 0.001;
	public static final double I_MEDIUM_SPEED_SHOOTER = 0.024;
	public static final double D_MEDIUM_SPEED_SHOOTER = 0.000;

	public static final double P_HIGH_SPEED_SHOOTER = 0.001;
	public static final double I_HIGH_SPEED_SHOOTER = 0.009;
	public static final double D_HIGH_SPEED_SHOOTER = 0.000;

	public static final double P_BRAKE = 0.003;
	public static final double I_BRAKE = 0.000;
	public static final double D_BRAKE = 0.000;

	// INTAKE STATE MACHINE

	public static final int INTAKE_WAIT_STATE = 0;
	public static final int INTAKE_SPOON_DOWN_STATE = 1;
	public static final int INTAKE_START_INTAKE_STATE = 2;
	public static final int INTAKE_WAIT_FOR_OPTICAL_STATE = 3;
	public static final int INTAKE_SPOON_UP_STATE = 4;
	public static final int INTAKE_REVERSE_SPOON_DOWN_STATE = 5;
	public static final int INTAKE_REVERSE_OUTTAKE_STATE = 6;
	public static int intakeState = INTAKE_WAIT_STATE;

	// SHOOTER STATE MACHINE

	public static final int SHOOTER_WAIT_STATE = 0;
	public static final int SHOOTER_SPOON_DOWN_STATE = 1;
	public static final int SHOOTER_SPIN_MOTORS_STATE = 2;
	public static final int SHOOTER_DELAY_AFTER_SHOOTING_STATE = 3;
	public static final int SHOOTER_STOP_SHOOTER_STATE = 4;
	public static final int SHOOTER_REVERSE_SHOOTER_STATE = 5;
	public static int shooterState = SHOOTER_WAIT_STATE;

	// CONSTANTS

	public static final int BALL_OPTICAL_DELAY_TIME = 500; // In ms
	public static final int REVERSE_OUTTAKE_DELAY_TIME = 100; // In ms
	public static final int SHOOTER_DELAY_TIME = 1500; // In ms
	public static final double SHOOTER_DEADZONE = 0.01;
	public static final int LOWER_OBSTACLE_MOTOR_TIME = 300; // In ms
	public static final double AUTONOMOUS_SLOW_DOWN_INCREMENT = 0.03;
	public static final int OUTTAKE_MOTOR_AUTO_RUN_TIME = 2000;
	public static final int AUTONOMOUS_DRIVE_OVER_OBSTACLE_TIME = 6000;

	// TESTING CONSTANTS

	// These constants are used to test various functions
	public static final boolean USE_OLD_CAM = false;
	public static final boolean USE_SHOOTER_PID = false;
	public static final int LOW_BAR_TURN_AROUND_DISTANCE = 800;

	public static boolean haveCam = true;
}
