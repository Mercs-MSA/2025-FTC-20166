ShowLimits = false;
ShowShooter = true;
ShowRampPlate = true;
ShowServos = true;
ShowServoMounts = true;
ShowGears = true;
LimitBounds = [18, 18, 18];
ShooterAngle = 0;
TurretAngle  = 0.1;
ShooterWallHeight = 5.5;
ShooterPlateWidth = 10;
ShooterPlateHeight = 2;
FlywheelDiameter = 3.75;
BallCompression = 0.2;
ShooterWallT = 0.2;
ShooterPlateT = 0.2;
MotorMountD = 2.5;
RampLowerCutoff = 0.4;
RampSupportT = .5;
FlywheelClearance = 2.4;
RampTiltHOffset = -1.8;
RampTiltVOffset = -ShooterWallHeight / 2;//-2.8;//Make centered to simplify 3D printing since can now split in 2 more cleanly
ShooterCurveAngle = 52;
ShowShooterMotor = 3;
BearingD = 14.2 / 25.4;;
RampXOffset = 1.2;
RampYOffset = 1.8 ;
RampZOffset = .5 ;
BallExitOffset = 2.7;
ServoHOffset = 102 / 25.4;//90mm
ServoVOffset = 14 / 25.4;//12mm
LazySusanOuterD = 200/25.4;
LazySusanInnerD = 146/25.4;
LazySusanInnderMountD = 157.5/25.4;
LazySusanOuterMountD = 187/25.4;
LazySusanMountHoleD = 3/16;
LazySusanAccessHoleD = 10.5/25.4;
LazySusanT = .2;
ServoBlockLength = 2.7;
ServoBlockLengthOpening = 41/25.4;
TurretServoLocation = [-4.51, -2.15, 0];
TurretSensorLocation = [0, 5.0, -0.2];
TurretSensorMountD = 9.3/25.4;

module Stop(){}

$fn = 150;
BallDiameter = 5.0;
ShooterInnerCurveRadius = (FlywheelDiameter / 2) + BallDiameter - BallCompression;
ShooterOuterCurveRadius = ShooterInnerCurveRadius + ShooterWallT;
BallArcRadius = (FlywheelDiameter + BallDiameter - BallCompression) / 2;
ServoRotateOffset = 9.85 / 25.4;

module Servo()
{
  color("DarkOrchid", 0.5) 
    translate([ServoRotateOffset, 0, 0])
      scale (1/25.4)
        import("GoBildaServoLoRes.stl");
}

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
    cylinder(d = FlywheelDiameter, h = 3, center = true);
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
  color("LightSteelBlue", 0.5)
  difference()
  {
    cylinder(d = LazySusanOuterD, h = LazySusanT, center = true);
    cylinder(d = LazySusanInnerD, h = LazySusanT + .1, center = true);
  }
  color("plum", 0.5)
  translate([0, 0, -LazySusanT])
    rotate(90, [1, 0, 0])
      scale(1/25.4)
        import("Big gear.stl", 4);
}

module Shooter()
{
  {
    translate([-RampXOffset, -RampYOffset, RampZOffset])
    {
      translate([0, -RampTiltHOffset, -RampTiltVOffset])
      {
        rotate(-ShooterAngle, [1, 0,0])
        {
          translate([0, ShooterInnerCurveRadius, 0])
          {
            translate([0, RampTiltHOffset, RampTiltVOffset])
            {
              ShooterBody();
              if (ShowGears)
                translate([BallExitOffset, 0, ShooterWallHeight / 2])
                  FlywheelHorizontal();
            }
          }
        }
      }
    }
  }
  
  
/*  rotate(60, [0, 0, 1])
    translate([0, -5, 3])
      color("Violet")
        Ball();
*/
//  rotate(50, [0, 0, 1])
//    translate([0, 0, 2])
//      color("Violet", 0.3)
//        Ball();
}

module TurretServoGear()
{
  color("plum", 0.5)
    translate([-4.999, 0, 0])
      rotate(180, [1, 0, 0])
      rotate(90, [1, 0, 0])
        scale(1/25.4)
          import("servo gear - Spur gear (36 teeth).stl", 4);
}

