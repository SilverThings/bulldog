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
package io.silverspoon.bulldog.core.pwm;

import io.silverspoon.bulldog.core.event.ThresholdListener;
import io.silverspoon.bulldog.core.pin.AbstractPinFeature;
import io.silverspoon.bulldog.core.pin.Pin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractAnalogInput extends AbstractPinFeature implements AnalogInput {

   private static final String NAME_FORMAT = "Analog Input on Pin %s";

   private ScheduledFuture<?> future;
   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

   public AbstractAnalogInput(Pin pin) {
      super(pin);
   }

   public String getName() {
      return String.format(NAME_FORMAT, getPin().getName());
   }

   public void startMonitor(int periodMicroSeconds, final ThresholdListener listener) {
      if (listener == null) {
         throw new IllegalArgumentException("listener cannot be null");
      }

      blockPin();
      future = scheduler.scheduleAtFixedRate(new Runnable() {
                                                public void run() {
                                                   double[] samples = sample(10);
                                                   for (int i = 0; i < samples.length; i++) {
                                                      if (listener.isThresholdReached(samples[i])) {
                                                         listener.thresholdReached();
                                                      }
                                                   }
                                                }
                                             },
            0,
            periodMicroSeconds,
            TimeUnit.MICROSECONDS);
   }

   public void stopMonitor() {
      if (future == null) {
         return;
      }
      future.cancel(true);
      unblockPin();
   }
}
