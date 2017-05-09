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
package io.silverspoon.bulldog.core.pin;

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
