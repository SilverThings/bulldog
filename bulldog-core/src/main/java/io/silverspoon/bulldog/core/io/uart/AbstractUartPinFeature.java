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
package io.silverspoon.bulldog.core.io.uart;

import io.silverspoon.bulldog.core.pin.AbstractPinFeature;
import io.silverspoon.bulldog.core.pin.Pin;

public abstract class AbstractUartPinFeature extends AbstractPinFeature implements UartRx, UartTx {

   private static final String NAME = "UART %s on Pin %s";
   private UartSignalType signalType;

   public AbstractUartPinFeature(Pin pin, UartSignalType signalType) {
      super(pin);
      this.signalType = signalType;
   }

   @Override
   public String getName() {
      return String.format(NAME, signalType, getPin().getName());
   }

   public UartSignalType getSignalType() {
      return signalType;
   }
}
