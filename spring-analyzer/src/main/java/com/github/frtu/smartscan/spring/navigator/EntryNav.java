package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class EntryNav extends IntermediateNav {
	private Object entry;

	public EntryNav(BeanDefinitionRegistry registry, Object entry) {
		super(registry);
		this.entry = entry;
	}

	@Override
	protected Object innerObject() {
		return entry;
	}
}
