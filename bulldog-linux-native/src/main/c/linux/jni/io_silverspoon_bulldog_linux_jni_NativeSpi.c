#include <jni.h>
#include <sys/types.h>
#include <fcntl.h>
#include <unistd.h>
#include "io_silverspoon_bulldog_linux_jni_NativeSpi.h"
#include "../bulldog/bulldogSpi.h"

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeSpi
 * Method:    spiOpen
 * Signature: (Ljava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeSpi_spiOpen
  (JNIEnv * env, jclass clazz, jstring path, jint mode, jint speed, jint bitsPerWord, jboolean lsbFirst) {
	char fileName[256];
	int len = (*env)->GetStringLength(env, path);
	(*env)->GetStringUTFRegion(env, path, 0, len, fileName);
	return spiOpen(fileName, mode, speed, bitsPerWord, lsbFirst == JNI_TRUE ? 1 : 0);
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeSpi
 * Method:    spiOpen
 * Signature: (Ljava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeSpi_spiConfig
  (JNIEnv * env, jclass clazz, jint fileDescriptor, jint mode, jint speed, jint bitsPerWord, jboolean lsbFirst) {
	return spiConfig(fileDescriptor, mode, speed, bitsPerWord, lsbFirst == JNI_TRUE ? 1 : 0);
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeSpi
 * Method:    spiClose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeSpi_spiClose
  (JNIEnv * env, jclass clazz, jint fileDescriptor) {
	return spiClose(fileDescriptor);
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeSpi
 * Method:    spiTransfer
 * Signature: (IJJIIII)I
 */
JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeSpi_spiTransfer
  (JNIEnv * env , jclass clazz, jint fileDescriptor, jobject txBuffer, jobject rxBuffer, jint transferLength, jint delay, jint speed, jint bitsPerWord) {
	unsigned int *pTx = NULL;
	unsigned int *pRx = NULL;

	if(txBuffer != NULL) {
		pTx = (unsigned int *) (*env)->GetDirectBufferAddress(env, txBuffer);
	}

	if(rxBuffer != NULL) {
		pRx = (unsigned int *) (*env)->GetDirectBufferAddress(env, rxBuffer);
	}

	return spiTransfer((int) fileDescriptor, (unsigned int *) pTx, (unsigned int *) pTx, (int) transferLength,
	    (int) delay, (int) speed, (int) bitsPerWord);
}
