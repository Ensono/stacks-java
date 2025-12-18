package com.amido.stacks.tests.api;

import java.util.regex.Pattern;
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

  private static final String GROUPS_PROPERTY = "groups";
  private static final String CUCUMBER_TAGS_PROPERTY = "cucumber.filter.tags";
  private static final String ALLOW_EMPTY_SUITES_PROPERTY = "junit.platform.suite.allowEmptySuites";
  private static final String UNTAGGED_CHECK_PROPERTY = "untagged.test.check";

  private static final Pattern TAG_TOKEN =
      Pattern.compile("\\b(?!(?:not|and|or)\\b)([A-Za-z][A-Za-z0-9_-]*)\\b");

  @Override
  public void launcherSessionOpened(LauncherSession session) {
    System.setProperty(ALLOW_EMPTY_SUITES_PROPERTY, "true");
    translateGroupsToCucumberTags();
    // Register cleanup at session start, before any test discovery.
    // This ensures cleanup runs even if NoTestsDiscoveredException is thrown.
    UntaggedTestArtifactsCleaner.registerShutdownHook();
  }

  private static void translateGroupsToCucumberTags() {
    // If a cucumber tag filter is already provided, do nothing.
    if (System.getProperty(CUCUMBER_TAGS_PROPERTY) != null) {
      return;
    }

    String groupsExpression = System.getProperty(GROUPS_PROPERTY);
    if (groupsExpression == null || groupsExpression.isBlank()) {
      return;
    }

    // If the untagged property is not set, but the groups expression is a negation, enable it so
    // the cleanup hook runs in CI even when the pipeline omits the property.
    if (System.getProperty(UNTAGGED_CHECK_PROPERTY) == null && groupsExpression.contains("!")) {
      System.setProperty(UNTAGGED_CHECK_PROPERTY, "true");
    }

    String normalized = normalizeExpression(groupsExpression);
    String withTags = TAG_TOKEN.matcher(normalized).replaceAll("@$1");
    System.setProperty(CUCUMBER_TAGS_PROPERTY, withTags.trim());
  }

  private static String normalizeExpression(String expression) {
    String result = expression;
    // Normalize common JUnit group syntax to Cucumber boolean syntax.
    result = result.replace("! (", "not (");
    result = result.replace("!(`", "not (");
    result = result.replace("!(", "not (");
    result = result.replace(") & !(", ") and not (");
    result = result.replace("& !(", "and not (");
    result = result.replace("&", "and");
    result = result.replace("|", "or");
    result = result.replace("!", "not ");
    // Collapse repeated spaces.
    result = result.replaceAll("\\s+", " ");
    return result.trim();
  }
}
