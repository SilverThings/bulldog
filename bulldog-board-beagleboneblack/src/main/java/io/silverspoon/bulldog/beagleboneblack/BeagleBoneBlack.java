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
package io.silverspoon.bulldog.beagleboneblack;

import io.silverspoon.bulldog.beagleboneblack.gpio.BBBDigitalInput;
import io.silverspoon.bulldog.beagleboneblack.gpio.BBBDigitalOutput;
import io.silverspoon.bulldog.beagleboneblack.gpio.BBBEmmc;
import io.silverspoon.bulldog.beagleboneblack.gpio.BBBHdmi;
import io.silverspoon.bulldog.beagleboneblack.io.BBBUartPort;
import io.silverspoon.bulldog.beagleboneblack.pwm.BBBAnalogInput;
import io.silverspoon.bulldog.beagleboneblack.pwm.BBBPwm;
import io.silverspoon.bulldog.beagleboneblack.sysfs.BBBSysFs;
import io.silverspoon.bulldog.core.event.FeatureActivationEventArgs;
import io.silverspoon.bulldog.core.event.FeatureActivationListener;
import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.AbstractBoard;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.linux.gpio.LinuxDigitalInput;
import io.silverspoon.bulldog.linux.gpio.LinuxDigitalOutput;
import io.silverspoon.bulldog.linux.io.LinuxI2cBus;
import io.silverspoon.bulldog.linux.io.LinuxSpiBus;

import java.io.File;

public class BeagleBoneBlack extends AbstractBoard implements FeatureActivationListener {

   private static final String NAME = "BeagleBone Black";
   private BBBSysFs sysFs = new BBBSysFs();

   BeagleBoneBlack() {
      super();
      createPins();
      createBuses();
      createProperties();
   }

