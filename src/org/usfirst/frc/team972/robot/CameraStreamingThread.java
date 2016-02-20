package org.usfirst.frc.team972.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.vision.*;

public class CameraStreamingThread implements Runnable {
	Robot r;
	Image img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	CameraServer camServer = CameraServer.getInstance();

	public CameraStreamingThread(Robot r) {
		this.r = r;
		camServer.setQuality(50);
	}

	public void run() {
		while (r.isOperatorControl() && r.isEnabled()) {
			// camera streaming
			if (Robot.rearCam) {
				Robot.camBack.getImage(img);
			} else {
				Robot.camFront.getImage(img);
			}
			camServer.setImage(img); // puts image on the dashboard
		}
	}
	

	public void reverse() {
		// switch front of robot
		if (Robot.rearCam) {
			Robot.camBack.stopCapture();
			Robot.camFront.startCapture();
		} else {
			Robot.camFront.stopCapture();
			Robot.camBack.startCapture();
		}
		// end switch front of robot
	}
}
