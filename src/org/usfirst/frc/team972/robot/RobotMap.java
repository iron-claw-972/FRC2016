package org.usfirst.frc.team972.robot;

public class RobotMap {
	/*
	 * Constants style guide:
	 * 
	 * Define all constants in capital letters with underscores between each word.
	 * End the name of the constant with a general word describing what it is, like CHANNEL.
	 * 
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
	 
	 // CAN IDs
	 
	 public static final int FRONT_LEFT_MOTOR_CAN_ID = 1;
	 public static final int FRONT_RIGHT_MOTOR_CAN_ID = 2;
	 public static final int BACK_LEFT_MOTOR_CAN_ID = 3;
	 public static final int BACK_RIGHT_MOTOR_CAN_ID = 4;
	 public static final int SHOOTER_BOTTOM_MOTOR_CAN_ID = 5;
	 public static final int SHOOTER_TOP_MOTOR_CAN_ID = 6;
	 public static final int INTAKE_MOTOR_CAN_ID = 7; // TODO Check online
	 public static final int OBSTACLE_MOTOR_CAN_ID = 8; // TODO Check online
	 public static final int PCM_CAN_ID = 10;
	 
	 // ENCODERS
	 
	 public static final int LEFT_DRIVE_ENCODER_DIO_A_PORT = 0;
	 public static final int LEFT_DRIVE_ENCODER_DIO_B_PORT = 1;	 
	 public static final int RIGHT_DRIVE_ENCODER_DIO_A_PORT = 2;
	 public static final int RIGHT_DRIVE_ENCODER_DIO_B_PORT = 3;
	 public static final int FLIPPY_THING_UPPER_LIMIT_SWITCH = 4;
	 public static final int FLIPPY_THING_LOWER_LIMIT_SWITCH = 5;
	 public static final int BALL_OPTICAL_SENSOR_PORT = 7;
	 //TODO change shooter encoders to go with Talons
//	 public static final int SHOOTER_BOTTOM_ENCODER_DIO_A_PORT = 4;
//	 public static final int SHOOTER_BOTTOM_ENCODER_DIO_B_PORT = 5;
//	 public static final int SHOOTER_TOP_ENCODER_DIO_A_PORT = 2;
//	 public static final int SHOOTER_TOP_ENCODER_DIO_B_PORT = 3;
	 
	 // BUTTONS
	 
	 // LEFT JOYSTICK
	 
	 public static final int JOYSTICK_CAMERA_TOGGLE_BUTTON = 1;
	 public static final int JOYSTICK_SPEED_1_BUTTON = 3;
	 public static final int JOYSTICK_SPEED_2_BUTTON = 5;
	 public static final int JOYSTICK_SPEED_3_BUTTON = 2;
	 public static final int JOYSTICK_SPEED_4_BUTTON = 6;
	 public static final int JOYSTICK_SPEED_5_BUTTON = 4;

	 // RIGHT JOYSTICK
	 
	 public static final int JOYSTICK_BRAKE_MODE_BUTTON = 1;
	 public static final int JOYSTICK_GEARSHIFT_BUTTON = 2;
	 public static final int JOYSTICK_SPLIT_ARCADE_DRIVE_BUTTON = 3;
	 public static final int JOYSTICK_DRIVE_SET_DISTANCE_BUTTON = 5;
	 
	 // OPERATOR JOYSTICK
	
	 public static final int JOYSTICK_USE_SHOOTER_BUTTON = 1;
	 public static final int JOYSTICK_START_INTAKE_BUTTON = 2;
	 public static final int JOYSTICK_REVERSE_INTAKE_BUTTON = 7;
	 public static final int JOYSTICK_START_HIGH_SPEED_SHOOTER_BUTTON = 4;
	 public static final int JOYSTICK_START_MEDIUM_SPEED_SHOOTER_BUTTON = 3;
	 public static final int JOYSTICK_START_LOW_SPEED_SHOOTER_BUTTON = 5;
	 public static final int JOYSTICK_STOP_SHOOTER_BUTTON = 6;
	 
	 // SPEEDS
	 
	 public static final double SHOOTER_FAST_SPEED = 1;
	 public static final double SHOOTER_MEDIUM_SPEED = 0.6;
	 public static final double SHOOTER_SLOW_SPEED = 0.3;
	 //shooter speeds will not happen in actual bot because of PID
	 public static final double INTAKE_MOTOR_SPEED = 0.3;
	 public static final double INTAKE_REVERSE_MOTOR_SPEED = -0.3;
	 public static final double OBSTACLE_MOTOR_SPEED = 0.2;
	 
	 // DRIVE MULTIPLIERS
	 
	 public static final double DRIVE_MODE_1 = 0.6;// TODO 0.6
	 public static final double DRIVE_MODE_2 = 0.7;// TODO 0.7
	 public static final double DRIVE_MODE_3 = 0.8;// TODO 0.8
	 public static final double DRIVE_MODE_4 = 0.9;// TODO 0.9
	 public static final double DRIVE_MODE_5 = 1.0;// TODO 1.0
	 public static final double DEFAULT_DRIVE_MODE = DRIVE_MODE_5;
	 
	 // PISTONS
	 
	 public static final int PISTON_GEARBOX_LEFT_SHIFTING_FORWARD_CHANNEL = 0;
	 public static final int PISTON_GEARBOX_LEFT_SHIFTING_REVERSE_CHANNEL = 1;
	 public static final int PISTON_GEARBOX_RIGHT_SHIFTING_FORWARD_CHANNEL = 2;
	 public static final int PISTON_GEARBOX_RIGHT_SHIFTING_REVERSE_CHANNEL = 3;
	 public static final int PISTON_BALL_PUSHER_FORWARD_CHANNEL = 0;
	 public static final int PISTON_BALL_PUSHER_REVERSE_CHANNEL = 1;
	 
	 // SHOOTER PISTON STATE MACHINE
	 public static final int DO_NOTHING_STATE = 1;
	 public static final int SHOOTER_PISTON_UP_STATE = 2;
	 public static final int SHOOTER_PISTON_DOWN_STATE = 3;
	 public static int currentState = DO_NOTHING_STATE;
	 
	 // AUTONOMOUS DEFENSES
	 
	 public static final int LOW_BAR_MODE = 0;
	 public static final int PORTCULLIS_MODE = 1;
	 public static final int CHEVAL_DE_FRISE_MODE = 2;
	 public static final int MOAT_MODE = 3;
	 public static final int RAMPARTS_MODE = 4;
	 public static final int DRAWBRIDGE_MODE = 5;
	 public static final int SALLY_PORT_MODE = 6;
	 public static final int ROCK_WALL_MODE = 7;
	 public static final int ROUGH_TERRAIN_MODE = 8;
	 public static final int DO_NOTHING_MODE = 9;
	 
	 // AUTONOMOUS DELAYS
	 
	 public static final int NO_DELAY = 0;
	 public static final int TWO_SECOND_DELAY = 1;
	 public static final int FOUR_SECOND_DELAY = 2;
	 public static final int SIX_SECOND_DELAY = 3;
	 
	 // AUTONOMOUS START POSITIONS
	 
	 public static final int POSITION_SPY = 0;
	 public static final int POSITION_1 = 1;
	 public static final int POSITION_2 = 2;
	 public static final int POSITION_3 = 3;
	 public static final int POSITION_4 = 4;
	 public static final int POSITION_5 = 5;

	 // AUTONOMOUS SHOOTER MODES
	 
	 public static final int DO_NOT_SHOOT = 0;
	 public static final int SHOOTER_LEFT_HIGH_GOAL = 1;
	 public static final int SHOOTER_CENTER_HIGH_GOAL = 2;
	 public static final int SHOOTER_RIGHT_HIGH_GOAL = 3;
	 public static final int ASSIST_SHOOT = 4;
	 
	 // AUTONOMOUS MODES
	 
	 public static int autonomousDefenseMode = LOW_BAR_MODE;
	 public static int autonomousDelayMode = NO_DELAY;
	 public static int autonomousShooterMode = DO_NOT_SHOOT;
	 public static int autonomousPositionMode = POSITION_1;
	
	 // AUTONOMOUS DRIVE SPEEDS
	 
	 public static final double LOW_BAR_LEFT_DRIVE_SPEED = 0.5;
	 public static final double LOW_BAR_RIGHT_DRIVE_SPEED = 0.5;
	 public static final int LOW_BAR_LEFT_DRIVE_DISTANCE = 500;
	 public static final int LOW_BAR_RIGHT_DRIVE_DISTANCE = 500;
	 
	 // AUTONOMOUS DEFENSE DRIVE DISTANCE
	 
	 public static final int LOW_BAR_DEFENSE_DRIVE_DISTANCE = 12; // TODO add actual number
	 public static final int MOAT_DEFENSE_DRIVE_DISTANCE = 28; // TODO add actual number
	 public static final int RAMPARTS_DEFENSE_DRIVE_DISTANCE = 62; // TODO add actual number
	 public static final int DRAWBRIDGE_DEFENSE_DRIVE_DISTANCE = 39; // TODO add actual number
	 public static final int SALLY_PORT_DEFENSE_DRIVE_DISTANCE = 72; // TODO add actual number
	 public static final int ROCK_WALL_DEFENSE_DRIVE_DISTANCE = 93; // TODO add actual number
	 public static final int ROUGH_TERRAIN_DEFENSE_DRIVE_DISTANCE = 2; // TODO add actual number
	 
	 // SHOOTER MOTOR PID
	 
	 public static final double HIGH_SPEED_SHOOTER_MOTOR_SETPOINT = 2800.0; // P=0.001, I=0.009, D=0.000
	 public static final double MEDIUM_SPEED_SHOOTER_MOTOR_SETPOINT = 1600.0;// P=0.001, I=0.024, D=0.000
	 public static final double LOW_SPEED_SHOOTER_MOTOR_SETPOINT = 700.0;// P=0.001, I=0.024, D=0.000
	 // P is very low; increasing causes violent oscillation
	 // I increases as setpoint decreases; this is to prevent the rate from reversing and making robot oscillate between directions
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
	 public static final int INTAKE_REVERSE_STATE = 5;
	 public static int intakeState = INTAKE_WAIT_STATE;
	 
	 // SHOOTER STATE MACHINE
	 
	 public static final int SHOOTER_WAIT_STATE = 0;
	 public static final int SHOOTER_SPOON_DOWN_STATE = 1;
	 public static final int SHOOTER_LOW_SPEED_STATE = 2;
	 public static final int SHOOTER_MEDIUM_SPEED_STATE = 3;
	 public static final int SHOOTER_HIGH_SPEED_STATE = 4;
	 public static final int SHOOTER_SPOON_UP_STATE = 5;
	 public static final int SHOOTER_STOP_SHOOTER_STATE = 6;
	 public static int shooterState = SHOOTER_WAIT_STATE;
	 
	 
	 // CONSTANTS
	 
	 public static final int BALL_OPTICAL_DELAY_TIME = 500; // In ms
	 public static final int REVERSE_OPTICAL_DELAY_TIME = 1000; // In ms
	 public static final int SHOOTER_DELAY_TIME = 1000; // In ms
}
