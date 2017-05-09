/*
 * (C) Copyright 2014 libbulldog (http://libbulldog.org/) and others.
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
 *
 */
package io.silverspoon.bulldog.core.gpio;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import io.silverspoon.bulldog.core.pin.PinFeature;

import java.util.List;

/**
 * This interface specified the operations that can be used on a Pin that is
 * configured as a digital input. This includes reading the current state on
 * the pin as well as handling interrupt events.
 */
public interface DigitalInput extends PinFeature {

   /**
    * Reads the current signal on the pin.
    *
    * @return the signal - either low or high
    */
   Signal read();

   /**
    * Reads the signal on the pin debounced. This is useful if one needs
    * to read a bouncing state transition. It does so by continuously
    * reading the input and after it had a number of stable reads it
    * returns the read value.
    *
    * @param debounceMilliseconds
    *       this is the time that is used for debouncing
    *       in the worst case. When the input is still bouncing after the
    *       specified period, then the return value will not be reliable.
    * @return the signal that was read from the pin - either low or high
    */
   Signal readDebounced(int debounceMilliseconds);

   /**
    * This method disables interrupt events on this input.
    * When interrupt events are disabled, then edges on the pins won't trigger
    * the {@link InterruptListener InterruptListener's}
    * {@link InterruptListener#interruptRequest(InterruptEventArgs) interruptRequest}
    * method.
    */
   void disableInterrupts();

   /**
    * This method enables interrupt events on this input.
    */
   void enableInterrupts();

   /**
    * This method checks whether interrupts are currently enabled.
    * If they are enabled, interrupt events will be handled.
    * {@link DigitalInput#disableInterrupts() disableInterrupts()} to disable
    * interrupts and {@link DigitalInput#enableInterrupts()} to enable them.
    *
    * @return true, if interrupt requests are currently handled, false otherwise.
    */
   boolean areInterruptsEnabled();

   /**
    * If the edges of interrupts should be debounced before calling
    * the interrupt event handlers, then the maximum time that
    * should be used for debouncing can be specified here.
    * When the input is still bouncing after the specified period, then
    * the return value will not be reliable. However, please note that
    * this is the maximum time taken. If the input can stably be read
    * earlier, then it won't take as long.
    *
    * The interrupt will then trigger only once instead of multiple
    * times on a bouncing input.
    *
    * @param milliSeconds
    *       The maximum time that is used for debouncing
    */
   void setInterruptDebounceMs(int milliSeconds);

   /**
    * Returns the maximum time that is used for debouncing
    * interrupts.
    *
    * @return The maximum time that is used for debouncing.
    */
   int getInterruptDebounceMs();

   /**
    * Sets the interrupt trigger. This is the {@link Edge} that is
    * used for interrupt detection. Interrupts can either trigger on a
    * {@link Edge#Rising rising edge}, a {@link Edge#Falling falling edge},
    * or on {@link Edge#Both both edges}.
    *
    * The default value is {@link Edge#Both both edges}.
    *
    * @param edge
    *       the edge that is used for triggering interrupts on this input.
    */
   void setInterruptTrigger(Edge edge);

   /**
    * Gets the interrupt trigger that is currently set.
    *
    * @return the interrupt trigger.
    */
   Edge getInterruptTrigger();

   /**
    * Fire interrupt event.
    *
    * @param args
    *       the args
    */
   void fireInterruptEvent(InterruptEventArgs args);

   /**
    * Adds an interrupt listener to the digital input.
    *
    * @param listener
    *       the listener that is to be added.
    */
   void addInterruptListener(InterruptListener listener);

   /**
    * Removes an interrupt listener from the digital input.
    *
    * @param listener
    *       the listener that is to be removed.
    */
   void removeInterruptListener(InterruptListener listener);

   /**
    * Removes all interrupt listeners that are currently registered.
    */
   void clearInterruptListeners();

   /**
    * Gets a list of all the registered interrupt listeners.
    *
    * @return a list of all interrupt listeners.
    */
   List<InterruptListener> getInterruptListeners();
}
