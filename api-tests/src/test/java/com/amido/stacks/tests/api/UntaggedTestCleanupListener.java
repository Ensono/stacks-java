package com.amido.stacks.tests.api;

import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

/**
 * Registers the shutdown hook that clears the transient report directories when running the
 * untagged test check. This listener uses the {@link LauncherSessionListener} interface which is
 * invoked at session start - before any test discovery or execution - ensuring cleanup is
 * registered even when test discovery throws {@code NoTestsDiscoveredException}.
 *
 * <p>The cleanup hook runs unconditionally when the {@code untagged.test.check} system property is
 * {@code true}, which is the expected scenario for the invalid tag check pipeline step.
 */
public final class UntaggedTestCleanupListener implements LauncherSessionListener {

  @Override
  public void launcherSessionOpened(LauncherSession session) {
    // Register cleanup at session start, before any test discovery.
    // This ensures cleanup runs even if NoTestsDiscoveredException is thrown.
    UntaggedTestArtifactsCleaner.registerShutdownHook();
  }
}
