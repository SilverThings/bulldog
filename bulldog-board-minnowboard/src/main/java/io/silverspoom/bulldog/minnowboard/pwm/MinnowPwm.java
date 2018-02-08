package io.silverspoom.bulldog.minnowboard.pwm;

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.pwm.AbstractPwm;

public class MinnowPwm extends AbstractPwm {

    public MinnowPwm(Pin pin) {
        super(pin);
    }

    @Override
    protected void setPwmImpl(double frequency, double duty) {

    }

    @Override
    protected void enableImpl() {

    }

    @Override
    protected void disableImpl() {

    }

    @Override
    protected void setupImpl() {

    }

    @Override
    protected void teardownImpl() {

    }
}
