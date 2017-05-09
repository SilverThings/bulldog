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

import io.silverspoon.bulldog.core.io.bus.BusConnection;

import java.io.IOException;

public class I2cConnection extends BusConnection {

   public I2cConnection(I2cBus bus, int address) {
      super(bus, address);
   }

   public byte readByteFromRegister(int register) throws IOException {
      writeByte((byte) register);
      return readByte();
   }

   public int readBytesFromRegister(int register, byte[] buffer) throws IOException {
      writeByte((byte) register);
      return readBytes(buffer);
   }

   public void writeByteToRegister(int register, int data) throws IOException {
      writeBytes(new byte[] { (byte) register, (byte) data });
   }

   public void writeBytesToRegister(int register, byte[] data) throws IOException {
      byte[] bytesToWrite = new byte[data.length + 1];
      bytesToWrite[0] = (byte) register;
      System.arraycopy(data, 0, bytesToWrite, 1, data.length);
   }

   public I2cBus getBus() {
      return (I2cBus) super.getBus();
   }
}
