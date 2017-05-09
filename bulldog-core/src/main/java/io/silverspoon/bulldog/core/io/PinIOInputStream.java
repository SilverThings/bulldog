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
package io.silverspoon.bulldog.core.io;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.util.BitMagic;

import java.io.IOException;
import java.io.InputStream;

public class PinIOInputStream extends InputStream {

   private PinIOGroup group;

   public PinIOInputStream(PinIOGroup group) {
      this.group = group;
   }

   @Override
   public int read() throws IOException {

      group.startEnable();

      int value = 0;
      for (int i = 0; i < group.getDataPins().length; i++) {
         DigitalInput in = group.getDataPins()[i];
         Signal signal = in.read();
         value = BitMagic.setBit(value, i, signal.getNumericValue());
      }

      group.endEnable();

      return value;
   }

}
