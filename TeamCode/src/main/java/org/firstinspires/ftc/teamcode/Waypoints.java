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
    public static final Pose blueStartPoseWall = new Pose(60, 0, Math.toRadians(90));
    public static final Pose redStartPoseWall = new Pose(84, 0, Math.toRadians(90));
    public static final Pose redShooterPoint = new Pose(144, 144, Math.toRadians(45));
    public static final Pose redShooterPark = new Pose (120, 120, Math.toRadians(45));
    public static final Pose blueShooterPoint = new Pose(0, 144, Math.toRadians(135));
    public static final Pose blueShooterPark = new Pose (20, 120, Math.toRadians(135));
    public static final Pose blueAudienceParkTemp = new Pose(17, 105, Math.toRadians(90));

    public static Pose startPoseBlueAudience = new Pose(27,128,Math.toRadians(135));

    public static Pose blueWallParkTemp = new Pose (60, 35, Math.toRadians(90));
    public static Pose startPoseRedAudience = new Pose(117,128,Math.toRadians(45));
    public static Pose redAudienceParkTemp = new Pose (127, 105, Math.toRadians(90));
    public static Pose redWallParkTemp = new Pose(82, 35, Math.toRadians(90));
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

    // Waypoints for testing turret
    public static Pose tempStart = new Pose(0,0,Math.toRadians(90));
    public static Pose forward = new Pose(0,10,Math.toRadians(90));
    public static Pose backward = new Pose(0,-10,Math.toRadians(90));
    public static Pose left = new Pose(-10,0,Math.toRadians(90));
    public static Pose right = new Pose(10,10,Math.toRadians(90));

    public static void setRobotID(int robotID)
    {

    }
}
