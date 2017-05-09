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
package io.silverspoon.bulldog.core.bus;

import io.silverspoon.bulldog.core.io.bus.BusConnection;
import io.silverspoon.bulldog.core.mocks.MockedBus;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

public class TestBusConnection {

	@Test
	public void testBusConnection() throws IOException {
		MockedBus bus = new MockedBus("TESTBUS");
		BusConnection busConnection = new BusConnection(bus, 0x10);
		TestCase.assertEquals(0x10, busConnection.getAddress());
		
		bus.selectSlave(0x05);
		TestCase.assertTrue(bus.isSlaveSelected(0x05));
		busConnection.readByte();
		TestCase.assertTrue(bus.isSlaveSelected(0x10));
		TestCase.assertTrue(bus.isOpen());
		TestCase.assertEquals(0x10, busConnection.getAddress());
		
		bus.close();
		TestCase.assertFalse(bus.isOpen());
		bus.selectSlave(0x10);
		TestCase.assertTrue(bus.isSlaveSelected(0x10));
		bus.open();
		TestCase.assertTrue(bus.isOpen());
		busConnection.writeByte(0xff);
		TestCase.assertTrue(bus.isSlaveSelected(0x10));
		TestCase.assertTrue(bus.isOpen());
		
		bus.close();
		TestCase.assertFalse(bus.isOpen());
		bus.selectSlave(0x10);
		TestCase.assertTrue(bus.isSlaveSelected(0x10));
		TestCase.assertFalse(bus.isOpen());
		busConnection.getOutputStream();
		TestCase.assertTrue(bus.isSlaveSelected(0x10));
		TestCase.assertTrue(bus.isOpen());
		
		bus.close();
		TestCase.assertFalse(bus.isOpen());
		bus.selectSlave(0x10);
		TestCase.assertTrue(bus.isSlaveSelected(0x10));
		TestCase.assertFalse(bus.isOpen());
		busConnection.getInputStream();
		TestCase.assertTrue(bus.isSlaveSelected(0x10));
		TestCase.assertTrue(bus.isOpen());
	}
	
}
