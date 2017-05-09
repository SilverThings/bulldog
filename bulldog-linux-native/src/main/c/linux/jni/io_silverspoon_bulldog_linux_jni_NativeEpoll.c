#include <errno.h>
#include <fcntl.h>
#include <jni.h>
#include <jni_md.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/epoll.h>
#include <unistd.h>
#include "io_silverspoon_bulldog_linux_jni_NativeEpoll.h"

static int openWait(const char *filepath) {
	int fd = -1;
	while(fd <= 0) {
		fd = open(filepath, O_RDONLY);
		if(fd > 0) {
			return fd;
		}

		sleep(100);
	}

	return -1;
}

static int readData(int fd, char *buffer, int bufferSize) {
	lseek(fd, 0, 0);
	int size = read(fd, buffer, bufferSize);

	if (size > 0) {
		return size;
	}

	return 0;
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeEpoll
 * Method:    epollCreate
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeEpoll_epollCreate(
		JNIEnv * env, jclass clazz) {
	int fd = epoll_create(1);
	if (fd < 0) {
		printf("epoll fd could not be created: %s", strerror(errno));
	}
	return fd;
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeEpoll
 * Method:    addFile
 * Signature: (IILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeEpoll_addFile(
		JNIEnv * env, jclass clazz, jint epollfd, jint opcode, jstring file,
		jint events) {
	char fileName[256];
	int len = (*env)->GetStringLength(env, file);
	(*env)->GetStringUTFRegion(env, file, 0, len, fileName);

	int fd = openWait(fileName);
	if (fd < 0) {
		printf("file could not be opened: %s - error %s", fileName, strerror(errno)); /*do we need continue?*/
	}


	struct epoll_event ev;
	ev.events = events;
	ev.data.fd = fd;

	int epollCtl = epoll_ctl(epollfd, opcode, fd, &ev);
	if (epollCtl < 0) {
		printf("epoll_ctl failed: %s", strerror(errno));
	}

	return fd;
}

JNIEXPORT jint JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeEpoll_removeFile(
		JNIEnv * env, jclass clazz, jint epollfd, jint fd)
{
	int epollCtl = epoll_ctl(epollfd, EPOLL_CTL_DEL, fd, NULL);
	return epollCtl;
}
/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeEpoll
 * Method:    waitForInterrupt
 * Signature: (I)[Lio/silverspoon/bulldog/beagleboneblack/jni/NativePollResult;
 */
JNIEXPORT jobjectArray JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeEpoll_waitForInterrupt(
		JNIEnv * env, jclass clazz, jint epollfd) {

	struct epoll_event *epoll_events;
	int epollReturn = 0;
	int pollSize = 0;
	int arrayIndex = 0;

	jclass nativePollResult = (*env)->FindClass(env,"io/silverspoon/bulldog/linux/jni/NativePollResult");
	jmethodID constructor = (*env)->GetMethodID(env, nativePollResult, "<init>","(II[B)V");
	jobjectArray pollResults;
	jbyteArray fileData;

	pollSize = 1;
	epoll_events = malloc(sizeof(struct epoll_event));
	epollReturn = epoll_wait(epollfd, epoll_events, pollSize, -1);
	if (epollReturn < 0) {
		printf("epoll failed! %s\n", strerror(errno));
		pollResults = (*env)->NewObjectArray(env, 0, nativePollResult, NULL);
		return pollResults;
	}

	pollResults = (*env)->NewObjectArray(env, epollReturn, nativePollResult, NULL);
	for (int i = 0; i < epollReturn; i++) {
		int events = epoll_events->events;
		char buffer[4096] = {'\0'};
		int bytesRead = readData(epoll_events[i].data.fd, buffer, 4096);
		if(bytesRead < 0) {
			fileData = (*env)->NewByteArray(env, 0);
		} else {
			fileData = (*env)->NewByteArray(env, bytesRead);
			jbyte *bytes = (*env)->GetByteArrayElements(env, fileData, NULL);
			memcpy(bytes, buffer, bytesRead);
			(*env)->SetByteArrayRegion(env, fileData, 0, bytesRead, bytes);
		}


		jobject aPollResult = (*env)->NewObject(env, nativePollResult, constructor, epoll_events[i].data.fd, events, fileData);
		(*env)->SetObjectArrayElement(env, pollResults, arrayIndex, aPollResult);
		arrayIndex++;
	}
	free(epoll_events);

	return pollResults;
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeEpoll
 * Method:    stopWait
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeEpoll_stopWait(
		JNIEnv * env, jclass clazz, jint epollfd) {
	 int pipefds[2] = {};
	 struct epoll_event ev = {};
	 pipe(pipefds);
	 int read_pipe = pipefds[0];
	 int write_pipe = pipefds[1];

	 // make read-end non-blocking
	 int flags = fcntl(read_pipe, F_GETFL, 0);
	 fcntl(write_pipe, F_SETFL, flags | O_NONBLOCK);

	 // add the read end to the epoll
	 ev.events = EPOLLIN;
	 ev.data.fd = read_pipe;
	 epoll_ctl(epollfd, EPOLL_CTL_ADD, read_pipe, &ev);

	 char *terminate = "terminate";
	 write(write_pipe, terminate, 1);
}

/*
 * Class:     io_silverspoon_bulldog_linux_jni_NativeEpoll
 * Method:    shutdown
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_io_silverspoon_bulldog_linux_jni_NativeEpoll_shutdown(
		JNIEnv * env, jclass clazz, jint epollfd) {
	close(epollfd);
}

