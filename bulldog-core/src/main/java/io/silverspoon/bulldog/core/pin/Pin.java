/*
 * (C) Copyright 2014 libbulldog (http://libbulldog.org/) and others.
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
 *
 */
package io.silverspoon.bulldog.core.pin;

import io.silverspoon.bulldog.core.event.FeatureActivationEventArgs;
import io.silverspoon.bulldog.core.event.FeatureActivationListener;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>A pin in libbulldog is solely a management container. It contains
 * features that can be activated on it.</p>
 *
 * <p>E.g. one might choose to use the Pin as a {@code DigitalOutput} or a
 * {@code DigitalInput}.</p>
 *
 * <p>The pin contains the necessary information to be set up according
 * to the user's wishes. If the information of this class is not enough,
 * then board implementations derive from it. It ensures that only one
 * feature can be activated at a time. If asynchronous or blocking operations
 * are currently in progress on another feature, then the pin will deny any
 * other feature activation during that time.</p>
 *
 * <p>Mostly, the pin is just used to grab a feature that is available on
 * it:<br/>
 * {@code DigitalOutput output = pin.as(DigitalOutput.class);}<br/>
 * {@code Pwm pwm = pin.as(Pwm.class); }
 * </p>
 */
public class Pin {

   /**
    * The list of features that his pin instance possesses.
    */
   private List<PinFeature> features = new ArrayList<PinFeature>();

   /**
    * The address of the pin.
    */
   private int address = 0;

   /**
    * The name given to the pin Usually
    * a human readable name that is easy to remember.
    */
   private String name = null;

   /**
    * The user can give an alias to the pin,
    * so he can identify the pin by his own name and is
    * not dependent on the name given to it by the system.
    */
   private String alias = null;

   /**
    * The port of the pin.
    */
   private String port = null;

   /**
    * The index of this pin on the port.
    */
   private int indexOnPort = 0;

   /**
    * The feature that is currently active.
    */
   private PinFeature activeFeature = null;

   /**
    * If the pin is blocked an no other feature can be activated,
    * then this variable hold the feature that currently blocks
    * ths pin.
    */
   private PinFeature blocker = null;

   /**
    * A list of activation listeners.
    */
   private List<FeatureActivationListener> activationListeners = new ArrayList<FeatureActivationListener>();

   /**
    * Instantiates a new pin.
    *
    * @param name
    *       the name of the pin
    * @param address
    *       the address of the pin
    * @param port
    *       the port of the pin
    * @param indexOnPort
    *       the index of the pin on the port
    */
   public Pin(String name, int address, String port, int indexOnPort) {
      this.name = name;
      this.address = address;
      this.port = port;
      this.indexOnPort = indexOnPort;
   }

   /**
    * Activates a feature on a pin.
    *
    * @param <T>
    *       the type (classname) of the desired feature
    * @param feature
    *       the type (classname) of the desired feature
    */
   public <T extends PinFeature> void activateFeature(Class<T> feature) {
      checkIfDesiredFeatureIsAvailable(feature);
      T selectedFeature = (T) getFeature(feature);
      activateFeature(selectedFeature);
   }

   /**
    * Adds the feature to the pin. This method is used
    * to provide a pin instance with all the features that
    * it is capable of.
    *
    * @param feature
    *       the instance of the feature to be added
    * @return the pin for a fluent style API
    */
   public Pin addFeature(PinFeature feature) {
      for (PinFeature feat : getFeatures()) {
         if (feat.getClass().equals(feature.getClass())) {
            throw new IllegalArgumentException("This pin already posseses the feature you are trying to add!");
         }
      }

      getFeatures().add(feature);
      return this;
   }

   /**
    * Removes a feature from a pin. More precisely,
    * it removes the first occurrence of a feature class
    * from the pin.
    *
    * @param featureClass
    *       the feature class to be removed
    * @return the pin for a fluent style API
    */
   public Pin removeFeature(Class<? extends PinFeature> featureClass) {
      PinFeature featureInstance = null;
      for (PinFeature feature : getFeatures()) {
         if (feature.getClass().equals(featureClass)) {
            featureInstance = feature;
            break;
         }
      }

      if (featureInstance != null) {
         featureInstance.teardown();
         if (featureInstance == getActiveFeature()) {
            setActiveFeature(null);
         }
         getFeatures().remove(featureInstance);
      }

      return this;
   }

   /**
    * This is a very important method. It is the standard way to configure
    * a pin for usage with a desired feature (e.g. PWM, DigitalIO).
    *
    * @param <T>
    *       the concrete type of the pin feature.
    * @param feature
    *       the classname of the feature.
    * @return the the concrete type of the pin feature.
    */
   public <T extends PinFeature> T as(Class<T> feature) {
      checkIfDesiredFeatureIsAvailable(feature);
      T selectedFeature = getFeature(feature);
      activateFeature(selectedFeature);
      return selectedFeature;
   }

