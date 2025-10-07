public class LERP
{
    private boolean capped = false;
    private double globx1 = 0.0;
    private double globy1 = 0.0;
    private double globx2 = 0.0;
    private double globy2 = 0.0;
    private double slope = 0.0;
    private double intercept = 0.0;

    public LERP(double x1, double y1, double x2, double y2, boolean cap)
    {
        capped = cap;
        globx1 = x1;
        globx2 = x2;
        globy1 = y1;
        globy2 = y2;
        slope = (globy2 - globy1) / (globx2 - globx1);
        intercept = globy1 - slope * globx1;
    }

    public double interpolated(double x)
    {
        double calc = slope * x + intercept;
        if (capped)
        {
            if (calc > globx2)
            {
                calc = globx2;
            } else if (calc < globx1)
            {
                calc = globx1;
            }
        }
        return calc;
    }
}