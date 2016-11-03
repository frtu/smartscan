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
public class Bean extends AbtractBaseNavigator {
	private BeanDefinition beanDefinition;

	private Bean(BeanDefinitionRegistry registry, BeanDefinition beanDefinition) {
		super(registry);
		this.beanDefinition = beanDefinition;
	}

	static Bean build(BeanDefinitionRegistry registry, String beanName) throws NoSuchBeanDefinitionException {
		BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
		return new Bean(registry, beanDefinition);
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
	
	public Property property(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return Property.build(this.registry, propertyValue);
	}
	
	public Bean beanProperty(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return buildBean(this.registry, propertyValue.getValue());
	}

	public ListProperty listProperty(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return buildList(this.registry, propertyValue.getValue());
	}

	public SetProperty setProperty(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return buildSet(this.registry, propertyValue.getValue());
	}

	public MapProperty mapProperty(String propertyName) {
		PropertyValue propertyValue = getPropertyValue(propertyName);
		return buildMap(this.registry, propertyValue.getValue());
	}
}
