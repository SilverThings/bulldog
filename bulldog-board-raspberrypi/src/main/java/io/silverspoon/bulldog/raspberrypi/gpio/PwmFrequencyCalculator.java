package io.silverspoon.bulldog.raspberrypi.gpio;

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
