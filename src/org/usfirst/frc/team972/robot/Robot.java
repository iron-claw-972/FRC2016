
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
	// public static CANTalon frontLeftMotor = new
	// CANTalon(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
	// NOTE: this is called a PID Reverse CAN Talon because PIDControllers
	// cannot reverse output on their own.
	// Therefore, we subclassed the CANTalon object and reversed all PID Outputs
	// Normal outputs are NOT reversed on a PIDReverseCANTalon
	// See source code for this subclass at bottom of Robot.java
	public static CANTalon frontRightMotor = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
	// public static PIDReverseCANTalon frontRightMotor = new
	// PIDReverseCANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
	public static CANTalon backLeftMotor = new CANTalon(RobotMap.BACK_LEFT_MOTOR_CAN_ID);
	public static CANTalon backRightMotor = new CANTalon(RobotMap.BACK_RIGHT_MOTOR_CAN_ID);

	public static CANTalon shooterBottomMotor = new CANTalon(RobotMap.SHOOTER_BOTTOM_MOTOR_CAN_ID);
	public static CANTalon shooterTopMotor = new CANTalon(RobotMap.SHOOTER_TOP_MOTOR_CAN_ID);
	public static CANTalon intakeMotor = new CANTalon(RobotMap.INTAKE_MOTOR_CAN_ID);
	public static CANTalon obstacleMotor = new CANTalon(RobotMap.OBSTACLE_MOTOR_CAN_ID);

	public static RobotDrive botDrive = new RobotDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

	// encoders and PID
	public static Encoder rightDriveEncoder = new Encoder(RobotMap.RIGHT_DRIVE_ENCODER_DIO_A_PORT,
			RobotMap.RIGHT_DRIVE_ENCODER_DIO_B_PORT);
	public static Encoder leftDriveEncoder = new Encoder(RobotMap.LEFT_DRIVE_ENCODER_DIO_A_PORT,
			RobotMap.LEFT_DRIVE_ENCODER_DIO_B_PORT);

	// public static Encoder shooterBottomEncoder = new
	// Encoder(RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_A_PORT,
	// RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_B_PORT);
	// public static Encoder shooterTopEncoder = new
	// Encoder(RobotMap.SHOOTER_TOP_ENCODER_DIO_A_PORT,
	// RobotMap.SHOOTER_TOP_ENCODER_DIO_B_PORT);

	public static PIDController pidRightDrive = new PIDController(0, 0, 0, rightDriveEncoder, frontRightMotor);
	public static PIDController pidLeftDrive = new PIDController(0, 0, 0, leftDriveEncoder, frontLeftMotor);
	// public static PIDController pidBottomShooter = new PIDController(0, 0, 0,
	// shooterBottomEncoder, frontLeftMotor);
	// public static PIDController pidTopShooter = new PIDController(0, 0, 0,
	// shooterTopEncoder, frontRightMotor);

	// pneumatics
	public static Compressor compressor = new Compressor(RobotMap.PCM_CAN_ID);
	// public static DoubleSolenoid gearboxPistonLeft = new
	// DoubleSolenoid(RobotMap.PCM_CAN_ID,
	// RobotMap.PISTON_GEARBOX_LEFT_SHIFTING_FORWARD_CHANNEL,
	// RobotMap.PISTON_GEARBOX_LEFT_SHIFTING_REVERSE_CHANNEL);
	// public static DoubleSolenoid gearboxPistonRight = new
	// DoubleSolenoid(RobotMap.PCM_CAN_ID,
	// RobotMap.PISTON_GEARBOX_RIGHT_SHIFTING_FORWARD_CHANNEL,
	// RobotMap.PISTON_GEARBOX_RIGHT_SHIFTING_REVERSE_CHANNEL);
	public static DoubleSolenoid spoonPiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID,
			RobotMap.PISTON_BALL_PUSHER_FORWARD_CHANNEL, RobotMap.PISTON_BALL_PUSHER_REVERSE_CHANNEL);

	// sensors
	DigitalInput ballOpticalSensor = new DigitalInput(RobotMap.BALL_OPTICAL_SENSOR_PORT);
	public static DigitalInput obstacleMotorUpperLimitSwitch = new DigitalInput(
			RobotMap.FLIPPY_THING_UPPER_LIMIT_SWITCH);
	public static DigitalInput obstacleMotorLowerLimitSwitch = new DigitalInput(
			RobotMap.FLIPPY_THING_LOWER_LIMIT_SWITCH);

	// intake

	static Intake intakeSystem = new Intake(intakeMotor);
	static Shooter shooterSystem = new Shooter(shooterTopMotor, shooterBottomMotor);
	static Drive driveController = new Drive(botDrive, frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
	static AutonomousChooser autonomousChooserSystem = new AutonomousChooser();

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

	static long autonomousDelayStartTime;

	double obstacleMotorSpeed;
	boolean obstacleMotorManualOverride;

	// shooter variables
	double shooterSpeed = 0;
	boolean shooterHighSpeedMotorButtonPressed = false;
	boolean shooterMediumSpeedMotorButtonPressed = false;
	boolean shooterSlowSpeedMotorButtonPressed = false;
	boolean shooterStopMotorButtonPressed = false;

	// set distance variables
	boolean leftDistance = false;
	boolean goingSetDistance = false;

	double kP, kI, kD, leftJoystickY, rightJoystickY;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		System.out.println("Robot Init");
		compressor.start();
		// compressor.stop();

		botDrive.setSafetyEnabled(false); // Prevents "output not updated
											// enough" message -- Need to set to
											// true in teleop

		autonomousChooserSystem.createChooser();

		/*
		 * shooterBottomEncoder.setPIDSourceType(PIDSourceType.kRate);
		 * shooterTopEncoder.setPIDSourceType(PIDSourceType.kRate);
		 */
		// backLeftMotor.changeControlMode(TalonControlMode.Follower);
		// backRightMotor.changeControlMode(TalonControlMode.Follower); // only
		// for testing shooter PID on practice bot

		botDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		botDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

		pidLeftDrive.setSetpoint(0);
		pidRightDrive.setSetpoint(0);
		rightDriveEncoder.reset();
		leftDriveEncoder.reset();

		try {
			cameraFront = new USBCamera("cam0");
			cameraBack = new USBCamera("cam1");
			cameraFront.openCamera();
			cameraBack.openCamera();
			cameraFront.startCapture(); // startCapture so that it doesn't try
										// to take a picture before the camera
										// is on
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
		intakeSystem.spoonUp(spoonPiston);
		// System.out.println("autonomous init");
		botDrive.setSafetyEnabled(false); // Prevents "output not updated
											// enough" error message

		autonomousChooserSystem.checkChoices();
		// Autonomous.startAutonomous();

		botDrive.setSafetyEnabled(false); // Prevents "output not updated
											// enough" error message
	} // autonomous brace

	/**
	 * This function is called periodically during autonomous
	 */

	public void autonomousPeriodic() {

	}

	public void teleopInit() {
		stopEverything(); // stops all motors
		intakeSystem.spoonUp(spoonPiston); // Move spoon up at the beginning
	}

	public void teleopPeriodic() {
		botDrive.setSafetyEnabled(true); // Helps prevent "output not updated
											// enough"

		obstacleMotorSpeed = (((-joystickOp.getThrottle()) + 1) / 2);
		// Operator joystick throttle (0.0 bottom to 1.0 top)

		// MAKE SURE YOU HAVE FLIPPY SPEED AT NOT ZERO (not down)
		obstacleMotorManualOverride = joystickOp.getRawButton(RobotMap.JOYSTICK_OBSTACLE_MOTOR_MANUAL_OVERRIDE_BUTTON);
		
		if (obstacleMotorUpperLimitSwitch.get() && obstacleMotorLowerLimitSwitch.get()) {
			obstacleMotorManualOverride = true;
			// If both limit switches are triggered, automatically manual override (shouldn't happen)
		}
		if ((joystickOp.getPOV(0) == 0 || joystickOp.getPOV(0) == 45 || joystickOp.getPOV(0) == 315)
				&& (!obstacleMotorUpperLimitSwitch.get() || obstacleMotorManualOverride)) {
			obstacleMotor.set(-obstacleMotorSpeed); // Go up
		} else if ((joystickOp.getPOV(0) == 180 || joystickOp.getPOV(0) == 225 || joystickOp.getPOV(0) == 135)
				&& (!obstacleMotorLowerLimitSwitch.get() || obstacleMotorManualOverride)) {
			obstacleMotor.set(obstacleMotorSpeed); // Go down
		} else {
			obstacleMotor.set(0);
		}
		
		// TODO remove if limit switch works
//		if (obstacleMotorManualOverride) {
//			if ((joystickOp.getPOV(0) == 0 || joystickOp.getPOV(0) == 45 || joystickOp.getPOV(0) == 315)) {
//				obstacleMotor.set(-obstacleMotorSpeed); // Go up
//			} else if ((joystickOp.getPOV(0) == 180 || joystickOp.getPOV(0) == 225 || joystickOp.getPOV(0) == 135)) {
//				obstacleMotor.set(obstacleMotorSpeed); // Go down
//			} else {
//				obstacleMotor.set(0);
//			}
//		} else { // normal circumstances
//			if ((joystickOp.getPOV(0) == 0 || joystickOp.getPOV(0) == 45 || joystickOp.getPOV(0) == 315)
//					&& !obstacleMotorUpperLimitSwitch.get()) {
//				obstacleMotor.set(-obstacleMotorSpeed); // Go up
//			} else if ((joystickOp.getPOV(0) == 180 || joystickOp.getPOV(0) == 225 || joystickOp.getPOV(0) == 135)
//					&& !obstacleMotorLowerLimitSwitch.get()) {
//				obstacleMotor.set(obstacleMotorSpeed); // Go down
//			} else {
//				obstacleMotor.set(0);
//			}
//		}

		// gearbox switch
		boolean gearboxSwitchingButtonIsPressed = joystickRight.getRawButton(RobotMap.JOYSTICK_GEARSHIFT_BUTTON);
		if (gearboxSwitchingButtonIsPressed && !gearboxSwitchingPressedLastTime) {
			// driveController.switchModes(gearboxPistonLeft); TODO uncomment
		}
		gearboxSwitchingPressedLastTime = gearboxSwitchingButtonIsPressed;

		try {
			// switch front of robot
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
			// end switch front of robot
			
			// camera streaming
			if (rearCam) {
				cameraBack.getImage(img);
			} else {
				cameraFront.getImage(img);
			}
			camServer.setImage(img); // puts image on the dashboard
		} catch (Exception e) {
			// switch front of robot
			boolean cameraToggleButtonPressed = joystickLeft.getRawButton(RobotMap.JOYSTICK_CAMERA_TOGGLE_BUTTON);
			if (cameraToggleButtonPressed && !cameraSwitchPressedLastTime) {
				rearCam = !rearCam;
			}
			cameraSwitchPressedLastTime = cameraToggleButtonPressed;
			// finish switching front of robot
		} // end catch

		driveMultiplier = driveController.setDriveMultiplier(driveMultiplier);

		leftJoystickY = joystickLeft.getY();
		rightJoystickY = joystickRight.getY();

		if (rearCam) {
			leftDriveSpeed = leftJoystickY * driveMultiplier * -1;
			rightDriveSpeed = rightJoystickY * driveMultiplier * -1;
			// If using the rear cam, we always want the drive multiplier to be
			// negative (go backwards)
		} else {
			leftDriveSpeed = joystickRight.getY() * driveMultiplier;
			rightDriveSpeed = joystickLeft.getY() * driveMultiplier;
		}

		// This sets P, I, and D from throttle (bottom is zero)
		// double kP = (((joystickLeft.getZ() * -1) + 1) / 2.0) * 0.01;
		// double kI = (((joystickRight.getZ() * -1) + 1) / 2.0) * 0.01;
		// double kD = (((joystickOp.getThrottle() * -1) + 1) / 2.0) * 0.01;

		// This sets P, I, and D to default values
		kP = RobotMap.P_BRAKE;
		kI = RobotMap.I_BRAKE;
		kD = RobotMap.D_BRAKE;

		if (joystickRight.getRawButton(RobotMap.JOYSTICK_BRAKE_MODE_BUTTON)) {
			driveController.pidBrake(pidMode, pidLeftDrive, pidRightDrive, leftDriveEncoder, rightDriveEncoder, kP, kI,
					kD);
		} else {
			driveController.stopPIDBrake(pidMode, pidLeftDrive, pidRightDrive);
			if (joystickRight.getRawButton(RobotMap.JOYSTICK_SPLIT_ARCADE_DRIVE_BUTTON)) {
				botDrive.drive(joystickRight.getY() * driveMultiplier, joystickLeft.getX() * driveMultiplier); // Split
																												// arcade
			} else {
				driveController.tankDrive(leftDriveSpeed, rightDriveSpeed);
			}
		}

		// TODO Go set distance AND go straight

		intakeSystem.intakeStateMachine(spoonPiston, ballOpticalSensor);
		shooterSystem.shooterStateMachine(spoonPiston);

		printEverything();
	}

	public void printEverything() {
		SmartDashboard.putNumber("P", kP);
		SmartDashboard.putNumber("I", kI);
		SmartDashboard.putNumber("D", kD);
		// SmartDashboard.putNumber("Shooter Bottom Motor", shooterSpeed);
		// SmartDashboard.putNumber("Shooter Top Motor", shooterSpeed);
		SmartDashboard.putNumber("Back Left Motor Speed", backLeftMotor.get());
		SmartDashboard.putNumber("Back Right Motor Speed", backRightMotor.get());
		SmartDashboard.putNumber("Front Left Motor Speed", frontLeftMotor.get());
		SmartDashboard.putNumber("Front Right Motor Speed", frontRightMotor.get());
		SmartDashboard.putNumber("Left Encoder Value", leftDriveEncoder.get());
		SmartDashboard.putNumber("Right Encoder Value", rightDriveEncoder.get());
		SmartDashboard.putBoolean("Going Set Distance", goingSetDistance);
		SmartDashboard.putBoolean("Ball Present", !ballOpticalSensor.get());
		SmartDashboard.putNumber("Flippy Speed", obstacleMotorSpeed);
		SmartDashboard.putBoolean("Upper LS", obstacleMotorUpperLimitSwitch.get());
		SmartDashboard.putBoolean("Lower LS", obstacleMotorLowerLimitSwitch.get());
		SmartDashboard.putNumber("Left Joystick Y", leftJoystickY);
		SmartDashboard.putNumber("Right Joystick Y", rightJoystickY);
		SmartDashboard.putNumber("Drive Multiplier", (driveMultiplier));
		SmartDashboard.putNumber("Left Speed", leftDriveSpeed);
		SmartDashboard.putNumber("Right Speed", rightDriveSpeed);
		SmartDashboard.putBoolean("Flippy Thing Manual Override", obstacleMotorManualOverride);
		SmartDashboard.putNumber("Shooter Bottom Encoder Value", shooterBottomMotor.getPosition());
		SmartDashboard.putNumber("Shooter Top Encoder Value", shooterTopMotor.getPosition());
		if (rearCam) {
			SmartDashboard.putString("Front Side", "BATTERY");
		} else {
			SmartDashboard.putString("Front Side", "INTAKE");
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		printEverything();
	}

	public void disabledInit() {
		System.out.println("Left Speed " + frontLeftMotor.get() + " " + leftDriveSpeed);
		System.out.println("Right Speed " + frontRightMotor.get() + " " + rightDriveSpeed);
		botDrive.setSafetyEnabled(false); // Prevents "output not updated
											// enough" error message
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
		// pidTopShooter.disable();
		// pidBottomShooter.disable();
	}
}

/**
 * PIDReverseCANTalon is a subclass of CANTalon that reverses outputs to PID.
 * This is used for a PIDController, which is unable to reverse its outputs, to
 * the author's knowledge.
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