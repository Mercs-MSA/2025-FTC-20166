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
    public enum location {
        FRONT,
        BACK,
        TEST
    }
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
    public double leftMaxAngleSetting = 0.2;
    public double rightMaxAngleSetting = 0.8;
    public double leftMinAngleSetting = 0.9;
    public double rightMinAngleSetting = 0.1;
    public double shooterMaxAngle = 84;
    private double robotAShooterMaxAngle = 84;
    private double robotCShooterMaxAngle = 84;
    public double shooterMinAngle = 41;
    private double robotAShooterMinAngle = 41;
    private double robotCShooterMinAngle = 41;
    public double nearShooterAngle = 70;
    private double robotANearShooterAngle = 70;
    private double robotCNearShooterAngle = 70;
    public double nearVelocity = 1020;
    private double robotANearVelocity = 1020;
    private double robotCNearVelocity = 1020;
    public double nearDistance = 30; //In inches
    private double robotANearDistance = 30;
    private double robotCNearDistance = 30;
    public double farShooterAngle = 50;
    private double robotAfarShooterAngle = 50;
    private double robotCFarShooterAngle = 50;
    public double farVelocity = 1670;
    private double robotAFarVelocity = 1710;
    private double robotCFarVelocity = 1680;
    public double farDistance = 165; //In inches
    private double robotAFarDistance = 165;
    private double robotCFarDistance = 165;
    private double robotCAgitator = -1;
    private double robotAAgitator = 1;
    public double agitator;

    //liftArm angles
    public static double robotCliftArmDownAngle = 0.386;
    public static double robotCliftArmUpAngle = 0.7;
    public static double robotAliftArmDownAngle = 0.4;
    public static double robotAliftArmUpAngle = 0.75;
    public static double robotBliftArmDownAngle = 0.4;
    public static double robotBliftArmUpAngle = 0.7;
    public double gateUpAngle = 0.2;
    public double gateDownAngle = 0.5;
    public double liftArmUpAngle = 0.7;
    public double liftArmDownAngle = 0.4;


    public GoBildaPinpointDriver.EncoderDirection encoderForward;
    public GoBildaPinpointDriver.EncoderDirection encoderStrafe;
    public DcMotorSimple.Direction leftSideDirection;
    public DcMotorSimple.Direction rightSideDirection;

    //pinpoint offsets
    public double podX = 0;
    public double podY = 0;
    private double robotAPodYOffset = -6.25;
    private double robotAPodXOffset = -6.5;
    private double robotBPodYOffset = -7.8;//45mm in front of the center
    private double robotBPodXOffset = 2.65;//220mm to the right of the center
    private double robotCPodXOffset = 0;
    private double robotCPodYOffset = -7.5;




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
            agitator = robotAAgitator;
            shooterMaxAngle = robotAShooterMaxAngle;
            shooterMinAngle = robotAShooterMinAngle;
            nearShooterAngle = robotANearShooterAngle;
            nearDistance = robotANearDistance;
            nearVelocity = robotANearVelocity;
            farShooterAngle = robotAfarShooterAngle;
            farDistance = robotAFarDistance;
            farVelocity = robotAFarVelocity;
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
            encoderForward = GoBildaPinpointDriver.EncoderDirection.FORWARD;
            encoderStrafe = GoBildaPinpointDriver.EncoderDirection.REVERSED;
            leftSideDirection = DcMotorSimple.Direction.FORWARD;
            rightSideDirection = DcMotorSimple.Direction.REVERSE;
            agitator = robotCAgitator;
            shooterMaxAngle = robotCShooterMaxAngle;
            shooterMinAngle = robotCShooterMinAngle;
            nearShooterAngle = robotCNearShooterAngle;
            nearDistance = robotCNearDistance;
            nearVelocity = robotCNearVelocity;
            farShooterAngle = robotCFarShooterAngle;
            farDistance = robotCFarDistance;
            farVelocity = robotCFarVelocity;
        }
    }
}
