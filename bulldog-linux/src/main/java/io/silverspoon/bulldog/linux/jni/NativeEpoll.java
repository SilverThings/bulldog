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
package io.silverspoon.bulldog.linux.jni;

public class NativeEpoll {

   public static final int EPOLLIN = 0x001;
   public static final int EPOLLPRI = 0x002;
   public static final int EPOLLOUT = 0x004;
   public static final int EPOLLRDNORM = 0x040;
   public static final int EPOLLRDBAND = 0x080;
   public static final int EPOLLWRNORM = 0x100;
   public static final int EPOLLWRBAND = 0x200;
   public static final int EPOLLMSG = 0x400;
   public static final int EPOLLERR = 0x008;
   public static final int EPOLLHUP = 0x010;
   public static final int EPOLLRDHUP = 0x2000;
   public static final int EPOLLWAKEUP = 1 << 29;
   public static final int EPOLLONESHOT = 1 << 30;
   public static final int EPOLLET = 1 << 31;
   public static final int EPOLL_CTL_ADD = 1;
   public static final int EPOLL_CTL_DEL = 2;
   public static final int EPOLL_CTL_MOD = 3;

   public static native int epollCreate();

   public static native int addFile(int epfd, int opcode, String filename, int events);

   public static native int removeFile(int epfd, int fd);

   public static native NativePollResult[] waitForInterrupt(int epfd);

   public static native void stopWait(int epfd);

   public static native void shutdown(int epfd);
}

