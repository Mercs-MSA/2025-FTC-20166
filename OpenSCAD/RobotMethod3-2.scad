ShowLimits = false;
LimitBounds = [15, 15, 15];
ShowShooter = true;
ShowRampPlate = true;
ShowBallStore = 2;
ShowDriveBase = true;
ShowServos = true;
ShowServoMounts = true;
ShowGears = true;
ShowAlignMarkers = true;
ShowBallLifter = true;
ShowTurretMountPlate = true;
ShowLeftSideAssembly = true;
ShowRightSideAssembly = true;
ShowIntake = true;
ShowBellypan = false;
ShowRevMountPlate = true;
ShooterAngle = -90;
TurretAngle  = 0.0;
ShooterWallT = 0.2;
ShooterPlateT = 0.125;
ShowShooterMotor = 5;
BallStoreSpacingAngle = 120;
BallStoreRadius = 3.2;
BallStoreAngle = -30;
BallLifterAngle = 110;
FreeHoleOversize = 0.00001;
IntakeD = 3;

module Stop(){}
RobotOuterWidth = 16.063;//This puts the outer side channels on an 8mm grid
RobotOuterLength = 15;
DriveBaseVOffset = -5.3;
WheelSpacingOffset = (RobotOuterLength / 2) - 2.2;
IntakeArmLength = 7;

SensorGear36 = false;//Select 36 tooth vs 22 for servo gear
BallDiameter = 5.0;
BallClearance = .25;
ShooterWallHeight = BallClearance + BallDiameter;
ShooterPlateWidth = 10;
ShooterPlateHeight = 2;
FlywheelDiameter = 3.75;
BallCompression = 0.2;
MotorMountD = 2.5;
RampLowerCutoff = 0.4;
RampSupportT = .5;
FlywheelClearance = (FlywheelDiameter / 2) + .5;
RampTiltHOffset = -1.8;
RampTiltVOffset = -ShooterWallHeight / 2;
ShooterCurveAngle = 52;
BearingD = 14.2 / 25.4;
M3FreeHoleD = (3.1 + FreeHoleOversize) / 25.4;
M3TapHoleD = 3.0 / 25.4;
M4FreeHoleD = (4.1 + FreeHoleOversize) / 25.4;
M4TapHoleD = 4.0 / 25.4;
M5FreeHoleD = (5.1 + FreeHoleOversize) / 25.4;
RampXOffset = 1.2;
RampYOffset = 1.8 ;
RampZOffset = -0.5 ;
BallExitOffset = 2.7;
ServoHOffset = 102 / 25.4;
ServoVOffset = 14 / 25.4;
LazySusanOuterD = 200/25.4;
LazySusanInnerD = 146/25.4;
LazySusanInnderMountD = 157.5/25.4;
LazySusanOuterMountD = 187/25.4;
LazySusanMountHoleD = 3/16;
LazySusanAccessHoleD = 10.5/25.4;
LazySusanT = .2;
ServoBlockLength = 2.7;
ServoBlockLengthOpening = 41/25.4;
TurretServoLocation = [-2.5, 3.70, 0]; //[-2.39, 4.2, 0];//[-4.51, -2.15, 0]
TurretSensorLocation = [1.5, 4.4, -0.2]; //[-4.1, 2.85, -0.2];//[0, 5.0, -0.2]
TurretSensorMountD = 9.3/25.4;
ShooterVersion = 2;
BallLifterOffset = [2.6, .18, 4.11];//[3, .18, 4.11]

$fn = 150;
ShooterInnerCurveRadius = (FlywheelDiameter / 2) + BallDiameter - BallCompression;
ShooterOuterCurveRadius = ShooterInnerCurveRadius + ShooterWallT;
BallArcRadius = (FlywheelDiameter + BallDiameter - BallCompression) / 2;
ServoRotateOffset = 9.85 / 25.4;
CurveBallPathExtension = .4;
CurveBallProfileWallT = .7;
CurveBallProfileInnerR = 6.5;
CurveBallPathSegmentAngle = 50;
ShooterInnerCurveBackT = .2;
RampRotationSupportT = 0.2;
UpperBallTractionGap = 1.0;
UpperBallTractionLocation = [-.4, -3.3, .70];
LTBlockT = 0.3;//Lower traction pulley block thickness
LTBlockL = 1.1;//Lower traction pulley block length
LTBlockH = 1.2;//Lower traction pulley block height
LTMountSpacing = 0.8;//Lower traction pulley block mount hole spacing
LTBlockSpacing = 3.5;//Lower traction pulley block spacing
LTVO = 0.7;//Lower traction pulley block vertical offset
LTMD = 2.9/25.4;//Lower traction pulley block mount hole diameter (M3 self tap)

module Servo()
{
  color("DarkOrchid", 0.5) 
  rotate(90, [0, 0, 1])
  rotate(90, [1, 0, 0])
    translate([0, 0, 0])
      scale (1/25.4)
        import("Components/GoBildaServoLoRes.stl");
}

module GoBildaServoBlock()
{
  translate([-0.167, 1.218, -0.617])
  rotate(90, [0, 0, 1])
  scale(1/25.4)
    import("Components/1802-0043-0001assembly.stl");
}

module GoBildaServoPlate()
{
  translate([0.71, 2.225, -0.495])
  rotate(90, [0, 0, 1])
  scale(1/25.4)
    import("Components/1801-0040-0001 assembly (2.5mm thick plate).stl");
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
  rotate(-10, [0, 0, 1])
  translate([-0.6, -(FlywheelDiameter + BallDiameter) / 2, 0])
    Ball();

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
  color("Purple", 0.5)
    sphere(d = 5);
}

module TurretGear()
{
  difference()
  {
    translate([0, 0, .0])
      scale([1.0, 1.0, 2.0])
        rotate(90, [1, 0, 0])
          scale(1/25.4)
            import("Components/Big gear.stl", 4);
    cylinder(d = LazySusanOuterD + 0.03, h = 1, center = false);
    for (i = [0:3])
      rotate((90 * i) + 45, [0, 0, 1])
        translate([3.671, 0, 0])
          cylinder(d = 4.8 / 25.4, h = 1, center = true);
  }
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
  TurretGear();
}

module Shooter()
{
  if (ShooterVersion == 1)
    Shooter1();
  else if (ShooterVersion == 2)
    Shooter2();
}

module Shooter1()
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

module Shooter2()
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
              ShooterBody2();
              if (ShowGears)
                translate([BallExitOffset, 0, ShooterWallHeight / 2])
                  FlywheelHorizontal();
            }
          }
        }
      }
    }
  }
}

module TurretServoGear()
{
  color("plum", 0.5)
  difference()
  {
    rotate(6.6, [0, 0, 1])
    union()
    {
    translate([-4.9985, 0, 0])
      rotate(180, [1, 0, 0])
      rotate(90, [1, 0, 0])
        scale(1/25.4)
          import("Components/servo gear - Spur gear (36 teeth).stl", 4);
    cylinder(d = 1, h = .3);
    }
    cylinder(d = .2, h = 1, center = true);
    rotate(45, [0, 0, 1])
      QuadHoles(d = .275 * 2, hole = 3.2/25.4);
    rotate(45, [0, 0, 1])
      QuadHoles(d = .275 * 2, hole = 6/25.4, h = .3);

  }
}

