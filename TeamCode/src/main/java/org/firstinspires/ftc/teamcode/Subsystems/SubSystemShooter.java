package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Utilities.GeneralUtils;
import org.firstinspires.ftc.teamcode.Utilities.LERP;
import org.firstinspires.ftc.teamcode.Waypoints;

public class SubSystemShooter {
    private LERP shooterTiltLeftLERP;
    private RobotConstants robotConstants;
    private LERP shooterTiltRightLERP;
    private LERP shooterAngleLERP;
    private LERP shooterVelocityLERP;
    private Servo shooterTiltLeft;
    private Servo shooterTiltRight;
    private Servo transferArm;
    private Servo gateServo;
    private DcMotorEx shooterFlyWheel;
    private DcMotorEx intakeMotor;
    private CRServo turretRotation;
    private CRServo transferServo1;
//    private RobotConstants.alliance alliance;
//    private Pose goalPose;
    private CRServo transferServo2;
    private CRServo agitator;
    private AnalogInput turretPositionSensor;
    private double turretDelta;
    PIDFCoefficients pidStore;
    private double turretRotatePower;
    private double currentTurretAngleError;
    private double shooterTargetVelocity;
    private double intakeShooterVelocity;
    private Pose goalPose;
    public SubSystemShooter(HardwareMap hardwareMap, RobotConstants robotConstants) throws InterruptedException {
        this.robotConstants = robotConstants;

        shooterTiltLeftLERP = new LERP(robotConstants.shooterMinAngle,robotConstants.leftMinAngleSetting,robotConstants.shooterMaxAngle,robotConstants.leftMaxAngleSetting,true);
        shooterTiltRightLERP = new LERP(robotConstants.shooterMinAngle,robotConstants.rightMinAngleSetting,robotConstants.shooterMaxAngle,robotConstants.rightMaxAngleSetting,true);
        shooterAngleLERP = new LERP(robotConstants.farDistance,robotConstants.farShooterAngle,robotConstants.nearDistance,robotConstants.nearShooterAngle,true);
        shooterVelocityLERP = new LERP(robotConstants.farDistance,robotConstants.farVelocity,robotConstants.nearDistance,robotConstants.nearVelocity,false);

        transferArm = hardwareMap.get(Servo.class, "lift");

        agitator = hardwareMap.get(CRServo.class, "BTS");

        shooterTiltLeft = hardwareMap.get(Servo.class, "shooterTiltLeft");//shooterTiltLeft
        shooterTiltRight = hardwareMap.get(Servo.class, "shooterTiltRight");//shooterTiltRight

        gateServo = hardwareMap.get(Servo.class, "gate");

        transferServo1 = hardwareMap.get(CRServo.class, "transfer1");
        transferServo2 = hardwareMap.get(CRServo.class, "transfer2");

        turretRotation = hardwareMap.get(CRServo.class, "turretRotation");

        shooterFlyWheel = hardwareMap.get(DcMotorEx.class, "shooterFlyWheel");
        pidStore = shooterFlyWheel.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
        pidStore.d = 0.005;
        pidStore.p = 200;
        shooterFlyWheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidStore);

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");

        turretPositionSensor = hardwareMap.get(AnalogInput.class, "turretPositionSensor");

        turretDelta = 0;
        shooterTargetVelocity = 0;

    }

    public void setTurretRotationSpeed(double power)
    {
        turretRotation.setPower(power);
    }
