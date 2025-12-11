package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class SubSystemRobotIMU {

    RevHubOrientationOnRobot.LogoFacingDirection logoDirection;
    RevHubOrientationOnRobot.UsbFacingDirection usbDirection;
    RevHubOrientationOnRobot orientationOnRobot;
    private IMU imu;
    public SubSystemRobotIMU(HardwareMap hardwareMap, int robotID) throws InterruptedException {

        if (robotID == 0) {
            logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
            usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        }
        else
        {
            logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.FORWARD;
            usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;
        }

        imu = hardwareMap.get(IMU.class, "imu");

        orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

    }
    public void resetHeading()
    {
        imu.resetYaw();
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
    }
