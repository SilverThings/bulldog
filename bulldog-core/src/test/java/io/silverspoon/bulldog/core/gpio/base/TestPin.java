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
package io.silverspoon.bulldog.core.gpio.base;

import io.silverspoon.bulldog.core.event.FeatureActivationEventArgs;
import io.silverspoon.bulldog.core.event.FeatureActivationListener;
import io.silverspoon.bulldog.core.mocks.MockedPinFeature1;
import io.silverspoon.bulldog.core.mocks.MockedPinFeature2;
import io.silverspoon.bulldog.core.mocks.MockedPinFeature3;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.pin.PinBlockedException;
import io.silverspoon.bulldog.core.pin.PinFeature;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestPin {
	
	private Pin pin;
	private MockedPinFeature1 type1;
	private MockedPinFeature2 type2;
	private MockedPinFeature3 type3;
	
	@Before 
	public void setup() {
		pin = new Pin("Testpin", 0, "A", 0);
		type1 = new MockedPinFeature1(pin);
		type2 = new MockedPinFeature2(pin);
		type3 = new MockedPinFeature3(pin);
		
		pin.addFeature(type3)
		   .addFeature(type2)
		   .addFeature(type1);
	}
	
	@Test
	public void testFeatureActivation() {
		TestCase.assertFalse(pin.isFeatureActive(MockedPinFeature1.class));
		TestCase.assertFalse(pin.isFeatureActive(MockedPinFeature2.class));
		TestCase.assertFalse(pin.isFeatureActive(MockedPinFeature3.class));
		
		pin.activateFeature(MockedPinFeature2.class);
		PinFeature feature = pin.getActiveFeature();
		TestCase.assertTrue(feature.isSetup());
		TestCase.assertEquals(type2, feature);
		TestCase.assertFalse(pin.isFeatureActive(MockedPinFeature3.class));
		
		pin.activateFeature(MockedPinFeature3.class);
		feature = pin.getActiveFeature();
		TestCase.assertEquals(type3, feature);
		TestCase.assertTrue(pin.isFeatureActive(MockedPinFeature3.class));
		TestCase.assertTrue(pin.isFeatureActive(MockedPinFeature2.class));	//Type 3 is subclass of Type 2
		TestCase.assertTrue(pin.isFeatureActive(pin.getFeature(MockedPinFeature3.class)));
		TestCase.assertFalse(pin.isFeatureActive(pin.getFeature(MockedPinFeature2.class)));
		
		feature = pin.as(MockedPinFeature2.class);
		TestCase.assertEquals(type2, feature);
	
		feature = pin.as(MockedPinFeature3.class);
		TestCase.assertEquals(type3, feature);
		feature = pin.getActiveFeature();
		TestCase.assertEquals(type3, feature);
		
		pin.removeFeature(MockedPinFeature2.class);
		TestCase.assertEquals(2, pin.getFeatures().size());
		feature = pin.as(MockedPinFeature2.class);  //type3 is still present and extends type2
		TestCase.assertEquals(type3, feature);
		
		pin.removeFeature(MockedPinFeature2.class);
	}
	
	@Test
	public void testAddRemoveFeature() {
		
		try {
			pin.addFeature(type3);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
		
		TestCase.assertTrue(pin.hasFeature(MockedPinFeature2.class));
		TestCase.assertEquals(3, pin.getFeatures().size());
		pin.removeFeature(MockedPinFeature2.class);
		TestCase.assertEquals(2, pin.getFeatures().size());
		TestCase.assertTrue(pin.hasFeature(MockedPinFeature2.class)); //type 3 inherits and is still present
		pin.removeFeature(MockedPinFeature2.class);
		TestCase.assertEquals(2, pin.getFeatures().size());
		pin.removeFeature(MockedPinFeature3.class);
		TestCase.assertEquals(1, pin.getFeatures().size());
		TestCase.assertFalse(pin.hasFeature(MockedPinFeature3.class)); 
		
		PinFeature feature = pin.as(MockedPinFeature1.class);
		TestCase.assertEquals(pin.getActiveFeature(), feature);
		pin.removeFeature(MockedPinFeature1.class);
		TestCase.assertNull(pin.getActiveFeature());
		
		try {
			pin.as(MockedPinFeature1.class);
			TestCase.fail();
		} catch(IllegalArgumentException ex) {}
	}
		
	@Test
	public void testBlocking() {
		
		PinFeature feature = pin.as(MockedPinFeature1.class);
		type1.blockPin();
		TestCase.assertEquals(feature, pin.getBlocker());
		TestCase.assertTrue(pin.isBlocked());
		
		type1.blockPin(); //Should have no effect;
		
		try {
			type2.blockPin();
			TestCase.fail();
		} catch(PinBlockedException ex) {
			TestCase.assertEquals(feature, ex.getBlocker());
		}
		
		try {
			feature = pin.as(MockedPinFeature2.class);
			TestCase.fail();
		} catch(PinBlockedException ex) {
			TestCase.assertEquals(pin.getBlocker(), ex.getBlocker());
		}
		
		try {
			type2.unblockPin();
			TestCase.fail();
		} catch(PinBlockedException ex) {
			TestCase.assertEquals(feature, ex.getBlocker());
		}
		
		
		type1.unblockPin();
		
		type1.unblockPin();  //Should have no effect
		type2.unblockPin();  //Should have no effect
		
		TestCase.assertNull(pin.getBlocker());
		TestCase.assertFalse(pin.isBlocked());
		
	}
	
	@Test
	public void testFeatureActivationListeners() {
		final String listenerActivating = "LISTENER%s_ACTIVATING";
		final String listenerActivated = "LISTENER%s_ACTIVATED";
		final String listenerDeactivating = "LISTENER%s_DEACTIVATING";
		final String listenerDeactivated = "LISTENER%s_DEACTIVATED";
		final List<String> listenerOutput = new ArrayList<String>();
		pin.addFeatureActivationListener(new FeatureActivationListener() {

			int counter = 0;
			
			@Override
			public void featureActivating(Object o, FeatureActivationEventArgs args) {
				listenerOutput.add(String.format(listenerActivating, "1"));
				if(counter == 0) {
					TestCase.assertEquals(MockedPinFeature1.class, args.getPinFeature().getClass());
				} else {
					TestCase.assertEquals(MockedPinFeature2.class, args.getPinFeature().getClass());
				}
				counter++;
			}

			@Override
			public void featureActivated(Object o, FeatureActivationEventArgs args) {
				listenerOutput.add(String.format(listenerActivated, "1"));
				
			}

			@Override
			public void featureDeactivating(Object o, FeatureActivationEventArgs args) {
				listenerOutput.add(String.format(listenerDeactivating, "1"));
			}

			@Override
			public void featureDeactivated(Object o, FeatureActivationEventArgs args) {
				listenerOutput.add(String.format(listenerDeactivated, "1"));
			}
			
		});
		
		pin.addFeatureActivationListener(new FeatureActivationListener() {

			@Override
			public void featureActivating(Object o, FeatureActivationEventArgs args) {
				listenerOutput.add(String.format(listenerActivating, "2"));
			}

			@Override
			public void featureActivated(Object o, FeatureActivationEventArgs args) {
				listenerOutput.add(String.format(listenerActivated, "2"));
				
			}

			@Override
			public void featureDeactivating(Object o, FeatureActivationEventArgs args) {
				listenerOutput.add(String.format(listenerDeactivating, "2"));
			}

			@Override
			public void featureDeactivated(Object o, FeatureActivationEventArgs args) {
				listenerOutput.add(String.format(listenerDeactivated, "2"));
			}
			
		});
		
		TestCase.assertEquals(2, pin.getFeatureActivationListeners().size());
		
		pin.activateFeature(MockedPinFeature1.class);
		pin.activateFeature(MockedPinFeature1.class); //does not cause new activation
		pin.activateFeature(MockedPinFeature2.class);
		
		TestCase.assertEquals(String.format(listenerActivating, "1"), listenerOutput.get(0));
		TestCase.assertEquals(String.format(listenerActivating, "2"), listenerOutput.get(1));
		TestCase.assertEquals(String.format(listenerActivated, "1"), listenerOutput.get(2));
		TestCase.assertEquals(String.format(listenerActivated, "2"), listenerOutput.get(3));
		TestCase.assertEquals(String.format(listenerDeactivating, "1"), listenerOutput.get(4));
		TestCase.assertEquals(String.format(listenerDeactivating, "2"), listenerOutput.get(5));
		TestCase.assertEquals(String.format(listenerDeactivated, "1"), listenerOutput.get(6));
		TestCase.assertEquals(String.format(listenerDeactivated, "2"), listenerOutput.get(7));
		TestCase.assertEquals(String.format(listenerActivating, "1"), listenerOutput.get(8));
		TestCase.assertEquals(String.format(listenerActivating, "2"), listenerOutput.get(9));
		TestCase.assertEquals(String.format(listenerActivated, "1"), listenerOutput.get(10));
		TestCase.assertEquals(String.format(listenerActivated, "2"), listenerOutput.get(11));
		
		
		pin.removeFeatureActivationListener(pin.getFeatureActivationListeners().get(0));
		TestCase.assertEquals(1, pin.getFeatureActivationListeners().size());
		pin.clearFeatureActivationListeners();
		TestCase.assertEquals(0, pin.getFeatureActivationListeners().size());
	}
}