module TurretSensorGear()
{
  color("plum", 0.5)
  {
    difference()
    {
      union()
      {
        if (SensorGear36)
          translate([-4.999, 0, 0])
            rotate(-90, [1, 0, 0])
              scale(1/25.4)
                  import("Components/36 tooth small gear.stl", 4);
        else
          translate([-4.65, 0, 0])
            rotate(-90, [1, 0, 0])
              scale(1/25.4)
                  import("Components/22 tooth small gear.stl", 4);
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
        cube([1.8/25.4, 6.0/25.4, 1], center = true);
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
      translate([-ServoRotateOffset, ServoRotateOffset, -.52])
        rotate(-5, [0, 0, 1])
          TurretServoGear();
    //Position sensor
    translate(TurretSensorLocation)
    {
      //Sensor gear
      translate([0, 0, -0.32])
        rotate(-9.5, [0, 0, 1])
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
}

module frame(thickness) 
{
  difference() 
  {
    children();
    offset(r = -thickness) children();
  }
}

module MotorBody(BodyL = 108/25.4, ShaftL = 24/25.4)
{
  color("Silver")
  translate([0, 0, -BodyL])
    cylinder(d = 1.5, h = BodyL);
  color("DarkGray")
  cylinder(d = 8/25.4, h = ShaftL, $fn = 6);
}

module YellowJacketMountPattern()
{
  cylinder(d = BearingD, h = 1, center = true);
  translate([-8 / 25.4, -8 / 25.4, 0])
    cylinder(d = M4FreeHoleD, h = 1, center = true);
  translate([8 / 25.4, -8 / 25.4, 0])
    cylinder(d = M4FreeHoleD, h = 1, center = true);
  translate([-8 / 25.4, 8 / 25.4, 0])
    cylinder(d = M4FreeHoleD, h = 1, center = true);
  translate([8 / 25.4, 8 / 25.4, 0])
    cylinder(d = M4FreeHoleD, h = 1, center = true);
}

module pie_slice(r=3.0,a=30) {     
   intersection() {
    circle(r=r);
    square(r);
    rotate(a-90) square(r);  
  } 
}

module UpperBallTractionSupportMountHoles()
{
  translate([.3/2, 0, 0])
    cylinder(d = 3/25.4, h = 3, center = true);
  translate([.3/2, 1, 0])
    cylinder(d = 3/25.4, h = 3, center = true);
}

module BearingBlock(L = 1.4, H = 1, T = 0.3, VO = 0.5, MS = 1.0, MD = 0.118)
{
  difference()
  {
    translate([-L / 2, -T / 2, 0])
      cube([L, T, H]);
    translate([0, 0, VO])
      rotate(90, [1, 0, 0])
        cylinder(d = BearingD, h = T + .1, center = true);
    DualHoles(S = MS, D = MD, H = (H * 2) + .1);
    if (ShowAlignMarkers)
    {
#      translate([0, 0, H / 2])
        DualHoles(S = MS, D = MD, H = (H * 2));
#      translate([0, 0, VO])
        rotate(90, [1, 0, 0])
          cylinder(d = BearingD, h = T + .1, center = true);
    }
  }
}



module UpperBallTraction()
{
  {
    rotate(180, [0, 0, 1])
      ServoBlock(BlockWidthOffset = 0);
    translate([-UpperBallTractionGap - 0.65, ServoRotateOffset, -0.52])
    {
      rotate(90, [0, 0, 1])
        BearingBlock(L = 1.4, H = 1, T = 0.3, VO = 0.52, MS = 1.0, MD = 0.118);
    }
  }
}

module LowerBallTractionMountHoles()
{

  translate([0.4, 0, 0])
  {
  translate([-LTBlockSpacing / 2, 0, 0])
    rotate(90, [0, 0, 1])
      DualHoles(S = LTMountSpacing, D = 0.118, H = (LTBlockH * 2));
  translate([(LTBlockSpacing / 2), 0, 0])
    rotate(90, [0, 0, 1])
      DualHoles(S = LTMountSpacing, D = 0.118, H = (LTBlockH * 2));
  }
}

module LowerBallTraction()
{
  translate([0.4, 0, 0])
  {
    translate([-LTBlockSpacing / 2, 0, 0])
      rotate(90, [0, 0, 1])
        BearingBlock(L = LTBlockL, H = LTBlockH, T = LTBlockT, VO = LTVO, MS = LTMountSpacing, MD = 0.118);
    translate([LTBlockSpacing / 2, 0, 0])
      rotate(90, [0, 0, 1])
        BearingBlock(L = LTBlockL, H = LTBlockH, T = LTBlockT, VO = LTVO, MS = LTMountSpacing, MD = 0.118);
    translate([(UpperBallTractionGap / 2) - .1, 0, LTVO])
      UpperBallTractionPulley();
  }
}

module Roller(L, D, CF)
{
  rotate_extrude(angle = 360)
  intersection()
  {
    scale([1.0, CF])
      circle(d = D);
    translate([0, -L/2])
      square([D, L]);
  }
}

module ServoShaft()
{
  color("silver")
  {
    cylinder(d = .3, h = 1.25 + 0.1653, $fn = 6);
    cylinder(d = .394, h = 0.1653);
  }
}

module UpperBallTractionPulley()
{
  RollerL = (UpperBallTractionGap - .1) / 3;
    rotate(-90, [0, 1, 0])
    {
    difference()
    {
      union()
      {
        Roller(L = RollerL, D = 1, CF = 1.0);
        translate([0, 0, RollerL])
          Roller(L = RollerL, D = 1, CF = 1.0);
        translate([0, 0, RollerL + RollerL])
          Roller(L = RollerL, D = 1, CF = 1.0);
      }
      cylinder(d = 8.1 / 25.5, h = 3, $fn = 6, center = true);
    }
    if (ShowAlignMarkers)
      translate([0, 0, -.39])
        ServoShaft();
  }
}

module ShooterBasePlate2TopAssembly()
{
  ShooterBasePlate2TopPlate();
  translate(UpperBallTractionLocation)
  {
    UpperBallTraction();
    translate([-.7, ServoRotateOffset, 0])
      UpperBallTractionPulley();
  }
}

module ShooterBasePlate2TopPlate()
{
  difference()
  {
    ShooterBasePlate2Core();
    //Ball feeder belt opening
    translate([UpperBallTractionLocation[0] - UpperBallTractionGap - 0.5, UpperBallTractionLocation[1] - 1.7, -0.5])
      cube([UpperBallTractionGap , 3, 1]);
    //Ball feeder servo and bearing support mount holes
    translate([UpperBallTractionLocation[0], UpperBallTractionLocation[1], 0])
    {
      //Printed block
      PrintedServoBlockMountHoles(d = M3FreeHoleD);
      //GoBilda block    
      GoBildaServoBlockMountHoles(d = M4FreeHoleD);
      //Bearing support
      translate([-UpperBallTractionGap - .8, -.1123, -0.5])
        UpperBallTractionSupportMountHoles();
    }    
    
  }
}

module ShooterBasePlateMountHolesScoop()
{
  //Shooter scoop mount holes
  for (i = [0:4])
  {
    rotate(i * 12, [0, 0, 1])
      translate([- ShooterInnerCurveRadius + 0.3 + 0.2, 0])
        cylinder(d = M4FreeHoleD, h = 1, center = true);
  }
}

module ShooterBasePlate2Core()
{
  LRD = 5;
  
  difference()
  {
    //Main base plate
    hull()
    {
      cylinder(d = FlywheelDiameter, h = ShooterPlateT);
      rotate(180, [0, 0, 1])
        linear_extrude(ShooterPlateT)
          pie_slice(r = ShooterInnerCurveRadius - .3, a = CurveBallPathSegmentAngle);
      translate([-ShooterInnerCurveRadius + .3 + (0.3 / 2), CurveBallPathExtension, 0])
        cylinder(d = .3, h = ShooterPlateT);
      translate([FlywheelClearance - (0.3 / 2), CurveBallPathExtension, 0])
        cylinder(d = .3, h = ShooterPlateT);
      translate([FlywheelClearance - (LRD / 2), -4.88 + (LRD / 2), 0])
        cylinder(d = LRD, h = ShooterPlateT);

    }
    //Motor mount holes
    rotate(45, [0, 0, 1])
      YellowJacketMountPattern();
    //Mounting holes
    ShooterBasePlateMountHolesScoop();
    //Side support
    translate([FlywheelClearance - ShooterPlateT - (4 / 25.4), 0, 0])
      rotate(-90, [0, 0, 1])
        HoleSet(D = M4FreeHoleD, C = 5, S = (16 / 25.4));
  }
}

module HoleSet(D, C, S, Center = false)
{
  Offset = Center ? ((C - 1) * S) / 2 : 0;
  translate([-Offset, 0, 0])
    for (i = [0:C - 1])
      translate([i * S, 0, 0])
        cylinder(d = D, h = 1, center = true);
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
    else if (ShowShooterMotor == 4)
      translate([0, 0, - (1.5/2)])
        rotate(-45, [0, 0, 1])
          rotate(-90, [1, 0, 0])
            translate([0, ShooterPlateT, -(1.5 / 2)])
                MotorBody();
    else if (ShowShooterMotor == 5)
      translate([-0.3, 0, - (1.5/2)])
        rotate(-90, [0, 0, 1])
          rotate(-90, [1, 0, 0])
            translate([0, ShooterPlateT, -(1.5 / 2)])
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

module CurveBallProfile()//ShooterInnerCurveRadius
{
    translate([ShooterInnerCurveRadius -(CurveBallProfileInnerR - BallCompression)/ 2, 0, 0])
    intersection()
    {
      difference()
      {
          circle(d = CurveBallProfileInnerR + CurveBallProfileWallT + CurveBallProfileWallT);
        circle(d = CurveBallProfileInnerR - BallCompression);
      }
      translate([(CurveBallProfileInnerR / 2) + .1, 0])
        square([10, BallDiameter + BallClearance], center = true);
    }
}

module ScoopPlateHoles(D)
{
  rotate(90, [1, 0, 0])
    rotate(90, [0, 1, 0])
    {
      translate([0, 1.7, 0])
        HoleSet(D = D, C = 3, S = (16 / 25.4));
      translate([0, -1.7, 0])
        HoleSet(D = D, C = 3, S = (16 / 25.4));
    }
}

module BallScoop()
{
  difference()
  {
    intersection()
    {
      union()
      {
        //'Donut' segment
        rotate_extrude(angle = CurveBallPathSegmentAngle) 
          CurveBallProfile();
        //Exit
        rotate(90, [1, 0, 0])
          linear_extrude(CurveBallPathExtension) CurveBallProfile();  
        //Ramp rotation pivots
        translate([ShooterInnerCurveRadius + ShooterPlateT + ShooterPlateT, 4.875, 0])
        {
          //Left (scoop) side
          difference()
          {
            rotate(-90, [0, 1, 0])
              cylinder(d = 1, h = 2.1);
            translate([-2.5, -.7, 0])
              rotate(ShooterCurveAngle - 8, [0, 0, 1])
                cube([2, 2, 2], center = true);
          }
        }
      }
      //Chop off the back side
      translate([0, -5, -(BallDiameter + BallClearance)/ 2])
        cube([ShooterInnerCurveRadius + ShooterInnerCurveBackT , CurveBallProfileInnerR + 5, BallDiameter + BallClearance], center = false);
    }
    //Ramp pivot hole
    translate([6, 4.875, 0])
      rotate(90, [0, 1, 0])
        cylinder(d = M4TapHoleD, h = 7, center = true);
    //Servo attach pivot hole. This is just clearance to allow bolt attach to plate
    translate([6, 4.875 - (11 / 2.54), 0])
      rotate(90, [0, 1, 0])
        cylinder(d = 7 / 25.4, h = 7, center = true);
    //Plate attach holes
    translate([ShooterInnerCurveRadius - .2, 0, 0])
      ScoopPlateHoles(D = M4TapHoleD);
    //Top/bottom attach holes
    translate([0, 0, 2.5])
      rotate(180, [0, 0, 1])
        ShooterBasePlateMountHolesScoop();
    translate([0, 0, -2.5])
      rotate(180, [0, 0, 1])
        ShooterBasePlateMountHolesScoop();
  }
//  cylinder(d = .1, h = 10);
//  FlywheelHorizontal();
//  translate([(BallDiameter + FlywheelDiameter) / 2, 0, 0])
//    Ball();
}

module ShooterBody2SidePlateScoop()
{
  difference()
  {
    hull()
    {
      translate([0, 0.4 - 0.005, 0])
        cube([ShooterPlateT, .01, 4.49], center = true);
      translate([0, -1.5, 0])
        rotate(90, [0, 1, 0])
          cylinder(d = 3.8, h = ShooterPlateT, center = true);
      translate([0, -4.875, 0])
        rotate(90, [0, 1, 0])
          cylinder(d = 1, h = ShooterPlateT, center = true);
    }
    //Ramp pivot holes
    translate([0, -ShooterInnerCurveRadius - RampTiltHOffset, 0])
      rotate(90, [0, 1, 0])
        cylinder(d = M4FreeHoleD, h = 2, center = true);
    //Servo attach pivot holes
    translate([0, -ShooterInnerCurveRadius - RampTiltHOffset + (11 / 2.54), 0])
      rotate(90, [0, 1, 0])
        cylinder(d = M4FreeHoleD, h = 2, center = true);
    //Scoop attach holes
    translate([0, -(32 / 25.4), 0])
      ScoopPlateHoles(D = M4FreeHoleD);
  }
}

module ShooterBody2SidePlateFlywheel()
{
  difference()
  {
    hull()
    {
      translate([0, 0.4 - 0.005, 0])
        cube([ShooterPlateT, .01, BallDiameter + BallClearance], center = true);
      translate([0, -2.5, 0])
        rotate(90, [0, 1, 0])
          cylinder(d = BallDiameter + BallClearance, h = ShooterPlateT, center = true);
      translate([0, -4.875, 0])
        rotate(90, [0, 1, 0])
          cylinder(d = 1, h = ShooterPlateT, center = true);
    }
    //Upper/lower plate attach holes
    translate([0, -(8 / 25.4), ((BallDiameter + BallClearance) / 2) - (4 / 25.4)])
      rotate(90, [0, 1, 0])
        rotate(-90, [0, 0, 1])
          HoleSet(D = M4FreeHoleD, C = 4, S = (16 / 25.4));
    translate([0, -(8 / 25.4), -((BallDiameter + BallClearance) / 2) + (4 / 25.4)])
      rotate(90, [0, 1, 0])
        rotate(-90, [0, 0, 1])
          HoleSet(D = M4FreeHoleD, C = 4, S = (16 / 25.4));
    //
    //Ramp pivot holes
    translate([0, -ShooterInnerCurveRadius - RampTiltHOffset, 0])
      rotate(90, [0, 1, 0])
        cylinder(d = M4FreeHoleD, h = 2, center = true);
    //Servo attach pivot holes
    translate([0, -ShooterInnerCurveRadius - RampTiltHOffset + (11 / 2.54), 0])
      rotate(90, [0, 1, 0])
        cylinder(d = M4FreeHoleD, h = 2, center = true);
    
  }
}

module ShooterScoop()
{
}

module ShooterBody2SidePlateScoopAssembly()
{
  translate([ShooterInnerCurveRadius + ShooterPlateT + (ShooterPlateT / 2), 0, 0])
    rotate(180, [0, 0, 1])
  color("blue")
      BallScoop();
  ShooterBody2SidePlateScoop();
//  //Servo attach pivot
//  translate([-RampRotationSupportT - (ShooterPlateT / 2), -ShooterInnerCurveRadius - RampTiltHOffset + (11 / 2.54),0])
//    rotate(90, [0, 1, 0])
//      cylinder(d = 1, h = RampRotationSupportT);
}

module ShooterBasePlate2BottomPlate()
{
  difference()
  {
    ShooterBasePlate2Core();
    translate([.7, -5.1, -.25])
      cube([2, 2, .5]);
    translate([-0.9, 0, 0])
      rotate(90, [0, 0, 1])
        DualHoles(32 / 25.4, M4FreeHoleD, 1);
    translate([-1.9, 0, 0])
      rotate(90, [0, 0, 1])
        DualHoles(32 / 25.4, M4FreeHoleD, 1);
    translate([-2.9, 0, 0])
      rotate(90, [0, 0, 1])
        DualHoles(32 / 25.4, M4FreeHoleD, 1);
  }
}

module ShooterBody2()
{
  //Center on middle of ball exit
//  translate([BallArcRadius, 0, 0])
  //Center on middle of ball entry (approximately)
  translate([BallExitOffset, 0, 0])//0 = center of flywheel
  {        
    RenderMotorBody();
    //Lower base plate
    translate([0, 0, -ShooterPlateT])
      ShooterBasePlate2BottomPlate();

    //Upper base plate
    translate([0, 0, ShooterWallHeight])
      ShooterBasePlate2TopAssembly();
      
    translate([0, 0, (BallDiameter + BallClearance) / 2])
    {
      //Left (scoop) side 
      translate([-ShooterInnerCurveRadius - ShooterPlateT, 0, 0])
        ShooterBody2SidePlateScoopAssembly();
      //Right side plate
      translate([FlywheelClearance - (ShooterPlateT / 2), 0, 0])
        ShooterBody2SidePlateFlywheel();
    }
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

module ServoBlockOpenings(BlockDepth, OpeningLength, OpeningWidth)
{
  //Mount holes
  ServoBlockHoles();
  //Servo opening
  cube([BlockDepth + .5, OpeningLength, OpeningWidth], center = true);
  //Slot opening(s)
  cube([1, 54/25.4, 3/25.4], center = true);
}

module ServoBlock(BlockDepth = .3, BlockWidth = 1.0, BlockLength = ServoBlockLength, OpeningWidth = 21/25.4, OpeningLength = ServoBlockLengthOpening, BlockWidthOffset = -0.05, AddAttachHoles = true, ServoOrientation = 0)
{  
  difference()
  {
    //Base
      cube([BlockDepth, BlockLength, BlockWidth], center = true);
    translate([0, 0, BlockWidthOffset])
    {
      ServoBlockOpenings(BlockDepth, OpeningLength, OpeningWidth);
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
    if (ShowServos)
      translate([.5 - BlockDepth, 0, BlockWidthOffset])
        rotate(ServoOrientation, [1, 0, 0])
          Servo();
  
  //Alignment test
  if (AddAttachHoles && ShowAlignMarkers)
  {
    color("cyan")
      PrintedServoBlockMountHoles(d = 2.9/25.4);
  }
}

module RampPivotPlate()
{
  difference()
  {
    hull()
    {
      rotate(90, [0, 1, 0])
        cylinder(d = 1, h = ShooterPlateT);
      translate([0, -1, RampTiltVOffset - RampZOffset - (ShooterPlateT / 2)])
        cube([ShooterPlateT, 3, 0.001]);
    }
    //Base plate mount holes
    translate([0, -0.55 + (8 / 25.4), RampTiltVOffset - RampZOffset + (ShooterPlateT / 2) + (4 / 25.4)])
      rotate(90, [1, 0, 0])
        rotate(90, [0, 1, 0])
        {
          HoleSet(D = M4FreeHoleD, C = 5, S = (16 / 25.4));
          if (ShowAlignMarkers)
            HoleSet(D = M4FreeHoleD, C = 5, S = (16 / 25.4));
        }
    //Shooter pivot hole
    rotate(90, [0, 1, 0])
      cylinder(d = M4FreeHoleD, h = 1, center = true);
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
    translate([RampSupportT - 0.05, -2, -1.7])
      cube([.06, 3, 4]);
  }
  translate([0, -1.6, RampTiltVOffset - RampZOffset - (ShooterPlateT / 2)])
    cube([RampSupportT + .3, 1.3, .5]);
}

module DualHoles(S, D, H)
{
  translate([-S / 2, 0, 0])
    cylinder(d = D, h = H, center = true);
  translate([S / 2, 0, 0])
    cylinder(d = D, h = H, center = true);
}

module QuadHoles(d, hole, h = 1)
{
  rotate(45, [0, 0, 1])
    translate([d / 2, 0, 0])
      cylinder(d = hole, h , center = true);
  rotate(135, [0, 0, 1])
    translate([d / 2, 0, 0])
      cylinder(d = hole, h , center = true);
  rotate(-45, [0, 0, 1])
    translate([d / 2, 0, 0])
      cylinder(d = hole, h , center = true);
  rotate(-135, [0, 0, 1])
    translate([d / 2, 0, 0])
      cylinder(d = hole, h , center = true);
}

module PrintedServoBlockMountHoles(d)
{
  //ToDo : convert to DualHoles(S, D, H)
  translate([0, (ServoBlockLength / 2) - .15, 0])
    cylinder(d = d, h = 2, center = true);
  translate([0, -(ServoBlockLength / 2) + .15, 0])
    cylinder(d = d, h = 2, center = true);
}


module GoBildaServoBlockMountHoles(d)
{
  //ToDo : Convert to DualHoles(S, D, H)
  translate([0,(1.654 / 2), 0])
    cylinder(d = d, h = 1, center = true);
  translate([0, - (1.654 / 2), 0])
    cylinder(d = d, h = 1, center = true);
}

module RampPlate()
{
  //Main plate with ball entry opening
  difference()
  {
    //Main plate
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset + ((ShooterOuterCurveRadius + FlywheelClearance) / 2) - (ShooterPlateT / 4), 1.756 + .7, 0])
    {
//        translate([0, 1.8, 0])
//        cube([ShooterOuterCurveRadius + FlywheelClearance + (ShooterPlateT / 2) - ShooterPlateT - ShooterPlateT, 3, ShooterPlateT], center = true);
      hull()
      {
        translate([0.01, -0.69, 0])
          cube([ShooterOuterCurveRadius + FlywheelClearance + (ShooterPlateT / 2) - 0.02, 5.6, ShooterPlateT], center = true);
        translate([0.8, -5.2, 0])
          scale([1, .5, 1.0])
            cylinder(d = 6, h = ShooterPlateT, center = true);
        translate([0.8, 1.6, 0])
          scale([1, .5, 1.0])
            cylinder(d = 6, h = ShooterPlateT, center = true);
      }
    }
    //Ball opening
    cylinder(d = LazySusanInnerD + .05, h = 1, center = true);
    //Lazy Susan inner ring attach holes
    QuadHoles(d = LazySusanInnderMountD, hole = LazySusanMountHoleD);
    //Lazy Susan outer access holes
    QuadHoles(d = LazySusanOuterMountD, hole = LazySusanAccessHoleD);
    //Shooter servo block mount holes
    //Left
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset + .08, ServoHOffset - 0.8, -ShooterPlateT / 2])
    {
      //Printed block
      PrintedServoBlockMountHoles(d = M3FreeHoleD);
      //GoBilda block    
      GoBildaServoBlockMountHoles(d = M4FreeHoleD);
    }
    //Right
    translate([BallExitOffset - RampXOffset + FlywheelClearance - .51 + ShooterPlateT + ShooterPlateT, ServoHOffset - 0.8, -ShooterPlateT / 2])    
    {
      //Printed block
      PrintedServoBlockMountHoles(d = M3FreeHoleD);
      //GoBilda block    
      GoBildaServoBlockMountHoles(d = M4FreeHoleD);
    }    
    
    //Turret servo opening
    translate([0, ServoRotateOffset, .1])
      translate(TurretServoLocation)
      {
        rotate(90, [0, 0, 1])
        {
          cube([.85, ServoBlockLengthOpening, .8], center = true);
          rotate(90, [0, 1, 0])
            ServoBlockHoles();
        }
      }
    //Turret sensor opening
    translate(TurretSensorLocation)
      intersection()
      {
        cylinder(d = TurretSensorMountD, h = 1.5, center = true);
        cube([8.5 / 25.4, TurretSensorMountD, 1.5], center = true);
      }
    translate([UpperBallTractionLocation[0], -3.0, ShooterPlateT / 2])
      LowerBallTractionMountHoles();
    //Side support mount holes
    translate([BallExitOffset - RampXOffset + FlywheelClearance - (4 / 25.4), -(3 * 12/25.4) + 0.86733, 0])
      rotate(90, [0, 0, 1])
        HoleSet(D = M4FreeHoleD, C = 4, S = (16 / 25.4));
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset - (ShooterPlateT / 2) + (4/25.4) + 0.02, -(3 * 12/25.4) + 0.86733, 0])//No idea why 0.86733 is needed !!
      rotate(90, [0, 0, 1])
        HoleSet(D = M4FreeHoleD, C = 4, S = (16 / 25.4));
  }
}

module RampPlateAssembly()
{
  RampPlate();
  //Ramp servo support blocks
  if (ShowServoMounts)
  {
    //Left
//    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset  + .15 + 0.025, ServoHOffset + .39 , (ShooterPlateT + 1) / 2])
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset  + .26 - ShooterPlateT, ServoHOffset -.8 , (ShooterPlateT + 1) / 2])
      rotate(180, [0, 1, 0])
        ServoBlock(BlockWidthOffset = 0.05, ServoOrientation = 0);
    //Right
    translate([BallExitOffset - RampXOffset + FlywheelClearance -0.33 + ShooterPlateT, ServoHOffset -0.8, (ShooterPlateT + 1) / 2])
      rotate(180, [0, 1, 0])
        mirror([1, 0, 0])
          ServoBlock(BlockWidthOffset = 0.05, ServoOrientation = 0);
  }
  //Ramp pivot blocks
  //Left
  translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset - ShooterPlateT - (ShooterPlateT / 2) + 0.015, 0, -RampTiltVOffset + RampZOffset])
    //RampPivotBlock();
    RampPivotPlate();
  //Right
  translate([BallExitOffset - RampXOffset + FlywheelClearance + ShooterPlateT, 0,  -RampTiltVOffset + RampZOffset])
    mirror([1, 0, 0])
      //RampPivotBlock();
      RampPivotPlate();
  if (ShowServoMounts)
  {
    //Turret servo mount
    translate([0, ServoRotateOffset, .1 + (.31 / 2)])
      translate(TurretServoLocation)
        rotate(90, [0, 0, 1])
          rotate(90, [0, 1, 0])
            ServoBlock(BlockDepth = .31, BlockWidth = 21/25.41, BlockLength =  2.3, OpeningWidth = 21/25.4, BlockWidthOffset = 0, AddAttachHoles = false, ServoOrientation = 180);
  }
  //Traction blocks
  translate([UpperBallTractionLocation[0], -3.0, ShooterPlateT / 2])
    {
      LowerBallTraction();
    }
}

