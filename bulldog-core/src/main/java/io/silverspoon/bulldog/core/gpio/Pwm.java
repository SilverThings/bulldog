package io.silverspoon.bulldog.core.gpio;

public interface Pwm extends PinFeature {

   void enable();

   void disable();

   boolean isEnabled();

   void setDuty(double duty);

   double getDuty();

   void setFrequency(double frequency);

   double getFrequency();
}