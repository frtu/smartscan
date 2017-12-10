package com.github.frtu.smartscan.text;

import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder for {@link Pattern} that allow to capture text sequences.
 * 
 * @author fred
 */
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
	 *            Name of the captured digits
	 * @return itself
	 */
	public PatternBuilder digits(String sequenceName) {
		return seq(sequenceName, "\\d+");
	}

	/**
	 * Capture a word [a-zA-Z_0-9]
	 * 
	 * @param sequenceName
	 *            Name of the captured word
	 * @return itself
	 */
	public PatternBuilder word(String sequenceName) {
		return seq(sequenceName, "\\w+");
	}

	/**
	 * Capture a anything (Attention, since it is eager, make sure you add separator using {@link #skip(String)}
	 * 
	 * @param sequenceName
	 *            Name of the captured group
	 * @return itself
	 */
	public PatternBuilder any(String sequenceName) {
		return seq(sequenceName, ".*");
	}

	/**
	 * Add all the options for a sequenceName
	 * 
	 * @param sequenceName
	 *            Name of the captured sequence
	 * @param sequence
	 *            All the options
	 * @return itself
	 */
	public PatternBuilder seq(String sequenceName, String... sequence) {
		return seq(sequenceName, Stream.of(sequence));
	}

	/**
	 * Add all the options for a sequenceName
	 * 
	 * @param sequenceName
	 *            Name of the captured sequence
	 * @param streamSequence
	 *            Stream of optional string
	 * @return itself
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
	 *            Name of the captured sequence
	 * @param sequencePattern
	 *            Any regex
	 * @return itself
	 */
	public PatternBuilder seq(String sequenceName, String sequencePattern) {
		regexStringBuilder.append("(?<").append(sequenceName).append(">").append(sequencePattern).append(")");
		return this;
	}

	/**
	 * Previous sequence is optional.
	 * 
	 * @return itself
	 */
	public PatternBuilder isOptional() {
		regexStringBuilder.append("?");
		return this;
	}

	/**
	 * Separate with whitespace
	 * 
	 * @return itself
	 */
	public PatternBuilder skipSpace() {
		return skip("\\s+");
	}

	/**
	 * Separate with dot '.'
	 * 
	 * @return itself
	 */
	public PatternBuilder skipDot() {
		return skip("\\.+");
	}

	/**
	 * Contains this text in the middle
	 * 
	 * @param text
	 *            A text
	 * @return itself
	 */
	public PatternBuilder skip(String text) {
		regexStringBuilder.append(text);
		return this;
	}

	/**
	 * Termination method that build a Pattern based on previous methods calls.
	 * 
	 * @return Pattern
	 */
	public Pattern build() {
		if (regexStringBuilder.length() == 0) {
			throw new IllegalStateException("capture() method should be called before build()");
		}
		String regex = regexStringBuilder.toString();
		LOGGER.info("Initialize patterh with {}", regex);
		return Pattern.compile(regex);
	}
}
