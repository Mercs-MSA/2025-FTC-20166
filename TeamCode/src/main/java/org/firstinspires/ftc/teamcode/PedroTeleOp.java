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
@Config
@TeleOp
public class PedroTeleOp extends OpMode {
    private Follower follower;
    private SubSystemShooter subSystemShooter;
    private boolean automatedDrive;
    private boolean shouldDoPositionLoop = false;
    private Telemetry telemetryA;
    private RobotConstants.alliance previousAlliance;
    private Pose pose = null;
    private int joystickMultiplier;
    private Pose goalPose;
    private Supplier<PathChain> pathChain;
    private boolean slowMode = false;
    private double slowModeMultiplier = 0.5;
    public static double turretTargetAngle = 0;
    private double debugturretAngle = 0;
    //private TelemetryManager telemetryP = Panels.getTelemetry();

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
        } else if (number < -range)
        {
            return -range;
        } else
        {
            return number;
        }
    }
    public void updateTurret()
    {
        double turretRotatePower;
        double currentTurretAngle;
        double currentTurretAngleError;

        //this is for turret rotation
        currentTurretAngle = subSystemShooter.getTurretAngle();
        debugturretAngle = currentTurretAngle;
        currentTurretAngleError = currentTurretAngle - turretTargetAngle;
        turretRotatePower = clampRange(RobotConstants.turretRotationP*currentTurretAngleError, RobotConstants.turretMaxPower);
        subSystemShooter.setTurretRotationSpeed(turretRotatePower);
    }
    @Override
    public void init()
    {
        try {
            subSystemShooter = new SubSystemShooter(hardwareMap);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        //Call this once per loop
        follower.update();

        if (!automatedDrive)
        {
            //Make the last parameter false for field-centric
            //In case the drivers want to use a "slowMode" you can scale the vectors

            //This is the normal version to use in the TeleOp
            if (!slowMode) follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * joystickMultiplier,
                    -gamepad1.left_stick_x * joystickMultiplier,
                    -gamepad1.right_stick_x,
                    false // Robot Centric
            );

                //This is how it looks with slowMode on
            else follower.setTeleOpDrive
                    (
                    -gamepad1.left_stick_y * slowModeMultiplier * joystickMultiplier,
                    -gamepad1.left_stick_x * slowModeMultiplier * joystickMultiplier,
                    -gamepad1.right_stick_x * slowModeMultiplier,
                    false // Robot Centric
            );
        }

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

        //Slow Mode
        if (gamepad1.rightBumperWasPressed()) {
            slowMode = !slowMode;
        }

        if (gamepad2.dpadUpWasPressed())
        {
            subSystemShooter.setShooterAngle(RobotConstants.shooterMaxAngle);
        }
        else if (gamepad2.dpadDownWasPressed())
        {
            subSystemShooter.setShooterAngle(RobotConstants.shooterMinAngle);
        }

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
//        //Optional way to change slow mode strength
//        if (gamepad1.xWasPressed()) {
//            slowModeMultiplier += 0.25;
//        }
//
//        //Optional way to change slow mode strength
//        if (gamepad2.yWasPressed()) {
//            slowModeMultiplier -= 0.25;
//        }
        updateTurret();
        telemetryA.addData("position", follower.getPose());
        telemetryA.addData("velocity", follower.getVelocity());
        telemetryA.addData("automatedDrive", automatedDrive);
        telemetryA.addData("turretTargetAngle", turretTargetAngle );
        telemetryA.addData("turretAngle", debugturretAngle);
        telemetryA.addData("potVoltage",subSystemShooter.getPotVoltage());
//        telemetryP.debug(
//                Rectangle(
//                        Point()
//                )
//        );

        telemetryA.update();


    }
}