   private void createPins() {
      getPins().add(createNativeDigitalIOPin("P8_3", "GPIO1_6", 1, 6, "P8", 3));
      getPins().add(createNativeDigitalIOPin("P8_4", "GPIO1_7", 1, 7, "P8", 4));
      getPins().add(createNativeDigitalIOPin("P8_5", "GPIO1_2", 1, 2, "P8", 5));
      getPins().add(createNativeDigitalIOPin("P8_6", "GPIO1_3", 1, 3, "P8", 6));
      getPins().add(createNativeDigitalIOPin("P8_7", "TIMER4", 2, 2, "P8", 7));
      getPins().add(createNativeDigitalIOPin("P8_8", "TIMER7", 2, 3, "P8", 8));
      getPins().add(createNativeDigitalIOPin("P8_9", "TIMER5", 2, 5, "P8", 9));
      getPins().add(createNativeDigitalIOPin("P8_10", "TIMER6", 2, 4, "P8", 10));
      getPins().add(createNativeDigitalIOPin("P8_11", "GPIO1_13", 1, 13, "P8", 11));
      getPins().add(createNativeDigitalIOPin("P8_12", "GPIO1_12", 1, 12, "P8", 12));
      getPins().add(createNativeDigitalIOPin("P8_13", "EHRPWM2B", 0, 23, "P8", 13));
      getPins().add(createNativeDigitalIOPin("P8_14", "GPIO0_26", 0, 26, "P8", 14));
      getPins().add(createNativeDigitalIOPin("P8_15", "GPIO1_15", 1, 15, "P8", 15));
      getPins().add(createNativeDigitalIOPin("P8_16", "GPIO1_14", 1, 14, "P8", 16));
      getPins().add(createNativeDigitalIOPin("P8_17", "GPIO0_27", 0, 27, "P8", 17));
      getPins().add(createNativeDigitalIOPin("P8_18", "GPIO2_1", 2, 1, "P8", 18));
      getPins().add(createNativeDigitalIOPin("P8_19", "EHRPWM2A", 0, 22, "P8", 19));
      getPins().add(createNativeDigitalIOPin("P8_20", "GPIO1_31", 1, 31, "P8", 20));
      getPins().add(createNativeDigitalIOPin("P8_21", "GPIO1_30", 1, 30, "P8", 21));
      getPins().add(createNativeDigitalIOPin("P8_22", "GPIO1_5", 1, 5, "P8", 22));
      getPins().add(createNativeDigitalIOPin("P8_23", "GPIO1_4", 1, 4, "P8", 23));
      getPins().add(createNativeDigitalIOPin("P8_24", "GPIO1_1", 1, 1, "P8", 24));
      getPins().add(createNativeDigitalIOPin("P8_25", "GPIO1_0", 1, 0, "P8", 25));
      getPins().add(createNativeDigitalIOPin("P8_26", "GPIO1_29", 1, 29, "P8", 26));
      getPins().add(createNativeDigitalIOPin("P8_27", "GPIO2_22", 2, 22, "P8", 27));
      getPins().add(createNativeDigitalIOPin("P8_28", "GPIO2_24", 2, 24, "P8", 28));
      getPins().add(createNativeDigitalIOPin("P8_29", "GPIO2_23", 2, 23, "P8", 29));
      getPins().add(createNativeDigitalIOPin("P8_30", "GPIO2_25", 2, 25, "P8", 30));
      getPins().add(createNativeDigitalIOPin("P8_31", "GPIO0_10", 0, 10, "P8", 31));
      getPins().add(createNativeDigitalIOPin("P8_32", "GPIO0_11", 0, 11, "P8", 32));
      getPins().add(createNativeDigitalIOPin("P8_33", "GPIO0_9", 0, 9, "P8", 33));
      getPins().add(createNativeDigitalIOPin("P8_34", "GPIO2_17", 2, 17, "P8", 34));
      getPins().add(createNativeDigitalIOPin("P8_35", "GPIO0_8", 0, 8, "P8", 35));
      getPins().add(createNativeDigitalIOPin("P8_36", "GPIO2_16", 2, 16, "P8", 36));
      getPins().add(createNativeDigitalIOPin("P8_37", "GPIO2_14", 2, 14, "P8", 37));
      getPins().add(createNativeDigitalIOPin("P8_38", "GPIO2_1", 2, 1, "P8", 38));
      getPins().add(createNativeDigitalIOPin("P8_39", "GPIO2_12", 2, 12, "P8", 39));
      getPins().add(createNativeDigitalIOPin("P8_40", "GPIO2_13", 2, 14, "P8", 40));
      getPins().add(createNativeDigitalIOPin("P8_41", "GPIO2_10", 2, 10, "P8", 41));
      getPins().add(createNativeDigitalIOPin("P8_42", "GPIO2_11", 2, 11, "P8", 42));
      getPins().add(createNativeDigitalIOPin("P8_43", "GPIO2_8", 2, 8, "P8", 43));
      getPins().add(createNativeDigitalIOPin("P8_44", "GPIO2_9", 2, 9, "P8", 44));
      getPins().add(createNativeDigitalIOPin("P8_45", "GPIO2_6", 2, 6, "P8", 45));
      getPins().add(createNativeDigitalIOPin("P8_46", "GPIO2_27", 2, 27, "P8", 46));

      getPins().add(createNativeDigitalIOPin("P9_11", "UART4_RX", 0, 30, "P9", 11));
      getPins().add(createNativeDigitalIOPin("P9_12", "GPIO1_28", 1, 28, "P9", 12));
      getPins().add(createNativeDigitalIOPin("P9_13", "UART4_TX", 0, 31, "P9", 13));
      getPins().add(createNativeDigitalIOPin("P9_14", "EHRPWM1A", 1, 18, "P9", 14));
      getPins().add(createNativeDigitalIOPin("P9_15", "GPIO1_16", 1, 16, "P9", 15));
      getPins().add(createNativeDigitalIOPin("P9_16", "EHRPWM1B", 1, 19, "P9", 16));
      getPins().add(createNativeDigitalIOPin("P9_17", "GPIO0_5", 0, 5, "P9", 17));
      getPins().add(createNativeDigitalIOPin("P9_18", "GPIO0_4", 0, 4, "P9", 18));
      getPins().add(createNativeDigitalIOPin("P9_19", "GPIO0_15", 0, 15, "P9", 19));
      getPins().add(createNativeDigitalIOPin("P9_20", "GPIO0_14", 0, 14, "P9", 20));
      getPins().add(createNativeDigitalIOPin("P9_21", "UART2TX", 0, 3, "P9", 21));
      getPins().add(createNativeDigitalIOPin("P9_22", "UART2RX", 0, 2, "P9", 22));
      getPins().add(createNativeDigitalIOPin("P9_23", "GPIO1_17", 1, 17, "P9", 23));
      getPins().add(createNativeDigitalIOPin("P9_24", "UART1TX", 0, 12, "P9", 24));
      getPins().add(createNativeDigitalIOPin("P9_25", "GPIO3_21", 3, 21, "P9", 25));
      getPins().add(createNativeDigitalIOPin("P9_26", "UART1RX", 0, 13, "P9", 26));
      getPins().add(createSysFsDigitalIOPin("P9_27", "GPIO3_19", 3, 19, "P9", 27));
      getPins().add(createNativeDigitalIOPin("P9_28", "GPIO3_17", 3, 17, "P9", 28));
      getPins().add(createNativeDigitalIOPin("P9_29", "GPIO3_15", 3, 15, "P9", 29));
      getPins().add(createSysFsDigitalIOPin("P9_30", "GPIO3_16", 3, 16, "P9", 30));
      getPins().add(createNativeDigitalIOPin("P9_31", "EHRPWM0A", 3, 14, "P9", 31));

      getPins().add(createAnalogInputPin("P9_33", "AIN4", 2, 7, "P9", 33, 4));
      getPins().add(createAnalogInputPin("P9_35", "AIN6", 2, 9, "P9", 35, 6));
      getPins().add(createAnalogInputPin("P9_36", "AIN5", 2, 8, "P9", 36, 5));
      getPins().add(createAnalogInputPin("P9_37", "AIN2", 2, 5, "P9", 37, 2));
      getPins().add(createAnalogInputPin("P9_38", "AIN3", 2, 6, "P9", 38, 3));
      getPins().add(createAnalogInputPin("P9_39", "AIN0", 2, 3, "P9", 39, 0));
      getPins().add(createAnalogInputPin("P9_40", "AIN1", 2, 4, "P9", 40, 1));

      getPins().add(createNativeDigitalIOPin("P9_41", "GPIO0_20", 0, 20, "P9", 41));
      getPins().add(createNativeDigitalIOPin("P9_42", "GPIO0_7", 0, 7, "P9", 42));

      getPins().add(new BeagleBonePin("J1_1", "UART01", 0, 0, "J1", 1));
      getPins().add(new BeagleBonePin("J1_2", "UART01", 0, 0, "J1", 2));
      getPins().add(new BeagleBonePin("J1_3", "UART01", 0, 0, "J1", 3));
      getPins().add(new BeagleBonePin("J1_4", "UART01", 0, 0, "J1", 4));
      getPins().add(new BeagleBonePin("J1_5", "UART01", 0, 0, "J1", 5));
      getPins().add(new BeagleBonePin("J1_6", "UART01", 0, 0, "J1", 6));

      addPwmToPin(getPin(BBBNames.EHRPWM0A_P9_21), 0x154, 0x3, BBBNames.EHRPWM0, "B", 1);
      addPwmToPin(getPin(BBBNames.EHRPWM0A_P9_31), 0x190, 0x1, BBBNames.EHRPWM0, "A", 0);
      addPwmToPin(getPin(BBBNames.EHRPWM0B_P9_22), 0x150, 0x3, BBBNames.EHRPWM0, "A", 0);
      addPwmToPin(getPin(BBBNames.EHRPWM0B_P9_29), 0x194, 0x1, BBBNames.EHRPWM0, "B", 1);
      addPwmToPin(getPin(BBBNames.EHRPWM1A_P8_36), 0x0C8, 0x2, BBBNames.EHRPWM1, "A", 0);
      addPwmToPin(getPin(BBBNames.EHRPWM1A_P9_14), 0x048, 0x6, BBBNames.EHRPWM1, "A", 0);
      addPwmToPin(getPin(BBBNames.EHRPWM1B_P8_34), 0x0CC, 0x2, BBBNames.EHRPWM1, "B", 1);
      addPwmToPin(getPin(BBBNames.EHRPWM1B_P9_16), 0x04C, 0x6, BBBNames.EHRPWM1, "B", 1);
      addPwmToPin(getPin(BBBNames.EHRPWM2A_P8_19), 0x020, 0x4, BBBNames.EHRPWM2, "A", 0);
      addPwmToPin(getPin(BBBNames.EHRPWM2A_P8_45), 0x0A0, 0x3, BBBNames.EHRPWM2, "A", 0);
      addPwmToPin(getPin(BBBNames.EHRPWM2B_P8_13), 0x024, 0x4, BBBNames.EHRPWM2, "B", 1);
      addPwmToPin(getPin(BBBNames.EHRPWM2B_P8_46), 0x0a4, 0x3, BBBNames.EHRPWM2, "B", 1);
      addPwmToPin(getPin(BBBNames.ECAPPWM0_P9_42), 0x164, 0x0, BBBNames.ECAPPWM0, "0", 0);
      addPwmToPin(getPin(BBBNames.ECAPPWM2_P9_28), 0x19C, 0x4, BBBNames.ECAPPWM2, "2", 0);

      if (isHdmiEnabled()) {
         blockHdmiPins();
      }

      if (isEmmcEnabled()) {
         blockEmmcPins();
      }
   }

