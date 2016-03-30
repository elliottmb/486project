package edu.iastate.cs.theseguys;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClientTest {
    @Test
    public void testSomeLibraryMethod() {
        Client classUnderTest = new Client();
        assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }
}
