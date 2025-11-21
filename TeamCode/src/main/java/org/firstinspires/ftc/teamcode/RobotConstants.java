package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

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
    public static double turretMaxPower = .5;
    public static double turretRotationP = 0.01;
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
    public static double farVelocity = 1650;
    public static double farDistance = 148; //In inches
    public static double shooterVelocity = 300;
    public static double shooterAngle = 45;
    public static double podX = 0;
    public static double podY = 0;
    private static double robotAPodYOffset = 0;
    private static double robotAPodXOffset = 0;
    //    public final static double robotBPodYOffset = -19.5/2.54;
    private static double robotBPodYOffset = -7.8;//45mm in front of the center
    //    public final static double robotBPodXOffset = 7.5/2.54;
    private static double robotBPodXOffset = 2.65;//220mm to the right of the center

    public static void setRobotID(int robotID)
    {
        if (robotID == 0)
        {
            podX = robotAPodXOffset;
            podY = robotAPodYOffset;
        }
        else if (robotID == 1)
        {
            podX = robotBPodXOffset;
            podY = robotBPodYOffset;
        }
        else if (robotID == 2)
        {

        }
    }
}
