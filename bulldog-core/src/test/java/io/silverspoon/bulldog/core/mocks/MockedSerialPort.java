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

import io.silverspoon.bulldog.core.Parity;
import io.silverspoon.bulldog.core.io.serial.SerialDataListener;
import io.silverspoon.bulldog.core.io.serial.SerialPort;

public class MockedSerialPort extends MockedIOPort implements SerialPort {
	
	public MockedSerialPort(String name) {
		super(name);
	}

	@Override
	public int getBaudRate() {
		return 0;
	}

	@Override
	public void setBaudRate(int baudRate) {	
	}

	@Override
	public Parity getParity() {
		return null;
	}

	@Override
	public void setParity(Parity parity) {	
	}

	@Override
	public void setBlocking(boolean blocking) {
	}

	@Override
	public boolean getBlocking() {
		return false;
	}

	@Override
	public void addListener(SerialDataListener listener) {	
	}

	@Override
	public void removeListener(SerialDataListener listener) {
	}

	@Override
	public int getDataBits() {
		return 0;
	}

	@Override
	public void setDataBits(int dataBits) {
	}

	@Override
	public int getStopBits() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setStopBits(int stopBits) {
		// TODO Auto-generated method stub
		
	}
	
}
