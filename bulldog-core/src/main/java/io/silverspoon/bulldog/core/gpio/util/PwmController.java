package io.silverspoon.bulldog.core.gpio.util;

import io.silverspoon.bulldog.core.gpio.Pwm;
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
