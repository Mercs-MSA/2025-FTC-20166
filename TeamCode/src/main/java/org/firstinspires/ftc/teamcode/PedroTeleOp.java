package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.configurables.annotations.Configurable;
//import com.bylazar.ftcontrol.panels.integration.TelemetryManager;
//import com.bylazar.ftcontrol.panels.json.Canvas;
//import com.bylazar.ftcontrol.panels.json.Rectangle;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.robot.Robot;

//import com.bylazar.ftcontrol.panels.Panels;
//import com.bylazar.ftcontrol.panels.json.CanvasRotation;
//import com.bylazar.ftcontrol.panels.json.Circle;
//import com.bylazar.ftcontrol.panels.json.Look;
//import com.bylazar.ftcontrol.panels.json.Point;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
import org.firstinspires.ftc.teamcode.Waypoints;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.opencv.core.Mat;

import java.util.function.Supplier;
//config name                hub                slot                    description

//motors
//FL                         control              0                     front left motor
//FR                         control              1                     front right motor
//BL                         control              2                     back left motor
//BR                         control              3                     back right motor
//shooterFlyWheel            expansion            0                     shooter flywheel motor

//servos
//shooterTiltLeft            control              0                     shooterTiltLeft servo
//shooterTiltRight           control              1                     shooterTiltRight servo
//                                        PORT TWO SERVO IS BAD
//turretRotation             control               3                    turretRotation servo
//transfer1                  control               4                    transfer1 turret belt servo 1
//transfer2                  control               5                    transfer2 turret belt servo 2

//Sensors
//sensor_otos               control                I2C 1                otos sensor //WE DO NOT USE THIS
//pinpoint                  control                I2C 2                pinpoint sensor for odometry!
//Turret Position Sensor -  control                Analog Input 0

//Controller Buttons Used:
//Gamepad 1:
//Left joystick - moving forward, backword, and strafing
//Right joystick - turning
//X button - goes to box
//Circle button - stops holding box position
//dpad left + dpad right - box bind, resets position to box pose
//Gamepad 2:
//dpad up - sets shooter angle to max angle
//dpad down - sets shooter angle to min angle


@Config
@TeleOp
public class PedroTeleOp extends OpMode {
    private Follower follower;
    private SubSystemShooter subSystemShooter;
    private boolean automatedDrive;
    private boolean shouldDoPositionLoop = false;
    boolean currentlyTurning = false;
    private Telemetry telemetryA;
    private RobotConstants.alliance previousAlliance;
    private Pose pose = null;
    private int joystickMultiplier;
    private Pose goalPose;
    private Supplier<PathChain> pathChain;
    private boolean slowMode = false;
    private double slowModeMultiplier = 0.5;
    public static double turretTargetAngle = 0;
    private double desiredHeading = 0;
    private double debugturretAngle = 0;
    double currentTurretAngleError;
    double turretRotatePower;


    //private TelemetryManager telemetryP = Panels.getTelemetry();

