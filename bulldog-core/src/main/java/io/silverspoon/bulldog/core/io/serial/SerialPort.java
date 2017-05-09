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
package io.silverspoon.bulldog.core.io.serial;

import io.silverspoon.bulldog.core.Parity;
import io.silverspoon.bulldog.core.io.IOPort;

public interface SerialPort extends IOPort {

   int getBaudRate();

   void setBaudRate(int baudRate);

   Parity getParity();

   void setParity(Parity parity);

   int getDataBits();

   void setDataBits(int dataBits);

   int getStopBits();

   void setStopBits(int stopBits);

   void setBlocking(boolean blocking);

   boolean getBlocking();

   void addListener(SerialDataListener listener);

   void removeListener(SerialDataListener listener);
}
