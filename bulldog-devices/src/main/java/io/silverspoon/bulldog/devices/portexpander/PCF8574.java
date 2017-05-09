/*******************************************************************************
 * Copyright (c) 2016 Silverspoon.io (silverspoon@silverware.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.silverspoon.bulldog.devices.portexpander;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.AbstractPinProvider;
import io.silverspoon.bulldog.core.util.BitMagic;

import java.io.IOException;

/**
 * This class represents the popular PCF8574(A) I2C Port Expander family.
 * It supports all the features of the PCF8574, including interrupts
 * if you connect the INT signal to a DigitalInput of your board.
 *
 * You can use all the pins as if they are normal pins or you can read
 * or write the state of all pins directly with the readState/writeState
 * methods.
 *
 * @author Datenheld
 */
public class PCF8574 extends AbstractPinProvider implements InterruptListener {

   public static final String P0 = "P0";
   public static final String P1 = "P1";
   public static final String P2 = "P2";
   public static final String P3 = "P3";
   public static final String P4 = "P4";
   public static final String P5 = "P5";
   public static final String P6 = "P6";
   public static final String P7 = "P7";

   private I2cConnection connection;
   private DigitalInput interrupt;

   private int state = 0xFF;

   public PCF8574(I2cConnection connection) throws IOException {
      this(connection, null);
   }

   public PCF8574(I2cBus bus, int address) throws IOException {
      this(bus.createI2cConnection(address), null);
   }

   public PCF8574(I2cBus bus, int address, DigitalInput interrupt) throws IOException {
      this(bus.createI2cConnection(address), interrupt);
   }

   public PCF8574(I2cConnection connection, DigitalInput interrupt) throws IOException {
      createPins();
      this.connection = connection;
      setInterrupt(interrupt);
   }

   private void createPins() {
      for (int i = 0; i <= 7; i++) {
         Pin pin = new Pin("P" + i, i, "P", i);
         pin.addFeature(new DigitalIOFeature(pin, new PCF8574DigitalInput(pin, this), new PCF8574DigitalOutput(pin, this)));
         getPins().add(pin);
      }
   }

   @Override
   public void interruptRequest(InterruptEventArgs args) {
      byte lastKnownState = (byte) state;
      byte readState = readState();
      for (int i = 0; i <= 7; i++) {
         Pin currentPin = getPin(i);

         if (!currentPin.isFeatureActive(PCF8574DigitalInput.class)) {
            continue;
         }

         PCF8574DigitalInput input = currentPin.as(PCF8574DigitalInput.class);
         int lastKnownPinState = BitMagic.getBit(lastKnownState, currentPin.getAddress());
         int currentState = BitMagic.getBit(readState, currentPin.getAddress());
         if (lastKnownPinState == currentState) {
            continue;
         }
         input.handleInterruptEvent(Signal.fromNumericValue(lastKnownState), Signal.fromNumericValue(currentState));
      }
   }

   public byte getState() {
      return (byte) state;
   }

   public void writeState(int state) {
      this.state = state;
      try {
         connection.writeByte(state);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * This method preservers the outputs' state and adjust
    * the state of the inputs.
    *
    * Before it reads, it sets all the input pins that
    * are to be read to high, according to the PCF8574
    * datasheet.
    *
    * @return the state read from the PCF857
    */
   public byte readState() {
      try {
         byte buffer = getState();
         switchInputsHigh();
         byte readByte = connection.readByte();
         buffer = applyReadInputs(readByte, buffer);
         writeState(buffer);
         return readByte;
      } catch (IOException e) {
         e.printStackTrace();
      }

      return (byte) 0;
   }

   private void switchInputsHigh() {
      byte highInputState = getState();
      for (Pin pin : getPins()) {
         if (pin.isFeatureActive(DigitalInput.class)) {
            highInputState = BitMagic.setBit(highInputState, pin.getAddress(), 1);
         }
      }

      writeState(highInputState);
   }

   private byte applyReadInputs(byte b, byte buffer) {
      for (Pin pin : getPins()) {
         if (pin.isFeatureActive(DigitalInput.class)) {
            buffer = BitMagic.setBit(buffer, pin.getAddress(), BitMagic.getBit(b, pin.getAddress()));
         }
      }

      return buffer;
   }

   public void setInterrupt(DigitalInput input) {
      if (interrupt != null) {
         interrupt.removeInterruptListener(this);
      }

      interrupt = input;
      if (interrupt != null) {
         interrupt.addInterruptListener(this);
      }
   }
}
