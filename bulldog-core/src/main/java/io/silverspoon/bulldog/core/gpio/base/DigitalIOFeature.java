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

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import io.silverspoon.bulldog.core.gpio.DigitalIO;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.pin.AbstractPinFeature;
import io.silverspoon.bulldog.core.pin.Pin;

import java.util.List;

public class DigitalIOFeature extends AbstractPinFeature implements DigitalIO {

   private static final String NAME_FORMAT = "Digital IO on Pin %s";
   private DigitalOutput output;
   private DigitalInput input;

   public DigitalIOFeature(Pin pin, DigitalInput input, DigitalOutput output) {
      super(pin);
      this.output = output;
      this.input = input;
   }

   @Override
   public Signal read() {
      setupInputIfNecessary();
      return input.read();
   }

   private void setupInputIfNecessary() {
      if (!input.isSetup()) {
         if (output.isSetup()) {
            output.teardown();
         }
         input.setup();
      }
   }

   private void setupOutputIfNecessary() {
      if (!output.isSetup()) {
         if (input.isSetup()) {
            input.teardown();
         }
         ;
         output.setup();
      }
   }

   @Override
   public Signal readDebounced(int debounceMilliseconds) {
      setupInputIfNecessary();
      return input.readDebounced(debounceMilliseconds);
   }

   @Override
   public void disableInterrupts() {
      setupInputIfNecessary();
      input.disableInterrupts();
   }

   @Override
   public void enableInterrupts() {
      setupInputIfNecessary();
      input.enableInterrupts();
   }

   @Override
   public boolean areInterruptsEnabled() {
      setupInputIfNecessary();
      return input.areInterruptsEnabled();
   }

   @Override
   public void setInterruptDebounceMs(int milliSeconds) {
      setupInputIfNecessary();
      input.setInterruptDebounceMs(milliSeconds);
   }

   @Override
   public int getInterruptDebounceMs() {
      setupInputIfNecessary();
      return input.getInterruptDebounceMs();
   }

   @Override
   public void setInterruptTrigger(Edge edge) {
      setupInputIfNecessary();
      input.setInterruptTrigger(edge);
   }

   @Override
   public Edge getInterruptTrigger() {
      setupInputIfNecessary();
      return input.getInterruptTrigger();
   }

   @Override
   public void fireInterruptEvent(InterruptEventArgs args) {
      setupInputIfNecessary();
      input.fireInterruptEvent(args);
   }

   @Override
   public void addInterruptListener(InterruptListener listener) {
      setupInputIfNecessary();
      input.addInterruptListener(listener);
   }

   @Override
   public void removeInterruptListener(InterruptListener listener) {
      setupInputIfNecessary();
      input.removeInterruptListener(listener);
   }

   @Override
   public void clearInterruptListeners() {
      setupInputIfNecessary();
      input.clearInterruptListeners();
   }

   @Override
   public List<InterruptListener> getInterruptListeners() {
      setupInputIfNecessary();
      return input.getInterruptListeners();
   }

   @Override
   public String getName() {
      return String.format(NAME_FORMAT, getPin().getName());
   }

   @Override
   public void write(Signal signal) {
      setupOutputIfNecessary();
      output.write(signal);
   }

   @Override
   public void applySignal(Signal signal) {
      setupOutputIfNecessary();
      output.applySignal(signal);
   }

   @Override
   public Signal getAppliedSignal() {
      setupOutputIfNecessary();
      return output.getAppliedSignal();
   }

   @Override
   public void high() {
      setupOutputIfNecessary();
      output.high();
   }

   @Override
   public void low() {
      setupOutputIfNecessary();
      output.low();
   }

   @Override
   public void toggle() {
      setupOutputIfNecessary();
      output.toggle();
   }

   @Override
   public boolean isHigh() {
      setupOutputIfNecessary();
      return output.isHigh();
   }

   @Override
   public boolean isLow() {
      setupOutputIfNecessary();
      return output.isLow();
   }

   @Override
   public void startBlinking(int periodLengthMilliseconds) {
      setupOutputIfNecessary();
      output.startBlinking(periodLengthMilliseconds);
   }

   @Override
   public void startBlinking(int periodLengthMilliseconds,
         int durationMilliseconds) {
      setupOutputIfNecessary();
      output.startBlinking(periodLengthMilliseconds, durationMilliseconds);

   }

   @Override
   public void blinkTimes(int periodLengthMilliseconds, int times) {
      setupOutputIfNecessary();
      output.blinkTimes(periodLengthMilliseconds, times);
   }

   @Override
   public void stopBlinking() {
      setupOutputIfNecessary();
      output.stopBlinking();
   }

   @Override
   public boolean isBlinking() {
      setupOutputIfNecessary();
      return output.isBlinking();
   }

   @Override
   public void awaitBlinkingStopped() {
      setupOutputIfNecessary();
      output.awaitBlinkingStopped();
   }

   @Override
   protected void setupImpl() {
   }

   @Override
   protected void teardownImpl() {
      if (isInputActive()) {
         input.teardown();
      } else if (isOutputActive()) {
         output.teardown();
      }
   }

   public boolean isBlocking() {
      return getPin().getBlocker() == this || getPin().getBlocker() == output || getPin().getBlocker() == input;
   }

   @Override
   public boolean isInputActive() {
      return input.isSetup();
   }

   @Override
   public boolean isOutputActive() {
      return output.isSetup();
   }

}
