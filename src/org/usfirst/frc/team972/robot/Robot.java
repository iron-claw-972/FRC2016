
package org.usfirst.frc.team972.robot;

import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// joysticks
	public static Joystick joystickLeft = new Joystick(RobotMap.JOYSTICK_LEFT_USB_PORT);
	public static Joystick joystickRight = new Joystick(RobotMap.JOYSTICK_RIGHT_USB_PORT);
	public static Joystick joystickOp = new Joystick(RobotMap.JOYSTICK_OP_USB_PORT);

	// motors
	public static CANTalon frontLeftMotor = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
//	public static CANTalon frontLeftMotor = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
	// NOTE: this is called a PID Reverse CAN Talon because PIDControllers cannot reverse output on their own.
	// Therefore, we subclassed the CANTalon object and reversed all PID Outputs
	// Normal outputs are NOT reversed on a PIDReverseCANTalon
	// See source code for this subclass at bottom of Robot.java
	public static CANTalon frontRightMotor = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
//	public static PIDReverseCANTalon frontRightMotor = new PIDReverseCANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
	public static CANTalon backLeftMotor = new CANTalon(RobotMap.BACK_LEFT_MOTOR_CAN_ID);
	public static CANTalon backRightMotor = new CANTalon(RobotMap.BACK_RIGHT_MOTOR_CAN_ID);

	public static CANTalon shooterBottomMotor = new CANTalon(RobotMap.SHOOTER_BOTTOM_MOTOR_CAN_ID);
	public static CANTalon shooterTopMotor = new CANTalon(RobotMap.SHOOTER_TOP_MOTOR_CAN_ID);
	public static CANTalon intakeMotor = new CANTalon(RobotMap.INTAKE_MOTOR_CAN_ID);
	public static CANTalon obstacleMotor = new CANTalon(RobotMap.OBSTACLE_MOTOR_CAN_ID);

	public static RobotDrive botDrive = new RobotDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

	// encoders and PID
	public static Encoder rightDriveEncoder = new Encoder(RobotMap.RIGHT_DRIVE_ENCODER_DIO_A_PORT, RobotMap.RIGHT_DRIVE_ENCODER_DIO_B_PORT);
	public static Encoder leftDriveEncoder = new Encoder(RobotMap.LEFT_DRIVE_ENCODER_DIO_A_PORT, RobotMap.LEFT_DRIVE_ENCODER_DIO_B_PORT);

	// public static Encoder shooterBottomEncoder = new Encoder(RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_A_PORT, RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_B_PORT);
	// public static Encoder shooterTopEncoder = new Encoder(RobotMap.SHOOTER_TOP_ENCODER_DIO_A_PORT, RobotMap.SHOOTER_TOP_ENCODER_DIO_B_PORT);

	public static PIDController pidRightDrive = new PIDController(0, 0, 0, rightDriveEncoder, frontRightMotor);
	public static PIDController pidLeftDrive = new PIDController(0, 0, 0, leftDriveEncoder, frontLeftMotor);
	// public static PIDController pidBottomShooter = new PIDController(0, 0, 0, shooterBottomEncoder, frontLeftMotor);
	// public static PIDController pidTopShooter = new PIDController(0, 0, 0, shooterTopEncoder, frontRightMotor);

	// pneumatics
	public static Compressor compressor = new Compressor(RobotMap.PCM_CAN_ID);
