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

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.base.AbstractDigitalInput;
import io.silverspoon.bulldog.core.pin.Pin;

public class MockedDigitalInput extends AbstractDigitalInput {

	private volatile Signal appliedSignal;
	
	public MockedDigitalInput(Pin pin) {
		super(pin);
		appliedSignal = Signal.Low;
	}
	
	public void setSignalToRead(Signal signal) {
		this.appliedSignal = signal;
	}
	
	public void bounceSignal(final Signal signal, final long bounceTimeMs) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				long delta = 0;
				long start = System.currentTimeMillis();
				Signal nextSignal = signal;
				while(delta < bounceTimeMs) {
					setSignalToRead( nextSignal == Signal.High ? Signal.Low : Signal.High);
					delta = System.currentTimeMillis() - start;
				}
				
				setSignalToRead(signal);
			}
			
		});
		
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	@Override
	public Signal read() {
		return appliedSignal;
	}

	@Override
	protected void setupImpl() {
	}

	@Override
	protected void teardownImpl() {
	}


	@Override
	protected void enableInterruptsImpl() {
	}

	@Override
	protected void disableInterruptsImpl() {
	}


}
