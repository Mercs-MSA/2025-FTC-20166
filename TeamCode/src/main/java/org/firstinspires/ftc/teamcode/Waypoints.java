package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

@Config
public class Waypoints
{

    public static double redGoalPointx = 72;
    public static double redGoalPointy = 72;

    public static double blueGoalPointx = -72;
    public static double blueGoalPointy = 72;
    public static SparkFunOTOS.Pose2D startPointMiddleBottom = new SparkFunOTOS.Pose2D(0, -60, Math.toRadians(0));
    public static void setRobotID(int robotID)
    {

    }
}
