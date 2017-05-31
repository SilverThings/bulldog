package io.silverspoon.bulldog.core.gpio;

public interface PinFeature {

   String getName();

   Pin getPin();

   boolean isActivatedFeature();

   void activate();

   boolean isBlocking();

   void blockPin();

   void unblockPin();

   boolean isSetup();

   void setup();

   void teardown();

   boolean isTorndownOnShutdown();

   void setTeardownOnShutdown(boolean teardown);
}
