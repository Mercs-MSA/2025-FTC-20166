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
    //Poses configured based on alliance and start location
    public Pose autoShootFromPose;
    public Pose autoEndParkPose;
    public Pose startingPose;
    public Pose goalPoint;
    public Pose endgameParkBoxPose;

    //PedroPoints for each pose and target location
    //CHECK ALL THESE !!!
    //BLUE
    public static final Pose blueStartFrontPose = new Pose(51.2, 8.2, Math.toRadians(180));
    public static final Pose blueShootFromFrontPose = new Pose(53.6, 8.2, Math.toRadians(180));
    public static final Pose blueAutoParkAtFrontPose = new Pose(53.6, 36, Math.toRadians(180));
    public static final Pose blueStartBackPose = new Pose(14.1, 113,Math.toRadians(270));
    public static final Pose blueShootFromBackPose = new Pose(27,128,Math.toRadians(-90));
    public static final Pose blueAutoParkAtBackPose = new Pose(48,132,Math.toRadians(180));
    public static final Pose blueGoalPoint = new Pose(8, 136, 0);//Heading is not used for goal point
    public static final Pose blueEndgameBoxPose = new Pose(99.8,32.6,Math.toRadians(0));
    //RED
    public static final Pose redStartFrontPose = new Pose(85.3,8.6,Math.toRadians(0));
    public static final Pose redShootFromFrontPose = new Pose(85.3,8.6,Math.toRadians(0));//done
    public static final Pose redAutoParkAtFrontPose = new Pose(90.4,36,Math.toRadians(0));//done
    public static final Pose redStartBackPose = new Pose(123.6,112.3,Math.toRadians(-90));
    public static final Pose redShootFromBackPose = new Pose(86,118,Math.toRadians(-90));//done
    public static final Pose redAutoParkAtBackPose = new Pose(86,118,Math.toRadians(-90));//done
    public static final Pose redGoalPoint = new Pose(136, 136, 0);//Heading is not used for goal point
    public static final Pose redEndgameBoxPose = new Pose(36.7,33.9,Math.toRadians(0));
    //Neutral test
    public static final Pose testStartPose = new Pose(8,8,Math.toRadians(0));
    public static final Pose testFieldCenter = new Pose(72,72,Math.toRadians(90));

//    /// /////////////////////////////////////////////////////////////////////////////////////////////////
// Copied from original code.
// Doubts about audience vs wall Y coordinate :)
//    /// /////////////////////////////////////////////////////////////////////////////////////////////////
//    public static Pose blueStartPoseWall = new Pose(53.6, 8.2, Math.toRadians(180));
//    public static Pose startPoseBlueAudience = new Pose(27,128,Math.toRadians(135));
//    public static Pose startPoseRedAudience = new Pose(128,109.7,Math.toRadians(270));
//    public static Pose redStartPoseWall = new Pose(87.6, 8.2, Math.toRadians(0));
//    public static Pose redShooterPoint = new Pose(144, 144, Math.toRadians(45));
//    public static Pose redShooterPark = new Pose (120, 120, Math.toRadians(45));
//    public static Pose blueShooterPoint = new Pose(0, 144, Math.toRadians(135));
//    public static Pose blueShooterPark = new Pose(33, 115, Math.toRadians(135));
//    public static Pose startPoseNeutral = new Pose(8,8,Math.toRadians(0));
//    public static Pose redBox = new Pose(36.7,31.5,Math.toRadians(0));
//    public static Pose blueBox = new Pose(104.4,31.5,Math.toRadians(180));
//    public static Pose box;
//    public static double blueGoalPointx = 60;
//    public static double blueGoalPointy = 60;
//    public static SparkFunOTOS.Pose2D startPointMiddleBottom = new SparkFunOTOS.Pose2D(0, -63, Math.toRadians(0));
//    public static Pose2D startPointMiddleBottomPinpoint = new Pose2D(DistanceUnit.INCH,-63, 0, AngleUnit.RADIANS, Math.toRadians(0));
//    public static Pose blueAudienceParkTemp = new Pose(17, 105, Math.toRadians(90));
//    public static Pose blueWallParkTemp = new Pose (60, 35, Math.toRadians(90));
//    public static Pose redAudienceParkTemp = new Pose (127, 105, Math.toRadians(90));
//    public static Pose redWallParkTemp = new Pose(82, 35, Math.toRadians(90));
//
//    /// /////////////////////////////////////////////////////////////////////////////////////////////////
//    /// /////////////////////////////////////////////////////////////////////////////////////////////////

    public Waypoints(RobotConstants.alliance alliance, RobotConstants.location location)
    {
        setWaypoints(alliance, location);
    }
    public void setWaypoints(RobotConstants.alliance alliance, RobotConstants.location location) {
        if (alliance == RobotConstants.alliance.BLUE)
        {
            //Alliance specific poses
            goalPoint = blueGoalPoint;//Where is the goal?
            endgameParkBoxPose = blueEndgameBoxPose;//Where is the end game park box?
            //Start location specific poses
            if (location == RobotConstants.location.FRONT)
            {
                startingPose = blueStartFrontPose;//Where are we starting?
                autoShootFromPose = blueShootFromFrontPose;//Where should we shoot from?
                autoEndParkPose = blueAutoParkAtFrontPose;//Where should we park at the end of auto?
            }
            else if (location == RobotConstants.location.BACK)
            {
                startingPose = blueStartBackPose;//Where are we starting?
                autoShootFromPose = blueShootFromBackPose;//Where should we shoot from?
                autoEndParkPose = blueAutoParkAtBackPose;//Where should we park at the end of auto?
            }
            else //Test location
            {
                startingPose = testStartPose;//Where are we starting?
                autoShootFromPose = testFieldCenter;//Where should we shoot from?
                autoEndParkPose =testFieldCenter;//Where should we park at the end of auto?
            }
        }
        else
        {
            //Alliance specific poses
            goalPoint = redGoalPoint;//Where is the goal?
            endgameParkBoxPose = redEndgameBoxPose;//Where is the end game park box?
            //Start location specific poses
            if (location == RobotConstants.location.FRONT)
            {
                startingPose = redStartFrontPose;//Where are we starting?
                autoShootFromPose = redShootFromFrontPose;//Where should we shoot from?
                autoEndParkPose = redAutoParkAtFrontPose;//Where should we park at the end of auto?
            }
            else if (location == RobotConstants.location.BACK)
            {
                startingPose = redStartBackPose;//Where are we starting?
                autoShootFromPose = redShootFromBackPose;//Where should we shoot from?
                autoEndParkPose = redAutoParkAtBackPose;//Where should we park at the end of auto?
            }
            else //Test location
            {
                startingPose = testStartPose;//Where are we starting?
                autoShootFromPose = testFieldCenter;//Where should we shoot from?
                autoEndParkPose =testFieldCenter;//Where should we park at the end of auto?
            }
        }
    }
}