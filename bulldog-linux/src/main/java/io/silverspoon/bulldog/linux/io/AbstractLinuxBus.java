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

import io.silverspoon.bulldog.core.io.bus.Bus;
import io.silverspoon.bulldog.linux.jni.NativeTools;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class AbstractLinuxBus implements Bus {

   protected static final String ERROR_OPENING_BUS = "Bus '%s' could not be opened";
   protected static final String ERROR_CLOSING_BUS = "Bus could not be closed. Invalid file descriptor?";
   protected static final String ERROR_SELECTING_SLAVE = "Error selecting slave on address %s";
   protected static final String ERROR_WRITING_BYTE = "Byte could not be written to bus";
   protected static final String ERROR_READING_BYTE = "Byte could not be read from bus";
   protected static final String ERROR_BUS_NOT_OPENED = "Bus has not been opened!";

   private String deviceFilePath;
   private boolean isOpen = false;
   private int fileDescriptor;
   private String alias;
   private String name;
   private FileInputStream inputStream;
   private FileOutputStream outputStream;
   private FileDescriptor streamDescriptor;

   public AbstractLinuxBus(String name, String deviceFilePath) {
      this.deviceFilePath = deviceFilePath;
      this.name = name;
   }

   public String getAlias() {
      return alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public String getName() {
      return name;
   }

   @Override
   public FileInputStream getInputStream() throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_BUS_NOT_OPENED);
      }
      return inputStream;
   }

   @Override
   public FileOutputStream getOutputStream() throws IOException {
      if (!isOpen()) {
         throw new IllegalStateException(ERROR_BUS_NOT_OPENED);
      }
      return outputStream;
   }

   public boolean isOpen() {
      return isOpen;
   }

   public void open() throws IOException {
      if (isOpen()) {
         return;
      }
      fileDescriptor = openImpl();
      if (fileDescriptor <= 0) {
         isOpen = false;
         throw new IOException(String.format(ERROR_OPENING_BUS, getDeviceFilePath()));
      }

      streamDescriptor = NativeTools.getJavaDescriptor(fileDescriptor);
      inputStream = new FileInputStream(streamDescriptor);
      outputStream = new FileOutputStream(streamDescriptor);
      isOpen = true;
   }

   protected abstract int openImpl();

   protected String getDeviceFilePath() {
      return this.deviceFilePath;
   }

   protected int getFileDescriptor() {
      return this.fileDescriptor;
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
         } catch (Exception ex) {
         } finally {
            outputStream = null;
         }
      }
   }

   public void close() throws IOException {
      try {
         if (!isOpen()) {
            return;
         }

         fileDescriptor = 0;
         isOpen = false;
         int returnValue = closeImpl();
         if (returnValue < 0) {
            throw new IOException(ERROR_CLOSING_BUS);
         }
      } finally {
         finalizeStreams();
      }
   }

   protected abstract int closeImpl();

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
      AbstractLinuxBus other = (AbstractLinuxBus) obj;
      if (deviceFilePath == null) {
         if (other.deviceFilePath != null) {
            return false;
         }
      } else if (!deviceFilePath.equals(other.deviceFilePath)) {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime
            * result
            + ((deviceFilePath == null) ? 0 : deviceFilePath
            .hashCode());
      return result;
   }
}
