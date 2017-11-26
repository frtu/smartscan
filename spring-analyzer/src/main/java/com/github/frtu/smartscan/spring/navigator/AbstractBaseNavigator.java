package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.util.Assert;

/**
 * Base class for all XML config Navigation classes in this package.
 * 
 * @author fred
 */
public abstract class AbstractBaseNavigator {
	private BeanDefinitionRegistry registry;

	public AbstractBaseNavigator() {
		this(new SimpleBeanDefinitionRegistry());
	}

	public AbstractBaseNavigator(BeanDefinitionRegistry registry) {
		super();
		Assert.notNull(registry, "registry cannot be null!");
		this.registry = registry;
	}

	protected BeanDefinitionRegistry getRegistry() {
		return registry;
	}
}