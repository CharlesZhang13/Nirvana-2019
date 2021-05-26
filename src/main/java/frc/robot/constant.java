/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * This class is used for storing constants
 */
public class constant {
    public static boolean reverse = false;

    public final static int kPIDLoopIdx = 0;
    public final static int kTimeoutMs = 10;

    //base
    public final static double VCoefficient = 500.0*(4096.0/600.0);
    public static double velocityControl = 0.75;

    //intaker angle
    public final static double maxOutput = 4096;
    public final static double maxOutAngle = 90.0;
    public final static double intakerAngleIntial = (5.0/maxOutAngle)*maxOutput;
    public final static double intakerIntakeAngle = (38.0/maxOutAngle)*maxOutput;
    public final static double intakerAngleRocket1 = (90.0/maxOutAngle)*maxOutput;
    public final static double intakerAngleRocket2 = (120.0/maxOutAngle)*maxOutput;
    public final static double intakerAngleRocket3 = (130.0/maxOutAngle)*maxOutput;
    public final static double intakerAngleMid = (40.0/maxOutAngle)*maxOutput;
    public final static double intakerAngleShip = (110.0/maxOutAngle)*maxOutput;
}
