#include <jni.h>
#include <stdint.h>
#include <sys/mman.h>
#include "io_silverspoon_bulldog_linux_jni_NativeMmap.h"

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    createMap
 * Signature: (JJIIJ)J
 */
JNIEXPORT jlong JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_createMap
  (JNIEnv * env, jobject clazz, jlong address, jlong length, jint prot, jint flags, jint fileDescriptor, jlong offset) {
	int *addrPointer = (int *)(intptr_t)address;
	void *retval = NULL;


	retval = mmap(addrPointer, length, prot, flags, fileDescriptor, offset);
	unsigned int *modified_retval = (unsigned int *) retval;
	printf("%x, %x\n", modified_retval, retval);
	if (retval == MAP_FAILED) {
	   perror("mmap");
	   return (jlong)(intptr_t) 0x0;
	}
	return (jlong) modified_retval;
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    deleteMap
 * Signature: (J)V
 */
JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_deleteMap
  (JNIEnv * env, jobject clazz, jlong address, jlong length) {
	int *addrPointer = (int *)(intptr_t) address;
	return (jint)(intptr_t) munmap(addrPointer, length);
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    setIntValueAt
 * Signature: (LI)V
 */
JNIEXPORT void JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_setIntValueAt
  (JNIEnv * env, jobject clazz, jlong address, jint value) {
	int *ptr = (int *) address;
	*ptr = value;
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    setShortValueAt
 * Signature: (LI)V
 */
JNIEXPORT void JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_setShortValueAt
  (JNIEnv * env, jobject clazz, jlong address, jshort value) {
	short *ptr = (short *)(intptr_t) address;
	*ptr = value;
}


/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    setLongValueAt
 * Signature: (LI)V
 */
JNIEXPORT void JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_setLongValueAt
  (JNIEnv * env, jobject clazz, jlong address, jlong value) {
	long *ptr = (long *)(intptr_t) address;
	*ptr = value;
}


/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    setByteValueAt
 * Signature: (LI)V
 */
JNIEXPORT void JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_setByteValueAt
  (JNIEnv * env, jobject clazz, jlong address, jbyte value) {
	char * ptr = (char *)(intptr_t) address;
	*ptr = value;
}


/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    getIntValueAt
 * Signature: (L)I
 */
JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_getIntValueAt
  (JNIEnv * env, jobject clazz, jlong address) {
	int * ptr = (int *)(intptr_t) address;
	return (jint)(*ptr);
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    getByteValueAt
 * Signature: (L)I
 */
JNIEXPORT jbyte JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_getByteValueAt
  (JNIEnv * env, jobject clazz, jlong address) {
	char *ptr = (char *)(intptr_t) address;
	return (jbyte)(*ptr);
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    getShortValueAt
 * Signature: (L)I
 */
JNIEXPORT jshort JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_getShortValueAt
  (JNIEnv * env, jobject clazz, jlong address) {
	short *ptr = (short *)(intptr_t) address;
	return (jshort)(*ptr);
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeMmap
 * Method:    getLongValueAt
 * Signature: (L)I
 */
JNIEXPORT jlong JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeMmap_getLongValueAt
  (JNIEnv * env, jobject clazz, jlong address) {
	long *ptr = (long *)(intptr_t) address;
	return (jlong)(*ptr);
}

