package io.silverspoon.bulldog.core.gpio.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.core.gpio.base.AbstractPwm;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.core.util.DaemonThreadFactory;

public class SoftPwm extends AbstractPwm implements Runnable {

   private static final long NANOSECONDS_PER_SECOND = 1000000000;

   private ScheduledExecutorService executorService;
   private DigitalOutput output;

   private int dutyInNanoseconds;
   private int periodInNanoseconds;
   private ScheduledFuture<?> future;

   public SoftPwm(Pin pin) {
      super(pin);
      if (!pin.hasFeature(DigitalOutput.class)) {
         throw new IllegalArgumentException("The pin must be able to act as a DigitalOutput");
      }

      this.output = pin.getFeature(DigitalOutput.class);
      pin.getFeatures().add(this);
   }

   public SoftPwm(DigitalOutput output) {
      this(output.getPin());
   }

   private void createScheduler() {
      if (executorService != null) {
         return;
      }
      executorService = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());
      future = executorService.scheduleAtFixedRate(this, 0, periodInNanoseconds, TimeUnit.NANOSECONDS);
   }

   private void terminateScheduler() {
      if (executorService == null) {
         return;
      }
      if (future != null) {
         future.cancel(true);
         future = null;
      }
      executorService.shutdownNow();
      try {
         executorService.awaitTermination(1000, TimeUnit.MICROSECONDS);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      executorService = null;
   }

   @Override
   public void run() {
      output.applySignal(Signal.High);
      BulldogUtil.sleepNs(dutyInNanoseconds);
      output.applySignal(Signal.Low);
   }

   @Override
   public void setupImpl() {
      output.setup();
   }

   @Override
   public void teardownImpl() {
      terminateScheduler();
      output.teardown();
   }

   @Override
   protected void setPwmImpl(double frequency, double duty) {
      periodInNanoseconds = (int) ((1.0 / frequency) * NANOSECONDS_PER_SECOND);
      dutyInNanoseconds = (int) (periodInNanoseconds * duty);
   }

   @Override
   protected void enableImpl() {
      if (!isActivatedFeature()) {
         activate();
      }

      blockPin();
      createScheduler();
   }

   @Override
   protected void disableImpl() {
      terminateScheduler();
      unblockPin();
   }

}
