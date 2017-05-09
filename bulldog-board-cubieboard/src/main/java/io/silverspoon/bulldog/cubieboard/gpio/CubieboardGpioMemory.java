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
package io.silverspoon.bulldog.cubieboard.gpio;

import io.silverspoon.bulldog.linux.io.mmap.MemoryMap;

public class CubieboardGpioMemory {
   private static final long SUNXI_SW_PORTC_IO_BASE = 0x01c20800;
   private static final long SUNXI_GPIO_DATA_OFFSET = 0x10;

   private static final long MAP_SIZE = 4096 * 2;
   private static final long MAP_MASK = ~(MAP_SIZE - 1);

   private static final long[] PORT_OFFSETS = new long[20];
   private static final long[] SET_PORT_MASK = new long[32];
   private static final long[] CLEAR_PORT_MASK = new long[32];

   static {
      for (int i = 0; i < PORT_OFFSETS.length; i++) {
         PORT_OFFSETS[i] = i * 36;
      }
      for (int i = 0; i < SET_PORT_MASK.length; i++) {
         SET_PORT_MASK[i] = 1 << i;
         CLEAR_PORT_MASK[i] = ~(1 << i);
      }
   }

   private final MemoryMap memoryMap;
   private final long addrOffset;

   public CubieboardGpioMemory() {
      long addrStart = SUNXI_SW_PORTC_IO_BASE & MAP_MASK;
      addrOffset = SUNXI_SW_PORTC_IO_BASE & ~MAP_MASK;

      memoryMap = new MemoryMap("/dev/mem", addrStart, MAP_SIZE);
   }

   public void setPinDirection(int portIndex, int pinIndex, int dir) {
      long index = (pinIndex >> 3) << 2;
      long offset = addrOffset + PORT_OFFSETS[portIndex] + index;

      long cfg = memoryMap.getLongValueAt(offset);

      int pinOffset = ((pinIndex & 0x7) << 2);
      cfg &= ~(0xF << pinOffset);
      if (dir == 1) {
         cfg |= 1 << pinOffset;
      }

      memoryMap.setLongValue(offset, cfg);
   }

   public void setPinValue(int portIndex, int pinIndex, int value) {
      long offset = addrOffset + SUNXI_GPIO_DATA_OFFSET + PORT_OFFSETS[portIndex];
      long dat = memoryMap.getLongValueAt(offset);

      if (value == 1) {
         dat |= SET_PORT_MASK[pinIndex];
      } else {
         dat &= CLEAR_PORT_MASK[pinIndex];
      }

      memoryMap.setLongValue(offset, dat);
   }

   public int getPinValue(int portIndex, int pinIndex) {
      long offset = addrOffset + SUNXI_GPIO_DATA_OFFSET + PORT_OFFSETS[portIndex];
      long dat = memoryMap.getLongValueAt(offset);

      return (int) (dat & SET_PORT_MASK[pinIndex]);
   }

   public void cleanup() {
      memoryMap.closeMap();
   }
}
