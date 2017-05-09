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
import io.silverspoon.bulldog.core.util.DaemonThreadFactory;
import io.silverspoon.bulldog.core.util.easing.SineEasing;
import io.silverspoon.bulldog.devices.actuators.Actuator;
import io.silverspoon.bulldog.devices.actuators.movement.DirectMove;
import io.silverspoon.bulldog.devices.actuators.movement.EasedMove;
import io.silverspoon.bulldog.devices.actuators.movement.LinearMove;
import io.silverspoon.bulldog.devices.actuators.movement.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Servo implements Actuator {

   private static final float MIN_ANGLE_DEFAULT = 0.05f;
   private static final float MAX_ANGLE_DEFAULT = 0.10f;
   private static final float FREQUENCY_HZ = 50.0f;
   private static final float INITIAL_POSITION_DEFAULT = 0.0f;
   private static final int TIME_PER_DEGREE_DEFAULT = (int) (0.1f / 60.0f * 1000);

   private Pwm pwm;
   private double angle;
   private double minAngleDuty;
   private double maxAngleDuty;
   private int degreeMilliseconds;

   private ExecutorService executor = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
   private Future<?> currentMove = null;

   private List<ServoListener> listeners = Collections.synchronizedList(new ArrayList<ServoListener>());

   public Servo(Pwm pwm) {
      this(pwm, INITIAL_POSITION_DEFAULT);
   }

   public Servo(Pwm pwm, float initialAngle) {
      this(pwm, initialAngle, MIN_ANGLE_DEFAULT, MAX_ANGLE_DEFAULT, TIME_PER_DEGREE_DEFAULT);
   }

   public Servo(Pwm pwm, double initialAngle, double minAngleDuty, double maxAngleDuty, int degreeMilliseconds) {
      this.pwm = pwm;
      this.minAngleDuty = minAngleDuty;
      this.maxAngleDuty = maxAngleDuty;
      this.degreeMilliseconds = degreeMilliseconds;
      this.pwm.setFrequency(FREQUENCY_HZ);
      this.pwm.setDuty(getDutyForAngle(initialAngle));
      if (!this.pwm.isEnabled()) {
         this.pwm.enable();
      }
   }

   public void setAngle(double degrees) {
      double oldAngle = angle;
      angle = degrees;
      if (angle < 0.0f) {
         angle = 0.0f;
      }

      if (angle > 180.0f) {
         angle = 180.0f;
      }

      pwm.setDuty(getDutyForAngle(angle));
      fireAngleChanged(oldAngle, angle);
   }

   public void moveTo(double angle) {
      move(new DirectMove(angle));
   }

   public void moveTo(double angle, int milliseconds) {
      move(new LinearMove(angle, milliseconds));
   }

   public void moveSmoothTo(double angle) {
      move(new EasedMove(new SineEasing(), angle));
   }

   public void moveSmoothTo(double angle, int milliseconds) {
      move(new EasedMove(new SineEasing(), angle, milliseconds));
   }

   public void move(Move move) {
      double startAngle = getAngle();
      move.execute(this);
      fireMoveCompleted(startAngle, getAngle());
   }

   public void moveAsync(final Move move) {
      currentMove = executor.submit(new Runnable() {

         @Override
         public void run() {
            move(move);
         }

      });
   }

   public void moveAsyncTo(double angle) {
      moveAsync(new DirectMove(angle));
   }

   public void moveAsyncTo(double angle, int milliseconds) {
      moveAsync(new LinearMove(angle, milliseconds));
   }

   public void moveSmoothAsyncTo(double angle) {
      moveAsync(new EasedMove(new SineEasing(), angle));
   }

   public void moveSmoothAsyncTo(double angle, int milliseconds) {
      moveAsync(new EasedMove(new SineEasing(), angle, milliseconds));
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

   public double getAngle() {
      return angle;
   }

   protected double getDutyForAngle(double angle) {
      double maxAngle = 180.0;
      double anglePercent = angle / maxAngle;
      double dutyLength = (maxAngleDuty - minAngleDuty) * anglePercent;
      return minAngleDuty + dutyLength;
   }

   public Pwm getPwm() {
      return this.pwm;
   }

   public void addServoListener(ServoListener listener) {
      listeners.add(listener);
   }

   public void removeServoListener(ServoListener listener) {
      listeners.remove(listener);
   }

   public void clearServoListeners() {
      listeners.clear();
   }

   protected void fireAngleChanged(double oldAngle, double newAngle) {
      synchronized (listeners) {
         for (ServoListener listener : listeners) {
            listener.angleChanged(this, oldAngle, newAngle);
         }
      }
   }

   protected void fireMoveCompleted(double oldAngle, double newAngle) {
      synchronized (listeners) {
         for (ServoListener listener : listeners) {
            listener.moveCompleted(this, oldAngle, newAngle);
         }
      }
   }

   public int getMillisecondsPerDegree() {
      return degreeMilliseconds;
   }

   @Override
   public double getPosition() {
      return getAngle();
   }

   public void setPosition(double position) {
      this.setAngle(position);
   }

   @Override
   public int getMillisecondsPerUnit() {
      return this.getMillisecondsPerDegree();
   }

   @Override
   public int getRefreshIntervalMilliseconds() {
      return (int) (1000 / this.getPwm().getFrequency());
   }

   @Override
   public boolean isMoving() {
      if (currentMove != null) {
         return !currentMove.isDone();
      }

      return false;
   }
}
