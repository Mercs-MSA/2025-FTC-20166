package org.firstinspires.ftc.teamcode.Utilities;

public class LERP
{
    private boolean hasCap = false;
    private double x1 = 0.0;
    private double y1 = 0.0;
    private double x2 = 0.0;
    private double y2 = 0.0;
    private double slope = 0.0;
    private double intercept = 0.0;

    public LERP(double x1, double y1, double x2, double y2, boolean cap)
    {
        //Checks if line should have a cap or not
        hasCap = cap;

        //Point coordinates
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;

        //Calculating slope and intercept
        slope = (this.y2 - this.y1) / (this.x2 - this.x1);
        intercept = this.y1 - slope * this.x1;
    }

    public double interpolated(double x)
    {
        //Checking if the input should be capped.
        if (hasCap)
        {
            if (x > x2)
            {
                x = x2;
            }
            else if (x < x1)
            {
                x = x1;
            }
        }

        //Calculating y using slope-intercept form using updated x value.
        double result = slope * x + intercept;

        return result;
    }
}