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
package io.silverspoon.bulldog.devices.pwmdriver;

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.pwm.AbstractPwm;

public class PCA9685Pwm extends AbstractPwm {

   private PCA9685 driver;

   public PCA9685Pwm(Pin pin, PCA9685 driver) {
      super(pin);
      this.driver = driver;
   }

   @Override
   protected void setupImpl() {
   }

   @Override
   protected void teardownImpl() {
   }

   @Override
   protected void setPwmImpl(double frequency, double duty) {
      driver.setFrequency(frequency);
      driver.setDuty(getPin().getAddress(), duty);
   }

   @Override
   protected void enableImpl() {
      driver.enableChannel(getPin().getAddress());
   }

   @Override
   protected void disableImpl() {
      driver.enableChannel(getPin().getAddress());
   }

}
