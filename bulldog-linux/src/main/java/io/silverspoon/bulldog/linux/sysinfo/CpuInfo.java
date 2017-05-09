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
package io.silverspoon.bulldog.linux.sysinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class CpuInfo {
   private final static Logger logger = LoggerFactory.getLogger(CpuInfo.class);

   private static final String CPU_REVISION = "CPU revision";
   private static final String HW = "Hardware";

   private static HashMap<String, String> properties = new HashMap<String, String>();

   static {
      String cpuInfo = readFile("/proc/cpuinfo");
      String[] infos = cpuInfo.split("\n");
      for (String info : infos) {
         String[] tokens = info.split(":");
         if (tokens.length >= 2) {
            properties.put(tokens[0].trim(), tokens[1].trim());
         }
      }
   }

   public static String getCPURevision() {
      return properties.get(CPU_REVISION);
   }

   public static String getHardware() {
      return properties.get(HW);
   }

   private static String readFile(String path) {
      try {
         byte[] encoded = Files.readAllBytes(Paths.get(path));
         return new String(encoded, Charset.defaultCharset());
      } catch (IOException e) {
         logger.error("Cannot read file " + path, e);
      }

      return "";
   }
}
