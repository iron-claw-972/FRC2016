
package org.usfirst.frc.team972.robot;

import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.Timer;
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

	Joystick joystickLeft = new Joystick(RobotMap.JOYSTICK_LEFT_USB_PORT);
	Joystick joystickRight = new Joystick(RobotMap.JOYSTICK_RIGHT_USB_PORT);
	Joystick joystickOp = new Joystick(RobotMap.JOYSTICK_OP_USB_PORT);

	CANTalon frontLeftMotor = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
	CANTalon frontRightMotor = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
	CANTalon backLeftMotor = new CANTalon(RobotMap.BACK_LEFT_MOTOR_CAN_ID);
	CANTalon backRightMotor = new CANTalon(RobotMap.BACK_RIGHT_MOTOR_CAN_ID);

	CANTalon shooterBottomMotor = new CANTalon(RobotMap.SHOOTER_BOTTOM_MOTOR_CAN_ID);
	CANTalon shooterTopMotor = new CANTalon(RobotMap.SHOOTER_TOP_MOTOR_CAN_ID);
	CANTalon intakeMotor = new CANTalon(RobotMap.INTAKE_MOTOR_CAN_ID);
	CANTalon obstacleMotor = new CANTalon(RobotMap.OBSTACLE_MOTOR_CAN_ID);

	Compressor compressor = new Compressor(RobotMap.PCM_CAN_ID);

	Encoder rightDriveEncoder = new Encoder(RobotMap.LEFT_DRIVE_ENCODER_DIO_A_PORT,
			RobotMap.LEFT_DRIVE_ENCODER_DIO_B_PORT);
