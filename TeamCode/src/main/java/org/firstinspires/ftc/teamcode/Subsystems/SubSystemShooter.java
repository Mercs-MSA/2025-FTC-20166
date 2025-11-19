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

    public double getTurretAngle()
    {
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
