
package org.usfirst.frc.team972.robot;

import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
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
	// Joystick joystickOp = new Joystick(RobotMap.JOYSTICK_OP_USB_PORT);

	CANTalon frontLeftMotor = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_CAN_ID);
	CANTalon frontRightMotor = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_CAN_ID);
	CANTalon backLeftMotor = new CANTalon(RobotMap.BACK_LEFT_MOTOR_CAN_ID);
	CANTalon backRightMotor = new CANTalon(RobotMap.BACK_RIGHT_MOTOR_CAN_ID);
	
	// CANTalon shooterBottomMotor = new
	// CANTalon(RobotMap.SHOOTER_BOTTOM_MOTOR_CAN_ID);
	// CANTalon shooterTopMotor = new
	// CANTalon(RobotMap.SHOOTER_TOP_MOTOR_CAN_ID);
	// CANTalon intakeMotor = new CANTalon(RobotMap.INTAKE_MOTOR_CAN_ID);
	// CANTalon obstacleMotor = new CANTalon(RobotMap.OBSTACLE_MOTOR_CAN_ID);
	//
	// Encoder leftDriveEncoder = new
	// Encoder(RobotMap.LEFT_DRIVE_ENCODER_DIO_A_PORT,
	// RobotMap.LEFT_DRIVE_ENCODER_DIO_B_PORT);
	// Encoder rightDriveEncoder = new
	// Encoder(RobotMap.RIGHT_DRIVE_ENCODER_DIO_A_PORT,
	// RobotMap.RIGHT_DRIVE_ENCODER_DIO_B_PORT);
	// Encoder shooterBottomEncoder = new
	// Encoder(RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_A_PORT,
	// RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_B_PORT);
	// Encoder shooterTopEncoder = new
	// Encoder(RobotMap.SHOOTER_TOP_ENCODER_DIO_A_PORT,
	// RobotMap.SHOOTER_TOP_ENCODER_DIO_B_PORT);

	RobotDrive robotDrive = new RobotDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

	double driveMultiplier = RobotMap.STARTING_DRIVE_MULTIPLIER;
	// Multiplied with joystick values to set the speed of the drive motor
	double leftDriveSpeed = 0.0;
	// this should always be 0 because if not there's something wrong.
	double rightDriveSpeed = 0.0;

	boolean speedBoostPressedLastTime = false;
	boolean cameraSwitchPressedLastTime = false;

	Image img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	
	boolean rearCam = false;
	// stores whether the front camera is on
	
	USBCamera cameraFront = new USBCamera("cam0");
	USBCamera cameraBack = new USBCamera("cam1");
	
	CameraServer camServer = CameraServer.getInstance();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		
		try {
			cameraFront.openCamera();
	    	cameraBack.openCamera();
	    	cameraFront.startCapture(); // startCapture so that it doesn't try to take a picture 
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

	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
	
	}

	/**
	 * This function is called periodically during operator control
	 */
	
	public void teleopPeriodic() {
		boolean cameraToggleButtonPressed = joystickRight.getRawButton(RobotMap.JOYSTICK_CAMERA_TOGGLE_BUTTON);
		if (cameraToggleButtonPressed) {
			if (cameraSwitchPressedLastTime == false) {
				if (rearCam) {
					cameraBack.stopCapture();
					cameraFront.startCapture();
					rearCam = false;
				} else { 
					cameraFront.stopCapture();
					cameraBack.startCapture();
					rearCam = true;
				}
				driveMultiplier *= -1;
			}
		}
		cameraSwitchPressedLastTime = cameraToggleButtonPressed;

		if (rearCam == true) {
			cameraBack.getImage(img);
		} else {
			cameraFront.getImage(img);
		}
		camServer.setImage(img); // puts image on the dashboard
		
		boolean driveSpeedToggleButtonPressed = joystickLeft.getRawButton(RobotMap.JOYSTICK_SPEED_TOGGLE_BUTTON);
		if (driveSpeedToggleButtonPressed) {
			if (!speedBoostPressedLastTime) {
				if (driveMultiplier == RobotMap.STARTING_DRIVE_MULTIPLIER) {
					driveMultiplier = RobotMap.FAST_MODE_DRIVE_MULTIPLIER;
				} else {
					driveMultiplier = RobotMap.STARTING_DRIVE_MULTIPLIER;
				}
			}
		}
		speedBoostPressedLastTime = driveSpeedToggleButtonPressed;

		leftDriveSpeed = joystickLeft.getY() * driveMultiplier;
		rightDriveSpeed = joystickRight.getY() * driveMultiplier;

		SmartDashboard.putNumber("Drive Multiplier", driveMultiplier);
		SmartDashboard.putNumber("Left Speed", leftDriveSpeed);
		SmartDashboard.putNumber("Right Speed", rightDriveSpeed);

		robotDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
