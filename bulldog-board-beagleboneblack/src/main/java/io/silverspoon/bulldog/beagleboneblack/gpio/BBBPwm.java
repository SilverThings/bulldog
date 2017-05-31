package io.silverspoon.bulldog.beagleboneblack.gpio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import io.silverspoon.bulldog.beagleboneblack.devicetree.DeviceTreeCompiler;
import io.silverspoon.bulldog.beagleboneblack.sysfs.BBBSysFs;
import io.silverspoon.bulldog.beagleboneblack.sysfs.SysFsPwm;
import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.core.gpio.base.AbstractPwm;
import io.silverspoon.bulldog.core.util.BulldogUtil;

public class BBBPwm extends AbstractPwm {

   private static final String FILENAME_TEMPLATE = "bd_pwm_%s-%s";
   private static final String PWM_OCP_PATTERN = "pwm_test_%s";
   private static final String DEVICE_NAME_PATTERN = "bd_pwm_%s";

   private static final String VERSION = "00A0";

   private static final long NANOSECONDS_PER_SECOND = 1000000000;
   private static BBBPwmManager PWM_MANAGER = new BBBPwmManager();

   private int registerAddress;
   private int muxMode;
   private String pwmName;
   private String qualifier;
   private int channel;

   private long period;
   private long dutyPeriod;
   private long previousPeriod = 0;

   private BBBSysFs sysFsWrapper = new BBBSysFs();
   private SysFsPwm sysFsPwm;

   public BBBPwm(Pin pin, int registerAddress, int muxMode, String pwmName, String qualifier, int channel) {
      super(pin);
      this.registerAddress = registerAddress;
      this.muxMode = muxMode;
      this.pwmName = pwmName;
      this.qualifier = qualifier;
      this.channel = channel;
   }

   @Override
   protected void setupImpl() {
      if (!PWM_MANAGER.canActivatePwmOnPin(this)) {
         BBBPwm activePwm = PWM_MANAGER.getActivePin(this);
         throw new RuntimeException("You cannot activate " + activePwm.getName() + " on this pin. It is already active on: " + activePwm.getPin().getName());
      }

      sysFsWrapper.createSlotIfNotExists("am33xx_pwm");

      BBBPwm siblingPwm = PWM_MANAGER.getActiveSibling(this);
      if (siblingPwm != null) {
         setFrequency(siblingPwm.getFrequency());
      }
   }

   public void configureOverlay(long period, long duty) {
      try {
         String overlay = createOverlay(period, duty);
         installOverlay(overlay);

         sysFsWrapper.createSlotIfNotExists(getDeviceName());
         String deviceName = String.format(PWM_OCP_PATTERN, getPin().getName());
         File sysFsFile = sysFsWrapper.findOcpDevice(deviceName);
         sysFsPwm = new SysFsPwm(sysFsFile.getAbsolutePath(), sysFsWrapper.getSlotNumber(deviceName));
         sysFsPwm.setPeriod(period);
         sysFsPwm.setDuty(duty);

         PWM_MANAGER.addActivePwm(this);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private void installOverlay(String overlay) throws FileNotFoundException, IOException, InterruptedException {
      String deviceFileName = String.format(FILENAME_TEMPLATE, getPin().getName(), VERSION);
      DeviceTreeCompiler.compileOverlay(overlay, deviceFileName);
   }

   private String createOverlay(long period, long duty) throws IOException {
      InputStream stream = this.getClass().getResourceAsStream("/org/bulldog/beagleboneblack/devicetree/resources/pwm.dts.template");
      String overlay = BulldogUtil.convertStreamToString(stream);
      stream.close();

      overlay = overlay.replaceAll("\\$\\{version\\}", VERSION);
      overlay = overlay.replaceAll("\\$\\{pin_name\\}", getPin().getName());
      overlay = overlay.replaceAll("\\$\\{pin_address\\}", "0x" + Integer.toHexString(registerAddress));
      overlay = overlay.replaceAll("\\$\\{mux_mode\\}", "0x" + Integer.toHexString(muxMode));
      overlay = overlay.replaceAll("\\$\\{pwm_name\\}", pwmName.toLowerCase());
      overlay = overlay.replaceAll("\\$\\{qualifier\\}", qualifier);
      overlay = overlay.replaceAll("\\$\\{polarity\\}", "0");
      overlay = overlay.replaceAll("\\$\\{channel\\}", String.valueOf(channel));
      overlay = overlay.replaceAll("\\$\\{period\\}", String.valueOf(period));
      overlay = overlay.replaceAll("\\$\\{duty\\}", String.valueOf(duty));
      overlay = overlay.replaceAll("\\$\\{pin_name_dotted\\}", getPin().getPort() + "." + getPin().getIndexOnPort());
      return overlay;
   }

   @Override
   protected void setPwmImpl(double frequency, double duty) {
      period = (long) ((1.0 / frequency) * NANOSECONDS_PER_SECOND);
      dutyPeriod = (long) (period * duty);

      setFrequencyImpl(period);
      setDutyImpl(dutyPeriod);
   }

   private void setDutyImpl(long duty) {
      if (sysFsPwm == null) {
         return;
      }
      sysFsPwm.setDuty(duty);

   }

   private void setFrequencyImpl(long period) {
      if (previousPeriod == period) {
         return;
      }

      teardown();
      configureOverlay(period, dutyPeriod);
      configureSibling();

      if (isEnabled()) {
         sysFsPwm.enable();
      }

      previousPeriod = period;
   }

   /**
    * The Beaglebone can only have the same frequency on all
    * pwm groups. That means we need to tear down sibling pwms
    * in order to activate the new frequency for the group
    */
   private void configureSibling() {
      BBBPwm siblingPwm = PWM_MANAGER.getActiveSibling(this);
      if (siblingPwm != null && siblingPwm.isEnabled()) {
         siblingPwm.setPwmImpl(getFrequency(), siblingPwm.getDuty());
      }
   }

   @Override
   protected void teardownImpl() {
      sysFsWrapper.removeSlotOfDevice(getDeviceName());
      PWM_MANAGER.removeActivePwm(this);
      sysFsPwm = null;
   }

   @Override
   protected void enableImpl() {
      sysFsPwm.enable();
   }

   @Override
   protected void disableImpl() {
      if (sysFsPwm == null) {
         return;
      }
      sysFsPwm.disable();
      teardown();
   }

   public String getPwmGroup() {
      return this.pwmName;
   }

   public String getQualifier() {
      return this.qualifier;
   }

   private String getDeviceName() {
      return String.format(DEVICE_NAME_PATTERN, getPin().getName());
   }

   public long getPeriod() {
      return period;
   }

   public long getDutyPeriod() {
      return dutyPeriod;
   }
}