    public void initializeHardware()
    {
        try
        {
            subSystemShooter = new SubSystemShooter(hardwareMap);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateConstants()
    {
        pose = (Pose) blackboard.get("Position");
        previousAlliance = (RobotConstants.alliance) blackboard.get("Alliance");
        follower = Constants.createFollower(hardwareMap);
        if (pose != null)
        {
            follower.setStartingPose(pose);
            blackboard.remove("Position");
        } else
        {
            shouldDoPositionLoop = true;
            pose = Waypoints.startPoseBlueAudience;
        }
        if (previousAlliance != null)
        {
            blackboard.remove("Alliance");
        }
        else
        {
            previousAlliance = RobotConstants.alliance.BLUE;
        }
    }
    private void setStartPos()
    {
        if (gamepad1.a)
        {
            pose = Waypoints.redStartPoseWall;
            goalPose = Waypoints.redShooterPoint;
            previousAlliance = RobotConstants.alliance.RED;
        }
        else if (gamepad1.b)
        {
            pose = Waypoints.startPoseRedAudience;
            goalPose = Waypoints.redShooterPoint;
            previousAlliance = RobotConstants.alliance.RED;
        }
        else if (gamepad1.x)
        {
            pose = Waypoints.blueStartPoseWall;
            goalPose = Waypoints.blueShooterPoint;
            previousAlliance = RobotConstants.alliance.BLUE;
        }
        else if (gamepad1.y) {
            pose = Waypoints.startPoseBlueAudience;
            goalPose = Waypoints.blueShooterPoint;
            previousAlliance = RobotConstants.alliance.BLUE;
        }
    }
    private void changeAllianceMultiplier()
    {
        if (previousAlliance == RobotConstants.alliance.RED)
        {
            joystickMultiplier = -1;
        }
        else if (previousAlliance == RobotConstants.alliance.BLUE)
        {
            joystickMultiplier = 1;
        }
    }
    private void draw()
    {
    }
    public double wrapRange(double number, double range)
    {
        while(number >= range)
            number -= (range * 2);
        while(number <= -range)
            number += (range * 2);
        return number;
    }
    public double clampRange(double number, double range)
    {
        if (number > range)
        {
            return range;
        }
        else if (number < -range)
        {
            return -range;
        } else
        {
            return number;
        }
    }
    public void updateTurret()
    {
        double currentTurretAngle;

        //this is for turret rotation
        currentTurretAngle = subSystemShooter.getTurretAngle();
        debugturretAngle = currentTurretAngle;
        currentTurretAngleError =  turretTargetAngle - currentTurretAngle;
        turretRotatePower = clampRange(RobotConstants.turretRotationP*currentTurretAngleError, RobotConstants.turretMaxPower);
        subSystemShooter.setTurretRotationSpeed(turretRotatePower);
    }
    public void updatePedroDrive()
    {
        if (!automatedDrive)
        {
            double forward = -gamepad1.left_stick_y * joystickMultiplier;
            double strafe = -gamepad1.left_stick_x * joystickMultiplier;
            double turn = -gamepad1.right_stick_x;
            boolean fieldCentric = false; //If false, it's robot centric

            if (!slowMode)
            {
                follower.setTeleOpDrive(forward, strafe, turn, fieldCentric);
            }
            else
            {
                follower.setTeleOpDrive(forward * slowModeMultiplier, strafe * slowModeMultiplier, turn * slowModeMultiplier, fieldCentric);
            }
        }
    }
    public void updatePedroDriveTest()
    {

        if (!automatedDrive)
        {
            double forward = -gamepad1.left_stick_y * joystickMultiplier;
            double strafe = -gamepad1.left_stick_x * joystickMultiplier;
            double turn = -gamepad1.right_stick_x;
            boolean fieldCentric = false; //If false, it's robot centric

            if (Math.abs(turn) >= RobotConstants.joystickRotateDeadband)
            {
                currentlyTurning = true;
            }
            else
            {
                double currentHeading = follower.getPose().getHeading();
                if (currentlyTurning)
                {
                    currentlyTurning = false;
                    desiredHeading = Math.toDegrees(currentHeading);
                }
                double headingError = wrapRange(Math.toDegrees(currentHeading) - Math.toDegrees(desiredHeading), 180);
                if (Math.abs(headingError) > RobotConstants.headingErrorDeadZone) {

                    turn = headingError * RobotConstants.headingPFactor;
                }
                else
                {
                    turn = 0;
                }
            }


            if (!slowMode)
            {
                follower.setTeleOpDrive(forward, strafe, turn, fieldCentric);
            }
            else
            {
                follower.setTeleOpDrive(forward * slowModeMultiplier, strafe * slowModeMultiplier, turn * slowModeMultiplier, fieldCentric);
            }
        }
    }
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
    public void updateShooter()
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
    public void updateBoxBind()
    {
        if (gamepad1.dpadLeftWasPressed() && gamepad1.dpadRightWasPressed())
        {
            if (previousAlliance == RobotConstants.alliance.RED)
            {
                follower.setPose(Waypoints.redBox);
            }
            else if (previousAlliance == RobotConstants.alliance.BLUE)
            {
                follower.setPose(Waypoints.blueBox);
            }
        }
    }
    public void updateTelemetry()
    {
        telemetryA.addData("position", follower.getPose());
        telemetryA.addData("velocity", follower.getVelocity());
        telemetryA.addData("automatedDrive", automatedDrive);
        telemetryA.addData("turretTargetAngle", turretTargetAngle );
        telemetryA.addData("turretAngle", debugturretAngle);
        telemetryA.addData("potVoltage",subSystemShooter.getPotVoltage());
        telemetryA.addData("Turret rotate power", turretRotatePower);
        telemetryA.addData("Turret angle error", currentTurretAngleError);



        telemetryA.update();

    }

    //main loops
    @Override
    public void init()
    {
        initializeHardware();
        updateConstants();

        follower.update();
        telemetryA = new MultipleTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(),this.telemetry);

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
            Pose prevPose = pose;
            setStartPos();

            if (pose != prevPose)
            {
                follower.setStartingPose(pose);
                changeAllianceMultiplier();
            }

            telemetryA.addData("Starting Pose: ", pose);
            telemetryA.addData("Doing loop? ",shouldDoPositionLoop);
            telemetryA.update();
        }
    }
    @Override
    public void start()
    {
        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        follower.startTeleopDrive();
    }
    @Override
    public void loop()
    {
        follower.update();

        updateSlowMode();

        updatePedroDrive();
        updateAutomatedDrive();

        updateBoxBind();

        updateShooter();
        updateTurret();

        updateTelemetry();
    }
}