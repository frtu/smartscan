package com.github.frtu.smartscan.text;

import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(PatternBuilder.class);

	private StringBuilder regexStringBuilder = new StringBuilder();
	
	public static PatternBuilder capture() {
		return new PatternBuilder();
	}
	
	/**
	 * Capture a number [0-9]
	 * 
	 * @param sequenceName
	 */
	public PatternBuilder digits(String sequenceName) {
		return seq(sequenceName, "\\d+");
	}

	/**
	 * Capture a word [a-zA-Z_0-9]
	 * 
	 * @param sequenceName
	 */
	public PatternBuilder word(String sequenceName) {
		return seq(sequenceName, "\\w+");
	}

	/**
	 * Capture a anything (Attention, since it is eager, make sure you add separator using {@link skip}
	 * 
	 * @param sequenceName
	 */
	public PatternBuilder any(String sequenceName) {
		return seq(sequenceName, ".*");
	}

	/**
	 * Add all the options for a sequenceName
	 * 
	 * @param sequenceName
	 * @param sequence
	 */
	public PatternBuilder seq(String sequenceName, String... sequence) {
		return seq(sequenceName, Stream.of(sequence));
	}

	/**
	 * Add all the options for a sequenceName
	 * 
	 * @param sequenceName
	 * @param streamSequence
	 */
	public PatternBuilder seq(String sequenceName, Stream<String> streamSequence) {
		// Regex capture find the first match, so longuest should be first
		String sequencePattern = streamSequence.sorted(Comparator.comparingInt(String::length).reversed())
		        .collect(Collectors.joining("|"));

		return seq(sequenceName, sequencePattern);
	}

	/**
	 * Add a sequence to capture
	 * 
	 * @param sequenceName
	 * @param sequencePattern
	 */
	public PatternBuilder seq(String sequenceName, String sequencePattern) {
		regexStringBuilder.append("(?<").append(sequenceName).append(">").append(sequencePattern).append(")");
		return this;
	}

	/**
	 * Previous sequence is optional.
	 */
	public PatternBuilder isOptional() {
		regexStringBuilder.append("?");
		return this;
	}

	/**
	 * Separate with whitespace
	 */
	public PatternBuilder skipSpace() {
		return skip("\\s+");
	}

	/**
	 * Separate with dot '.'
	 */
	public PatternBuilder skipDot() {
		return skip("\\.+");
	}

	/**
	 * Contains this text in the middle
	 */
	public PatternBuilder skip(String text) {
		regexStringBuilder.append(text);
		return this;
	}

	public Pattern build() {
		if (regexStringBuilder.length() == 0) {
			throw new IllegalStateException("capture() method should be called before build()");
		}
		String regex = regexStringBuilder.toString();
		LOGGER.info("Initialize patterh with {}", regex);
		return Pattern.compile(regex);
	}
}
