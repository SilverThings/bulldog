package io.silverspoon.bulldog.devices.sensors;

import java.io.IOException;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiDevice;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMessage;
import io.silverspoon.bulldog.core.util.BulldogUtil;

/**
 * Texas Instruments LM74 temperature sensor implementation.
 * 
 * @author matejperejda
 *
 */
public class LM74TemperatureSensor extends SpiDevice {
   DigitalOutput digitalOutput = null;

   public LM74TemperatureSensor(SpiConnection connection) {
      super(connection);
   }

   public LM74TemperatureSensor(SpiConnection connection, DigitalOutput digitalOutput) {
      super(connection);
      this.digitalOutput = digitalOutput;
   }

   /**
    * Reading a temperature from temp sensor. Two's complement calculation.
    * 
    * @see Page 11: http://www.ti.com/lit/ds/symlink/lm74.pdf
    * @return float temperature value
    **/
   public float readTemperature() throws IOException {

      float temperature = 0;
      int bitShift;

      // LM74 activation signal
      // LM74'll be activated when signal goes from 1 to 0
      digitalOutput.low();
      digitalOutput.toggle();
      BulldogUtil.sleepMs(500);

      this.open();

      // sending bytes
      byte[] buffer = new byte[] {(byte) 0x00, (byte) 0x00};

      SpiMessage message = this.transfer(buffer);

      byte[] rec = message.getReceivedBytes();

      int rec_0 = rec[0] & 0xFF;

      int merged = (((BulldogUtil.getUnsignedByte(rec[0]) << 8)) | BulldogUtil.getUnsignedByte(rec[1]));
      int first = rec_0 & Integer.parseInt("80", 16);

      // negative temperature
      // first binary digit 1 represents negative value
      // 128 = 10000000
      if (first == 128) {
         int substr = merged - 1;
         short inverted = (short) ~substr;
         bitShift = inverted >> 3;
         temperature = (float) (bitShift * 0.0625 * -1f);
      }
      // positive temperature
      else {
         bitShift = merged >> 3;
         temperature = (float) (bitShift * 0.0625);
      }

      // chip shutdown
      buffer = new byte[] {(byte) 0xFF};
      this.transfer(buffer);
      this.close();

      return temperature;
   }

}