//	public static DoubleSolenoid gearboxPistonLeft = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.PISTON_GEARBOX_LEFT_SHIFTING_FORWARD_CHANNEL, RobotMap.PISTON_GEARBOX_LEFT_SHIFTING_REVERSE_CHANNEL);
//	public static DoubleSolenoid gearboxPistonRight = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.PISTON_GEARBOX_RIGHT_SHIFTING_FORWARD_CHANNEL, RobotMap.PISTON_GEARBOX_RIGHT_SHIFTING_REVERSE_CHANNEL);
	public static DoubleSolenoid spoonPiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.PISTON_BALL_PUSHER_FORWARD_CHANNEL, RobotMap.PISTON_BALL_PUSHER_REVERSE_CHANNEL);

	// sensors
	DigitalInput ballOpticalSensor = new DigitalInput(RobotMap.BALL_OPTICAL_SENSOR_PORT);
	public static DigitalInput flippyThingUpperLimitSwitch = new DigitalInput(RobotMap.FLIPPY_THING_UPPER_LIMIT_SWITCH);
	public static DigitalInput flippyThingLowerLimitSwitch = new DigitalInput(RobotMap.FLIPPY_THING_LOWER_LIMIT_SWITCH);

	// intake

	static Intake intakeSystem = new Intake(intakeMotor);
	static Shooter shooterSystem = new Shooter(shooterTopMotor, shooterBottomMotor);
	static Drive driveController = new Drive(botDrive, frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);

	// speeds and multipliers
	double driveMultiplier = RobotMap.DEFAULT_DRIVE_MODE;
	double leftDriveSpeed = 0.0;
	double rightDriveSpeed = 0.0;

	// camera stuff
	boolean cameraSwitchPressedLastTime = false;
	Image img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	boolean rearCam = false; // stores whether the front camera is on

	public static USBCamera cameraFront;
	public static USBCamera cameraBack;

	public static CameraServer camServer = CameraServer.getInstance();

	// autonomous stuff
	SendableChooser autonomousDefenseChooser = new SendableChooser();
	SendableChooser autonomousDelayChooser = new SendableChooser();
	SendableChooser autonomousPositionChooser = new SendableChooser();
	SendableChooser autonomousShooterChooser = new SendableChooser();

	// gearbox switching variables
	boolean gearboxSwitchingPressedLastTime = false;

	// PID variables
	boolean pidMode = false;

	// shooter piston variables
	boolean shooterPistonPressedLastTime = false;
	boolean shooterPistonForward = false;
	long pistonTimer = -1;

	// intake button variables
	boolean intakeButtonPressed = false;
	boolean intakeReverseButtonPressed = false;

	// shooter variables
	double shooterSpeed = 0;
	boolean shooterHighSpeedMotorButtonPressed = false;
	boolean shooterMediumSpeedMotorButtonPressed = false;
	boolean shooterSlowSpeedMotorButtonPressed = false;
	boolean shooterStopMotorButtonPressed = false;

	// set distance variables
	boolean leftDistance = false;
	boolean goingSetDistance = false;

	// autonomous variables
	int autonomousDelay; // How long we delay
	long autonomousDelayStartTime;
	long chevalDeFriseStartTime = -1; // This means the timer has not been set

	double kP, kI, kD;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		System.out.println("Robot Init");
		compressor.start();
//		compressor.stop();

		botDrive.setSafetyEnabled(false); // Prevents "output not updated enough" message -- Need to set to true in teleop

		/*
		 * shooterBottomEncoder.setPIDSourceType(PIDSourceType.kRate);
		 * shooterTopEncoder.setPIDSourceType(PIDSourceType.kRate);
		 */
		// backLeftMotor.changeControlMode(TalonControlMode.Follower);
		// backRightMotor.changeControlMode(TalonControlMode.Follower); // only for testing shooter PID on practice bot
