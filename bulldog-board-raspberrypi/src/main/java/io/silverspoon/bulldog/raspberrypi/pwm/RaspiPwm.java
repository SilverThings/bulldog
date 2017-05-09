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

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.pwm.AbstractPwm;
import io.silverspoon.bulldog.core.util.BitMagic;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.raspberrypi.RaspberryPiPin;
import io.silverspoon.bulldog.raspberrypi.bcm.AbstractBCM;
import io.silverspoon.bulldog.raspberrypi.bcm.BCMFactory;

public class RaspiPwm extends AbstractPwm {

   private double previousFrequency = 0.0;
   
   public static final AbstractBCM BCM = BCMFactory.getBCM();

   public RaspiPwm(Pin pin) {
      super(pin);
   }

   @Override
   protected void setupImpl() {
      RaspberryPiPin pin = (RaspberryPiPin) getPin();
      BCM.configureAlternateFunction(pin.getGpioNumber(), 5);
      stopClock();
      int value = BCM.getPwmMemory().getIntValueAt(BCM.getPWMCtl());
      value = BitMagic.setBit(value, 5, 0);
      value = BitMagic.setBit(value, 7, 1);
      BCM.getPwmMemory().setIntValue(BCM.getPWMCtl(), value);
      value = BCM.getPwmMemory().getIntValueAt(BCM.getPWMCtl());
   }

   @Override
   protected void teardownImpl() {
      disableImpl();
      stopClock();
   }

   @Override
   protected void setPwmImpl(double frequency, double duty) {
      if (previousFrequency != frequency) {
         setFrequencyImpl(frequency);
         previousFrequency = frequency;
      }

      setDutyImpl(duty);
   }

   private void setFrequencyImpl(double frequency) {
      if (isEnabled()) {
         disableImpl();
      }

      int divisorRegister = PwmFrequencyCalculator.calculateDivisorRegister(frequency);
      BCM.getClockMemory().setIntValue(BCM.getPWMClkDiv(), divisorRegister);
      BCM.getPwmMemory().setIntValue(BCM.getPWMRng1(), 0x100000);
      BulldogUtil.sleepMs(1);
      startClock();

      if (isEnabled()) {
         enableImpl();
      }
   }

   protected void setDutyImpl(double duty) {
      int myDuty = (int) (0x100000 * duty);
      BCM.getPwmMemory().setIntValue(BCM.getPWMDat1(), myDuty);
   }

   private void startClock() {
      BCM.getClockMemory().setIntValue(BCM.getPWMClkCntl(), 0x5A000011);
   }

   private void stopClock() {
      BCM.getClockMemory().setIntValue(BCM.getPWMClkCntl(), 0x5A000000 | (1 << 5));
      BulldogUtil.sleepMs(1);
   }

   @Override
   protected void enableImpl() {
      int value = BCM.getPwmMemory().getIntValueAt(BCM.getPWMCtl());
      BCM.getPwmMemory().setIntValue(BCM.getPWMCtl(), BitMagic.setBit(value, 0, 1));
      BulldogUtil.sleepMs(1);
   }

   @Override
   protected void disableImpl() {
      int value = BCM.getPwmMemory().getIntValueAt(BCM.getPWMCtl());
      BCM.getPwmMemory().setIntValue(BCM.getPWMCtl(), BitMagic.setBit(value, 0, 0));
      BulldogUtil.sleepMs(1);
   }

}