module RampPlateAssembly_old()
{
  RampPlate();
  //Ramp servo support blocks
  if (ShowServoMounts)
  {
    //Left
    translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset  + .15 + 0.025, ServoHOffset + .39 , (ShooterPlateT + 1) / 2])
      rotate(180, [0, 1, 0])
        ServoBlock();
    //Right
    translate([BallExitOffset - RampXOffset + FlywheelClearance -.15 - 0.025, ServoHOffset + .39 , (ShooterPlateT + 1) / 2])
      rotate(180, [0, 1, 0])
        mirror([1, 0, 0])
          ServoBlock();
  }
  //Ramp pivot blocks
  //Left
  translate([-ShooterOuterCurveRadius + BallExitOffset - RampXOffset - ShooterPlateT - (ShooterPlateT / 2), 0, -RampTiltVOffset + RampZOffset])
    //RampPivotBlock();
    RampPivotPlate();
  //Right
  translate([BallExitOffset - RampXOffset + FlywheelClearance + ShooterPlateT, 0,  -RampTiltVOffset + RampZOffset])
    mirror([1, 0, 0])
      //RampPivotBlock();
      RampPivotPlate();
  //Turret servo mount
  translate([0, ServoRotateOffset, .1 + (.31 / 2)])
    translate(TurretServoLocation)
      rotate(90, [0, 0, 1])
      rotate(90, [0, 1, 0])
        ServoBlock(BlockDepth = .31, BlockWidth = 21/25.41, BlockLength =  2.3, OpeningWidth = 21/25.4, BlockWidthOffset = 0, AddAttachHoles = false);
  //Traction blocks
  translate([UpperBallTractionLocation[0], -3.0, ShooterPlateT / 2])
    {
      LowerBallTraction();
    }
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
    cylinder(d = M3FreeHoleD, h = .21, center = true);
    translate([150/25.4, 0, 0])
      cylinder(d = M4FreeHoleD, h = .21, center = true);
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

