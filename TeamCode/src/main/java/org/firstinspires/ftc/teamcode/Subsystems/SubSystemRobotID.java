package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SubSystemRobotID {

    private DigitalChannel limitSwitch;
    private DigitalChannel limitSwitch2;
    public SubSystemRobotID(HardwareMap hardwareMap) throws InterruptedException {
        limitSwitch = hardwareMap.get(DigitalChannel.class, "limitSwitch");
        limitSwitch.setMode(DigitalChannel.Mode.INPUT);

        limitSwitch2 = hardwareMap.get(DigitalChannel.class, "limitSwitch2");
        limitSwitch2.setMode(DigitalChannel.Mode.INPUT);

    }
    public int getRobotID()
    {
        if (limitSwitch.getState() && limitSwitch2.getState()) {
            return 0;
        }
        else if (!limitSwitch.getState() && limitSwitch2.getState())
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }

    }