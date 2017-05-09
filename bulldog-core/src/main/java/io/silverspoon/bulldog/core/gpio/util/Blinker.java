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
package io.silverspoon.bulldog.core.gpio.util;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.core.util.DaemonThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Blinker implements Runnable {

   private ScheduledExecutorService executorService;
   private ScheduledFuture<?> future;
   private DigitalOutput output;
   private int durationMilliseconds = 0;
   private long startTime = 0;
   private int times = 0;
   private boolean doTimes = false;

   public Blinker(DigitalOutput output) {
      executorService = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());
      this.output = output;
   }

   public void startBlinking(int periodLengthMilliseconds) {
      startBlinking(periodLengthMilliseconds, 0);
   }

   public void blinkTimes(int periodLengthMilliseconds, int times) {
      if (times <= 0) {
         throw new IllegalArgumentException("You must specify to blink at least one time");
      }
      this.times = times * 2;
      doTimes = true;
      startBlinking(periodLengthMilliseconds);
   }

   public void startBlinking(int periodLengthMilliseconds, int durationMilliseconds) {
      if (future != null) {
         future.cancel(true);
      }

      this.durationMilliseconds = durationMilliseconds;
      startTime = System.currentTimeMillis();
      output.blockPin();
      future = executorService.scheduleAtFixedRate(this, 0,
            periodLengthMilliseconds / 2, TimeUnit.MILLISECONDS);
   }

   public void stopBlinking() {
      if (future == null) {
         return;
      }
      future.cancel(true);
      future = null;
      doTimes = false;
      output.unblockPin();
   }

   @Override
   public void run() {
      if (!doTimes) {
         if (durationMilliseconds > 0) {
            long delta = System.currentTimeMillis() - startTime;
            if (delta >= durationMilliseconds) {
               stopBlinking();
            }
         }
      } else {
         times = times - 1;
         if (times == 0) {
            stopBlinking();
            doTimes = false;
         }
      }

      output.toggle();
   }

   public boolean isBlinking() {
      if (this.future != null) {
         return !future.isDone();
      }

      return false;
   }

   public void awaitBlinkingStopped() {
      while (isBlinking()) {
         BulldogUtil.sleepMs(1);
      }
   }
}
