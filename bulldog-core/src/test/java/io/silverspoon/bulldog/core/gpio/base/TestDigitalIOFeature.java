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

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalIO;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.mocks.MockedDigitalInput;
import io.silverspoon.bulldog.core.mocks.MockedDigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class TestDigitalIOFeature {

	private Pin pin;
	private GpioTester gpioTester;
	private MockedDigitalInput mockedInput;
	
	@Before
	public void setup() {
		pin = new Pin("Testpin", 0, "A", 0);
		mockedInput = new MockedDigitalInput(pin);
		MockedDigitalOutput output = new MockedDigitalOutput(pin);
		pin.addFeature(new DigitalIOFeature(pin, mockedInput, output));
		gpioTester = new GpioTester();
	}
	
	@Test
	public void testOutput() {
		gpioTester.testOutput(pin.as(DigitalIO.class));
	}
	
	@Test
	public void testName() {
		DigitalOutput output = pin.as(DigitalOutput.class);
		String name = output.getName();
		TestCase.assertNotNull(name);
	}
	
	
	@Test
	public void testBlinking() {
		gpioTester.testBlinking(pin.as(DigitalIO.class));
	}
	
	@Test
	public void testRead() {
		gpioTester.testRead(pin.as(DigitalIO.class), mockedInput);
	}
	
	@Test
	public void testReadDebounced() {
		gpioTester.testReadDebounced(pin.as(DigitalIO.class), mockedInput);
	}
	
	@Test
	public void testInterruptTrigger() {
		gpioTester.testInterruptTrigger(pin.as(DigitalIO.class));
	}
	
	@Test
	public void testInterrupts() {
		gpioTester.testInterrupts(pin.as(DigitalIO.class));
	}
	
	@Test
	public void testInputOutputSwitch() {
		DigitalIO io = pin.as(DigitalIO.class);
		TestCase.assertFalse(io.isOutputActive());
		TestCase.assertFalse(io.isInputActive());
		io.applySignal(Signal.High);
		TestCase.assertTrue(io.isOutputActive());
		TestCase.assertFalse(io.isInputActive());
		mockedInput.setSignalToRead(Signal.Low);
		TestCase.assertEquals(Signal.Low, io.read());
		TestCase.assertFalse(io.isOutputActive());
		TestCase.assertTrue(io.isInputActive());
		io.applySignal(Signal.High);
		TestCase.assertEquals(io.getAppliedSignal(), Signal.High);
		TestCase.assertTrue(io.isOutputActive());
		TestCase.assertFalse(io.isInputActive());
	}
	
}
