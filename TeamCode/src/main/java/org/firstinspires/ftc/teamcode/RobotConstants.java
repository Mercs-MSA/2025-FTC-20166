package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Utilities.LERP;

@Config
public class RobotConstants
{
    public final static double headingErrorDeadZone = 5.0;
    public static double headingPFactor = (2.0 / 90.0);
    public final static double joystickRotateDeadband = 0.05;
    public final static double robotAPodYOffset = 0;
    public final static double robotAPodXOffset = 0;
//    public final static double robotBPodYOffset = -19.5/2.54;
    public final static double robotBPodYOffset = 55;//45mm in front of the center
//    public final static double robotBPodXOffset = 7.5/2.54;
    public final static double robotBPodXOffset = -185;//220mm to the right of the center

    public static int robotId;
    public static double distanceFromGoal = 0;
    public static double potVoltage = .5;
    public static double potVoltageMin = .5;
    public static double potVoltageMax = 3;
    public static double turretAngleMin = -180;
    public static double turretAngleMax = 180;
    public static double servoAngle = 0;
    public static double leftMaxAngleSetting = 0.9;
    public static double rightMaxAngleSetting = 0.1;
    public static double leftMinAngleSetting = 0.1;
    public static double rightMinAngleSetting = 0.9;
    public static double shooterMaxAngle = 90;
    public static double shooterMinAngle = 30;
    public static double nearShooterAngle = 80;
    public static double nearVelocity = 1000;
    public static double nearDistance = 6; //In inches
    public static double farShooterAngle = 50;
    public static double farVelocity = 1650;
    public static double farDistance = 148; //In inches
    public static double shooterVelocity = 300;
    public static double shooterAngle = 45;
    public static void setRobotID(int robotID)
    {
        if (robotID == 0)
        {

        }
        else if (robotID == 1)
        {

        }
    }
}