//		
		botDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

		autonomousDefenseChooser.addObject("Low Bar", new Integer(RobotMap.LOW_BAR_MODE));
		autonomousDefenseChooser.addObject("Portcullis", new Integer(RobotMap.PORTCULLIS_MODE));
		autonomousDefenseChooser.addObject("Cheval de Frise", new Integer(RobotMap.CHEVAL_DE_FRISE_MODE));
		autonomousDefenseChooser.addObject("Moat", new Integer(RobotMap.MOAT_MODE));
		autonomousDefenseChooser.addObject("Ramparts", new Integer(RobotMap.RAMPARTS_MODE));
		autonomousDefenseChooser.addObject("Drawbridge", new Integer(RobotMap.DRAWBRIDGE_MODE));
		autonomousDefenseChooser.addObject("Sally Port", new Integer(RobotMap.SALLY_PORT_MODE));
		autonomousDefenseChooser.addObject("Rock Wall", new Integer(RobotMap.ROCK_WALL_MODE));
		autonomousDefenseChooser.addObject("Rough Terrain", new Integer(RobotMap.ROUGH_TERRAIN_MODE));
		autonomousDefenseChooser.addDefault("Do Nothing", new Integer(RobotMap.DO_NOTHING_MODE));
		SmartDashboard.putData("Autonomous Defense Chooser", autonomousDefenseChooser);

		autonomousPositionChooser.addObject("Spy Position", new Integer(RobotMap.POSITION_SPY));
		autonomousPositionChooser.addDefault("Position 1", new Integer(RobotMap.POSITION_1));
		autonomousPositionChooser.addObject("Position 2", new Integer(RobotMap.POSITION_2));
		autonomousPositionChooser.addObject("Position 3", new Integer(RobotMap.POSITION_3));
		autonomousPositionChooser.addObject("Position 4", new Integer(RobotMap.POSITION_4));
		autonomousPositionChooser.addObject("Position 5", new Integer(RobotMap.POSITION_5));
		SmartDashboard.putData("Autonomous Position Chooser", autonomousPositionChooser);

		autonomousDelayChooser.addDefault("No Delay", new Integer(RobotMap.NO_DELAY));
		autonomousDelayChooser.addObject("2 Sec Delay", new Integer(RobotMap.TWO_SECOND_DELAY));
		autonomousDelayChooser.addObject("4 Sec Delay", new Integer(RobotMap.FOUR_SECOND_DELAY));
		autonomousDelayChooser.addObject("6 Sec Delay", new Integer(RobotMap.SIX_SECOND_DELAY));
		SmartDashboard.putData("Autonomous Delay Chooser", autonomousDelayChooser);

		autonomousShooterChooser.addDefault("Do Not Shoot", new Integer(RobotMap.DO_NOT_SHOOT));
		autonomousShooterChooser.addObject("Left High Goal", new Integer(RobotMap.SHOOTER_LEFT_HIGH_GOAL));
		autonomousShooterChooser.addObject("Center High Goal", new Integer(RobotMap.SHOOTER_CENTER_HIGH_GOAL));
		autonomousShooterChooser.addObject("Right High Goal", new Integer(RobotMap.SHOOTER_RIGHT_HIGH_GOAL));
		autonomousShooterChooser.addObject("Assist", new Integer(RobotMap.ASSIST_SHOOT));
		SmartDashboard.putData("Autonomous Shooting Chooser", autonomousShooterChooser);

		pidLeftDrive.setSetpoint(0);
		pidRightDrive.setSetpoint(0);
		rightDriveEncoder.reset();
		leftDriveEncoder.reset();

		try {
			cameraFront = new USBCamera("cam0");
			cameraBack = new USBCamera("cam1");
			cameraFront.openCamera();
			cameraBack.openCamera();
			cameraFront.startCapture(); // startCapture so that it doesn't try to take a picture before the camera is on
			camServer.setQuality(100);
		} catch (VisionException e) {
			System.out.println("VISION EXCEPTION ~ " + e);
		}
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	public void autonomousInit() {
//		System.out.println("autonomous init");
		botDrive.setSafetyEnabled(false); // Prevents "output not updated enough" error message

		RobotMap.autonomousDefenseMode = ((Integer) (autonomousDefenseChooser.getSelected())).intValue();
		RobotMap.autonomousDelayMode = ((Integer) (autonomousDelayChooser.getSelected())).intValue();
		RobotMap.autonomousPositionMode = ((Integer) (autonomousPositionChooser.getSelected())).intValue();
		RobotMap.autonomousShooterMode = ((Integer) (autonomousShooterChooser.getSelected())).intValue();
		// These lines store the value of the Autonomous Chooser as an int


		switch (RobotMap.autonomousDelayMode) {
			case RobotMap.NO_DELAY:
				SmartDashboard.putString("Autonomous Delay Mode", "No Delay");
				autonomousDelay = 0;
				break;
			case RobotMap.TWO_SECOND_DELAY:
				SmartDashboard.putString("Autonomous Delay Mode", "Two Second Delay");
				autonomousDelay = 2;
				break;
			case RobotMap.FOUR_SECOND_DELAY:
				SmartDashboard.putString("Autonomous Delay Mode", "Four Second Delay");
				autonomousDelay = 4;
				break;
			case RobotMap.SIX_SECOND_DELAY:
				SmartDashboard.putString("Autonomous Delay Mode", "Six Second Delay");
				autonomousDelay = 6;
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Delay Mode", "Default error!!!");
				System.out.println("Default Autonomous Delay Mode Error!!!");
				break;
		}

		switch (RobotMap.autonomousPositionMode) {
			case RobotMap.POSITION_SPY:
				SmartDashboard.putString("Autonomous Position Mode", "Spy Position");
				break;
			case RobotMap.POSITION_1:
				SmartDashboard.putString("Autonomous Position Mode", "Position 1");
				break;
			case RobotMap.POSITION_2:
				SmartDashboard.putString("Autonomous Position Mode", "Position 2");
				break;
			case RobotMap.POSITION_3:
				SmartDashboard.putString("Autonomous Position Mode", "Position 3");
				break;
			case RobotMap.POSITION_4:
				SmartDashboard.putString("Autonomous Position Mode", "Position 4");
				break;
			case RobotMap.POSITION_5:
				SmartDashboard.putString("Autonomous Position Mode", "Position 5");
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Position Mode", "Default error!!!");
				System.out.println("Default Autonomous Position Mode Error!!!");
				break;
		}

		switch (RobotMap.autonomousShooterMode) {
			case RobotMap.DO_NOT_SHOOT:
				SmartDashboard.putString("Autonomous Shooter Mode", "Do Not Shoot");
				break;
			case RobotMap.SHOOTER_LEFT_HIGH_GOAL:
				SmartDashboard.putString("Autonomous Shooter Mode", "Left High Goal");
				break;
			case RobotMap.SHOOTER_CENTER_HIGH_GOAL:
				SmartDashboard.putString("Autonomous Shooter Mode", "Center High Goal");
				break;
			case RobotMap.SHOOTER_RIGHT_HIGH_GOAL:
				SmartDashboard.putString("Autonomous Shooter Mode", "Right High Goal");
				break;
			case RobotMap.ASSIST_SHOOT:
				SmartDashboard.putString("Autonomous Shooter Mode", "Assist For Shooting");
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Shooter Mode", "Default error!!!");
				System.out.println("Default Autonomous Shooter Mode Error!!!");
				break;
		}

		switch (RobotMap.autonomousDefenseMode) {
			case RobotMap.LOW_BAR_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Low Bar");
				break;
			case RobotMap.PORTCULLIS_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Portcullis");
				break;
			case RobotMap.CHEVAL_DE_FRISE_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Cheval de Frise");
				break;
			case RobotMap.MOAT_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Moat");
				break;
			case RobotMap.RAMPARTS_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Ramparts");
				break;
			case RobotMap.DRAWBRIDGE_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Drawbridge");
				break;
			case RobotMap.SALLY_PORT_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Sally Port");
				break;
			case RobotMap.ROCK_WALL_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Rock Wall");
				break;
			case RobotMap.ROUGH_TERRAIN_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Rough Terrain");
				break;
			case RobotMap.DO_NOTHING_MODE:
				SmartDashboard.putString("Autonomous Defense Mode", "Do Nothing");
				return;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Defense Mode", "Default error!!!");
				System.out.println("Default Autonomous Defense Mode Error!!!");
				break;
		} // switch brace

		autonomousDelayStartTime = System.currentTimeMillis();
		boolean finishedDelaying = false;
		while (isAutonomous() && isEnabled() && !finishedDelaying) {
			finishedDelaying = Autonomous.autonomousDelay(autonomousDelayStartTime, autonomousDelay * 1000); // Converting from seconds to milliseconds and
		}

		botDrive.setSafetyEnabled(true); // Prevents "output not updated enough" error message
		System.out.println("finished autonomous delay");

		switch (RobotMap.autonomousDefenseMode) {
			case RobotMap.LOW_BAR_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.LOW_BAR_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.PORTCULLIS_MODE:
				Autonomous.autonomousFlippyThing(true);
				break;
			case RobotMap.CHEVAL_DE_FRISE_MODE:
				Autonomous.autonomousFlippyThing(false);
				break;
			case RobotMap.MOAT_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.MOAT_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.RAMPARTS_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.RAMPARTS_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.DRAWBRIDGE_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.DRAWBRIDGE_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.SALLY_PORT_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.SALLY_PORT_DEFENSE_DRIVE_DISTANCE);
				break;
			case RobotMap.ROCK_WALL_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.ROCK_WALL_DEFENSE_DRIVE_DISTANCE); // May need to be tuned
				break;
			case RobotMap.ROUGH_TERRAIN_MODE:
				Autonomous.autonomousDriveOverDefense(RobotMap.ROUGH_TERRAIN_DEFENSE_DRIVE_DISTANCE); // May need to be tuned
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Defense Mode", "Default error!!!");
				System.out.println("Default Autonomous Defense Mode Error -- Actually driving!!!");
				break;
		} // switch brace

		botDrive.setSafetyEnabled(false); // Prevents "output not updated enough" error message
	} // autonomous brace

	/**
	 * This function is called periodically during autonomous
	 */

	public void autonomousPeriodic() {

	}

	public void teleopInit() {
		stopEverything(); // stops all motors
	}

	/**
	 * This function is called periodically during operator control
	 */

	public void teleopPeriodic() {	
		botDrive.setSafetyEnabled(true); // Prevents "output not updated enough" error message

		double flippySpeed = (((-joystickOp.getThrottle())+1)/2);
		// obstacle motor "flippy thing"
		if ((joystickOp.getPOV(0) == 0 || joystickOp.getPOV(0) == 45 || joystickOp.getPOV(0) == 315)/* && !flippyThingUpperLimitSwitch.get()*/) {
			obstacleMotor.set(-flippySpeed); // Go up
		} else if ((joystickOp.getPOV(0) == 180 || joystickOp.getPOV(0) == 225 || joystickOp.getPOV(0) == 135)/* && !flippyThingLowerLimitSwitch.get()*/) {
			obstacleMotor.set(flippySpeed); // Go down
		} else {
			obstacleMotor.set(0);
		}
		// MAKE SURE YOU HAVE FLIPPY SPEED AT NOT ZERO (not down)
		
		SmartDashboard.putNumber("Flippy Speed", flippySpeed);
		
		SmartDashboard.putBoolean("Upper LS", flippyThingUpperLimitSwitch.get());
		SmartDashboard.putBoolean("Lower LS", flippyThingLowerLimitSwitch.get());
		
		
		// gearbox switch
		// TODO uncomment
//		boolean gearboxSwitchingButtonIsPressed = joystickRight.getRawButton(RobotMap.JOYSTICK_GEARSHIFT_BUTTON);
//		if (gearboxSwitchingButtonIsPressed && !gearboxSwitchingPressedLastTime) {
//			driveController.switchModes(gearboxPistonLeft/*, gearboxPistonRight*/);
//		}
//		gearboxSwitchingPressedLastTime = gearboxSwitchingButtonIsPressed;

		try {
			// reverse drive mode (with camera switching)
			boolean cameraToggleButtonPressed = joystickLeft.getRawButton(RobotMap.JOYSTICK_CAMERA_TOGGLE_BUTTON);
			if (cameraToggleButtonPressed && !cameraSwitchPressedLastTime) {
				if (rearCam) {
					cameraBack.stopCapture();
					cameraFront.startCapture();
					rearCam = false;
				} else {
					cameraFront.stopCapture();
					cameraBack.startCapture();
					rearCam = true;
				}
			}

			cameraSwitchPressedLastTime = cameraToggleButtonPressed;
			// finish switching
			// camera streaming
			if (rearCam == true) {
				cameraBack.getImage(img);
				SmartDashboard.putString("Front", "BATTERY");
			} else {
				cameraFront.getImage(img);
				SmartDashboard.putString("Front", "INTAKE");
			}
			camServer.setImage(img); // puts image on the dashboard
			// finish camera streaming
		} catch (NullPointerException e) {

			// In this catch, we are switching drive modes, like in the try, but
			// without cameras, since we have no cameras
			boolean cameraToggleButtonPressed = joystickLeft.getRawButton(RobotMap.JOYSTICK_CAMERA_TOGGLE_BUTTON);
			if (cameraToggleButtonPressed && !cameraSwitchPressedLastTime) {
				if (rearCam) {
					rearCam = false;
				} else {
					rearCam = true;
				}
			}
			cameraSwitchPressedLastTime = cameraToggleButtonPressed;
			// finish switching

			// camera streaming
			if (rearCam == true) {
				SmartDashboard.putString("Front Side", "BATTERY");
			} else {
				SmartDashboard.putString("Front Side", "INTAKE");
			}
		} catch (VisionException e) {
			System.out.println(e);

			// In this catch, we are switching drive modes, like in the try, but
			// without cameras, since we have no cameras
			boolean cameraToggleButtonPressed = joystickLeft.getRawButton(RobotMap.JOYSTICK_CAMERA_TOGGLE_BUTTON);
			if (cameraToggleButtonPressed && !cameraSwitchPressedLastTime) {
				if (rearCam) {
					rearCam = false;
				} else {
					rearCam = true;
				}
			}
			cameraSwitchPressedLastTime = cameraToggleButtonPressed;
			// finish switching

			// camera streaming
			if (rearCam == true) {
				SmartDashboard.putString("Front Side", "BATTERY");
			} else {
				SmartDashboard.putString("Front Side", "INTAKE");
			}
		} // end catch

		// drive multiplier
		if (joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_1_BUTTON)) {
			driveMultiplier = RobotMap.DRIVE_MODE_1;
		} else if (joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_2_BUTTON)) {
			driveMultiplier = RobotMap.DRIVE_MODE_2;
		} else if (joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_3_BUTTON)) {
			driveMultiplier = RobotMap.DRIVE_MODE_3;
		} else if (joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_4_BUTTON)) {
			driveMultiplier = RobotMap.DRIVE_MODE_4;
		} else if (joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_5_BUTTON)) {
			driveMultiplier = RobotMap.DRIVE_MODE_5;
		}
		// finish drive multiplier

		double leftJoystickSpeed = joystickLeft.getY();
		double rightJoystickSpeed = joystickRight.getY();
		
		SmartDashboard.putNumber("Left Joystick Y", leftJoystickSpeed);
		SmartDashboard.putNumber("Right Joystick Y", rightJoystickSpeed);
		
		// drive code
		if (rearCam) {
			leftDriveSpeed = leftJoystickSpeed * driveMultiplier * -1;
			rightDriveSpeed = rightJoystickSpeed * driveMultiplier * -1;
			// If using the rear cam, we always want the drive multiplier to be
			// negative
		} else {
			leftDriveSpeed = joystickRight.getY() * driveMultiplier;
			rightDriveSpeed = joystickLeft.getY() * driveMultiplier;
		}

		SmartDashboard.putNumber("Drive Multiplier", (driveMultiplier));
		SmartDashboard.putNumber("Left Speed", leftDriveSpeed);
		SmartDashboard.putNumber("Right Speed", rightDriveSpeed);
		// finish drive code

		// PID Brake
