package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants
{
    public final static double errorDeadZone = 5.0;
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
