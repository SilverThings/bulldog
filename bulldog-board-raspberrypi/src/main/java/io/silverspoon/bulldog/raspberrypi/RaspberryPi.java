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
package io.silverspoon.bulldog.raspberrypi;

import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.AbstractBoard;
import io.silverspoon.bulldog.linux.io.LinuxSpiBus;
import io.silverspoon.bulldog.linux.sysinfo.CpuInfo;
import io.silverspoon.bulldog.raspberrypi.bcm.AbstractBCM;
import io.silverspoon.bulldog.raspberrypi.bcm.BCMFactory;
import io.silverspoon.bulldog.raspberrypi.gpio.RaspiDigitalInput;
import io.silverspoon.bulldog.raspberrypi.gpio.RaspiDigitalOutput;
import io.silverspoon.bulldog.raspberrypi.io.RaspberryPiI2cBus;
import io.silverspoon.bulldog.raspberrypi.pwm.RaspiPwm;

public class RaspberryPi extends AbstractBoard {

   public static final AbstractBCM BCM = BCMFactory.getBCM();
   private static final String NAME = "Raspberry Pi";

   RaspberryPi() {
      super();
      if (getRevision() >= 0xd) {
         createPinsPlus();
         createIoPortsRev2();
      }
      else if (getRevision() >= 4 ) {
         createPinsRev2();
         createIoPortsRev2();
      } else {
         createPinsRev1();
         createIoPortsRev1();
      }
   }

   @Override
   public String getName() {
      return NAME;
   }

   private void createPinsRev1() {
      getPins().add(createDigitalIOPin(RaspiNames.P1_3, "P1", 3, 0));
      getPins().add(createDigitalIOPin(RaspiNames.P1_5, "P1", 5, 1));
      getPins().add(createDigitalIOPin(RaspiNames.P1_7, "P1", 7, 4));
      getPins().add(createDigitalIOPin(RaspiNames.P1_8, "P1", 8, 14));
      getPins().add(createDigitalIOPin(RaspiNames.P1_10, "P1", 10, 15));
      getPins().add(createDigitalIOPin(RaspiNames.P1_11, "P1", 11, 17));
      getPins().add(createDigitalIOPin(RaspiNames.P1_12, "P1", 12, 18));
      getPins().add(createDigitalIOPin(RaspiNames.P1_13, "P1", 13, 21));
      getPins().add(createDigitalIOPin(RaspiNames.P1_15, "P1", 15, 22));
      getPins().add(createDigitalIOPin(RaspiNames.P1_16, "P1", 16, 23));
      getPins().add(createDigitalIOPin(RaspiNames.P1_18, "P1", 18, 24));
      getPins().add(createDigitalIOPin(RaspiNames.P1_19, "P1", 19, 10));
      getPins().add(createDigitalIOPin(RaspiNames.P1_21, "P1", 21, 9));
      getPins().add(createDigitalIOPin(RaspiNames.P1_22, "P1", 22, 25));
      getPins().add(createDigitalIOPin(RaspiNames.P1_23, "P1", 23, 11));
      getPins().add(createDigitalIOPin(RaspiNames.P1_24, "P1", 24, 8));
      getPins().add(createDigitalIOPin(RaspiNames.P1_26, "P1", 26, 7));

      Pin pwmPin = getPin(RaspiNames.P1_12);
      pwmPin.addFeature(new RaspiPwm(pwmPin));
   }

   private void createPinsRev2() {
      getPins().add(createDigitalIOPin(RaspiNames.P1_3, "P1", 3, 2));
      getPins().add(createDigitalIOPin(RaspiNames.P1_5, "P1", 5, 3));
      getPins().add(createDigitalIOPin(RaspiNames.P1_7, "P1", 7, 4));
      getPins().add(createDigitalIOPin(RaspiNames.P1_8, "P1", 8, 14));
      getPins().add(createDigitalIOPin(RaspiNames.P1_10, "P1", 10, 15));
      getPins().add(createDigitalIOPin(RaspiNames.P1_11, "P1", 11, 17));
      getPins().add(createDigitalIOPin(RaspiNames.P1_12, "P1", 12, 18));
      getPins().add(createDigitalIOPin(RaspiNames.P1_13, "P1", 13, 27));
      getPins().add(createDigitalIOPin(RaspiNames.P1_15, "P1", 15, 22));
      getPins().add(createDigitalIOPin(RaspiNames.P1_16, "P1", 16, 23));
      getPins().add(createDigitalIOPin(RaspiNames.P1_18, "P1", 18, 24));
      getPins().add(createDigitalIOPin(RaspiNames.P1_19, "P1", 19, 10));
      getPins().add(createDigitalIOPin(RaspiNames.P1_21, "P1", 21, 9));
      getPins().add(createDigitalIOPin(RaspiNames.P1_22, "P1", 22, 25));
      getPins().add(createDigitalIOPin(RaspiNames.P1_23, "P1", 23, 11));
      getPins().add(createDigitalIOPin(RaspiNames.P1_24, "P1", 24, 8));
      getPins().add(createDigitalIOPin(RaspiNames.P1_26, "P1", 26, 7));

      getPins().add(createDigitalIOPin(RaspiNames.P1_29, "P1", 29, 5));
      getPins().add(createDigitalIOPin(RaspiNames.P1_31, "P1", 31, 6));
      getPins().add(createDigitalIOPin(RaspiNames.P1_32, "P1", 32, 12));
      getPins().add(createDigitalIOPin(RaspiNames.P1_33, "P1", 33, 13));
      getPins().add(createDigitalIOPin(RaspiNames.P1_35, "P1", 35, 19));
      getPins().add(createDigitalIOPin(RaspiNames.P1_36, "P1", 36, 16));
      getPins().add(createDigitalIOPin(RaspiNames.P1_37, "P1", 37, 26));
      getPins().add(createDigitalIOPin(RaspiNames.P1_38, "P1", 38, 20));
      getPins().add(createDigitalIOPin(RaspiNames.P1_40, "P1", 40, 21));

      getPins().add(createDigitalIOPin(RaspiNames.P5_3, "P5", 3, 28));
      getPins().add(createDigitalIOPin(RaspiNames.P5_4, "P5", 4, 29));
      getPins().add(createDigitalIOPin(RaspiNames.P5_5, "P5", 5, 30));
      getPins().add(createDigitalIOPin(RaspiNames.P5_6, "P5", 6, 31));

      Pin pwmPin = getPin(RaspiNames.P1_12);
      pwmPin.addFeature(new RaspiPwm(pwmPin));
   }

