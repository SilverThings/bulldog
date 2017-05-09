#include <stdarg.h>
#include <stdio.h>
#include "bulldog.h"

void errorMessage(char* messageFormat, ...) {
	va_list args;
	va_start(args, messageFormat);
	fprintf(stderr, messageFormat, args);
	va_end(args);
}
