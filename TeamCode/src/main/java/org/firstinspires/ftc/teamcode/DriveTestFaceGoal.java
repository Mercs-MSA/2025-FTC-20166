package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemRobotID;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemRobotIMU;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
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
public class DriveTestFaceGoal extends LinearOpMode
{
    private Telemetry telemetryA;
    private SubSystemShooter subSystemShooter;
    private SubSystemRobotID robotIDSubSystem;
    private int robotID = 0;
    private SubSystemRobotIMU robotIMUSubSystem;

    private double driveX;
    private double driveY;
    SparkFunOTOS myOtos;

    //Motor demo variables
    private DcMotorEx m0 = null;
    private DcMotorEx m1 = null;
    private DcMotorEx m2 = null;
    private DcMotorEx m3 = null;

//    private DcMotorEx m4 = null;
//    private DcMotorEx m5 = null;
//    private DcMotorEx m6 = null;
//    private DcMotorEx m7 = null;
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
    private double desiredHeading;
    private double goalHeading;
    private double error;
    private boolean currentlyTurning = false;
    private double joystickHeading;

    SparkFunOTOS.Pose2D robotPose;

    GoBildaPinpointDriver pinpoint;


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


        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,"pinpoint");

        myOtos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");
        myOtos.setPosition(Waypoints.startPointMiddleBottom);
        //SparkFunOTOS.Pose2D offset = new SparkFunOTOS.Pose2D(0, 0, 90);
        //myOtos.setOffset(offset);

        m0 = hardwareMap.get(DcMotorEx.class, "FL");
        m1 = hardwareMap.get(DcMotorEx.class, "FR");
        m2 = hardwareMap.get(DcMotorEx.class, "BL");
        m3 = hardwareMap.get(DcMotorEx.class, "BR");
//        m4 = hardwareMap.get(DcMotorEx.class, "M4");
//        m5 = hardwareMap.get(DcMotorEx.class, "M5");
//        m6 = hardwareMap.get(DcMotorEx.class, "M6");
//        m7 = hardwareMap.get(DcMotorEx.class, "M7");

        m0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        m5.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        m6.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        m7.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (robotID == 1)
        {
            m1.setDirection(DcMotorSimple.Direction.REVERSE);
            m3.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        imu = hardwareMap.get(IMU.class, "imu");
        imu.resetYaw();
    }

    public void updateRobotPose()
    {
        robotPose = myOtos.getPosition();
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

    public double headingError(double actualHeading, double desiredHeading)
    {
        error = actualHeading - desiredHeading;
        if (error > 180)
        {
            error -= 360;
        } else if (error < -180)
        {
            error += 360;
        }
        return error;
    }

    private void updateDriveControls() 
    {
        double angleInRadians;
        double oldDriveX = gamepad1.left_stick_x;
        double oldDriveY = gamepad1.left_stick_y;
        double headingPFactor = (1.0 / 90.0);


        //Robot Centric or Field Centric switching
        if(gamepad1.right_bumper)
        {
            angleInRadians = 0;
        }
        else
        {
            angleInRadians = robotIMUSubSystem.getHeadingRadians();
        }

        // Applying Trig for field centric driving
        driveX = oldDriveX * Math.cos(angleInRadians) - oldDriveY * Math.sin(angleInRadians);
        driveY = oldDriveX * Math.sin(angleInRadians) + oldDriveY * Math.cos(angleInRadians);
        driveRotate = gamepad1.right_stick_x;

        //setting josytick heading if rotating
        if (currentlyTurning)
        {
            joystickHeading = robotIMUSubSystem.getHeadingDegrees();
        }

        //Setting dead zone
        if (Math.abs(driveRotate) < .1)
        {
            driveRotate = 0;
            currentlyTurning = false;
        }
        else
        {
            currentlyTurning = true;
        }

        //setting heading to goal heading or regular heading
        goalHeading = getPointsHeading(Waypoints.blueGoalPointx, Waypoints.blueGoalPointy, robotPose.x, robotPose.y);
        if (gamepad1.left_bumper)
        {
            desiredHeading = goalHeading;
        } else
        {
            desiredHeading = joystickHeading;
        }

        //Calculating and applying heading error
        double error = headingError(robotIMUSubSystem.getHeadingDegrees(), desiredHeading);
        if (Math.abs(error) > RobotConstants.errorDeadZone)
        {
            driveRotate = error * headingPFactor;
        }
        //displaying telemetry on the driver hub


    }

    private void calculateDrivePower()
    {
        // Setting the power for forwards and backwards
        FLYPower = -driveY;
        FRYPower = -driveY;
        BLYPower = -driveY;
        BRYPower = -driveY;

        //Setting power for strafing
        FLXPower = driveX;
        FRXPower = -driveX;
        BLXPower = -driveX;
        BRXPower = driveX;

        //Setting rotational power
        FLRPower = driveRotate; //gamepad1.right_stick_x;
        FRRPower = -driveRotate; //-gamepad1.right_stick_x;
        BLRPower = driveRotate; //gamepad1.right_stick_x;
        BRRPower = -driveRotate; //-gamepad1.right_stick_x;
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

    public void runOpMode() throws InterruptedException
    {
       initializeHardware();
        waitForStart();
        while (opModeIsActive())
        {
            updateRobotPose();
            updateDriveControls();
            calculateDrivePower();

            setDriveMotors((FLXPower + FLYPower + FLRPower), (FRXPower + FRYPower + FRRPower), (BLXPower + BLYPower + BLRPower), (BRXPower + BRYPower + BRRPower));
            updateTelemetryA();
            test();
        }
    }

    private void updateTelemetryA()
    {
        //Robot status
        telemetryA.addData("X coordinate", robotPose.x);
        telemetryA.addData("Y coordinate", robotPose.y);
        telemetryA.addData("Heading angle", robotIMUSubSystem.getHeadingDegrees());
        telemetryA.addData("Robot ID: ", RobotConstants.robotId);

        //Heading Correction
        telemetryA.addData("error", error);
        telemetryA.addData("Desired Heading", desiredHeading);
        telemetryA.addData("Joystick Heading", joystickHeading);
        telemetryA.addData("Goal Heading", goalHeading);

        //Pinpoint
        //telemetryA.addData("Status", pinpoint.getDeviceStatus());
        //telemetryA.addData("X offset", pinpoint.getXOffset(DistanceUnit.MM));
        //telemetryA.addData("Y offset", pinpoint.getYOffset(DistanceUnit.MM));
        //telemetryA.addData("Device Version Number:", pinpoint.getDeviceVersion());
        //telemetryA.addData("Heading Scalar", Point.getYawScalar());

        updateTelemetry(telemetryA);

    }
}




