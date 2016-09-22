
package org.usfirst.frc.team972.robot;

import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
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
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
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
	public static CANTalon frontRightMotor = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
	public static CANTalon backLeftMotor = new CANTalon(RobotMap.BACK_LEFT_MOTOR_CAN_ID);
	public static CANTalon backRightMotor = new CANTalon(RobotMap.BACK_RIGHT_MOTOR_CAN_ID);

	/*
	 * NOTE: this is called a PID Reverse CAN Talon because PIDControllers
	 * cannot reverse output on their own. Therefore, we subclassed the CANTalon
	 * object and reversed all PID Outputs Normal outputs are NOT reversed on a
	 * PIDReverseCANTalon See source code for this subclass at bottom of
	 * Robot.java. This class is not currently in use.
	 */

	// public static PIDReverseCANTalon frontRightMotor = new
	// PIDReverseCANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);

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

	public static PIDController pidRightDrive = new PIDController(0, 0, 0, rightDriveEncoder, frontRightMotor);
	public static PIDController pidLeftDrive = new PIDController(0, 0, 0, leftDriveEncoder, frontLeftMotor);
	public static PIDController driveStraightRightPID = new PIDController(0, 0, 0, rightDriveEncoder, frontRightMotor);
	public static PIDController driveStraightLeftPID = new PIDController(0, 0, 0, leftDriveEncoder, frontLeftMotor);

	// These are used for PID Shooter

	// public static PIDController pidBottomShooter = new PIDController(0, 0, 0,
	// shooterBottomEncoder, frontLeftMotor);
	// public static PIDController pidTopShooter = new PIDController(0, 0, 0,
	// shooterTopEncoder, frontRightMotor);

	// pneumatics
	public static Compressor compressor = new Compressor(RobotMap.PCM_CAN_ID);
	public static DoubleSolenoid gearboxPiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID,
			RobotMap.PISTON_GEARBOX_SHIFTING_FORWARD_CHANNEL, RobotMap.PISTON_GEARBOX_SHIFTING_REVERSE_CHANNEL);
	public static DoubleSolenoid spoonPiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID,
			RobotMap.PISTON_BALL_PUSHER_FORWARD_CHANNEL, RobotMap.PISTON_BALL_PUSHER_REVERSE_CHANNEL);
	public static DoubleSolenoid outtakePiston = new DoubleSolenoid(RobotMap.PCM_CAN_ID,
			RobotMap.PISTON_OUTTAKE_FORWARD_CHANNEL, RobotMap.PISTON_OUTTAKE_REVERSE_CHANNEL);

	// sensors
	public static DigitalInput ballOpticalSensor = new DigitalInput(RobotMap.BALL_OPTICAL_SENSOR_PORT);
	public static DigitalInput obstacleMotorUpperLimitSwitch = new DigitalInput(
			RobotMap.FLIPPY_THING_UPPER_LIMIT_SWITCH);
	public static DigitalInput obstacleMotorLowerLimitSwitch = new DigitalInput(
			RobotMap.FLIPPY_THING_LOWER_LIMIT_SWITCH);
	// TODO: Pick a name, flippy thing or obstacle motor

	// TODO: Make this work
	public static AnalogGyro gyro;

	// intake
	static Intake intakeSystem = new Intake(intakeMotor, spoonPiston, outtakePiston, ballOpticalSensor);
	static Shooter shooterSystem = new Shooter(shooterTopMotor, shooterBottomMotor, spoonPiston);
	static Drive driveController = new Drive(botDrive, frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);

	public static Thread cstThread;
	// This is a thread to stream camera while not lagging the robot

	// speeds and multipliers
	double driveMultiplier = RobotMap.DEFAULT_DRIVE_MODE;
	double leftDriveSpeed = 0.0;
	double rightDriveSpeed = 0.0;

	// camera stuff
	static boolean cameraSwitchPressedLastTime = false; // used for camera
														// toggle
	static boolean rearCam = true; // if streaming rear cam currently

	public static USBCamera camFront;
	public static USBCamera camBack;

	// gearbox switching variables
	boolean gearboxSwitchingPressedLastTime = false; // used for gearbox toggle

	// PID variables
	boolean pidMode = false;

	// shooter piston variables
	boolean shooterPistonPressedLastTime = false;
	boolean shooterPistonForward = false;
	long pistonTimer = -1; // -1 shows that it hasn't been started yet

	// intake button variables
	boolean intakeButtonPressed = false;
	boolean intakeReverseButtonPressed = false;

	// obstacle motor variables
	boolean flippyUpMode = false;

	static long autonomousDelayStartTime;

	static double obstacleMotorSpeed;
	static boolean obstacleMotorManualOverride;

	// shooter variables
	double shooterTopSpeed = 0;
	boolean shooterHighSpeedMotorButtonPressed = false;
	boolean shooterMediumSpeedMotorButtonPressed = false;
	boolean shooterSlowSpeedMotorButtonPressed = false;
	boolean shooterStopMotorButtonPressed = false;

	// set distance variables
	boolean leftDistance = false;
	boolean goingSetDistance = false;

	static double kP;
	static double kI;
	static double kD;
	double leftJoystickY;
	double rightJoystickY;

	CameraStreamingThread cst;
	Image img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	CameraServer camServer = CameraServer.getInstance();
	public boolean shooterReverseButtonPressed;
	public double shooterBottomSpeed = 0;

	public boolean brakeMode; // Brake vs Coast Motor Mode
	public boolean brakeCoastButtonPressedLastTime = false;

	SendableChooser autonomousDefenseChooser = new SendableChooser();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		System.out.println("Start robotInit()");

		// This is for Shooter PID, which we are not using
		// shooterTopMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		// shooterBottomMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		// shooterTopMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		// shooterBottomMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		// shooterTopMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		// shooterBottomMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		// shooterTopMotor.changeControlMode(TalonControlMode.Speed);
		// shooterBottomMotor.changeControlMode(TalonControlMode.Speed);

		shooterTopMotor.changeControlMode(TalonControlMode.PercentVbus);
		shooterBottomMotor.changeControlMode(TalonControlMode.PercentVbus);

		frontLeftMotor.changeControlMode(TalonControlMode.PercentVbus);
		frontRightMotor.changeControlMode(TalonControlMode.PercentVbus);
		backLeftMotor.changeControlMode(TalonControlMode.PercentVbus);
		backRightMotor.changeControlMode(TalonControlMode.PercentVbus);

		compressor.start();
		// compressor.stop();

		botDrive.setSafetyEnabled(false);
		// Prevents "output not updated enough" message mostly

		// TODO: Fix gyro
		try {
			gyro = new AnalogGyro(0);
		} catch (Exception e) {
			System.out.println("Gyro fail: " + e);
		}

		// shooterBottomEncoder.setPIDSourceType(PIDSourceType.kRate);
		// shooterTopEncoder.setPIDSourceType(PIDSourceType.kRate);
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
		System.out.println("Start autonomousInit()");

		compressor.stop(); // To keep consistent voltage while moving
		driveController.switchToLowGear(gearboxPiston);
		intakeSystem.spoonUp();

		frontLeftMotor.enableBrakeMode(true);
		frontRightMotor.enableBrakeMode(true);
		backLeftMotor.enableBrakeMode(true);
		backRightMotor.enableBrakeMode(true);

		Autonomous.createChooser(); // creates the button choices on
									// SmartDashboards
		Autonomous.runAutonomous(this); // runs the autonomous routine selected
										// on SmartDashboard

		leftDriveEncoder.reset();
		rightDriveEncoder.reset();

		SmartDashboard.putString("Autonomous Mode", "Done");
		System.out.println("End autonomousInit()");

	} // autonomous brace

	/**
	 * This function is called periodically during autonomous
	 */

	public void autonomousPeriodic() {
		System.out.println("Start autonomousPeriodic()");
		// our autonomous runs exclusively in autonomousInit()
		System.out.println("End autonomousPeriodic()");
	}

	public void teleopInit() {
		System.out.println("Start teleopInit()");

		compressor.start();
		stopEverything(); // stops all motors

		// Brake vs Coast Motor Mode
		brakeMode = false;
		frontLeftMotor.enableBrakeMode(false);
		frontRightMotor.enableBrakeMode(false);
		backLeftMotor.enableBrakeMode(false);
		backRightMotor.enableBrakeMode(false);

		cst = new CameraStreamingThread(this);
		new Thread(cst).start();

		intakeSystem.spoonUp(); // Move spoon up at the beginning
		outtakePiston.set(DoubleSolenoid.Value.kReverse);

		System.out.println("End teleopInit()");
	}

	public void teleopPeriodic() {
		System.out.println("Left Drive Encoder " + leftDriveEncoder.get());
		System.out.println("Right Drive Encoder " + rightDriveEncoder.get());

		botDrive.setSafetyEnabled(true);
		// Helps prevent "output not updated enough"

		if (joystickOp.getRawButton(RobotMap.JOYSTICK_OBSTACLE_MOTOR_KEEP_FLIPPY_UP_BUTTON)) {
			flippyUpMode = true;
		}

		obstacleMotorSpeed = (((-joystickOp.getThrottle()) + 1) / 2);
		// Operator joystick throttle (0.0 bottom to 1.0 top)

		// MAKE SURE YOU HAVE FLIPPY SPEED AT NOT ZERO (not down)
		obstacleMotorManualOverride = joystickOp.getRawButton(RobotMap.JOYSTICK_OBSTACLE_MOTOR_MANUAL_OVERRIDE_BUTTON);
		if (obstacleMotorManualOverride) {
			flippyUpMode = false;
		}

		if (obstacleMotorUpperLimitSwitch.get() && obstacleMotorLowerLimitSwitch.get()) {
			obstacleMotorManualOverride = true;
			// If both limit switches are triggered, automatically manual
			// override (shouldn't happen)
		}
		if (flippyUpMode) {
			keepFlippyUp();
		} else {
			// the joystickOp POV is the hat
			// Limit Switch true when not pressed due to wiring
			if ((joystickOp.getPOV(0) == 0 || joystickOp.getPOV(0) == 45 || joystickOp.getPOV(0) == 315)
					&& (obstacleMotorUpperLimitSwitch.get() || obstacleMotorManualOverride)) {
				obstacleMotor.set(-obstacleMotorSpeed); // Go up
			} else if ((joystickOp.getPOV(0) == 180 || joystickOp.getPOV(0) == 225 || joystickOp.getPOV(0) == 135)
					&& (obstacleMotorLowerLimitSwitch.get() || obstacleMotorManualOverride)) {
				obstacleMotor.set(obstacleMotorSpeed); // Go down
			} else {
				obstacleMotor.set(0);
			}
		}

		// gearshift
		boolean gearboxSwitchingButtonIsPressed = joystickRight.getRawButton(RobotMap.JOYSTICK_GEARSHIFT_BUTTON);
		if (gearboxSwitchingButtonIsPressed && !gearboxSwitchingPressedLastTime) {
			driveController.switchModes(gearboxPiston);
		}
		gearboxSwitchingPressedLastTime = gearboxSwitchingButtonIsPressed;

		// switch front of robot
		boolean cameraToggleButtonPressed = joystickLeft.getRawButton(RobotMap.JOYSTICK_CAMERA_TOGGLE_BUTTON);
		if (cameraToggleButtonPressed && !cameraSwitchPressedLastTime) {
			driveController.reverse();
		}
		cameraSwitchPressedLastTime = cameraToggleButtonPressed;
		// end switch front of robot

		leftJoystickY = joystickLeft.getY();
		rightJoystickY = joystickRight.getY();

		if (rearCam) {
			leftDriveSpeed = leftJoystickY * driveMultiplier * -1;
			rightDriveSpeed = rightJoystickY * driveMultiplier * -1;
			// The drive speeds ARE supposed to equal the opposite motor
			// If using the rear cam, we always want the drive multiplier to be
			// negative (go backwards)
		} else {
			leftDriveSpeed = rightJoystickY * driveMultiplier;
			rightDriveSpeed = leftJoystickY * driveMultiplier;
		}

		// This sets P, I, and D from throttle (bottom is zero)
		// double kP = (((joystickLeft.getZ() * -1) + 1) / 2.0) * 0.01;
		// double kI = (((joystickRight.getZ() * -1) + 1) / 2.0) * 0.01;
		// double kD = (((joystickOp.getThrottle() * -1) + 1) / 2.0) * 0.01;

		// This sets P, I, and D to default values
		kP = RobotMap.P_BRAKE;
		kI = RobotMap.I_BRAKE;
		kD = RobotMap.D_BRAKE;

		// Change brake/coast using toggle or buttons

		if (joystickRight.getRawButton(RobotMap.JOYSTICK_BRAKE_BUTTON)) {
			frontLeftMotor.enableBrakeMode(true);
			frontRightMotor.enableBrakeMode(true);
			backLeftMotor.enableBrakeMode(true);
			backRightMotor.enableBrakeMode(true);
		} else if (joystickRight.getRawButton(RobotMap.JOYSTICK_COAST_BUTTON)) {
			frontLeftMotor.enableBrakeMode(false);
			frontRightMotor.enableBrakeMode(false);
			backLeftMotor.enableBrakeMode(false);
			backRightMotor.enableBrakeMode(false);
		}

		boolean brakeCoastButtonPressed = joystickRight.getRawButton(RobotMap.JOYSTICK_TOGGLE_BRAKE_COAST_BUTTON);
		if (brakeCoastButtonPressed) {
			if (!brakeCoastButtonPressedLastTime) {
				if (brakeMode) {
					brakeMode = false;
					frontLeftMotor.enableBrakeMode(false);
					frontRightMotor.enableBrakeMode(false);
					backLeftMotor.enableBrakeMode(false);
					backRightMotor.enableBrakeMode(false);
				} else {
					brakeMode = true;
					frontLeftMotor.enableBrakeMode(true);
					frontRightMotor.enableBrakeMode(true);
					backLeftMotor.enableBrakeMode(true);
					backRightMotor.enableBrakeMode(true);
				}
			}
		}
		brakeCoastButtonPressedLastTime = brakeCoastButtonPressed;

		if (joystickLeft.getRawButton(RobotMap.JOYSTICK_BRAKE_MODE_BUTTON)) {
			driveController.pidBrake(pidMode, pidLeftDrive, pidRightDrive, leftDriveEncoder, rightDriveEncoder, kP, kI,
					kD);
		} else {
			driveController.stopPIDBrake(pidMode, pidLeftDrive, pidRightDrive);
			driveController.tankDrive(leftDriveSpeed, rightDriveSpeed);
		}

		intakeSystem.intakeStateMachine();
		shooterSystem.shooterStateMachine();

		printEverything();
	}

	public static void printEverything() {
		SmartDashboard.putNumber("P", kP);
		SmartDashboard.putNumber("I", kI);
		SmartDashboard.putNumber("D", kD);
		SmartDashboard.putNumber("Shooter Bottom Motor Speed", shooterBottomMotor.getSpeed());
		SmartDashboard.putNumber("Shooter Top Motor Speed", shooterTopMotor.getSpeed());
		SmartDashboard.putNumber("Left Encoder Value", leftDriveEncoder.get());
		SmartDashboard.putNumber("Right Encoder Value", -rightDriveEncoder.get());
		SmartDashboard.putBoolean("Ball Present", !ballOpticalSensor.get()); // true with ball
		SmartDashboard.putNumber("Flippy Speed", obstacleMotorSpeed);
		SmartDashboard.putBoolean("Upper LS", !obstacleMotorUpperLimitSwitch.get()); // true default
		SmartDashboard.putBoolean("Lower LS", !obstacleMotorLowerLimitSwitch.get()); // true default
		 SmartDashboard.putBoolean("Flippy Thing Manual Override",
		 obstacleMotorManualOverride);
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
		System.out.println("Start disabledInit()");
		stopEverything();
		RobotMap.haveCam = true;

		System.out.println("End disabledInit()");
	}

	public void setDriveMotorsToLeaders() {
		frontLeftMotor.changeControlMode(TalonControlMode.PercentVbus);
		frontRightMotor.changeControlMode(TalonControlMode.PercentVbus);
		backLeftMotor.changeControlMode(TalonControlMode.PercentVbus);
		backRightMotor.changeControlMode(TalonControlMode.PercentVbus);
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
		shooterTopSpeed = 0;
		pidMode = false;
	}

	public void keepFlippyUp() {
		if (obstacleMotorUpperLimitSwitch.get()) {
			obstacleMotor.set(-0.5);
		} else {
			obstacleMotor.set(0.0);
		}
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
