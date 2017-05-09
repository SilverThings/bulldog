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

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.core.pin.Pin;

import java.io.IOException;

public class MockedI2cBus extends MockedBus implements I2cBus {

	public MockedI2cBus(String name) {
		super(name);
	}

	public Pin getSDA() {
		return null;
	}

	public Pin getSCL() {
		return null;
	}

	@Override
	public int getFrequency() {
		return 0;
	}

	@Override
	public void writeByteToRegister(int register, int b) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeBytesToRegister(int register, byte[] bytes)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte readByteFromRegister(int register) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readBytesFromRegister(int register, byte[] buffer)
			throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public I2cConnection createI2cConnection(int address) {
		// TODO Auto-generated method stub
		return null;
	}

}