module TurretMountPlate()
{
  difference()
  {
    hull()
    {
      cylinder(d = LazySusanOuterD + 1, h = ShooterPlateT, center = true);
      cube([RobotOuterWidth + ShooterPlateT + ShooterPlateT, 3, ShooterPlateT], center = true);
    }
    //Shooter ball opening
    cylinder(d = LazySusanInnerD, h = ShooterPlateT + 0.1, center = true);
    //Lazy Susan Attachholes
    QuadHoles(d = LazySusanOuterMountD, hole = M5FreeHoleD);
    //Base mount holes
    for (i = [-4 : 4])
    {      
      translate([(RobotOuterWidth / 2) - (8 / 25.4), (i * 8) / 25.4, 0])
        cylinder(d = M4FreeHoleD, h = .5, center = true);
      translate([(RobotOuterWidth / 2) - ((8 + 32) / 25.4), (i * 8) / 25.4, 0])
        cylinder(d = M4FreeHoleD, h = .5, center = true);
      translate([-(RobotOuterWidth / 2) + (8 / 25.4), (i * 8) / 25.4, 0])
        cylinder(d = M4FreeHoleD, h = .5, center = true);
      translate([-(RobotOuterWidth / 2) + ((8 + 32) / 25.4), (i * 8) / 25.4, 0])
        cylinder(d = M4FreeHoleD, h = .5, center = true);
    }
    
    
  }
}

