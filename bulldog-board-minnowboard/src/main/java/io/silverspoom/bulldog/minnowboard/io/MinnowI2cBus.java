package io.silverspoom.bulldog.minnowboard.io;

import io.silverspoon.bulldog.linux.io.LinuxI2cBus;

public class MinnowI2cBus extends LinuxI2cBus {

    public MinnowI2cBus(String name, String deviceFilePath) {
        super(name, deviceFilePath);
    }

}
