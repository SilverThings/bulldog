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
package io.silverspoon.bulldog.beagleboneblack.io;

import io.silverspoon.bulldog.beagleboneblack.sysfs.BBBSysFs;
import io.silverspoon.bulldog.core.io.uart.UartPort;
import io.silverspoon.bulldog.core.io.uart.UartRx;
import io.silverspoon.bulldog.core.io.uart.UartSignalType;
import io.silverspoon.bulldog.core.io.uart.UartTx;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.linux.io.LinuxSerialPort;

import java.io.IOException;

public class BBBUartPort extends LinuxSerialPort implements UartPort {

   private Pin rxPin;
   private Pin txPin;
   private String slotName;
   private String name;
   private BBBSysFs sysFs = new BBBSysFs();
   private boolean setupInProgress = false;

   public BBBUartPort(String name, String filename, String slotName, Pin rx, Pin tx) {
      super(filename);
      this.slotName = slotName;
      this.rxPin = rx;
      this.txPin = tx;
      this.slotName = slotName;
      this.name = name;
      addRxFeatureToPin(rx);
      addTxFeatureToPin(tx);
   }

   @Override
   public void open() throws IOException {
      setup();
      super.open();
   }

   @Override
   public void close() throws IOException {
      super.close();
      teardown();
   }

   public boolean isSlotLoaded() {
      return sysFs.getSlotNumber(getSlotName()) >= 0;
   }

   public void setup() {
      if (setupInProgress) {
         return;
      }
      setupInProgress = true;
      sysFs.createSlotIfNotExists(getSlotName());
      if (getRx() != null) {
         getRx().activateFeature(UartTx.class);
      }
      if (getTx() != null) {
         getTx().activateFeature(UartRx.class);
      }
      setupInProgress = false;
   }

   public void teardown() {
      System.out.println("Cannot remove UART '" + getName() + "' from device tree;\nIt will crash the bone.");
   }

   private void addRxFeatureToPin(Pin pin) {
      if (pin == null) {
         return;
      }
      pin.addFeature(new BBBUartPinFeature(this, pin, UartSignalType.RX));
   }

   private void addTxFeatureToPin(Pin pin) {
      if (pin == null) {
         return;
      }
      pin.addFeature(new BBBUartPinFeature(this, pin, UartSignalType.TX));
   }

   @Override
   public Pin getRx() {
      return rxPin;
   }

   @Override
   public Pin getTx() {
      return txPin;
   }

   @Override
   public String getName() {
      return name;
   }

   public String getSlotName() {
      return slotName;
   }

}
