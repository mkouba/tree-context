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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Martin Kouba
 *
 */
@TreeRoot
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
@Interceptor
@Dependent
public class TreeRootInterceptor {

    private final TreeContext treeContext;

    @Inject
    public TreeRootInterceptor(TreeContextExtension extension) {
        this.treeContext = extension.getTreeContext();
    }

    @AroundConstruct
    void aroundConstruct(InvocationContext ctx) throws Exception {
        treeContext.activate();
        ctx.proceed();
    }

    @PostConstruct
    void postConstruct(InvocationContext ctx) throws Exception {
        treeContext.deactivate();
        ctx.proceed();
    }

    @PreDestroy
    void preDestroy(InvocationContext ctx) throws Exception {
        treeContext.destroy();
        ctx.proceed();
    }

}
