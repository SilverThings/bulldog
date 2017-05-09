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
import io.silverspoon.bulldog.core.platform.AbstractBoard;

public class MockedBoard extends AbstractBoard {

	public MockedBoard() {
		for(int i = 0; i < 10; i++) {
			getPins().add(new Pin("P" + i, i, "A", i));
			getI2cBuses().add(new MockedI2cBus("I2C" + i));
			getSerialPorts().add(new MockedSerialPort("Serial" + i));
		}
	}
	
	@Override
	public String getName() {
		return "MockedBoard";
	}


}
