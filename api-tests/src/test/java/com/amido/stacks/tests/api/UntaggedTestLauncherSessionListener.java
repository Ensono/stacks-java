package com.amido.stacks.tests.api;

import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

/** Registers the untagged test cleanup hook whenever the JUnit launcher boots. */
public final class UntaggedTestLauncherSessionListener implements LauncherSessionListener {

  @Override
  public void launcherSessionOpened(LauncherSession session) {
    UntaggedTestArtifactsCleaner.registerShutdownHook();
  }
}
