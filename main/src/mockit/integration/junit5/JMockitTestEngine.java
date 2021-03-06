/*
 * Copyright (c) 2006 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit5;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.engine.extension.*;
import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.*;

import mockit.*;
import mockit.internal.startup.*;

public final class JMockitTestEngine implements TestEngine
{
   private final TestDescriptor descriptor;

   public JMockitTestEngine()
   {
      descriptor = new EngineDescriptor(UniqueId.forEngine("jmockit"), "JMockit engine for JUnit 5");

      if (Startup.initializeIfPossible()) {
         new MockUp<ExtensionRegistry>() {
            @Mock
            ExtensionRegistry createRegistryWithDefaultExtensions(Invocation inv)
            {
               ExtensionRegistry registry = inv.proceed();

               Extension extension = new JMockitExtension();
               registry.registerExtension(extension, extension);

               return registry;
            }
         };
      }
   }

   @Override
   public String getId() { return "jmockit"; }

   @Override
   public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) { return descriptor; }

   @Override
   public void execute(ExecutionRequest executionRequest) {}
}