module TurretSensorGear()
{
  color("plum", 0.5)
  {
    difference()
    {
      union()
      {
        translate([-4.999, 0, 0])
          rotate(-90, [1, 0, 0])
            scale(1/25.4)
              import("36 tooth small gear.stl", 4);
        //Fill in the original hole
        cylinder(d = .5, h = .40);
      }
      //Modified shaft opening
      intersection()
      {
        cylinder(d = (6.25 + .24)/25.4, h = 2, center = true);
        cube([5.6/25.4, 1, 2], center = true);
      }
      //Grub screw captive bolt
      translate([.2, 0, 0.5])
        cube([1.8/25.4, 5.4/25.4, 1], center = true);
      //Grub screw opening
      translate([0, 0, .2])
        rotate(90, [0, 1, 0])
          cylinder(d = 3.2 / 25.4, h = 2);
    }
  }
}

module TurretServo()
{
  if (ShowGears)
  {
    //Drive servo
    translate(TurretServoLocation)
      translate([0, 0, -.6])
      TurretServoGear();
    //Position sensor
    translate(TurretSensorLocation)
    {
      //Sensor gear
      TurretSensorGear();
      //Sensor Body & shaft
      color("lightgreen", 0.5)
      {
        translate([0, 0, -.5])
          cylinder(d = .2, h = 1);
        translate([0, 0, .3])
          cylinder(d = .75, h = .5);
      }
    }
  }
  if (ShowServos)
  {
    translate(TurretServoLocation)
      translate([0, 0, .075])
        rotate(90, [0, 0, 1])
          rotate(180, [1, 0, 0])
            Servo();
  }
}

module frame(thickness) 
{
  difference() 
  {
    children();
    offset(r = -thickness) children();
  }
}

module MotorBody()
{
  translate([0, 0, -4.25])
    cylinder(d = 1.5, h = 4.25);
}

module YellowJacketMountPattern()
{
  cylinder(d = BearingD, h = 1, center = true);
  translate([-8 / 25.4, -8 / 25.4, 0])
    cylinder(d = 5/25.4, h = 1, center = true);
  translate([8 / 25.4, -8 / 25.4, 0])
    cylinder(d = 5/25.4, h = 1, center = true);
  translate([-8 / 25.4, 8 / 25.4, 0])
    cylinder(d = 5/25.4, h = 1, center = true);
  translate([8 / 25.4, 8 / 25.4, 0])
    cylinder(d = 5/25.4, h = 1, center = true);
}

module ShooterBasePlate()
{
  difference()
  {
    union()
    {
      //Main base plate
      translate([-ShooterOuterCurveRadius, -ShooterOuterCurveRadius + 1 + RampLowerCutoff, -ShooterPlateT])
        cube([ShooterOuterCurveRadius + FlywheelClearance, ShooterOuterCurveRadius - RampLowerCutoff, ShooterPlateT]);
    }
    //Motor mount holes
    YellowJacketMountPattern();
    //Ball insertion clearance
    translate([-1, -7, 0])
      cylinder(d = 7, h = 1, center = true);      
    //Turret position sensor
    translate(TurretSensorLocation)
      translate([-1.5, -4.875, 0])//Should really calculate this position
        {
          cylinder(d = 1.2, h = .5, center = true);
          translate([0, 1, 0])
            cube([1.2, 2, .5], center = true);
        }
  }
}

module RenderMotorBody()
{
  if (ShowGears)
  {
    if (ShowShooterMotor == 1)
      MotorBody();
    else if (ShowShooterMotor == 2)
      translate([0, 0, ShooterWallHeight + ShooterPlateT])
        rotate(180, [1, 0, 0])
          MotorBody();
    else if (ShowShooterMotor == 3)
      translate([0, 0, ShooterWallHeight + ShooterPlateT + (1.5 / 2)])
        rotate(-45, [0, 0, 1])
          rotate(-90, [1, 0, 0])
            translate([0, 0, -1.5 / 2])
                MotorBody();
  }
}

