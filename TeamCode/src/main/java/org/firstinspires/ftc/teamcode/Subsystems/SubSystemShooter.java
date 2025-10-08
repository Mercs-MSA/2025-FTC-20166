package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Utilities.LERP;

public class SubSystemShooter {
    private LERP shooterTiltLeftLERP;
    private LERP shooterTiltRightLERP;
    private Servo shooterTiltLeft;
    private Servo shooterTiltRight;
    private DcMotorEx shooterFlyWheel;
    private CRServo turretRotation;
    private AnalogInput turretPositionSensor;

    public SubSystemShooter(HardwareMap hardwareMap) throws InterruptedException {
        shooterTiltLeftLERP = new LERP(RobotConstants.shooterMinAngle,RobotConstants.leftMinAngleSetting,RobotConstants.shooterMaxAngle,RobotConstants.leftMaxAngleSetting,true);
        shooterTiltRightLERP = new LERP(RobotConstants.shooterMinAngle,RobotConstants.rightMinAngleSetting,RobotConstants.shooterMaxAngle,RobotConstants.rightMaxAngleSetting,true);

        shooterTiltLeft = hardwareMap.get(Servo.class, "shooterTiltLeft");
        shooterTiltRight = hardwareMap.get(Servo.class, "shooterTiltRight");

        turretRotation = hardwareMap.get(CRServo.class, "turretRotation");

        shooterFlyWheel = hardwareMap.get(DcMotorEx.class, "shooterFlyWheel");

        turretPositionSensor = hardwareMap.get(AnalogInput.class, "turretPositionSensor");

    }

    public void shooterSetAngle(double tiltAngle)
    {
        shooterTiltLeft.setPosition(shooterTiltLeftLERP.interpolated(tiltAngle));
        shooterTiltRight.setPosition(shooterTiltRightLERP.interpolated(tiltAngle));
    }
}
