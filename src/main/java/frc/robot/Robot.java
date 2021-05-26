/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  
  /**instantiate */
  Joystick MoveStick = new Joystick(0);
  Joystick FunctionStick = new Joystick(1);
  Base mBase = new Base(); 
  Intake mIntaker = new Intake();
  Panel mPanel = new Panel();
  Climb mClimb = new Climb();
  Cameras mCameras = new Cameras();
  // Compressor compressor = new Compressor();

  private Timer autoTimer = new Timer();
  private Timer intakeZeroTimer = new Timer();

  private Double intakeAxis = 0.0;

  private Boolean intakeManualMode = false;
  private Boolean intakeManualReleased = true;

  // constant

  private static int autoState = 0;
  private boolean shouldDisable = false;
  private int speedMode = 0;
  private int directionSign = 1;

  // Location

  /* VERY IMPORTANT !!!
    this variable should change before any match!!!
    this variable means the start position of the robot.
    1 represent the left position
    2 represent the middle position
    3 represent the right position
  */

  //private int robotLocation = DriverStation.getInstance().getLocation();
  private int robotLocation = 2;
  

  /**
   * This function is run whesn the robot is first started up and should be
   * used for any initializatsion code.
   */
  @Override
  public void robotInit() {
    intakeZeroTimer.start();
    mPanel.panelInit();
  }


  @Override
  public void robotPeriodic() {

  }

  @Override
  public void disabledInit() {
    super.disabledInit();
    mPanel.panelInit();
  }


  @Override
  public void autonomousInit() {
    autoState = 0;
    mBase.resetAll();
    autoTimer.reset();
    autoTimer.start();
  }


  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    this.teleopPeriodic();
    /*
    two automous code 
      1. when robot is in the middle of driver station
      2. when ronot is in the side of driver station
    */

    // the side with intake is the front; the side with panel is the back

    /* ALL THE ANGLE, TIME, VELOCITY GAVE IS NOT CORRECT!!!
      Test it according to the auto path
    */

    // switch (robotLocation){
    //   case 2:
    //     // when robot is in the middle
    //     switch (autoState){
    //       case 0:
    //         // make robot leave the habitat and leave the line
    //         if (autoTimer.get() < 3 ){
    //           mBase.velDrive(-0.5, 0);
    //         }else{
    //           mBase.rotate(-90);
    //           autoState ++;
    //           autoTimer.reset();
    //         }
    //         break;
    //       case 1:
    //         // rotate the robot so that the position of the robot is aimed for the panel in cargo ship
    //         if (autoTimer.get() < 0.5){
    //           mBase.velDrive(-0.2, 0);
    //         }else{
    //           mBase.rotate(330); // not sure it work... Test first and if not modify the rotate code!!
    //           autoState ++;
    //           autoTimer.reset();
    //         }
    //         break;
    //       case 2:
    //         // put the panel in
    //         if (autoTimer.get() < 1){
    //           mBase.velDrive(-0.1, 0);
    //         }else if(autoTimer.get() < 2.5){
    //           mPanel.setMode(1);
    //           mPanel.run(true);
    //         }else{
    //           autoState ++;
    //           autoTimer.reset();
    //         }
    //         break;
    //       case 3:
    //         // make the robot move to human player to collect one more panel
    //         if(autoTimer.get() < 1){
    //           mBase.velDrive(0.3, 0);
    //         }else{
    //           mBase.rotate(150);
    //           autoState ++;
    //           autoTimer.reset();
    //         }
    //         break;
    //       case 4:
    //         // adjust postion to aim the human player
    //         if (autoTimer.get() < 0.5){
    //           mBase.velDrive(-0.2, 0);
    //         }else{
    //           mBase.rotate(150); // still not sure
    //           autoState ++;
    //           autoTimer.reset();
    //         }
    //         break;
    //       case 5:
    //         // move and catch the panel
    //         if (autoTimer.get() < 1){
    //           mBase.velDrive(-0.3, 0);
    //         }else if(autoTimer.get() < 2.5){
    //           mPanel.setMode(0);
    //           mPanel.run(true);
    //         }else{
    //           autoState ++;
    //           autoTimer.reset();
    //         } 
    //         break;
    //     }
    //     break;
      
    //   default:
    //     // when robot is in the Left or Right
    //     switch(autoState){
    //       case 0:
    //         //move to the side of the cargo ship
    //         if (autoTimer.get() < 2){
    //           mBase.velDrive(-0.4, 0);
    //         }else{
    //           if(robotLocation == 1){
    //             mBase.rotate(90);
    //           }else{
    //             mBase.rotate(-90);
    //           }
    //           autoState ++;
    //           autoTimer.reset(); 
    //         }
    //         break;
    //       case 1:
    //         // put panel
    //         if (autoTimer.get() < 0.3){
    //           mBase.velDrive(-0.06, 0);
    //         }else if(autoTimer.get() < 1.8){
    //           mPanel.setMode(1);
    //           mPanel.run(true);
    //         }else{
    //           autoState ++;
    //           autoTimer.reset(); 
    //         }
    //         break;
    //       case 2:
    //         // change position
    //         if(autoTimer.get() < 0.5){
    //           mBase.velDrive(0.1, 0);
    //         }else{
    //           if (robotLocation == 1){
    //             mBase.rotate(90);
    //           }else{
    //             mBase.rotate(-90);
    //           }
    //           autoState ++;
    //           autoTimer.reset(); 
    //         }
    //         break;
    //       case 3:
    //         if (autoTimer.get() < 1){
    //           mBase.velDrive(-0.3, 0);
    //         }else{
    //           if(robotLocation == 1){
    //             mBase.rotate(30);
    //           }else{
    //             mBase.rotate(-30);
    //           }
    //           autoState ++;
    //           autoTimer.reset(); 
    //         }
    //         break;
    //       case 4:
    //         if (autoTimer.get() < 1){
    //           mBase.velDrive(-0.08, 0);
    //         }else{
    //           if(robotLocation == 1){
    //             mBase.rotate(-30);
    //           }else{
    //             mBase.rotate(30);
    //           }
    //           autoState ++;
    //           autoTimer.reset(); 
    //         }
    //         break;
    //       case 5:
    //         if (autoTimer.get() < 0.4){
    //           mBase.velDrive(-0.2, 0);
    //         }else if(autoTimer.get() < 1.9){
    //           mPanel.setMode(0);
    //           mPanel.run(true);
    //         }else{
    //           autoState ++;
    //           autoTimer.reset(); 
    //         }
    //         break;
    //     }
    //     break;
    // }
   
  }




  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //Compressor control
    
    // if (compressor.getCompressorCurrent() > )

    //Controlling robot through two joysticks
    /*
    //Intaker
      //Intake the cargo
      */

      if (FunctionStick.getRawButton(Buttom.IntakeIn)){
        mIntaker.intakeCargo();
      }
      

        //Controlling the angle of intake, having 3 different angles
      if(intakeManualMode){
        intakeAxis = -FunctionStick.getRawAxis(1)*4096*1.35;
        mIntaker.intakeAngle(intakeAxis>=0?intakeAxis:-intakeAxis);
      }else{
        switch(FunctionStick.getPOV()){
          case 0:
            mIntaker.intakePOVAngle(constant.intakerAngleRocket2);
            break;
          case 90:
            mIntaker.intakePOVAngle(constant.intakerAngleRocket1);
            break;
          case 180:
            mIntaker.intakePOVAngle(constant.intakerAngleMid);
            break;
          case 270:
            mIntaker.intakePOVAngle(constant.intakerAngleShip);
            break;
        }
        mIntaker.intakeAdjustAngle(FunctionStick.getRawAxis(1));
      }
      
      //Next line is just for testing purpose
        //mIntaker.intakeAngle(-(FunctionStick.getRawAxis(1)*4096*2));
        //Emit the cargo by either High Speed or Low Speed
        
      if (FunctionStick.getRawButton(Buttom.IntakeOutHighSpeed)){
        mIntaker.emitCargo(true);
      }else if(FunctionStick.getRawButton(Buttom.IntakeOut)){
        mIntaker.emitCargo(false);
      }

      if(FunctionStick.getRawButton(Buttom.fetchOut)){
        mIntaker.fetchMotorOut();
      }

        //Return the cargo to its  initial state if No Cargo inside
      if (FunctionStick.getRawButton(Buttom.intakeDisabe)){
        mIntaker.allDisable();
        intakeZeroTimer.reset();
        intakeZeroTimer.start();
      }


    if(FunctionStick.getRawButton(Buttom.IntakeZeroCal)){
      mIntaker.zeroCalibrate();
    }
    if(intakeManualReleased && FunctionStick.getRawButton(Buttom.intakeManual)){
      intakeManualMode = !intakeManualMode;
      intakeManualReleased = false;
    }
    if(!FunctionStick.getRawButton(Buttom.intakeManual)){
      intakeManualReleased = true;
    }

    
    //panel
    mPanel.run(FunctionStick.getRawButton(Buttom.panelOperation));


    //climb
    
    
    if (MoveStick.getRawButton(Buttom.climbUpOne) && MoveStick.getRawButton(Buttom.climbUpTwo)){
      mClimb.climbUp();
    }

    
    
    mClimb.climbDown(MoveStick.getRawButton(Buttom.climbDown));
    
    

    mClimb.climbDrive(MoveStick.getRawButton(Buttom.climbMove));

    //base
      //Controlling the moving speed, having 2 different Velocity Control
        //HighSpeed Mode now deleted

    if(MoveStick.getRawButton(Buttom.BaseLowSpeed) ){
        //LowSpeed Mode
        constant.velocityControl = 0.2;
        speedMode = 2;
    }else{
        //NormalSpeed Mode
        constant.velocityControl = 0.68;
        speedMode = 0;
    }
     // reverse the direction of joystick on controlling movement
        //requires long period of pressing the button
    if (MoveStick.getRawButton(Buttom.change)){
      directionSign = - 1;
     // mBase.rotate(180f);
    }else{
      directionSign = 1;
    }
    if(MoveStick.getPOV()!=0){
      mBase.rotate(MoveStick.getPOV());
    }
  
      // basic drive
    mBase.velDrive(directionSign*MoveStick.getRawAxis(1), MoveStick.getRawAxis(2));

    if(MoveStick.getRawButton(Buttom.resetBasePos)){
      mBase.resetPos();
    }

    //disply
    //mBase.DisplyInfo();
    mIntaker.DisplyInfo();
    mClimb.DisplyInfo();
    mPanel.DisplyInfo();
    // SmartDashboard.putNumber("compressorCurrent", compressor.getCompressorCurrent());
    //SmartDashboard.putNumber("getRawAxis", MoveStick.getPOV());
    SmartDashboard.putBoolean("IntakeAngleMode", intakeManualMode);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
