package io.silverspoom.bulldog.minnowboard;

import io.silverspoon.bulldog.core.pin.Pin;

public class MinnowPin extends Pin {
    /**
     * Instantiates a new pin.
     *
     * @param name        the name of the pin
     * @param address     the address of the pin
     * @param port        the port of the pin
     * @param indexOnPort
     */
    public MinnowPin(String name, int address, String port, int indexOnPort) {
        super(name, address, port, indexOnPort);
    }
}
