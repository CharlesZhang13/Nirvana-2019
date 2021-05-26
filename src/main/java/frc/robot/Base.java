/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The class which controls the behaviors of base.
 */

public class Base implements PIDOutput{

    TalonSRX RTalon = new TalonSRX(pin.RTalonID);
    TalonSRX LTalon = new TalonSRX(pin.LTalonID);
    VictorSPX LVictor = new VictorSPX(pin.LVictorID);
    VictorSPX RVictor = new VictorSPX(pin.RVictorID);
    AHRS ahrs = new AHRS(SPI.Port.kMXP);
    PIDController turnController = new PIDController(0.03, 0.0, 0.0, 0.0, ahrs, this);
    double rotateToAngle;
    double targetAngle;
    double rotateError= 0.0;
    double rotateTimeOutSec=0.0;
    double rotateSpeed = 0.0;

    Timer rotateTimer = new Timer();

    // constants
    private static final double kToleranceDegrees = 2.0f;

    
    public Base(){
        //Constructor of Class Base
            //initialises all talon and victor on Base
        TalonSRXInit(RTalon);
        TalonSRXInit(LTalon);

        LVictor.setInverted(true);
        LTalon.setInverted(true);
        RTalon.setInverted(false);
        RVictor.setInverted(false);

        configPID(LTalon, 0.1097, 0.22, 0, 0, 0.4);
        configPID(RTalon, 0.1097, 0.22, 0, 0, 0.4);
        
        LVictor.follow(LTalon);
        RVictor.follow(RTalon);

        resetAll();
    }


    public void velDrive(double forward, double turn){
        /*
        controlling basic movement of base
        */

        double LTempV = (forward-turn*1.3)
                        *constant.VCoefficient*constant.velocityControl;
        double RTempV = (forward+turn*1.3)
                        *constant.VCoefficient*constant.velocityControl;

        LTalon.set(ControlMode.Velocity, LTempV);
        RTalon.set(ControlMode.Velocity, RTempV);

    }

    
    public void rotate(double angle){
        //rotate base to target angle
            //currently VERY inaccurate3
        if(angle>0.1){
            angle = (angle>=0)?(angle%360.0):(360.0-((-angle)%360.0));
            rotateTimer.reset();
            rotateTimer.start();
            if(angle<=180.0){
                rotateTimeOutSec = (angle/180.0)* 3.0;
                targetAngle = ahrs.getAngle() + angle;
                while (ahrs.getAngle() < targetAngle && rotateTimer.get() < rotateTimeOutSec){
                    rotateError = targetAngle - ahrs.getAngle();
                    rotateSpeed = rotateError / 135.0;
                    velDrive(0.0, rotateSpeed < 0.3 ? 0.3 : rotateSpeed);
                }
            }
            else{
                rotateTimeOutSec = ((360.0-angle)/180.0)* 3.0;
                targetAngle = ahrs.getAngle() - (360.0-angle);
                while (ahrs.getAngle() > targetAngle && rotateTimer.get() < rotateTimeOutSec){
                    rotateError = targetAngle - ahrs.getAngle();
                    rotateSpeed = rotateError / 135.0;
                    velDrive(0.0, rotateSpeed > -0.3 ? -0.3 : rotateSpeed);
                    SmartDashboard.putNumber("rotateError", rotateError);
                } 
            }
            rotateTimer.stop();
            velDrive(0.0, 0.0);
        }
    }
    
/*
    public void changeDirection(boolean changeFlag){
        if (changeFlag){
            RTalon.setInverted(true);
            RVictor.setInverted(true);
            LTalon.setInverted(false);
            RTalon.setInverted(false);
        }else{
            LVictor.setInverted(true);
            LTalon.setInverted(true);
            RTalon.setInverted(false);
            RVictor.setInverted(false);
            constant.reverse = false;
        }

    }
*/

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

    private void configPID(TalonSRX _talon,double kF, double kP,double kI, double kD, double kRamp) {
		_talon.config_kF(constant.kPIDLoopIdx, kF, constant.kTimeoutMs);
		_talon.config_kP(constant.kPIDLoopIdx, kP, constant.kTimeoutMs);
		_talon.config_kI(constant.kPIDLoopIdx, kI, constant.kTimeoutMs);
        _talon.config_kD(constant.kPIDLoopIdx, kD, constant.kTimeoutMs);
        _talon.configClosedloopRamp(kRamp, constant.kTimeoutMs);

    }

    public void DisplyInfo(){
        //Disply information on driverstation
        /*
        SmartDashboard.putNumber("Left velocity", LTalon.getSelectedSensorVelocity(constant.kPIDLoopIdx));
        SmartDashboard.putNumber("Left output", LTalon.getMotorOutputPercent());
        SmartDashboard.putNumber("Right velocity", RTalon.getSelectedSensorVelocity(constant.kPIDLoopIdx));
        SmartDashboard.putNumber("Right output", RTalon.getMotorOutputPercent());
        */
        SmartDashboard.putNumber("LPosition", LTalon.getSelectedSensorPosition());
        SmartDashboard.putNumber("RPosition", RTalon.getSelectedSensorPosition());
        SmartDashboard.putNumber("ahrsXPosition", ahrs.getDisplacementX());
    }

    public void resetPos(){
        LTalon.setSelectedSensorPosition(0);
        RTalon.setSelectedSensorPosition(0);
        ahrs.resetDisplacement();
    }

    public void resetAll(){
        //reset ahrs
        ahrs.reset();
        ahrs.resetDisplacement();
    }

    @Override
    public void pidWrite (double output){
        rotateToAngle = output;
    }
/*
    @Override
	public void pidWrite(double output) {
	}
	
	@Override
	public double pidGet() {
		return 0.0;
	}
    
    @Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kRate;
	}
	
	@Override
    public void setPIDSourceType(PIDSourceType pidSource) {	}
    */
}


