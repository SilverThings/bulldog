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
package io.silverspoon.bulldog.devices.motor;

import io.silverspoon.bulldog.devices.actuators.Actuator;
import io.silverspoon.bulldog.devices.actuators.movement.Move;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractStepper implements Actuator {

   private ExecutorService executor = Executors.newSingleThreadExecutor();
   private Future<?> currentMove = null;

   public abstract void forward();

   public abstract void backward();

   public abstract void stop();

   public void move(Move move) {
      move.execute(this);
   }

   public void moveAsync(final Move move) {
      currentMove = executor.submit(new Runnable() {

         @Override
         public void run() {
            move(move);
         }

      });
   }

   public void awaitMoveCompleted() {
      if (currentMove != null) {
         try {
            currentMove.get();
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }
   }

}
