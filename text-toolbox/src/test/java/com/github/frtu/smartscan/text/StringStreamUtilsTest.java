package com.github.frtu.smartscan.text;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class StringStreamUtilsTest {
	private static final String EMPTY = "";

	private static final List<String> list = Arrays.asList(null, "word1", "This sentence last word2",
	        "Another sentence last word3", "A third sentence last word4", " ");

	@Test
	public void testGetLastWord() {
		assertEquals("word", StringStreamUtils.getLastWord("This sentence last word"));
	}

	@Test
	public void testGetClassname() {
		assertEquals(StringStreamUtils.class.getSimpleName(),
		        StringStreamUtils.getClassname(StringStreamUtils.class.getCanonicalName()));
	}

	@Test
	public void testEmptyInputGetLastToken() {
		assertNull(StringStreamUtils.getLastToken(null, EMPTY));
		assertEquals(EMPTY, StringStreamUtils.getLastToken(EMPTY, EMPTY));
	}

	@Test
	public void testEmptySeparatorGetLastToken() {
		String input = "input";
		assertEquals(input, StringStreamUtils.getLastToken(input, null));
		assertEquals(input, StringStreamUtils.getLastToken(input, EMPTY));
	}

	@Test
	public void testStream() {
		List<String> wordList = list.stream().map(StringStreamUtils::getLastWord).collect(Collectors.toList());

		assertEquals(6, wordList.size());
		assertEquals(null, wordList.get(0));
		assertEquals("word1", wordList.get(1));
		assertEquals("word2", wordList.get(2));
		assertEquals("word3", wordList.get(3));
		assertEquals("word4", wordList.get(4));
	}

	@Test
	public void testStreamRemoveEmpty() {
		// If null is not desirable
		List<String> wordList = list.stream().filter(StringStreamUtils::isNotEmpty).map(StringStreamUtils::getLastWord)
		        .collect(Collectors.toList());

		assertEquals(5, wordList.size());
		for (String word : wordList) {
			assertNotNull(word);
		}
	}

	@Test
	public void testStreamRemoveBlank() {
		// If null and empty is not desirable
		List<String> wordList = list.stream().filter(StringStreamUtils::isNotBlank).map(StringStreamUtils::getLastWord)
		        .collect(Collectors.toList());

		assertEquals(4, wordList.size());
		for (String word : wordList) {
			assertTrue(word.length() > 0);
		}
	}
}
