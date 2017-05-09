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

public class NativeI2c {

   public static native byte i2cRead(int fileDescriptor);

   public static native int i2cWrite(int fileDescriptor, byte data);

   public static native int i2cSelectSlave(int busFileDescriptor, int slaveAddress);

   public static native int i2cOpen(String devicePath);

   public static native int i2cClose(int fileDescriptor);

}
