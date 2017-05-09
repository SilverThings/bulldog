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
package io.silverspoon.bulldog.core.util;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public final class BulldogUtil {

   public static void sleepMs(final long ms) {
      try {
         Thread.sleep(ms);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   public static void sleepNs(final long ns) {
      final long start = System.nanoTime();
      final long end = start + ns;
      long now = 0;
      do {
         now = System.nanoTime();
      } while (now < end);
   }

   public static String bytesToString(byte[] bytes, String encoding) {
      if (bytes == null) {
         throw new IllegalArgumentException("bytes may not be null in string conversion");
      }

      if (bytes.length == 0) {
         return null;
      }

      try {
         return new String(bytes, encoding);
      } catch (Exception e) {
         throw new IllegalArgumentException("Unknown encoding");
      }
   }

   public static String bytesToString(byte[] bytes) {
      return bytesToString(bytes, "ASCII");
   }

   @SuppressWarnings("resource")
   public static String convertStreamToString(java.io.InputStream is) {
      Scanner s = new Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
   }

   public static String readFileAsString(String path) {
      try {
         byte[] encoded = Files.readAllBytes(Paths.get(path));
         return new String(encoded, Charset.defaultCharset());
      } catch (Exception ex) {
         return null;
      }
   }

   public static boolean isStringNumeric(String str) {
      return str.matches("-?\\d+(\\.\\d+)?");
   }

   public static final int getUnsignedByte(byte b) {
      return b & 0xFF;
   }
}
