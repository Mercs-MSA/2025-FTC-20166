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
    public static Pose goalPark;
    public static Pose goalPoint;
    public static Pose pickupOnePositioning;
    public static Pose pickupOneCollect;
    public static Pose pickupTwoPositioning;
    public static Pose pickupTwoCollect;
    public static Pose pickupThreePositioning;
    public static Pose pickupThreeCollect;
    public static Pose endPose;

    //PedroPoints
    public static final Pose blueStartPoseWall = new Pose(60, 8, Math.toRadians(90));
    public static final Pose redStartPoseWall = new Pose(84, 8, Math.toRadians(90));
    public static final Pose redShooterPoint = new Pose(144, 137, Math.toRadians(45));
    public static final Pose redShooterPark = new Pose (120, 120, Math.toRadians(45));
    public static final Pose blueShooterPoint = new Pose(0, 144, Math.toRadians(135));
    public static final Pose blueShooterPark = new Pose(33, 115, Math.toRadians(135));


    public static Pose startPoseNeutral = new Pose(8,8,Math.toRadians(0));
    public static Pose startPoseBlueAudience = new Pose(27,128,Math.toRadians(135));
    public static Pose startPoseRedAudience = new Pose(117,128,Math.toRadians(45));
    public static Pose redBox = new Pose(38,30,Math.toRadians(90));
    public static Pose blueBox = new Pose(105.2,30,Math.toRadians(90));

    public static double blueGoalPointx = 60;
    public static double blueGoalPointy = 60;
    public static SparkFunOTOS.Pose2D startPointMiddleBottom = new SparkFunOTOS.Pose2D(0, -63, Math.toRadians(0));
    public static Pose2D startPointMiddleBottomPinpoint = new Pose2D(DistanceUnit.INCH,-63, 0, AngleUnit.RADIANS, Math.toRadians(0));

    // Waypoints for testing turret

    public static void setTeam(boolean isRed) {
        if (isRed) {
            pickupOnePositioning = new Pose(94, 84, 0);
            pickupOneCollect = new Pose(127, 84, 0);
            pickupTwoPositioning = new Pose(94, 60, 0);
            pickupTwoCollect = new Pose(127, 60, 0);
            pickupThreePositioning = new Pose(94, 36, 0);
            pickupThreeCollect = new Pose(127, 36, 0);
            endPose = new Pose(124, 100, 0);
            goalPark = redShooterPark;
            goalPoint = redShooterPoint;
        }
        else
        {
            pickupOnePositioning = new Pose(50, 84, 180);
            pickupOneCollect = new Pose(17, 84, 180);
            pickupTwoPositioning = new Pose(50, 60, 180);
            pickupTwoCollect = new Pose(17, 60, 180);
            pickupThreePositioning = new Pose(50, 36, 180);
            pickupThreeCollect = new Pose(17, 36, 180);
            endPose = new Pose(20, 90, 90);
            goalPark = redShooterPark;
            goalPoint = redShooterPoint;
        }
    }
    //temp variables

    public static final Pose blueAudienceParkTemp = new Pose(17, 105, Math.toRadians(90));
    public static Pose blueWallParkTemp = new Pose (60, 35, Math.toRadians(90));
    public static Pose redAudienceParkTemp = new Pose (127, 105, Math.toRadians(90));
    public static Pose redWallParkTemp = new Pose(82, 35, Math.toRadians(90));

    public static void setRobotID(int robotID)
    {

    }
}