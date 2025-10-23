//https://github.com/acmerobotics/road-runner-quickstart
//https://ftc-docs.firstinspires.org/en/latest/game_specific_resources/field_coordinate_system/field-coordinate-system.html
//https://gm0.org/en/latest/docs/software/tutorials/bulk-reads.html

package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemRobotID;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemRobotIMU;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemRobotPinpoint;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
import org.firstinspires.ftc.teamcode.Utilities.RobotStatus;

import java.util.List;
//Fl Motor - Motor 0
//FR Motor - Motor 1
//BL Motor - Motor 2
//BR Motor - Motor 3
//Otos sensor - I2C 1
//Pinpoint - I2C 2
//Shooter Tilt Left Servo - Servo 0
//Shooter Tilt Right Servo - Servo 1
//Shooter Fly Wheel Motor - Motor 0 Expansion Hub
//Shooter Turret Rotation - Servo 2
//Turret Position Sensor - Analog Input 0

@TeleOp
//@Disabled
public class DriveTestFaceGoalV2 extends LinearOpMode
{
    private List<LynxModule> allHubs;
    private Telemetry telemetryA;
    private SubSystemShooter subSystemShooter;
    private SubSystemRobotID robotIDSubSystem;
    private int robotID = 0;
    private SubSystemRobotIMU robotIMUSubSystem;
    private SubSystemRobotPinpoint robotPinpointSubSystem;
    private double driveTranslateX;
    private double driveTranslateY;
    SparkFunOTOS myOtos;

    //Motor demo variables
    private DcMotorEx m0 = null;
    private DcMotorEx m1 = null;
    private DcMotorEx m2 = null;
    private DcMotorEx m3 = null;
    private IMU imu;

    private double FLYPower = 0.0;
    private double FRYPower = 0.0;
    private double BLYPower = 0.0;
    private double BRYPower = 0.0;
    private double FLXPower = 0.0;
    private double FRXPower = 0.0;
    private double BLXPower = 0.0;
    private double BRXPower = 0.0;

    private double FLRPower = 0.0;
    private double FRRPower = 0.0;
    private double BLRPower = 0.0;
    private double BRRPower = 0.0;

    private double driveRotate;
    private double headingError = 0.0;
    boolean faceGoal = false;
    private double desiredHeading;
    private boolean currentlyTurning = false;

    RobotStatus robotPose;


    //public Point startPose