//		double kP = (((joystickLeft.getZ() * -1) + 1) / 2.0) * 0.01;
//		double kI = (((joystickRight.getZ() * -1) + 1) / 2.0) * 0.01;
//		double kD = (((joystickOp.getThrottle() * -1) + 1) / 2.0) * 0.01;

		kP = RobotMap.P_BRAKE;
		kI = RobotMap.I_BRAKE;
		kD = RobotMap.D_BRAKE;

//		double error = rightDriveEncoder.get() - 0;
//


		// TODO fix set forward code and use PID for it
		if (joystickRight.getRawButton(RobotMap.JOYSTICK_BRAKE_MODE_BUTTON)) {
			botDrive.setSafetyEnabled(false);
			driveController.pidBrake(pidMode, pidLeftDrive, pidRightDrive, leftDriveEncoder, rightDriveEncoder, kP, kI, kD);
		} else {
			botDrive.setSafetyEnabled(true);
			driveController.stopPIDBrake(pidMode, pidLeftDrive, pidRightDrive);
			if (joystickRight.getRawButton(RobotMap.JOYSTICK_SPLIT_ARCADE_DRIVE_BUTTON)) {
				botDrive.drive(joystickRight.getY() * driveMultiplier, joystickLeft.getX() * driveMultiplier); // Split arcade
			} else {
				driveController.tankDrive(leftDriveSpeed, rightDriveSpeed);
			}
		}
