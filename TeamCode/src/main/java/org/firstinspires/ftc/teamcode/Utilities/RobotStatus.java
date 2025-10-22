package org.firstinspires.ftc.teamcode.Utilities;

public class RobotStatus
{
    private double theta;
    private double x;
    private double y;

    public RobotStatus(double x, double y, double theta, boolean inDegrees)
    {
        this.x = x;
        this.y = y;
        if (inDegrees)
        {
            this.theta = theta;
        }
        else
        {
            this.theta = Math.toDegrees(theta);
        }
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getThetaDegrees() {
        return theta;
    }

    public double getThetaRadians() {
        return Math.toRadians(theta);
    }
}