module JointLug()
{
  translate([-.5/2, 0, 0])
  {
    difference()
    {
      union()
      {
        cylinder(d = .5, h = 1, center = true);
        translate([0, -.5 / 2, -.5])
          cube([.5 / 2, .5, 1]);
      }
      cylinder(d = 4.2 / 25.4, h = 2, center = true);
    }
  }
}

module ShooterBody()
{
  //Center on middle of ball exit
//  translate([BallArcRadius, 0, 0])
  difference()
  {
    union()
    {
      //Center on middle of ball entry (approximately)
      translate([BallExitOffset, 0, 0])
      {
        //Ramp rotation pivots
        translate([-ShooterInnerCurveRadius - ShooterPlateT - 0.25, -ShooterInnerCurveRadius - RampTiltHOffset, -RampTiltVOffset])
        {
          //Left side
          difference()
          {
            rotate(90, [0, 1, 0])
              cylinder(d = 1, h = 2.8 + ShooterPlateT);
            translate([2 + .17 + 0.25+ 0.707, .707, 0])
              rotate(ShooterCurveAngle - 8, [0, 0, 1])
                cube([2, 2, 2], center = true);
          }
          //Right side
          translate([ShooterInnerCurveRadius + FlywheelClearance + ShooterPlateT + ShooterPlateT + .05, 0, 0])//Not sure why 0.05 needed?
            rotate(90, [0, 1, 0])
              cylinder(d = 1, h = 0.25);
        }
        //Left servo attch pivot
        translate([-ShooterInnerCurveRadius - ShooterPlateT - 0.25, -ShooterInnerCurveRadius - RampTiltHOffset + (11 / 2.54), -RampTiltVOffset])
          rotate(90, [0, 1, 0])
            cylinder(d = 1, h = 0.25);
        //Right servo attch pivot
        translate([FlywheelClearance, -ShooterInnerCurveRadius - RampTiltHOffset + (11 / 2.54), -RampTiltVOffset])
          rotate(90, [0, 1, 0])
            cylinder(d = 1, h = 0.25);
        
        RenderMotorBody();
        //Lower base plate
        ShooterBasePlate();
        //Upper base plate
        translate([0, 0, ShooterWallHeight + ShooterPlateT])
          ShooterBasePlate();
        //Left sidewall
        //Inner curve
          rotate(180, [0, 0, 1])
            rotate_extrude(angle = ShooterCurveAngle) translate([ShooterInnerCurveRadius, 0, 0]) square([ShooterWallT,ShooterWallHeight]);
        //Left side plate
        translate([-ShooterInnerCurveRadius - ShooterWallT, -ShooterOuterCurveRadius + 1 + RampLowerCutoff, 0])
          cube([ShooterWallT, ShooterOuterCurveRadius - RampLowerCutoff, ShooterWallHeight]);
        //Right side plate
        translate([FlywheelClearance - ShooterWallT, -ShooterOuterCurveRadius + 1 + RampLowerCutoff, 0])
          cube([ShooterWallT, ShooterOuterCurveRadius - RampLowerCutoff, ShooterWallHeight]);
/*        //Left join lugs
        translate([-ShooterInnerCurveRadius - ShooterWallT, -ShooterOuterCurveRadius + 1.2 + RampLowerCutoff + 1.2, ShooterWallHeight / 2])
          JointLug();
        translate([-ShooterInnerCurveRadius - ShooterWallT, -ShooterOuterCurveRadius + 1 + RampLowerCutoff + 5.8, ShooterWallHeight / 2])
          JointLug();
        translate([FlywheelClearance, -ShooterOuterCurveRadius + 1.2 + RampLowerCutoff + 1.2, ShooterWallHeight / 2])
          mirror([1, 0, 0]) JointLug();
        translate([FlywheelClearance, -ShooterOuterCurveRadius + 1 + RampLowerCutoff + 5.8, ShooterWallHeight / 2])
          mirror([1, 0, 0]) JointLug();
          */
          /*
        //Flywheel
        color("lightgreen", 0.5)
          cylinder(d = FlywheelDiameter, h = ShooterPlateT + 0.01);
        //Ball outlines
        color("lightblue", 0.5)
        {
          rotate(0, [0, 0, 1])
            translate([-BallArcRadius, 0, 0])
              cylinder(d = BallDiameter, h = ShooterPlateT + 0.01);
          rotate(55, [0, 0, 1])
            translate([-BallArcRadius, 0, 0])
              cylinder(d = BallDiameter, h = ShooterPlateT + 0.01);
          }
          */
      }
    }
    //Ramp pivot holes
    translate([0, -ShooterInnerCurveRadius - RampTiltHOffset, -RampTiltVOffset])
      rotate(90, [0, 1, 0])
        cylinder(d = 3.8 / 25.4, h = 20, center = true);
    //Servo attach pivot holes
    translate([0, -ShooterInnerCurveRadius - RampTiltHOffset + (11 / 2.54), -RampTiltVOffset])
      rotate(90, [0, 1, 0])
        cylinder(d = 3.8 / 25.4, h = 20, center = true);
        
        
  }
}