//			boolean encoderValueButtonPressed = joystickRight.getRawButton(RobotMap.JOYSTICK_DRIVE_SET_DISTANCE_BUTTON);
//			if (encoderValueButtonPressed && !goingSetDistance) {
//				goingSetDistance = true;
//				leftDriveEncoder.reset();
//				rightDriveEncoder.reset();
//			}
//			if (goingSetDistance && joystickRight.getRawButton(7)) {
//				count++;
//				SmartDashboard.putNumber("Count", count);
//				// if (Math.abs(leftDriveEncoder.get()) > 10) {
//				// frontLeftMotor.set(0);
//				// backLeftMotor.set(0);
//				// } else {
//				// frontLeftMotor.set(0.1);
//				// backLeftMotor.set(0.1);
//				// }
//				if (Math.abs(rightDriveEncoder.get()) > 100) {
//					frontRightMotor.set(0);
//					backRightMotor.set(0);
//				} else {
//					frontRightMotor.set(0.15);
//					backRightMotor.set(0.15);
//				}
//				if (/* Math.abs(leftDriveEncoder.get()) > 100 && */ Math.abs(rightDriveEncoder.get()) > 100) {
//					goingSetDistance = false;
//				}
//			} else {
//				count = 0;
//			botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed); // in else above
//			}
		// finish PID Brake

		intakeSystem.intakeStateMachine(spoonPiston, ballOpticalSensor);
		shooterSystem.shooterStateMachine(spoonPiston);
		