//	 Encoder leftDriveEncoder = new
//	 Encoder(RobotMap.RIGHT_DRIVE_ENCODER_DIO_A_PORT,
//	 RobotMap.RIGHT_DRIVE_ENCODER_DIO_B_PORT);
	// Encoder(RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_A_PORT,
	// RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_B_PORT);
	// Encoder shooterTopEncoder = new
	// Encoder(RobotMap.SHOOTER_TOP_ENCODER_DIO_A_PORT,
	// RobotMap.SHOOTER_TOP_ENCODER_DIO_B_PORT);

	PIDController pid = new PIDController(0, 0, 0, rightDriveEncoder, frontLeftMotor);

	RobotDrive botDrive = new RobotDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

	double driveMultiplier = RobotMap.DEFAULT_DRIVE_MODE;
	double leftDriveSpeed = 0.0;
	// this should always be 0 because if not there's something wrong.
	double rightDriveSpeed = 0.0;

	boolean cameraSwitchPressedLastTime = false;
	Image img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

	boolean rearCam = false;
	// stores whether the front camera is on

	USBCamera cameraFront;
	USBCamera cameraBack;

	CameraServer camServer = CameraServer.getInstance();

	SendableChooser autonomousChooser = new SendableChooser();
	SendableChooser delayAutonomousChooser = new SendableChooser();

	// DoubleSolenoid gearboxPistonLeft = new
	// DoubleSolenoid(RobotMap.PCM_CAN_ID,
	// RobotMap.PISTON_GEARBOX_LEFT_SHIFTING_FORWARD_CHANNEL,
	// RobotMap.PISTON_GEARBOX_LEFT_SHIFTING_REVERSE_CHANNEL);
	// DoubleSolenoid gearboxPistonRight = new
	// DoubleSolenoid(RobotMap.PCM_CAN_ID,
	// RobotMap.PISTON_GEARBOX_RIGHT_SHIFTING_FORWARD_CHANNEL,
	// RobotMap.PISTON_GEARBOX_RIGHT_SHIFTING_REVERSE_CHANNEL);
	// boolean gearboxSwitchingPressedLastTime = false;
	// boolean gearboxPistonForward = false;
	boolean pidMode = false;
	boolean leftDistance = false;

	DoubleSolenoid shooterPiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.PISTON_BALL_PUSHER_FORWARD_CHANNEL,
			RobotMap.PISTON_BALL_PUSHER_REVERSE_CHANNEL);
	boolean shooterPistonPressedLastTime = false;
	boolean shooterPistonForward = false;
	long pistonTimer = -1;

	boolean intakeButtonPressed = false;
	boolean intakeReverseButtonPressed = false;

	double shooterSpeed = 0;
	boolean shooterHighSpeedMotorButtonPressed = false;
	boolean shooterMediumSpeedMotorButtonPressed = false;
	boolean shooterSlowSpeedMotorButtonPressed = false;
	boolean shooterStopMotorButtonPressed = false;

	boolean goingSetDistance = false;

	int start = 0;

	int count = 0; // TODO remove

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		compressor.start();

		botDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		botDrive.setSafetyEnabled(false); // Prevents "output not updated enough" message -- Need to set to true in teleop

		autonomousChooser.addObject("Low Bar", new Integer(RobotMap.LOW_BAR_MODE));
		autonomousChooser.addObject("Defend", new Integer(RobotMap.DEFEND_MODE));
		autonomousChooser.addObject("Drive Over Defense", new Integer(RobotMap.DRIVE_OVER_DEFENSE_MODE));
		autonomousChooser.addObject("Cheval de Frise", new Integer(RobotMap.CHEVAL_DE_FRISE_MODE));
		autonomousChooser.addObject("Portcullis", new Integer(RobotMap.PORTCULLIS_MODE));
		autonomousChooser.addDefault("Do Nothing", new Integer(RobotMap.DO_NOTHING_MODE));
		SmartDashboard.putData("Autonomous Chooser", autonomousChooser);

		delayAutonomousChooser.addObject("Delay", new Integer(RobotMap.YES_DELAY));
		delayAutonomousChooser.addDefault("No Delay", new Integer(RobotMap.NO_DELAY));
		SmartDashboard.putData("Delay Autonomous Chooser", delayAutonomousChooser);
		
		 pid.setSetpoint(0);
		rightDriveEncoder.reset();
		 rightDriveEncoder.reset();

		try {
			cameraFront = new USBCamera("cam0");
			cameraBack = new USBCamera("cam1");
			cameraFront.openCamera();
			cameraBack.openCamera();
			cameraFront.startCapture(); // startCapture so that it doesn't try
										// to take a picture
										// before the camera is on
			camServer.setQuality(50); // 50 is currently perfect
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
		botDrive.setSafetyEnabled(false); // Prevents "output not updated enough" error message
		
		RobotMap.autonomousMode = ((Integer) (autonomousChooser.getSelected())).intValue();
		// This line stores the value of the Autonomous Chooser as an int
		
		RobotMap.delayAutonomousMode = ((Integer) (delayAutonomousChooser.getSelected())).intValue();

		switch (RobotMap.autonomousMode) {
			case RobotMap.LOW_BAR_MODE:
				SmartDashboard.putString("Autonomous Mode", "Low Bar");
				break;
			case RobotMap.DEFEND_MODE:
				SmartDashboard.putString("Autonomous Mode", "Defend");
				break;
			case RobotMap.DRIVE_OVER_DEFENSE_MODE:
				SmartDashboard.putString("Autonomous Mode", "Drive Over Defense");
				break;
			case RobotMap.CHEVAL_DE_FRISE_MODE:
				SmartDashboard.putString("Autonomous Mode", "Cheval de Frise");
				break;
			case RobotMap.PORTCULLIS_MODE:
				SmartDashboard.putString("Autonomous Mode", "Portcullis");
				break;
			case RobotMap.DO_NOTHING_MODE:
				SmartDashboard.putString("Autonomous Mode", "Do Nothing");
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Autonomous Mode", "Default error!!!");
				System.out.println("Default Autonomous Mode Error!!!");
				break;
		} //switch brace
		
		switch (RobotMap.delayAutonomousMode) {
			case RobotMap.YES_DELAY:
				SmartDashboard.putString("Delay Autonomous Mode", "Yes");
				break;
			case RobotMap.NO_DELAY:
				SmartDashboard.putString("Delay Autonomous Mode", "No");
				break;
			default:
				// This should never happen
				SmartDashboard.putString("Delay Autonomous Mode", "Default error!!!");
				System.out.println("Default Delay Autonomous Mode Error!!!");
				break;
		}
		
	} //autonomous brace  

	/**
	 * This function is called periodically during autonomous
	 */

	public void autonomousPeriodic() {
		
	}

	public void teleopInit() {
		botDrive.setSafetyEnabled(true); // Originally set as false during autonomous to prevent the "output not updated enough" error
	}
	
	/**
	 * This function is called periodically during operator control
	 */

	public void teleopPeriodic() {

		if (joystickOp.getPOV(0) == 0 || joystickOp.getPOV(0) == 45 || joystickOp.getPOV(0) == 315) {
			obstacleMotor.set(0.8);
		} else if (joystickOp.getPOV(0) == 180 || joystickOp.getPOV(0) == 225 || joystickOp.getPOV(0) == 135) {
			obstacleMotor.set(-0.8);
		} else {
			obstacleMotor.set(0);
		}

		// gearbox switch
		// boolean gearboxSwitchingButtonIsPressed =
		// joystickRight.getRawButton(RobotMap.JOYSTICK_GEARSHIFT_BUTTON);
		// if (gearboxSwitchingButtonIsPressed &&
		// !gearboxSwitchingPressedLastTime) {
		// if (gearboxPistonForward == false) {
		// gearboxPistonLeft.set(DoubleSolenoid.Value.kForward);
		// gearboxPistonRight.set(DoubleSolenoid.Value.kForward);
		// gearboxPistonForward = true;
		// } else {
		// gearboxPistonLeft.set(DoubleSolenoid.Value.kReverse);
		// gearboxPistonRight.set(DoubleSolenoid.Value.kReverse);
		// gearboxPistonForward = false;
		// }
		// }
		// gearboxSwitchingPressedLastTime = gearboxSwitchingButtonIsPressed;

		// shooter piston
		switch (RobotMap.currentState) {
		case RobotMap.SHOOTER_PISTON_UP_STATE:
			SmartDashboard.putString("Piston State", "Forward");
			shooterPiston.set(DoubleSolenoid.Value.kForward);
			if (pistonTimer < 0) {
				pistonTimer = System.currentTimeMillis();
			}
			if (System.currentTimeMillis() - pistonTimer > 1000) {
				RobotMap.currentState = RobotMap.SHOOTER_PISTON_DOWN_STATE;
				pistonTimer = -1;
			}
			break;
		case RobotMap.SHOOTER_PISTON_DOWN_STATE:
			SmartDashboard.putString("Piston State", "Reverse");
			shooterPiston.set(DoubleSolenoid.Value.kReverse);
			RobotMap.currentState = RobotMap.DO_NOTHING_STATE;

			break;
		case RobotMap.DO_NOTHING_STATE:
			SmartDashboard.putString("Piston State", "Do Nothing");
			if (joystickOp.getRawButton(RobotMap.JOYSTICK_ACTIVATE_PISTON_BUTTON)) {
				RobotMap.currentState = RobotMap.SHOOTER_PISTON_UP_STATE;
			}
			break;
		}
		boolean shooterPistonButtonIsPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_ACTIVATE_PISTON_BUTTON);
		if (shooterPistonButtonIsPressed && !shooterPistonPressedLastTime) {
			if (shooterPistonForward) {
				shooterPiston.set(DoubleSolenoid.Value.kReverse);
				shooterPistonForward = false;
			} else {
				shooterPiston.set(DoubleSolenoid.Value.kForward);
				shooterPistonForward = true;
			}
		}
		shooterPistonPressedLastTime = shooterPistonButtonIsPressed;
		// end shooter piston

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
			SmartDashboard.putString("Front", "LED");
		} else {
			cameraFront.getImage(img);
			SmartDashboard.putString("Front", "PISTON");
		}
		camServer.setImage(img); // puts image on the dashboard
		// finish camera streaming

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

		// drive code
		if (rearCam) {
			driveMultiplier = -Math.abs(driveMultiplier);
			// If using the rear cam, we always want the drive multiplier to be
			// negative
		} else {
			driveMultiplier = Math.abs(driveMultiplier);
		}

		leftDriveSpeed = joystickLeft.getY() * driveMultiplier;
		rightDriveSpeed = joystickRight.getY() * driveMultiplier;

		SmartDashboard.putNumber("Drive Multiplier", (driveMultiplier));
		SmartDashboard.putNumber("Left Speed", leftDriveSpeed);
		SmartDashboard.putNumber("Right Speed", rightDriveSpeed);
		// SmartDashboard.putNumber("Encoder Left",
		// leftDriveEncoder.getPeriod());
		// SmartDashboard.putNumber("Encoder Right",
		// rightDriveEncoder.getDistance());
		// finish drive code

		// PID Brake
		// double kP = (((joystickLeft.getZ() * -1) + 1) / 2.0) * 0.1;
		// double kI = (((joystickRight.getZ() * -1) + 1) / 2.0) * 0.1;
		// double kD = (((joystickOp.getThrottle() * -1) + 1) / 2.0) * 0.1;

		double kP = 0.008;
		double kI = 0.001;
		double kD = 0.006;

		double error = rightDriveEncoder.get() - 0;

		SmartDashboard.putNumber("Error", error);
		SmartDashboard.putNumber("P", kP);
		SmartDashboard.putNumber("I", kI);
		SmartDashboard.putNumber("D", kD);

		frontRightMotor.reverseOutput(true);
		backRightMotor.reverseOutput(true);

		if (joystickRight.getRawButton(RobotMap.JOYSTICK_BRAKE_MODE_BUTTON)) {
			if (!pidMode) {
				pidMode = true;
				rightDriveEncoder.reset();

				// this sets all the motors except front left to be followers
				// this way they will do the same thing that the front left
				// motor does
				// the front left motor is controlled by the PID Controller
				// object
				backRightMotor.changeControlMode(TalonControlMode.Follower);
				backLeftMotor.changeControlMode(TalonControlMode.Follower);
				frontRightMotor.changeControlMode(TalonControlMode.Follower);
			}
			// has the other motors follow the PID controlled motor
			backRightMotor.set(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
			backLeftMotor.set(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
			frontRightMotor.set(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
			pid.setPID(kP, kI, kD);
			pid.enable();
		} else {
			pidMode = false;
			backRightMotor.changeControlMode(TalonControlMode.PercentVbus);
			backLeftMotor.changeControlMode(TalonControlMode.PercentVbus);
			frontRightMotor.changeControlMode(TalonControlMode.PercentVbus);
			pid.disable();

			boolean encoderValueButtonPressed = joystickRight.getRawButton(RobotMap.JOYSTICK_DRIVE_SET_DISTANCE_BUTTON);
			if (encoderValueButtonPressed && !goingSetDistance) {
				goingSetDistance = true;
				// leftDriveEncoder.reset();
				rightDriveEncoder.reset();
			}
			if (goingSetDistance && joystickRight.getRawButton(7)) {
				count++; // TODO remove
				SmartDashboard.putNumber("Count", count); // TODO Remove
				// if (Math.abs(leftDriveEncoder.get()) > 10) {
				// frontLeftMotor.set(0);
				// backLeftMotor.set(0);
				// } else {
				// frontLeftMotor.set(0.1);
				// backLeftMotor.set(0.1);
				// }
				if (Math.abs(rightDriveEncoder.get()) > 100) {
					frontRightMotor.set(0);
					backRightMotor.set(0);
				} else {
					frontRightMotor.set(0.15);
					backRightMotor.set(0.15);
				}
				if (/* Math.abs(leftDriveEncoder.get()) > 100 && */ Math.abs(rightDriveEncoder.get()) > 100) {
					goingSetDistance = false;
				}
			} else {
				count = 0; // TODO remove
				botDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
			}

		}
		// finish PID Brake

		// SmartDashboard.putNumber("Left Encoder Value",
		// Math.abs(leftDriveEncoder.get()));
		SmartDashboard.putNumber("Right Encoder Value", Math.abs(rightDriveEncoder.get()));
		SmartDashboard.putBoolean("Going Set Distance", goingSetDistance);

		// intake motor
		intakeButtonPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_START_INTAKE_BUTTON);
		intakeReverseButtonPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_REVERSE_INTAKE_BUTTON);
		if (intakeButtonPressed) {
			intakeMotor.set(RobotMap.INTAKE_MOTOR_SPEED);
		} else if (intakeReverseButtonPressed) {
			intakeMotor.set(RobotMap.INTAKE_REVERSE_MOTOR_SPEED);
		} else {
			intakeMotor.set(0);
		}
		// end intake motor

		// shooter motors
		shooterHighSpeedMotorButtonPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_START_HIGH_SPEED_SHOOTER_BUTTON);
		shooterMediumSpeedMotorButtonPressed = joystickOp
				.getRawButton(RobotMap.JOYSTICK_START_MEDIUM_SPEED_SHOOTER_BUTTON);
		shooterSlowSpeedMotorButtonPressed = joystickOp.getRawButton(RobotMap.JOYTSTICK_START_LOW_SPEED_SHOOTER_BUTTON);
		shooterStopMotorButtonPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_STOP_SHOOTER_BUTTON);
		if (shooterHighSpeedMotorButtonPressed) {
			shooterSpeed = 0.6;
		} else if (shooterMediumSpeedMotorButtonPressed) {
			shooterSpeed = RobotMap.SHOOTER_FAST_SPEED;
		} else if (shooterMediumSpeedMotorButtonPressed) {
			shooterSpeed = RobotMap.SHOOTER_MEDIUM_SPEED;
		} else if (shooterSlowSpeedMotorButtonPressed) {
			shooterSpeed = RobotMap.SHOOTER_SLOW_SPEED;
		}
		shooterBottomMotor.set(shooterSpeed);
		shooterTopMotor.set(shooterSpeed);
		// shooter motors
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		System.out.println(rightDriveEncoder.get());

	}

	public void disabled() {
		// pid.disable();
		frontRightMotor.changeControlMode(TalonControlMode.PercentVbus);
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
	}
}
