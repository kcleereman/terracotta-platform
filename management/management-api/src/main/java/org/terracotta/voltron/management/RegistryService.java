/*
 * Copyright Terracotta, Inc.
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
 */
package org.terracotta.voltron.management;

import org.terracotta.voltron.management.consumer.CapabilityConsumer;
import org.terracotta.voltron.management.consumer.ProviderConsumer;
import org.terracotta.voltron.management.producer.RegistryProducer;

/**
 * Registry service provided for managed entities to publish their managed
 * objects and capabilities and for management entities to discover these
 * capabilities.
 *
 * @author RKAV
 */
public interface RegistryService {
  /**
   * Get a Management Registry producer that allows managed entities to
   * put themselves in the registry and publish their capabilities.
   * <p>
   * Typically there is only one registry producer for the management service.
   *
   * @return A registry producer that provides managed entities to put themselves in
   *         a registry.
   */
  RegistryProducer getRegistryProducer();

  /**
   * Allows consumers (typically management entities) to understand all the
   * capabilities of the registered managed entities.
   *
   * @return A capability consumer that can be used to discover capabilities of
   *         the registered managed entities.
   */
  CapabilityConsumer getCapabilityConsumer();


  /**
   * Allows consumers (typically management entities) to access the
   * provider interfaces of the managed entities.
   *
   * @return A provider consumer that can be used to access the provider
   *         interfaces of various registered entities and their registered
   *         objects.
   */
  ProviderConsumer getProviderConsumer();
}
