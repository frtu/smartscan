package com.github.frtu.smartscan.text;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Util class easily used in conjunction with {@link Stream}.
 * 
 * To avoid getting a list of Optional{@literal <}String{@literal >} in Java 8, append
 * <code>.flatMap(o -{@literal >} o.isPresent() ? Stream.of(o.get()) :
 * Stream.empty())</code>, in Java 9 append <code>.flatMap(Optional::stream)</code>
 * 
 * @author fred
 * @since 2.3
 */
public class StringStreamUtils {
	/**
	 * Extract the last word of a sentence using the following syntax : Stream.map(StringStreamUtils::getLastWord)
	 * 
	 * @param input
	 *            a sentence
	 * @return the last word
	 */
	public static Optional<String> getLastWord(String input) {
		return StringStreamUtils.getLastToken(input, " ");
	}

	/**
	 * Extract the simple Class name from a String just like {@link Class#getSimpleName()} would do using the following
	 * syntax : Stream.map(StringStreamUtils::getClassname).
	 * 
	 * @param input
	 *            a String classname
	 * @return the simple name of the class
	 */
	public static Optional<String> getClassname(String input) {
		return StringStreamUtils.getLastToken(input, ".");
	}

	/**
	 * Extract the last token from a String.
	 * 
	 * @param input
	 *            any String
	 * @param tokenSeparator
	 *            the token separator
	 * @return last token
	 */
	public static Optional<String> getLastToken(String input, String tokenSeparator) {
		if (StringUtils.isEmpty(input)) {
			return Optional.empty();
		}
		if (StringUtils.isEmpty(tokenSeparator) || !StringUtils.contains(input, tokenSeparator)) {
			return Optional.of(input);
		}
		// substringAfterLast() returns empty string when there is no separator in the input string so we need the if
		// statement for that case
		return Optional.of(StringUtils.substringAfterLast(input, tokenSeparator));
	}
}
