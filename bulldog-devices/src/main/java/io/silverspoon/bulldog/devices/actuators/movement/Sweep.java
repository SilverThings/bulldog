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

import io.silverspoon.bulldog.devices.actuators.Actuator;

public class Sweep implements Move {

   private int durationMilliseconds;

   public Sweep(int durationMilliseconds) {
      this.durationMilliseconds = durationMilliseconds;
   }

   @Override
   public void execute(Actuator actuator) {
      double currentPosition = actuator.getPosition();
      double totalDistance = (180.0 - currentPosition) + 180.0;

      double timeFactorFirstMove = (180.0 - currentPosition) / totalDistance;
      double timeFactorSecondMove = 1.0 - timeFactorFirstMove;

      new LinearMove(180.0, (int) Math.round(durationMilliseconds * timeFactorFirstMove)).execute(actuator);
      new LinearMove(0.0, (int) Math.round(durationMilliseconds * timeFactorSecondMove)).execute(actuator);
   }

}
