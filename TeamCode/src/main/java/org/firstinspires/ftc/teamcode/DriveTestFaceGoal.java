package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@TeleOp
//@Disabled
public class DriveTestFaceGoal extends LinearOpMode
{
    private Telemetry telemetryA;
    private double driveX;
    private double driveY;
    SparkFunOTOS myOtos;

    //Motor demo variables
    private DcMotorEx m0 = null;
    private DcMotorEx m1 = null;
    private DcMotorEx m2 = null;
    private DcMotorEx m3 = null;
    private Servo servo1;
    private Servo servo2;
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


    SparkFunOTOS.Pose2D robotPose;

    GoBildaPinpointDriver pinpoint;


    public static SparkFunOTOS.Pose2D startPointMiddleBottom = new SparkFunOTOS.Pose2D(0, -60, Math.toRadians(0));

    private double redGoalPointx = 72;
    private double redGoalPointy = 72;

    private double blueGoalPointx = -72;
    private double blueGoalPointy = 72;
    private final double errorDeadZone = 5.0;
    private boolean currentlyTurning = false;
    private double joystickHeading;

    //public Point startPose

    public void initializeHardware()
    {
        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetryA.update();

        //servo1 = hardwareMap.get(Servo.class, "servo1");
        //servo2 = hardwareMap.get(Servo.class, "servo2");

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,"pinpoint");

        myOtos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");
        myOtos.setPosition(startPointMiddleBottom);
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

        imu = hardwareMap.get(IMU.class, "imu");
        imu.resetYaw();
    }

    public void updateRobotPose()
    {
        robotPose = myOtos.getPosition();
    }

    public double getHeadingDegrees()
    {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
        //robotPos = myOtos.getPosition();
        //return robotPos.h;
    }
    public double getHeadingRadians()
    {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.RADIANS);
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

    public static double headingError(double actualHeading, double desiredHeading)
    {
        double error = actualHeading - desiredHeading;
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
        double desiredHeading;
        double goalHeading;

        //Robot Centric or Field Centric switching
        if(gamepad1.right_bumper)
        {
            angleInRadians = 0;
        }
        else
        {
            angleInRadians = getHeadingRadians();
        }

        // Applying Trig for field centric driving
        driveX = oldDriveX * Math.cos(angleInRadians) - oldDriveY * Math.sin(angleInRadians);
        driveY = oldDriveX * Math.sin(angleInRadians) + oldDriveY * Math.cos(angleInRadians);
        driveRotate = gamepad1.right_stick_x;

        //setting josytick heading if rotating
        if (currentlyTurning)
        {
            joystickHeading = getHeadingDegrees();
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
        goalHeading = getPointsHeading(blueGoalPointx, blueGoalPointy, robotPose.x, robotPose.y);
        if (gamepad1.left_bumper)
        {
            desiredHeading = goalHeading;
        } else
        {
            desiredHeading = joystickHeading;
        }

        //Calculating and applying heading error
        double error = headingError(getHeadingDegrees(), desiredHeading);
        if (Math.abs(error) > errorDeadZone)
        {
            driveRotate = error * headingPFactor;
        }
        //displaying telemetry on the driver hub
        telemetryA.addData("error", error);
        telemetryA.addData("Desired Heading", desiredHeading);
        telemetryA.addData("Joystick Heading", joystickHeading);
        telemetryA.addData("Goal Heading", goalHeading);

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

    public void runOpMode() throws InterruptedException
    {
       initializeHardware();
        waitForStart();
        while (opModeIsActive())
        {
 /*           telemetryA.addData("Motor 0", m0.getCurrentPosition());
            telemetryA.addData("Motor 1", m1.getCurrentPosition());
            telemetryA.addData("Motor 2", m2.getCurrentPosition());
            telemetryA.addData("Motor 3", m3.getCurrentPosition());
            telemetryA.addData("Motor 4", m4.getCurrentPosition());
            telemetryA.addData("Motor 5", m5.getCurrentPosition());
            telemetryA.addData("Motor 6", m6.getCurrentPosition());
            telemetryA.addData("Motor 7", m7.getCurrentPosition());

  */
//            telemetryA.addData("IMU X", orientation.getYaw(AngleUnit.DEGREES));
//            telemetryA.addData("IMU Y", orientation.getPitch(AngleUnit.DEGREES));
//            telemetryA.addData("IMU Z", orientation.getRoll(AngleUnit.DEGREES));
            updateRobotPose();
            updateDriveControls();
            calculateDrivePower();

            //FLXPower.setVelocity(1000);
            //Sets target velocity to 1000 ticks per second
            //m0.setVelocity();
            setDriveMotors((FLXPower + FLYPower + FLRPower), (FRXPower + FRYPower + FRRPower), (BLXPower + BLYPower + BLRPower), (BRXPower + BRYPower + BRRPower));
            //telemetryA.addData("Robot ID",  )
            telemetryA.addData("X coordinate", robotPose.x);
            telemetryA.addData("Y coordinate", robotPose.y);
            telemetryA.addData("Heading angle", getHeadingDegrees());
            telemetry.addData("Status", pinpoint.getDeviceStatus());
            telemetry.addData("X offset", pinpoint.getXOffset(DistanceUnit.MM));
            telemetry.addData("Y offset", pinpoint.getYOffset(DistanceUnit.MM));
            telemetry.addData("Device Version Number:", pinpoint.getDeviceVersion());
            //telemetry.addData("Heading Scalar", Point.getYawScalar());


            updateTelemetry(telemetryA);
        }
    }
}