   /**
    * Blocks a pin. While a pin is blocked, not other pin feature
    * can be activated on the pin until it unblocks the pin.
    *
    * See {@link Pin#unblock(PinFeature) unblock}.
    *
    * @param blocker
    *       the pin feature that should block the pin.
    */
   public void block(PinFeature blocker) {
      if (getBlocker() != null && getBlocker() != blocker) {
         throw new PinBlockedException(getBlocker());
      }
      this.blocker = blocker;
   }

   /**
    * Unblocks a pin if it is currently blocked.
    *
    * @param blocker
    *       the feature that currently blocks the pin. If
    *       the pin is blocked, but not by the pin feature given
    *       in the argument, then a PinBlockedException will be
    *       thrown.
    */
   public void unblock(PinFeature blocker) {
      if (getBlocker() != null && getBlocker() != blocker) {
         throw new PinBlockedException(getBlocker());
      }
      this.blocker = null;
   }

   /**
    * Gets the pin feature that currently blocks the pin.
    *
    * @return the pin feature that currently blocks the pin.
    */
   public PinFeature getBlocker() {
      return blocker;
   }

   /**
    * Checks if the pin is currently blocked by a feature.
    *
    * @return true, if is blocked
    */
   public boolean isBlocked() {
      return blocker != null;
   }

   /**
    * Grabs a feature from the pin, if it is available. First, the method
    * will look for a pin feature class that exactly matches the class
    * specified in the argument. If that cannot be found, it returns a
    * pin feature that is at least assignable from the desired feature class.
    *
    * @param <T>
    *       the concrete type of the pin feature
    * @param featureClass
    *       the class of the pin feature
    * @return the feature if it was found or {@code null} otherwise.
    */
   @SuppressWarnings("unchecked")
   public <T extends PinFeature> T getFeature(Class<T> featureClass) {
      PinFeature foundFeature = null;
      for (PinFeature feature : getFeatures()) {
         if (feature.getClass().equals(featureClass)) {
            foundFeature = feature;
            break;
         } else if (foundFeature == null && featureClass.isAssignableFrom(feature.getClass())) {
            foundFeature = feature;
         }
      }

      return (T) foundFeature;
   }

   /**
    * Gets the active feature.
    *
    * @return the feature that is currently active on this pin.
    */
   public PinFeature getActiveFeature() {
      return activeFeature;
   }

   /**
    * Checks if a feature class is currently active on this pin.
    *
    * @param featureClass
    *       the feature class
    * @return true, if the feature class is active on the pin
    */
   public boolean isFeatureActive(Class<? extends PinFeature> featureClass) {
      if (getActiveFeature() == null) {
         return false;
      }
      return featureClass.isAssignableFrom(getActiveFeature().getClass());
   }

   /**
    * Checks if the feature instance is currently active on this pin.
    *
    * @param feature
    *       the feature
    * @return true, if the feature instance is currently active on the pin.
    */
   public boolean isFeatureActive(PinFeature feature) {
      return getActiveFeature() == feature;
   }

   /**
    * Gets the address of the pin.
    *
    * Usually this corresponds with the GPIO pin number
    * of the package or board.
    *
    * @return the address
    */
   public int getAddress() {
      return address;
   }

   /**
    * Gets the port of the pin.
    *
    * @return the port
    */
   public String getPort() {
      return port;
   }

   /**
    * Gets the index of the pin on the port.
    *
    * @return the index of the pin on the port.
    */
   public int getIndexOnPort() {
      return indexOnPort;
   }

   /**
    * Gets a list of all available feature instances.
    *
    * @return all feature instances of this pin.
    */
   public List<PinFeature> getFeatures() {
      return features;
   }

   /**
    * Gets the name of the pin. Usually a combination of port name
    * and pin index.
    *
    * @return the name of the pin.
    */
   public String getName() {
      return name;
   }

   /**
    * Gets the alias of the pin. The alias is an alternate name
    * that can be set by the user. {@link Pin#setAlias(String) setAlias}.
    *
    * @return the alias of the pin.
    */
   public String getAlias() {
      return alias;
   }

   /**
    * Sets the alias of the pin. This is an alternate name
    * that can be set by the user.
    *
    * @param alias
    *       the alternate name of the pin.
    */
   public void setAlias(String alias) {
      this.alias = alias;
   }

   /**
    * Checks if a feature class is available on a pin.
    *
    * @param featureClass
    *       the feature class
    * @return true, if the feature class is available on the pin,
    * false otherwise.
    */
   public boolean hasFeature(Class<? extends PinFeature> featureClass) {
      for (PinFeature feature : getFeatures()) {
         if (featureClass.isAssignableFrom(feature.getClass())) {
            return true;
         }
      }

      return false;
   }

