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

public class IncrementalRotaryEncoder {

   private DigitalInput interruptSignalA;
   private DigitalInput interruptSignalB;
   private boolean signalA = false;
   private boolean signalB = false;
   private volatile int position = 0;
   private List<RotaryEncoderListener> listeners = Collections
         .synchronizedList(new ArrayList<RotaryEncoderListener>());

   public IncrementalRotaryEncoder(DigitalInput signalA, DigitalInput signalB) {
      this.interruptSignalA = signalA;
      this.interruptSignalB = signalB;

      initializeClockwiseInterrupt(signalA);
      initializeCounterClockwiseInterrupt(signalB);
   }

   private void initializeCounterClockwiseInterrupt(DigitalInput counterClockwise) {
      this.interruptSignalA.setInterruptTrigger(Edge.Both);
      counterClockwise.addInterruptListener(new InterruptListener() {

         @Override
         public void interruptRequest(InterruptEventArgs args) {
            signalB = args.getEdge() == Edge.Rising;
            if (signalB && !(interruptSignalA.read() == Signal.High)) {
               position--;
               fireValueChanged(position - 1, position);
               fireCounterclockwiseTurn();
            }
         }

      });
   }

   private void initializeClockwiseInterrupt(DigitalInput clockwise) {
      this.interruptSignalB.setInterruptTrigger(Edge.Both);
      clockwise.addInterruptListener(new InterruptListener() {

         @Override
         public void interruptRequest(InterruptEventArgs args) {
            signalA = args.getEdge() == Edge.Rising;
            if (signalA && !(interruptSignalB.read() == Signal.High)) {
               position++;
               fireValueChanged(position - 1, position);
               fireClockwiseTurn();
            }
         }

      });
   }

   public int getValue() {
      return position;
   }

   public void addListener(RotaryEncoderListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(RotaryEncoderListener listener) {
      this.listeners.remove(listener);
   }

   public void clearListeners() {
      this.listeners.clear();
   }

   protected void fireValueChanged(int oldValue, int newValue) {
      synchronized (listeners) {
         for (RotaryEncoderListener listener : listeners) {
            listener.valueChanged(oldValue, newValue);
         }
      }
   }

   protected void fireClockwiseTurn() {
      synchronized (listeners) {
         for (RotaryEncoderListener listener : listeners) {
            listener.turnedClockwise();
         }
      }
   }

   protected void fireCounterclockwiseTurn() {
      synchronized (listeners) {
         for (RotaryEncoderListener listener : listeners) {
            listener.turnedCounterclockwise();
         }
      }
   }

   protected void setPosition(int position) {
      int oldPosition = this.position;
      this.position = position;
      fireValueChanged(oldPosition, position);
   }

}
