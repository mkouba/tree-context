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
import static org.junit.Assert.assertNotNull;

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
            Root root1 = container.select(Root.class).get();
            Root root2 = container.select(Root.class).get();
            assertRoot(root1);
            assertRoot(root2);
            assertEquals(0, Root.DESTROYED.get());
            assertEquals(0, Reused.DESTROYED.get());
            container.destroy(root1);
            assertEquals(1, Root.DESTROYED.get());
            assertEquals(1, Reused.DESTROYED.get());
            container.destroy(root2);
            assertEquals(2, Root.DESTROYED.get());
            assertEquals(2, Reused.DESTROYED.get());
        }
    }

    private void assertRoot(Root root) {
        assertNotNull(root);
        assertNotNull(root.getBloom());
        assertNotNull(root.getReused());
        assertEquals(root.getReused().getId(), root.getBloom().getReused().getId());
    }

}
