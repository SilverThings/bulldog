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
package io.silverspoon.bulldog.raspberrypi.pwm;

public class PwmFrequencyCalculator {

   private static final double CLOCK_FREQUENCY = 19200000.0;

   public static int calculateDivisorRegister(double targetFrequency) {

      int minDivF = 1024;
      int currentDivreg = 0;

      for (int i = 1; i < 4096; i++) {
         boolean error = false;
         double frequency = targetFrequency * i;

         double divisor = CLOCK_FREQUENCY / frequency;
         int DIVI = (int) Math.floor(divisor);
         int DIVF = (int) Math.floor((divisor - DIVI) * 1024);
         int divreg = (0x5a << 24) | ((int) DIVI << 12) | (DIVF);

         if (Double.isNaN(DIVF) || Double.isInfinite(DIVI) || DIVI < 1 || DIVI > 4095) {
            error = true;
         }

         if (DIVF < minDivF && error == false) {
            currentDivreg = divreg;
            minDivF = DIVF;
         }

         if (DIVF == 0 && error == false) {
            return divreg;
         }

      }

      return currentDivreg;

   }

}
