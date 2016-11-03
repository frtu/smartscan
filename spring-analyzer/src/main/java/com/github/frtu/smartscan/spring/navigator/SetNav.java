package com.github.frtu.smartscan.spring.navigator;

import java.util.Set;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Support <set> tag.
 * 
 * @author fred
 */
public class SetNav extends AbtractBaseNavigator {
	private Set<?> innerObject;

	SetNav(BeanDefinitionRegistry registry, Set<?> value) {
		super(registry);
		this.innerObject = value;
	}
}
