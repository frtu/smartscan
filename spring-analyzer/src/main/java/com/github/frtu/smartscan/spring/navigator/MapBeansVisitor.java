package com.github.frtu.smartscan.spring.navigator;

public interface MapBeansVisitor<T> {
	public void visit(String key, T value);

}
