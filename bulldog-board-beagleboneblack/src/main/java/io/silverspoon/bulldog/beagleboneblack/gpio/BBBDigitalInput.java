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
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.linux.gpio.LinuxDigitalInput;

public class BBBDigitalInput extends LinuxDigitalInput {

   public BBBDigitalInput(Pin pin) {
      super(pin);
   }

   public Signal read() {
      BeagleBonePin bbbPin = (BeagleBonePin) getPin();
      return Signal.fromNumericValue(NativeGpio.digitalRead(bbbPin.getPortNumeric(), bbbPin.getIndexOnPort()));
   }

   public void setup() {
      super.setup();
      BeagleBonePin bbbPin = (BeagleBonePin) getPin();
      NativeGpio.pinMode(bbbPin.getPortNumeric(), bbbPin.getIndexOnPort(), NativeGpio.DIRECTION_IN);
   }

}
