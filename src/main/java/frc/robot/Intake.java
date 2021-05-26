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
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The class used for intaking and shooting the balls
 */

public class Intake {

    // constants
    private final static double HighSpeedOut = 0.8;
    private final static double LowSpeedOut = 0.6;
    private final static double FetchSpeed = 0.5;
    private final static double SpeedIn = 0.5;
    private final static int kCurrentLimit = 8;
    private final static int kCurrentLimitDuration=10;
    private static double povAngle = 0;
    private static double errTolerance = 0.5;
    
    // motor control
    private VictorSPX FetchMoter = new VictorSPX(pin.FetchVictorID);
    private VictorSPX IntakerMotor = new VictorSPX(pin.IntakerVictorID);
    private TalonSRX IntakerAngleMoter = new TalonSRX(pin.IntakerAngleMoterTalonID);

    // sensor
        //Due to the fact that first prgrammer spelled sensor as Seneor in every places, DigitalInput IntakerSeneor will maintain its spelling mistake
    //private DigitalInput IntakerSeneor = new DigitalInput(pin.Intaker);

    // flag
    public static boolean hasCargo = false;
    private static boolean isPOV = false;
    private static boolean angleCalibrated = false;

    public Intake(){
        
        //config motor control

        FetchMoter.setInverted(false);
        IntakerMotor.setInverted(false);
        TalonSRXInit(IntakerAngleMoter);
        configPID(IntakerAngleMoter, 0.0, 0.4, 0.0002, 40, 150, 0.5);
        IntakerAngleMoter.setInverted(true);
        IntakerAngleMoter.setSelectedSensorPosition(0);


        // motor safety

        IntakerAngleMoter.enableCurrentLimit(true);
        IntakerAngleMoter.configContinuousCurrentLimit(kCurrentLimit, constant.kTimeoutMs);
        IntakerAngleMoter.configPeakCurrentLimit((int)(kCurrentLimit*1.5), constant.kTimeoutMs);
        IntakerAngleMoter.configPeakCurrentDuration(kCurrentLimitDuration, constant.kTimeoutMs);
    }

    public void intakeCargo(){
        /*
        if (IntakerSeneor.get()){
            FetchMoter.set(ControlMode.PercentOutput, 0);
            IntakerMotor.set(ControlMode.PercentOutput, 0);
            hasCargo = true;
        }
        */
        //intaking cargo into intake
        intakeAngle(constant.intakerIntakeAngle);
        FetchMoter.set(ControlMode.PercentOutput, FetchSpeed);
        IntakerMotor.set(ControlMode.PercentOutput, SpeedIn);
            //if a cargo is already inside intake, stop intaking
    }

    public void allDisable(){
        //Stop emitting cargo and reset intake to its initial angle
        //if (!IntakerSeneor.get()){
            FetchMoter.set(ControlMode.PercentOutput, 0);
            IntakerMotor.set(ControlMode.PercentOutput, 0);
            IntakerAngleMoter.set(ControlMode.PercentOutput, 0);
            //intakeAngle(constant.intakerAngleIntial);
            povAngle = 0.0;
            angleCalibrated = false;
            hasCargo = false;
            isPOV = false;
            IntakerAngleMoter.setSelectedSensorPosition(0);
        //}
            //disply whether a cargo is inside intake onto driverstation
        //SmartDashboard.putBoolean("EMIT cargo STATE", hasCargo);
    }

    public void intakePOVAngle(double povVal){
        povAngle = povVal;
        isPOV = true;
    }

    public void intakeAdjustAngle(double axis){
        if (isPOV){
            intakeAngle(povAngle - axis*4096*0.3);
        }
    }

    public void intakeAngle(double intakeAngle){
        //change angle of intake
        while  (!isAngleCalibrated(intakeAngle)){
            IntakerAngleMoter.set(ControlMode.Position, intakeAngle);
        }
            //tell angle is calibrated, so cargo can be emitted
        angleCalibrated = isAngleCalibrated(intakeAngle);
    }

    public void emitCargo(boolean HighSpeed){
        //emit cargo if angle is calibrated
        if(HighSpeed && angleCalibrated){
            //high speed emission
            IntakerMotor.set(ControlMode.PercentOutput, -HighSpeedOut);
        }else if (angleCalibrated){
            //low speed emission
            IntakerMotor.set(ControlMode.PercentOutput, -LowSpeedOut);
        }else{
            //tell driver angle is not calibrated and cargo cannot be emitted
            SmartDashboard.putBoolean("Angle flag", angleCalibrated);
        }
    }

    public void zeroCalibrate(){
        IntakerAngleMoter.setSelectedSensorPosition(0);
    }

    public void fetchMotorOut(){
        FetchMoter.set(ControlMode.PercentOutput, -FetchSpeed);
    }

    public void DisplyInfo(){
        //intaker angle data
        //SmartDashboard.putNumber("Intaker Angle", IntakerAngleMoter.getSelectedSensorPosition(constant.kPIDLoopIdx));
        //SmartDashboard.putNumber("Intaker Angle", IntakerAngleMoter.getOutputCurrent());
        SmartDashboard.putNumber("Intaker Angle", IntakerAngleMoter.getSelectedSensorPosition());
        //SmartDashboard.putBoolean("Intaker Sensor", IntakerSeneor.get());
        SmartDashboard.putNumber("intaker Speed", IntakerMotor.getMotorOutputPercent());
        SmartDashboard.putNumber("fetch Speed", FetchMoter.getMotorOutputPercent());
        
    }

    private boolean isAngleCalibrated(double targetAngle){
        return Math.abs(IntakerAngleMoter.getSelectedSensorPosition() - targetAngle) < errTolerance;
    }

    private void TalonSRXInit(TalonSRX _talon) {
		//set up TalonSRX and closed loop
		_talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
				constant.kPIDLoopIdx,constant.kTimeoutMs);

		_talon.setSensorPhase(false);

		_talon.configNominalOutputForward(0, constant.kTimeoutMs);
		_talon.configNominalOutputReverse(0, constant.kTimeoutMs);
		_talon.configPeakOutputForward(1, constant.kTimeoutMs);
		_talon.configPeakOutputReverse(-1, constant.kTimeoutMs);

		_talon.setSelectedSensorPosition(0, constant.kPIDLoopIdx, constant.kTimeoutMs);

    }

    private void configPID(TalonSRX _talon,double kF, double kP,double kI, double kD, int kIzone, double kRamp) {
		_talon.config_kF(constant.kPIDLoopIdx, kF, constant.kTimeoutMs);
		_talon.config_kP(constant.kPIDLoopIdx, kP, constant.kTimeoutMs);
		_talon.config_kI(constant.kPIDLoopIdx, kI, constant.kTimeoutMs);
        _talon.config_kD(constant.kPIDLoopIdx, kD, constant.kTimeoutMs);
        _talon.config_IntegralZone(constant.kPIDLoopIdx, kIzone, constant.kTimeoutMs);
        _talon.configClosedloopRamp(kRamp, constant.kTimeoutMs);

    }



}

