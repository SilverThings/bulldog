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
package io.silverspoon.bulldog.beagleboneblack.pwm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BBBPwmManager {

   private Set<BBBPwm> activePwms = new HashSet<BBBPwm>();

   public boolean canActivatePwmOnPin(BBBPwm pwm) {
      if (pwm == null) {
         throw new IllegalArgumentException("pwm must not be null!");
      }
      BBBPwm activePwm = getActivePin(pwm);
      return activePwm == null && !(pwm == activePwm);
   }

   public BBBPwm getActivePin(BBBPwm pwm) {
      List<BBBPwm> activeSiblings = findPwmByGroup(pwm.getPwmGroup());
      for (BBBPwm siblingPwm : activeSiblings) {
         if (siblingPwm.getQualifier().equals(pwm.getQualifier())) {
            return siblingPwm;
         }
      }

      return null;
   }

   public BBBPwm getActiveSibling(BBBPwm pwm) {
      List<BBBPwm> activeSiblings = findPwmByGroup(pwm.getPwmGroup());
      for (BBBPwm siblingPwm : activeSiblings) {
         if (!siblingPwm.getQualifier().equals(pwm.getQualifier())) {
            return siblingPwm;
         }
      }

      return null;
   }

   public void addActivePwm(BBBPwm pwm) {
      activePwms.add(pwm);
   }

   public void removeActivePwm(BBBPwm pwm) {
      activePwms.remove(pwm);
   }

   private List<BBBPwm> findPwmByGroup(String group) {
      List<BBBPwm> pwms = new ArrayList<BBBPwm>();
      for (BBBPwm pwm : activePwms) {
         if (pwm.getPwmGroup().equals(group)) {
            pwms.add(pwm);
         }
      }

      return pwms;
   }
}
