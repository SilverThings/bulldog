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

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalIO;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.io.PinIOGroup;
import io.silverspoon.bulldog.core.util.BitMagic;
import io.silverspoon.bulldog.core.util.BulldogUtil;

import java.io.IOException;

public class HD44780Compatible implements Lcd {

   private static final int CMD_CLEAR = 0x01;
   private static final int CMD_HOME = 0b00000010;

   private PinIOGroup dataLine;
   private DigitalOutput rs;
   private DigitalOutput rw;

   private boolean isOn = false;
   private boolean blinkCursor = false;
   private boolean showCursor = false;
   private HD44780Mode mode = HD44780Mode.EightBit;
   private LcdMode lcdMode;

   public HD44780Compatible(DigitalIO rs, DigitalIO rw, DigitalIO enable,
         DigitalIO db4,
         DigitalIO db5,
         DigitalIO db6,
         DigitalIO db7) {
      this(rs, rw, new PinIOGroup(enable, 0, db4, db5, db6, db7), HD44780Mode.FourBit);
   }

   public HD44780Compatible(DigitalIO rs, DigitalIO rw, DigitalIO enable,
         DigitalIO db0,
         DigitalIO db1,
         DigitalIO db2,
         DigitalIO db3,
         DigitalIO db4,
         DigitalIO db5,
         DigitalIO db6,
         DigitalIO db7) {
      this(rs, rw, new PinIOGroup(enable, 0, db0, db1, db2, db3, db4, db5, db6, db7), HD44780Mode.EightBit);
   }

   public HD44780Compatible(DigitalOutput rs, DigitalOutput rw, PinIOGroup group, HD44780Mode mode) {
      this.dataLine = group;
      this.rs = rs;
      this.rw = rw;
      showCursor = true;
      blinkCursor = true;
      this.mode = mode;
   }

   @Override
   public void setMode(LcdMode lcdMode, LcdFont font) {
      if (mode == HD44780Mode.EightBit) {
         writeCommand(0b00110000);
         BulldogUtil.sleepMs(20);
         writeCommand(0b00110000);
         BulldogUtil.sleepMs(1);
         writeCommand(0b00110000);
         BulldogUtil.sleepMs(1);
      } else {
         writeCommand(0b00000011);
         BulldogUtil.sleepMs(20);
         writeCommand(0b00000011);
         BulldogUtil.sleepMs(1);
         writeCommand(0b00000011);
         BulldogUtil.sleepMs(1);
         writeCommand(0b0010);
      }
      clear();
      functionSet(lcdMode.getRows(), font);
      off();
      writeCommand(0x06);
      on();

      this.lcdMode = lcdMode;
   }

   public void home() {
      writeCommand(CMD_HOME);
      BulldogUtil.sleepMs(2);
   }

   private void functionSet(int lines, LcdFont font) {
      byte command = 0b00100000;
      if (mode == HD44780Mode.FourBit) {
         command = BitMagic.setBit(command, 4, 0);
      } else {
         command = BitMagic.setBit(command, 4, 1);
      }

      if (lines > 1) {
         command = BitMagic.setBit(command, 3, 1);
      } else {
         command = BitMagic.setBit(command, 3, 0);
      }

      if (font == LcdFont.Font_8x10) {
         command = BitMagic.setBit(command, 2, 1);
      } else {
         command = BitMagic.setBit(command, 2, 0);
      }

      writeCommand(command);
   }

   private void writeByteAsNibbles(int data) throws IOException {
      dataLine.writeByte(BitMagic.getUpperNibble((byte) data));
      dataLine.writeByte(BitMagic.getLowerNibble((byte) data));
   }

   private void writeToDisplay(int data) {
      try {
         if (this.mode == HD44780Mode.FourBit) {
            writeByteAsNibbles(data);
         } else {
            dataLine.writeByte(data);
         }
      } catch (Exception ex) {
         throw new RuntimeException(ex);
      }
   }

   private void writeCommand(int command) {
      rw.applySignal(Signal.Low);
      rs.applySignal(Signal.Low);
      writeToDisplay(command);
      BulldogUtil.sleepMs(5);
   }

   private byte readFromDisplay() {
      rw.applySignal(Signal.High);
      rs.applySignal(Signal.High);

      byte value = 0;
      try {
         if (mode == HD44780Mode.FourBit) {
            byte highNibble = dataLine.readByte();
            byte lowNibble = dataLine.readByte();
            value = BitMagic.byteFromNibbles(highNibble, lowNibble);
         } else {
            value = dataLine.readByte();
         }
      } catch (Exception ex) {
         throw new RuntimeException(ex);
      }

      return value;
   }

   private void writeData(int data) {
      rw.applySignal(Signal.Low);
      rs.applySignal(Signal.High);
      writeToDisplay(data);
   }

   public void clear() {
      writeCommand(CMD_CLEAR);
   }

   public void on() {
      byte command = 0b1100;
      if (showCursor) {
         command = BitMagic.setBit(command, 1, 1);
      }

      if (blinkCursor) {
         command = BitMagic.setBit(command, 0, 1);
      }

      writeCommand(command);
      isOn = true;
   }

   public void off() {
      byte command = 0b1000;
      writeCommand(command);
      isOn = false;
   }

   @Override
   public void writeAt(int row, int column, String text) {
      setCursorPosition(row, column);
      write(text);
   }

   @Override
   public String readLine(int line) {
      return read(line, 0, lcdMode.getRows() * lcdMode.getColumns());
   }

   @Override
   public String read(int line, int column, int length) {
      setCursorPosition(line, column);
      return read(length);
   }

   @Override
   public String read(int length) {
      byte buffer[] = new byte[length];
      for (int i = 0; i < length; i++) {
         buffer[i] = readFromDisplay();
      }

      return BulldogUtil.bytesToString(buffer);
   }

   @Override
   public void blinkCursor(boolean blink) {
      blinkCursor = blink;
      if (isOn) {
         on();
      }
   }

   @Override
   public void showCursor(boolean show) {
      showCursor = show;
      if (isOn) {
         on();
      }
   }

   @Override
   public void write(String text) {
      byte[] bytes = text.getBytes();
      for (int i = 0; i < bytes.length; i++) {
         writeData(bytes[i]);
      }
   }

   @Override
   public void setCursorPosition(int line, int column) {
      int memoryPosition = lcdMode.getMemoryOffset(line, column);
      writeCommand(0x80 | memoryPosition);
   }

}