module LazySusanOuterSpacer()
{
  difference()
  {
    cylinder(d = LazySusanOuterD, h = .039, center = true);
    cylinder(d = (LazySusanOuterD + LazySusanInnerD) / 2, h = 1, center = true);
    QuadHoles(d = LazySusanOuterMountD, hole = LazySusanMountHoleD);
  }
}

module DriveBase()
{
  SwyftSet();
}


module IntakePlate()
{
  difference()
  {
    translate([(-21.5 / 25.4), -1, -ShooterPlateT])
      cube([44 / 25.4, IntakeArmLength, ShooterPlateT]);
    //Motor mount holes
    translate([0, 1.1, 0])
      DualHoles(32/25.4, M4FreeHoleD, 1);
    translate([0, 2.1, 0])
      DualHoles(32/25.4, M4FreeHoleD, 1);
    translate([0, 3.1, 0])
      DualHoles(32/25.4, M4FreeHoleD, 1);
    //Bearing mounts
    translate([-(16 / 25.4), 0, 0])
      rotate(90, [0, 0, 1])
        DualHoles(32/25.4, M4FreeHoleD, 1);
    translate([(16 / 25.4), 0, 0])
      rotate(90, [0, 0, 1])
        DualHoles(32/25.4, M4FreeHoleD, 1);
    //Rotation mount
    translate([0, IntakeArmLength - 1 - (4 / 25.4), 0])
      HoleSet(D = M4FreeHoleD, C = 5, S = (8 / 25.4), Center = true);
  }
}

