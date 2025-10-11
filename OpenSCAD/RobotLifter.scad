ShowBearings = false;
Stage1Height = 50;
Stage2Height = 50;
PlateT = 3.3;
Stage1L = 350;
Stage2L = 300;
Stage3L = 250;
M4HoleFree = 4.3;

BalancePlateGussetL = 8*7;

//V624ZZ for vgrove pulleys
// or https://www.amazon.com/PATIKIL-U-Groove-Bearing-4x13x4mm-Bearings/dp/B0D4LPFWV6


module Stop(){};

$fn = 40;

ChannelD = 48;
ChannelLowD = 12;
ChannelLowW = 48;
ChannelLowMiniW = 32;
ChannelT = 2.5;

BearingMedD = 18;
BearingMedT = 4;
BearingSmallD = 10;
BearingSmallT = 4;
BearingVD = 13;

module Bearing(o, i, t)
{
  if (ShowBearings)
  color("silver", 0.5)
    rotate(90, [1, 0, 0])
      difference()
      {
        cylinder(d = o, h = t, center = true);
        cylinder(d = i, h = t + 0.2, center = true);
      }
}

module BearingPair(o, i, t, w)
{
  translate([0, (w + t) / 2, 0])
    Bearing(o, i, t);
  translate([0, -(w + t) / 2, 0])
    Bearing(o, i, t);
}

module Channel(d, w, l)
{
  translate([0, -w / 2, 0])
    difference()
    {
      cube([d, w, l]);
      translate([ChannelT, ChannelT, -0.1])
        cube([d, w - ChannelT - ChannelT, l + .2]);
      translate([8, -0.1, 8])
        rotate(-90, [1, 0, 0])
          cylinder(d = 4, h = w + .2);
      translate([8, -0.1, l - 8])
        rotate(-90, [1, 0, 0])
          cylinder(d = 4, h = w + .2);
    }
}

module OuterBearingGuide()
{
  BearingOffset = -(BearingSmallD/2) - ChannelT - ((BearingSmallD - 8) / 2) - ChannelLowD;//-((BearingSmallD / 2) + ChannelLowD);
  
  rotate(-90, [1, 0, 0])
  {
    difference()
    {
      hull()
      {
        //Channel side attach
        cube([ChannelLowD - ChannelT, 36, PlateT]);
        //Channel bearings
        translate([BearingOffset, BearingSmallD/2, 0])
          cylinder(d = 10, h = PlateT);
        translate([BearingOffset, (BearingSmallD/2) + 12, 0])
          cylinder(d = 10, h = PlateT);
        //Cable pulley
        translate([0, -8, 0])
          cylinder(d = 10, h = PlateT);
      }
      for (i = [1:4])
      {
        translate([8 - ChannelT, (i * 8), 0])
          cylinder(d = M4HoleFree, h = 10, center = true);
      }
      translate([BearingOffset, BearingSmallD/2, -0.1])
        cylinder(d = M4HoleFree, h = PlateT + .2);
      translate([BearingOffset, (BearingSmallD/2) + 12, -0.1])
        cylinder(d = M4HoleFree, h = PlateT + .2);
        translate([0, -8, -0.1])
          cylinder(d = M4HoleFree, h = PlateT + .2);
    }
      if (ShowBearings)
      color("silver", 0.5)
      {
        translate([BearingOffset, BearingSmallD/2, 4])
          cylinder(d = 10, h = PlateT);
        translate([BearingOffset, (BearingSmallD/2) + 12, 4])
          cylinder(d = 10, h = PlateT);
        translate([0, -8, 15])
          cylinder(d = BearingVD, h = PlateT + .2);
      }
  }
}

module Stage1()
{
  //First stage inner travel
    Channel(d = ChannelLowD, w = ChannelLowMiniW, l = Stage1L);
    translate([8, 0, 8])  
      BearingPair(o = BearingSmallD, i = 4, t = BearingSmallT, w = ChannelLowMiniW);
    translate([8, 0, 24])  
      BearingPair(o = BearingSmallD, i = 4, t = BearingSmallT, w = ChannelLowMiniW);
}

module Stage2()
{
  //Second stage outer fixed
  translate([ChannelLowD + ((BearingSmallD - 8) / 2), 0, 0])
  {
    {
      //Main channel
      Channel(d = ChannelLowD, w = ChannelLowW, l = Stage2L);
      //Upper bearing guides
      translate([ChannelT, -(ChannelLowW / 2) - PlateT, 300])
        OuterBearingGuide();
      mirror([0, 1, 0])
        translate([ChannelT, -(ChannelLowW / 2) - ChannelT, 300])
          OuterBearingGuide();
      //Lower brearings
    translate([8, 0, 8])  
      BearingPair(o = BearingSmallD, i = 4, t = BearingSmallT, w = ChannelLowW);
    translate([8, 0, 24])  
      BearingPair(o = BearingSmallD, i = 4, t = BearingSmallT, w = ChannelLowW);
    }
  }
}

module Stage3()
{
  //Second stage outer fixed
  translate([ChannelLowD + ChannelLowD + ((BearingSmallD - 8) / 2) + ((BearingSmallD - 8) / 2), 0, 0])
  {
    {
      //Main channel
      Channel(d = ChannelLowD, w = ChannelLowW, l = Stage3L);
      //Upper bearing guides
      translate([ChannelT, -(ChannelLowW / 2) - PlateT, Stage3L])
        OuterBearingGuide();
      mirror([0, 1, 0])
        translate([ChannelT, -(ChannelLowW / 2) - ChannelT, Stage3L])
          OuterBearingGuide();
    }
  }
}

module LifterExtender()
{
  translate([0, 0, Stage2Height])
  {
    translate([0, 0, Stage1Height])
      Stage1();
    Stage2();
  }
  Stage3();
  translate([ChannelLowD + ChannelLowD + ((BearingSmallD - 8) / 2) + ((BearingSmallD - 8) / 2) + 8, ChannelLowW / 2, 0])
    BalancePlateGusset();
  mirror([0, 1, 0])
  translate([ChannelLowD + ChannelLowD + ((BearingSmallD - 8) / 2) + ((BearingSmallD - 8) / 2) + 8, ChannelLowW / 2, 0])
    BalancePlateGusset();

}

module BalancePlateGusset()
{
  HoleCount = floor(BalancePlateGussetL / 8);
  
  rotate(-90, [1, 0,0])
    difference()
    {
      hull()
      {
        cylinder(d = 10, h = PlateT);
        translate([-BalancePlateGussetL, 0, 0])
          cylinder(d = 10, h = PlateT);
        translate([0, -BalancePlateGussetL, 0])
          cylinder(d = 10, h = PlateT);
      }
      for (i = [0:HoleCount])
      {
        translate([0, -(8*i), 0])
          cylinder(d = M4HoleFree, h = 10, center = true);
        translate([-(8*i), 0, 0])
          cylinder(d = M4HoleFree, h = 10, center = true);
        translate([-(HoleCount * 8) + (8*i), -(8*i), 0])
          cylinder(d = M4HoleFree, h = 10, center = true);
      }
    }
}

module Lifter()
{
  translate([0, 150, 0])
    LifterExtender();
  mirror([0, 1, 0])
    translate([0, 150, 0])
      LifterExtender();
}

Lifter();

//BalancePlateGusset();
//OuterBearingGuide();