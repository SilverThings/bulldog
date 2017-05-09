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
package io.silverspoon.bulldog.beagleboneblack.devicetree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class DeviceTreeCompiler {

   private static final String FIRMWARE_PATH = "/lib/firmware/";
   private static final String OBJECT_FILE_PATTERN = "%s%s.dtbo";
   private static final String DEFINITION_FILE_PATTERN = "%s%s.dts";
   private static final String COMPILER_CALL = "dtc -O dtb -o %s -b 0 -@ %s";

   public static void compileOverlay(String overlay, String deviceName) throws IOException, InterruptedException {
      String objectFile = String.format(OBJECT_FILE_PATTERN, FIRMWARE_PATH, deviceName);
      String overlayFile = String.format(DEFINITION_FILE_PATTERN, FIRMWARE_PATH, deviceName);

      File file = new File(overlayFile);
      FileOutputStream outputStream = new FileOutputStream(file);
      PrintWriter writer = new PrintWriter(outputStream);
      writer.write(overlay);
      writer.close();

      Process compile = Runtime.getRuntime().exec(String.format(COMPILER_CALL, objectFile, overlayFile));
      int code = compile.waitFor();
      if (code > 0) {
         throw new RuntimeException("Device Tree Overlay compilation failed: " + overlayFile + " could not be compiled");
      }
   }

}
