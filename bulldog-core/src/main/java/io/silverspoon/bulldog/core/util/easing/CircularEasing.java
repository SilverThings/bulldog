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

public class CircularEasing implements Easing {

   public float easeIn(float t, float d) {
      return -1.0f * ((float) Math.sqrt(1 - (t /= d) * t) - 1);
   }

   public float easeOut(float t, float d) {
      return (float) Math.sqrt(1 - (t = t / d - 1) * t);
   }

   public float easeInOut(float t, float d) {
      if ((t /= d / 2) < 1) {
         return -0.5f * ((float) Math.sqrt(1.0f - t * t) - 1);
      }

      return 0.5f * ((float) Math.sqrt(1 - (t -= 2) * t) + 1);
   }

}
