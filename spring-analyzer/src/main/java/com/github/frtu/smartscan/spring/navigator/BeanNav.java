package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Represent one tag <bean>
 * 
 * @author fred
 */
public class BeanNav extends AbtractBaseNavigator {
	private BeanDefinition beanDefinition;

	private BeanNav(BeanDefinitionRegistry registry, BeanDefinition beanDefinition) {
		super(registry);
		this.beanDefinition = beanDefinition;
	}

	static BeanNav build(BeanDefinitionRegistry registry, String beanName) throws NoSuchBeanDefinitionException {
		BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
		return build(registry, beanDefinition);
	}

	static BeanNav build(BeanDefinitionRegistry registry, BeanDefinition beanDefinition) {
		return new BeanNav(registry, beanDefinition);
	}

	public boolean isClass(Class<?> clazz) {
		return isClass((clazz == null) ? null : clazz.getCanonicalName());
	}

	public boolean isClass(String className) {
		if (beanDefinition.getBeanClassName() != null) {
			return beanDefinition.getBeanClassName().equals(className);
		}
		return (className == null);
	}

	public BeanDefinition getBeanDefinition() {
		return beanDefinition;
	}

	public PropertyNav property(String propertyName) {
		PropertyValue propertyValue = beanDefinition.getPropertyValues().getPropertyValue(propertyName);
		return new PropertyNav(this.registry, propertyValue);
	}
}
