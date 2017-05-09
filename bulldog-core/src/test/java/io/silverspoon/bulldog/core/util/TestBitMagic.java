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

public class TestBitMagic {

	@Test
	public void testIntegerBitManipulations() {
		int testValue = 0b00000000;
		testValue = BitMagic.setBit(testValue, 0, 1);
		TestCase.assertEquals(0b00000001, testValue);
		testValue = BitMagic.setBit(testValue, 1, 1);
		TestCase.assertEquals(0b00000011, testValue);
		testValue = BitMagic.setBit(testValue, 0, 0);
		TestCase.assertEquals(0b00000010, testValue);
		testValue = BitMagic.toggleBit(testValue, 2);
		TestCase.assertEquals(0b00000110, testValue);
		testValue = BitMagic.toggleBit(testValue, 1);
		TestCase.assertEquals(0b00000100, testValue);
		
		testValue = 0b101010;
		for(int i = 0; i < 6; i++) {
			if(i%2 == 0) {
				TestCase.assertEquals(0, BitMagic.getBit(testValue, i));
				TestCase.assertFalse(BitMagic.isBitSet(testValue, i));
			} else {
				TestCase.assertEquals(1, BitMagic.getBit(testValue, i));
				TestCase.assertTrue(BitMagic.isBitSet(testValue, i));
			}
		}
		
		testValue = BitMagic.setBit(testValue, 31, 1);
		try {
			testValue = BitMagic.setBit(testValue, 32, 1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("There are only 32 bits (bit 0 to 31) in this type. Invalid bit position", ex.getMessage());
		}
		
		try {
			testValue = BitMagic.setBit(testValue, -1, 1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("There are only 32 bits (bit 0 to 31) in this type. Invalid bit position", ex.getMessage());
		}
		
		try {
			testValue = BitMagic.setBit(testValue, 0, -1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			testValue = BitMagic.setBit(testValue, 0, 2);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	public void testShortBitManipulations() {
		short testValue = 0b00000000;
		testValue = BitMagic.setBit(testValue, 0, 1);
		TestCase.assertEquals(0b00000001, testValue);
		testValue = BitMagic.setBit(testValue, 1, 1);
		TestCase.assertEquals(0b00000011, testValue);
		testValue = BitMagic.setBit(testValue, 0, 0);
		TestCase.assertEquals(0b00000010, testValue);
		testValue = BitMagic.toggleBit(testValue, 2);
		TestCase.assertEquals(0b00000110, testValue);
		testValue = BitMagic.toggleBit(testValue, 1);
		TestCase.assertEquals(0b00000100, testValue);
		
		testValue = 0b101010;
		for(int i = 0; i < 6; i++) {
			if(i%2 == 0) {
				TestCase.assertEquals(0, BitMagic.getBit(testValue, i));
				TestCase.assertFalse(BitMagic.isBitSet(testValue, i));
			} else {
				TestCase.assertEquals(1, BitMagic.getBit(testValue, i));
				TestCase.assertTrue(BitMagic.isBitSet(testValue, i));
			}
		}
		
		testValue = BitMagic.setBit(testValue, 15, 1);
		try {
			testValue = BitMagic.setBit(testValue, 16, 1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("There are only 16 bits (bit 0 to 15) in this type. Invalid bit position", ex.getMessage());
		}
		
		try {
			testValue = BitMagic.setBit(testValue, -1, 1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("There are only 16 bits (bit 0 to 15) in this type. Invalid bit position", ex.getMessage());
		}
		
		try {
			testValue = BitMagic.setBit(testValue, 0, -1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			testValue = BitMagic.setBit(testValue, 0, 2);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
	}
	
	@Test
	public void testByteBitManipulations() {
		byte testValue = 0b00000000;
		testValue = BitMagic.setBit(testValue, 0, 1);
		TestCase.assertEquals(0b00000001, testValue);
		testValue = BitMagic.setBit(testValue, 1, 1);
		TestCase.assertEquals(0b00000011, testValue);
		testValue = BitMagic.setBit(testValue, 0, 0);
		TestCase.assertEquals(0b00000010, testValue);
		testValue = BitMagic.toggleBit(testValue, 2);
		TestCase.assertEquals(0b00000110, testValue);
		testValue = BitMagic.toggleBit(testValue, 1);
		TestCase.assertEquals(0b00000100, testValue);
		
		testValue = 0b101010;
		for(int i = 0; i < 6; i++) {
			if(i%2 == 0) {
				TestCase.assertEquals(0, BitMagic.getBit(testValue, i));
				TestCase.assertFalse(BitMagic.isBitSet(testValue, i));
			} else {
				TestCase.assertEquals(1, BitMagic.getBit(testValue, i));
				TestCase.assertTrue(BitMagic.isBitSet(testValue, i));
			}
		}
		
		testValue = BitMagic.setBit(testValue, 7, 1);
		try {
			testValue = BitMagic.setBit(testValue, 8, 1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("There are only 8 bits (bit 0 to 7) in this type. Invalid bit position", ex.getMessage());
		}
		
		try {
			testValue = BitMagic.setBit(testValue, -1, 1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("There are only 8 bits (bit 0 to 7) in this type. Invalid bit position", ex.getMessage());
		}
		
		try {
			testValue = BitMagic.setBit(testValue, 0, -1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			testValue = BitMagic.setBit(testValue, 0, 2);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
	}
	
	@Test
	public void testLongBitManipulations() {
		long testValue = 0b00000000;
		testValue = BitMagic.setBit(testValue, 0, 1);
		TestCase.assertEquals(0b00000001, testValue);
		testValue = BitMagic.setBit(testValue, 1, 1);
		TestCase.assertEquals(0b00000011, testValue);
		testValue = BitMagic.setBit(testValue, 0, 0);
		TestCase.assertEquals(0b00000010, testValue);
		testValue = BitMagic.toggleBit(testValue, 2);
		TestCase.assertEquals(0b00000110, testValue);
		testValue = BitMagic.toggleBit(testValue, 1);
		TestCase.assertEquals(0b00000100, testValue);
		
		testValue = 0b101010;
		for(int i = 0; i < 6; i++) {
			if(i%2 == 0) {
				TestCase.assertEquals(0, BitMagic.getBit(testValue, i));
				TestCase.assertFalse(BitMagic.isBitSet(testValue, i));
			} else {
				TestCase.assertEquals(1, BitMagic.getBit(testValue, i));
				TestCase.assertTrue(BitMagic.isBitSet(testValue, i));
			}
		}
		
		testValue = BitMagic.setBit(testValue, 63, 1);
		try {
			testValue = BitMagic.setBit(testValue, 64, 1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("There are only 64 bits (bit 0 to 63) in this type. Invalid bit position", ex.getMessage());
		}
		
		try {
			testValue = BitMagic.setBit(testValue, -1, 1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {
			TestCase.assertEquals("There are only 64 bits (bit 0 to 63) in this type. Invalid bit position", ex.getMessage());
		}
		
		try {
			testValue = BitMagic.setBit(testValue, 0, -1);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		try {
			testValue = BitMagic.setBit(testValue, 0, 2);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
	}
	
	@Test
	public void testNibbles() {
		byte testByte = (byte)0xFA;
		TestCase.assertEquals(0x0A, BitMagic.getLowerNibble(testByte));
		TestCase.assertEquals(0x0F, BitMagic.getUpperNibble(testByte));
		
		testByte = (byte)0x0A;
		TestCase.assertEquals(0x0A, BitMagic.getLowerNibble(testByte));
		TestCase.assertEquals(0x00, BitMagic.getUpperNibble(testByte));
		
		testByte = (byte)0x00;
		TestCase.assertEquals(0x00, BitMagic.getLowerNibble(testByte));
		TestCase.assertEquals(0x00, BitMagic.getUpperNibble(testByte));
		
		testByte = (byte)0xFF;
		TestCase.assertEquals(0x0F, BitMagic.getLowerNibble(testByte));
		TestCase.assertEquals(0x0F, BitMagic.getUpperNibble(testByte));
		
		TestCase.assertEquals((byte)0xFF, BitMagic.byteFromNibbles(0x0F, 0x0F));
		TestCase.assertEquals((byte)0xAB, BitMagic.byteFromNibbles(0x0A, 0x0B));
	}
	
	@Test
	public void testReversing() {
		TestCase.assertEquals((byte)0b10000000, BitMagic.reverse((byte)0b00000001));
		TestCase.assertEquals((byte)0b11000000, BitMagic.reverse((byte)0b00000011));
		TestCase.assertEquals((byte)0b10001000, BitMagic.reverse((byte)0b00010001));
		TestCase.assertEquals((byte)0x00, BitMagic.reverse((byte)0x00));
		TestCase.assertEquals((byte)0xFF, BitMagic.reverse((byte)0xFF));
	}
	
	@Test
	public void testBitToString() {
		TestCase.assertEquals("0b00000000", BitMagic.toBitString((byte)0b00000000));
		TestCase.assertEquals("0b00100010", BitMagic.toBitString((byte)0b00100010));
		TestCase.assertEquals("0b11111111", BitMagic.toBitString((byte)0b11111111));
		TestCase.assertEquals("0b0000000000000000", BitMagic.toBitString((short)0b00000000));
		TestCase.assertEquals("0b0000000000100010", BitMagic.toBitString((short)0b00100010));
		TestCase.assertEquals("0b0000000011111111", BitMagic.toBitString((short)0b11111111));
		TestCase.assertEquals("0b00000000000000000000000000000000", BitMagic.toBitString(0b00000000));
		TestCase.assertEquals("0b00000000000000000000000000100010", BitMagic.toBitString(0b00100010));
		TestCase.assertEquals("0b00000000000000000000000011111111", BitMagic.toBitString(0b11111111));
		TestCase.assertEquals("0b0000000000000000000000000000000000000000000000000000000000000000", BitMagic.toBitString(0b00000000L));
		TestCase.assertEquals("0b0000000000000000000000000000000000000000000000000000000000100010", BitMagic.toBitString(0b00100010L));
		TestCase.assertEquals("0b0000000000000000000000000000000000000000000000000000000011111111", BitMagic.toBitString(0b11111111L));
	}
}
