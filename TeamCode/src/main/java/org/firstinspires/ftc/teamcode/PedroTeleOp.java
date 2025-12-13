package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.bylazar.ftcontrol.panels.integration.TelemetryManager;
//import com.bylazar.ftcontrol.panels.json.Canvas;
//import com.bylazar.ftcontrol.panels.json.Rectangle;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.bylazar.ftcontrol.panels.Panels;
//import com.bylazar.ftcontrol.panels.json.CanvasRotation;
//import com.bylazar.ftcontrol.panels.json.Circle;
//import com.bylazar.ftcontrol.panels.json.Look;
//import com.bylazar.ftcontrol.panels.json.Point;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemRobotID;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
import org.firstinspires.ftc.teamcode.Utilities.GeneralUtils;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;
//config name                hub                slot                    description

//motors
//FL                         control              0                     front left motor
//FR                         control              1                     front right motor
//BL                         control              2                     back left motor
//BR                         control              3                     back right motor
//shooterFlyWheel            expansion            0                     shooter flywheel motor
//intake                     expansion            1                     intake motor

//servos
//shooterTiltLeft            control              0                     shooterTiltLeft servo
//shooterTiltRight           control              1                     shooterTiltRight servo
//lift                       control              2                     ball lift
//turretRotation             control              3                     turretRotation servo
//transfer1                  control              4                     transfer1 turret belt servo 1
//transfer2                  control              5                     transfer2 turret belt servo 2
//led                        expansion            0                     light emitting diodes
//gate                       expansion            1                     gate in shooter

//Sensors


//sensor_otos               control               I2C 1                 otos sensor //WE DO NOT USE THIS
//pinpoint                  expansion             I2C 0                 pinpoint sensor for odometry!
//limitSwitch               control               Digital 1             limit Switch
//limitSwitch2              control               Digital 3             limit Switch Two
//Turret Position Sensor    control               Analog Input 0

//Controller Buttons Used:
//Gamepad 1:
//Left joystick - moving forward, backword, and strafing
//Right joystick - turning
//X button - goes to box
//Circle button - stops holding box position
//dpad left + dpad right - box bind, resets position to box startingPose
//Gamepad 2:
//dpad up - sets shooter angle to max angle
//dpad down - sets shooter angle to min angle


@Config
@TeleOp
public class PedroTeleOp extends OpMode {
    private Follower follower;
    ElapsedTime timer = new ElapsedTime();

    private SubSystemShooter subSystemShooter;
    private double DTG;
    public static double telemetryUpper = 2500;
    public static double telemetryLower = 0;
    public static double tempVelocity = 1000;
    private boolean automatedDrive;
    private boolean shouldDoPositionLoop = false;
    boolean currentlyTurning = false;
    private Telemetry telemetryA;
    private RobotConstants.alliance alliance;
    private RobotConstants.location location;

    private Pose startingPose = null;
    private int joystickMultiplier;
    private Pose goalPose;
    private Supplier<PathChain> pathChain;
    private boolean slowMode = false;
    private double slowModeMultiplier = 0.5;
    private String startPosVerbose;
    private String allianceVerbose;
    private boolean defaultFieldCentric = true;
    private SubSystemRobotID subSystemRobotID;
    private Pose robotPose;
    private RobotConstants robotConstants;
    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;
    private Waypoints waypoints;

    //private TelemetryManager telemetryP = Panels.getTelemetry();

