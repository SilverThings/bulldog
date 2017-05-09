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
package io.silverspoon.bulldog.raspberrypi.bcm;

/**
 * BCM2836 implementation of {@link io.silverspoon.bulldog.raspberrypi.bcm.AbstractBCM}
 * Used by RaspberryPi 2
 */
public class BCM2836 extends AbstractBCM {

   public static final int BCM_PERI_BASE = 0x3F000000;
   public static final int GPIO_BASE = (BCM_PERI_BASE + 0x200000);
   public static final int PWM_BASE = (BCM_PERI_BASE + 0x20C000);
   public static final int CLOCK_BASE = (BCM_PERI_BASE + 0x101000);
   public static final int REGISTER_SIZE = 4;

   public static final int PWMCLK_CNTL = 40 * REGISTER_SIZE;
   public static final int PWMCLK_DIV = 41 * REGISTER_SIZE;

   public static final int PWM_CTL = 0;
   public static final int PWM_RNG1 = 4 * REGISTER_SIZE;
   public static final int PWM_DAT1 = 5 * REGISTER_SIZE;

   public static final int GPIO_SET = 7 * REGISTER_SIZE;
   public static final int GPIO_CLEAR = 10 * REGISTER_SIZE;
   public static final int GPIO_READ = 13 * REGISTER_SIZE;

   @Override
   public int getBCMPeriBase() {
      return BCM_PERI_BASE;
   }

   @Override
   public int getGPIOBase() {
      return GPIO_BASE;
   }

   @Override
   public int getPWMBase() {
      return PWM_BASE;
   }

   @Override
   public int getClockBase() {
      return CLOCK_BASE;
   }

   @Override
   public int getPWMClkCntl() {
      return PWMCLK_CNTL;
   }

   @Override
   public int getPWMClkDiv() {
      return PWMCLK_DIV;
   }

   @Override
   public int getPWMCtl() {
      return PWM_CTL;
   }

   @Override
   public int getPWMRng1() {
      return PWM_RNG1;
   }

   @Override
   public int getPWMDat1() {
      return PWM_DAT1;
   }

   @Override
   public int getGPIOSet() {
      return GPIO_SET;
   }

   @Override
   public int getGPIOClear() {
      return GPIO_CLEAR;
   }

   @Override
   public int getGPIORead() {
      return GPIO_READ;
   }

   @Override
   public int getRegisterSize() {
      return REGISTER_SIZE;
   }
}
