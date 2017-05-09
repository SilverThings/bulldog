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

import io.silverspoon.bulldog.core.io.IOPort;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.mocks.MockedBoard;
import junit.framework.TestCase;
import org.junit.Test;

public class TestAbstractBoard {

	private String[] aliases = new String[] { "HanSolo" , "Princess Leia", "Chewbacca", "R2D2", "C3PO" };

	
	@Test
	public void testGetBusByAlias() {
		MockedBoard board = new MockedBoard();
		
		for(int i = 0; i < aliases.length; i++) {
			if(i%2 == 0)  {
				board.getI2cBuses().get(i).setAlias(aliases[i]);
			} else {
				board.getSerialPorts().get(i).setAlias(aliases[i]);
			}
		}
		
		IOPort availablePort = board.getIOPortByAlias(aliases[4]);
		TestCase.assertNotNull(availablePort);
		TestCase.assertEquals(aliases[4], availablePort.getAlias());
		TestCase.assertTrue( I2cBus.class.isAssignableFrom(availablePort.getClass()));
		
		
		IOPort nonExistentPort = board.getIOPortByAlias("LALALALALA");
		TestCase.assertNull(nonExistentPort);
		
		nonExistentPort = board.getIOPortByAlias("Greedo");
		TestCase.assertNull(nonExistentPort);
		
		for(IOPort port : board.getAllIOPorts()) {
			if(port.getAlias() != null) {
				IOPort crossCheck = board.getIOPortByAlias(port.getAlias());
				TestCase.assertSame(port, crossCheck);
			} else {
				try {
					board.getIOPortByAlias(port.getAlias());
					TestCase.fail();
				} catch(IllegalArgumentException ex) {}

			}
		}
		
		try {
			board.getIOPortByAlias(null);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
	}
	
	@Test
	public void testGetBusByName() {
		MockedBoard board = new MockedBoard();
		
		IOPort availablePort = board.getIOPortByName("I2C1");
		TestCase.assertNotNull(availablePort);
		TestCase.assertTrue(availablePort instanceof I2cBus);
		
		IOPort nonExistentPort = board.getIOPortByName("LALALALALA");
		TestCase.assertNull(nonExistentPort);
		
		nonExistentPort = board.getIOPortByName("Greedo");
		TestCase.assertNull(nonExistentPort);
		
		for(IOPort bus : board.getAllIOPorts()) {
			if(bus.getName() != null) {
				IOPort crossCheck = board.getIOPortByName(bus.getName());
				TestCase.assertSame(bus, crossCheck);
			} else {
				try {
					board.getIOPortByName(bus.getName());
					TestCase.fail();
				} catch(IllegalArgumentException ex) {}
			}
		}
		
		try {
			board.getIOPortByName(null);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}

	}
	
	@Test
	public void testGetI2cBus() {
		MockedBoard board = new MockedBoard();
		TestCase.assertNotNull(board.getI2cBus("I2C1"));
		TestCase.assertNull(board.getI2cBus("I2C999"));
	}
	
	@Test
	public void testGetSerialPort() {
		MockedBoard board = new MockedBoard();
		TestCase.assertNotNull(board.getSerialPort("Serial1"));
		TestCase.assertNull(board.getI2cBus("Serial999"));
	}
	
	@Test
	public void testProperties() {
		MockedBoard board = new MockedBoard();
		TestCase.assertNotNull(board.getProperties());
		TestCase.assertNull(board.getProperty("ASD"));
		
		board.setProperty("test", "hello");
		try {
			board.setProperty("test2", null);
			TestCase.fail();
		} catch(NullPointerException ex) { }
		TestCase.assertTrue(board.hasProperty("test"));
		TestCase.assertFalse(board.hasProperty("test2"));
		TestCase.assertFalse(board.hasProperty("TEST"));
		TestCase.assertFalse(board.hasProperty("test2 "));
		try {
			TestCase.assertFalse(board.hasProperty(null));
			TestCase.fail();
		} catch(NullPointerException ex) {}
		TestCase.assertEquals("hello", board.getProperty("test"));
		
	}
}
