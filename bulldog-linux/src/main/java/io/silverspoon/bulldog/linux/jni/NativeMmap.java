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
package io.silverspoon.bulldog.linux.jni;

public class NativeMmap {

   public static final int NONE = 0x00;
   public static final int READ = 0x01;
   public static final int WRITE = 0x02;
   public static final int EXEC = 0x04;

   public static final int SHARED = 0x01;
   public static final int PRIVATE = 0x02;

   public static native long createMap(long address, long length, int protection, int flags, int fileDescriptor, long offset);

   public static native int deleteMap(long address, long length);

   public static native void setLongValueAt(long address, long value);

   public static native long getLongValueAt(long address);

   public static native void setIntValueAt(long address, int value);

   public static native int getIntValueAt(long address);

   public static native void setShortValueAt(long address, short value);

   public static native short getShortValueAt(long address);

   public static native void setByteValueAt(long address, byte value);

   public static native byte getByteValueAt(long address);
}
