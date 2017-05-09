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

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.pin.PinFeature;

/**
 * This interface specified the operations that can be used on a Pin that is
 * configured as a digital output. This includes setting the state on the pin
 * and also some convenience features.
 */
public interface DigitalOutput extends PinFeature {

   /**
    * Writes a state to the pin.
    *
    * @param signal
    *       the signal
    */
   void write(Signal signal);

   /**
    * Writes a state to the pin.
    *
    * @param signal
    *       the signal
    */
   void applySignal(Signal signal);

   /**
    * Gets the signal that is currently applied to the pin.
    *
    * @return the applied signal
    */
   Signal getAppliedSignal();

   /**
    * Applies a high level signal to the pin.
    */
   void high();

   /**
    * Applies a low level signal to the pin.
    */
   void low();

   /**
    * Toggles the current signal on the pin.
    */
   void toggle();

   /**
    * Queries if the signal on the pin
    * is currently high.
    *
    * @return true, if the signal is high
    */
   boolean isHigh();

   /**
    * Queries if the signal on the pin
    * is currently low.
    *
    * @return true, if the signal is low
    */
   boolean isLow();

   /**
    * This method is used to let the pin blink <i>indefinitely</i>.
    * It is a non blocking method that will be executed
    * asynchronously. To stop the blinking, see the
    * {@link DigitalOutput#stopBlinking() stopBlinking()} method.
    * You can specify the duration of a blinking period.
    * A blinking period is the full cycle of a state transition
    * and includes the the high phase as well as the low phase.
    * Both phases have the same duration.
    *
    * @param periodLengthMilliseconds
    *       the duration of one full
    *       blinking period in  milliseconds
    */
   void startBlinking(int periodLengthMilliseconds);

   /**
    * This method is used to let the pin blink for a specified
    * <i>duration</i>. It is a non blocking method that will be
    * executed asynchronously.To stop the blinking prematurely,
    * see the {@link DigitalOutput#stopBlinking() stopBlinking()}
    * method. You can specify the duration of a blinking period.
    * A blinking period is the full cycle of a state transition
    * and includes the the high phase as well as the low phase.
    * Both phases have the same duration.
    *
    * Also, you can specify the duration of the whole blinking process,
    * e.g. specify to let the output blink for 3000 milliseconds.
    *
    * @param periodLengthMilliseconds
    *       the duration of one full
    *       blinking period in  milliseconds
    * @param durationMilliseconds
    *       the duration of the whole blinking
    *       process in milliseconds. A value of 0
    *       let's it blink indefinitely.
    */
   void startBlinking(int periodLengthMilliseconds, int durationMilliseconds);

   /**
    * This method is used to let the input blink a number of times.
    *
    * @param periodLengthMilliseconds
    *       the duration of one full
    *       blinking period in  milliseconds
    * @param times
    *       the amount of times the output should blink
    */
   void blinkTimes(int periodLengthMilliseconds, int times);

   /**
    * Stops any blinking process that is currently in progress on
    * this output.
    */
   void stopBlinking();

   /**
    * Checks if this output is currently blinking.
    *
    * @return true, if the output is currently blinking
    */
   boolean isBlinking();

   /**
    * Halts the current thread until the blinking on the output
    * has stopped.
    */
   void awaitBlinkingStopped();
}
