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
package io.silverspoon.bulldog.devices.led;

import io.silverspoon.bulldog.core.pwm.Pwm;
import io.silverspoon.bulldog.core.pwm.PwmController;
import io.silverspoon.bulldog.core.util.easing.Easing;
import io.silverspoon.bulldog.core.util.easing.EasingOptions;
import io.silverspoon.bulldog.core.util.easing.SineEasing;

public class Led {

   private Pwm pwm;
   private PwmController pwmController;
   private Easing defaultEasing;

   public Led(Pwm pwm) {
      this(pwm, new SineEasing());
   }

   public Led(Pwm pwm, Easing defaultEasing) {
      this.pwm = pwm;
      this.defaultEasing = defaultEasing;
      this.pwmController = new PwmController();
      pwm.setFrequency(10000.0);
      pwm.setDuty(0.0);
      pwm.enable();
   }

   public void fadeOut(int milliseconds) {
      fadeOut(milliseconds, defaultEasing);
   }

   public void fadeOut(int milliseconds, Easing easing) {
      pwmController.dutyTransition(pwm, 0.0, milliseconds, easing, EasingOptions.EaseOut);
   }

   public void fadeIn(int milliseconds) {
      fadeIn(milliseconds, defaultEasing);
   }

   public void fadeIn(int milliseconds, Easing easing) {
      pwmController.dutyTransition(pwm, 1.0, milliseconds, easing, EasingOptions.EaseIn);
   }

   public void fadeToBrightness(int milliseconds, double toBrightness, Easing easing) {
      pwmController.dutyTransition(pwm, toBrightness, milliseconds, easing, EasingOptions.EaseIn);
   }

   public void fadeToBrightness(int milliseconds, double toBrightness) {
      fadeToBrightness(milliseconds, toBrightness, defaultEasing);
   }

   public void setBrightness(double brightness) {
      pwm.setDuty(brightness);
   }

   public void on() {
      pwm.enable();
   }

   public void off() {
      pwm.disable();
   }

}
