package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp
//@Disabled
public class MotorTest extends LinearOpMode {

    //Motor demo variables
    private DcMotorEx frontLeftDrive = null;
    private DcMotorEx frontRightDrive = null;
    private DcMotorEx backLeftDrive = null;
    private DcMotorEx backRightDrive = null;

    public void initializeDriveMotors()
    {
        frontLeftDrive = hardwareMap.get(DcMotorEx.class, "FL");
        frontRightDrive = hardwareMap.get(DcMotorEx.class, "FR");
        backLeftDrive = hardwareMap.get(DcMotorEx.class, "BL");
        backRightDrive = hardwareMap.get(DcMotorEx.class, "BR");

        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void runOpMode() throws InterruptedException {
        initializeDriveMotors();

        waitForStart();
        while (opModeIsActive())
        {

            telemetry.addData("FL (0)", frontLeftDrive.getCurrentPosition());
            telemetry.addData("FR (1)", frontRightDrive.getCurrentPosition());
            telemetry.addData("BL (2)", backLeftDrive.getCurrentPosition());
            telemetry.addData("BR (3)", backRightDrive.getCurrentPosition());


            if(gamepad1.x)
                frontLeftDrive.setPower(-gamepad1.left_stick_y);
            else
                frontLeftDrive.setPower(0);

            if(gamepad1.y)
                frontRightDrive.setPower(-gamepad1.left_stick_y);
            else
                frontRightDrive.setPower(0);

            if(gamepad1.a)
                backLeftDrive.setPower(-gamepad1.left_stick_y);
            else
                backLeftDrive.setPower(0);

            if(gamepad1.b)
                backRightDrive.setPower(-gamepad1.left_stick_y);
            else
                backRightDrive.setPower(0);


            updateTelemetry(telemetry);
        }
    }
}




