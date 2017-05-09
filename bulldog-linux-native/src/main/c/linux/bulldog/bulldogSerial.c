#include <errno.h>
#include <termios.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <stdio.h>
#include "bulldog.h"
#include "bulldogSerial.h"



int serialSetAttributes(int fd, int speed, int parity, int readTimeout, int numDataBits, int numStopBits) {
	struct termios tty;
	int baudRate = B115200;
	memset(&tty, 0, sizeof tty);
	if (tcgetattr(fd, &tty) != 0) {
		errorMessage("error %d from tcgetattr", errno);
		return -1;
	}

	switch (speed)
	  {
	    case     50:	baudRate =     B50 ; break ;
	    case     75:	baudRate =     B75 ; break ;
	    case    110:	baudRate =    B110 ; break ;
	    case    134:	baudRate =    B134 ; break ;
	    case    150:	baudRate =    B150 ; break ;
	    case    200:	baudRate =    B200 ; break ;
	    case    300:	baudRate =    B300 ; break ;
	    case    600:	baudRate =    B600 ; break ;
	    case   1200:	baudRate =   B1200 ; break ;
	    case   1800:	baudRate =   B1800 ; break ;
	    case   2400:	baudRate =   B2400 ; break ;
	    case   9600:	baudRate =   B9600 ; break ;
	    case  19200:	baudRate =  B19200 ; break ;
	    case  38400:	baudRate =  B38400 ; break ;
	    case  57600:	baudRate =  B57600 ; break ;
	    case 115200:	baudRate = B115200 ; break ;
	    case 230400:	baudRate = B230400 ; break ;

	    default:
	      errorMessage("error setting baud rate - unknown speed: %d", speed);
	      return -1;
	  }

	cfsetospeed(&tty, baudRate);
	cfsetispeed(&tty, baudRate);

	int bitSize = CS8;
	if(numDataBits == 5) {
		bitSize = CS5;
	} else if(numDataBits == 6) {
		bitSize = CS6;
	} else if(numDataBits == 7) {
		bitSize = CS7;
	}

	tty.c_cflag = (tty.c_cflag & ~CSIZE) | bitSize;     // 8-bit chars
	// disable IGNBRK for mismatched speed tests; otherwise receive break
	// as \000 chars
	tty.c_iflag &= ~IGNBRK;         		  // disable break processing
	tty.c_lflag = 0;                		  // no signaling chars, no echo,
											  // no canonical processing
	tty.c_oflag = 0;                		  // no remapping, no delays
	tty.c_cc[VMIN] = 0;             		  // read doesn't block
	tty.c_cc[VTIME] = readTimeout;            // read timeout

	tty.c_iflag &= ~(IXON | IXOFF | IXANY);   // shut off xon/xoff ctrl

	tty.c_cflag |= (CLOCAL | CREAD);		 // ignore modem controls,
											 // enable reading
	tty.c_cflag &= ~(PARENB | PARODD);       // shut off parity
	tty.c_cflag |= parity;

	if(parity) {
		tty.c_iflag |= (INPCK | ISTRIP);
	}

	if(numStopBits == 1) {
		tty.c_cflag &= ~CSTOPB;
	} else {
		tty.c_cflag |= CSTOPB;
	}

	tty.c_cflag &= ~CRTSCTS;

	if (tcsetattr(fd, TCSANOW, &tty) != 0) {
		errorMessage("error %d from tcsetattr", errno);
		return -1;
	}

	return 0;
}

void serialSetBlocking(int fd, int block, int readTimeout) {
	struct termios tty;
	memset(&tty, 0, sizeof tty);

	if (tcgetattr(fd, &tty) != 0) {
		errorMessage("error %d from tggetattr", errno);
		return;
	}

	tty.c_cc[VMIN]  = block > 0 ? 1 : 0;
	tty.c_cc[VTIME] = readTimeout;

	if (tcsetattr(fd, TCSANOW, &tty) != 0) {
		errorMessage("error %d setting term attributes", errno);
	}
}

int serialOpen(char* portname, int baud, int parity, int blocking, int readTimeout, int numDataBits, int numStopBits) {
	int fd = open(portname, O_RDWR | O_NOCTTY | O_SYNC);
	if (fd < 0) {
		errorMessage("error %d opening %s: %s", errno, portname, strerror(errno));
		return 1;
	}

	serialSetAttributes(fd, baud, parity, readTimeout, numDataBits, numStopBits);
	serialSetBlocking(fd, blocking, readTimeout);

	return fd;
}

int serialOpenSimple(char* portname, int baud) {
	return serialOpen(portname, baud, 0, SERIAL_NO_BLOCK,
	    SERIAL_DEFAULT_TIMEOUT, SERIAL_DEFAULT_DATA_BITS, SERIAL_DEFAULT_STOP_BITS);
}

int serialClose(int fd) {
	return close(fd);
}

int serialWriteCharacter(int fileDescriptor, unsigned char data) {
	return write(fileDescriptor, &data, 1);
}

int serialWriteBuffer(int fileDescriptor, char* data) {
	return write(fileDescriptor, data, strlen(data));
}

int serialReadBuffer(int fileDescriptor, char* buffer, int bufferSize) {
	return read(fileDescriptor, buffer, bufferSize);
}

unsigned char serialReadCharacter(int fileDescriptor) {
	char output;
	if(read(fileDescriptor, &output, 1) < 0) {
		errorMessage("read failed: %s", strerror(errno));
	}

	return output;
}

int serialDataAvailable(int fd) {
  int result;

  if (ioctl(fd, FIONREAD, &result) == -1) {
	  return -1;
  }

  return result;
}


