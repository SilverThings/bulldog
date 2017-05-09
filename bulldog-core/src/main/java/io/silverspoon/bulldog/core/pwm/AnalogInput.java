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
package io.silverspoon.bulldog.core.pwm;

import io.silverspoon.bulldog.core.event.ThresholdListener;
import io.silverspoon.bulldog.core.pin.PinFeature;

import java.util.concurrent.Future;

/**
 * This interface specified the operations that can be used on a Pin that is
 * configured as an analog input.
 */
public interface AnalogInput extends PinFeature {

   /**
    * Reads the voltage that is currently applied to the pin.
    *
    * @return A value ranging from 0.0 to 1.0. This is the percentage
    * of the voltage reference value that is currently applied on this pin.
    */
   double read();

   /**
    * Grabs a number of readings from the analog input in short succession.
    *
    * @param amountSamples
    *       the amount samples to be taken
    * @return an array of values ranging between 0.0 and 1.0.
    * These represent the percentage of the voltage reference
    * that were supplied to the pin when the measurement was
    * taken.
    */
   double[] sample(int amountSamples);

   /**
    * Grabs a number of readings from the analog input, sampled
    * with the specified frequency.
    *
    * @param amountSamples
    *       the amount samples to be taken
    * @param frequency
    *       the frequency that should be used for sampling
    * @return an array of values ranging between 0.0 and 1.0.
    * These represent the percentage of the voltage reference
    * that were supplied to the pin when the measurement was
    * taken.
    */
   double[] sample(int amountSamples, float frequency);

   /**
    * Grabs a number of readings asynchronously from the analog input
    * in short succession. The program will continue execution while
    * the samples are being taken.
    *
    * @param amountSamples
    *       the amount samples to be taken
    * @return A future containing an array of the sampled values.
    * These values range between 0.0 and 1.0, which represents
    * the percentage of the reference voltage that has been
    * measured.
    */
   Future<double[]> sampleAsync(int amountSamples);

   /**
    * Grabs a number of readings asynchronously from the analog input,
    * sampled with the specified frequency.
    * The program will continue execution while the samples are being
    * taken.
    *
    * @param amountSamples
    *       the amount samples to be taken
    * @param frequency
    *       the frequency that should be used for sampling
    * @return A future containing an array of the sampled values.
    * These values range between 0.0 and 1.0, which represents
    * the percentage of the reference voltage that has been
    * measured.
    */
   Future<double[]> sampleAsync(int amountSamples, float frequency);

   /**
    * Start a monitor on the analog input. It will check the
    * value on the analog input periodically, as specified
    * with the periodMicroSeconds argument. When the threshold
    * has been reached the listener's {@link ThresholdListener#thresholdReached thresholdReached}
    * method will be called.
    *
    * @param periodMicroSeconds
    *       the period micro seconds
    * @param listener
    *       the listener
    */
   void startMonitor(int periodMicroSeconds, ThresholdListener listener);

   /**
    * Stops the monitor that currently listens on the pin.
    */
   void stopMonitor();
}
