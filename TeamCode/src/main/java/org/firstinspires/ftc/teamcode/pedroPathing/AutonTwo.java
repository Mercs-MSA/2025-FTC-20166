package org.firstinspires.ftc.teamcode.pedroPathing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemRobotID;
import org.firstinspires.ftc.teamcode.Utilities.GeneralUtils;
import org.firstinspires.ftc.teamcode.Waypoints;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;


@Autonomous
public class AutonTwo extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private Pose startingPose;
    private SubSystemShooter subSystemShooter;

    private boolean isRedTeam;

    //private final Pose startPose = new Pose(28.5, 128, Math.toRadians(180)); // Start Pose of our robot.
    //private final Pose scorePose = new Pose(60, 85, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
//    private final Pose pickup1Pose = new Pose(37, 121, Math.toRadians(0)); // Highest (First Set) of Artifacts from the Spike Mark.
//    private final Pose pickup2Pose = new Pose(43, 130, Math.toRadians(0)); // Middle (Second Set) of Artifacts from the Spike Mark.
//    private final Pose pickup3Pose = new Pose(49, 135, Math.toRadians(0)); // Lowest (Third Set) of Artifacts from the Spike Mark.

    private Path scorePreload;
    private SubSystemRobotID subSystemRobotID;
    private RobotConstants robotConstants;
    private Telemetry telemetryA;
    private boolean defaultFieldCentric = true;
    private String allianceVerbose;
    private RobotConstants.alliance alliance;
    private String startPosVerbose;
    private int joystickMultiplier;
    private double DTG;
    private Pose goalPose;
    private Pose robotPose;

    private static ElapsedTime timeoutTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    private int timeoutPeriod = 0;
    private Waypoints waypoints;




    private PathChain positionGrabPickup1, grabPickup1, positionGrabPickup2, grabPickup2, positionGrabPickup3, grabPickup3;

    private enum state {
        START,
        GO_TO_SCORE,
        SCORE,
        POSITION_PICKUP_ONE,
        GRAB_PICKUP_ONE,
        POSITION_PICKUP_TWO,
        GRAB_PICKUP_TWO,
        POSITION_PICKUP_THREE,
        GRAB_PICKUP_THREE,
        WAIT_PATH_DONE,
        DO_NOTHING,
        WAIT_TIMER_DONE_STATE,
        SHOOT_BALL_STATE,
        SHOOT_BALL_LIFT_UP_STATE,
        SHOOT_BALL_LIFT_DOWN_STATE

    }
    private state currentAutonomousState = state.START;
    private state waitTimerDoneNextState = state.DO_NOTHING;
    private state waitPathDoneNextState = state.DO_NOTHING;



    private state pathState;
    private state nextPathState;
    private state postScorePathState;

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(startingPose, Waypoints.goalPark));
        scorePreload.setLinearHeadingInterpolation(startingPose.getHeading(), Waypoints.goalPark.getHeading());

