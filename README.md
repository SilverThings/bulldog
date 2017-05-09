# Bulldog - Java GPIO Library

![Bulldog](https://raw.githubusercontent.com/px3/px3.github.io/master/img/bulldog-logo.png)

## Table of Contents
- [Summary](#summary)
- [Usage](#usage)
  - [Maven](#maven)
  - [Example](#example)
- [Building Bulldog (x86, x86_64)](#building-bulldog-x86-x86_64)
- [Building Bulldog (ARM)](#building-bulldog-arm)
- [Continuous Integration](#continuous-integration)
- [Contribution Guidelines](#contribution-guidelines)

## Summary

Bulldog is a Java library providing Java (IoT) Developers with GPIO and low-level IO capabilities of embedded linux platforms (RaspberryPi, CubieBoard, BeagleBoneBlack).

Bulldog library is a part of the Silverspoon IoT Platform: http://silverspoon.io.

Bulldog currently supports the following features:

 - Digital Input/Output on Pins (GPIOs)
 - Native Interrupts via epoll (easily usable on DigitalInputs)
 - Native PWM, ADC
 - I2C, SPI
 - All UARTs (including dynamic setup via capemgr on request)
 - A few devices: Simple button API, Incremental Rotary Encoder, Servos, LCD, ...

##Usage

###Maven

Stable versions are synced with Maven Central. You just need to add a dependency to appropriate board implementation and Bulldog will automatically select the correct platform implementation:

```xml
<dependencies>
  <dependency>
    <groupId>io.silverspoon</groupId>
    <artifactId>bulldog-board-${board-name}</artifactId>
    <version>${version.bulldog}</version>
  </dependency>
</dependencies>
```

To use our development (SNAPSHOT) versions you need to add the following repository to your settings.xml:

```xml
<repositories>
  <repository>
    <id>sonatype-public-repository</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>        
  </repository>
</repositories>
```

###Example

The following steps can all be performed on your target device (e.g. RaspberryPi).

- Connect a LED diode to your RaspberyPi (you can follow this [tutorial](https://projects.drogon.net/raspberry-pi/gpio-examples/tux-crossing/gpio-examples-1-a-single-led)). 

- Create new maven project and add an appropriate maven dependency:

```xml
<dependencies>
  <dependency>
    <groupId>io.silverspoon</groupId>
    <artifactId>bulldog-board-raspberrypi</artifactId>
    <version>0.2.1</version>
  </dependency>
</dependencies>
```

- Author the following code (You can find the example also in [here](https://github.com/px3/silverspoon-demos/tree/master/general/bulldog-led):

```java
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.raspberrypi.RaspiNames;
  
public class BulldogLED {
  
  public static void main(String[] args) {
    //Detect the board we are running on
    Board board = Platform.createBoard();
    
    //Set up a digital output
    DigitalOutput output = board.getPin(RaspiNames.P1_11).as(DigitalOutput.class);

    // Blink the LED
    output.high();
    BulldogUtil.sleepMs(1000);
    output.low();
    }
}
```
- Configure maven-exec-plugin

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>1.3.2</version>
      <configuration>
        <mainClass>com.example.BulldogLED</mainClass>
      </configuration>
    </plugin>
  </plugins>
</build>
```

- Compile & execute:

```
mvn clean package
mvn exec:java
```

- If you have done everything well, your LED diode should come on, and after 1 second go off again.
- Please note that the JVM must be started with root privileges or user need to be in appropriate group for ```/dev/mem``` access e.g. _kmem_

_Note: For more see [bulldog-examples](https://github.com/px3/bulldog-examples) project._

##Building Bulldog (x86, x86_64)

###Prerequisites

- ARM C/C++ Cross Compiler (4.8+)
- Maven 3+

Fedora (20+)

    sudo yum install glibc-devel.i686
    sudo yum install gcc-arm-linux-gnu

Ubuntu (13.04+)

    sudo apt-get install gcc-4.8-arm-linux-gnueabihf

###Build

Fedora (default)

    mvn clean install

Ubuntu - need to overide compiler/linker binary:

    mvn clean install -Dcompiler.exec=arm-linux-gnueabihf-gcc-4.8 -Dlinker.exec=arm-linux-gnueabihf-ld

##Building Bulldog (ARM)

###Prerequisites

- ARM C/C++ Compiler (4.8+)
- Maven 3+

Raspbian

    sudo apt-get install gcc-4.8

###Build

    mvn clean install -Pnative-compile

##Continuous Integration

CI server hosted on Travis-ci.org: [![Build Status](https://travis-ci.org/px3/bulldog.svg?branch=master)](https://travis-ci.org/px3/bulldog)

##Contribution Guidelines

- If you find a bug, or have a feature request you think we should consider, please report it [here](https://github.com/px3/bulldog/issues).
- We use [gitflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) as a development workflow, so If you want to contribute to our code base create your own fork & send a pull request to the devel branch or ask to join our community to be able to commit directly to devel branch.
