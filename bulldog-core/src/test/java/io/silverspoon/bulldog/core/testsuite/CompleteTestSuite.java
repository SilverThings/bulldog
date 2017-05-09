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
package io.silverspoon.bulldog.core.testsuite;

import io.silverspoon.bulldog.core.TestSignal;
import io.silverspoon.bulldog.core.bus.TestBusConnection;
import io.silverspoon.bulldog.core.gpio.base.*;
import io.silverspoon.bulldog.core.gpio.util.TestSoftPwm;
import io.silverspoon.bulldog.core.platform.TestAbstractBoard;
import io.silverspoon.bulldog.core.platform.TestAbstractPinProvider;
import io.silverspoon.bulldog.core.util.TestBitMagic;
import io.silverspoon.bulldog.core.util.TestBulldogUtil;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
				TestAbstractPinProvider.class,
				TestAbstractBoard.class, 
				TestPin.class,
				TestAbstractPinFeature.class,
				TestAbstractDigitalOutput.class,
				TestAbstractDigitalInput.class,
				TestDigitalIOFeature.class,
				TestAbstractAnalogInput.class,
				TestAbstractPwm.class,
				TestSignal.class,
				TestSoftPwm.class,
				TestBusConnection.class,
				TestBulldogUtil.class,
				TestBitMagic.class
			  })
public class CompleteTestSuite {

}