package io.silverspoon.bulldog.core.gpio.base;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.silverspoon.bulldog.core.gpio.AnalogInput;
import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.core.gpio.event.ThresholdListener;

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
