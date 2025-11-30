package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utilities.LERP;

@Config
public class RobotConstants
{
    public final static double headingErrorDeadZone = 5.0;
    public enum alliance {
        BLUE,
        RED
    };
    public static double headingPFactor = (2.0 / 90.0);
    public final static double joystickRotateDeadband = 0.05;
    public static int robotId;
    public static double distanceFromGoal = 0;
    public static double potVoltage = .5;
    public static double potVoltageMin = .352;
    public static double potVoltageMax = 2.522;
    public static double turretAngleMin = -180;
    public static double turretAngleMax = 180;
    public static double turretMaxPower = 1;
    public static double turretRotationP = 0.02;
    public static double servoAngle = 0;
    public static double leftMaxAngleSetting = 0.2;
    public static double rightMaxAngleSetting = 0.8;
    public static double leftMinAngleSetting = 0.9;
    public static double rightMinAngleSetting = 0.1;
    public static double shooterMaxAngle = 84;
    public static double shooterMinAngle = 41;
    public static double nearShooterAngle = 80;
    public static double nearVelocity = 1000;
    public static double nearDistance = 6; //In inches
    public static double farShooterAngle = 50;


    //liftArm angles
    public static double robotCliftArmDownAngle = 0.386;
    public static double robotCliftArmUpAngle = 0.7;
    public static double robotAliftArmDownAngle = 0.4;
    public static double robotAliftArmUpAngle = 0.7;
    public static double robotBliftArmDownAngle = 0.4;
    public static double robotBliftArmUpAngle = 0.7;
    public static double liftArmUpAngle = 0.7;
    public static double liftArmDownAngle = 0.4;

    public static double farVelocity = 1650;
    public static double farDistance = 148; //In inches
    public static double shooterVelocity = 300;
    public static double shooterAngle = 45;



    public GoBildaPinpointDriver.EncoderDirection encoderForward;
    public GoBildaPinpointDriver.EncoderDirection encoderStrafe;
    public DcMotorSimple.Direction leftSideDirection;
    public DcMotorSimple.Direction rightSideDirection;

    //pinpoint offsets
    public double podX = 0;
    public double podY = 0;
    private static double robotAPodYOffset = -6.25;
    private static double robotAPodXOffset = -6.5;
    private static double robotBPodYOffset = -7.8;//45mm in front of the center
    private static double robotBPodXOffset = 2.65;//220mm to the right of the center
    private static double robotCPodXOffset = -7.5;
    private static double robotCPodYOffset = -7.5;




    public RobotConstants(int robotID)
    {
        if (robotID == 0)
        {
            podX = robotAPodXOffset;
            podY = robotAPodYOffset;
            liftArmDownAngle = robotAliftArmDownAngle;
            liftArmUpAngle = robotAliftArmUpAngle;
            encoderForward = GoBildaPinpointDriver.EncoderDirection.FORWARD;
            encoderStrafe = GoBildaPinpointDriver.EncoderDirection.FORWARD;
            leftSideDirection = DcMotorSimple.Direction.FORWARD;
            rightSideDirection = DcMotorSimple.Direction.FORWARD;
        }
        else if (robotID == 1)
        {
            podX = robotBPodXOffset;
            podY = robotBPodYOffset;
            liftArmDownAngle = robotBliftArmDownAngle;
            liftArmUpAngle = robotBliftArmUpAngle;
            encoderForward = GoBildaPinpointDriver.EncoderDirection.FORWARD;
            encoderStrafe = GoBildaPinpointDriver.EncoderDirection.REVERSED;
            leftSideDirection = DcMotorSimple.Direction.FORWARD;
            rightSideDirection = DcMotorSimple.Direction.REVERSE;
        }
        else if (robotID == 2)
        {
            podX = robotCPodXOffset;
            podY = robotCPodYOffset;
            liftArmDownAngle = robotCliftArmDownAngle;
            liftArmUpAngle = robotCliftArmUpAngle;
            encoderForward = GoBildaPinpointDriver.EncoderDirection.REVERSED;
            encoderStrafe = GoBildaPinpointDriver.EncoderDirection.REVERSED;
            leftSideDirection = DcMotorSimple.Direction.FORWARD;
            rightSideDirection = DcMotorSimple.Direction.REVERSE;
        }
    }
}
