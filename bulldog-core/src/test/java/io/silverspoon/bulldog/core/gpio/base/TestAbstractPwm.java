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

import io.silverspoon.bulldog.core.mocks.MockedPwm;
import io.silverspoon.bulldog.core.pin.Pin;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractPwm {

	private Pin pin;
	
	@Before
	public void setup() {
		pin = new Pin("Testpin", 0, "A", 0);
		MockedPwm pwm = new MockedPwm(pin);
		pin.addFeature(pwm);
	}
	
	@Test
	public void testPwm() {
		MockedPwm pwm = pin.as(MockedPwm.class);
		TestCase.assertTrue(pwm.isSetup());
		TestCase.assertFalse(pwm.isBlocking());
	}
	
	@Test
	public void testFrequency() {
		MockedPwm pwm = pin.as(MockedPwm.class);
		
		try {
			pwm.setFrequency(0.0);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			pwm.setFrequency(-1.0);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			pwm.setFrequency(0.99);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		TestCase.assertFalse(pwm.pwmImplCalled());
		
		pwm.setFrequency(1.00);
		TestCase.assertEquals(1.00, pwm.getFrequency());
		TestCase.assertTrue(pwm.pwmImplCalled());
		pwm.reset();
		TestCase.assertFalse(pwm.pwmImplCalled());
		
		pwm.setFrequency(1000.0);
		TestCase.assertEquals(1000.0, pwm.getFrequency());
		TestCase.assertTrue(pwm.pwmImplCalled());
	}
	
	@Test
	public void testDuty() {
		MockedPwm pwm = pin.as(MockedPwm.class);
		
		try {
			pwm.setDuty(-1.0);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			pwm.setDuty(2.0);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			pwm.setDuty(1.00001);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		TestCase.assertFalse(pwm.pwmImplCalled());
		
		pwm.setDuty(1.00);
		TestCase.assertEquals(1.00, pwm.getDuty());
		TestCase.assertTrue(pwm.pwmImplCalled());
		pwm.reset();
		TestCase.assertFalse(pwm.pwmImplCalled());
		
		pwm.setDuty(0.00001);
		TestCase.assertEquals(0.00001, pwm.getDuty());
		TestCase.assertTrue(pwm.pwmImplCalled());
		pwm.reset();
		TestCase.assertFalse(pwm.pwmImplCalled());
		
		pwm.setDuty(0.999999999999999);
		TestCase.assertEquals(0.999999999999999, pwm.getDuty());
		TestCase.assertTrue(pwm.pwmImplCalled());
	}
	
	@Test
	public void testEnable() {
		MockedPwm pwm = pin.as(MockedPwm.class);
		TestCase.assertFalse(pwm.enableImplCalled());
		TestCase.assertFalse(pwm.disableImplCalled());
		pwm.enable();
		TestCase.assertTrue(pwm.isEnabled());
		TestCase.assertTrue(pwm.enableImplCalled());
		TestCase.assertFalse(pwm.disableImplCalled());
		pwm.disable();
		TestCase.assertFalse(pwm.isEnabled());
		TestCase.assertTrue(pwm.disableImplCalled());
	}
	
	@Test
	public void testName() {
		MockedPwm pwm = pin.as(MockedPwm.class);
		String name = pwm.getName();
		TestCase.assertNotNull(name);
		
		pwm.enable();
		name = pwm.getName();
		TestCase.assertNotNull(name);
	}
	
}
