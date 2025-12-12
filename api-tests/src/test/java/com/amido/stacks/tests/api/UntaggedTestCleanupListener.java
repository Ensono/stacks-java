package com.amido.stacks.tests.api;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

/**
 * Registers the shutdown hook that clears the transient report directories if no tests are found.
 */
public final class UntaggedTestCleanupListener implements TestExecutionListener {

  @Override
  public void testPlanExecutionStarted(TestPlan testPlan) {
    long testCount = testPlan.countTestIdentifiers(TestIdentifier::isTest);
    if (testCount == 0) {
      UntaggedTestArtifactsCleaner.registerShutdownHook();
    }
  }
}
