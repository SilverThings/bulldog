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
import io.silverspoon.bulldog.core.gpio.base.AbstractDigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.util.BitMagic;

public class PCF8574DigitalOutput extends AbstractDigitalOutput {

   private PCF8574 expander;

   public PCF8574DigitalOutput(Pin pin, PCF8574 expander) {
      super(pin);
      this.expander = expander;
   }

   @Override
   protected void setupImpl() {
   }

   @Override
   protected void teardownImpl() {
   }

   @Override
   protected void applySignalImpl(Signal signal) {
      byte state = expander.getState();
      byte newState = BitMagic.setBit(state, getPin().getAddress(), signal.getNumericValue());
      expander.writeState(newState);
   }

   @Override
   public Signal getAppliedSignal() {
      return Signal.fromNumericValue(BitMagic.getBit(expander.getState(), getPin().getAddress()));
   }

}
