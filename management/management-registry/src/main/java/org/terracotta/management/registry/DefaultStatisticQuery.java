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
package org.terracotta.management.registry;

import org.terracotta.management.model.context.Context;
import org.terracotta.management.model.stats.ContextualStatistics;
import org.terracotta.management.model.stats.Statistic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author Mathieu Carbou
 */
public class DefaultStatisticQuery implements StatisticQuery {

  private final CapabilityManagementSupport capabilityManagement;
  private final String capabilityName;
  private final Collection<String> statisticNames;
  private final Collection<Context> contexts;
  private final long since;

  public DefaultStatisticQuery(CapabilityManagementSupport capabilityManagement, String capabilityName, Collection<String> statisticNames, Collection<Context> contexts, long since) {
    this.capabilityManagement = capabilityManagement;
    this.capabilityName = capabilityName;
    this.statisticNames = Collections.unmodifiableSet(new LinkedHashSet<String>(statisticNames));
    this.since = since;
    this.contexts = Collections.unmodifiableCollection(new ArrayList<Context>(contexts));
  }

  @Override
  public String getCapabilityName() {
    return capabilityName;
  }

  @Override
  public Collection<Context> getContexts() {
    return contexts;
  }

  @Override
  public Collection<String> getStatisticNames() {
    return statisticNames;
  }

  @Override
  public long getSince() {
    return since;
  }

  @Override
  public ResultSet<ContextualStatistics> execute() {
    Map<Context, ContextualStatistics> contextualStatistics = new LinkedHashMap<Context, ContextualStatistics>(contexts.size());
    Collection<ManagementProvider<?>> managementProviders = capabilityManagement.getManagementProvidersByCapability(capabilityName);

    for (Context context : contexts) {
      Map<String, Statistic<?, ?>> statistics = new HashMap<String, Statistic<?, ?>>();
      for (ManagementProvider<?> managementProvider : managementProviders) {
        if (managementProvider.supports(context)) {
          statistics.putAll(managementProvider.collectStatistics(context, statisticNames, since));
        }
      }
      contextualStatistics.put(context, new ContextualStatistics(capabilityName, context, statistics));
    }

    return new DefaultResultSet<ContextualStatistics>(contextualStatistics);
  }

}
