package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants
{
    public final static double headingErrorDeadZone = 5.0;
    public final static double headingPFactor = (1.0 / 90.0);
    public final static double joystickRotateDeadband = 0.05;
    public final static double robotAPodYOffset = 0;
    public final static double robotAPodXOffset = 0;
//    public final static double robotBPodYOffset = -19.5/2.54;
    public final static double robotBPodYOffset = 55;//45mm in front of the center
//    public final static double robotBPodXOffset = 7.5/2.54;
    public final static double robotBPodXOffset = -185;//220mm to the right of the center

    public static int robotId;
    public static double servoAngle = 0;
    public static double leftMaxAngleSetting = 0.9;
    public static double rightMaxAngleSetting = 0.1;
    public static double leftMinAngleSetting = 0.1;
    public static double rightMinAngleSetting = 0.9;
    public static double shooterMaxAngle = 90;
    public static double shooterMinAngle = 30;
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
