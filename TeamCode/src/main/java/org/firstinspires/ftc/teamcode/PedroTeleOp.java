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
    private SubSystemShooter subSystemShooter;
    private double DTG;
    private boolean automatedDrive;
    private boolean shouldDoPositionLoop = false;
    boolean currentlyTurning = false;
    private Telemetry telemetryA;
    private RobotConstants.alliance alliance;
    private Pose startingPose = null;
    private int joystickMultiplier;
    private Pose goalPose;
    private Supplier<PathChain> pathChain;
    private boolean slowMode = false;
    private double slowModeMultiplier = 0.5;
    private double desiredHeading = 0;
    private double debugturretAngle = 0;
    private SubSystemRobotID subSystemRobotID;
    private Pose robotPose;
    private RobotConstants robotConstants;


    //private TelemetryManager telemetryP = Panels.getTelemetry();

    public void initializeHardware()
    {
        try
        {
            subSystemRobotID = new SubSystemRobotID(hardwareMap);
            subSystemShooter = new SubSystemShooter(hardwareMap);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateConstants()
    {
        startingPose = (Pose) blackboard.get("Position");
        alliance = (RobotConstants.alliance) blackboard.get("Alliance");
        follower = Constants.createFollower(hardwareMap, robotConstants);
        if (startingPose != null)
        {
            //follower.setPose(startingPose);
            blackboard.remove("Position");
        } else
        {
            shouldDoPositionLoop = true;
            startingPose = Waypoints.startPoseBlueAudience;
        }
        if (alliance != null)
        {
            blackboard.remove("Alliance");
        }
        else
        {
            alliance = RobotConstants.alliance.BLUE;
        }
        Waypoints.setTeam(alliance == RobotConstants.alliance.RED);
        follower.setPose(startingPose);
    }
    private void setStartPos()
    {
        if (gamepad1.a)
        {
            startingPose = Waypoints.redStartPoseWall;
            //goalPose = Waypoints.redShooterPoint;
            alliance = RobotConstants.alliance.RED;
        }
        else if (gamepad1.b)
        {
            startingPose = Waypoints.startPoseRedAudience;
            //goalPose = Waypoints.redShooterPoint;
            alliance = RobotConstants.alliance.RED;
        }
        else if (gamepad1.x)
        {
            startingPose = Waypoints.blueStartPoseWall;
            //goalPose = Waypoints.blueShooterPoint;
            alliance = RobotConstants.alliance.BLUE;
        }
        else if (gamepad1.y)
        {
            startingPose = Waypoints.startPoseBlueAudience;
            //goalPose = Waypoints.blueShooterPoint;
            alliance = RobotConstants.alliance.BLUE;
        }
        else if (gamepad1.right_bumper)
        {
            startingPose = Waypoints.startPoseNeutral;
            //goalPose = Waypoints.blueShooterPoint;
            alliance = RobotConstants.alliance.BLUE;
        }
    }
    private void changeAllianceMultiplier()
    {
        if (alliance == RobotConstants.alliance.RED)
        {
            joystickMultiplier = -1;
        }
        else if (alliance == RobotConstants.alliance.BLUE)
        {
            joystickMultiplier = 1;
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
        if (!automatedDrive)
        {
            double forward = -gamepad1.left_stick_y * joystickMultiplier;
            double strafe = -gamepad1.left_stick_x * joystickMultiplier;
            double turn = -gamepad1.right_stick_x;
            boolean robotCentric = !gamepad1.left_bumper; //If true, it's robot centric

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
        goalPose = Waypoints.goalPoint;
        DTG = GeneralUtils.getPointsDistance(goalPose.getX(),goalPose.getY(),robotPose.getX(),robotPose.getY());
    }
////    public void updatePedroDriveTest()
////    {
////
////        if (!automatedDrive)
////        {
////            double forward = -gamepad1.left_stick_y * joystickMultiplier;
////            double strafe = -gamepad1.left_stick_x * joystickMultiplier;
////            double turn = -gamepad1.right_stick_x;
////            boolean fieldCentric = !gamepad1.left_bumper; //If false, it's robot centric
////
////            if (Math.abs(turn) >= RobotConstants.joystickRotateDeadband)
////            {
////                currentlyTurning = true;
////            }
////            else
////            {
////                double currentHeading = robotPose.getHeading();
////                if (currentlyTurning)
////                {
////                    currentlyTurning = false;
////                    desiredHeading = Math.toDegrees(currentHeading);
////                }
////                double headingError = GeneralUtils.wrapRange(Math.toDegrees(currentHeading) - Math.toDegrees(desiredHeading), 180);
////                if (Math.abs(headingError) > RobotConstants.headingErrorDeadZone) {
////
////                    turn = headingError * RobotConstants.headingPFactor;
////                }
////                else
////                {
////                    turn = 0;
////                }
////            }
//
//
//            if (!slowMode)
//            {
//                follower.setTeleOpDrive(forward, strafe, turn, fieldCentric);
//            }
//            else
//            {
//                follower.setTeleOpDrive(forward * slowModeMultiplier, strafe * slowModeMultiplier, turn * slowModeMultiplier, fieldCentric);
//            }
//        }
//    }
    public void updateAutomatedDrive()
    {
        //Automated PathFollowing
        if (gamepad1.aWasPressed()) {
            follower.holdPoint(Waypoints.redBox);
            automatedDrive = true;
        }

        //Stop automated following if the follower is done
        if (automatedDrive && gamepad1.bWasPressed()) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }
    }
    public void updateTransfer()
    {
        if (gamepad2.dpadUpWasPressed())
        {
            subSystemShooter.setShooterAngle(RobotConstants.shooterMaxAngle);
        }
        else if (gamepad2.dpadDownWasPressed())
        {
            subSystemShooter.setShooterAngle(RobotConstants.shooterMinAngle);
        }
    }
    public void doTest()
    {

//        if(gamepad2.left_bumper)
//        {
//
//        }
//        else
//        {
//
//        }
//        if (gamepad2.right_bumper)
//        {
//            subSystemShooter.setShooterSpeed(1500);
//        }
//        else
//        {
//            subSystemShooter.setShooterSpeed(0);
//        }
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
            subSystemShooter.setIntakeSpeed(800);
        }
        else if (gamepad2.b)
        {
            subSystemShooter.setIntakeSpeed(-800);
        }
        else
        {
            subSystemShooter.setIntakeSpeed(0);
        }
    }
    public void updateSlowMode()
    {
//        //Optional way to change slow mode strength
//        if (gamepad1.xWasPressed()) {
//            slowModeMultiplier += 0.25;
//        }
//
//        //Optional way to change slow mode strength
//        if (gamepad2.yWasPressed()) {
//            slowModeMultiplier -= 0.25;
//        }

        //Slow Mode
        if (gamepad1.rightBumperWasPressed()) {
            slowMode = !slowMode;
        }
    }
    public void updateFeedback()
    {
        double targetVel = subSystemShooter.getShooterTargetVelocity();
        double vel = subSystemShooter.getShooterVelocity();

        if (Math.abs(vel - targetVel) < 75)
        {
            gamepad2.rumble(500);
        }
    }
    public void updateBoxBind()
    {
        if (gamepad1.dpadLeftWasPressed() && gamepad1.dpadRightWasPressed())
        {
            if (alliance == RobotConstants.alliance.RED)
            {
                follower.setPose(Waypoints.redBox);
            }
            else if (alliance == RobotConstants.alliance.BLUE)
            {
                follower.setPose(Waypoints.blueBox);
            }
        }
    }
    public void updateTelemetryA()
    {
        telemetryA.addData("position X", robotPose.getX());
        telemetryA.addData("position Y", robotPose.getY());
        telemetryA.addData("position theta", Math.toDegrees(robotPose.getHeading()));
        telemetryA.addData("automatedDrive", automatedDrive);
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

//        telemetryA.addData("potVoltage",subSystemShooter.getPotVoltage());
        telemetryA.addLine();
        telemetryA.addData("RobotID", subSystemRobotID.getRobotID());
        telemetryA.addData("Robot podX offset", robotConstants.podX);
        telemetryA.addData("Robot podY offset", robotConstants.podY);
        telemetryA.addData("DTG", DTG);

        drawField();

        updateTelemetry(telemetryA);
    }

    //main loops
    @Override
    public void init()
    {
        robotPose = new Pose();
        initializeHardware();
        robotConstants = new RobotConstants(subSystemRobotID.getRobotID());
        updateConstants();

        follower.update();
        telemetryA = new MultipleTelemetry(this.telemetry,FtcDashboard.getInstance().getTelemetry());
        telemetryA.update();

        changeAllianceMultiplier();
//        pathChain = () -> follower.pathBuilder() //Lazy Curve Generation
//                .addPath(new Path(new BezierLine(follower::getPose, Waypoints.)))
//                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(180), 0.8))
//
//                .build();

    }
    @Override
    public void init_loop()
    {

        if (shouldDoPositionLoop)
        {
            Pose prevPose = startingPose;
            setStartPos();

            if (startingPose != prevPose)
            {
                follower.setPose(startingPose);
                Waypoints.setTeam(alliance == RobotConstants.alliance.RED);
                changeAllianceMultiplier();
            }

            updatePose();

            telemetryA.addData("Starting Pose: ", startingPose);
            telemetryA.addData("Goal Pose: ", Waypoints.goalPoint);
            telemetryA.addData("Doing loop? ",shouldDoPositionLoop);

            updateTelemetryA();

        }
//        subSystemShooter.setAlliance(alliance);
    }
    @Override
    public void start()
    {
        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        Waypoints.setTeam(alliance == RobotConstants.alliance.RED);
        follower.update();
        follower.startTeleopDrive();
    }
    @Override
    public void loop()
    {
        updatePose();

        updateSlowMode();

        updatePedroDrive();
        updateAutomatedDrive();

        updateBoxBind();

        updateTransfer();
        subSystemShooter.updateTurret(robotPose, DTG);

        updateFeedback();

        // do bs
        doTest();

        updateTelemetryA();
    }
}