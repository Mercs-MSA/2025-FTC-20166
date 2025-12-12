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
    private Timer actionTimer, opmodeTimer;
    private Pose startingPose;
    private SubSystemShooter subSystemShooter;

    private SubSystemRobotID subSystemRobotID;
    private RobotConstants robotConstants;
    private Telemetry telemetryA;
    private boolean defaultFieldCentric = true;
    private String allianceVerbose;
    private RobotConstants.alliance alliance;
    private RobotConstants.location location;
    public static double telemetryUpper = 2500;
    public static double telemetryLower = 0;
    private String startPosVerbose;
    private int joystickMultiplier;
    private double DTG;
    private Pose goalPose;
    private Pose robotPose;
    private int ballCount = 3;

    private static ElapsedTime timeoutTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    private int timeoutPeriod = 0;
    private Waypoints waypoints;

    private enum state {
        START,
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


    public void autonomousPathUpdate() {
        switch (currentAutonomousState) {
            case START:
                processStateStart();
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

    private void processStateDoNothing()
    {
    }

    private void processStateStart()
    {
        follower.holdPoint(waypoints.autoShootFromPose);
        currentAutonomousState = state.WAIT_PATH_DONE;
        waitPathDoneNextState = state.SHOOT_BALL_STATE;

    }
    private void processBallLiftUp()
    {
        subSystemShooter.setLiftArm(true);
        restartTimeout(1000);

        currentAutonomousState = state.WAIT_TIMER_DONE_STATE;
        waitTimerDoneNextState = state.SHOOT_BALL_LIFT_DOWN_STATE;

    }
    private void processBallLiftDown()
    {
        subSystemShooter.setLiftArm(false);
        ballCount--;
        if (ballCount > 0)
        {
            restartTimeout(2000);
            currentAutonomousState = state.WAIT_TIMER_DONE_STATE;
            waitTimerDoneNextState = state.SHOOT_BALL_LIFT_UP_STATE;
        }
        else
        {
            follower.holdPoint(waypoints.autoEndParkPose);

            currentAutonomousState = state.DO_NOTHING;
        }
    }

    private void processShootBall()
    {
        currentAutonomousState = state.WAIT_TIMER_DONE_STATE;
        restartTimeout(7000);
        waitTimerDoneNextState = state.SHOOT_BALL_LIFT_UP_STATE;
    }
    private void processStateWaitPathDone() {
        if (!follower.isBusy())
        {
            currentAutonomousState = waitPathDoneNextState;
        }
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init()
    {
        //Set default alliance and location etc...
        alliance = RobotConstants.alliance.BLUE;
        location = RobotConstants.location.TEST;
        try {
            subSystemRobotID = new SubSystemRobotID(hardwareMap);
            robotConstants = new RobotConstants(subSystemRobotID.getRobotID());
            subSystemShooter = new SubSystemShooter(hardwareMap, robotConstants);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        waypoints = new Waypoints(alliance, location);
        startingPose = waypoints.startingPose;

        follower = Constants.createFollower(hardwareMap, robotConstants);

        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        currentAutonomousState = state.START;

        //Initialize follower
        follower.setPose(startingPose);
        follower.update();
        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        updateTelemetry();
    }

    private boolean updateInitialSettings()
    {
        if (gamepad1.dpadUpWasPressed())
        {
            defaultFieldCentric = !defaultFieldCentric;
        }
        if (gamepad1.aWasPressed())
        {
            alliance = RobotConstants.alliance.RED;
            location = RobotConstants.location.FRONT;
            startPosVerbose = "Against audience wall, near red goal, forward";
            allianceVerbose = "Red";
            return true;
        }
        else if (gamepad1.bWasPressed())
        {
            alliance = RobotConstants.alliance.RED;
            location = RobotConstants.location.BACK;
            startPosVerbose = "Against red goal, right wheels on tape, top right corner touching goal";
            allianceVerbose = "Red";
            return true;
        }
        else if (gamepad1.xWasPressed())
        {
            alliance = RobotConstants.alliance.BLUE;
            location = RobotConstants.location.FRONT;
            startPosVerbose = "Against audience wall, near blue goal, forward";
            allianceVerbose = "Blue";
            return true;
        }
        else if (gamepad1.yWasPressed())
        {
            alliance = RobotConstants.alliance.BLUE;
            location = RobotConstants.location.BACK;
            startPosVerbose = "Against blue goal, left wheels on tape, top left corner touching goal";
            allianceVerbose = "Blue";
            return true;
        }
        else if (gamepad1.rightBumperWasPressed())
        {
            alliance = RobotConstants.alliance.BLUE;
            location = RobotConstants.location.TEST;
            startPosVerbose = "Testing, in bottom left corner facing right (BLUE)";
            allianceVerbose = "Blue";
            return true;
        }
        else if (gamepad1.leftBumperWasPressed())
        {
            alliance = RobotConstants.alliance.RED;
            location = RobotConstants.location.TEST;
            startPosVerbose = "Testing, in bottom left corner facing right (RED)";
            allianceVerbose = "Red";
            return true;
        }
        else
            return false;
    }

    public void init_loopSelections(){
        boolean settingsChanged;

        settingsChanged = updateInitialSettings();

        if (settingsChanged)
        {
            waypoints.setWaypoints(alliance, location);
            startingPose = waypoints.startingPose;
            follower.setPose(startingPose);
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
        goalPose = waypoints.goalPoint;
        DTG = GeneralUtils.getPointsDistance(goalPose.getX(),goalPose.getY(),robotPose.getX(),robotPose.getY());
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
        subSystemShooter.setGoalPose(goalPose);
        opmodeTimer.resetTimer();
        subSystemShooter.setAgitator(-1);
        subSystemShooter.setTransfer(true);
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
        blackboard.put("Alliance",alliance);
        blackboard.put("Position",follower.getPose());
    }

    private void updateTelemetry()
    {
        telemetry.addData("Starting Pose x", startingPose.getX());
        telemetry.addData("Starting Pose y", startingPose.getY());
        telemetry.addData("Starting Pose Heading", Math.toDegrees(startingPose.getHeading()));
        telemetry.addLine();
        telemetry.addData("Goal Pose x", waypoints.goalPoint.getX());
        telemetry.addData("Goal Pose y", waypoints.goalPoint.getY());
        telemetry.addData("Goal Pose Heading", Math.toDegrees(waypoints.goalPoint.getHeading()));
        telemetry.addLine();
        telemetry.addData("path state", currentAutonomousState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Follower busy?", follower.isBusy());
        telemetry.addData("Alliance", alliance);
        telemetry.addLine();
        telemetry.addData("PodXOffset", robotConstants.podX);
        telemetry.addData("PodYOffset", robotConstants.podY);
        telemetryA.addData("Turret Target Velocity", subSystemShooter.getShooterTargetVelocity());
        telemetryA.addData("Turret Actual Velocity", subSystemShooter.getShooterVelocity());
        telemetryA.addData("Upper: ", telemetryUpper);
        telemetryA.addData("Lower: ", telemetryLower);

        telemetryA.update();
    }
}
