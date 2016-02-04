
package org.usfirst.frc.team972.robot;

import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Sendable;
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
	
	
	// CANTalon shooterBottomMotor = new
	// CANTalon(RobotMap.SHOOTER_BOTTOM_MOTOR_CAN_ID);
	// CANTalon shooterTopMotor = new
	// CANTalon(RobotMap.SHOOTER_TOP_MOTOR_CAN_ID);
	// CANTalon intakeMotor = new CANTalon(RobotMap.INTAKE_MOTOR_CAN_ID);
	// CANTalon obstacleMotor = new CANTalon(RobotMap.OBSTACLE_MOTOR_CAN_ID);
	
	Compressor compressor = new Compressor(RobotMap.PCM_CAN_ID);
	
//	 Encoder leftDriveEncoder = new Encoder(RobotMap.LEFT_DRIVE_ENCODER_DIO_A_PORT, RobotMap.LEFT_DRIVE_ENCODER_DIO_B_PORT) ;
//	 Encoder rightDriveEncoder = new
//	 Encoder(RobotMap.RIGHT_DRIVE_ENCODER_DIO_A_PORT,
//	 RobotMap.RIGHT_DRIVE_ENCODER_DIO_B_PORT);
//	 Encoder(RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_A_PORT,
//	 RobotMap.SHOOTER_BOTTOM_ENCODER_DIO_B_PORT);
//	 Encoder shooterTopEncoder = new
//	 Encoder(RobotMap.SHOOTER_TOP_ENCODER_DIO_A_PORT,
//	 RobotMap.SHOOTER_TOP_ENCODER_DIO_B_PORT);

	RobotDrive robotDrive = new RobotDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

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
	
	DoubleSolenoid gearboxPistonLeft = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.PISTON_GEARBOX_SHIFTING_FORWARD_CHANNEL, RobotMap.PISTON_GEARBOX_SHIFTING_REVERSE_CHANNEL);
	boolean gearboxSwitchingPressedLastTime = false;
	boolean gearboxSwitchingButtonIsPressed = false;
	boolean gearboxPistonLeftForward = false;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		compressor.start();
		
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		
//		leftDriveEncoder.reset();
//		rightDriveEncoder.reset();
		
		
		try {
			cameraFront = new USBCamera("cam0");
			cameraBack = new USBCamera("cam1");
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
		gearboxSwitchingButtonIsPressed = joystickOp.getRawButton(RobotMap.JOYSTICK_GEARBOX_PISTON_BUTTON);
		if (gearboxSwitchingButtonIsPressed) {
			if (!gearboxSwitchingPressedLastTime) {
				if (gearboxPistonLeftForward == false) {
					gearboxPistonLeft.set(DoubleSolenoid.Value.kForward);
					gearboxPistonLeftForward = true;
				} else {
					gearboxPistonLeft.set(DoubleSolenoid.Value.kReverse);
					gearboxPistonLeftForward = false;
				}
			}
		}
		gearboxSwitchingPressedLastTime = gearboxSwitchingButtonIsPressed;
		
		boolean cameraToggleButtonPressed = joystickLeft.getRawButton(RobotMap.JOYSTICK_CAMERA_TOGGLE_BUTTON);
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
			}
		}
		cameraSwitchPressedLastTime = cameraToggleButtonPressed;

		if (rearCam == true) {
			cameraBack.getImage(img);
			SmartDashboard.putString("Front", "LED"); // TODO Change for real robot :)
		} else {
			cameraFront.getImage(img);
			SmartDashboard.putString("Front", "PISTON"); // TODO Change for real robot :)
		}
		camServer.setImage(img); // puts image on the dashboard
		
		// NOTE -- SMART DASHBOARD CAMERA FEED ISN'T ENABLED PROGRAMMATICALLY
		// USE View->Add...->USBWebcamViewer
		
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
		
		if (rearCam) {
			driveMultiplier = -Math.abs(driveMultiplier);
			// If using the rear cam, we always want the drive multiplier to be negative
		} else {
			driveMultiplier = Math.abs(driveMultiplier);
		}
		
		leftDriveSpeed = joystickLeft.getY() * driveMultiplier;
		rightDriveSpeed = joystickRight.getY() * driveMultiplier;

		SmartDashboard.putNumber("Drive Multiplier", (driveMultiplier));
		SmartDashboard.putNumber("Left Speed", leftDriveSpeed);
		SmartDashboard.putNumber("Right Speed", rightDriveSpeed);
//		SmartDashboard.putNumber("Encoder Left", leftDriveEncoder.getPeriod());
		//SmartDashboard.putNumber("Encoder Right", rightDriveEncoder.getDistance());
		//TODO

		robotDrive.tankDrive(leftDriveSpeed, rightDriveSpeed);
		
		if (joystickRight.getRawButton(RobotMap.JOYSTICK_BRAKE_MODE_BUTTON)) {
			frontLeftMotor.enableBrakeMode(true);
			frontRightMotor.enableBrakeMode(true);
			backLeftMotor.enableBrakeMode(true);
			backRightMotor.enableBrakeMode(true); 
		} else {
			frontLeftMotor.enableBrakeMode(false);
			frontRightMotor.enableBrakeMode(false);
			backLeftMotor.enableBrakeMode(false);
			backRightMotor.enableBrakeMode(false);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
//