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
package io.silverspoon.bulldog.core.mocks;

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.pwm.AbstractAnalogInput;

import java.util.concurrent.Future;

public class MockedAnalogInput extends AbstractAnalogInput {

	private double value;
	private double[] samples;
	
	public MockedAnalogInput(Pin pin) {
		super(pin);
	}

	public void setValueToRead(double value) {
		this.value = value;
	}
	
	public void setSamples(double[] samples) {
		this.samples = samples;
	}
	
	@Override
	public double read() {
		return value;
	}

	@Override
	public double[] sample(int amountSamples) {
		return samples;
	}

	@Override
	public double[] sample(int amountSamples, float frequency) {
		return samples;
	}

	@Override
	public Future<double[]> sampleAsync(int amountSamples) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<double[]> sampleAsync(int amountSamples, float frequency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setupImpl() {
	}

	@Override
	protected void teardownImpl() {
	}

}