   private Pin createSysFsDigitalIOPin(String name, String internalName, int bank, int pinIndex, String port, int portIndex) {
      BeagleBonePin pin = new BeagleBonePin(name, internalName, bank, pinIndex, port, portIndex);
      pin.addFeature(new DigitalIOFeature(pin, new LinuxDigitalInput(pin), new LinuxDigitalOutput(pin)));
      return pin;
   }

   private Pin createNativeDigitalIOPin(String name, String internalName, int bank, int pinIndex, String port, int portIndex) {
      BeagleBonePin pin = new BeagleBonePin(name, internalName, bank, pinIndex, port, portIndex);
      pin.addFeature(new DigitalIOFeature(pin, new BBBDigitalInput(pin), new BBBDigitalOutput(pin)));
      return pin;
   }

   private void addPwmToPin(Pin pin, int registerAddress, int muxMode, String pwmGroup, String qualifier, int channel) {
      pin.addFeature(new BBBPwm(pin, registerAddress, muxMode, pwmGroup, qualifier, channel));
      pin.addFeatureActivationListener(this);
   }

   private Pin createAnalogInputPin(String name, String internalName, int bank, int pinIndex, String port, int portIndex, int channelId) {
      BeagleBonePin pin = new BeagleBonePin(name, internalName, bank, pinIndex, port, portIndex);
      pin.addFeature(new BBBAnalogInput(pin, channelId));
      return pin;
   }

