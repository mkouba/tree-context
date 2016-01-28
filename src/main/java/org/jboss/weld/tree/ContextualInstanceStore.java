/*
 * Copyright 2013 Martin Kouba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.tree;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Vetoed;

/**
 * Maps contextuals to instances for a certain thread.
 *
 * @author Martin Kouba
 */
@Vetoed
final class ContextualInstanceStore {

    private final AtomicBoolean isActive;

    private final ConcurrentMap<Contextual<?>, ContextualInstance<?>> instancesMap;

    ContextualInstanceStore() {
        this.isActive = new AtomicBoolean(true);
        this.instancesMap = new ConcurrentHashMap<Contextual<?>, ContextualInstance<?>>();
    }

    @SuppressWarnings("unchecked")
    <T> ContextualInstance<T> get(Contextual<T> contextual, CreationalContext<T> creationalContext) {

        ContextualInstance<T> contextualInstance = (ContextualInstance<T>) instancesMap.get(contextual);

        if (contextualInstance == null && creationalContext != null) {
            contextualInstance = new ContextualInstance<T>(contextual.create(creationalContext), creationalContext, contextual);
            instancesMap.put(contextual, contextualInstance);
        }
        return contextualInstance;
    }

    boolean isActive() {
        return isActive.get();
    }

    void deactivate() {
        isActive.set(false);
    }

    void destroy() {
        for (ContextualInstance<?> contextualInstance : instancesMap.values()) {
            contextualInstance.destroy();
        }
        instancesMap.clear();
    }

}
