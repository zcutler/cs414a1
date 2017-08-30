package edu.colostate.cs.cs414.a1.zcutler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import junit.framework.JUnit4TestAdapter;


// This section declares all of the test classes in the program.
@RunWith (Suite.class)
@Suite.SuiteClasses ({ WorkerTest.class, ProjectTest.class, QualificationTest.class }) // Add test classes here.

public class TestAll {
// Execution begins in main(). This test class executes a
// test runner that tells the tester if any fail.

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    // The suite() method helps when using JUnit 3 Test Runners or Ant.
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestAll.class);
    }

}