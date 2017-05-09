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
package io.silverspoon.bulldog.devices.sensors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Dallas DS18B20 temperature sensor implementation.
 *
 * @author sbunciak
 */
public class DS18B20TemperatureSensor {

   private File deviceFile = null;

   public DS18B20TemperatureSensor(File deviceFile) {
      this.deviceFile = deviceFile;
   }

   // TODO: implement using UART?
   public float readTemperature() throws IOException {

      byte[] encoded = Files.readAllBytes(new File(deviceFile, "w1_slave").toPath());

      String tmp = new String(encoded);
      int tmpIndex = tmp.indexOf("t=");

      if (tmpIndex < 0) {
         throw new IOException("Could not read temperature!");
      }

      return Integer.parseInt(tmp.substring(tmpIndex + 2).trim()) / 1000f;
   }
}
