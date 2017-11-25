package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Represent one tag &lt;bean&gt;
 * 
 * @author fred
 */
public class BeanNav extends AbtractBaseNavigator {
	private String id;
	private BeanDefinition beanDefinition;
	
	private BeanNav(BeanDefinitionRegistry registry, String id, BeanDefinition beanDefinition) {
		super(registry);
		this.id = id;
		this.beanDefinition = beanDefinition;
	}

	static BeanNav build(BeanDefinitionRegistry registry, String beanName) throws NoSuchBeanDefinitionException {
		BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
		return build(registry, beanName, beanDefinition);
	}

	static BeanNav build(BeanDefinitionRegistry registry, BeanDefinition beanDefinition) {
		return build(registry, null, beanDefinition);
	}
	
	static BeanNav build(BeanDefinitionRegistry registry, String id, BeanDefinition beanDefinition) {
		return new BeanNav(registry, id, beanDefinition);
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
	
	public String id() {
		return id;
	}

	public PropertyNav property(String propertyName) {
		PropertyValue propertyValue = beanDefinition.getPropertyValues().getPropertyValue(propertyName);
		return new PropertyNav(super.getRegistry(), propertyValue);
	}
}
