ShowLimits = false;
ShowShooter = true;
ShowRamp = true;
LimitBounds = [18, 18, 18];
ShooterAngle = -40;
TurretAngle  = 15;
IndexerBallOffset = 3;
IndexerCenterOffset = 3;
BallD  = 5;
WheelD = 3.75;

module Stop(){}

$fn = 150;
ChannelWidth = 5.5;

module Hood(Angle = 0)
{
  //Hood
  rotate(Angle, [0, 1, 0])
    color([0.2, 0.2, 0.7, 0.7])
      difference()
      {
        intersection()
        {
          //Ramp
          rotate(90, [1, 0,0])
            difference()
            {    
              cylinder(d = 15.3 - .2, h = ChannelWidth + .3, center = true);
              cylinder(d = 15 - .2, h = ChannelWidth, center = true);
            }
            translate([5, 0, -5])
              cube([10, 10, 10], center = true);
        }
        rotate(40, [0, 1, 0])
            translate([5, 0, 5])
              cube([10, 10, 10], center = true);
      }
}

module Limits()
{
  //Limits
  color([0.2, 0.8, 0.1, 0.2])
    translate([0, 0, 15/2])
      cube(LimitBounds, center = true);
}


module Flywheel()
{
  //Flywheel
  rotate(90, [1, 0,0])
    cylinder(d = 5, h = 4, center = true);
}

module LaunchGuide()
{
  //Guide
  difference()
  {
    intersection()
    {
      //Ramp
      rotate(90, [1, 0,0])
        difference()
        {    
          cylinder(d = 15.3, h = ChannelWidth, center = true);
          cylinder(d = 15, h = ChannelWidth + .01, center = true);
        }
        translate([5, 0, -10])
          cube([10, 10, 10], center = true);
    }
    //Ball pushup clearance
    translate([-1, 0, -10])
    cylinder(d = 5.5, h = 10);
  }
}

module LaunchBall()
{
  //Launch ball
  color("Violet")
    rotate(90, [0, 1, 0])
      translate([5, 0, 0])
        sphere(d = 5);
}


module RotateObject(Radius, Angle, ShowLever = true)
{
  //Rotate the object around a point 'Radius' from the zero referenced point of the child object
  translate([Radius, 0, 0])
    rotate(Angle, [0, 1, 0])
      translate([-Radius, 0, 0])
    {
      children();
      if (ShowLever)
      {
        translate([0, -0.05, -0.05])
        cube([Radius, .1, .1]);
      }
    }
}


module IntakeBalls()
{  
  rotate(60, [0, 0, 1])
    translate([0, 0, 2.5])
    {
      translate([IndexerBallOffset, 0, 0])
        Ball();
      rotate(120, [0, 0, 1])
        translate([IndexerBallOffset, 0, 0])
          Ball();
      rotate(-120, [0, 0, 1])
        translate([IndexerBallOffset, 0, 0])
          Ball();
    }
}

module Ball()
{
  sphere(d = 5);
}

module LazySusan()
{
  difference()
  {
    cylinder(d = 8, h = .2, center = true);
    cylinder(d = 6, h = .21, center = true);
  }
}

module Shooter()
{
  translate([1.2, 0, 0])
  {
    LaunchBall();
    LaunchGuide();
    Flywheel();
    Hood(Angle = ShooterAngle);
  }
  translate([0, 0, -7.75])
    LazySusan();
}

module ShowRamp()
{
  difference()
  {
    translate([0, -3/2, 0])
      cube([(WheelD + BallD)/2, 3, (WheelD + BallD) / 2]);
    translate([(WheelD + BallD)/2, 0, (WheelD + BallD)/2])
      rotate(90, [1, 0, 0])
        cylinder(d = (WheelD + BallD), h = 6, center = true);
    translate([0, -3/2, 0])
      cube([(WheelD + BallD)/2, 3, 1.0]);    
  }
}

module Everything()
{
  rotate(TurretAngle, [0, 0, 1])
  translate([0, 0, 13])
    Shooter();
  translate([IndexerCenterOffset, 0, 0])
    IntakeBalls();
  if (ShowLimits)
    Limits();
}

if (ShowShooter)
  Everything();


if (ShowRamp)
  scale(25.2)
    ShowRamp();

