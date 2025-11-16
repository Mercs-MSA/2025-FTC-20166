package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Config
public class Waypoints
{
    //PedroPoints
    public static final Pose blueStartPoseWall = new Pose(84, 0, Math.toRadians(90));
    public static final Pose redStartPoseWall = new Pose(60, 0, Math.toRadians(90));
    public static final Pose redGoal = new Pose(144, 144, Math.toRadians(45));
    public static final Pose blueGoal = new Pose(0, 144, Math.toRadians(135));
    public static Pose startPoseBlueAudience = new Pose(72,0,Math.toRadians(90));
    public static Pose startPoseRedAudience = new Pose(72,0,Math.toRadians(90)); //Need to change
    public static Pose redBox = new Pose(38.5,33.5,Math.toRadians(90));
    public static Pose blueBox = new Pose(105.2,33.5,Math.toRadians(90));



    public static double redGoalPointx = 72;
    public static double redGoalPointy = 72;

//    public static double blueGoalPointx = 41;//??? Seems wrong? diagonal from the center should be more like +-72
    public static double blueGoalPointx = 60;
//    public static double blueGoalPointy = 106;//??? This can't be correct. +- 72 is the max/min distance from the center
    public static double blueGoalPointy = 60;
    public static SparkFunOTOS.Pose2D startPointMiddleBottom = new SparkFunOTOS.Pose2D(0, -63, Math.toRadians(0));
    public static Pose2D startPointMiddleBottomPinpoint = new Pose2D(DistanceUnit.INCH,-63, 0, AngleUnit.RADIANS, Math.toRadians(0));
    public static void setRobotID(int robotID)
    {

    }
}
