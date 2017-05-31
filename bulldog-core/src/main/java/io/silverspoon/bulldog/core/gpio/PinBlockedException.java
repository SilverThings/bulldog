/*
 * (C) Copyright 2014 libbulldog (http://libbulldog.org/) and others.
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
 *
 */
package io.silverspoon.bulldog.core.gpio;

// TODO: Auto-generated Javadoc

/**
 * The Class PinBlockedException.
 */
public class PinBlockedException extends RuntimeException {

   /**
    * The Constant serialVersionUID.
    */
   private static final long serialVersionUID = 6737984685844582750L;

   /**
    * The blocker.
    */
   private PinFeature blocker;

   /**
    * Instantiates a new pin blocked exception.
    *
    * @param blocker
    *       the blocker
    */
   public PinBlockedException(PinFeature blocker) {
      super(String.format("Pin %s is currently blocked by %s", blocker.getPin().getName(), blocker.getName()));
      this.blocker = blocker;
   }

   /**
    * Gets the blocker.
    *
    * @return the blocker
    */
   public PinFeature getBlocker() {
      return blocker;
   }

}
