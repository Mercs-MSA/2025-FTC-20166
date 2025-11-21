//package org.firstinspires.ftc.teamcode.Subsystems;
//
//import static java.lang.Thread.sleep;
//
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
//import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;
//import org.firstinspires.ftc.teamcode.RobotConstants;
//import org.firstinspires.ftc.teamcode.Waypoints;
//
//public class SubSystemRobotPinpoint
//{
//    private GoBildaPinpointDriver pinpoint;
//    private int robotID;
//    private double startPositionX;
//    private double startPositionY;
//    private double startPositionT;
//    private double podOffsetX;
//    private double podOffsetY;
//
//    public SubSystemRobotPinpoint(HardwareMap hardwareMap, int robotID) throws InterruptedException
//    {
//        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
//        this.robotID = robotID;
//
//        configurePinpoint();
//        pinpoint.setPosition(Waypoints.startPointMiddleBottomPinpoint);
//        startPositionX = Waypoints.startPointMiddleBottomPinpoint.getX(DistanceUnit.INCH);
//        startPositionY = Waypoints.startPointMiddleBottomPinpoint.getY(DistanceUnit.INCH);
//        startPositionT = Waypoints.startPointMiddleBottomPinpoint.getHeading(AngleUnit.DEGREES);
//    }
//
//    public void configurePinpoint() throws InterruptedException {
//        /*
//         *  Set the odometry pod positions relative to the point that you want the position to be measured from.
//         *
//         *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
//         *  Left of the center is a positive number, right of center is a negative number.
//         *
//         *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
//         *  Forward of center is a positive number, backwards is a negative number.
//         * Set the direction that each of the two odometry pods count. The X (forward) pod should
//         * increase when you move the robot forward. And the Y (strafe) pod should increase when
//         * you move the robot to the left.
//         */
//        podOffsetX = 0;
//        podOffsetY = 0;
//        if (robotID == 0) //robot A
//        {
//            pinpoint.setOffsets(RobotConstants.robotAPodXOffset, RobotConstants.robotAPodYOffset, DistanceUnit.MM);
//            podOffsetX = 1;
//            podOffsetY = 1;
//            pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
//        }
//        else //robotB has robotID = 1
//        {
//            pinpoint.setOffsets(RobotConstants.robotBPodXOffset, RobotConstants.robotBPodYOffset, DistanceUnit.MM);
//            podOffsetX = RobotConstants.robotBPodXOffset;
//            podOffsetY = RobotConstants.robotBPodYOffset;
//            pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.REVERSED);
//
//        }
//        //Test only
////        pinpoint.setOffsets(RobotConstants.robotBPodXOffset, RobotConstants.robotBPodYOffset, DistanceUnit.MM);
////        podOffsetX = RobotConstants.robotBPodXOffset;
////        podOffsetY = RobotConstants.robotBPodYOffset;
////        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
//        /*
//         * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
//         * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
//         * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
//         * number of ticks per unit of your odometry pod.  For example:
//         *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
//         */
//        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
//
//
//        /*
//         * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
//         * The IMU will automatically calibrate when first powered on, but recalibrating before running
//         * the robot is a good idea to ensure that the calibration is "good".
//         * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
//         * This is recommended before you run your autonomous, as a bad initial calibration can cause
//         * an incorrect starting value for x, y, and heading.
//         */
//        pinpoint.resetPosAndIMU();
//        sleep(1000);
//
//    }
//
//    public void updatePinpoint()
//    {
//        pinpoint.update();
//    }
//
//    public Pose2D getStartPose()
//    {
//
//        return new Pose2D(DistanceUnit.INCH,startPositionX,startPositionY, AngleUnit.DEGREES,startPositionT);
//    }
//
//    public Pose2D getPodOffsets()
//    {
//        return new Pose2D(DistanceUnit.MM, podOffsetX, podOffsetY, AngleUnit.DEGREES,0);
//    }
//
//    public Pose2D getPinpointPos()
//    {
//        Pose2D remuxPosition = pinpoint.getPosition();
////        This should be x, y, t !!! We have y, x, t !!! Could be the cause of a lot of our issues???
////        return new Pose2D(DistanceUnit.INCH,remuxPosition.getY(DistanceUnit.INCH),remuxPosition.getX(DistanceUnit.INCH), AngleUnit.DEGREES,remuxPosition.getHeading(AngleUnit.DEGREES));
//        return new Pose2D(DistanceUnit.INCH,remuxPosition.getX(DistanceUnit.INCH),remuxPosition.getY(DistanceUnit.INCH), AngleUnit.DEGREES,remuxPosition.getHeading(AngleUnit.DEGREES));
//    }
//}
