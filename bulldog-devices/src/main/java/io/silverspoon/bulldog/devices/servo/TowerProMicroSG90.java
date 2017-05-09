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

import io.silverspoon.bulldog.core.pwm.Pwm;

public class TowerProMicroSG90 extends Servo {

   private static final double MIN_ANGLE_DEFAULT = 0.0225;
   private static final double MAX_ANGLE_DEFAULT = 0.1150;
   private static final double INITIAL_ANGLE_DEFAULT = 0.0;
   private static final int TIME_PER_DEGREE = (int) (0.1 / 60.0 * 1000);

   public TowerProMicroSG90(Pwm pwm) {
      super(pwm, INITIAL_ANGLE_DEFAULT, MIN_ANGLE_DEFAULT, MAX_ANGLE_DEFAULT, TIME_PER_DEGREE);
   }
}
