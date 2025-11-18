package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Utilities.LERP;

public class SubSystemShooter {
    private LERP shooterTiltLeftLERP;
    private LERP shooterTiltRightLERP;
    private LERP turretInterpolator;
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
        turretInterpolator = new LERP(RobotConstants.potVoltageMin,RobotConstants.turretAngleMin,RobotConstants.potVoltageMax,RobotConstants.turretAngleMax,true);

        shooterTiltLeft = hardwareMap.get(Servo.class, "shooterTiltLeft");
        shooterTiltRight = hardwareMap.get(Servo.class, "shooterTiltRight");

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
        RobotConstants.potVoltage = turretPositionSensor.getVoltage();
        return turretInterpolator.interpolated(RobotConstants.potVoltage);
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
            transferServo1.setPower(-1);
            transferServo2.setPower(1);
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