//    public void setAlliance(RobotConstants.alliance alliance)
//    {
//        this.alliance = alliance;
//        if (alliance == RobotConstants.alliance.RED)
//        {
//            goalPose = Waypoints.redShooterPoint;
//        }
//        else if (alliance == RobotConstants.alliance.BLUE)
//        {
//            goalPose = Waypoints.blueShooterPoint;
//        }
//    }


    //Edited in VISUAL STUDIO CODE by Steve :)
    private double [][] turretSensorCorrectionTable =
            {
//                    { -180,  -135,   -90,  -45,     0,    45,    90,   135,   180}, //Turret angle
//                    {2.465, 1.984, 1.615, 1.34, 1.141, 0.915, 0.721, 0.536, 0.308}, //ROBOT 0

                    { -180,  -135,   -90,   -45,     0,    45,    90,   135,   180},
                    {2.553, 2.032, 1.671, 1.379, 1.135, 0.931, 0.743, 0.556, 0.347},
                    //Measured voltage. MUST be ordered high to low. Need to update these to 'real life'
            };//Should move to robot constants since may be different for each robot

    public double interpolateTurretAngle(double Voltage)
    {
        int tableWidth = turretSensorCorrectionTable[0].length;//Number of columns in the array. (Technically the number of columns in the first row since Java can have variable sized rows)
        double lowerA = 0; //Declare here so lifetime lasts to the end of the method
        double lowerV; //Declare here so lifetime lasts to the end of the method

        for (int i = 0; i < tableWidth; i++)
        {
            lowerA = turretSensorCorrectionTable[0][i];//Get the parameters from the table and use as the segment lower voltage points
            lowerV = turretSensorCorrectionTable[1][i];
            if (Voltage >= lowerV)
            {
                if (i == 0)//Voltage is higher than the highest entry in the table so maxed out
                {
                    return lowerA;
                }
                else
                {
                    double upperA = turretSensorCorrectionTable[0][i - 1];//Get the parameters for the previous table entry which is the higher voltage segment
                    double upperV = turretSensorCorrectionTable[1][i - 1];
                    LERP interpolator = new LERP(lowerV, lowerA, upperV, upperA, true);//Create a LERP to interpolate between the lower voltage and upper voltage
                    return interpolator.interpolated(Voltage);//Actually interpolate and return the calculated voltage
                }
            }
        }
        //If we make it here then the voltage is less than the lowest voltage entry in the table
        return lowerA;
    }
    public double getTurretAngle()
    {
        return interpolateTurretAngle(turretPositionSensor.getVoltage());
        /*
        double volt = turretPositionSensor.getVoltage();
        double mid = 1.13; //(2.512 + .327) / 2;
        double angle;
        if (volt > mid)
        {
            return turretInterpolator1.interpolated(volt);
        } else
        {
            return turretInterpolator2.interpolated(volt);
        }
         */
    }
    public void setAgitator(double power)
    {
        agitator.setPower(power);
    }
    public double getPotVoltage()
    {
        return turretPositionSensor.getVoltage();
    }
    public void setLiftArm(boolean enabled)
    {
        if (enabled)
        {
            transferArm.setPosition(robotConstants.liftArmUpAngle);
            gateServo.setPosition(robotConstants.gateUpAngle);
        }
        else
        {
            transferArm.setPosition(robotConstants.liftArmDownAngle);
            gateServo.setPosition(robotConstants.gateDownAngle);

        }
    }
    public void setGoalPose(Pose goalPose)

    {
        this.goalPose = goalPose;
    }
    private void updateTurretHeading(Pose robotPos)
    {
        double currentTurretAngle;
        double goalHeading = GeneralUtils.getPointsHeading(goalPose.getX(), goalPose.getY(), robotPos.getX(), robotPos.getY()) - 180;

        turretDelta = GeneralUtils.wrapRange(goalHeading - Math.toDegrees(robotPos.getHeading()), 180);

        //this is for turret rotation.
        currentTurretAngle = getTurretAngle();
        currentTurretAngleError =  turretDelta - currentTurretAngle;
        turretRotatePower = GeneralUtils.clampRange(RobotConstants.turretRotationP*currentTurretAngleError, RobotConstants.turretMaxPower);
        setTurretRotationSpeed(turretRotatePower);
    }

    private void updateTurretAngle(Pose robotPos, double dis)
    {
        setShooterAngle(shooterAngleLERP.interpolated(dis));
    }

    public void updateTurret(Pose robotPos, double dis)
    {
        updateTurretHeading(robotPos);
        updateTurretAngle(robotPos, dis);
        updateTurretFlywheel(dis);
    }

    public void updateTurretFlywheel(double distance)
    {
        shooterTargetVelocity = shooterVelocityLERP.interpolated(distance);
        setShooterSpeed(shooterTargetVelocity);
    }
    public double getTurretDelta()
    {
        return turretDelta;
    }
    public void setShooterAngle(double tiltAngle)
    {
        shooterTiltLeft.setPosition(shooterTiltLeftLERP.interpolated(tiltAngle));
        shooterTiltRight.setPosition(shooterTiltRightLERP.interpolated(tiltAngle));
    }

    public void setShooterSpeed(double velocity)
    {
        shooterFlyWheel.setVelocity(velocity);
    }

    public void setIntakeSpeed(double velocity)
    {
        this.intakeShooterVelocity = velocity;
        intakeMotor.setVelocity(velocity);
    }
    public double getTurretRotatePower()
    {
        return turretRotatePower;
    }
    public void setTransfer(boolean enabled)
    {
        if (enabled)
        {
            transferServo1.setPower(RobotConstants.turretMaxPower);
            transferServo2.setPower(-RobotConstants.turretMaxPower);
        }
        else
        {
            transferServo1.setPower(0);
            transferServo2.setPower(0);
        }
    }
    public double getTurretError()
    {
        return currentTurretAngleError;
    }
    public double getShooterVelocity()
    {
        return shooterFlyWheel.getVelocity();
    }
    public double getShooterTargetVelocity()
    {
        return shooterTargetVelocity;
    }
    public double getIntakeVelocity()
    {
        return intakeMotor.getVelocity();
    }
    public double getIntakeTargetVelocity()
    {
        return intakeShooterVelocity;
    }
    public PIDFCoefficients getCoefficients()
    {
        return pidStore;
    }
    public void setTurretTargetVelocity(double in)
    {
        shooterTargetVelocity = in;
    }
}