    public void initializeHardware()
    {
        try
        {
            subSystemRobotID = new SubSystemRobotID(hardwareMap);
            robotConstants = new RobotConstants(subSystemRobotID.getRobotID());
            subSystemShooter = new SubSystemShooter(hardwareMap, robotConstants);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void initializeLEDs(){
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE);
    }

    public String updateLEDStatus()
    {

        String test = "Nothing";
        if (timer.time() > 110)
        {
            test = "Endgame";
            blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
        }
        else if (timer.time() > 95)
        {
            test = "Endgame";
            blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
        }
        else
        {
            if (Math.abs(subSystemShooter.getShooterVelocity() - subSystemShooter.getShooterTargetVelocity()) < 50 && subSystemShooter.getShooterTargetVelocity() != 0)
            {
                test = "Ready to shoot";
                blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            }
            else
            {
                test = "Not ready";
                blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
            }

        }
        return test;
    }

    public void checkBlackboardFromAuto()
    {
        boolean positionGoodFromAuto = false;
        boolean allianceGoodFromAuto = false;
        startingPose = (Pose) blackboard.get("Position");
        alliance = (RobotConstants.alliance) blackboard.get("Alliance");
        follower = Constants.createFollower(hardwareMap, robotConstants);
        if (startingPose != null)
        {
            blackboard.remove("Position");
            positionGoodFromAuto = true;
        }
        if (alliance != null)
        {
            blackboard.remove("Alliance");
            allianceGoodFromAuto = true;
        }
        if (allianceGoodFromAuto && positionGoodFromAuto)
        {
            waypoints = new Waypoints(alliance, location);
            follower.setPose(startingPose);
            robotPose = startingPose;
        }
        else
        {
            waypoints = new Waypoints(RobotConstants.alliance.BLUE, RobotConstants.location.TEST);
            startingPose = waypoints.startingPose;
            follower.setPose(startingPose);
            updatePose();
            shouldDoPositionLoop = true;
        }
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
    private void changeAllianceMultiplier()
    {
        if (alliance == RobotConstants.alliance.RED)
        {
            joystickMultiplier = 1;
        }
        else if (alliance == RobotConstants.alliance.BLUE)
        {
            joystickMultiplier = -1;
        }
    }
    private void drawField()
    {
        TelemetryPacket fieldPayload = new TelemetryPacket(true);

        fieldPayload.fieldOverlay()
                .setTranslation(-robotPose.getX(), -robotPose.getY())
                .setRotation(robotPose.getHeading())
                .strokeRect(-5,-5,10,10)
                .strokeLine(0,0,-5,0)
                .setStroke("red")
                .setRotation(Math.toRadians(Math.toDegrees(robotPose.getHeading()) + subSystemShooter.getTurretAngle()))
                .strokeLine(0,0,-10,0)
                .setStroke("blue")
                .setRotation(Math.toRadians(subSystemShooter.getTurretDelta() + Math.toDegrees(robotPose.getHeading())))
                .strokeLine(0,0,-15,0);

        FtcDashboard.getInstance().sendTelemetryPacket(fieldPayload);
    }


    public void updateTurret()
    {

    }
    public void updatePedroDrive()
    {
        double forward;
        double strafe;
        if (!automatedDrive)
        {
            boolean robotCentric;
            if (defaultFieldCentric)
            {
                robotCentric = gamepad1.left_bumper;
            }
            else
            {
                robotCentric = !gamepad1.left_bumper;
            }
            if (!robotCentric)
            {
                forward = -gamepad1.left_stick_y * joystickMultiplier;
                strafe = -gamepad1.left_stick_x * joystickMultiplier;
            }
            else
            {

                forward = -gamepad1.left_stick_y;
                strafe = -gamepad1.left_stick_x;
            }
            double turn = -gamepad1.right_stick_x;

            if (!slowMode)
            {
                follower.setTeleOpDrive(forward, strafe, turn, robotCentric);
            }
            else
            {
                follower.setTeleOpDrive(forward * slowModeMultiplier, strafe * slowModeMultiplier, turn * slowModeMultiplier, robotCentric);
            }
        }
    }
    public void updatePose()
    {
        follower.update();
        robotPose = follower.getPose();
        goalPose = waypoints.goalPoint;
        DTG = GeneralUtils.getPointsDistance(goalPose.getX(),goalPose.getY(),robotPose.getX(),robotPose.getY());
    }

    public void updateAutomatedDrive()
    {
        //Automated PathFollowing
        if (gamepad1.aWasPressed()) {
        follower.holdPoint(waypoints.endgameParkBoxPose);
            automatedDrive = true;
        }

        //Stop automated following if the follower is done
        if (automatedDrive && gamepad1.bWasPressed()) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }
    }
    public void updateOperatorControls()
    {

        if (gamepad2.y)
        {
            subSystemShooter.setLiftArm(true);
            subSystemShooter.setTransfer(true);

        }
        else
        {
            subSystemShooter.setLiftArm(false);
            subSystemShooter.setTransfer(false);
        }

        if (gamepad2.x)
        {
            subSystemShooter.setIntakeSpeed(1200);
        }
        else if (gamepad2.b)
        {
            subSystemShooter.setIntakeSpeed(-1200);
        }
        else
        {
            subSystemShooter.setIntakeSpeed(0);
        }
        if (gamepad2.dpadUpWasPressed())
        {
            subSystemShooter.resetTurretOffset();
        }
        else if (gamepad2.dpad_right)
        {
            subSystemShooter.incrementTurretOffset(-.5);
        }
        else if (gamepad2.dpad_left)
        {
            subSystemShooter.incrementTurretOffset(.5);
        }
    }
    public void updateSlowMode()
    {
        //Slow Mode
        if (gamepad1.rightBumperWasPressed()) {
            slowMode = !slowMode;
        }
    }
    public void updateFeedback()
    {
        double targetVel = subSystemShooter.getShooterTargetVelocity();
        double vel = subSystemShooter.getShooterVelocity();

        if (Math.abs(vel - targetVel) <= 25)
        {
            if (gamepad2.dpad_up)
            {
                gamepad2.rumble(500);
            }
        }
    }
    public void updateTelemetryA()
    {
        telemetryA.addData("Alliance", allianceVerbose);
        telemetryA.addData("position X", robotPose.getX());
        telemetryA.addData("position Y", robotPose.getY());
        telemetryA.addData("position theta", Math.toDegrees(robotPose.getHeading()));
        telemetryA.addData("automatedDrive", automatedDrive);
        telemetryA.addLine();
        telemetryA.addData("Goal Pose", goalPose);

        telemetryA.addLine();
        telemetryA.addLine("Turret Data");
        telemetryA.addData("turretTargetAngle", subSystemShooter.getTurretDelta());
        telemetryA.addData("turretAngle", subSystemShooter.getTurretAngle());
        telemetryA.addData("Turret Error", subSystemShooter.getTurretError());
        telemetryA.addData("Turret Power", subSystemShooter.getTurretRotatePower());
        telemetryA.addData("Turret Target Velocity", subSystemShooter.getShooterTargetVelocity());
        telemetryA.addData("Turret Actual Velocity", subSystemShooter.getShooterVelocity());
        telemetryA.addData("Intake Target Velocity", subSystemShooter.getIntakeTargetVelocity());
        telemetryA.addData("Intake Actual Velocity", subSystemShooter.getIntakeVelocity());

        telemetryA.addLine();
        telemetryA.addData("RobotID", subSystemRobotID.getRobotID());
        telemetryA.addData("Robot podX offset", robotConstants.podX);
        telemetryA.addData("Robot podY offset", robotConstants.podY);
        telemetryA.addData("DTG", DTG);

        telemetryA.addLine();
        telemetryA.addData("Upper: ", telemetryUpper);
        telemetryA.addData("Lower: ", telemetryLower);

        telemetryA.addData("potVoltage",subSystemShooter.getPotVoltage());
        telemetryA.addData("ready to shoot", updateLEDStatus());
        telemetry.addData("timer", timer);
        drawField();

        updateTelemetry(telemetryA);
    }

    //main loops
    @Override
    public void init()
    {
        robotPose = new Pose();
        initializeHardware();
        initializeLEDs();

        checkBlackboardFromAuto();
        updateLEDStatus();

        follower.update();
        telemetryA = new MultipleTelemetry(this.telemetry,FtcDashboard.getInstance().getTelemetry());
        telemetryA.update();

        changeAllianceMultiplier();

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
        changeAllianceMultiplier();

        telemetryA.addData("Alliance", allianceVerbose);
        telemetryA.addData("Starting Position", startPosVerbose);
        telemetryA.addData("Default Drive Mode", (defaultFieldCentric) ? "Field Centric" : "Robot Centric");
        telemetryA.addData("RobotID", subSystemRobotID.getRobotID());

        telemetryA.update();

    }
    @Override
    public void init_loop()
    {

        if (shouldDoPositionLoop)
        {
            init_loopSelections();
        }
        updatePose();
        subSystemShooter.setGoalPose(goalPose);
//        subSystemShooter.setAlliance(alliance);
    }
    @Override
    public void start()
    {
        subSystemShooter.setGoalPose(goalPose);
        subSystemShooter.setAgitator(robotConstants.agitator);
        follower.update();
        follower.startTeleopDrive();
        timer.reset();
    }
    @Override
    public void loop()
    {
        updatePose();

        updateSlowMode();

        updatePedroDrive();
        updateAutomatedDrive();

        subSystemShooter.updateTurret(robotPose, DTG);
        updateOperatorControls();

//        updateFeedback();

        // do bs
        //doTest();

        updateTelemetryA();
    }
}