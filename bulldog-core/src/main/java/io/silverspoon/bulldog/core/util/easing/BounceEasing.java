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
package io.silverspoon.bulldog.core.util.easing;

public class BounceEasing implements Easing {

   public float easeIn(float t, float d) {
      return 1.0f - easeOut(d - t, d);
   }

   public float easeOut(float t, float d) {
      if ((t /= d) < (1 / 2.75f)) {
         return 1.0f * (7.5625f * t * t);
      } else if (t < (2 / 2.75f)) {
         return 1.0f * (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f);
      } else if (t < (2.5 / 2.75)) {
         return 1.0f * (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f);
      } else {
         return 1.0f * (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f);
      }
   }

   public float easeInOut(float t, float d) {
      if (t < d / 2) {
         return easeIn(t * 2, d) * 0.5f;
      } else {
         return easeOut(t * 2 - d, d) * .5f + 0.5f;
      }
   }

}
