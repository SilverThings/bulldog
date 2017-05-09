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
import io.silverspoon.bulldog.core.gpio.base.AbstractDigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;

public class CubieboardDigitalOutput extends AbstractDigitalOutput {

   private final CubieboardGpioMemory gpioMemory;
   private final int pinIndex;
   private final int portIndex;

   public CubieboardDigitalOutput(Pin pin, CubieboardGpioMemory gpioMemory, int portIndex) {
      super(pin);

      this.gpioMemory = gpioMemory;
      this.pinIndex = pin.getIndexOnPort();
      this.portIndex = portIndex;
   }

   @Override protected void setupImpl() {
      gpioMemory.setPinDirection(portIndex, pinIndex, 1);
      int res = gpioMemory.getPinValue(portIndex, pinIndex);
      if (res != 0) {
         setSignal(Signal.High);
      } else {
         setSignal(Signal.Low);
      }

   }

   @Override protected void teardownImpl() {
   }

   @Override
   protected void applySignalImpl(Signal signal) {
      gpioMemory.setPinValue(portIndex, pinIndex, signal.getNumericValue());
   }
}
