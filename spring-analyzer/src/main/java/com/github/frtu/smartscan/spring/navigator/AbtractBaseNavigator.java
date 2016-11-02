package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Base class for all config navigation classes.
 * 
 * @author fred
 */
public abstract class AbtractBaseNavigator {
	protected BeanDefinitionRegistry registry;

	AbtractBaseNavigator(BeanDefinitionRegistry registry) {
		super();
		this.registry = registry;
	}
}