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
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.io.serial.SerialPort;

import java.util.List;
import java.util.Properties;

public interface Board extends PinProvider {

   String getName();

   List<I2cBus> getI2cBuses();

   I2cBus getI2cBus(String name);

   List<SpiBus> getSpiBuses();

   SpiBus getSpiBus(String name);

   List<SerialPort> getSerialPorts();

   SerialPort getSerialPort(String name);

   List<IOPort> getAllIOPorts();

   IOPort getIOPortByAlias(String alias);

   IOPort getIOPortByName(String name);

   Properties getProperties();

   void setProperty(String propertyName, String value);

   String getProperty(String propertyName);

   boolean hasProperty(String propertyName);

   void shutdown();
}
