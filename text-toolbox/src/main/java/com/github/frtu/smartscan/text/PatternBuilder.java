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
	 * Capture a word with dot [a-zA-Z_0-9\.]
	 * 
	 * @param sequenceName
	 *            Name of the captured word
	 * @return itself
	 */
	public PatternBuilder wordContainingDots(String sequenceName) {
		return wordContaining(sequenceName, '.');
	}

	/**
	 * Capture a word [a-zA-Z_0-9] containing other characters (like dot, ...)
	 * 
	 * @param sequenceName
	 *            Name of the captured word
	 * @param additionalCharacters any other charaters
	 * @return itself
	 * @since 2.2
	 */
	public PatternBuilder wordContaining(String sequenceName, char... additionalCharacters) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("[a-zA-Z_0-9");
		for (char c : additionalCharacters) {
			pattern.append("\\").append(c);
		}
		pattern.append("]+");
		
		return seq(sequenceName, pattern.toString());
	}
	
	/**
	 * Capture a anything (Attention, since it is eager, make sure you add separator using {@link #splitWith(String)}
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
	 * @param options
	 *            All the options
	 * @return itself
	 * @since 2.2
	 */
	public PatternBuilder enumeration(String sequenceName, String... options) {
		return enumeration(sequenceName, Stream.of(options));
	}

	/**
	 * @param sequenceName
	 *            Name of the captured sequence
	 * @param options
	 *            All the options
	 * @return itself
	 */
	@Deprecated
	public PatternBuilder seq(String sequenceName, String... options) {
		return enumeration(sequenceName, Stream.of(options));
	}

	/**
	 * Add all the options for a sequenceName
	 * 
	 * @param sequenceName
	 *            Name of the captured sequence
	 * @param streamOptions
	 *            Stream of optional string
	 * @return itself
	 */
	private PatternBuilder enumeration(String sequenceName, Stream<String> streamOptions) {
		// Regex capture find the first match, so longuest should be first
		String sequencePattern = streamOptions.sorted(Comparator.comparingInt(String::length).reversed())
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
	 * Separate with ONE OR MANY whitespace
	 * 
	 * @return itself
	 * @since 2.2
	 */
	public PatternBuilder splitWithSpaces() {
		return splitWith("\\s+");
	}

	/**
	 * Separate with ONE OR MANY dot '.'
	 * 
	 * @return itself
	 * @since 2.2
	 */
	public PatternBuilder splitWithDots() {
		return splitWith("\\.+");
	}

	/**
	 * Separator of ONE character
	 * 
	 * @param character
	 *            A character
	 * @return itself
	 * @since 2.2
	 */
	public PatternBuilder splitWith(char character) {
		regexStringBuilder.append("\\").append(character);
		return this;
	}

	/**
	 * Contains this text in the middle
	 * 
	 * @param text
	 *            A text
	 * @return itself
	 * @since 2.2
	 */
	public PatternBuilder splitWith(String text) {
		regexStringBuilder.append(text);
		return this;
	}

	/**
	 * @return itself
	 * @deprecated use {@link #splitWithSpaces()} instead
	 */
	@Deprecated
	public PatternBuilder skipSpace() {
		return splitWithSpaces();
	}

	/**
	 * @return itself
	 * @deprecated use {@link #splitWithDots()} instead
	 */
	@Deprecated
	public PatternBuilder skipDot() {
		return splitWithDots();
	}

	/**
	 * @param text a text
	 * @return itself
	 * @deprecated use {@link #splitWith(String)} instead
	 */
	@Deprecated
	public PatternBuilder skip(String text) {
		return splitWith(text);
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
