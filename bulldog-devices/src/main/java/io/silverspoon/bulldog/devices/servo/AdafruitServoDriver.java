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
package io.silverspoon.bulldog.devices.servo;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.devices.pwmdriver.PCA9685;

public class AdafruitServoDriver extends PCA9685 {

   private static final String NAME = "ADAFRUIT 16-CHANNEL 12-BIT PWM/SERVO DRIVER - I2C INTERFACE";

   public AdafruitServoDriver(I2cConnection connection) {
      super(connection);
      setName(NAME);
   }

   public AdafruitServoDriver(I2cBus bus, int address) {
      this(bus.createI2cConnection(address));
   }
}
