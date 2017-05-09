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
package io.silverspoon.bulldog.core.pwm;

import io.silverspoon.bulldog.core.util.easing.Easing;
import io.silverspoon.bulldog.core.util.easing.EasingOptions;

public class PwmController {

   public void dutyTransition(Pwm pwm, double toDuty, int milliseconds, Easing easing, EasingOptions option) {
      long startTime = System.currentTimeMillis();
      double startDuty = pwm.getDuty();
      double diff = Math.abs(startDuty - toDuty);
      long currentTime = 0;
      while (currentTime < milliseconds) {
         currentTime = System.currentTimeMillis() - startTime;
         double currentDuty = diff * option.calculate(easing, currentTime, milliseconds);
         if (startDuty < toDuty) {
            double duty = startDuty + currentDuty;
            if (duty > 1.0) {
               duty = 1.0;
            }
            if (duty < 0.0) {
               duty = 0.0;
            }
            pwm.setDuty(duty);
         } else {
            double duty = startDuty - currentDuty;
            if (duty > 1.0) {
               duty = 1.0;
            }
            if (duty < 0.0) {
               duty = 0.0;
            }
            pwm.setDuty(duty);
         }
      }
   }

   public void frequencyTransition(Pwm pwm, double toFrequency, int milliseconds, Easing easing, EasingOptions option) {
      long startTime = System.currentTimeMillis();
      double startFrequency = pwm.getFrequency();
      double diff = Math.abs(startFrequency - toFrequency);

      long currentTime = 0;
      while (currentTime < milliseconds) {
         currentTime = System.currentTimeMillis() - startTime;
         double currentDuty = diff * option.calculate(easing, currentTime, milliseconds);
         if (startFrequency < toFrequency) {
            pwm.setDuty(startFrequency + currentDuty);
         } else {
            pwm.setDuty(startFrequency - currentDuty);
         }
      }
   }

}
