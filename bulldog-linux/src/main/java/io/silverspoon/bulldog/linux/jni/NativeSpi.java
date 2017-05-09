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

import java.nio.ByteBuffer;

public class NativeSpi {

   public static native int spiOpen(String filename, int mode, int speed, int bitsPerWord, boolean lsbFirst);

   public static native int spiConfig(int fileDescriptor, int mode, int speed, int bitsPerWord, boolean lsbFirst);

   public static native int spiClose(int fileDescriptor);

   public static native int spiTransfer(int fileDescriptor, ByteBuffer txBuffer, ByteBuffer rxBuffer, int transferLength, int delay, int speed, int bitsPerWord);

}
