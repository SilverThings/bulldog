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
package io.silverspoon.bulldog.linux.io;

import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.linux.jni.NativeEpoll;
import io.silverspoon.bulldog.linux.jni.NativePollResult;

import java.util.ArrayList;
import java.util.List;

public class LinuxEpollThread implements Runnable {

   private Thread listenerThread = new Thread(this);
   private boolean running = false;
   private int epollFd = 0;
   private int fileDescriptor = 0;
   private boolean isSetup = false;
   private String filename;
   private List<LinuxEpollListener> listeners = new ArrayList<LinuxEpollListener>();

   public LinuxEpollThread(String filename) {
      listenerThread.setDaemon(true);
      this.filename = filename;
   }

   public void setup() {
      if (!isSetup) {
         epollFd = NativeEpoll.epollCreate();
         fileDescriptor = NativeEpoll.addFile(epollFd, NativeEpoll.EPOLL_CTL_ADD, filename, NativeEpoll.EPOLLPRI | NativeEpoll.EPOLLIN | NativeEpoll.EPOLLET);
         isSetup = true;
      }
   }

   public void start() {
      if (running) {
         return;
      }
      if (listenerThread.isAlive()) {
         return;
      }
      listenerThread = new Thread(this);
      listenerThread.setDaemon(true);
      listenerThread.start();
      running = true;
   }

   public void stop() {
      if (!running) {
         return;
      }
      running = false;
      NativeEpoll.stopWait(epollFd);

      //block until thread is dead
      while (listenerThread.isAlive()) {
         BulldogUtil.sleepMs(10);
      }
   }

   public void run() {
      while (running) {
         NativePollResult[] results = NativeEpoll.waitForInterrupt(epollFd);
         if (results == null) {
            continue;
         }
         fireEpollEvent(results);
      }
   }

   public void teardown() {
      stop();
      if (isSetup) {
         NativeEpoll.removeFile(epollFd, fileDescriptor);
      }
      NativeEpoll.shutdown(epollFd);
      isSetup = false;
   }

   public boolean isRunning() {
      return listenerThread.isAlive();
   }

   public void addListener(LinuxEpollListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(LinuxEpollListener listener) {
      this.listeners.remove(listener);
   }

   public void clearListeners() {
      this.listeners.clear();
   }

   protected void fireEpollEvent(NativePollResult[] results) {
      for (LinuxEpollListener listener : this.listeners) {
         listener.processEpollResults(results);
      }
   }
}
