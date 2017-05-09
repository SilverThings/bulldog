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
package io.silverspoon.bulldog.core.platform;

import io.silverspoon.bulldog.core.pin.Pin;

import java.util.ArrayList;
import java.util.List;

public class AbstractPinProvider implements PinProvider {

   private List<Pin> pins = new ArrayList<Pin>();

   public List<Pin> getPins() {
      return pins;
   }

   public Pin getPin(int address) {
      for (Pin pin : getPins()) {
         if (pin.getAddress() == address) {
            return pin;
         }
      }

      return null;
   }

   public Pin getPin(String port, int index) {
      if (port == null) {
         throw new IllegalArgumentException("Null may not be passed as a name for a port.");
      }
      if (index < 0) {
         throw new IllegalArgumentException("Index cannot be smaller than 0");
      }

      for (Pin pin : getPins()) {
         if (port.equals(pin.getPort()) && index == pin.getIndexOnPort()) {
            return pin;
         }
      }

      return null;
   }

   public Pin getPin(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Null may not be passed as a name for a pin.");
      }

      for (Pin pin : getPins()) {
         if (name.equals(pin.getName())) {
            return pin;
         }
      }

      return null;
   }

   public Pin getPinByAlias(String alias) {
      if (alias == null) {
         throw new IllegalArgumentException("Null may not be passed as an alias name for a pin.");
      }

      for (Pin pin : getPins()) {
         if (alias.equals(pin.getAlias())) {
            return pin;
         }
      }

      return null;
   }

}
