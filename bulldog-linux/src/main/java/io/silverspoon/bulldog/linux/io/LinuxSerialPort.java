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

import io.silverspoon.bulldog.core.Parity;
import io.silverspoon.bulldog.core.io.serial.SerialDataEventArgs;
import io.silverspoon.bulldog.core.io.serial.SerialDataListener;
import io.silverspoon.bulldog.core.io.serial.SerialPort;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.linux.jni.NativePollResult;
import io.silverspoon.bulldog.linux.jni.NativeSerial;
import io.silverspoon.bulldog.linux.jni.NativeTools;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinuxSerialPort implements SerialPort, LinuxEpollListener {

   private static final String ERROR_CLOSING_PORT = "Port could not be closed. Invalid file descriptor?";
   private static final String ERROR_PORT_NOT_OPEN = "Serial port is not open!";
   private static final String ERROR_PORT_ALREADY_OPEN = "Serial port has already been opened! Please close it first and reopen it!";

   private static final int DEFAULT_BAUD_RATE = 9600;
   private static final int DEFAULT_READ_TIMEOUT = 5;
   private static final int DEFAULT_DATA_BITS = 8;
   private static final int DEFAULT_STOP_BITS = 1;

   private String deviceFilePath;
   private int baudRate = DEFAULT_BAUD_RATE;
   private boolean isOpen = false;
   private int fileDescriptor = 0;
   private String alias = "";
   private Parity parity = Parity.None;
   private int dataBits = DEFAULT_DATA_BITS;
   private int stopBits = DEFAULT_STOP_BITS;
   private FileDescriptor streamDescriptor;
   private OutputStream outputStream;
   private InputStream inputStream;
   private boolean blocking = true;
   private LinuxEpollThread listenerThread;

   private List<SerialDataListener> listeners = Collections.synchronizedList(new ArrayList<SerialDataListener>());

   public LinuxSerialPort(String filename) {
      this.deviceFilePath = filename;
      listenerThread = new LinuxEpollThread(filename);
      listenerThread.addListener(this);
   }

   @Override
   public void addListener(SerialDataListener listener) {
      this.listeners.add(listener);
      if (!listenerThread.isRunning()) {
         listenerThread.start();
      }
   }

   public void close() throws IOException {
      if (!isOpen()) {
         return;
      }

      listenerThread.stop();

      try {
         int returnValue = NativeSerial.serialClose(fileDescriptor);
         if (returnValue < 0) {
            throw new IOException(ERROR_CLOSING_PORT);
         }
      } finally {
         finalizeStreams();
      }

      isOpen = false;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      LinuxSerialPort other = (LinuxSerialPort) obj;
      if (deviceFilePath == null) {
         if (other.deviceFilePath != null) {
            return false;
         }
      } else if (!deviceFilePath.equals(other.deviceFilePath)) {
         return false;
      }
      return true;
   }

   private void finalizeStreams() throws IOException {
      if (inputStream != null) {
         try {
            inputStream.close();
         } catch (Exception ex) {
         } finally {
            inputStream = null;
         }
      }

      if (outputStream != null) {
         try {
            outputStream.close();
            outputStream = null;
         } catch (Exception ex) {
         } finally {
            outputStream = null;
         }
      }
   }

   public void fireSerialDataEvent(byte[] data) {
      synchronized (listeners) {
         for (SerialDataListener listener : listeners) {
            listener.onSerialDataAvailable(new SerialDataEventArgs(this, data));
         }
      }
   }

   public String getAlias() {
      return alias;
   }

   @Override
   public int getBaudRate() {
      return this.baudRate;
   }

   @Override
   public boolean getBlocking() {
      return blocking;
   }

   public int getDataBits() {
      return dataBits;
   }

   public String getDeviceFilePath() {
      return deviceFilePath;
   }

   @Override
   public InputStream getInputStream() throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_PORT_NOT_OPEN);
      }

      return inputStream;
   }

   @Override
   public String getName() {
      return deviceFilePath;
   }

   @Override
   public OutputStream getOutputStream() throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_PORT_NOT_OPEN);
      }

      return outputStream;
   }

   @Override
   public Parity getParity() {
      return this.parity;
   }

   private int getParityCode() {
      if (parity == Parity.Even) {
         return NativeSerial.PARENB;
      } else if (parity == Parity.Odd) {
         return NativeSerial.PARENB | NativeSerial.PARODD;
      } else if (parity == Parity.Mark) {
         return NativeSerial.PARENB | NativeSerial.PARODD | NativeSerial.CMSPAR;
      } else if (parity == Parity.Space) {
         return NativeSerial.PARENB | NativeSerial.CMSPAR;
      }

      return 0;
   }

   @Override
   public int getStopBits() {
      return stopBits;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
            + ((deviceFilePath == null) ? 0 : deviceFilePath.hashCode());
      return result;
   }

   public boolean isOpen() {
      return isOpen;
   }

   public void open() throws IOException {
      fileDescriptor = NativeSerial.serialOpen(deviceFilePath, baudRate, getParityCode(), getBlocking(), DEFAULT_READ_TIMEOUT, dataBits, stopBits);
      streamDescriptor = NativeTools.getJavaDescriptor(fileDescriptor);
      outputStream = new FileOutputStream(streamDescriptor);
      inputStream = new FileInputStream(streamDescriptor);
      isOpen = true;
      listenerThread.setup();
      if (listeners.size() > 0) {
         listenerThread.start();
      }
   }

   public byte readByte() throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_PORT_NOT_OPEN);
      }

      return NativeSerial.serialRead(fileDescriptor);
   }

   @Override
   public int readBytes(byte[] buffer) throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_PORT_NOT_OPEN);
      }

      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(buffer.length);
      int bytesRead = NativeSerial.serialReadBuffer(fileDescriptor, byteBuffer, buffer.length);
      byteBuffer.get(buffer);
      return bytesRead;
   }

   @Override
   public String readString() throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_PORT_NOT_OPEN);
      }

      return BulldogUtil.convertStreamToString(getInputStream());
   }

   @Override
   public void removeListener(SerialDataListener listener) {
      this.listeners.remove(listener);
      if (listenerThread.isRunning()) {
         listenerThread.stop();
      }
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   @Override
   public void setBaudRate(int baudRate) {
      if (isOpen()) {
         throw new IllegalStateException(ERROR_PORT_ALREADY_OPEN);
      }

      this.baudRate = baudRate;
   }

   @Override
   public void setBlocking(boolean blocking) {
      if (isOpen()) {
         throw new IllegalStateException(ERROR_PORT_ALREADY_OPEN);
      }

      this.blocking = blocking;
   }

   public void setDataBits(int dataBits) {
      if (dataBits < 5 || dataBits > 8) {
         throw new IllegalArgumentException("The amount of databits must be between 5 and 8");
      }
      this.dataBits = dataBits;
   }

   @Override
   public void setParity(Parity parity) {
      if (isOpen()) {
         throw new IllegalStateException(ERROR_PORT_ALREADY_OPEN);
      }

      this.parity = parity;
   }

   @Override
   public void setStopBits(int stopBits) {
      if (stopBits != 1 && stopBits != 2) {
         throw new IllegalArgumentException("You can only have 1 or 2 stop bits");
      }
      this.stopBits = stopBits;
   }

   public void writeByte(int data) throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_PORT_NOT_OPEN);
      }

      NativeSerial.serialWrite(fileDescriptor, (byte) data);
   }

   @Override
   public void writeBytes(byte[] bytes) throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_PORT_NOT_OPEN);
      }

      outputStream.write(bytes);
   }

   @Override
   public void writeString(String string) throws IOException {
      writeBytes(string.getBytes());
   }

   @Override
   public void processEpollResults(NativePollResult[] results) {
      for (NativePollResult result : results) {
         fireSerialDataEvent(result);
      }
   }

   protected void fireSerialDataEvent(NativePollResult result) {
      synchronized (listeners) {
         for (SerialDataListener listener : listeners) {
            listener.onSerialDataAvailable(new SerialDataEventArgs(this, result.getData()));
         }
      }
   }
}
