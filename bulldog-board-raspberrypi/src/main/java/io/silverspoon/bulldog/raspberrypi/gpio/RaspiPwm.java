package io.silverspoon.bulldog.raspberrypi.gpio;

import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.core.gpio.base.AbstractPwm;
import io.silverspoon.bulldog.core.util.BitMagic;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.raspberrypi.bcm.AbstractBCM;
import io.silverspoon.bulldog.raspberrypi.bcm.BCMFactory;
import io.silverspoon.bulldog.raspberrypi.RaspberryPiPin;

public class RaspiPwm extends AbstractPwm {

   private double previousFrequency = 0.0;
   
   public static final AbstractBCM BCM = BCMFactory.getBCM();

   public RaspiPwm(Pin pin) {
      super(pin);
   }

   @Override
   protected void setupImpl() {
      RaspberryPiPin pin = (RaspberryPiPin) getPin();
      BCM.configureAlternateFunction(pin.getGpioNumber(), 5);
      stopClock();
      int value = BCM.getPwmMemory().getIntValueAt(BCM.getPWMCtl());
      value = BitMagic.setBit(value, 5, 0);
      value = BitMagic.setBit(value, 7, 1);
      BCM.getPwmMemory().setIntValue(BCM.getPWMCtl(), value);
      value = BCM.getPwmMemory().getIntValueAt(BCM.getPWMCtl());
   }

   @Override
   protected void teardownImpl() {
      disableImpl();
      stopClock();
   }

   @Override
   protected void setPwmImpl(double frequency, double duty) {
      if (previousFrequency != frequency) {
         setFrequencyImpl(frequency);
         previousFrequency = frequency;
      }

      setDutyImpl(duty);
   }

   private void setFrequencyImpl(double frequency) {
      if (isEnabled()) {
         disableImpl();
      }

      int divisorRegister = PwmFrequencyCalculator.calculateDivisorRegister(frequency);
      BCM.getClockMemory().setIntValue(BCM.getPWMClkDiv(), divisorRegister);
      BCM.getPwmMemory().setIntValue(BCM.getPWMRng1(), 0x100000);
      BulldogUtil.sleepMs(1);
      startClock();

      if (isEnabled()) {
         enableImpl();
      }
   }

   protected void setDutyImpl(double duty) {
      int myDuty = (int) (0x100000 * duty);
      BCM.getPwmMemory().setIntValue(BCM.getPWMDat1(), myDuty);
   }

   private void startClock() {
      BCM.getClockMemory().setIntValue(BCM.getPWMClkCntl(), 0x5A000011);
   }

   private void stopClock() {
      BCM.getClockMemory().setIntValue(BCM.getPWMClkCntl(), 0x5A000000 | (1 << 5));
      BulldogUtil.sleepMs(1);
   }

   @Override
   protected void enableImpl() {
      int value = BCM.getPwmMemory().getIntValueAt(BCM.getPWMCtl());
      BCM.getPwmMemory().setIntValue(BCM.getPWMCtl(), BitMagic.setBit(value, 0, 1));
      BulldogUtil.sleepMs(1);
   }

   @Override
   protected void disableImpl() {
      int value = BCM.getPwmMemory().getIntValueAt(BCM.getPWMCtl());
      BCM.getPwmMemory().setIntValue(BCM.getPWMCtl(), BitMagic.setBit(value, 0, 0));
      BulldogUtil.sleepMs(1);
   }

}
