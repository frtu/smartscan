package com.github.frtu.smartscan.text;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class easily used in conjunction with {@link Stream}.
 * 
 * <p>
 * Treat null as a pass through : if null is passed, null is returned. Just filter null with
 * {@link #isNotEmpty(CharSequence)} or {@link #isNotEmpty(CharSequence)} in
 * {@link Stream#filter(java.util.function.Predicate)}.
 * </p>
 * 
 * @author frtu
 * @since 2.4 This version is more simple by considering null as a pass through.
 */
public class StringStreamUtils {
	private static final String DOT = ".";

	/**
	 * Extract the last word of a sentence using the following syntax : Stream.map(StringStreamUtils::getLastWord)
	 * 
	 * @param input
	 *            a sentence
	 * @return the last word
	 */
	public static String getLastWord(String input) {
		return StringStreamUtils.getLastToken(input, StringUtils.SPACE);
	}

	/**
	 * Extract the simple Class name from a String just like {@link Class#getSimpleName()} would do using the following
	 * syntax : Stream.map(StringStreamUtils::getClassname).
	 * 
	 * @param input
	 *            a String classname
	 * @return the simple name of the class
	 */
	public static String getClassname(String input) {
		return StringStreamUtils.getLastToken(input, DOT);
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
	public static String getLastToken(String input, String tokenSeparator) {
		if (isEmpty(tokenSeparator) || !StringUtils.contains(input, tokenSeparator)) {
			return input;
		}
		// substringAfterLast() returns empty string when there is no separator in the input string so we need the if
		// statement for that case
		return StringUtils.substringAfterLast(input, tokenSeparator);
	}

	/**
	 * Encapsulation of {@link StringUtils#isEmpty(CharSequence)}, should use it instead of this method if you need more
	 * check flavor. This is just a shortcut to use with {@link Stream} using the following syntax :
	 * Stream.filter(StringStreamUtils::isEmpty).
	 * 
	 * <p>
	 * Checks if a CharSequence is empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * @param cs
	 *            the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is empty or null
	 */
	public static boolean isEmpty(CharSequence cs) {
		return StringUtils.isEmpty(cs);
	}

	/**
	 * Just the opposite of {@link #isEmpty(CharSequence)}. This is just a shortcut to use with {@link Stream} using the
	 * following syntax : Stream.filter(StringStreamUtils::isNotEmpty).
	 * 
	 * <p>
	 * Checks if a CharSequence is not empty ("") and not null.
	 * </p>
	 *
	 * <pre>
	 * isNotEmpty(null)      = false
	 * isNotEmpty("")        = false
	 * isNotEmpty(" ")       = true
	 * isNotEmpty("bob")     = true
	 * isNotEmpty("  bob  ") = true
	 * </pre>
	 *
	 * @param cs
	 *            the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is not empty and not null
	 */
	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}

	/**
	 * Encapsulation of {@link StringUtils#isBlank(CharSequence)}, should use it instead of this method if you need more
	 * check flavor. This is just a shortcut to use with {@link Stream} using the following syntax :
	 * Stream.filter(StringStreamUtils::isBlank).
	 * 
	 * <p>
	 * Checks if a CharSequence is empty (""), null or whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * isBlank(null)      = true
	 * isBlank("")        = true
	 * isBlank(" ")       = true
	 * isBlank("bob")     = false
	 * isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param cs
	 *            the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace only
	 */
	public static boolean isBlank(final CharSequence cs) {
		return StringUtils.isBlank(cs);
	}

	/**
	 * Just the opposite of {@link #isBlank(CharSequence)}. This is just a shortcut to use with {@link Stream} using the
	 * following syntax : Stream.filter(StringStreamUtils::isNotBlank).
	 * 
	 * <p>
	 * Checks if a CharSequence is not empty (""), not null and not whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * isNotBlank(null)      = false
	 * isNotBlank("")        = false
	 * isNotBlank(" ")       = false
	 * isNotBlank("bob")     = true
	 * isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param cs
	 *            the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is not empty and not null and not whitespace only
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}
}
