package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Waypoints;

@Autonomous
public class AutonOne extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private Pose startingPose;
    private Pose goalPose;
    private boolean isRedTeam;

    //private final Pose startPose = new Pose(28.5, 128, Math.toRadians(180)); // Start Pose of our robot.
    //private final Pose scorePose = new Pose(60, 85, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose pickup1Pose = new Pose(37, 121, Math.toRadians(0)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose pickup2Pose = new Pose(43, 130, Math.toRadians(0)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose pickup3Pose = new Pose(49, 135, Math.toRadians(0)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private Path scorePreload;
    private PathChain grabPickup1, scorePickup1, grabPickup2, scorePickup2, grabPickup3, scorePickup3;

    private enum state {
        START,
        WAIT_PATH_DONE,
        SCORE_PRELOAD,
        DO_NOTHING
    }
    private state pathState;
    private state nextPathState;
    public void buildPaths() {
        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(startingPose, goalPose));
        scorePreload.setLinearHeadingInterpolation(startingPose.getHeading(), goalPose.getHeading());

    /* Here is an example for Constant Interpolation
    scorePreload.setConstantInterpolation(startPose.getHeading()); */

        /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(goalPose, pickup1Pose))
                .setLinearHeadingInterpolation(goalPose.getHeading(), pickup1Pose.getHeading())
                .build();

        /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose, goalPose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), goalPose.getHeading())
                .build();

        /* This is our grabPickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(goalPose, pickup2Pose))
                .setLinearHeadingInterpolation(goalPose.getHeading(), pickup2Pose.getHeading())
                .build();

        /* This is our scorePickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(pickup2Pose, goalPose))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), goalPose.getHeading())
                .build();

        /* This is our grabPickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(goalPose, pickup3Pose))
                .setLinearHeadingInterpolation(goalPose.getHeading(), pickup3Pose.getHeading())
                .build();

        /* This is our scorePickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(pickup3Pose, goalPose))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), goalPose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case START:
                processStateStart();
                break;
            case WAIT_PATH_DONE:
                processStateWaitPathDone();
                break;
            case SCORE_PRELOAD:
                scorePreload();
                break;
            case DO_NOTHING:
                processStateDoNothing();
        }
    }

    private void processStateDoNothing(){}

    private void processStateStart()
    {
        pathState = state.SCORE_PRELOAD;
    }
    private void processStateWaitPathDone() {
        while (!follower.isBusy())
        {
            pathState = nextPathState;
        }
    }
    private void scorePreload()
    {
        follower.followPath(scorePreload);
        pathState = state.WAIT_PATH_DONE;
        nextPathState = state.DO_NOTHING;

    }
    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();

        updateTelemetry();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init()
    {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        startingPose = Waypoints.blueStartPoseWall;
        goalPose = Waypoints.blueGoal;

        pathState = state.DO_NOTHING;

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startingPose);
        updateTelemetry();
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop()
    {
        Pose prevStartingPose = startingPose;
        setStartingPose();

        if (startingPose != prevStartingPose)
        {
            follower = Constants.createFollower(hardwareMap);
            buildPaths();
            follower.setStartingPose(startingPose);
            updateTelemetry();
        }
    }

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start()
    {
        opmodeTimer.resetTimer();
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop()
    {
        if (isRedTeam)
        {
            blackboard.put("Alliance",RobotConstants.alliance.RED);
        } else
        {
            blackboard.put("Alliance",RobotConstants.alliance.BLUE);
        }
        blackboard.put("Position",follower.getPose());
    }
    public void setStartingPose()
    {
            if (gamepad1.a)
            {
                startingPose = Waypoints.redStartPoseWall;
                goalPose = Waypoints.redGoal;
                isRedTeam = true;
            }
            else if (gamepad1.b)
            {
                startingPose = Waypoints.startPoseRedAudience;
                goalPose = Waypoints.redGoal;
                isRedTeam = true;
            }
            else if (gamepad1.x)
            {
                startingPose = Waypoints.blueStartPoseWall;
                goalPose = Waypoints.blueGoal;
                isRedTeam = false;
            }
            else if (gamepad1.y) {
                startingPose = Waypoints.startPoseBlueAudience;
                goalPose = Waypoints.blueGoal;
                isRedTeam = false;
            }
    }

    private void updateTelemetry()
    {
        telemetry.addData("Starting Pose x", startingPose.getX());
        telemetry.addData("Starting Pose y", startingPose.getY());
        telemetry.addData("Starting Pose Heading", Math.toDegrees(startingPose.getHeading()));

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("Follower busy?", follower.isBusy());
        telemetry.addData("Red Team?", isRedTeam);

        telemetry.update();
    }
}