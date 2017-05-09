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
package io.silverspoon.bulldog.core.platform;

import io.silverspoon.bulldog.core.mocks.MockedBoard;
import io.silverspoon.bulldog.core.pin.Pin;
import junit.framework.TestCase;
import org.junit.Test;

public class TestAbstractPinProvider {
	
	private String[] aliases = new String[] { "HanSolo" , "Princess Leia", "Chewbacca", "R2D2", "C3PO" };

	
	@Test
	public void testGetPinByName() {
		MockedBoard board = new MockedBoard();
		
		Pin availablePin = board.getPin("P4");
		TestCase.assertNotNull(availablePin);
		TestCase.assertEquals("P4", availablePin.getName());
		
		Pin nonExistentPin = board.getPin("P11");
		TestCase.assertNull(nonExistentPin);
		
		nonExistentPin = board.getPin("Chewbacca");
		TestCase.assertNull(nonExistentPin);
		
		for(Pin pin : board.getPins()) {
			Pin crossCheck = board.getPin(pin.getName());
			TestCase.assertSame(pin, crossCheck);
		}
		
		try {
			board.getPin(null);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
	}
	
	@Test
	public void testGetPin() {
		MockedBoard board = new MockedBoard();
		
		Pin availablePin = board.getPin("A", 1);
		TestCase.assertNotNull(availablePin);
		TestCase.assertEquals("A", availablePin.getPort());
		TestCase.assertEquals(1, availablePin.getIndexOnPort());
		
		Pin nonExistentPin = board.getPin("A", 11);
		TestCase.assertNull(nonExistentPin);
		
		nonExistentPin = board.getPin("Chewbacca", 1);
		TestCase.assertNull(nonExistentPin);
		
		for(Pin pin : board.getPins()) {
			Pin crossCheck = board.getPin(pin.getPort(), pin.getIndexOnPort());
			TestCase.assertSame(pin, crossCheck);
		}
		
		try {
			board.getPin(null, 2);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}

		try {
			board.getPin(null, -1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			board.getPin("A", -1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
	}
	
	
	@Test
	public void testGetPinByAlias() {
		MockedBoard board = new MockedBoard();
		
		for(int i = 0; i < aliases.length; i++) {
			board.getPins().get(i).setAlias(aliases[i]);
		}
		
		Pin availablePin = board.getPinByAlias(aliases[4]);
		TestCase.assertNotNull(availablePin);
		TestCase.assertEquals(aliases[4], availablePin.getAlias());
		
		Pin nonExistentPin = board.getPinByAlias("LALALALALA");
		TestCase.assertNull(nonExistentPin);
		
		nonExistentPin = board.getPinByAlias("Greedo");
		TestCase.assertNull(nonExistentPin);
		
		for(Pin pin : board.getPins()) {
			if(pin.getAlias() != null) {
				Pin crossCheck = board.getPinByAlias(pin.getAlias());
				TestCase.assertSame(pin, crossCheck);
			} else {
				try {
					board.getPinByAlias(pin.getAlias());
					TestCase.fail();
				} catch(IllegalArgumentException ex) {}
			}
		}
		
		try {
			board.getPinByAlias(null);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	public void testGetPinByAdress() {
		MockedBoard board = new MockedBoard();
		
		Pin availablePin = board.getPin(0);
		TestCase.assertNotNull(availablePin);
		TestCase.assertEquals(0, availablePin.getAddress());
		
		Pin nonExistentPin = board.getPin(-1);
		TestCase.assertNull(nonExistentPin);
		
		nonExistentPin = board.getPin(11);
		TestCase.assertNull(nonExistentPin);
		
		for(Pin pin : board.getPins()) {
			Pin crossCheck = board.getPin(pin.getAddress());
			TestCase.assertSame(pin, crossCheck);
		}
	}
	
}