   private void blockWithHdmiFeature(Pin pin) {
      pin.addFeature(new BBBHdmi(pin));
      pin.activateFeature(BBBHdmi.class);
   }

   private void blockHdmiPins() {
      blockWithHdmiFeature(getPin(BBBNames.P8_27));
      blockWithHdmiFeature(getPin(BBBNames.P8_28));
      blockWithHdmiFeature(getPin(BBBNames.P8_29));
      blockWithHdmiFeature(getPin(BBBNames.P8_30));
      blockWithHdmiFeature(getPin(BBBNames.P8_31));
      blockWithHdmiFeature(getPin(BBBNames.P8_32));
      blockWithHdmiFeature(getPin(BBBNames.P8_33));
      blockWithHdmiFeature(getPin(BBBNames.P8_34));
      blockWithHdmiFeature(getPin(BBBNames.P8_35));
      blockWithHdmiFeature(getPin(BBBNames.P8_36));
      blockWithHdmiFeature(getPin(BBBNames.P8_37));
      blockWithHdmiFeature(getPin(BBBNames.P8_38));
      blockWithHdmiFeature(getPin(BBBNames.P8_39));
      blockWithHdmiFeature(getPin(BBBNames.P8_40));
      blockWithHdmiFeature(getPin(BBBNames.P8_41));
      blockWithHdmiFeature(getPin(BBBNames.P8_42));
      blockWithHdmiFeature(getPin(BBBNames.P8_43));
      blockWithHdmiFeature(getPin(BBBNames.P8_44));
      blockWithHdmiFeature(getPin(BBBNames.P8_45));
      blockWithHdmiFeature(getPin(BBBNames.P8_46));

      blockWithHdmiFeature(getPin(BBBNames.P9_25));
      blockWithHdmiFeature(getPin(BBBNames.P9_28));
      blockWithHdmiFeature(getPin(BBBNames.P9_29));
      blockWithHdmiFeature(getPin(BBBNames.P9_31));
   }

   private void blockWithEmmcFeature(Pin pin) {
      pin.addFeature(new BBBEmmc(pin));
      pin.activateFeature(BBBEmmc.class);
   }

