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
package io.silverspoon.bulldog.raspberrypi.gpio;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.base.AbstractDigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.raspberrypi.RaspberryPiPin;
import io.silverspoon.bulldog.raspberrypi.bcm.AbstractBCM;
import io.silverspoon.bulldog.raspberrypi.bcm.BCMFactory;

public class RaspiDigitalOutput extends AbstractDigitalOutput {

   public static final AbstractBCM BCM = BCMFactory.getBCM();
   public RaspiDigitalOutput(Pin pin) {
      super(pin);
   }

   @Override
   protected void setupImpl() {
      RaspberryPiPin pin = (RaspberryPiPin) getPin();
      BCM.configureAsInput(pin.getGpioNumber());
      BCM.configureAsOutput(pin.getGpioNumber());
      int address = 1 << pin.getGpioNumber();
      Signal s = Signal.fromNumericValue(BCM.getGpioMemory().getIntValueAt(BCM.getGPIORead()) & address);
      setSignal(s);
   }

   @Override
   protected void teardownImpl() {

   }

   @Override
   protected void applySignalImpl(Signal signal) {
      int value = 1 << getRaspberryPiPin().getGpioNumber();
      if (signal == Signal.High) {
         BCM.getGpioMemory().setIntValue(BCM.getGPIOSet(), value);
      } else {
         BCM.getGpioMemory().setIntValue(BCM.getGPIOClear(), value);
      }
   }

   private RaspberryPiPin getRaspberryPiPin() {
      RaspberryPiPin pin = (RaspberryPiPin) getPin();
      return pin;
   }
}
