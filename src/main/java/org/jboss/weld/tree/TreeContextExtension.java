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
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessBean;

/**
 *
 * @author Martin Kouba
 *
 */
public class TreeContextExtension implements Extension {

    private TreeContext treeContext;

    private List<Bean<?>> beansToValidate;

    public TreeContextExtension() {
        this.beansToValidate = new LinkedList<>();
    }

    void processBean(@Observes ProcessBean<?> event) {
        Bean<?> bean = event.getBean();
        if (!Dependent.class.equals(bean.getScope()) && !TreeDependent.class.equals(bean.getScope()) && bean.getInjectionPoints() != null
                && !bean.getInjectionPoints().isEmpty()) {
            beansToValidate.add(bean);
        }
    }

    void registerTreeContext(@Observes AfterBeanDiscovery event, BeanManager manager) {
        this.treeContext = new TreeContext();
        event.addContext(treeContext);
        // Validate beans
        for (Bean<?> bean : beansToValidate) {
            for (InjectionPoint injectionPoint : bean.getInjectionPoints()) {
                Bean<?> injectedBean = manager.resolve(manager.getBeans(injectionPoint.getType(), injectionPoint.getQualifiers().toArray(new Annotation[] {})));
                if (TreeDependent.class.equals(injectedBean.getScope())) {
                    event.addDefinitionError(new IllegalStateException("@TreeDependent bean can only be injected into @Dependent or @TreeDependent bean"));
                }
            }
        }
    }

    TreeContext getTreeContext() {
        return treeContext;
    }

}