module ServoBlockHoles()
{
  translate([0, (48 / 25.4) / 2, (9.9 / 25.4) / 2])
    rotate(90, [0, 1, 0])
      cylinder(d = 4.5/25.4, h = 1, center = true);
  translate([0, -(48 / 25.4) / 2, (9.9 / 25.4) / 2])
    rotate(90, [0, 1, 0])
      cylinder(d = 4.5/25.4, h = 1, center = true);
  translate([0, (48 / 25.4) / 2, -(9.9 / 25.4) / 2])
    rotate(90, [0, 1, 0])
      cylinder(d = 4.5/25.4, h = 1, center = true);
  translate([0, -(48 / 25.4) / 2, -(9.9 / 25.4) / 2])
    rotate(90, [0, 1, 0])
      cylinder(d = 4.5/25.4, h = 1, center = true);
}

module ServoBlock(BlockDepth = .3, BlockWidth = 1.0, BlockLength = ServoBlockLength, OpeningWidth = 21/25.4, OpeningLength = ServoBlockLengthOpening, BlockWidthOffset = -0.05, AddAttachHoles = true)
{  
  difference()
  {
    //Base
      cube([BlockDepth, BlockLength, BlockWidth], center = true);
    translate([0, 0, BlockWidthOffset])
    {
      //Mount holes
      ServoBlockHoles();
      //Servo opening
      cube([BlockDepth + .5, OpeningLength, OpeningWidth], center = true);
      //Slot opening(s)
      cube([1, 54/25.4, 3/25.4], center = true);
    }
    //Attach holes
    if (AddAttachHoles)
    {
      translate([0, (ServoBlockLength / 2) - .15, 0])
        cylinder(d = 2.9/25.4, h = 2, center = true);
      translate([0, -(ServoBlockLength / 2) + .15, 0])
        cylinder(d = 2.9/25.4, h = 2, center = true);
    }
  }
  
  //Alignment test
  if (AddAttachHoles && ShowGears)
  {
#  translate([0, (ServoBlockLength / 2) - .15, 0])
    cylinder(d = 2.9/25.4, h = 2, center = true);
#  translate([0, -(ServoBlockLength / 2) + .15, 0])
    cylinder(d = 2.9/25.4, h = 2, center = true);
  }
}

module RampPivotBlock()
{
  difference()
  {
    hull()
    {
      rotate(90, [0, 1, 0])
        cylinder(d = 1, h = RampSupportT);
      translate([0, -2, RampTiltVOffset - RampZOffset - (ShooterPlateT / 2)])
        cube([RampSupportT, 2, 0.001]);
    }
    translate([RampSupportT - .25 - .02, 0, 0])
      rotate(90, [0, 1, 0])
        cylinder(d = 1.2, h = 1);
    rotate(90, [0, 1, 0])
      cylinder(d = 4.2/25.4, h = 1, center = true);
    translate([RampSupportT - 0.05, -2, -3.15])
      cube([.05, 3, 4]);
  }
  translate([0, -1.7, RampTiltVOffset - RampZOffset - (ShooterPlateT / 2)])
    cube([RampSupportT + .3, 1.0, .5]);
}

