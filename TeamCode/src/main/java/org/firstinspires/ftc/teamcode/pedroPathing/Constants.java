package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotConstants;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants();

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    private static MecanumConstants initializeMecanumConstants(RobotConstants robotConstants)
    {
        MecanumConstants driveConstants = new MecanumConstants()
                .maxPower(1)
                .rightFrontMotorName("FR")
                .rightRearMotorName("BR")
                .leftRearMotorName("BL")
                .leftFrontMotorName("FL")
                .leftFrontMotorDirection(robotConstants.leftSideDirection)
                .leftRearMotorDirection(robotConstants.leftSideDirection)
                .rightFrontMotorDirection(robotConstants.rightSideDirection)
                .rightRearMotorDirection(robotConstants.rightSideDirection)
                .useBrakeModeInTeleOp(false);
        return driveConstants;
    }

    public static Follower createFollower(HardwareMap hardwareMap, RobotConstants robotConstants) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pinpointLocalizer(initializeLocalizerConstants(robotConstants))
                .mecanumDrivetrain(initializeMecanumConstants(robotConstants))
                .pathConstraints(pathConstraints)
                .build();
    }

    private static PinpointConstants initializeLocalizerConstants(RobotConstants robotConstants)
    {
        PinpointConstants localizerConstants = new PinpointConstants()
                .forwardPodY(robotConstants.podY)
                .strafePodX(robotConstants.podX)
                .distanceUnit(DistanceUnit.INCH)
                .hardwareMapName("pinpoint")
                .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
                .forwardEncoderDirection(robotConstants.encoderForward)
                .strafeEncoderDirection(robotConstants.encoderStrafe);
        return localizerConstants;
    }
}
