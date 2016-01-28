/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.tree;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Vetoed;

/**
 *
 * @author Martin Kouba
 */
@Vetoed
public class TreeContext implements Context {

    private final ThreadLocal<ContextualInstanceStore> contextualInstanceStore = new ThreadLocal<ContextualInstanceStore>();

    @Override
    public Class<? extends Annotation> getScope() {
        return TreeDependent.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {

        ContextualInstanceStore store = contextualInstanceStore.get();

        if (store == null) {
            throw new ContextNotActiveException();
        }

        ContextualInstance<T> contextualInstance = store.get(contextual, creationalContext);

        if (contextualInstance != null) {
            return contextualInstance.getInstance();
        }
        return null;
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    @Override
    public boolean isActive() {
        ContextualInstanceStore store = contextualInstanceStore.get();
        return store != null && store.isActive();
    }

    void activate() {
        if (isActive()) {
            throw new IllegalStateException("Context already active - multiple tree roots are not supported");
        }
        contextualInstanceStore.set(new ContextualInstanceStore());
    }

    ContextualInstanceStore deactivate() {
        ContextualInstanceStore store = contextualInstanceStore.get();
        if (store != null) {
            try {
                store.deactivate();
            } finally {
                contextualInstanceStore.remove();
            }
        }
        return store;
    }

}
