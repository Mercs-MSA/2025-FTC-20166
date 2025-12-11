package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

//https://hermes.zharel.gay/

@Disabled
@TeleOp
//@Disabled
public class DriveTest extends LinearOpMode {

    private double driveX;
    private double driveY;
    private double driveRotate;
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

    //robotID initializing
    private DigitalChannel limitSwitch;
    private int robotID = 0;

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
    private double FLPower = 0.0;
    private double FRPower = 0.0;
    private double BLPower = 0.0;
    private double BRPower = 0.0;
    private double currentHeading = 0.0;

    private void initializeMotors()
    {
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
    }

    private void initializeIMU()
    {
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection;

        if (robotID == 1) {
            logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
            usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        }
        else
        {
            logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.FORWARD;
            usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;
       }
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        imu.resetYaw();
    }
    public void initializeHardware() {

        initializeMotors();
        initializeIMU();
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
    
    private void updateDriveControls() 
    {
        double angleInRadians;
        double oldDriveX = gamepad1.left_stick_x;
        double oldDriveY = gamepad1.left_stick_y;
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        if(gamepad1.right_bumper)
        {
            angleInRadians = 0;
        }
        else
        {
            angleInRadians = orientation.getYaw(AngleUnit.RADIANS);
        }

        driveX = oldDriveX * Math.cos(angleInRadians) - oldDriveY * Math.sin(angleInRadians);
        driveY = oldDriveX * Math.sin(angleInRadians) + oldDriveY * Math.cos(angleInRadians);
        driveRotate = gamepad1.right_stick_x;
        currentHeading = Math.toDegrees(angleInRadians);
    }

    private void calculateDrivePower()
    {
        FLYPower = -driveY;
        FRYPower = -driveY;
        BLYPower = -driveY;
        BRYPower = -driveY;

        FLXPower = driveX;
        FRXPower = -driveX;
        BLXPower = -driveX;
        BRXPower = driveX;

        FLRPower = driveRotate;
        FRRPower = -driveRotate;
        BLRPower = driveRotate;
        BRRPower = -driveRotate;

        FLPower = (FLXPower + FLYPower + FLRPower);
        FRPower = (FRXPower + FRYPower + FRRPower);
        BLPower = (BLXPower + BLYPower + BLRPower);
        BRPower = (BRXPower + BRYPower + BRRPower);
    }
    public void runOpMode() throws InterruptedException {
       initializeHardware();

        waitForStart();
        while (opModeIsActive())
        {
            updateDriveControls();
            calculateDrivePower();

            setDriveMotors(FLPower, FRPower, BLPower, BRPower);

            telemetry.addData("Robot ID",robotID);
            telemetry.addData("Current Heading ", currentHeading);
            telemetry.addData("Drive X", driveX);
            telemetry.addData("Drive Y", driveY);
            telemetry.addData("Drive Rotate", driveRotate);
            telemetry.addData("FL power ",FLPower);
            telemetry.addData("FL ticks ", m0.getCurrentPosition());
            telemetry.addData("FR power ",FRPower);
            telemetry.addData("FR ticks ", m1.getCurrentPosition());
            telemetry.addData("BL power ",BLPower);
            telemetry.addData("BL ticks ", m2.getCurrentPosition());
            telemetry.addData("BR power ",BRPower);
            telemetry.addData("BR ticks ", m3.getCurrentPosition());

            updateTelemetry(telemetry);
        }
    }
}