module IntakeDriveAssembly()
{
  translate([0, 0, -24/25.4])
  {
    IntakePlate();
    translate([0, IntakeArmLength - 1, 0])
    if (ShowAlignMarkers)
      rotate(90, [0, 1, 0])
#        cylinder(d = M4FreeHoleD, h = 4, center = true);
  }
  translate([0, 1.2, 0])
    rotate(90, [1, 0,0])
      MotorBody();
}


module IntakeAssembly()
{  
  translate([0, -IntakeArmLength + 1, 24 / 25.4])
  {
    //Right side
    translate([((RobotOuterWidth - 3.55) / 2) + (42 / 25.4) / 2, 0, 0])
      IntakeDriveAssembly();
    rotate(90, [0, 1, 0])
    {
      color("Gray", 0.5)
        cylinder(d = IntakeD, h = RobotOuterWidth - 4.2, center = true);
      color("DarkGray", 0.5)
        cylinder(d = 8/25.4, h = RobotOuterWidth, center = true, $fn = 6);
    }
  }
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

module BallStore1()
{  
  rotate(BallStoreAngle, [0, 0, 1])
  {
    translate([0, 0, -5.2 / 2])
      rotate(-30, [0, 0, 1])
        cylinder(d = 2.8, h = 5.2, $fn = 3);
    translate([0, -BallStoreRadius, 0])
      Ball();
    rotate(BallStoreSpacingAngle, [0, 0, 1])
      translate([0, -BallStoreRadius, 0])
        Ball();
    rotate(-BallStoreSpacingAngle, [0, 0, 1])
      translate([0, -BallStoreRadius, 0])
        Ball();
  }
  intersection()
  {
    sphere(d = 11.6);
    translate([0, 0, -2.0])
      cube([11.6, 11.6, 1], center = true);
  }
}

module StoreBottomPlateMountHoles()
{
  translate([5.1, 2.4, 0])
    cylinder(d = 4.1 / 25.4, h = 1, center = true);
  translate([5.1, -1.5, 0])
    cylinder(d = 4.1 / 25.4, h = 1, center = true);
  translate([-2.7, 2.4, 0])
    cylinder(d = 4.1 / 25.4, h = 1, center = true);
  translate([-2.7, -1.5, 0])
    cylinder(d = 4.1 / 25.4, h = 1, center = true);
  translate([5.1, -1.0, 0])
    cylinder(d = 4.1 / 25.4, h = 1, center = true);
  translate([-2.7, -1.0, 0])
    cylinder(d = 4.1 / 25.4, h = 1, center = true);

}

module BallStore2()
{
  translate([0, 0, .15])
  {
    if (ShowAlignMarkers)
    {
      //Pusher ball
      translate([0, 0, -4])
        Ball();
      //Ramp ball 1
      translate([2.6, -4.2, -3.2])
        Ball();
      //Ramp ball 2
      translate([-2.6, -4.2, -3.2])
        Ball();
    }
    translate([0, 0, -6.5])
    {
      //Main ball ramp
      hull()
      {
        translate([0, -1, 0.1])
          cube([5, .1, .1], center = true);
        translate([0, -5, .8])
          cube([8.5, .1, .1], center = true);
      }
      
      //Ball cylinder channel and servo support
      difference()
      {
        union()
        {
          //Outer ball storage
          cylinder(d = 5.5, h = 5.5);
          //Servo support
          translate([1.9, 1.1, 0])
          {
            difference()
            {
              cube([3.5, .5, 5.5]);
              translate(BallLifterOffset)
                rotate(-90, [0, 0, 1])
                  rotate(-90, [1, 0, 0])
                  {
                    ServoBlockOpenings(BlockDepth = .3, OpeningWidth = 21/25.4, OpeningLength = ServoBlockLengthOpening);
                    if (ShowServos)
                    {
  #                    Servo();
                      translate([0, -ServoRotateOffset, 0])
                      rotate(90, [0, 1, 0])
  #                    ServoShaft();
                    }
                  }
            }
          }
          //Bottom plate
          translate([-3, -2, -0.1])
            cube([8.4, 4.75, .1]);
        }
        StoreBottomPlateMountHoles();
        //Inner ball storage
        translate([0, 0, 0])
          cylinder(d = 5.3, h = 6.2);
        //Cut off front opening
        translate([-3, -7.5, 0])
          cube([6, 6, 6.2]);
        //Cut off pusher opening
        translate([0, -4.8, 0])
          cube([6, 6, 6.2]);
      }
    }
  }
}

module SonicHubHoles()
{
  translate([0, 0, 0.6])
  {
    cylinder(d = 8.4 / 25.5, h = 3, $fn = 6, center = true); //Servo hex shaft
    //GoBilda
    QuadHoles(d = .891, hole = 4.2/25.4, h = 1.5);
  }
}

module BallLifter()
{
 BallLifterArmLength = 4.5;//4.8
  
