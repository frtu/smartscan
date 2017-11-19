package com.github.frtu.smartscan.spring.navigator;

import java.util.function.Consumer;

/**
 * Deprecated, use {@link Consumer} interface in conjunction with {@link MapBeansNav#forEachEntry(Consumer)} instead.
 * 
 * @author fred
 *
 * @param <T>
 */
@Deprecated
public interface MapBeansVisitor<T> {
	public void visit(String key, T value);

}
