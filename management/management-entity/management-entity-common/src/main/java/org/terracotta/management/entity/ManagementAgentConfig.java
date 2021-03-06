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
package org.terracotta.management.entity;

import java.io.Serializable;

/**
 * @author Mathieu Carbou
 */
public final class ManagementAgentConfig implements Serializable {

  private static final long serialVersionUID = 3;

  // name must be hardcoded because it reference a class name in client package and is used on server-side
  public static final String ENTITY_TYPE = "org.terracotta.management.entity.client.ManagementAgentEntity";

  private int maximumUnreadClientNotifications = 1024 * 1024;
  private int maximumUnreadClientStatistics = 1024 * 1024;

  public ManagementAgentConfig setMaximumUnreadClientNotifications(int maximumUnreadClientNotifications) {
    this.maximumUnreadClientNotifications = maximumUnreadClientNotifications;
    return this;
  }

  public int getMaximumUnreadClientNotifications() {
    return maximumUnreadClientNotifications;
  }

  public int getMaximumUnreadClientStatistics() {
    return maximumUnreadClientStatistics;
  }

  public ManagementAgentConfig setMaximumUnreadClientStatistics(int maximumUnreadClientStatistics) {
    this.maximumUnreadClientStatistics = maximumUnreadClientStatistics;
    return this;
  }

}
