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
package io.silverspoon.bulldog.linux.sysfs;

import io.silverspoon.bulldog.core.util.BulldogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;

@Deprecated
public class SysFs {

   private static final int WAIT_TIMEOUT_MS = 5000;

   public SysFs() {

   }

   /**
    * Returns the first valid path from a given array, or throws IllegalArgumentException
    * @param paths
    * @param pattern Pattern of searched directory
    * @return first valid (exists) path
    * @throws IllegalArgumentException if no paths exist
     */
   public static String findValidPath(String[] paths, String pattern) {
      for (String path : paths) {
         File f = new File(path);
         if (f.exists() && f.isDirectory()) {
            if (getFilesInPath(path, pattern).length != 0) {
               return path;
            }
         }
      }
      throw new IllegalArgumentException("No valid paths");
   }

   public static File[] getFilesInPath(String path, final String namePattern) {
      File root = new File(path);
      File[] files = root.listFiles(new FileFilter() {
         public boolean accept(File pathname) {
            return pathname.getName().startsWith(namePattern);
         }
      });

      return files;
   }

   public void echo(String path, Object value) {
      echo(path, String.valueOf(value));
   }

   public void echo(String path, String value) {
      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(path));
         writer.write(value);
         writer.close();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void echoAndWait(String path, Object value, int waitMs) {
      echoAndWait(path, String.valueOf(value), waitMs);
   }

   public void echoAndWait(String path, String value, int waitMs) {
      try {
         waitForFileCreation(path, WAIT_TIMEOUT_MS);
         BufferedWriter writer = new BufferedWriter(new FileWriter(path));
         writer.write(value);
         BulldogUtil.sleepMs(waitMs);
         writer.close();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void waitForFileCreation(String filePath, long waitMillis) {
      long startWaitingTime = System.currentTimeMillis();
      File file = new File(filePath);
      while (!file.exists()) {
         BulldogUtil.sleepMs(10);

         long millisecondsInWait = System.currentTimeMillis() - startWaitingTime;
         if (millisecondsInWait >= waitMillis) {
            throw new RuntimeException("Could not find file " + filePath + " within " + waitMillis + " milliseconds. Aborting.");
         }
      }
   }

}
