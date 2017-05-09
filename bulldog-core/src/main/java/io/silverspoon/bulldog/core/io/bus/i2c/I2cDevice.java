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
package io.silverspoon.bulldog.core.io.bus.i2c;

import io.silverspoon.bulldog.core.io.bus.BusDevice;

import java.io.IOException;

public class I2cDevice extends BusDevice {

   public I2cDevice(I2cConnection connection) {
      super(connection);
   }

   public I2cDevice(I2cBus bus, int address) {
      this(bus.createI2cConnection(address));
   }

   public byte readByteFromRegister(int register) throws IOException {
      return getBusConnection().readByteFromRegister(register);
   }

   public int readBytesFromRegister(int register, byte[] buffer) throws IOException {
      return getBusConnection().readBytesFromRegister(register, buffer);
   }

   public void writeByteToRegister(int register, int data) throws IOException {
      getBusConnection().writeByteToRegister(register, data);
   }

   public void writeBytesToRegister(int register, byte[] data) throws IOException {
      getBusConnection().writeBytesToRegister(register, data);
   }

   public I2cConnection getBusConnection() {
      return (I2cConnection) super.getBusConnection();
   }
}
