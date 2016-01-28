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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.tree.TreeContext;
import org.jboss.weld.tree.TreeContextExtension;
import org.junit.Test;

/**
 *
 * @author Martin Kouba
 */
public class TreeContextTest {

    @Test
    public void testTreeContext() {
        try (WeldContainer container = new Weld().disableDiscovery().addExtension(new TreeContextExtension()).packages(TreeContext.class)
                .beanClasses(Root.class, Reused.class, Bloom.class).initialize()) {
            Root root = container.select(Root.class).get();
            assertNotNull(root);
            assertNotNull(root.getBloom());
            assertNotNull(root.getReused());
            assertEquals(root.getReused().getId(), root.getBloom().getReused().getId());
            assertFalse(Root.DESTROYED.get());
            assertFalse(Reused.DESTROYED.get());
            container.destroy(root);
            assertTrue(Root.DESTROYED.get());
            assertTrue(Reused.DESTROYED.get());
        }
    }

}
