/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * This class is used to store the ID of devices on robot
 */
public class pin {

    // base motor control
    public final static int LTalonID = 1;
    public final static int RTalonID = 3;
    public final static int LVictorID = 1;
    public final static int RVictorID = 0;

    // intake motor control
    public final static int IntakerVictorID = 3;
    public final static int FetchVictorID = 2;
    public final static int IntakerAngleMoterTalonID = 2;

    //Sensor pin
    public final static int Intaker = 0;

    //panel - unassaigned
    public final static int FPanelSolenoidForwardID = 4;
    public final static int FPanelSolenoidReverseID = 5;
    public final static int BPanelSolenoidForwardID = 7;
    public final static int BPanelSolenoidReverseID = 6;

    //climb
    public final static int ClimbTalonID = 0;
    public final static int PClimbSolenoidForwardID = 1;
    public final static int PClimbSolemoidReverseID = 0;
    public final static int MClimbSolenoidForwardID = 3;
    public final static int MClimbSolenoidReverseID = 2;
}
