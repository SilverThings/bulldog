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

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.mocks.MockedDigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractDigitalOutput {

	private Pin pin;
	private GpioTester gpioTester;
	
	@Before
	public void setup() {
		pin = new Pin("Testpin", 0, "A", 0);
		MockedDigitalOutput output = new MockedDigitalOutput(pin);
		pin.addFeature(output);
		gpioTester = new GpioTester();
	}
	
	@Test
	public void testOutput() {
		gpioTester.testOutput(pin.as(DigitalOutput.class));
	}
	
	@Test
	public void testName() {
		DigitalOutput output = pin.as(DigitalOutput.class);
		String name = output.getName();
		TestCase.assertNotNull(name);
	}
	
	@Test
	public void testBlinking() {
		gpioTester.testBlinking(pin.as(DigitalOutput.class));
	}


}