    public void initializeHardware() throws InterruptedException {
        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetryA.update();

        robotIDSubSystem = new SubSystemRobotID(hardwareMap);
        robotID = robotIDSubSystem.getRobotID();

        subSystemShooter = new SubSystemShooter(hardwareMap);

        RobotConstants.setRobotID(robotID);
        Waypoints.setRobotID(robotID);

        robotIMUSubSystem = new SubSystemRobotIMU(hardwareMap, robotID);

        myOtos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");
        myOtos.setPosition(Waypoints.startPointMiddleBottom);
        myOtos.calibrateImu();
        myOtos.resetTracking();

        robotPinpointSubSystem = new SubSystemRobotPinpoint(hardwareMap, robotID);

        Pose2D pinpointPos = robotPinpointSubSystem.getPinpointPos();
        robotPose = new RobotStatus(pinpointPos.getX(DistanceUnit.INCH), pinpointPos.getY(DistanceUnit.INCH), pinpointPos.getHeading(AngleUnit.DEGREES), true);
        //SparkFunOTOS.Pose2D offset = new SparkFunOTOS.Pose2D(0, 0, 90);
        //myOtos.setOffset(offset);

        m0 = hardwareMap.get(DcMotorEx.class, "FL");
        m1 = hardwareMap.get(DcMotorEx.class, "FR");
        m2 = hardwareMap.get(DcMotorEx.class, "BL");
        m3 = hardwareMap.get(DcMotorEx.class, "BR");

        m0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (robotID == 0) //robot A
        {
            m1.setDirection(DcMotorSimple.Direction.REVERSE);
            m3.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        imu = hardwareMap.get(IMU.class, "imu");
        imu.resetYaw();

        //Change the way hardware is read so that we only read things once per loop
        //Need to make sure we actually DO reset the cached values though !!!
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    public void updateRobotPose()
    {
        robotPinpointSubSystem.updatePinpoint();
        Pose2D pinpointPos = robotPinpointSubSystem.getPinpointPos();
        robotPose = new RobotStatus(pinpointPos.getX(DistanceUnit.INCH), pinpointPos.getY(DistanceUnit.INCH), pinpointPos.getHeading(AngleUnit.DEGREES), true);
    }


    private void setDriveMotors(double FL, double FR, double BL, double BR)
    {
        double greatest = Math.max(Math.max(FL, FR), Math.max(BL, BR));
        if (greatest > 1.0)
        {
            FL = FL/greatest;
            FR = FR/greatest;
            BL = BL/greatest;
            BR = BR/greatest;
        }
        m0.setPower(FL);
        m1.setPower(FR);
        m2.setPower(BL);
        m3.setPower(BR);

    }

    public static double getPointsHeading(double x, double y, double xr, double yr)
    {
        double calculatedAngleRads = Math.atan2(x - xr, y - yr);
        double calculatedAngleDegs = Math.toDegrees(calculatedAngleRads);
        //double correctedAngle = calculatedAngleDegs - 90.0;
        return -1 * calculatedAngleDegs;
    }

    public double wrapRange(double number, double range)
    {
        while(number >= range)
            number -= (range * 2);
        while(number <= -range)
            number += (range * 2);
        return number;
    }
    public double calculateHeadingError(double actualHeading, double desiredHeading)
    {
        /*
        double  error = actualHeading - desiredHeading;
        if (error > 180)
        {
            error -= 360;
        } else if (error < -180)
        {
            error += 360;
        }
        return error;
         */
        return wrapRange(actualHeading - desiredHeading, 180.0);
    }

    private void updateDesiredHeading()
    {
        double joystickRotation;

        joystickRotation = gamepad1.right_stick_x;
        faceGoal = gamepad1.left_bumper;

        if (faceGoal)
        {
            desiredHeading = getPointsHeading(Waypoints.blueGoalPointx, Waypoints.blueGoalPointy, robotPose.getX(), robotPose.getY());
        }
        else if (Math.abs(joystickRotation) >= RobotConstants.joystickRotateDeadband)
        {
            //Note that we are turning
            currentlyTurning = true;
            //Set the desired heading to be a factor of the joystick position
            //This will infer an error that then gets scaled and used as a turning 'power'
            desiredHeading = wrapRange(robotPose.getThetaDegrees() + (joystickRotation * 90), 180);
        }
        else
        {
            //Have we JUST stopped turning?
            if (currentlyTurning)
            {
                //Yes, note where we are now and make that our desired heading
                currentlyTurning = false;
                desiredHeading = robotPose.getThetaDegrees();
            }
        }
    }
    private void updateDriveControls()
    {
        double joystickX;
        double joystickY;
        double translateAdjustAngleRadians;
        double currentRobotHeadingRadians;
        boolean forceRobotCentric;

        //Capture some information we will need later
        joystickX = gamepad1.left_stick_x;
        joystickY = gamepad1.left_stick_y;
        forceRobotCentric = gamepad1.right_bumper;

        //Adjust translation factors if not robot centric
        if(forceRobotCentric)
            translateAdjustAngleRadians = 0;
        else
            translateAdjustAngleRadians = robotPose.getThetaRadians();

        // Applying Trig for field centric driving
        driveTranslateX = joystickX * Math.cos(translateAdjustAngleRadians) - joystickY * Math.sin(translateAdjustAngleRadians);
        driveTranslateY = joystickX * Math.sin(translateAdjustAngleRadians) + joystickY * Math.cos(translateAdjustAngleRadians);

        headingError = wrapRange(robotPose.getThetaDegrees() -  desiredHeading, 180);
        if (Math.abs(headingError) > RobotConstants.headingErrorDeadZone)
            driveRotate = headingError * RobotConstants.headingPFactor;
        else
            driveRotate = 0.0;
    }

    private void calculateDrivePower()
    {
        // Setting the power for forwards and backwards
        FLYPower = -driveTranslateY;
        FRYPower = -driveTranslateY;
        BLYPower = -driveTranslateY;
        BRYPower = -driveTranslateY;

        //Setting power for strafing
        FLXPower = driveTranslateX;
        FRXPower = -driveTranslateX;
        BLXPower = -driveTranslateX;
        BRXPower = driveTranslateX;

        //Setting rotational power
        FLRPower = driveRotate;
        FRRPower = -driveRotate;
        BLRPower = driveRotate;
        BRRPower = -driveRotate;
    }
    private void updateTelemetryA()
    {
        //Robot status
        telemetryA.addData("Robot ID: ", robotIDSubSystem.getRobotID());//Pinpoint
        telemetryA.addData("Pinpoint X coordinate", robotPose.getX());
        telemetryA.addData("Pinpoint Y coordinate", robotPose.getY());
        telemetryA.addLine();

        telemetryA.addData("Pinpoint heading", robotPose.getThetaDegrees());
        telemetryA.addData("OTOS Heading", myOtos.getPosition().h);
        telemetryA.addData("IMU heading", robotIMUSubSystem.getHeadingDegrees());
        telemetryA.addLine();

        //Heading Correction
        telemetryA.addData("Desired Heading", desiredHeading);
        telemetryA.addData("Heading Error", headingError);
//        telemetryA.addData("Joystick Heading", joystickHeading);
//        telemetryA.addData("Goal Heading", goalHeading);
        telemetryA.addLine();
        telemetryA.addData("Should Face Goal:", faceGoal);
        telemetryA.addData("Currently Turning: ", currentlyTurning);
        updateTelemetry(telemetryA);

    }

public void test()
    {
        if(gamepad2.y)
        {
            subSystemShooter.shooterSetAngle(RobotConstants.shooterMaxAngle);
        }
        else if(gamepad2.a)
        {
            subSystemShooter.shooterSetAngle(RobotConstants.shooterMinAngle);
        }
    }

    private void clearHubCache()
    {
        for (LynxModule hub : allHubs) 
            hub.clearBulkCache();
    }

    public void runOpMode() throws InterruptedException
    {
        initializeHardware();
        clearHubCache();
        waitForStart();
        while (opModeIsActive())
        {
            clearHubCache();
            updateRobotPose();
            updateDriveControls();
            calculateDrivePower();
            setDriveMotors((FLXPower + FLYPower + FLRPower), (FRXPower + FRYPower + FRRPower), (BLXPower + BLYPower + BLRPower), (BRXPower + BRYPower + BRRPower));
            updateTelemetryA();
            //test();
        }
    }

}
