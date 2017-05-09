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
package io.silverspoon.bulldog.raspberrypi.bcm;

import io.silverspoon.bulldog.linux.sysinfo.CpuInfo;

/**
 * BCM implementation factory
 */
public class BCMFactory {

   public static final String BCM2708_NAME = "BCM2708";
   public static final String BCM2709_NAME = "BCM2709";

   private static AbstractBCM instance = null;

   /**
    * Factory method used to retrieve the correct BCM implementation instance.
    *
    * @return BCM instance
    */
   public static AbstractBCM getBCM() {
      if (instance == null) {
         String name = CpuInfo.getHardware();
         if (name.contains(BCM2708_NAME)) {
            instance = new BCM2835();
         } else if (name.contains(BCM2709_NAME)) {
            instance = new BCM2836();
         } else {
            throw new IllegalArgumentException(name);
         }
      }

      return instance;
   }
}
