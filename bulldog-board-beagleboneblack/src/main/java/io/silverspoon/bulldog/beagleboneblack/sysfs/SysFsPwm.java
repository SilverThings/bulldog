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
package io.silverspoon.bulldog.beagleboneblack.sysfs;

import io.silverspoon.bulldog.core.Polarity;

public class SysFsPwm {

   private BBBSysFs sysfsUtil = new BBBSysFs();
   private String directory = null;
   private int slot = -1;

   public SysFsPwm(String path, int slot) {
      this.directory = path;
      this.slot = slot;
   }

   public void setPeriod(long period) {
      sysfsUtil.echo(directory + "/period", String.valueOf(period));
   }

   public void setDuty(long duty) {
      sysfsUtil.echo(directory + "/duty", String.valueOf(duty));
   }

   public void setPolarity(Polarity polarity) {
      sysfsUtil.echo(directory + "/polarity", polarity == Polarity.Negative ? "0" : "1");
   }

   public void enable() {
      sysfsUtil.echo(directory + "/run", "1");
   }

   public void disable() {
      sysfsUtil.echo(directory + "/run", "0");
   }

   public int getSlot() {
      return slot;
   }

   public String getDirectory() {
      return directory;
   }

}
