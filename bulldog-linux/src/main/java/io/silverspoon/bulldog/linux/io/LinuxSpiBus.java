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

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.io.bus.BusConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMessage;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMode;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.linux.jni.NativeSpi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinuxSpiBus extends AbstractLinuxBus implements SpiBus {

   private List<DigitalOutput> slaveSelectPins = new ArrayList<DigitalOutput>();
   private Board board;

   private int speed = 10000;
   private int bitsPerWord = 8;
   private int delayMicroSeconds = 0;
   private SpiMode mode = SpiMode.Mode0;
   private boolean lsbFirst = false;

   public LinuxSpiBus(String name, String deviceFilePath, Board board) {
      super(name, deviceFilePath);
      this.board = board;
   }

   public BusConnection createConnection(int address) {
      return createSpiConnection(address);
   }

   @Override
   public SpiConnection createSpiConnection() {
      return new SpiConnection(this);
   }

   @Override
   public SpiConnection createSpiConnection(int address) {
      return new SpiConnection(this, address);
   }

   @Override
   public SpiConnection createSpiConnection(DigitalOutput output) {
      return new SpiConnection(this, output.getPin().getAddress());
   }

   @Override
   public SpiConnection createSpiConnection(DigitalOutput... chipSelects) {
      return new SpiConnection(this, chipSelects);
   }

   @Override
   public SpiConnection createSpiConnection(int... addresses) {
      return new SpiConnection(this, addresses);
   }

   private void configure() {
      if (isOpen()) {
         int value = NativeSpi.spiConfig(getFileDescriptor(), getMode().getNumericValue(), getSpeedInHz(), getBitsPerWord(), lsbFirst);
         if (value < 0) {
            throw new RuntimeException("Configuration of SPI failed");
         }
      }
   }

   public byte readByte() throws IOException {
      try {
         byte[] buffer = new byte[1];
         getInputStream().read(buffer);
         return buffer[0];
      } catch (Exception ex) {
         throw new IOException(ERROR_READING_BYTE);
      }
   }

   protected int openImpl() {
      return NativeSpi.spiOpen(getDeviceFilePath(), getMode().getNumericValue(), getSpeedInHz(), getBitsPerWord(), lsbFirst);
   }

   protected int closeImpl() {
      return NativeSpi.spiClose(getFileDescriptor());
   }

   @Override
   public int readBytes(byte[] buffer) throws IOException {
      return getInputStream().read(buffer);
   }

   @Override
   public String readString() throws IOException {
      return BulldogUtil.convertStreamToString(getInputStream());
   }

   public void selectSlave(int address) throws IOException {
      DigitalOutput output = board.getPin(address).as(DigitalOutput.class);
      selectSlave(output);
   }

   @Override
   public void selectSlave(DigitalOutput output) {
      getSlaveSelectPins().clear();
      getSlaveSelectPins().add(output);
   }

   @Override
   public void selectSlaves(DigitalOutput... chipSelects) {
      getSlaveSelectPins().clear();
      if (chipSelects != null && chipSelects.length > 0) {
         getSlaveSelectPins().addAll(Arrays.asList(chipSelects));
      }
   }

   @Override
   public void selectSlaves(Integer... chipSelectAddresses) {
      List<DigitalOutput> outputs = new ArrayList<DigitalOutput>();
      for (Integer cs : chipSelectAddresses) {
         DigitalOutput output = board.getPin(cs).as(DigitalOutput.class);
         outputs.add(output);
      }
      selectSlaves(outputs.toArray(new DigitalOutput[outputs.size()]));
   }

   private void startOutput() {
      for (DigitalOutput output : getSlaveSelectPins()) {
         output.low();
      }
   }

   private void endOutput() {
      for (DigitalOutput output : getSlaveSelectPins()) {
         output.high();
      }
   }

   public void writeByte(int b) throws IOException {
      try {
         startOutput();
         getOutputStream().write(b);
         getOutputStream().flush();
         endOutput();
      } catch (Exception ex) {
         throw new IOException(ERROR_WRITING_BYTE);
      }
   }

   @Override
   public void writeBytes(byte[] bytes) throws IOException {
      startOutput();
      getOutputStream().write(bytes);
      getOutputStream().flush();
      endOutput();
   }

   @Override
   public void writeString(String string) throws IOException {
      writeBytes(string.getBytes());
   }

   @Override
   public SpiMessage transfer(byte[] buffer) {
      startOutput();

      byte[] sentBytes = buffer.clone();

      ByteBuffer bufferPointer = ByteBuffer.allocateDirect(buffer.length);
      bufferPointer.put(buffer);
      bufferPointer.rewind();

      int length = buffer.length / (getBitsPerWord() / 8);

      NativeSpi.spiTransfer(getFileDescriptor(), bufferPointer, bufferPointer, length, getDelayMicroseconds(), getSpeedInHz(), getBitsPerWord());

      endOutput();

      return createSpiMessage(bufferPointer, sentBytes);
   }

   private SpiMessage createSpiMessage(ByteBuffer buffer, byte[] sentBytes) {
      SpiMessage message = new SpiMessage();
      byte[] rxBytes = new byte[sentBytes.length];
      buffer.get(rxBytes);
      message.setReceivedBytes(rxBytes);
      message.setSentBytes(sentBytes);
      return message;
   }

   @Override
   public List<DigitalOutput> getSlaveSelectPins() {
      return slaveSelectPins;
   }

   @Override
   public boolean isSlaveSelected(int address) {
      for (DigitalOutput output : getSlaveSelectPins()) {
         if (output.getPin().getAddress() == address) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void broadcast(byte[] bytes, DigitalOutput... chipSelects) throws IOException {
      selectSlaves(chipSelects);
      writeBytes(bytes);
   }

   @Override
   public Pin getMISO() {
      throw new UnsupportedOperationException("Unsupported by generic bus object: " + this.getClass());
   }

   @Override
   public Pin getMOSI() {
      throw new UnsupportedOperationException("Unsupported by generic bus object: " + this.getClass());
   }

   @Override
   public Pin getSCLK() {
      throw new UnsupportedOperationException("Unsupported by generic bus object: " + this.getClass());
   }

   @Override
   public int getSpeedInHz() {
      return this.speed;
   }

   @Override
   public void setSpeedInHz(int speedInHz) {
      this.speed = speedInHz;
      configure();
   }

   @Override
   public void setBitsPerWord(int bpw) {
      this.bitsPerWord = bpw;
      configure();
   }

   @Override
   public int getBitsPerWord() {
      return this.bitsPerWord;
   }

   @Override
   public void setDelayMicroseconds(int delay) {
      this.delayMicroSeconds = delay;
   }

   @Override
   public int getDelayMicroseconds() {
      return this.delayMicroSeconds;
   }

   @Override
   public void setMode(SpiMode mode) {
      this.mode = mode;
      configure();
   }

   @Override
   public SpiMode getMode() {
      return mode;
   }

   @Override
   public void useLeastSignificantBitFirst() {
      lsbFirst = true;
      configure();
   }

   @Override
   public void useMostSignificantBitFirst() {
      lsbFirst = false;
      configure();
   }

   @Override
   public boolean isLSBUsed() {
      return lsbFirst;
   }

   @Override
   public boolean isMSBUsed() {
      return !lsbFirst;
   }

}
