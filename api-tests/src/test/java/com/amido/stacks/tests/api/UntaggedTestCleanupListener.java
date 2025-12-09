package com.amido.stacks.tests.api;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

/** Registers the shutdown hook that clears the transient report directories. */
public final class UntaggedTestCleanupListener implements TestExecutionListener {

  @Override
  public void testPlanExecutionStarted(TestPlan testPlan) {
    UntaggedTestArtifactsCleaner.registerShutdownHook();
  }
}
