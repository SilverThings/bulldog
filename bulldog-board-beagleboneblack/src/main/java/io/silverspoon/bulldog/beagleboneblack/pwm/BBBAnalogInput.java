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

import io.silverspoon.bulldog.beagleboneblack.jni.NativeAdc;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.pwm.AbstractAnalogInput;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BBBAnalogInput extends AbstractAnalogInput {

   private IntBuffer buffer;
   private int channelId = -1;

   public BBBAnalogInput(Pin pin, int channel) {
      super(pin);
      this.channelId = channel;
   }

   protected void setupImpl() {
      if (buffer == null) {
         buffer = ByteBuffer.allocateDirect(4 * 100).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
      }

      NativeAdc.configureModule(NativeAdc.BBBIO_ADC_WORK_MODE_BUSY_POLLING, 1);
      NativeAdc.configureChannel(channelId, NativeAdc.BBBIO_ADC_STEP_MODE_SW_CONTINUOUS, 0, 1, 1, buffer, 100);
      NativeAdc.enableChannel(channelId);
   }

   protected void teardownImpl() {
      NativeAdc.disableChannel(channelId);
   }

   public double read() {
      return sample(1)[0];
   }

   public synchronized double[] sample(int amountSamples) {
      NativeAdc.configureModule(NativeAdc.BBBIO_ADC_WORK_MODE_BUSY_POLLING, 1);
      if (buffer.limit() < amountSamples) {
         buffer = ByteBuffer.allocateDirect(4 * amountSamples).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
         NativeAdc.configureChannel(channelId, NativeAdc.BBBIO_ADC_STEP_MODE_SW_CONTINUOUS, 0, 1, 1, buffer, amountSamples);
      }

      NativeAdc.fetchSamples(amountSamples);
      buffer.rewind();
      double[] doubles = new double[amountSamples];
      for (int i = 0; i < amountSamples; i++) {
         int bufferGet = buffer.get();
         doubles[i] = (bufferGet) / 4095.0;
      }
      buffer.clear();

      return doubles;
   }

   public synchronized double[] sample(int amountSamples, float frequency) {
      int divisor = NativeAdc.calculateNearestDivisorForFrequency(frequency);
      NativeAdc.configureModule(NativeAdc.BBBIO_ADC_WORK_MODE_BUSY_POLLING, divisor);
      return sample(amountSamples);
   }

   public Future<double[]> sampleAsync(final int amountSamples) {
      ExecutorService executor = Executors.newSingleThreadExecutor();
      Callable<double[]> callable = new Callable<double[]>() {

         public double[] call() throws Exception {
            return sample(amountSamples);
         }

      };

      return executor.submit(callable);
   }

   public Future<double[]> sampleAsync(final int amountSamples, final float frequency) {
      ExecutorService executor = Executors.newSingleThreadExecutor();
      Callable<double[]> callable = new Callable<double[]>() {

         public double[] call() throws Exception {
            return sample(amountSamples, frequency);
         }

      };

      return executor.submit(callable);
   }

}
