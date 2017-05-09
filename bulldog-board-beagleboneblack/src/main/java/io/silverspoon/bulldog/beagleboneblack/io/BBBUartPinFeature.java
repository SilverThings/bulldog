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
package io.silverspoon.bulldog.beagleboneblack.io;

import io.silverspoon.bulldog.core.io.uart.AbstractUartPinFeature;
import io.silverspoon.bulldog.core.io.uart.UartPort;
import io.silverspoon.bulldog.core.io.uart.UartSignalType;
import io.silverspoon.bulldog.core.pin.Pin;

public class BBBUartPinFeature extends AbstractUartPinFeature {

   private BBBUartPort port;

   public BBBUartPinFeature(BBBUartPort port, Pin pin, UartSignalType signalType) {
      super(pin, signalType);
      this.port = port;
   }

   @Override
   protected void setupImpl() {
      port.setup();
      blockPin();
   }

   @Override
   protected void teardownImpl() {
      port.teardown();
   }

   @Override
   public UartPort getPort() {
      return port;
   }

}
