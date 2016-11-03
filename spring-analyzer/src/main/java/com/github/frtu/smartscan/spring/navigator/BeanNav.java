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

	private PropertyValue getPropertyValue(String propertyName) {
		PropertyValue propertyValue = beanDefinition.getPropertyValues().getPropertyValue(propertyName);
		return propertyValue;
	}
	
	public PropertyNav property(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return PropertyNav.build(this.registry, propertyValue);
	}
	
	public BeanNav beanProperty(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return buildBean(this.registry, propertyValue.getValue());
	}

	public ListNav listProperty(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return buildList(this.registry, propertyValue.getValue());
	}

	public SetNav setProperty(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return buildSet(this.registry, propertyValue.getValue());
	}

	public MapNav mapProperty(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return buildMap(this.registry, propertyValue.getValue());
	}
}
