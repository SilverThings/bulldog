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
package io.silverspoon.bulldog.beagleboneblack.gpio;

import io.silverspoon.bulldog.beagleboneblack.BeagleBonePin;
import io.silverspoon.bulldog.beagleboneblack.jni.NativeGpio;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.base.AbstractDigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;

public class BBBDigitalOutput extends AbstractDigitalOutput {

   public BBBDigitalOutput(Pin pin) {
      super(pin);
   }

   protected void setupImpl() {
      BeagleBonePin bbbPin = (BeagleBonePin) getPin();
      NativeGpio.pinMode(bbbPin.getPortNumeric(), bbbPin.getIndexOnPort(), NativeGpio.DIRECTION_OUT);
      int res = NativeGpio.digitalRead(bbbPin.getPortNumeric(), bbbPin.getIndexOnPort());
      setSignal((res != 0) ? Signal.High : Signal.Low);
   }

   protected void teardownImpl() {
   }

   @Override
   protected void applySignalImpl(Signal signal) {
      BeagleBonePin bbbPin = (BeagleBonePin) getPin();
      NativeGpio.digitalWrite(bbbPin.getPortNumeric(), bbbPin.getIndexOnPort(), signal == Signal.High ? NativeGpio.HIGH : NativeGpio.LOW);
   }

}