   private void blockEmmcPins() {
      blockWithEmmcFeature(getPin(BBBNames.P8_3));
      blockWithEmmcFeature(getPin(BBBNames.P8_4));
      blockWithEmmcFeature(getPin(BBBNames.P8_5));
      blockWithEmmcFeature(getPin(BBBNames.P8_6));
      blockWithEmmcFeature(getPin(BBBNames.P8_20));
      blockWithEmmcFeature(getPin(BBBNames.P8_21));
      blockWithEmmcFeature(getPin(BBBNames.P8_22));
      blockWithEmmcFeature(getPin(BBBNames.P8_23));
      blockWithEmmcFeature(getPin(BBBNames.P8_24));
      blockWithEmmcFeature(getPin(BBBNames.P8_25));
   }

   private void createBuses() {
      createI2cBuses();
      createSerialPorts();
      createSpiBuses();
   }

   private void createI2cBuses() {
      //getI2cBuses().add(new LinuxI2cBus(BBBNames.I2C_0, "/dev/i2c-0"));
      getI2cBuses().add(new LinuxI2cBus(BBBNames.I2C_1, "/dev/i2c-2"));
   }

   private void createSpiBuses() {
      getSpiBuses().add(new LinuxSpiBus(BBBNames.SPI_0_CS0, "/dev/spidev0.0", this));
      getSpiBuses().add(new LinuxSpiBus(BBBNames.SPI_0_CS1, "/dev/spidev0.1", this));
      getSpiBuses().add(new LinuxSpiBus(BBBNames.SPI_1_CS0, "/dev/spidev1.0", this));
      getSpiBuses().add(new LinuxSpiBus(BBBNames.SPI_1_CS1, "/dev/spidev1.1", this));
   }

   private BBBUartPort createSerialPort(String name, String path, String slotName, Pin rx, Pin tx) {
      BBBUartPort uart = new BBBUartPort(name, path, slotName, rx, tx);
      if (uart.isSlotLoaded()) {
         uart.setup();
      }
      return uart;
   }

   private void createSerialPorts() {
      getSerialPorts().add(createSerialPort(BBBNames.UART0, "/dev/ttyO0", "BB-UART0", null, null));
      getSerialPorts().add(createSerialPort(BBBNames.UART1, "/dev/ttyO1", "BB-UART1", getPin(BBBNames.P9_26), getPin(BBBNames.P9_24)));
      getSerialPorts().add(createSerialPort(BBBNames.UART2, "/dev/ttyO2", "BB-UART2", getPin(BBBNames.P9_21), getPin(BBBNames.P9_22)));
      getSerialPorts().add(createSerialPort(BBBNames.UART3, "/dev/ttyO3", "BB-UART3", null, getPin(BBBNames.P9_42)));
      getSerialPorts().add(createSerialPort(BBBNames.UART4, "/dev/ttyO4", "BB-UART4", getPin(BBBNames.P9_11), getPin(BBBNames.P9_13)));
      getSerialPorts().add(createSerialPort(BBBNames.UART5, "/dev/ttyO5", "BB-UART5", getPin(BBBNames.P8_38), getPin(BBBNames.P8_37)));
   }

   private void createProperties() {
      if (isHdmiEnabled()) {
         setProperty(BBBProperties.HDMI_ENABLED, Boolean.TRUE.toString());
      }

      if (isEmmcEnabled()) {
         setProperty(BBBProperties.EMMC_ENABLED, Boolean.TRUE.toString());
      }

      if (new File("/etc/dogtag").exists()) {
         setProperty(BBBProperties.DOGTAG, BulldogUtil.readFileAsString("/etc/dogtag"));
      }

      getProperties().list(System.out);
      System.out.flush();
   }

   @Override
   public String getName() {
      return NAME;
   }

   public void featureActivating(Object o, FeatureActivationEventArgs args) {
   }

   public void featureActivated(Object o, FeatureActivationEventArgs args) {
   }

   public void featureDeactivating(Object o, FeatureActivationEventArgs args) {
   }

   public void featureDeactivated(Object o, FeatureActivationEventArgs args) {
   }

   public void loadSlot(String deviceId) {
      sysFs.createSlotIfNotExists(deviceId);
   }

   public void removeSlot(String deviceId) {
      sysFs.removeSlotOfDevice(deviceId);
   }

   public boolean isHdmiEnabled() {
      int slot = sysFs.getSlotNumber("BB-BONELT-HDMI");
      return sysFs.isSlotLoaded(slot);
   }

   public boolean isEmmcEnabled() {
      int slot = sysFs.getSlotNumber("BB-BONE-EMMC-2G");
      return sysFs.isSlotLoaded(slot);
   }
}
