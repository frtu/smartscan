package com.github.frtu.smartscan.text;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class PatternBuilderTest {
	@Test
	public void testFindSeqInMiddle() {
		String sequenceName = "sequenceName";
		String[] seqOptions = { "aa", "bb", "cc" };

		Pattern pattern = PatternBuilder.capture().enumeration(sequenceName, seqOptions).build();

		for (String option : seqOptions) {
			Matcher matcher = pattern.matcher("xxx" + option + "yyy");
			assertTrue(matcher.find());
			assertEquals(option, matcher.group(sequenceName));
		}
	}

	@Test
	public void testWordContaining() {
		Pattern pattern = PatternBuilder.capture().wordContaining("first", '.', ',').splitWith(';')
		        .wordContaining("second", ' ', ':').build();

		String firstWord = "a.word..with.dot,and,,comma";
		String secondWord = "another one with space:and::semicolon";
		Matcher matcher = pattern.matcher(firstWord + ";" + secondWord + ";anything else.with:other;char");

		assertTrue(matcher.find());
		assertEquals(secondWord, matcher.group("second"));
		assertEquals(firstWord, matcher.group("first"));
	}

	@Test
	public void testThreeLayers() {
		Pattern pattern = PatternBuilder.capture().digits("year").splitWithSpaces().word("word").splitWithDots()
		        .enumeration("field", "name", "type").build();

		Matcher matcher = pattern.matcher("2017    subpackage....name");
		assertTrue(matcher.find());
		assertEquals("name", matcher.group("field"));
		assertEquals("subpackage", matcher.group("word"));
		assertEquals("2017", matcher.group("year"));
	}

	@Test
	public void testSeparator() {
		Pattern pattern = PatternBuilder.capture().any("left").splitWith("separator").any("right").build();

		Matcher matcher = pattern.matcher("titi.toto.separator.coucou.lala");
		assertTrue(matcher.find());
		assertEquals("titi.toto.", matcher.group("left"));
		assertEquals(".coucou.lala", matcher.group("right"));
	}

	@Test
	public void testAny() {
		Pattern pattern = PatternBuilder.capture().word("first").splitWithDots().word("second").splitWithDots().any("all")
		        .build();

		Matcher matcher = pattern.matcher("titi.toto.tata.coucou.lala");
		assertTrue(matcher.find());
		assertEquals("toto", matcher.group("second"));
		assertEquals("titi", matcher.group("first"));
		assertEquals("tata.coucou.lala", matcher.group("all"));
	}

	@Test(expected = IllegalStateException.class)
	public void testEmpty() {
		PatternBuilder patternBuilder = new PatternBuilder();
		patternBuilder.build();
	}
}
