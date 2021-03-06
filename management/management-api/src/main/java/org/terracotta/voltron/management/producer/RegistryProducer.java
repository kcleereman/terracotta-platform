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
package org.terracotta.voltron.management.producer;

import org.terracotta.entity.ActiveServerEntity;
import org.terracotta.voltron.management.producer.registry.ManagedEntityRegistry;

/**
 * Provided Registry Producer side interface of the management service. Entities that wishes to
 * become managed entities must use this interface to register with the management service before
 * starting to push metrics and events and/or register providers for turning statistics on or off or
 * to do some specific action on a managed object within a managed entity.
 *
 * @author RKAV
 */
public interface RegistryProducer {
  /**
   * Given an {@code entityType} and {@code entityName}, return either an existing {@code MangedEntityRegistry} for
   * the passed in unique {@code entityName} and {@code entityType}.
   * <p>
   * If the entity register already exists, it returns the entity register instance that is found for
   * the {@code entityType} and {@code entityName} combination. Otherwise, it will atomically create a new entity
   * register instance and returns it.
   *
   * @param entityType Type of entity.
   * @param entityName Name of the entity, must uniquely identify a entity instance within a given {@code entityType}.
   * @param <E> The entity type.
   * @return created or looked up entity registry for the given entity instance.
   */
  <E extends ActiveServerEntity<?, ?>> ManagedEntityRegistry<E> createOrLookupManagedEntityRegistry(Class<E> entityType, String entityName);

  /**
   * Given an {@code entityType} and {@code entityName}, atomically remove the corresponding entity registry entry and
   * return it to the caller.
   *
   * @param entityType Type of the entity.
   * @param entityName Name of the entity.
   * @param <E> Managed Entity type.
   * @return Removed entity register.
   */
  <E extends ActiveServerEntity<?, ?>> ManagedEntityRegistry<E> removeFromEntityRegistry(Class<E> entityType, String entityName);
}
