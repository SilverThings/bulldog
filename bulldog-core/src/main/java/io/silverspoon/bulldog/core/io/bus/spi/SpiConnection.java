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
package io.silverspoon.bulldog.core.io.bus.spi;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.io.bus.BusConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpiConnection extends BusConnection {

   private List<Integer> chipSelectList = new ArrayList<Integer>();

   public SpiConnection(SpiBus bus, int address) {
      super(bus, address);
      chipSelectList.add(address);
   }

   public SpiConnection(SpiBus bus, DigitalOutput... outputs) {
      super(bus, 0);
      for (DigitalOutput output : outputs) {
         chipSelectList.add(output.getPin().getAddress());
      }
   }

   public SpiConnection(SpiBus bus, int... addresses) {
      super(bus, 0);
      for (Integer i : addresses) {
         chipSelectList.add(i);
      }
   }

   public SpiConnection(SpiBus bus) {
      super(bus, 0);
   }

   @Override
   protected void acquireBus() throws IOException {
      if (!getBus().isOpen()) {
         getBus().open();
      }

      if (chipSelectList.size() > 0) {
         getBus().selectSlaves(chipSelectList.toArray(new Integer[chipSelectList.size()]));
      }
   }

   /**
    * Full duplex transfer
    *
    * @param bytes
    * @return a SpiMessage containing the data that has been sent
    * and the data that has been received.
    * @throws IOException
    */
   public SpiMessage transfer(byte[] bytes) throws IOException {
      acquireBus();
      SpiMessage message = getBus().transfer(bytes);
      return message;
   }

   public SpiBus getBus() {
      return (SpiBus) super.getBus();
   }

}
