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
package io.silverspoon.bulldog.devices.switches;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import io.silverspoon.bulldog.core.gpio.DigitalInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Button implements InterruptListener {

   private static final int DEFAULT_DEBOUNCE_TIME_MS = 0;

   private DigitalInput input;
   private Signal activeState = Signal.Low;
   private List<ButtonListener> listeners = Collections.synchronizedList(new ArrayList<ButtonListener>());

   public Button(DigitalInput input, int debounceMilliseconds, Signal activeState) {
      this.input = input;
      this.input.setInterruptDebounceMs(debounceMilliseconds);
      this.input.addInterruptListener(this);
      this.input.enableInterrupts();
      this.activeState = activeState;
   }

   public Button(DigitalInput input, Signal activeState) {
      this(input, DEFAULT_DEBOUNCE_TIME_MS, activeState);
   }

   protected void fireButtonReleased() {
      synchronized (listeners) {
         for (ButtonListener listener : listeners) {
            listener.buttonReleased();
         }
      }
   }

   protected void fireButtonPressed() {
      synchronized (listeners) {
         for (ButtonListener listener : listeners) {
            listener.buttonPressed();
         }
      }
   }

   public void addListener(ButtonListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(ButtonListener listener) {
      this.listeners.remove(listener);
   }

   public void clearListeners() {
      this.listeners.clear();
   }

   @Override
   public void interruptRequest(InterruptEventArgs args) {
      if (args.getEdge() == Edge.Rising && activeState == Signal.High) {
         fireButtonPressed();
      } else if (args.getEdge() == Edge.Falling && activeState == Signal.Low) {
         fireButtonPressed();
      } else {
         fireButtonReleased();
      }
   }

   public boolean isButtonPressed() {
      return input.read() == activeState;
   }

}
