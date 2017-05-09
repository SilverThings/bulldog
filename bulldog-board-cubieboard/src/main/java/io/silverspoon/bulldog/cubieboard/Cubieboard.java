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
package io.silverspoon.bulldog.cubieboard;

import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.AbstractBoard;
import io.silverspoon.bulldog.cubieboard.gpio.CubieboardDigitalInput;
import io.silverspoon.bulldog.cubieboard.gpio.CubieboardDigitalOutput;
import io.silverspoon.bulldog.cubieboard.gpio.CubieboardGpioMemory;
import io.silverspoon.bulldog.linux.util.LinuxLibraryLoader;

public class Cubieboard extends AbstractBoard {
   private static final String NAME = "Cubieboard";
   private static Cubieboard instance;
   private CubieboardGpioMemory gpioMemory;

   public static Cubieboard getInstance() {
      if (instance == null) {
         LinuxLibraryLoader.loadNativeLibrary(NAME.toLowerCase());
         instance = new Cubieboard();
      }

      return instance;
   }

   private Cubieboard() {
      super();
      gpioMemory = new CubieboardGpioMemory();
      createPins();
   }

   @Override
   public void cleanup() {
      super.cleanup();
      gpioMemory.cleanup();
   }

   @Override
   public String getName() {
      return NAME;
   }

   private void createPins() {
      // PB
      getPins().add(createDigitalIOPin(CubieboardNames.PB10, 66, "B", 10, "66_pb10", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PB18, 3, "B", 18, "3_pb18", false));
      // PG
      getPins().add(createDigitalIOPin(CubieboardNames.PG0, 9, "G", 0, "9_pg0", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG1, 7, "G", 1, "7_pg1", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG2, 8, "G", 2, "8_pg2", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG4, 6, "G", 4, "6_pg4", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG5, 5, "G", 5, "5_pg5", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG6, 4, "G", 6, "4_pg6", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG7, 19, "G", 7, "19_pg7", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG8, 18, "G", 8, "18_pg8", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG9, 17, "G", 9, "17_pg9", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG10, 16, "G", 10, "16_pg10", false));
      getPins().add(createDigitalIOPin(CubieboardNames.PG11, 15, "G", 11, "15_pg11", false));
      // PH
      getPins().add(createDigitalIOPin(CubieboardNames.PH7, 67, "H", 7, "67_ph7", true));
      getPins().add(createDigitalIOPin(CubieboardNames.PH14, 10, "H", 14, "10_ph14", true));
      getPins().add(createDigitalIOPin(CubieboardNames.PH15, 11, "H", 11, "11_ph15", true));
   }

   public Pin createDigitalIOPin(String name, int address, String port, int indexOnPort, String fsName, boolean interrupt) {
      CubieboardPin pin = new CubieboardPin(name, address, port, indexOnPort, fsName, interrupt);
      int portIndex = port.charAt(0) - 'A';
      CubieboardDigitalInput input = new CubieboardDigitalInput(pin, gpioMemory, portIndex);
      CubieboardDigitalOutput output = new CubieboardDigitalOutput(pin, gpioMemory, portIndex);
      pin.addFeature(new DigitalIOFeature(pin, input, output));
      return pin;
   }
}
