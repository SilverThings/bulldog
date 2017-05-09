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
package io.silverspoon.bulldog.devices.lcd;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.core.util.BulldogUtil;

import java.io.IOException;

/**
 * This class represents a HD44780 compatible lcd which is driven
 * in 4 BIT mode behind an I2C Port Expander.
 *
 * @author Datenheld
 */
public class I2CLcd {

   public static final byte ENABLE = 0b01000000;
   public static final byte DISABLE = 0b00111111;
   public static final byte COMMAND = 0b00000000;
   public static final byte DATA = 0b00100000;
   public static final byte INIT_4BIT_MODE = 0b00000010;

   private I2cConnection connection;

   public I2CLcd(I2cBus bus, int address) throws IOException {
      this(bus.createI2cConnection(address));
   }

   public I2CLcd(I2cConnection connection) throws IOException {
      this.connection = connection;
      initialize();
   }

   public void initialize() throws IOException {
      writeByte(INIT_4BIT_MODE);
      writeCommand(0x28);
      writeCommand(0x0C);
      writeCommand(0x06);
      writeCommand(0x01);
      writeCommand(0x0f);
   }

   public void write(String string) throws IOException {
      for (int i = 0; i < string.length(); i++) {
         writeData(string.charAt(i));
      }
   }

   public void clear() {
   }

   public void writeByte(byte data) throws IOException {
      connection.writeByte(data | ENABLE);
      connection.writeByte(data & DISABLE);
   }

   private void writeByteAsNibbles(int data, int mask) throws IOException {
      byte nibble = (byte) (mask | ((data & 0xF0) >> 4));
      writeByte(nibble);
      nibble = (byte) (mask | (data & 0x0F));
      writeByte(nibble);
   }

   private void writeCommand(int data) throws IOException {
      writeByteAsNibbles(data, COMMAND);
      BulldogUtil.sleepMs(5);
   }

   private void writeData(int data) throws IOException {
      writeByteAsNibbles(data, DATA);
   }
}
