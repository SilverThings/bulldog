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
package io.silverspoon.bulldog.core.event;

/**
 * The listener interface for receiving threshold events.
 * The class that is interested in processing a threshold
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's {@code addThresholdListener} method. When
 * the threshold event occurs, that object's appropriate
 * method is invoked.
 */
public interface ThresholdListener {

   /**
    * Callback method that is triggered when the threshold is reached.
    */
   void thresholdReached();

   /**
    * Checks if the threshold is reached.
    *
    * @param thresholdValue
    *       the threshold value
    * @return true, if is threshold reached - false otherwise
    */
   boolean isThresholdReached(double thresholdValue);

}
