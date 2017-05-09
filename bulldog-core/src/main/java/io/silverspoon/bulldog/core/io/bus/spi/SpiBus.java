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
package io.silverspoon.bulldog.core.io.bus.spi;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.io.bus.Bus;
import io.silverspoon.bulldog.core.pin.Pin;

import java.io.IOException;
import java.util.List;

public interface SpiBus extends Bus {

   Pin getMISO();

   Pin getMOSI();

   Pin getSCLK();

   List<DigitalOutput> getSlaveSelectPins();

   int getSpeedInHz();

   void setSpeedInHz(int hz);

   void setBitsPerWord(int bpw);

   int getBitsPerWord();

   void setDelayMicroseconds(int delay);

   int getDelayMicroseconds();

   void setMode(SpiMode mode);

   SpiMode getMode();

   void useLeastSignificantBitFirst();

   void useMostSignificantBitFirst();

   boolean isLSBUsed();

   boolean isMSBUsed();

   void selectSlave(DigitalOutput chipSelect);

   void selectSlaves(DigitalOutput... chipSelects);

   void selectSlaves(Integer... chipSelectAddresses);

   SpiConnection createSpiConnection();

   SpiConnection createSpiConnection(int chipSelectAddress);

   SpiConnection createSpiConnection(DigitalOutput chipSelect);

   SpiConnection createSpiConnection(DigitalOutput... chipSelects);

   SpiConnection createSpiConnection(int... chipSelectAddress);

   void broadcast(byte[] bytes, DigitalOutput... chipSelects) throws IOException;

   SpiMessage transfer(byte[] buffer);
}