module QuadHoles(d, hole)
{
    //Lazy Susan mounting holes
    rotate(45, [0, 0, 1])
      translate([d / 2, 0, 0])
        cylinder(d = hole, h = 1, center = true);
    rotate(135, [0, 0, 1])
      translate([d / 2, 0, 0])
        cylinder(d = hole, h = 1, center = true);
    rotate(-45, [0, 0, 1])
      translate([d / 2, 0, 0])
        cylinder(d = hole, h = 1, center = true);
    rotate(-135, [0, 0, 1])
      translate([d / 2, 0, 0])
        cylinder(d = hole, h = 1, center = true);
}

module RampPlate()
{
  //Main plate with ball entry opening
  difference()
  {
    //Main plate
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset + ((ShooterOuterCurveRadius + FlywheelClearance) / 2), 0.9, 0])
      cube([ShooterOuterCurveRadius + FlywheelClearance, 9.8, ShooterPlateT], center = true);
    //Ball opening
    cylinder(d = LazySusanInnerD + .05, h = 1, center = true);
    //Lazy Susan inner ring attach holes
    QuadHoles(d = LazySusanInnderMountD, hole = LazySusanMountHoleD);
    //Lazy Susan outer access hole
    translate([0, LazySusanOuterMountD / 2, 0])
      cylinder(d = LazySusanAccessHoleD, h = 1, center = true);
    //Shooter servo block mount holes
    //Left
    //Printed block
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset + .3 + 0.025 - (.3/2), ServoHOffset - .96 + .15, -ShooterPlateT / 2])
      cylinder(d = .14, h = 1, center = true);
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset + .3 + 0.025 - (.3/2), ServoHOffset - .96 + ServoBlockLength - .15, -ShooterPlateT / 2])
      cylinder(d = .14, h = 1, center = true);
    //GoBilda block
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset + .3 + 0.025 - (.3/2), ServoHOffset + ServoRotateOffset + (1.654 / 2), -ShooterPlateT / 2])
      cylinder(d = .14, h = 1, center = true);
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset + .3 + 0.025 - (.3/2), ServoHOffset + ServoRotateOffset - (1.654 / 2), -ShooterPlateT / 2])
      cylinder(d = .14, h = 1, center = true);
    //Right
    //Printed block
    translate([BallExitOffset - RampXOffset + FlywheelClearance - 0.025 - (0.3 / 2), ServoHOffset - .96 + .15, -ShooterPlateT / 2])
      cylinder(d = .14, h = 1, center = true);
    translate([BallExitOffset - RampXOffset + FlywheelClearance - 0.025 - (0.3 / 2), ServoHOffset - .96 +ServoBlockLength - .15, -ShooterPlateT / 2])
      cylinder(d = .14, h = 1, center = true);
    //GoBilda block
    translate([BallExitOffset - RampXOffset + FlywheelClearance - 0.025 - (0.3 / 2), ServoHOffset  + ServoRotateOffset + (1.654 / 2), -ShooterPlateT / 2])
      cylinder(d = .14, h = 1, center = true);
    translate([BallExitOffset - RampXOffset + FlywheelClearance - 0.025 - (0.3 / 2), ServoHOffset  + ServoRotateOffset - (1.654 / 2), -ShooterPlateT / 2])
      cylinder(d = .14, h = 1, center = true);
    //Turret servo opening
    translate([0, ServoRotateOffset, 0])
      translate(TurretServoLocation)
      {
        cube([.85, ServoBlockLengthOpening, .8], center = true);
        rotate(90, [0, 1, 0])
          ServoBlockHoles();
      }
    //Turret sensor opening
    translate(TurretSensorLocation)
      intersection()
      {
        cylinder(d = TurretSensorMountD, h = 1.5, center = true);
        cube([8.5 / 25.4, TurretSensorMountD, 1.5], center = true);
      }

  }
  //Ramp servo support blocks
  if (ShowServoMounts)
  {
    //Left
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset  + .15 + 0.025, ServoHOffset + .39 , -(ShooterPlateT + 1) / 2])
      rotate(180, [0, 1, 0])
        ServoBlock();
    //Right
    translate([BallExitOffset - RampXOffset + FlywheelClearance -.15 - 0.025, ServoHOffset + .39 , -(ShooterPlateT + 1) / 2])
      rotate(180, [0, 1, 0])
        ServoBlock();
  }
  //Ramp pivot blocks
  //Left
  translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset - RampSupportT, 0, -RampTiltVOffset + RampZOffset])
    RampPivotBlock();
  //Right
  translate([BallExitOffset - RampXOffset + FlywheelClearance + RampSupportT, 0,  -RampTiltVOffset + RampZOffset])
    mirror([1, 0, 0])
      RampPivotBlock();
  //Turret servo mount
  translate([0, ServoRotateOffset, .1 + (.31 / 2)])
    translate(TurretServoLocation)
      rotate(90, [0, 1, 0])
        ServoBlock(BlockDepth = .31, BlockWidth = 21/25.41, BlockLength =  2.3, OpeningWidth = 21/25.4, BlockWidthOffset = 0, AddAttachHoles = false);
}