  translate([1.9, 0, -6.5 + ServoRotateOffset])
  {
    translate(BallLifterOffset)
    {
      rotate(90 + BallLifterAngle, [0, 1, 0])
      {
        difference()
        {
          hull()
          {
            translate([0, 0.7, 0])
              rotate(90, [1, 0, 0])
              cylinder(d = 1.5, h = .5, center = true);
            translate([BallLifterArmLength, 0, 0])
              cylinder(d = 2, h = .5, center = true);
          }
          translate([BallLifterArmLength, 0, -2.5])
            sphere(d = 5.2);
          translate([5.8, 0, 0])
            rotate(-30, [0, 1, 0])
              cube([1, 3, 3], center = true);
          
          rotate(-90, [1, 0, 0])
          {
            //cylinder(d = 5/25.4, h = 3, $fn = 6); //Servo screw access
            //Servo hub flattener
            translate([0, 0, -0.55])
              cylinder(d = 1.5, h = 1);
            //Servo hub mount holes
            //Amazon
            translate([0, 0, 0.6])
              rotate(45, [0, 0, 1])
                QuadHoles(d = .275 * 2, hole = 3.2/25.4);
            //GoBilda Sonic Hub
            SonicHubHoles();
          }
          //Band attach hole
          translate([BallLifterArmLength + .4, -.7, 0])
            cylinder(d = .2, h = 1, center = true);
        }

      }
    }
  }
}

module IntakeSpinner()
{
  translate([0, -8, -4.5])
    Ball();
  translate([0, -9, -2])
    rotate(90, [0, 1, 0])
      cylinder(d = 1, h = 10, center = true);
}

module SwyftSet()
{  
  translate([-RobotOuterWidth / 2, WheelSpacingOffset, DriveBaseVOffset])
    Swyft();
  translate([RobotOuterWidth / 2, WheelSpacingOffset, DriveBaseVOffset])
    rotate(180, [0, 1, 0])
      Swyft();
  translate([-RobotOuterWidth / 2, -WheelSpacingOffset, DriveBaseVOffset])
    Swyft();
  translate([RobotOuterWidth / 2, -WheelSpacingOffset, DriveBaseVOffset])
    rotate(180, [0, 1, 0])
      Swyft();

}

module Swyft()
{
  rotate(90, [0, 1, 0])
  {
    cylinder(d = 86/25.4, h = 48/25.4);
    cylinder(d = 38/25.4, h = 88/25.4);
  }
}

module SwyftHolesInner(MotorSlide = true)
{
  InnerD = 39;
  rotate(-90, [0, 0, 1])
  {
    for (i = [0:3])
      rotate((i * 90), [0, 0, 1])
        translate([22/25.4, 0, 0])
          cylinder(d = M3FreeHoleD, h = 1, center = true);
    cylinder(d = InnerD/25.4, h = 1, center = true);
    if (MotorSlide)
      translate([0, -(InnerD/2)/25.4, 0])
      cube([InnerD/25.4, InnerD/25.4, 1], center = true);
  }
}

module SwyftHolesOuter(DoFull = false)
{
  rotate(-90, [0, 0, 1])
  {
    if (DoFull)
      for (i = [-3:2])
        translate([(i * (8/25.4)) + (4/25.4), 12/25.4, 0])
          cylinder(d = M4FreeHoleD, h = 1, center = true);
    else
    {
      translate([(-3 * (8/25.4)) + (4/25.4), 12/25.4, 0])
        cylinder(d = M4FreeHoleD, h = 1, center = true);
      translate([(2 * (8/25.4)) + (4/25.4), 12/25.4, 0])
        cylinder(d = M4FreeHoleD, h = 1, center = true);
    }
    //4 lower holes
    if (DoFull)
    {
      translate([(2 * (8/25.4)) + (4/25.4), -4/25.4, 0])
        cylinder(d = M4FreeHoleD, h = 1, center = true);
      translate([(-3 * (8/25.4)) + (4/25.4), -4/25.4, 0])
        cylinder(d = M4FreeHoleD, h = 1, center = true);
      //2 upper holes
      translate([(1 * (8/25.4)) + (4/25.4), 20/25.4, 0])
        cylinder(d = M4FreeHoleD, h = 1, center = true);
      translate([(-2 * (8/25.4)) + (4/25.4), 20/25.4, 0])
        cylinder(d = M4FreeHoleD, h = 1, center = true);
    }
    translate([(2 * (8/25.4)) + (4/25.4), -12/25.4, 0])
      cylinder(d = M4FreeHoleD, h = 1, center = true);
    translate([(-3 * (8/25.4)) + (4/25.4), -12/25.4, 0])
      cylinder(d = M4FreeHoleD, h = 1, center = true);
  }
}

module LeftSideAssembly()
{
  translate([RobotOuterWidth / 2, 0, DriveBaseVOffset - 1.2])
  {
    //Outer plate
    translate([ShooterPlateT / 2, 0, 0])
      rotate(-90, [0, 1, 0])
        SidePlateOuter();
    //Inner plate
    translate([-(ShooterPlateT / 2) - (48 / 25.4), 0, 0])
      rotate(-90, [0, 1, 0])
        SidePlateInner();
    if (ShowAlignMarkers)
    color("Orange")
      translate([-5.1, 3.1, 3.3])
      rotate(90, [0, 1, 0])
      {
        RevHub();
#        RevHubHoles();
      }
  }
}

module RevMountPlate()
{
  translate([0, 2.7, .45])
  {
  difference()
  {
    hull()
    {
      cube([RobotOuterWidth - (96 / 25.4) - ShooterPlateT - ShooterPlateT, ShooterPlateT, 5], center = true);
      translate([0, 0, 3.2])
        cube([10, ShooterPlateT, .1], center = true);
    }
    //Bellypan attach holes
    translate([0.1575 - (8 / 25.4), 0, -(5/2) + (4 / 25.4)])
      rotate(90, [1, 0, 0])
        HoleSet(D = M4FreeHoleD, C = 19, S = (16 / 25.4), Center = true);
    //Rev hub mount options
    //Vertical
    translate([-2.5, 0, 0.35])
      RevHubHoles();
    translate([2.5, 0, 0.35])
      RevHubHoles();
    //Horizontal
    translate([2.932, 0, 0.725])
      rotate(90, [0, 1, 0])
        RevHubHoles();
    translate([-2.932, 0, 0.725])
      rotate(90, [0, 1, 0])
        RevHubHoles();
    //Side assembly attach holes
    translate([(RobotOuterWidth / 2) - (48 / 25.4) - ShooterPlateT - (4 / 25.4), 0, -1.945 + (8 / 25.4)])
      rotate(-90, [0, 1, 0])
        rotate(90, [1, 0, 0])
          HoleSet(D = M4FreeHoleD, C = 7, S = (16 / 25.4));
  }
}
}

module RightSideAssembly()
{
  mirror([1, 0, 0])
  translate([RobotOuterWidth / 2, 0, DriveBaseVOffset - 1.2])
  {
    //Outer plate
    translate([ShooterPlateT / 2, 0, 0])
      rotate(-90, [0, 1, 0])
        SidePlateOuter();
    //Inner plate
    translate([-(ShooterPlateT / 2) - (48 / 25.4), 0, 0])
      rotate(-90, [0, 1, 0])
        SidePlateInner();
    if (ShowAlignMarkers)
    color("Orange")
      translate([-5.53, 3, 2.925])
      {
        RevHub();
#          RevHubHoles();
      }
  }
}

module RevHubHoles()
{
  rotate(-90, [1, 0, 0])
  {
    translate([(44 / 25.4), (64 / 25.4), 0])
      cylinder(d = (3.5 / 25.4), h = 3, center = true);
    translate([-(44 / 25.4), (64 / 25.4), 0])
      cylinder(d = (3.5 / 25.4), h = 3, center = true);
    translate([(44 / 25.4), -(64 / 25.4), 0])
      cylinder(d = (3.5 / 25.4), h = 3, center = true);
    translate([-(44 / 25.4), -(64 / 25.4), 0])
      cylinder(d = (3.5 / 25.4), h = 3, center = true);
  }
}

module RevHub()
{
  rotate(90, [0, 1, 0])
    translate([0, (30 / 2)/25.4, 0])
      difference()
      {
        cube([143 / 25.4, 30/25.4, 103/25.4], center = true);
        RevHubHoles();
      }
}

module SidePlateOuter()
{
  difference()
  {
    SidePlateCore();
    //Drive wheel mounts
    translate([1.2, WheelSpacingOffset, 0])
      SwyftHolesOuter(MotorSlide = true);
    translate([1.2, -WheelSpacingOffset, 0])
      SwyftHolesOuter(MotorSlide = true);
  }
}

module SidePlateInner()
{
  difference()
  {
    SidePlateCore();
    translate([1.2, WheelSpacingOffset, 0])
      SwyftHolesInner(MotorSlide = true);
    translate([1.2, -WheelSpacingOffset, 0])
      SwyftHolesInner(MotorSlide = true);
    //Back plate mount holes
    translate([0.315 + (8 / 25.4), 2.834, 0])
      HoleSet(M4FreeHoleD, 8, (16 / 25.4));
    //Elevator support mount holes
    translate([3.9, 5.8, 0])
      rotate(90, [0, 0, 1])
        HoleSet(M4FreeHoleD, 5, (8 / 25.4), Center = true);
    
  }
}

module SidePlateCore()
{
  difference()
  {
    hull()
    {
      //Ground level
      translate([0.3, -(RobotOuterLength  - 0.5)/ 2, 0])
        cylinder(d = .5, h = ShooterPlateT, center = true);
      translate([0.3, (RobotOuterLength  - 0.5)/ 2, 0])
        cylinder(d = .5, h = ShooterPlateT, center = true);
      //Upper back
      translate([3.3, -(RobotOuterLength  - 5)/ 2, 0])
        cylinder(d = 5, h = ShooterPlateT, center = true);
      //Upper front
      translate([2.9, (RobotOuterLength  - 4)/ 2, 0])
        cylinder(d = 4, h = ShooterPlateT, center = true);
      //Top
      translate([5.31, 0, 0])
        cube([1, 3, ShooterPlateT], center = true);
    }
    //Upper channel mount holes
    for (i = [-4:4])
      translate([5.81 - (8 / 25.4), (8 / 25.4) * i, 0])
        cylinder(d = M4FreeHoleD, h = .5, center = true);
    //Bottom channel mount holes
    for (i = [-6:6])
      translate([(8 / 25.4) + .05, (16 / 25.4) * i, 0])
        cylinder(d = M4FreeHoleD, h = .5, center = true);
    //Mid channel mount holes
    for (i = [-7:7])
      translate([3.4, (24 / 25.4) * i, 0])
        cylinder(d = M4FreeHoleD, h = 2.5, center = true);
    //Front/back protector holes
    translate([.5, (RobotOuterLength  - (9/25.4))/ 2, 0])
      HoleSet(D = M4FreeHoleD, C = 4, S = (16 / 25.4));
    translate([.5, -(RobotOuterLength  - (9/25.4))/ 2, 0])
      HoleSet(D = M4FreeHoleD, C = 4, S = (16 / 25.4));
    //Odometry pod mounts
    translate([-0.5 + (35/25.4), (10/25.4), 0])
      HoleSet(D = M4FreeHoleD, C = 2, S = (32 / 25.4));
    translate([-0.5 + (35/25.4), (60/25.4), 0])
      HoleSet(D = M4FreeHoleD, C = 2, S = (32 / 25.4));
    //Intake pivot options
    translate([3.75, -1.1, 0])
      rotate(-90, [0, 0, 1])
        HoleSet(D = M4FreeHoleD, C = 6, S = (8 / 25.4));
    //Intake angle stop options
    translate([3.85, -6.5, 0])
      HoleSet(D = M4FreeHoleD, C = 3, S = (8 / 25.4));
  }  
}

module BellyPan()
{
  RampExtension = 1.7;
  difference()
  {
    translate([0, -RampExtension / 2, 0])
      cube([RobotOuterWidth, RobotOuterLength - 3.5 + RampExtension, ShooterPlateT], center = true);
    //Ramp slots
    for (i = [-2:2])
      translate([(i * 1.5), -(RobotOuterLength - 3.5) / 2, 0])
        cube([.8, 0.05, 1], center = true);
    //Wheel cutouts
    translate([(RobotOuterWidth / 2) - (24 / 25.4), WheelSpacingOffset + .3, 0])
    {
      cube([48/25.4, 4, 1], center = true);
      translate([0, -.3, 0])
        cube([6, 2, 1], center = true);
    }
    mirror([1, 0, 0])
    translate([(RobotOuterWidth / 2) - (24 / 25.4), WheelSpacingOffset + .3, 0])
    {
      cube([48/25.4, 4, 1], center = true);
      translate([0, -.3, 0])
        cube([6, 2, 1], center = true);
    }
    mirror([0, 1, 0])
    {
      //Wheel cutouts
      translate([(RobotOuterWidth / 2) - (24 / 25.4), WheelSpacingOffset + .3, 0])
      {
        cube([48/25.4, 4, 1], center = true);
        translate([0, -.3, 0])
          cube([6, 2, 1], center = true);
      }
      mirror([1, 0, 0])
      translate([(RobotOuterWidth / 2) - (24 / 25.4), WheelSpacingOffset + .3, 0])
      {
        cube([48/25.4, 4, 1], center = true);
        translate([0, -.3, 0])
          cube([6, 2, 1], center = true);
      }
    }
    //Odometry pod cutouts
    translate([(RobotOuterWidth / 2) - (21.5 / 25.4), -0.4, 0])
      cube([54/25.4, 45/25.4, 1], center = true);
    translate([-((RobotOuterWidth / 2) - (21.5 / 25.4)), -0.4, 0])
      cube([54/25.4, 45/25.4, 1], center = true);
    //Outer attach holes
    translate([(RobotOuterWidth / 2) - (8 / 25.4), -((8 * 11) / 25.4), 0])
      rotate(90, [0, 0, 1])
        HoleSet(D = M4FreeHoleD, C = 12, S = (16 / 25.4));
    translate([(RobotOuterWidth / 2) - (40 / 25.4), -((8 * 11) / 25.4), 0])
      rotate(90, [0, 0, 1])
        HoleSet(D = M4FreeHoleD, C = 12, S = (16 / 25.4));
    mirror([1, 0, 0])
    {
      //Outer attach holes
      translate([(RobotOuterWidth / 2) - (8 / 25.4), -((8 * 11) / 25.4), 0])
        rotate(90, [0, 0, 1])
          HoleSet(D = M4FreeHoleD, C = 12, S = (16 / 25.4));
      translate([(RobotOuterWidth / 2) - (40 / 25.4), -((8 * 11) / 25.4), 0])
        rotate(90, [0, 0, 1])
          HoleSet(D = M4FreeHoleD, C = 12, S = (16 / 25.4));
    }
    //Rev mount plate lower mount holes
    translate([(4 / 25.4), ((9 * 8) / 25.4), 0])
      HoleSet(D = M4FreeHoleD, C = 21, S = (16/ 25.4), Center = true);
    
  }
}

module IntakeWheel()
{
  difference()
  {
    cylinder(d = IntakeD + .25, h = .3, center = true);
    translate([0, 0, -0.5])
      SonicHubHoles();

    for (i = [0:10])
      rotate((360/11) * i, [0, 0, 1])
        translate([0, IntakeD / 2, 0])
        {
          cube([.4, .1, .5], center = true);
          translate([0, .2, 0])
            cube([.07, .5, .5], center = true);
        }
  }
}

module Everything()
{
  rotate(TurretAngle, [0, 0, 1])
  {
    if (ShowShooter)
      Shooter();
    
    if (ShowRampPlate)
      RampPlateAssembly();
    
    TurretServo();
  }
  
