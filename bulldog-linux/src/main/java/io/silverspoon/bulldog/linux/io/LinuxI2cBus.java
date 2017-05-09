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
package io.silverspoon.bulldog.linux.io;

import io.silverspoon.bulldog.core.io.bus.BusConnection;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.linux.jni.NativeI2c;

import java.io.IOException;

public class LinuxI2cBus extends AbstractLinuxBus implements I2cBus {

   private int selectedSlaveAddress;

   public LinuxI2cBus(String name, String deviceFilePath) {
      super(name, deviceFilePath);
   }

   public BusConnection createConnection(int address) {
      return createI2cConnection(address);
   }

   @Override
   public I2cConnection createI2cConnection(int address) {
      return new I2cConnection(this, address);
   }

   @Override
   public int getFrequency() {
      throw new UnsupportedOperationException("Unsupported by generic bus object: " + this.getClass());
   }

   public Pin getSCL() {
      throw new UnsupportedOperationException("Unsupported by generic bus object: " + this.getClass());
   }

   public Pin getSDA() {
      throw new UnsupportedOperationException("Unsupported by generic bus object: " + this.getClass());
   }

   public byte readByte() throws IOException {
      try {
         return NativeI2c.i2cRead(getFileDescriptor());
      } catch (Exception ex) {
         throw new IOException(ERROR_READING_BYTE);
      }
   }

   protected int openImpl() {
      return NativeI2c.i2cOpen(getDeviceFilePath());
   }

   protected int closeImpl() {
      return NativeI2c.i2cClose(getFileDescriptor());
   }

   @Override
   public int readBytes(byte[] buffer) throws IOException {
      return getInputStream().read(buffer);
   }

   @Override
   public String readString() throws IOException {
      return BulldogUtil.convertStreamToString(getInputStream());
   }

   public byte readByteFromRegister(int register) throws IOException {
      writeByte(register);
      return readByte();
   }

   public int readBytesFromRegister(int register, byte[] buffer) throws IOException {
      writeByte(register);
      return readBytes(buffer);
   }

   public void selectSlave(int address) throws IOException {
      if (!isOpen()) {
         open();
      }

      int returnCode = NativeI2c.i2cSelectSlave(getFileDescriptor(), address);
      if (returnCode < 0) {
         throw new IOException(String.format(ERROR_SELECTING_SLAVE, Integer.toHexString(address)));
      }

      this.selectedSlaveAddress = address;
   }

   public void writeByte(int b) throws IOException {
      int returnValue = NativeI2c.i2cWrite(getFileDescriptor(), (byte) b);
      if (returnValue < 0) {
         throw new IOException(ERROR_WRITING_BYTE);
      }
   }

   @Override
   public void writeBytes(byte[] bytes) throws IOException {
      getOutputStream().write(bytes);
      getOutputStream().flush();
   }

   @Override
   public void writeString(String string) throws IOException {
      writeBytes(string.getBytes());
   }

   @Override
   public void writeByteToRegister(int register, int data) throws IOException {
      writeBytes(new byte[] { (byte) register, (byte) data });
   }

   @Override
   public void writeBytesToRegister(int register, byte[] data) throws IOException {
      byte[] bytesToWrite = new byte[data.length + 1];
      bytesToWrite[0] = (byte) register;
      System.arraycopy(data, 0, bytesToWrite, 1, data.length);
      writeBytes(bytesToWrite);
   }

   @Override
   public boolean isSlaveSelected(int address) {
      return selectedSlaveAddress == address;
   }

}
