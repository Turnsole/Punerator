package com.lastminutedevice.punerator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class SoundexTest extends TestCase {

    public SoundexTest(String name) {
        super(name);
    }

    public void testEncode() {

        HashMap <String, String> testWords = new HashMap<>();
        testWords.put("Preponderance", "p6153652");
        testWords.put("infinite", "i5153");
        testWords.put("things*stuff", "t52231");
        testWords.put("a", "a");
        testWords.put("c", "c");

        for (Map.Entry<String, String> entries : testWords.entrySet()) {
            assertEquals(entries.getValue(), Soundex.encode(entries.getKey()));
        }

        assertEquals("", Soundex.encode(null));
        assertEquals("", Soundex.encode(""));
        assertEquals("!", Soundex.encode("!"));
    }

    public void testFindMatch() {
        assertEquals("GOOSEling", Soundex.findMatch("goose", "gosling"));
        assertEquals("prePUNderance", Soundex.findMatch("pun", "preponderance"));
    }
}
