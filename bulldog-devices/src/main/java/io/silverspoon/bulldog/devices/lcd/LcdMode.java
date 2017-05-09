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

public enum LcdMode {

   Display1x8(1, 8, 0x00, 0x07),
   Display1x16(1, 16, 0x00, 0x0F),
   Display1x20(1, 20, 0x00, 0x13),
   Display1x40(1, 40, 0x00, 0x27),
   Display2x8(2, 8, 0x00, 0x07, 0x40, 0x47),
   Display2x12(2, 12, 0x00, 0x0b, 0x40, 0x4b),
   Display2x16(2, 16, 0x00, 0x0f, 0x40, 0x4f),
   Display2x20(2, 20, 0x00, 0x13, 0x40, 0x53),
   Display2x24(2, 24, 0x00, 0x17, 0x40, 0x57),
   Display2x40(2, 40, 0x00, 0x27, 0x40, 0x67),
   Display4x16(4, 16, 0x00, 0x0F, 0x40, 0x4f, 0x10, 0x1f, 0x50, 0x5f),
   Display4x20(4, 20, 0x00, 0x13, 0x40, 0x53, 0x14, 0x27, 0x54, 0x67);

   private int[] memoryOffsets;
   private int rows;
   private int columns;

   private LcdMode(int rows, int cols, int... memoryOffsets) {
      this.memoryOffsets = memoryOffsets;
      this.rows = rows;
      this.columns = cols;
   }

   public int getRows() {
      return rows;
   }

   public int getColumns() {
      return columns;
   }

   public int getMemoryOffset(int row, int column) {
      if (column >= getColumns()) {
         throw new RuntimeException("Illegal column value specified");
      }

      if (row >= getRows()) {
         throw new RuntimeException("Illegal row value specified");
      }

      return memoryOffsets[row * 2] + column;
   }
}
