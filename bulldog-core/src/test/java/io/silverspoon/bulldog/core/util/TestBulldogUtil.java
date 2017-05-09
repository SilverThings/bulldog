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
package io.silverspoon.bulldog.core.util;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;

public class TestBulldogUtil {

	@Test
	public void testSleep() {
		long start = System.currentTimeMillis();
		BulldogUtil.sleepMs(10);
		long delta = System.currentTimeMillis() - start;
		TestCase.assertEquals(10, delta, 5);
		
		start = System.currentTimeMillis();
		BulldogUtil.sleepMs(1);
		delta = System.currentTimeMillis() - start;
		TestCase.assertEquals(1, delta, 5);
		
		start = System.currentTimeMillis();
		BulldogUtil.sleepMs(100);
		delta = System.currentTimeMillis() - start;
		TestCase.assertEquals(100, delta, 5);
		
		start = System.currentTimeMillis();
		BulldogUtil.sleepMs(500);
		delta = System.currentTimeMillis() - start;
		TestCase.assertEquals(500, delta, 5);
		
		start = System.currentTimeMillis();
		BulldogUtil.sleepMs(25);
		delta = System.currentTimeMillis() - start;
		TestCase.assertEquals(25, delta, 5);
	}
	
	@Test
	public void testSleepNs() {
		// This is hardly testable and won't be precise anyway.
		// The method call alone will change results drastically
	}
	
	@Test 
	public void testBytesToString() throws UnsupportedEncodingException {
		String string = BulldogUtil.bytesToString("Hello World".getBytes("ASCII"));
		TestCase.assertEquals("Hello World", string);
		
		string = BulldogUtil.bytesToString("Hello World".getBytes());
		TestCase.assertEquals("Hello World", string);
		
		string = BulldogUtil.bytesToString(new byte[] {});
		TestCase.assertNull(string);
		
		try {
			string = BulldogUtil.bytesToString(null);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {  }
		
		try {
			string = BulldogUtil.bytesToString("Hello World".getBytes("ASCII"), "NON_EXISTENT_ENCODING");
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("Unknown encoding", ex.getMessage());
		}
	}
	
	@Test
	public void testStreamToString() throws IOException {
		String text = "Hello World\n\tasdasdasdas";
		String text2 = "Han Solo is the best star wars character";
		File file = File.createTempFile("TEST", ".txt");
		file.deleteOnExit();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(text);
		writer.flush();
		FileInputStream inputStream = new FileInputStream(file);
		String readString = BulldogUtil.convertStreamToString(inputStream);
		TestCase.assertEquals(text, readString);
		
		readString = BulldogUtil.convertStreamToString(inputStream);
		TestCase.assertEquals("", readString);
		
		writer.write(text2);
		writer.flush();
		
		readString = BulldogUtil.convertStreamToString(inputStream);
		TestCase.assertEquals(text2, readString);
		
		inputStream.close();
		
		readString = BulldogUtil.convertStreamToString(inputStream);
		TestCase.assertEquals("", readString);
		
		writer.close();
	}
	
	@Test
	public void testReadFileAsString() throws IOException { 
		String text = "Hello World\n\tasdasdasdas";
		String text2 = "Han Solo is the best star wars character";
		File file = File.createTempFile("TEST", ".txt");
		file.deleteOnExit();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(text);
		writer.flush();
		String readString = BulldogUtil.readFileAsString(file.getAbsolutePath());
		TestCase.assertEquals(text, readString);
		
		readString = BulldogUtil.readFileAsString("DOES NOT EXIST");
		TestCase.assertNull(readString);;
		
		writer.write(text2);
		writer.flush();
		
		readString = BulldogUtil.readFileAsString(file.getAbsolutePath());
		TestCase.assertEquals(text + text2, readString);
				

		writer.close();
	}
}
