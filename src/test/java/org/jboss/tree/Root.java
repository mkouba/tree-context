/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.tree;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.jboss.weld.tree.TreeRoot;

@TreeRoot
public class Root {

    static final AtomicBoolean DESTROYED = new AtomicBoolean(false);

    // This should be the same instance as the one injected into Bloom#reused
    @Inject
    Reused reused;

    @Inject
    Bloom bloom;

    @PostConstruct
    public void init() {
        System.out.println("Init " + toString());
    }

    @PreDestroy
    public void destroy() {
        DESTROYED.set(true);
        System.out.println("Destroy " + toString());
    }

    public Root() {
    }

    public Reused getReused() {
        return reused;
    }

    public Bloom getBloom() {
        return bloom;
    }

}
