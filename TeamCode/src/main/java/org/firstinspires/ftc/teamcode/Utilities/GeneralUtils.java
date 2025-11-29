package org.firstinspires.ftc.teamcode.Utilities;

public class GeneralUtils
{
    public static double clampRange(double number, double range)
    {
        if (number > range)
        {
            return range;
        }
        else if (number < -range)
        {
            return -range;
        } else
        {
            return number;
        }
    }
    public static double wrapRange(double number, double range)
    {
        while(number >= range)
            number -= (range * 2);
        while(number <= -range)
            number += (range * 2);
        return number;
    }
    public static double getPointsHeading(double x, double y, double xr, double yr)
    {
        double calculatedAngleRads = Math.atan2(y-yr, x-xr);
        double calculatedAngleDegs = Math.toDegrees(calculatedAngleRads);
        //double correctedAngle = calculatedAngleDegs - 90.0;
        return calculatedAngleDegs;
    }

    public static double getPointsDistance(double x, double y, double xr, double yr)
    {
        return Math.hypot(xr-x,yr-y);
    }
}
