package io.silverspoon.bulldog.core.gpio.base;

import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.core.gpio.PinFeature;

public abstract class AbstractPinFeature implements PinFeature {

   private Pin pin;
   private boolean isSetup = false;
   private boolean teardownOnShutdown = false;

   public AbstractPinFeature(Pin pin) {
      this.pin = pin;
   }

   public Pin getPin() {
      return pin;
   }

   public boolean isActivatedFeature() {
      return this.getPin().getActiveFeature() == this;
   }

   public void activate() {
      getPin().activateFeature(getClass());
   }

   public void blockPin() {
      pin.block(this);
   }

   public boolean isBlocking() {
      return pin.getBlocker() == this;
   }

   public void unblockPin() {
      getPin().unblock(this);
   }

   protected abstract void setupImpl();

   protected abstract void teardownImpl();

   public void setup() {
      setupImpl();
      isSetup = true;
   }

   public void teardown() {
      teardownImpl();
      isSetup = false;
   }

   public boolean isSetup() {
      return isSetup;
   }

   public boolean isTorndownOnShutdown() {
      return teardownOnShutdown;
   }

   public void setTeardownOnShutdown(boolean teardown) {
      this.teardownOnShutdown = teardown;
   }

   @Override
   public String toString() {
      String string = this.getName();
      if (string == null) {
         string = super.toString();
      }

      return string;
   }
}
