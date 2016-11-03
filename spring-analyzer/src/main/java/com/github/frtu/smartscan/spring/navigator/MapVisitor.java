package com.github.frtu.smartscan.spring.navigator;

public interface MapVisitor<T> {
	public void visit(String key, T value);

}