  if (ShowGears)
    translate([0, 0, -.2])
      LazySusan();

  if (ShowTurretMountPlate)
    translate([0, 0, -.6])
      TurretMountPlate();
  
  if (ShowDriveBase)
    DriveBase();

  if (ShowBallLifter)
    BallLifter();


  if (ShowBallStore == 1)
  {
    translate([0-3, 0, -3.4])
      BallStore1();
  }
  else if (ShowBallStore == 2)
    BallStore2();

  if (ShowIntake)
  {
    translate([0, -7.5, -4.5])
      Ball();
    translate([0, -1.8, -3])
      rotate(-15, [1, 0, 0])
        IntakeAssembly();
  }
  
  if (ShowLeftSideAssembly)
    LeftSideAssembly();

  if (ShowRightSideAssembly)
    RightSideAssembly();

  if (ShowBellypan)
    translate([0, 0, -6.5])
      BellyPan();

  if (ShowRevMountPlate)
    translate([0, 0.33, -4.375])
      RevMountPlate();


  if (ShowLimits)
    translate([0, -(LimitBounds[1] / 2) + 7.5, (LimitBounds[2] / 2) - 14.5])
      Limits();
  
}

module DXF()
{
  projection(cut = false)
  //ShooterBasePlate2BottomPlate();
  //ShooterBasePlate2TopPlate();
  //rotate(90, [0, 1, 0]) ShooterBody2SidePlateScoop();
  //rotate(90, [0, 1, 0]) ShooterBody2SidePlateFlywheel();
  //rotate(90, [0, 1, 0]) RampPivotPlate();
  //RampPlate();
  //TurretMountPlate();
  //SidePlateOuter();
  //SidePlateInner();
  //IntakePlate();
  //BellyPan();
  //rotate(90, [1, 0, 0]) RevMountPlate();
  Linkage();
  
}

translate([0, 0, 7])
  Everything();
//scale(25.4)
//  DXF();

//IntakeSpinner();


//ServoBlock();

//TurretSensorGear();

//Linkage();
//LazySusanInnerSpacer();
//LazySusanOuterSpacer();


//SensorTest();

//intersection()
//{
//  TurretSensorGear();
//  cube([.6, .6, 2], center = true);
//}


//TurretServoGear();


IntakeWheel();
