/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 * The class used for climbing the stages by using pneumatic system
 */
public class Climb {
    //construct Solenoid instant
    
    DoubleSolenoid pulleySolenoid= new DoubleSolenoid(pin.PClimbSolenoidForwardID, pin.PClimbSolemoidReverseID);
    DoubleSolenoid motorSolenoid = new DoubleSolenoid(pin.MClimbSolenoidForwardID, pin.MClimbSolenoidReverseID);
    
    TalonSRX climbMotor = new TalonSRX(pin.ClimbTalonID);
    private final double climbVelocity = 0.2;// * constant.VCoefficient;//climb velocity is too fast, make it as small as possible
    private Boolean wasReleased = true;
    private Boolean climbDownMode = true;       //true : pulley up ; false : motor up
    private Boolean isClimbing = false;

    public Climb(){
        //constructor
        
        pulleySolenoid.set(DoubleSolenoid.Value.kReverse);
        motorSolenoid.set(DoubleSolenoid.Value.kReverse);
        
        climbMotor.setInverted(true);
        //constant.velocityControl = 0.05;

        //talon start up programme, inclding PID config
        /*
        TalonSRXInit(climbMotor);
        configPID(climbMotor, 0.1097, 0, 0, 0);
        */
    }

    
    public void climbUp (){
        //Solenoid raise
        pulleySolenoid.set(DoubleSolenoid.Value.kOff);
        motorSolenoid.set(DoubleSolenoid.Value.kOff);
        pulleySolenoid.set(DoubleSolenoid.Value.kForward);
        motorSolenoid.set(DoubleSolenoid.Value.kForward);
        isClimbing = true;
    }
    
    

    public void climbDrive(Boolean isDrive){
        //Drive forward while climbing
        //if(isClimbing){
            climbMotor.set(ControlMode.PercentOutput, isDrive?climbVelocity:0.0);
        //}
    }

    
    public void climbDown (Boolean buttonPressed){
        //retract the solenoids when finishing climbing
        if(wasReleased && buttonPressed){
            wasReleased = false;
            if(climbDownMode){
                pulleySolenoid.set(DoubleSolenoid.Value.kOff);
                pulleySolenoid.set(DoubleSolenoid.Value.kReverse);
                climbDownMode = false;
            }else{
                motorSolenoid.set(DoubleSolenoid.Value.kOff);
                motorSolenoid.set(DoubleSolenoid.Value.kReverse);
                climbDownMode = true;
                isClimbing = false;
            }
        }
        if((!wasReleased) && (!buttonPressed)){
            wasReleased = true;
        }
    }
    

    public void DisplyInfo(){
        //Disply information on driverstation
        SmartDashboard.putBoolean("climbDownMode: ", climbDownMode);
    }
    
    
    /*
    private void TalonSRXInit(TalonSRX _talon) {
        //set up TalonSRX and closed loop
        
		_talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
				constant.kPIDLoopIdx,constant.kTimeoutMs);

		_talon.setSensorPhase(true);

		_talon.configNominalOutputForward(0, constant.kTimeoutMs);
		_talon.configNominalOutputReverse(0, constant.kTimeoutMs);
		_talon.configPeakOutputForward(1, constant.kTimeoutMs);
		_talon.configPeakOutputReverse(-1, constant.kTimeoutMs);
    }

    private void configPID(TalonSRX _talon,double kF, double kP,double kI, double kD) {
		_talon.config_kF(constant.kPIDLoopIdx, kF, constant.kTimeoutMs);
		_talon.config_kP(constant.kPIDLoopIdx, kP, constant.kTimeoutMs);
		_talon.config_kI(constant.kPIDLoopIdx, kI, constant.kTimeoutMs);
		_talon.config_kD(constant.kPIDLoopIdx, kD, constant.kTimeoutMs);

    }
    */
    
}
