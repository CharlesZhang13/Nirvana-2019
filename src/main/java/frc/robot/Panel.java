/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The class used for grabing or giving the panel
 */

        //below is the process given initially
            // to take the panel
                
                //push the back Solenoid
                    // Do not push it all through, find a intermidite spot.
                
                // open the forward Solenoid to catch the panel

                //release the back Solenoid BUT NOT THE FRONT PANEL
            
            // to give the panel

                //push the back Solenoid
                    // just find a spot where the hatch can slightly attach to the rocket/cargo ship

                //releas the front Solenoid

                //further push the back Solenoid
                    // give panel a force to attach to the ship/rocket

public class Panel {
    //construct Solenoid instant
    DoubleSolenoid FrontSolenoid = new DoubleSolenoid(pin.FPanelSolenoidForwardID, pin.FPanelSolenoidReverseID);
    DoubleSolenoid BackSolenoid = new DoubleSolenoid(pin.BPanelSolenoidForwardID, pin.BPanelSolenoidReverseID);

    private boolean panelRunning = false;
    public boolean mode = false;        //true:take ; false:give
    private int panelTimer = 0;      //count number of cycles of teleopPeriodic
    private boolean isReleased = true;      //ensure the button is released before pressing again

    private final int backSolPushTime = 20;         //time requires for the back double solenoid to push completely - not calibrated
    private final int frontSolOpenTime = 10;         //time requires for the front double solenoid to push completely - not calibrated
   
    private final int intervalOne = backSolPushTime;        //time interval #1, push out the back double solenoid
    private final int intervalTwo = intervalOne + frontSolOpenTime;         //time interval #2, open or close the front double solenoid
    private final int intervalThree = intervalTwo + backSolPushTime;        //time interval #3, pull in the back double solenoid

    
    public Panel(){
        //constructor
            //turn off all solenoids
        FrontSolenoid.set(DoubleSolenoid.Value.kForward);
        BackSolenoid.set(DoubleSolenoid.Value.kOff);
    }

    public void panelInit(){
        FrontSolenoid.set(DoubleSolenoid.Value.kForward);
        BackSolenoid.set(DoubleSolenoid.Value.kOff);
        setMode(1);
    }

    public void setMode(int modeType){
        //set the mode of panel action
            //0:take ; 1:give
        switch(modeType){
            case 0:
                mode = true;
            case 1:
                mode = false;
        }
    }

    public void run(boolean isPressed){
        //main process of taking or giving the panel
        if ( (!panelRunning) && isReleased && isPressed){
            //start running, cannnot start again while running or button not released
            panelRunning = true;
            isReleased = false;
        }
        if((!panelRunning) && (!isPressed)){
            //note the button is released
            isReleased = true;
        }
        if(panelRunning){
            //main process, use the number of cycles of periodic to control
            panelTimer++;       //add one each cycle
            if (panelTimer < intervalOne){
                //push out the back double solenoid
                BackSolenoid.set(DoubleSolenoid.Value.kForward);
            }
            else if(panelTimer < intervalTwo){
                //turn off the back double solenoid
                BackSolenoid.set(DoubleSolenoid.Value.kOff);
                //open or close the front double solenoid
                FrontSolenoid.set(DoubleSolenoid.Value.kOff);
                if (mode){
                    FrontSolenoid.set(DoubleSolenoid.Value.kForward);
                }
                else{
                    FrontSolenoid.set(DoubleSolenoid.Value.kReverse);
                }
            }
            else if(panelTimer < intervalThree){
                //pull in the back double solenoid
                BackSolenoid.set(DoubleSolenoid.Value.kReverse);
                /*
                if(!mode){
                    //turn off the front double solenoid when giving
                    FrontSolenoid.set(DoubleSolenoid.Value.kOff);
                }
                */
            }
            else{
                //reintialize all variable and stop running
                BackSolenoid.set(DoubleSolenoid.Value.kOff);
                panelTimer = 0;
                mode = !mode;
                panelRunning = false;
            }
            
            }
        }
    
    public void DisplyInfo(){
        SmartDashboard.putBoolean("panel Mode, true:take", mode);
    }
        
    }

