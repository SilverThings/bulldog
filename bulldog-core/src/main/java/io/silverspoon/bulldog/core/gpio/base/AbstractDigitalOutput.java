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
package io.silverspoon.bulldog.core.gpio.base;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.gpio.util.Blinker;
import io.silverspoon.bulldog.core.pin.AbstractPinFeature;
import io.silverspoon.bulldog.core.pin.Pin;

public abstract class AbstractDigitalOutput extends AbstractPinFeature implements DigitalOutput {

   private static final String NAME_FORMAT = "Digital Output - Signal '%s' on Pin %s";

   private Signal signal = Signal.Low;
   private Blinker blinker;

   public AbstractDigitalOutput(Pin pin) {
      super(pin);
      blinker = new Blinker(this);
   }

   public String getName() {
      return String.format(NAME_FORMAT, signal, getPin().getName());
   }

   public void write(Signal signal) {
      applySignal(signal);
   }

   public void applySignal(Signal signal) {
      this.signal = signal;
      applySignalImpl(this.signal);
   }

   public void high() {
      applySignal(Signal.High);
   }

   public void low() {
      applySignal(Signal.Low);
   }

   public boolean isHigh() {
      return signal == Signal.High;
   }

   public boolean isLow() {
      return signal == Signal.Low;
   }

   public void toggle() {
      applySignal(signal.inverse());
   }

   public Signal getAppliedSignal() {
      return signal;
   }

   public void startBlinking(int periodMilliseconds) {
      blinker.startBlinking(periodMilliseconds);
   }

   public void startBlinking(int periodMilliseconds, int durationMilliseconds) {
      blinker.startBlinking(periodMilliseconds, durationMilliseconds);
   }

   public void stopBlinking() {
      blinker.stopBlinking();
   }

   public void blinkTimes(int periodMilliseconds, int times) {
      blinker.blinkTimes(periodMilliseconds, times);
   }

   public boolean isBlinking() {
      return blinker.isBlinking();
   }

   public void awaitBlinkingStopped() {
      blinker.awaitBlinkingStopped();
   }

   protected abstract void applySignalImpl(Signal signal);

   protected void setSignal(Signal signal) {
      this.signal = signal;
   }
}
