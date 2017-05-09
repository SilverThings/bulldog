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
package io.silverspoon.bulldog.beagleboneblack.jni;

public class NativeGpio {

   public static final int DIRECTION_OUT = 1;
   public static final int DIRECTION_IN = 0;

   public static final int HIGH = 1;
   public static final int LOW = 0;

   public static native boolean setup();

   public static native boolean teardown();

   public static native void pinMode(int port, int pin, int direction);

   public static native int digitalRead(int port, int pin);

   public static native void digitalWrite(int port, int pin, int state);

   public static native void enableGpio();
   //public static native int debouncePin(int port, int pin, int ms);

   private static boolean isInitialized = false;

   static {
      initialize();
   }

   public static int getBank(int pin) {
      return (int) (pin / 32);
   }

   public static void initialize() {
      if (isInitialized) {
         return;
      }
      setup();
      enableGpio();
      isInitialized = true;
      Runtime.getRuntime().addShutdownHook(new Thread() {
         @Override
         public void run() {
            deinitialize();
         }
      });

   }

   public static void deinitialize() {
      if (isInitialized) {
         teardown();
         isInitialized = false;
      }
   }
}
