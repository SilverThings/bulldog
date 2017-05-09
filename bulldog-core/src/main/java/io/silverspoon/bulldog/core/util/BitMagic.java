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
package io.silverspoon.bulldog.core.util;

public final class BitMagic {

   private static final String ERROR_INVALID_VALUE = "A bit can only be set to either 0 or 1";
   private static final String ERROR_INVALID_BIT_POSITION = "There are only %d bits (bit 0 to %d) in this type. Invalid bit position";

   public static int getBit(byte b, int bitPosition) {
      checkPosition(bitPosition, 8);
      return (b >> bitPosition) & 1;
   }

   public static int getBit(short word, int bitPosition) {
      checkPosition(bitPosition, 16);
      return (word >> bitPosition) & 1;
   }

   public static int getBit(int dword, int bitPosition) {
      checkPosition(bitPosition, 32);
      return (dword >> bitPosition) & 1;
   }

   public static int getBit(long qword, int bitPosition) {
      checkPosition(bitPosition, 64);
      return (int) (qword >> bitPosition) & 1;
   }

   public static byte setBit(byte b, int bitPosition, int value) {
      checkValue(value);
      checkPosition(bitPosition, 8);

      if (value == 1) {
         b |= (value << bitPosition);
      } else {
         b &= ~(1 << bitPosition);
      }

      return b;
   }

   public static short setBit(short word, int bitPosition, int value) {
      checkValue(value);
      checkPosition(bitPosition, 16);

      if (value == 1) {
         word |= (value << bitPosition);
      } else {
         word &= ~(1 << bitPosition);
      }

      return word;
   }

   public static int setBit(int dword, int bitPosition, int value) {
      checkValue(value);
      checkPosition(bitPosition, 32);

      if (value == 1) {
         dword |= (value << bitPosition);
      } else {
         dword &= ~(1 << bitPosition);
      }

      return dword;
   }

   public static long setBit(long qword, int bitPosition, int value) {
      checkValue(value);
      checkPosition(bitPosition, 64);

      if (value == 1) {
         qword |= (value << bitPosition);
      } else {
         qword &= ~(1 << bitPosition);
      }

      return qword;
   }

   public static boolean isBitSet(byte b, int bitPosition) {
      checkPosition(bitPosition, 8);
      return ((b & (1 << bitPosition)) != 0);
   }

   public static boolean isBitSet(short word, int bitPosition) {
      checkPosition(bitPosition, 16);
      return ((word & (1 << bitPosition)) != 0);
   }

   public static boolean isBitSet(int dword, int bitPosition) {
      checkPosition(bitPosition, 32);
      return ((dword & (1 << bitPosition)) != 0);
   }

   public static boolean isBitSet(long qword, int bitPosition) {
      checkPosition(bitPosition, 64);
      return ((qword & (1 << bitPosition)) != 0);
   }

   public static byte toggleBit(byte b, int bitPosition) {
      checkPosition(bitPosition, 8);
      b ^= (1 << bitPosition);
      return b;
   }

   public static short toggleBit(short word, int bitPosition) {
      checkPosition(bitPosition, 16);
      word ^= (1 << bitPosition);
      return word;
   }

   public static int toggleBit(int dword, int bitPosition) {
      checkPosition(bitPosition, 32);
      dword ^= (1 << bitPosition);
      return dword;
   }

   public static long toggleBit(long qword, int bitPosition) {
      checkPosition(bitPosition, 64);
      qword ^= (1 << bitPosition);
      return qword;
   }

   public static String toBitString(byte b) {
      String s = String.format("0b%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
      return s;
   }

   public static String toBitString(short word) {
      String s = String.format("0b%16s", Integer.toBinaryString(word & 0xFFFF)).replace(' ', '0');
      return s;
   }

   public static String toBitString(int dword) {
      String s = String.format("0b%32s", Integer.toBinaryString(dword)).replace(' ', '0');
      return s;
   }

   public static String toBitString(long qword) {
      String s = String.format("0b%64s", Long.toBinaryString(qword & 0xFFFFFFFFFFFFFFFFL)).replace(' ', '0');
      return s;
   }

   public static byte getLowerNibble(byte b) {
      return (byte) (b & 0x0F);
   }

   public static byte getUpperNibble(byte b) {
      return (byte) ((b & 0xF0) >> 4);
   }

   public static byte byteFromNibbles(int upperNibble, int lowerNibble) {
      return byteFromNibbles((byte) upperNibble, (byte) lowerNibble);
   }

   public static byte byteFromNibbles(byte upperNibble, byte lowerNibble) {
      return (byte) ((upperNibble << 4) | lowerNibble);
   }

   public static byte reverse(byte b) {
      b = (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
      b = (byte) ((b & 0xCC) >> 2 | (b & 0x33) << 2);
      b = (byte) ((b & 0xAA) >> 1 | (b & 0x55) << 1);
      return b;
   }

   private static void checkValue(int value) {
      if (value != 0 && value != 1) {
         throw new IllegalArgumentException(ERROR_INVALID_VALUE);
      }
   }

   private static void checkPosition(int bitPosition, int size) {
      if (bitPosition > size - 1 || bitPosition < 0) {
         throw new IllegalArgumentException(String.format(ERROR_INVALID_BIT_POSITION, size, size - 1));
      }
   }
}
