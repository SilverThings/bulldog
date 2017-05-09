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

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.gpio.base.AbstractDigitalInput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.util.BitMagic;

public class PCF8574DigitalInput extends AbstractDigitalInput {

   private PCF8574 expander;

   public PCF8574DigitalInput(Pin pin, PCF8574 expander) {
      super(pin);
      this.expander = expander;
   }

   @Override
   public Signal read() {
      byte state = expander.readState();
      return Signal.fromNumericValue((state >> getPin().getAddress()) & 1);
   }

   @Override
   protected void setupImpl() {
      byte state = expander.getState();
      byte newState = BitMagic.setBit(state, getPin().getAddress(), 1);
      expander.writeState(newState);
   }

   @Override
   protected void teardownImpl() {
   }

   public void handleInterruptEvent(Signal oldState, Signal currentState) {
      if (!areInterruptsEnabled()) {
         return;
      }

      Edge edge = determineInterruptEdge(oldState, currentState);
      if (!isInterruptTrigger(edge)) {
         return;
      }

      fireInterruptEvent(new InterruptEventArgs(getPin(), edge));
   }

   private boolean isInterruptTrigger(Edge edge) {
      return edge == getInterruptTrigger() || getInterruptTrigger() == Edge.Both;
   }

   private Edge determineInterruptEdge(Signal oldState, Signal currentState) {
      if (currentState == Signal.Low && oldState == Signal.High) {
         return Edge.Falling;
      }
      return Edge.Rising;
   }

   @Override
   protected void enableInterruptsImpl() {
   }

   @Override
   protected void disableInterruptsImpl() {
   }
}
