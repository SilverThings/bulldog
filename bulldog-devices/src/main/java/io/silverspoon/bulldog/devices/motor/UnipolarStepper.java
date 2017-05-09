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
package io.silverspoon.bulldog.devices.motor;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.io.PinIOGroup;

public class UnipolarStepper extends AbstractStepper {

   private byte[] steps = new byte[] { 0b0101, 0b0110, 0b1010, 0b1001 };
   private double position;

   public UnipolarStepper(DigitalOutput out1, DigitalOutput out2, DigitalOutput out3, DigitalOutput out4) {

   }

   public UnipolarStepper(PinIOGroup group) {

   }

   @Override
   public void moveTo(double position) {
      // TODO Auto-generated method stub

   }

   @Override
   public void setPosition(double position) {
      // TODO Auto-generated method stub

   }

   public void step() {
   }

   @Override
   public double getPosition() {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public int getMillisecondsPerUnit() {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public int getRefreshIntervalMilliseconds() {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public void forward() {
      // TODO Auto-generated method stub

   }

   @Override
   public void backward() {
      // TODO Auto-generated method stub

   }

   @Override
   public void stop() {
      // TODO Auto-generated method stub

   }

   @Override
   public boolean isMoving() {
      // TODO Auto-generated method stub
      return false;
   }

}
