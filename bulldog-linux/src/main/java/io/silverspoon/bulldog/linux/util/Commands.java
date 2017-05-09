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
package io.silverspoon.bulldog.linux.util;

import java.io.IOException;
import java.io.InputStream;

@Deprecated
public class Commands {

   public static String cat(String filename) {
      return shellExecute("cat", filename);
   }

   public static String shellExecute(String command, String... args) {
      ProcessBuilder cmd;
      String result = "";

      try {
         String[] processInfo = new String[args.length + 1];
         processInfo[0] = command;
         for (int i = 0; i < args.length; i++) {
            processInfo[i + 1] = args[i];
         }
         cmd = new ProcessBuilder(processInfo);

         Process process = cmd.start();
         InputStream in = process.getInputStream();
         byte[] re = new byte[1024];
         while (in.read(re) != -1) {
            result = result + new String(re);
         }
         in.close();
      } catch (IOException ex) {
         throw new RuntimeException(ex);
      }

      return result;
   }
}
