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
package io.silverspoon.bulldog.core.gpio.base;

import io.silverspoon.bulldog.core.event.ThresholdListener;
import io.silverspoon.bulldog.core.mocks.MockedAnalogInput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractAnalogInput {

	private Pin pin;
	
	@Before
	public void setup() {
		pin = new Pin("Testpin", 0, "A", 0);
		MockedAnalogInput input = new MockedAnalogInput(pin);
		pin.addFeature(input);
	}
	
	@Test
	public void testName() {
		MockedAnalogInput input = pin.as(MockedAnalogInput.class);
		String name = input.getName();
		TestCase.assertNotNull(name);
	}
	
	@Test
	public void testMonitoring() {
		MockedAnalogInput input = pin.as(MockedAnalogInput.class);
		TestCase.assertFalse(input.isBlocking());
		TestCase.assertTrue(input.isSetup());
		
		double[] samples = new double[20];
		for(int i = 0; i < 20; i++) {
			samples[i] = Math.sin(Math.PI / i);
		}
		
		input.setSamples(samples);
		input.stopMonitor(); // should do nothing
		TestCase.assertFalse(input.isBlocking());
		
		input.startMonitor(10, new ThresholdListener() {

			@Override
			public void thresholdReached() {
				
			}

			@Override
			public boolean isThresholdReached(double thresholdValue) {
				return thresholdValue >= 0.5;
			}
			
		});
		TestCase.assertTrue(input.isBlocking());
		BulldogUtil.sleepMs(1000);
		input.stopMonitor();
		TestCase.assertFalse(input.isBlocking());
	}
	
}
