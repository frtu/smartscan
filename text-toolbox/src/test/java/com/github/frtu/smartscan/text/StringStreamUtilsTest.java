package com.github.frtu.smartscan.text;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class StringStreamUtilsTest {
	@Test
	public void testGetLastWord() {
		assertEquals("word", StringStreamUtils.getLastWord("This sentence last word").get());
	}

	@Test
	public void testGetClassname() {
		assertEquals(StringStreamUtils.class.getSimpleName(),
		        StringStreamUtils.getClassname(StringStreamUtils.class.getCanonicalName()).get());
	}

	@Test
	public void testEmptyInputGetLastToken() {
		assertFalse(StringStreamUtils.getLastToken(null, "").isPresent());
	}

	@Test
	public void testEmptySeparatorGetLastToken() {
		String input = "input";
		Optional<String> lastToken = StringStreamUtils.getLastToken(input, null);
		assertTrue(lastToken.isPresent());
		assertEquals(input, lastToken.get());
	}

	@Test
	public void testStream() {
		List<String> wordList = Stream
		        .of(null, "word1", "This sentence last word2", "Another sentence last word3",
		                "A third sentence last word4")
		        .map(StringStreamUtils::getLastWord)
		        .flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty())
		        .collect(Collectors.toList());

		assertEquals(4, wordList.size());
		assertEquals("word1", wordList.get(0));
		assertEquals("word2", wordList.get(1));
		assertEquals("word3", wordList.get(2));
		assertEquals("word4", wordList.get(3));
	}
}
