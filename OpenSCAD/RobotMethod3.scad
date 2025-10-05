ShowLimits = false;
LimitBounds = [18, 18, 18];
ShooterAngle = -40;
TurretAngle  = 15;
ShooterWallHeight = 5;
ShooterPlateWidth = 10;
ShooterPlateHeight = 2;
ShooterCurveDiameter = 15;

module Stop(){}

$fn = 150;
ChannelWidth = 5.5;


module Limits()
{
  //Limits
  color([0.2, 0.8, 0.1, 0.2])
    translate([0, 0, 15/2])
      cube(LimitBounds, center = true);
}


module FlywheelHorizontal()
{
  //Flywheel
  color("FireBrick")
    cylinder(d = 5, h = 4, center = true);
}

module RotateObject(Radius, Angle, Axis, ShowLever = true)
{
  //Rotate the object around a point 'Radius' from the zero referenced point of the child object
  translate([Radius, 0, 0])
    rotate(Angle, Axis)
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
  rotate(-ShooterAngle, [1, 0,0])
  translate([-2.5, ShooterCurveDiameter / 2, 0])
  {
    ShooterBody();
    translate([2.5 + 5 , 0, 2.6])
      FlywheelHorizontal();
    translate([2.5, 0, 2.6])
      RotateObject(5, 20, [0, 0, 1])
      color("Violet")
        Ball();
  }
  
//  translate([0, 0, -7.75])
//    LazySusan();
}

module frame(thickness) 
{
  difference() 
  {
    children();
    offset(r = -thickness) children();
  }
}


module ShooterPlate()
{
  intersection()
  {
    union()
    {
      square([ShooterPlateWidth, ShooterPlateHeight]);
      translate([ShooterCurveDiameter / 2, 0])
        circle(d = ShooterCurveDiameter);
      translate([ShooterCurveDiameter / 2, -ShooterCurveDiameter/2])
        square([ShooterPlateWidth - 2.5, ShooterCurveDiameter / 2]);
    }
    translate([0, -ShooterCurveDiameter/2])
      square([ShooterPlateWidth, ShooterPlateHeight + (ShooterCurveDiameter / 2)]);
  }
}

module ShooterBody()
{
  linear_extrude(height = 0.2)
      ShooterPlate();
  difference()
  {
    linear_extrude(height = ShooterWallHeight)
      frame(thickness = 0.2)
        ShooterPlate();
    translate([-1, 0, 0])
      cube([ShooterPlateWidth + 3, 10, 6]);
    translate([(ShooterCurveDiameter / 2) - 5, -ShooterCurveDiameter / 1.9, 0])
      cube([10, ShooterCurveDiameter, 6]);
  }
}



module Everything()
{
  translate([-5, 0, 3])
    Shooter();
  
  translate([-2.5, -2, 5])
    Ball();
  translate([2.5, -1, 5])
    Ball();
  if (ShowLimits)
    Limits();
}

Everything();

//Shooter();


