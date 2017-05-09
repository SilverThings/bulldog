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
package io.silverspoon.bulldog.core;

import io.silverspoon.bulldog.core.util.BulldogUtil;

public enum Signal {
   High(1, true),
   Low(0, false);

   private int numericValue;
   private boolean booleanValue;

   Signal(int numericValue, boolean booleanValue) {
      this.numericValue = numericValue;
      this.booleanValue = booleanValue;
   }

   public int getNumericValue() {
      return numericValue;
   }

   public boolean getBooleanValue() {
      return booleanValue;
   }

   public static Signal fromNumericValue(int value) {
      if (value == 0) {
         return Signal.Low;
      }
      return Signal.High;
   }

   public static Signal fromBooleanValue(boolean value) {
      if (value) {
         return Signal.High;
      }
      return Signal.Low;
   }

   public static Signal fromString(String value) {
      if (value == null) {
         throw new IllegalArgumentException("value cannot be null!");
      }
      ;

      String interpretedValue = value.trim().toLowerCase();

      if (BulldogUtil.isStringNumeric(interpretedValue)) {
         if (Double.parseDouble(interpretedValue) == 0) {
            return Signal.Low;
         }
         return Signal.High;
      } else {
         if (interpretedValue.equals("low")) {
            return Signal.Low;
         } else if (interpretedValue.equals("high")) {
            return Signal.High;
         }
         ;

         throw new IllegalArgumentException(interpretedValue + " is not a valid value for a signal");
      }
   }

   public Signal inverse() {
      return this == Signal.High ? Signal.Low : Signal.High;
   }
}