module HMarker()
{
#  rotate(90, [0, 1, 0])
    cylinder(d = .1, h = 20, center = true);
}

module Linkage()
{
  difference()
  {
    hull()
    {
      cylinder(d = .7, h = .2, center = true);
      translate([150/25.4, 0, 0])
        cylinder(d = .7, h = .2, center = true);
    }
    cylinder(d = 3.5/25.4, h = .21, center = true);
    translate([150/25.4, 0, 0])
      cylinder(d = 4.5/25.4, h = .21, center = true);
  }
}

module LazySusanInnerSpacer()
{
  difference()
  {
    cylinder(d = (LazySusanOuterD + LazySusanInnerD) / 2, h = .039, center = true);
    cylinder(d = LazySusanInnerD, h = 1, center = true);
    echo (((LazySusanOuterD + LazySusanInnerD) / 2) - LazySusanInnerD);

    QuadHoles(d = LazySusanInnderMountD, hole = LazySusanMountHoleD);
  }
}

module LazySusanOuterSpacer()
{
  difference()
  {
    cylinder(d = LazySusanOuterD, h = .039, center = true);
    cylinder(d = (LazySusanOuterD + LazySusanInnerD) / 2, h = 1, center = true);
    echo (((LazySusanOuterD + LazySusanInnerD) / 2) - LazySusanInnerD);

    QuadHoles(d = LazySusanOuterMountD, hole = LazySusanMountHoleD);
  }
}

module Everything()
{
  rotate(TurretAngle, [0, 0, 1])
  {
    if (ShowShooter)
      Shooter();
    if (ShowRampPlate)
      RampPlate();
    if (ShowServos)
      translate([0, ServoHOffset, -ServoVOffset])
      {
        translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset, 0, 0])
          rotate(90, [0, 0, 1])
            rotate(-90, [1, 0, 0])
              Servo();
        translate([BallExitOffset - RampXOffset + FlywheelClearance, 0, 0])
          rotate(90, [0, 0, 1])
            rotate(90, [1, 0, 0])            
                Servo();
      }
    TurretServo();
  }
  if (ShowGears)
    translate([0, 0, -.2])
      LazySusan();

}

module SensorTest()
{
  scale([1.0,1.0, .2])
  intersection()
  {
    TurretSensorGear();
    cube([1, 1, 1], center = true);
  }
}

//Everything();

//ServoBlock();

//TurretSensorGear();

//Linkage();
//LazySusanInnerSpacer();
//LazySusanOuterSpacer();

if (ShowLimits)
  Limits();

//SensorTest();


TurretSensorGear();

