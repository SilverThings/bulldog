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
package io.silverspoon.bulldog.devices.dac;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiDevice;
import io.silverspoon.bulldog.core.util.BitMagic;

import java.io.IOException;

public class MCP4902 extends SpiDevice {

   public MCP4902(SpiBus bus, DigitalOutput chipSelect) {
      this(bus.createSpiConnection(chipSelect));
   }

   public MCP4902(SpiBus bus, int addressOfChipselect) {
      this(bus.createSpiConnection(addressOfChipselect));
   }

   public MCP4902(SpiConnection connection) {
      super(connection);
   }

   public void setVoltageOnDacA(double voltage, boolean outputGain) throws IOException {
      short command = 0x00;
      command = BitMagic.setBit(command, 15, 0);
      setVoltageOnDac(voltage, outputGain, false, command);
   }

   public void setVoltageOnDacB(double voltage, boolean outputGain) throws IOException {
      short command = 0x00;
      command = BitMagic.setBit(command, 15, 1);
      setVoltageOnDac(voltage, outputGain, false, command);
   }

   private void setVoltageOnDac(double voltage, boolean outputGain, boolean shutdown, short command)
         throws IOException {
      command = BitMagic.setBit(command, 14, 0);
      if (outputGain) {
         command = BitMagic.setBit(command, 13, 0);
      }

      if (shutdown) {
         command = BitMagic.setBit(command, 12, 0);
      } else {
         command = BitMagic.setBit(command, 12, 1);
      }
      command |= ((0xFFFFFFFF & ((int) (255 * voltage))) << 4);
      byte[] bytes = new byte[2];
      bytes[0] = (byte) ((0xFF00 & command) >> 8);
      bytes[1] = (byte) ((0X00FF & command));
      getBusConnection().writeBytes(bytes);
   }

   public void shutdownDacA() throws IOException {
      short command = 0x00;
      command = BitMagic.setBit(command, 15, 0);
      setVoltageOnDac(0.0, false, true, command);
   }

   public void shutdownDacB() throws IOException {
      short command = 0x00;
      command = BitMagic.setBit(command, 15, 1);
      setVoltageOnDac(0.0, false, true, command);
   }
}
