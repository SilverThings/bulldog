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
import io.silverspoon.bulldog.core.mocks.MockedPinFeature1;
import io.silverspoon.bulldog.core.pin.Pin;
import junit.framework.TestCase;
import org.junit.Test;

public class TestAbstractPinFeature {

	@Test
	public void testAbstractPinFeature() {
		Pin pin = new Pin("Testpin", 0, "A", 0);
		MockedDigitalOutput output = new MockedDigitalOutput(pin);
		pin.addFeature(output);
		MockedPinFeature1 feature1 = new MockedPinFeature1(pin);
		pin.addFeature(feature1);
		
		TestCase.assertEquals(pin, output.getPin());
		TestCase.assertFalse(output.isActivatedFeature());
		TestCase.assertFalse(feature1.isActivatedFeature());
		TestCase.assertFalse(output.isSetup());
		
		pin.activateFeature(DigitalOutput.class);
		TestCase.assertTrue(output.isSetup());
		TestCase.assertFalse(feature1.isSetup());
		TestCase.assertEquals(pin, output.getPin());
		TestCase.assertTrue(output.isActivatedFeature());
		TestCase.assertFalse(feature1.isActivatedFeature());
		
		pin.activateFeature(MockedPinFeature1.class);
		TestCase.assertFalse(output.isSetup());
		TestCase.assertTrue(feature1.isSetup());
		TestCase.assertEquals(pin, output.getPin());
		TestCase.assertFalse(output.isActivatedFeature());
		TestCase.assertTrue(feature1.isActivatedFeature());
		
		output.activate();
		TestCase.assertTrue(output.isSetup());
		TestCase.assertFalse(feature1.isSetup());
		TestCase.assertEquals(pin, output.getPin());
		TestCase.assertTrue(output.isActivatedFeature());
		TestCase.assertFalse(feature1.isActivatedFeature());
		
		output.blockPin();
		TestCase.assertTrue(output.isBlocking());
		
		output.unblockPin();
		TestCase.assertFalse(output.isBlocking());
	}
	
}
