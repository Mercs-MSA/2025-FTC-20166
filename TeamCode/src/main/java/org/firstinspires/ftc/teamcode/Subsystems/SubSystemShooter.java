package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Utilities.LERP;
import org.firstinspires.ftc.teamcode.Utilities.RobotStatus;

public class SubSystemShooter {
    private LERP shooterTiltLeftLERP;
    private LERP shooterTiltRightLERP;
    private LERP turretInterpolator1;
    private LERP turretInterpolator2;
    private Servo shooterTiltLeft;
    private Servo shooterTiltRight;
    private DcMotorEx shooterFlyWheel;
    private CRServo turretRotation;
    private CRServo transferServo1;
    private CRServo transferServo2;
    private AnalogInput turretPositionSensor;

    public SubSystemShooter(HardwareMap hardwareMap) throws InterruptedException {
        shooterTiltLeftLERP = new LERP(RobotConstants.shooterMinAngle,RobotConstants.leftMinAngleSetting,RobotConstants.shooterMaxAngle,RobotConstants.leftMaxAngleSetting,true);
        shooterTiltRightLERP = new LERP(RobotConstants.shooterMinAngle,RobotConstants.rightMinAngleSetting,RobotConstants.shooterMaxAngle,RobotConstants.rightMaxAngleSetting,true);
        turretInterpolator1 = new LERP(1.13,0,2.512,-180,true);
        turretInterpolator2 = new LERP(.327,180,1.13,0,true);

        shooterTiltLeft = hardwareMap.get(Servo.class, "shooterTiltLeft");//shooterTiltLeft
        shooterTiltRight = hardwareMap.get(Servo.class, "shooterTiltRight");//shooterTiltRight

        transferServo1 = hardwareMap.get(CRServo.class, "transfer1");
        transferServo2 = hardwareMap.get(CRServo.class, "transfer2");

        turretRotation = hardwareMap.get(CRServo.class, "turretRotation");

        shooterFlyWheel = hardwareMap.get(DcMotorEx.class, "shooterFlyWheel");

        turretPositionSensor = hardwareMap.get(AnalogInput.class, "turretPositionSensor");

    }

    public void setTurretRotationSpeed(double power)
    {
        turretRotation.setPower(power);
    }

    //Edited in GVIM by Steve :)
    private double [][] turretSensorCorrectionTable =
            {
//                    { -180,  -135,   -90,  -45,     0,    45,    90,   135,   180}, //Turret angle
//                    {2.465, 1.984, 1.615, 1.34, 1.141, 0.915, 0.721, 0.536, 0.308}, //ROBOT 0

                    { -180,  -135,   -90,   -45,     0,    45,    90,   135,   180},
                    {2.553, 2.032, 1.671, 1.379, 1.135, 0.931, 0.743, 0.556, 0.347},
                    //Measured voltage. MUST be ordered high to low. Need to update these to 'real life'
            };//Should move to robot constants since may be different for each robot

    public double interpolateTurretAngle(double Voltage)
    {
        int tableWidth = turretSensorCorrectionTable[0].length;//Number of columns in the array. (Technically the number of columns in the first row since Java can have variable sized rows)
        double lowerA = 0; //Declare here so lifetime lasts to the end of the method
        double lowerV; //Declare here so lifetime lasts to the end of the method

        for (int i = 0; i < tableWidth; i++)
        {
            lowerA = turretSensorCorrectionTable[0][i];//Get the parameters from the table and use as the segment lower voltage points
            lowerV = turretSensorCorrectionTable[1][i];
            if (Voltage >= lowerV)
            {
                if (i == 0)//Voltage is higher than the highest entry in the table so maxed out
                {
                    return lowerA;
                }
                else
                {
                    double upperA = turretSensorCorrectionTable[0][i - 1];//Get the parameters for the previous table entry which is the higher voltage segment
                    double upperV = turretSensorCorrectionTable[1][i - 1];
                    LERP interpolator = new LERP(lowerV, lowerA, upperV, upperA, true);//Create a LERP to interpolate between the lower voltage and upper voltage
                    return interpolator.interpolated(Voltage);//Actually interpolate and return the calculated voltage
                }
            }
        }
        //If we make it here then the voltage is less than the lowest voltage entry in the table
        return lowerA;
    }
    public double getTurretAngle()
    {
        return interpolateTurretAngle(turretPositionSensor.getVoltage());
        /*
        double volt = turretPositionSensor.getVoltage();
        double mid = 1.13; //(2.512 + .327) / 2;
        double angle;
        if (volt > mid)
        {
            return turretInterpolator1.interpolated(volt);
        } else
        {
            return turretInterpolator2.interpolated(volt);
        }
         */
    }
    public double getPotVoltage()
    {
        return turretPositionSensor.getVoltage();
    }
    public void setShooterAngle(double tiltAngle)
    {
        shooterTiltLeft.setPosition(shooterTiltLeftLERP.interpolated(tiltAngle));
        shooterTiltRight.setPosition(shooterTiltRightLERP.interpolated(tiltAngle));
    }

    public void setShooterSpeed(double velocity)
    {
        shooterFlyWheel.setVelocity(velocity);
    }
    public void setTransfer(boolean enabled)
    {
        if (enabled)
        {
            transferServo1.setPower(-RobotConstants.turretMaxPower);
            transferServo2.setPower(RobotConstants.turretMaxPower);
        }
        else
        {
            transferServo1.setPower(0);
            transferServo2.setPower(0);
        }
    }
    public double getShooterSpeed()
    {
        return shooterFlyWheel.getVelocity();
    }

}
