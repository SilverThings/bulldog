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

public class ElasticEasing implements Easing {

   public float easeIn(float t, float d) {
      if (t == 0) {
         return 0.0f;
      }
      if ((t /= d) == 1) {
         return 1.0f;
      }
      float p = d * .3f;
      float a = 1.0f;
      float s = p / 4;
      return -(a * (float) Math.pow(2, 10 * (t -= 1)) * (float) Math.sin((t
            * d - s)
            * (2 * (float) Math.PI) / p));
   }

   public float easeIn(float t, float d, float a, float p) {
      float s;

      if (t == 0) {
         return 0.0f;
      }
      if ((t /= d) == 1) {
         return 1.0f;
      }

      if (a < Math.abs(1.0f)) {
         a = 1.0f;
         s = p / 4;
      } else {
         s = p / (2 * (float) Math.PI) * (float) Math.asin(1.0f / a);
      }

      return -(a * (float) Math.pow(2, 10 * (t -= 1)) * (float) Math.sin((t
            * d - s)
            * (2 * Math.PI) / p));
   }

   public float easeOut(float t, float d) {
      if (t == 0) {
         return 0.0f;
      }
      if ((t /= d) == 1) {
         return 1.0f;
      }

      float p = d * .3f;
      float a = 1.0f;
      float s = p / 4;
      return (a * (float) Math.pow(2, -10 * t)
            * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p) + 1.0f);
   }

   public float easeOut(float t, float d, float a, float p) {
      float s;
      if (t == 0) {
         return 0.0f;
      }
      if ((t /= d) == 1) {
         return 1.0f;
      }
      if (a < Math.abs(1.0f)) {
         a = 1.0f;
         s = p / 4;
      } else {
         s = p / (2 * (float) Math.PI) * (float) Math.asin(1.0f / a);
      }
      return (a * (float) Math.pow(2, -10 * t)
            * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p) + 1.0f);
   }

   public float easeInOut(float t, float d) {
      if (t == 0) {
         return 0.0f;
      }
      if ((t /= d / 2) == 2) {
         return 1.0f;
      }

      float p = d * (.3f * 1.5f);
      float a = 1.0f;
      float s = p / 4;

      if (t < 1) {
         return -.5f
               * (a * (float) Math.pow(2, 10 * (t -= 1)) * (float) Math
               .sin((t * d - s) * (2 * (float) Math.PI) / p));
      }

      return a * (float) Math.pow(2, -10 * (t -= 1))
            * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p)
            * .5f + 1.0f;
   }

   public float easeInOut(float t, float d, float a, float p) {
      float s;
      if (t == 0) {
         return 0.0f;
      }
      if ((t /= d / 2) == 2) {
         return 1.0f;
      }

      if (a < Math.abs(1.0f)) {
         a = 1.0f;
         s = p / 4;
      } else {
         s = p / (2 * (float) Math.PI) * (float) Math.asin(1.0f / a);
      }

      if (t < 1) {
         return -.5f
               * (a * (float) Math.pow(2, 10 * (t -= 1)) * (float) Math
               .sin((t * d - s) * (2 * (float) Math.PI) / p));
      }

      return a * (float) Math.pow(2, -10 * (t -= 1))
            * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p)
            * .5f + 1.0f;
   }

}
