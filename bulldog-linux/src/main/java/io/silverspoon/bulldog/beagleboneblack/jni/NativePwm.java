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

public class NativePwm {

   public static native int setup();

   public static native void teardown();

   public static native int setPwm(int pwmId, float frequency, float dutyA, float dutyB);

   public static native void enable(int pwmId);

   public static native void disable(int pwmId);

   private static boolean isInitialized = false;

   static {
      initialize();
   }

   public static void initialize() {
      if (isInitialized) {
         return;
      }
      setup();
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
