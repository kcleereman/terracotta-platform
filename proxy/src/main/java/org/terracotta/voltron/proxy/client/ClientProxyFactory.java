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
package org.terracotta.voltron.proxy.client;

import org.terracotta.connection.entity.Entity;
import org.terracotta.entity.EntityClientEndpoint;
import org.terracotta.voltron.proxy.Codec;
import org.terracotta.voltron.proxy.SerializationCodec;
import org.terracotta.voltron.proxy.client.messages.ServerMessageAware;
import org.terracotta.voltron.proxy.server.messages.ProxyEntityMessage;
import org.terracotta.voltron.proxy.server.messages.ProxyEntityResponse;

import java.lang.reflect.Proxy;

import static org.terracotta.voltron.proxy.CommonProxyFactory.createMethodMappings;
import static org.terracotta.voltron.proxy.CommonProxyFactory.createResponseTypeMappings;
import static org.terracotta.voltron.proxy.CommonProxyFactory.invert;

/**
 * @author Alex Snaps
 */
public class ClientProxyFactory {

  public static <T extends Entity & ServerMessageAware<?>> T createEntityProxy(Class<T> clientType, Class<? super T> type,
                                                                               EntityClientEndpoint<ProxyEntityMessage, ProxyEntityResponse> entityClientEndpoint,
                                                                               Class<?>... messageTypes) {
    return createProxy(clientType, type, entityClientEndpoint, new SerializationCodec(), messageTypes);
  }

  public static <T extends Entity & ServerMessageAware<?>> T createEntityProxy(Class<T> clientType, Class<? super T> type,
                                                                               EntityClientEndpoint<ProxyEntityMessage, ProxyEntityResponse> entityClientEndpoint,
                                                                               final Codec codec, Class<?> messageType,
                                                                               Class<?>... messageTypes) {
    return createProxy(clientType, type, entityClientEndpoint, codec, sum(messageType, messageTypes));
  }

  public static <T extends Entity> T createEntityProxy(Class<T> clientType, Class<? super T> type,
                                                       EntityClientEndpoint<ProxyEntityMessage, ProxyEntityResponse> entityClientEndpoint) {
    return createProxy(clientType, type, entityClientEndpoint);
  }

  public static <T extends Entity> T createEntityProxy(Class<T> clientType, Class<? super T> type,
                                                       EntityClientEndpoint<ProxyEntityMessage, ProxyEntityResponse> entityClientEndpoint, final Codec codec) {
    return createProxy(clientType, type, entityClientEndpoint, codec);
  }

  public static <T> T createProxy(Class<T> clientType, Class<? super T> type, EntityClientEndpoint<ProxyEntityMessage, ProxyEntityResponse> entityClientEndpoint) {
    return createProxy(clientType, type, entityClientEndpoint, new SerializationCodec());
  }

  public static <T> T createProxy(Class<T> clientType, Class<? super T> type, EntityClientEndpoint<ProxyEntityMessage, ProxyEntityResponse> entityClientEndpoint,
                                  final Codec codec, Class... messageTypes) {
    
    if (entityClientEndpoint == null) {
      throw new NullPointerException("EntityClientEndpoint has to be provided!");
    }

    if (!type.isInterface()) {
      throw new IllegalArgumentException("We only proxy interfaces!");
    }

    final Class[] interfaces;
    if (messageTypes.length == 0) {
      interfaces = new Class[] { clientType, Entity.class };
    } else {
      interfaces = new Class[] { clientType, Entity.class, ServerMessageAware.class };
    }
    return clientType.cast(Proxy.newProxyInstance(Entity.class.getClassLoader(), interfaces,
        new VoltronProxyInvocationHandler(
                entityClientEndpoint,
                invert(createResponseTypeMappings(type, messageTypes)).values()
        )
    ));
  }

  private static Class<?>[] sum(Class<?> one, Class<?>[] others) {
    Class<?>[] result = new Class<?>[others.length + 1];
    result[0] = one;
    System.arraycopy(others, 0, result, 1, others.length);
    return result;
  }

}