//        positionGrabPickup1 = follower.pathBuilder()
//                .addPath(new BezierLine(Waypoints.goalPark, Waypoints.pickupOnePositioning))
//                .setLinearHeadingInterpolation(Waypoints.goalPark.getHeading(), Waypoints.pickupOnePositioning.getHeading())
//                .build();
//        grabPickup1 = follower.pathBuilder()
//                .addPath(new BezierLine(Waypoints.pickupOnePositioning, Waypoints.pickupOneCollect))
//                .setLinearHeadingInterpolation(Waypoints.pickupOnePositioning.getHeading(), Waypoints.pickupOneCollect.getHeading())
//                .build();
//
//        positionGrabPickup2 = follower.pathBuilder()
//                .addPath(new BezierLine(Waypoints.goalPark, Waypoints.pickupTwoPositioning))
//                .setLinearHeadingInterpolation(Waypoints.goalPark.getHeading(), Waypoints.pickupTwoPositioning.getHeading())
//                .build();
//        grabPickup2 = follower.pathBuilder()
//                .addPath(new BezierLine(Waypoints.pickupTwoPositioning, Waypoints.pickupTwoCollect))
//                .setLinearHeadingInterpolation(Waypoints.pickupTwoPositioning.getHeading(), Waypoints.pickupTwoCollect.getHeading())
//                .build();
//
//        positionGrabPickup3 = follower.pathBuilder()
//                .addPath(new BezierLine(Waypoints.goalPark, Waypoints.pickupThreePositioning))
//                .setLinearHeadingInterpolation(Waypoints.goalPark.getHeading(), Waypoints.pickupThreePositioning.getHeading())
//                .build();
//        grabPickup3 = follower.pathBuilder()
//                .addPath(new BezierLine(Waypoints.pickupThreePositioning, Waypoints.pickupThreeCollect))
//                .setLinearHeadingInterpolation(Waypoints.pickupThreePositioning.getHeading(), Waypoints.pickupThreeCollect.getHeading())
//                .build();
    }


    public void autonomousPathUpdate() {
        switch (currentAutonomousState) {
            case START:
                processStateStart();
                break;
//            case POSITION_PICKUP_ONE:
//                processStatePositionPickupOne();
//                break;
//            case GRAB_PICKUP_ONE:
//                processStateGrabPickupOne();
//                break;
//            case POSITION_PICKUP_TWO:
//                processStatePositionPickupTwo();
//                break;
//            case GRAB_PICKUP_TWO:
//                processStateGrabPickupTwo();
//                break;
//            case POSITION_PICKUP_THREE:
//                processStatePositionPickupThree();
//                break;
//            case GRAB_PICKUP_THREE:
//                processStateGrabPickupThree();
//                break;
            case GO_TO_SCORE:
                processStateGoToScore();
                break;
            case SCORE:
                score();
                break;
            case WAIT_PATH_DONE:
                processStateWaitPathDone();
                break;
            case DO_NOTHING:
                processStateDoNothing();
                break;
            case WAIT_TIMER_DONE_STATE:
                waitTimerDone();
                break;
            case SHOOT_BALL_STATE:
                processShootBall();
                break;
            case SHOOT_BALL_LIFT_UP_STATE:
                processBallLiftUp();
                break;
            case SHOOT_BALL_LIFT_DOWN_STATE:
                processBallLiftDown();
                break;


        }
    }
    private void restartTimeout(int timeout)
    {
        timeoutPeriod = timeout;
        timeoutTimer.reset();
    }
    private boolean hasTimededout()
    {
        if (timeoutTimer.time() < timeoutPeriod)
            return false;
        else
            return true;
    }
    private void waitTimerDone()
    {
        if (hasTimededout())
        {
            currentAutonomousState = waitTimerDoneNextState;
        }
    }

    private void processStateGoToScore()
    {
        Path goToScore = new Path(new BezierLine(follower.getPose(), Waypoints.goalPark)); // 12/9 change
        follower.followPath(goToScore);
        pathState = state.WAIT_PATH_DONE;
        nextPathState = state.SCORE;
    }


    private void processStateGrabPickupOne()
    {
        follower.followPath(grabPickup1);
        pathState = state.WAIT_PATH_DONE;
        nextPathState = state.GO_TO_SCORE;
        postScorePathState = state.POSITION_PICKUP_TWO;
    }

    private void processStatePositionPickupOne()
    {
        follower.followPath(positionGrabPickup1);
        pathState = state.WAIT_PATH_DONE;
        nextPathState = state.GRAB_PICKUP_ONE;
    }

    private void processStatePositionPickupTwo()
    {
        follower.followPath(positionGrabPickup2);
        pathState = state.WAIT_PATH_DONE;
        nextPathState = state.GRAB_PICKUP_TWO;
    }
    private void processStateGrabPickupTwo()
    {
        follower.followPath(grabPickup2);
        pathState = state.WAIT_PATH_DONE;
        nextPathState = state.GO_TO_SCORE;
        postScorePathState = state.POSITION_PICKUP_THREE;
    }
    private void processStatePositionPickupThree()
    {
        follower.followPath(positionGrabPickup3);
        pathState = state.WAIT_PATH_DONE;
        nextPathState = state.GRAB_PICKUP_THREE;
    }
    private void processStateGrabPickupThree()
    {
        follower.followPath(grabPickup3);
        pathState = state.WAIT_PATH_DONE;
        nextPathState = state.GO_TO_SCORE;
        postScorePathState = state.DO_NOTHING;
    }

    private void processStateDoNothing()
    {
        //follower.holdPoint(Waypoints.endPose);
    }

    private void processStateStart()
    {
        follower.holdPoint(waypoints.shootZone);
        currentAutonomousState = state.WAIT_PATH_DONE;
        waitPathDoneNextState = state.SHOOT_BALL_STATE;

    }
    private void processBallLiftUp()
    {
        subSystemShooter.setLiftArm(true);

        currentAutonomousState = state.WAIT_TIMER_DONE_STATE;
        waitTimerDoneNextState = state.SHOOT_BALL_LIFT_DOWN_STATE;

    }
    private void processBallLiftDown()
    {
        subSystemShooter.setLiftArm(false);

        currentAutonomousState = state.WAIT_TIMER_DONE_STATE;
        waitTimerDoneNextState = state.DO_NOTHING;
    }

    private void processShootBall()
    {
        currentAutonomousState = state.WAIT_TIMER_DONE_STATE;
        restartTimeout(1000);
        waitTimerDoneNextState = state.SHOOT_BALL_LIFT_UP_STATE;
    }
    private void processStateWaitPathDone() {
        if (!follower.isBusy())
        {
            pathState = nextPathState;
        }
    }
    private void score() {
        //Add code to score
        pathState = postScorePathState;
    }
    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init()
    {
        try {
            subSystemRobotID = new SubSystemRobotID(hardwareMap);
            robotConstants = new RobotConstants(subSystemRobotID.getRobotID());
            subSystemShooter = new SubSystemShooter(hardwareMap, robotConstants);
            waypoints = new Waypoints(isRedTeam);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        follower = Constants.createFollower(hardwareMap, robotConstants);
        alliance = (RobotConstants.alliance) blackboard.get("Alliance");
        //temp 12/9 ln 212
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        pathState = state.START;

        //Setting Defaults
        startingPose = Waypoints.blueStartPoseWall;
        isRedTeam = false; //Setting to blue team
        Waypoints.setTeam(isRedTeam);


        //Initialize follower
        follower.setPose(startingPose);
        buildPaths();
        follower.update();
        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        updateTelemetry();
    }

    private void setStartPos()
    {
        if (gamepad1.a)
        {
            startingPose = Waypoints.redStartPoseWall;
            //goalPose = Waypoints.redShooterPoint;
            alliance = RobotConstants.alliance.RED;
            startPosVerbose = "Against audience wall, near red goal, forward";
        }
        else if (gamepad1.b)
        {
            startingPose = Waypoints.startPoseRedAudience;
            //goalPose = Waypoints.redShooterPoint;
            alliance = RobotConstants.alliance.RED;
            startPosVerbose = "Against red goal, right wheels on tape, top right corner touching goal";
        }
        else if (gamepad1.x)
        {
            startingPose = Waypoints.blueStartPoseWall;
            //goalPose = Waypoints.blueShooterPoint;
            alliance = RobotConstants.alliance.BLUE;
            startPosVerbose = "Against audience wall, near blue goal, forward";
        }
        else if (gamepad1.y)
        {
            startingPose = Waypoints.startPoseBlueAudience;
            //goalPose = Waypoints.blueShooterPoint;
            alliance = RobotConstants.alliance.BLUE;
            startPosVerbose = "Against blue goal, left wheels on tape, top left corner touching goal";
        }
        else if (gamepad1.right_bumper)
        {
            startingPose = Waypoints.startPoseNeutral;
            //goalPose = Waypoints.blueShooterPoint;
            alliance = RobotConstants.alliance.BLUE;
            startPosVerbose = "Testing, in bottom left corner facing right (BLUE)";
        }
        else if (gamepad1.left_bumper)
        {
            startingPose = Waypoints.startPoseNeutral;
            //goalPose = Waypoints.blueShooterPoint;
            alliance = RobotConstants.alliance.RED;
            startPosVerbose = "Testing, in bottom left corner facing right (RED)";
        }
    }

    public void init_loopSelections(){
        Pose prevPose = startingPose;
        setStartPos();
        if (alliance == RobotConstants.alliance.RED)
        {
            allianceVerbose = "Red";
        }
        else if (alliance == RobotConstants.alliance.BLUE)
        {
            allianceVerbose = "Blue";
        }

        if (startingPose != prevPose)
        {
            follower.setPose(startingPose);
            Waypoints.setTeam(alliance == RobotConstants.alliance.RED);
        }




        telemetryA.addData("Alliance", allianceVerbose);
        telemetryA.addData("Starting Position", startPosVerbose);
        telemetryA.addData("Default Drive Mode", (defaultFieldCentric) ? "Field Centric" : "Robot Centric");
        telemetryA.addData("RobotID", subSystemRobotID.getRobotID());
        updatePose();

        telemetryA.update();

    }
    public void updatePose()
    {
        follower.update();
        robotPose = follower.getPose();
        goalPose = Waypoints.goalPark;
        DTG = GeneralUtils.getPointsDistance(goalPose.getX(),goalPose.getY(),robotPose.getX(),robotPose.getY());
    }
    public void dhruvasinit_loopSelections()
    {
        Pose prevStartingPose = startingPose;
        setStartingPose();

        if (startingPose != prevStartingPose)
        {
            follower.setPose(startingPose);
            Waypoints.setTeam(isRedTeam);
            buildPaths();
        }
        follower.update();
        updateTelemetry();
    }
    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop()
    {
        init_loopSelections();



    }

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start()
    {
        opmodeTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();
        subSystemShooter.updateTurret(robotPose, DTG);

        updateTelemetry();
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop()
    {
//        if (isRedTeam)
//        {
//            blackboard.put("Alliance",RobotConstants.alliance.RED);
//        } else
//        {
//            blackboard.put("Alliance",RobotConstants.alliance.BLUE);
//        }
//        blackboard.put("Position",follower.getPose());
        // 12/9 temp
    }
    public void setStartingPose()
    {
        if (gamepad1.a)
        {
            startingPose = Waypoints.redStartPoseWall;
            isRedTeam = true;
        }
        else if (gamepad1.b)
        {
            startingPose = Waypoints.startPoseRedAudience;
            isRedTeam = true;
        }
        else if (gamepad1.x)
        {
            startingPose = Waypoints.blueStartPoseWall;
            isRedTeam = false;
        }
        else if (gamepad1.y) {
            startingPose = Waypoints.startPoseBlueAudience;
            isRedTeam = false;
        }
    }

    private void updateTelemetry()
    {
        telemetry.addData("Starting Pose x", startingPose.getX());
        telemetry.addData("Starting Pose y", startingPose.getY());
        telemetry.addData("Starting Pose Heading", Math.toDegrees(startingPose.getHeading()));
        telemetry.addLine();
        telemetry.addData("Goal Pose x", Waypoints.goalPoint.getX());
        telemetry.addData("Goal Pose y", Waypoints.goalPoint.getY());
        telemetry.addData("Goal Pose Heading", Math.toDegrees(Waypoints.goalPoint.getHeading()));
        telemetry.addLine();
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Follower busy?", follower.isBusy());
        telemetry.addData("Red Team?", isRedTeam);
        telemetry.addLine();
        telemetry.addData("PodXOffset", robotConstants.podX);
        telemetry.addData("PodYOffset", robotConstants.podY);

        telemetry.update();
    }
}