/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.UsbCamera;
/**
 * The class which takes care of cameras
 */
public class Cameras {
    int camerasFPS = 24;
    int camerasWidth = 200;
    int camerasHeight= 100;


    public Cameras(){
        UsbCamera wdnmd = CameraServer.getInstance().startAutomaticCapture(0);
        UsbCamera NMSL = CameraServer.getInstance().startAutomaticCapture(1);

        // wdnmd.setResolution(camerasWidth, camerasWidth);
        // wdnmd.setFPS(camerasFPS);
        // NMSL.setResolution(camerasWidth, camerasWidth);
        // NMSL.setFPS(camerasFPS);
    }
}
