package org.usfirst.frc.team972.robot;

import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.vision.*;

public class CameraStreamingThread implements Runnable {
	Robot r;
	Image img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	CameraServer camServer = CameraServer.getInstance();
	boolean rearCam;

	public CameraStreamingThread(Robot r) {
		this.r = r;
		camServer.setQuality(25);
		this.rearCam = false;
	}

	public void run() {
		try {
			Robot.camFront = new USBCamera("cam1");
			Robot.camBack = new USBCamera("cam0");
			Robot.camFront.openCamera();
			Robot.camBack.openCamera();
			Robot.camFront.startCapture();
		} catch (VisionException e) {
			System.out.println("VISION EXCEPTION ~ " + e);
		}
		while ((r.isAutonomous() || r.isOperatorControl()) && r.isEnabled() && RobotMap.haveCam) {
			try {
				// camera streaming
				this.checkReverse();
				if (rearCam) {
					Robot.camBack.getImage(img);
				} else {
					Robot.camFront.getImage(img);
				}
				camServer.setImage(img); // puts image on the dashboard
			} catch (Exception e) {
				System.out.println("Error " + e.toString());
				e.printStackTrace();
				RobotMap.haveCam = false;
				System.out.println("You don't have teh camera!");
			}
		}
	}

	private void checkReverse() {
		if(Robot.rearCam != this.rearCam) {
			reverse();
		}
	}
	
	private void reverse() {
		// switch front of robot
		// rearCam already switched
		if (!Robot.rearCam) {
			rearCam = Robot.rearCam;
			Robot.camBack.stopCapture();
			Robot.camFront.startCapture();
		} else {
			rearCam = Robot.rearCam;
			Robot.camFront.stopCapture();
			Robot.camBack.startCapture();
		}
		// end switch front of robot
	}

}
