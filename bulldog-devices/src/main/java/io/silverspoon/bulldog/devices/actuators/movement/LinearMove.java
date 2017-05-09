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
package io.silverspoon.bulldog.devices.actuators.movement;

import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.devices.actuators.Actuator;

public class LinearMove implements Move {

   private double toPosition;
   private int milliseconds;

   public LinearMove(double toPosition, int milliseconds) {
      this.milliseconds = milliseconds;
      this.toPosition = toPosition;
   }

   @Override
   public void execute(Actuator actuator) {
      if (milliseconds <= 0) {
         new DirectMove(toPosition).execute(actuator);
      } else {
         double startPosition = actuator.getPosition();
         double delta = Math.abs(startPosition - toPosition);
         int amountSteps = (int) (milliseconds / actuator.getRefreshIntervalMilliseconds());
         double stepSize = delta / amountSteps;
         for (int i = 0; i < amountSteps; i++) {
            if (startPosition < toPosition) {
               actuator.setPosition(actuator.getPosition() + stepSize);
            } else {
               actuator.setPosition(actuator.getPosition() - stepSize);
            }
            BulldogUtil.sleepMs(actuator.getRefreshIntervalMilliseconds());
         }
      }
   }

}
