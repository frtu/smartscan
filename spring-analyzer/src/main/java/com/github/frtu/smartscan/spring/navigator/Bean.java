package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Represent one tag <bean>
 * 
 * @author fred
 */
public class Bean extends AbtractBaseNavigator {
	private BeanDefinition beanDefinition;

	Bean(BeanDefinitionRegistry registry, String beanName) {
		super(registry);
		beanDefinition = registry.getBeanDefinition(beanName);
	}

	public BeanDefinition getBeanDefinition() {
		return beanDefinition;
	}
}
