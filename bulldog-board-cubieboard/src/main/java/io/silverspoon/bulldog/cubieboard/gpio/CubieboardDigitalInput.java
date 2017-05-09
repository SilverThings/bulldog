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
package io.silverspoon.bulldog.cubieboard.gpio;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.event.InterruptListener;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.cubieboard.CubieboardPin;
import io.silverspoon.bulldog.linux.gpio.LinuxDigitalInput;
import io.silverspoon.bulldog.linux.io.LinuxEpollListener;
import io.silverspoon.bulldog.linux.sysfs.SysFsPin;

public class CubieboardDigitalInput extends LinuxDigitalInput implements LinuxEpollListener {

   private final CubieboardGpioMemory gpioMemory;
   private final int pinIndex;
   private final int portIndex;

   public CubieboardDigitalInput(Pin pin, CubieboardGpioMemory gpioMemory, int portIndex) {
      super(pin);

      this.gpioMemory = gpioMemory;
      this.pinIndex = pin.getIndexOnPort();
      this.portIndex = portIndex;
   }

   @Override
   protected SysFsPin createSysFsPin(Pin pin) {
      return new CubieboardSysFsPin(pin.getAddress(), ((CubieboardPin) pin).getFsName(), ((CubieboardPin) pin).isInterrupt());
   }

   @Override
   public void setup() {
      super.setup();
      gpioMemory.setPinDirection(portIndex, pinIndex, 0);
   }

   @Override
   public Signal read() {
      return Signal.fromNumericValue(gpioMemory.getPinValue(portIndex, pinIndex));
   }

   @Override
   public void addInterruptListener(InterruptListener listener) {
      checkInterruptsAllowed();
      super.addInterruptListener(listener);
   }

   @Override
   public void enableInterrupts() {
      checkInterruptsAllowed();
      super.enableInterrupts();
   }

   private void checkInterruptsAllowed() {
      if (!((CubieboardPin) getPin()).isInterrupt()) {
         throw new RuntimeException("Pin " + getName() + " is not connected to interrupt controller");
      }
   }
}

