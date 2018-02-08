package io.silverspoom.bulldog.minnowboard.gpio;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.base.AbstractDigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;

public class MinnowDigitalOutput extends AbstractDigitalOutput {
    public MinnowDigitalOutput(Pin pin) {
        super(pin);
    }

    @Override
    protected void applySignalImpl(Signal signal) {

    }

    @Override
    protected void setupImpl() {

    }

    @Override
    protected void teardownImpl() {

    }
}
