$fn = 150;

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
              cylinder(d = 15.3 - .2, h = 6.3, center = true);
              cylinder(d = 15 - .2, h = 6.0, center = true);
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
    cube([15, 15, 15], center = true);
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
  intersection()
  {
    //Ramp
    rotate(90, [1, 0,0])
      difference()
      {    
        cylinder(d = 15.3, h = 6, center = true);
        cylinder(d = 15, h = 6.01, center = true);
      }
      translate([5, 0, -10])
        cube([10, 10, 10], center = true);
  }
}

module LaunchInlet()
{
  //Inlet
  translate([-(7.5 / 2), 0, -7.5 - (.15 / 2)])
    cube([7.5, 6, .15], center = true);
}

module LaunchBall()
{
  //Launch ball
  color("Violet")
    rotate(50, [0, 1, 0])
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

module BallScoop()
{
  ArmL = 2.7;//Extends inside sphere so not the rotation point
  difference()
  {
    union()
    {
      sphere(d = 5.2);
      translate([ArmL / 2, -1, -.2])
        cube([ArmL, 2, .2]);
    }
    sphere(d = 5);
    translate([-3, -3, 0])
      cube([6, 6, 6]);
    translate([-2.6, -3, -1.4])
      rotate(-15, [0, 1, 0])
        cube([6, 6, 6]);
  }
}

module BallAndScoop()
{
  color("LimeGreen")
    sphere(d = 5);    
  color("Blue")
    BallScoop();
}

module IntakeBalls()
{
  CenterActuatorL =4;
  CenterActuatorR = 30;
  LeftActuatorL =4;
  LeftActuatorR = 30;
  RightActuatorL =4;
  RightActuatorR = 30;
  {
    //Intake balls
    translate([-4.5, 0, -5])
    
    //Rotate as if pushed by actuator
    RotateObject(CenterActuatorL, CenterActuatorR, true)
      BallAndScoop();
    translate([-3.5, 5.5, -5])
      rotate(-55, [0, 0, 1])
        RotateObject(LeftActuatorL, LeftActuatorR, true)
          BallAndScoop();
    translate([-3.5, -5.5, -5])
      rotate(55, [0, 0, 1])
        RotateObject(RightActuatorL, RightActuatorR, true)
          BallAndScoop();
  }
}

module Everything()
{
  translate([0, 0, 4])
  {
    LaunchBall();
    LaunchGuide();
    Flywheel();
    Hood(Angle = -40);
  }

  IntakeBalls();
  LaunchInlet();
  Limits();
}

Everything();

//BallScoop();