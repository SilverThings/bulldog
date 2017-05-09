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
import io.silverspoon.bulldog.core.util.easing.Easing;
import io.silverspoon.bulldog.core.util.easing.EasingOptions;
import io.silverspoon.bulldog.devices.actuators.Actuator;

public class EasedMove implements Move {

   private Easing easing;
   private double toPosition;
   private int durationMilliseconds;
   private EasingOptions options = EasingOptions.EaseInOut;

   public EasedMove(Easing easing, double toPosition) {
      this(easing, toPosition, 0);
   }

   public EasedMove(Easing easing, double toPosition, int durationMilliseconds) {
      this.easing = easing;
      this.toPosition = toPosition;
      this.durationMilliseconds = durationMilliseconds;
   }

   public EasedMove(Easing easing, double toPosition, int durationMilliseconds, EasingOptions options) {
      this.easing = easing;
      this.toPosition = toPosition;
      this.durationMilliseconds = durationMilliseconds;
      this.options = options;
   }

   @Override
   public void execute(Actuator actuator) {
      double startPosition = actuator.getPosition();
      double delta = Math.abs(startPosition - toPosition);
      if (durationMilliseconds <= 0) {
         durationMilliseconds = (int) (delta * actuator.getMillisecondsPerUnit());
      }

      int amountSteps = (int) (durationMilliseconds / actuator.getRefreshIntervalMilliseconds());
      boolean isInverse = startPosition > toPosition;
      for (int i = 0; i < amountSteps - 1; i++) {
         double positionFactor = options.calculate(easing, i, amountSteps);
         double step = positionFactor * delta;
         if (isInverse) {
            actuator.setPosition(startPosition - step);
         } else {
            actuator.setPosition(startPosition + step);
         }

         BulldogUtil.sleepMs(actuator.getRefreshIntervalMilliseconds());
      }

      actuator.setPosition(toPosition);
      BulldogUtil.sleepMs(actuator.getRefreshIntervalMilliseconds());
   }

}
