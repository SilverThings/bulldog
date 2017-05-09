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
package io.silverspoon.bulldog.linux.io.mmap;

import io.silverspoon.bulldog.core.util.BitMagic;

public class ByteRegister extends Register<Byte> {

   public ByteRegister(MemoryMap map, long address) {
      super(map, address, Byte.class);
   }

   @Override
   public void setValue(Byte value) {
      getMemoryMap().setByteValue(getAddress(), value);
   }

   @Override
   public Byte getValue() {
      return getMemoryMap().getByteValueAt(getAddress());
   }

   @Override
   public void setBit(int index) {
      setValue(BitMagic.setBit(getValue(), index, 1));
   }

   @Override
   public void clearBit(int index) {
      setValue(BitMagic.setBit(getValue(), index, 0));
   }

   @Override
   public void toggleBit(int index) {
      setValue(BitMagic.toggleBit(getValue(), index));
   }

}
