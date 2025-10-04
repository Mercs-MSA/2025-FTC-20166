package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SubSystemRobotID {

    private DigitalChannel limitSwitch;
    public SubSystemRobotID(HardwareMap hardwareMap) throws InterruptedException {
        limitSwitch = hardwareMap.get(DigitalChannel.class, "limitSwitch");
        limitSwitch.setMode(DigitalChannel.Mode.INPUT);

    }
    public int getRobotID()
    {
        if (limitSwitch.getState() == false) {
            return 0;
        } else
        {
            return 1;
        }
    }

    }
