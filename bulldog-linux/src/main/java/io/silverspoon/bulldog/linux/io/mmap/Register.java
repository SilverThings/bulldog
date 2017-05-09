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

public abstract class Register<T extends Number> {

   private MemoryMap map;
   private long address;
   private Class<T> clazz;

   public Register(MemoryMap map, long address, Class<T> clazz) {
      this.map = map;
      this.address = address;
      this.clazz = clazz;
   }

   public abstract void setValue(T value);

   public abstract T getValue();

   public abstract void setBit(int index);

   public abstract void clearBit(int index);

   public abstract void toggleBit(int index);

   public Class<T> getContentClass() {
      return clazz;
   }

   public MemoryMap getMemoryMap() {
      return map;
   }

   public long getAddress() {
      return address;
   }

   @SuppressWarnings("unchecked")
   public void xor(T val) {
      long currentValue = getValue().longValue();
      currentValue ^= val.longValue();
      this.setValue((T) val.getClass().cast(currentValue));
   }

   @SuppressWarnings("unchecked")
   public void or(T val) {
      long currentValue = getValue().longValue();
      currentValue |= val.longValue();
      this.setValue((T) val.getClass().cast(currentValue));
   }

   @SuppressWarnings("unchecked")
   public void and(T val) {
      long currentValue = getValue().longValue();
      currentValue &= val.longValue();
      this.setValue((T) val.getClass().cast(currentValue));
   }

}
