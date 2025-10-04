package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.RobotConstants;

public class SubSystemShooter {

    private Servo shooterTiltLeft;
    private Servo shooterTiltRight;
    private DcMotorEx shooterFlyWheel;
    private Servo turretRotation;
    private AnalogInput turretPositionSensor;

    public SubSystemShooter(HardwareMap hardwareMap) throws InterruptedException {
        shooterTiltLeft = hardwareMap.get(Servo.class, "shooterTiltLeft");
        shooterTiltRight = hardwareMap.get(Servo.class, "shooterTiltRight");

        turretRotation = hardwareMap.get(Servo.class, "turretRotation");

        shooterFlyWheel = hardwareMap.get(DcMotorEx.class, "shooterFlyWheel");

        turretPositionSensor = hardwareMap.get(AnalogInput.class, "turretPositionSensor");

    }

    public void shooterSetAngle(double tiltAngle)
    {

    }
    }
