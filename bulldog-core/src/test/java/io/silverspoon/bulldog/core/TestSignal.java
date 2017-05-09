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
package io.silverspoon.bulldog.core;

import junit.framework.TestCase;
import org.junit.Test;


public class TestSignal {

	@Test
	public void testNumericValue() {
		TestCase.assertEquals(1, Signal.High.getNumericValue());
		TestCase.assertEquals(0, Signal.Low.getNumericValue());
		
		TestCase.assertEquals(Signal.Low, Signal.fromNumericValue(0));
		TestCase.assertEquals(Signal.High, Signal.fromNumericValue(1));
		Signal signal = Signal.fromNumericValue(1000);
		TestCase.assertEquals(Signal.High, signal);
		TestCase.assertEquals(1, signal.getNumericValue());
	}
	
	@Test
	public void testBooleanValue() {
		TestCase.assertTrue(Signal.High.getBooleanValue());
		TestCase.assertFalse(Signal.Low.getBooleanValue());
		
		TestCase.assertEquals(Signal.Low, Signal.fromBooleanValue(false));
		TestCase.assertEquals(Signal.High, Signal.fromBooleanValue(true));
	}
	
	@Test 
	public void testInverse() {
		TestCase.assertEquals(Signal.Low, Signal.High.inverse());
		TestCase.assertEquals(Signal.High, Signal.Low.inverse());
	}
	
	@Test
	public void testFromString() {
		try {
			Signal.fromString(null);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			Signal.fromString("highhh");
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
	
		try {
			Signal.fromString("asdasd");
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		Signal signal = Signal.fromString("low");
		TestCase.assertEquals(Signal.Low, signal);
		
		signal = Signal.fromString("LOW");
		TestCase.assertEquals(Signal.Low, signal);
		
		signal = Signal.fromString("Low");
		TestCase.assertEquals(Signal.Low, signal);
		
		signal = Signal.fromString("lOw    ");
		TestCase.assertEquals(Signal.Low, signal);
		
		signal = Signal.fromString("high");
		TestCase.assertEquals(Signal.High, signal);
		
		signal = Signal.fromString("HIGH");
		TestCase.assertEquals(Signal.High, signal);
		
		signal = Signal.fromString("   HiGH ");
		TestCase.assertEquals(Signal.High, signal);
		
		signal = Signal.fromString("   1 ");
		TestCase.assertEquals(Signal.High, signal);
		
		signal = Signal.fromString("1");
		TestCase.assertEquals(Signal.High, signal);
		
		signal = Signal.fromString("0");
		TestCase.assertEquals(Signal.Low, signal);
		
		signal = Signal.fromString("  0000   ");
		TestCase.assertEquals(Signal.Low, signal);
		
		signal = Signal.fromString("  1.0   ");
		TestCase.assertEquals(Signal.High, signal);
	}
	
}