   private void createPinsPlus() {
      getPins().add(createDigitalIOPin(RaspiNames.P1_3, "P1", 3, 2));
      getPins().add(createDigitalIOPin(RaspiNames.P1_5, "P1", 5, 3));
      getPins().add(createDigitalIOPin(RaspiNames.P1_7, "P1", 7, 4));
      getPins().add(createDigitalIOPin(RaspiNames.P1_8, "P1", 8, 14));
      getPins().add(createDigitalIOPin(RaspiNames.P1_10, "P1", 10, 15));
      getPins().add(createDigitalIOPin(RaspiNames.P1_11, "P1", 11, 17));
      getPins().add(createDigitalIOPin(RaspiNames.P1_12, "P1", 12, 18));
      getPins().add(createDigitalIOPin(RaspiNames.P1_13, "P1", 13, 27));
      getPins().add(createDigitalIOPin(RaspiNames.P1_15, "P1", 15, 22));
      getPins().add(createDigitalIOPin(RaspiNames.P1_16, "P1", 16, 23));
      getPins().add(createDigitalIOPin(RaspiNames.P1_18, "P1", 18, 24));
      getPins().add(createDigitalIOPin(RaspiNames.P1_19, "P1", 19, 10));
      getPins().add(createDigitalIOPin(RaspiNames.P1_21, "P1", 21, 9));
      getPins().add(createDigitalIOPin(RaspiNames.P1_22, "P1", 22, 25));
      getPins().add(createDigitalIOPin(RaspiNames.P1_23, "P1", 23, 11));
      getPins().add(createDigitalIOPin(RaspiNames.P1_24, "P1", 24, 8));
      getPins().add(createDigitalIOPin(RaspiNames.P1_26, "P1", 26, 7));
      getPins().add(createDigitalIOPin(RaspiNames.P1_29, "P1", 29, 5));
      getPins().add(createDigitalIOPin(RaspiNames.P1_31, "P1", 31, 6));
      getPins().add(createDigitalIOPin(RaspiNames.P1_32, "P1", 32, 12));
      getPins().add(createDigitalIOPin(RaspiNames.P1_33, "P1", 33, 13));
      getPins().add(createDigitalIOPin(RaspiNames.P1_35, "P1", 35, 19));
      getPins().add(createDigitalIOPin(RaspiNames.P1_36, "P1", 36, 16));
      getPins().add(createDigitalIOPin(RaspiNames.P1_37, "P1", 37, 37));
      getPins().add(createDigitalIOPin(RaspiNames.P1_38, "P1", 38, 38));
      getPins().add(createDigitalIOPin(RaspiNames.P1_40, "P1", 40, 40));
   }

   private Pin createDigitalIOPin(String name, String port, int portIndex, int gpioAddress) {
      RaspberryPiPin pin = new RaspberryPiPin(name, gpioAddress, port, portIndex, gpioAddress);
      pin.addFeature(new DigitalIOFeature(pin, new RaspiDigitalInput(pin), new RaspiDigitalOutput(pin)));
      return pin;
   }

   private void createIoPortsRev1() {
      getI2cBuses().add(new RaspberryPiI2cBus(RaspiNames.I2C_0, "/dev/i2c-0", getPin(RaspiNames.P1_3), getPin(RaspiNames.P1_5)));
      getSpiBuses().add(new LinuxSpiBus(RaspiNames.SPI_0_CS0, "/dev/spidev0.0", this));
      getSpiBuses().add(new LinuxSpiBus(RaspiNames.SPI_0_CS1, "/dev/spidev0.1", this));
   }

   private void createIoPortsRev2() {
      getI2cBuses().add(new RaspberryPiI2cBus(RaspiNames.I2C_1, "/dev/i2c-1", getPin(RaspiNames.P1_3), getPin(RaspiNames.P1_5)));
      getSpiBuses().add(new LinuxSpiBus(RaspiNames.SPI_0_CS0, "/dev/spidev0.0", this));
      getSpiBuses().add(new LinuxSpiBus(RaspiNames.SPI_0_CS1, "/dev/spidev0.1", this));
   }

   private int getRevision() {
      String revision = CpuInfo.getCPURevision();
      if (revision.length() > 4) {
         revision = revision.substring(revision.length() - 4);
      }

      int numericRevision = Integer.parseInt(revision);
      return numericRevision;
   }

   @Override
   public void cleanup() {
      super.cleanup();
      BCM.cleanup();
   }

}
