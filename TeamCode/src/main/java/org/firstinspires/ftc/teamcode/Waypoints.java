package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Config
public class Waypoints
{

    public static double redGoalPointx = 72;
    public static double redGoalPointy = 72;

//    public static double blueGoalPointx = 41;//??? Seems wrong? diagonal from the center should be more like +-72
    public static double blueGoalPointx = 57;
//    public static double blueGoalPointy = 106;//??? This can't be correct. +- 72 is the max/min distance from the center
    public static double blueGoalPointy = -53;
    public static SparkFunOTOS.Pose2D startPointMiddleBottom = new SparkFunOTOS.Pose2D(0, -63, Math.toRadians(0));
    public static Pose2D startPointMiddleBottomPinpoint = new Pose2D(DistanceUnit.INCH,-63, 0, AngleUnit.RADIANS, Math.toRadians(0));
    public static void setRobotID(int robotID)
    {

    }
}