   /**
    * Sets the active feature.
    *
    * @param feature
    *       the new active feature
    */
   private void setActiveFeature(PinFeature feature) {
      activeFeature = feature;
   }

   /**
    * This method activates a feature on the pin.
    * This includes tearing down any active feature (if it is not blocking)
    * and setting up the new feature.
    *
    * It will also trigger the featureActivating and featureActivated events.
    *
    * @param feature
    *       the feature to activate
    */
   private void activateFeature(PinFeature feature) {
      if (feature == getActiveFeature()) {
         return;
      }

      PinFeature blocker = getBlocker();
      if (blocker != null) {
         throw new PinBlockedException(blocker);
      }

      if (getActiveFeature() != null) {
         deactivateFeature(getActiveFeature());
      }

      fireFeatureActivating(feature);
      if (!feature.isSetup()) {
         feature.setup();
      }
      setActiveFeature(feature);
      fireFeatureActivated(feature);
   }

   /**
    * This method deactivates a feature. The feature will be torn down
    * and the {@code activeFeature()} will be set to {@code null}.
    *
    * @param feature
    *       the feature
    */
   private void deactivateFeature(PinFeature feature) {
      fireFeatureDeactivating(feature);
      feature.teardown();
      setActiveFeature(null);
      fireFeatureDeactivated(feature);
   }

   /**
    * Check if a desired feature is available on the pin.
    *
    * @param <T>
    *       the concrete type of the pin feature
    * @param feature
    *       the class of the pin feature
    */
   private <T extends PinFeature> void checkIfDesiredFeatureIsAvailable(Class<T> feature) {
      if (!hasFeature(feature)) {
         throw new IllegalArgumentException("This pin does not possess the desired feature");
      }
   }

   /**
    * Adds a feature activation listener.
    *
    * @param listener
    *       the listener
    */
   public void addFeatureActivationListener(FeatureActivationListener listener) {
      activationListeners.add(listener);
   }

   /**
    * Removes a feature activation listener.
    *
    * @param listener
    *       the listener
    */
   public void removeFeatureActivationListener(FeatureActivationListener listener) {
      activationListeners.remove(listener);
   }

   /**
    * Clears the list of feature activation listeners.
    */
   public void clearFeatureActivationListeners() {
      activationListeners.clear();
   }

   /**
    * Gets a list of all registered {@link FeatureActivationListener
    * FeatureActivationListeners}.
    *
    * @return a list containing all registered
    * {@link FeatureActivationListener FeatureActivationListeners}
    */
   public List<FeatureActivationListener> getFeatureActivationListeners() {
      return activationListeners;
   }

   /**
    * This method fires the feature activating event. It denotes that
    * a feature activation has begun. the feature is not ready for use
    * yet.
    * All registered
    * {@link FeatureActivationListener FeatureActivationListeners} will
    * react on it.
    *
    * @param feature
    *       the feature that is currently being activated
    */
   protected void fireFeatureActivating(PinFeature feature) {
      for (FeatureActivationListener listener : activationListeners) {
         listener.featureActivating(this, new FeatureActivationEventArgs(feature));
      }
   }

   /**
    * This method fires the feature activated event. It denotes that
    * a feature has been fully activated and is ready for use.
    * All registered
    * {@link FeatureActivationListener FeatureActivationListeners} will
    * react on it.
    *
    * @param feature
    *       the feature that is currently being activated
    */
   protected void fireFeatureActivated(PinFeature feature) {
      for (FeatureActivationListener listener : activationListeners) {
         listener.featureActivated(this, new FeatureActivationEventArgs(feature));
      }
   }

   /**
    * This method fires the feature deactivating event. It denotes that
    * a feature deactivation has begun. the feature is not torn down
    * yet.
    * All registered
    * {@link FeatureActivationListener FeatureActivationListeners} will
    * react on it.
    *
    * @param feature
    *       the feature that is currently being activated
    */
   protected void fireFeatureDeactivating(PinFeature feature) {
      for (FeatureActivationListener listener : activationListeners) {
         listener.featureDeactivating(this, new FeatureActivationEventArgs(feature));
      }
   }

   /**
    * This method fires the feature deactivated event. It denotes that
    * a feature deactivation has finished. the feature is completely torn
    * down.
    * All registered
    * {@link FeatureActivationListener FeatureActivationListeners} will
    * react on it.
    *
    * @param feature
    *       the feature that is currently being activated
    */
   protected void fireFeatureDeactivated(PinFeature feature) {
      for (FeatureActivationListener listener : activationListeners) {
         listener.featureDeactivated(this, new FeatureActivationEventArgs(feature));
      }
   }
}