//		SmartDashboard.putNumber("Bottom Shooter Encoder Rate", shooterBottomEncoder.getRate());
//		SmartDashboard.putNumber("Top Shooter Encoder Rate", shooterTopEncoder.getRate());

		// shooter motors
//		shooterHighSpeedMotorButtonPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_START_HIGH_SPEED_SHOOTER_BUTTON);
//		shooterMediumSpeedMotorButtonPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_START_MEDIUM_SPEED_SHOOTER_BUTTON);
//		shooterSlowSpeedMotorButtonPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_START_LOW_SPEED_SHOOTER_BUTTON);
//		shooterStopMotorButtonPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_STOP_SHOOTER_BUTTON);
//		if (shooterHighSpeedMotorButtonPressed) { // 2800 Hz
////			shooterSpeed = RobotMap.SHOOTER_FAST_SPEED;
//			pidBottomShooter.setSetpoint(RobotMap.HIGH_SPEED_SHOOTER_MOTOR_SETPOINT);
//			pidTopShooter.setSetpoint(RobotMap.HIGH_SPEED_SHOOTER_MOTOR_SETPOINT);
//			pidBottomShooter.setPID(RobotMap.P_HIGH_SPEED_SHOOTER, RobotMap.I_HIGH_SPEED_SHOOTER, RobotMap.D_HIGH_SPEED_SHOOTER);
//			pidTopShooter.setPID(RobotMap.P_HIGH_SPEED_SHOOTER, RobotMap.I_HIGH_SPEED_SHOOTER, RobotMap.D_HIGH_SPEED_SHOOTER);
//			pidBottomShooter.enable();
//			pidTopShooter.enable();
//
//		} else if (shooterMediumSpeedMotorButtonPressed) { // 1600 Hz
////			shooterSpeed = RobotMap.SHOOTER_MEDIUM_SPEED;
//			pidBottomShooter.setSetpoint(RobotMap.MEDIUM_SPEED_SHOOTER_MOTOR_SETPOINT);
//			pidTopShooter.setSetpoint(RobotMap.MEDIUM_SPEED_SHOOTER_MOTOR_SETPOINT);
//			pidBottomShooter.setPID(RobotMap.P_MEDIUM_SPEED_SHOOTER, RobotMap.I_MEDIUM_SPEED_SHOOTER, RobotMap.D_MEDIUM_SPEED_SHOOTER);
//			pidTopShooter.setPID(RobotMap.P_MEDIUM_SPEED_SHOOTER, RobotMap.I_MEDIUM_SPEED_SHOOTER, RobotMap.D_MEDIUM_SPEED_SHOOTER);
//			pidBottomShooter.enable();
//			pidTopShooter.enable();
//		} else if (shooterSlowSpeedMotorButtonPressed) { // 700 Hz
////			shooterSpeed = RobotMap.SHOOTER_SLOW_SPEED;
//			pidBottomShooter.setSetpoint(RobotMap.LOW_SPEED_SHOOTER_MOTOR_SETPOINT);
//			pidTopShooter.setSetpoint(RobotMap.LOW_SPEED_SHOOTER_MOTOR_SETPOINT);
//			pidBottomShooter.setPID(RobotMap.P_LOW_SPEED_SHOOTER, RobotMap.I_LOW_SPEED_SHOOTER, RobotMap.D_LOW_SPEED_SHOOTER);
//			pidTopShooter.setPID(RobotMap.P_LOW_SPEED_SHOOTER, RobotMap.I_LOW_SPEED_SHOOTER, RobotMap.D_LOW_SPEED_SHOOTER);
//			pidBottomShooter.enable();
//			pidTopShooter.enable();
//
//		} else if (shooterStopMotorButtonPressed) {
////			shooterSpeed = 0;
//			pidTopShooter.disable();
//			pidBottomShooter.disable();
////			backLeftMotor.set(0);
////			backRightMotor.set(0);
//			frontLeftMotor.set(0);
//			frontRightMotor.set(0);
//		}
//		shooterBottomMotor.set(shooterSpeed);
//		shooterTopMotor.set(shooterSpeed);
//		frontLeftMotor.set(shooterSpeed);
//		frontRightMotor.set(shooterSpeed);
//		backLeftMotor.set(shooterSpeed);
//		backRightMotor.set(shooterSpeed);

