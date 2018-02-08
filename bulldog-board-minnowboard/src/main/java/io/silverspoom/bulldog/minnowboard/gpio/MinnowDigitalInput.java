package io.silverspoom.bulldog.minnowboard.gpio;

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.linux.gpio.LinuxDigitalInput;
import io.silverspoon.bulldog.linux.io.LinuxEpollListener;

public class MinnowDigitalInput extends LinuxDigitalInput implements LinuxEpollListener {

    public MinnowDigitalInput(Pin pin) {
        super(pin);
    }
}
