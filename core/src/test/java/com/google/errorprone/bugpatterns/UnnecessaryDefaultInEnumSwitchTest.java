/*
 * Copyright 2017 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone.bugpatterns;

import static com.google.errorprone.BugCheckerRefactoringTestHelper.TestMode.TEXT_MATCH;

import com.google.errorprone.BugCheckerRefactoringTestHelper;
import com.google.errorprone.CompilationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** {@link UnnecessaryDefaultInEnumSwitch}Test */
@RunWith(JUnit4.class)
public class UnnecessaryDefaultInEnumSwitchTest {

  private final CompilationTestHelper compilationHelper =
      CompilationTestHelper.newInstance(UnnecessaryDefaultInEnumSwitch.class, getClass());

  @Test
  public void switchCannotComplete() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      default:",
            "        // This is a comment",
            "        throw new AssertionError(c);",
            "    }",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      ",
            "    }",
            "// This is a comment",
            "throw new AssertionError(c);",
            "  }",
            "}")
        .doTest(TEXT_MATCH);
  }

  @Test
  public void switchCannotCompleteUnrecognized() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      default:",
            "        // This is a comment",
            "        throw new AssertionError(c);",
            "    }",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      case UNRECOGNIZED:",
            "        break;",
            "    }",
            "// This is a comment",
            "throw new AssertionError(c);",
            "  }",
            "}")
        .doTest(TEXT_MATCH);
  }

  @Test
  public void emptyDefault() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      default:",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void emptyDefaultUnrecognized() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      default:",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      case UNRECOGNIZED:",
            "        // continue below",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void defaultBreak() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      default:",
            "        break;",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void defaultBreakUnrecognized() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      default:",
            "        break;",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "      case THREE:",
            "        return true;",
            "      case UNRECOGNIZED:",
            "        // continue below",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void completes_noUnassignedVars_priorCaseExits() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        break;",
            "      case THREE:",
            "        return true;",
            "      default:",
            "        throw new AssertionError(c);",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        break;",
            "      case THREE:",
            "        return true;",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void completes_noUnassignedVars_priorCaseExitsUnrecognized() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        break;",
            "      case THREE:",
            "        return true;",
            "      default:",
            "        throw new AssertionError(c);",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        break;",
            "      case THREE:",
            "        return true;",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest(TEXT_MATCH);
  }

  @Test
  public void completes_noUnassignedVars_priorCaseDoesntExit() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      case THREE:",
            "      default:",
            "        // This is a comment",
            "        System.out.println(\"Test\");",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      case THREE:",
            "        // This is a comment",
            "        System.out.println(\"Test\");",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void completes_noUnassignedVars_priorCaseDoesntExitUnrecognized() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      case THREE:",
            "      default:",
            "        // This is a comment",
            "        System.out.println(\"Test\");",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      case THREE:",
            "      case UNRECOGNIZED:",
            "        // This is a comment",
            "        System.out.println(\"Test\");",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void completes_unassignedVars() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    int x;",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        x = 1;",
            "        break;",
            "      case THREE:",
            "        x = 2;",
            "        break;",
            "      default:",
            "        x = 3;",
            "    }",
            "    return x == 1;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void completes_unassignedVarsUnrecognized() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    int x;",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        x = 1;",
            "        break;",
            "      case THREE:",
            "        x = 2;",
            "        break;",
            "      default:",
            "        x = 3;",
            "    }",
            "    return x == 1;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void notExhaustive() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      default:",
            "        throw new AssertionError(c);",
            "    }",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void notExhaustiveUnrecognized() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      default:",
            "        throw new AssertionError(c);",
            "    }",
            "  }",
            "}")
        .addOutputLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      case UNRECOGNIZED:",
            "        break;",
            "    }",
            "    throw new AssertionError(c);",
            "  }",
            "}")
        .doTest(TEXT_MATCH);
  }

  @Test
  public void notExhaustive2() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(boolean f, Case c) {",
            "    if (f) {",
            "      switch (c) {",
            "        case ONE:",
            "        case TWO:",
            "        case THREE:",
            "          return true;",
            "        default:",
            "          return false;",
            "      }",
            "    } else {",
            "      return false;",
            "    }",
            "  }",
            "}")
        .addOutputLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE }",
            "  boolean m(boolean f, Case c) {",
            "    if (f) {",
            "      switch (c) {",
            "        case ONE:",
            "        case TWO:",
            "        case THREE:",
            "          return true;",
            "      }",
            "      return false;",
            "    } else {",
            "      return false;",
            "    }",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void notExhaustive2Unrecognized() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(boolean f, Case c) {",
            "    if (f) {",
            "      switch (c) {",
            "        case ONE:",
            "        case TWO:",
            "        case THREE:",
            "          return true;",
            "        default:",
            "          return false;",
            "      }",
            "    } else {",
            "      return false;",
            "    }",
            "  }",
            "}")
        .addOutputLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(boolean f, Case c) {",
            "    if (f) {",
            "      switch (c) {",
            "        case ONE:",
            "        case TWO:",
            "        case THREE:",
            "          return true;",
            "        case UNRECOGNIZED:",
            "          break;",
            "      }",
            "      return false;",
            "    } else {",
            "      return false;",
            "    }",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void unrecognizedIgnore() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "        return true;",
            "      default:",
            "        throw new AssertionError(c);",
            "    }",
            "  }",
            "}")
        .expectUnchanged()
        .doTest(TEXT_MATCH);
  }

  @Test
  public void defaultAboveCaseUnrecognized() throws Exception {
    BugCheckerRefactoringTestHelper.newInstance(new UnnecessaryDefaultInEnumSwitch(), getClass())
        .addInputLines(
            "in/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      default:",
            "      case THREE:",
            "        // This is a comment",
            "        System.out.println(\"Test\");",
            "    }",
            "    return false;",
            "  }",
            "}")
        .addOutputLines(
            "out/Test.java",
            "class Test {",
            "  enum Case { ONE, TWO, THREE, UNRECOGNIZED }",
            "  boolean m(Case c) {",
            "    switch (c) {",
            "      case ONE:",
            "      case TWO:",
            "        return true;",
            "      case UNRECOGNIZED:",
            "      case THREE:",
            "        // This is a comment",
            "        System.out.println(\"Test\");",
            "    }",
            "    return false;",
            "  }",
            "}")
        .doTest();
  }
}