//		backLeftMotor.set(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
//		backRightMotor.set(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
		// TODO using drive motors to test shooter PID; change this soon
		printEverything();
	}

	public void printEverything() {
		SmartDashboard.putNumber("P", kP);
		SmartDashboard.putNumber("I", kI);
		SmartDashboard.putNumber("D", kD);
//		SmartDashboard.putNumber("Shooter Bottom Motor", shooterSpeed);
//		SmartDashboard.putNumber("Shooter Top Motor", shooterSpeed);
		SmartDashboard.putNumber("Back Left Motor Speed", backLeftMotor.get());
		SmartDashboard.putNumber("Back Right Motor Speed", backRightMotor.get());
		SmartDashboard.putNumber("Front Left Motor Speed", frontLeftMotor.get());
		SmartDashboard.putNumber("Front Right Motor Speed", frontRightMotor.get());
		// Prints motor speed to the smart dashboard
		SmartDashboard.putNumber("Left Encoder Value", leftDriveEncoder.get());
		SmartDashboard.putNumber("Right Encoder Value", rightDriveEncoder.get());
		SmartDashboard.putBoolean("Going Set Distance", goingSetDistance);
		
//		SmartDashboard.putBoolean("Ball Present", ballOpticalSensor.get());
	}
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

	public void disabled() {
		botDrive.setSafetyEnabled(false); // Prevents "output not updated enough" error message
		stopEverything();
	}

	public void stopEverything() {
		pidLeftDrive.disable();
		pidRightDrive.disable();
		backRightMotor.changeControlMode(TalonControlMode.PercentVbus);
		backLeftMotor.changeControlMode(TalonControlMode.PercentVbus);
		frontRightMotor.set(0);
		backLeftMotor.set(0);
		backRightMotor.set(0);
		frontLeftMotor.set(0);
		botDrive.stopMotor();
		intakeMotor.set(0);
		shooterBottomMotor.set(0);
		shooterTopMotor.set(0);
		obstacleMotor.set(0);

		shooterSpeed = 0;

		pidMode = false;
//		pidTopShooter.disable();
//		pidBottomShooter.disable();
	}
}

/**
 * PIDReverseCANTalon is a subclass of CANTalon that reverses outputs to PID.
 * This is used for a PIDController, which is unable to reverse its outputs, to the author's knowledge.
 * 
 * @author Iron Claw Programming Team
 *
 */
class PIDReverseCANTalon extends CANTalon {

	public PIDReverseCANTalon(int deviceNumber) {
		super(deviceNumber);
	}

	public void pidWrite(double output) {
		super.pidWrite(output * -1);
	}

}