package com.github.frtu.smartscan.spring.navigator;

import java.util.Map.Entry;

import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Object encapsulating {@link Entry}. Use method {@link #key()} to get the Key & {@link #value()} to get the String
 * value or {@link #ref()}, {@link #listBeans()}, {@link #setBeans()}, {@link #mapBeans()} to continue navigation.
 * 
 * @author fred
 */
public class EntryNav extends IntermediateNav {
	private Entry<TypedStringValue, Object> entry;

	public EntryNav(BeanDefinitionRegistry registry, Entry<TypedStringValue, Object> entry) {
		super(registry);
		this.entry = entry;
	}

	@Override
	protected Object innerObject() {
		return entry.getValue();
	}

	public String key() {
		return entry.getKey().getValue();
	}
}
