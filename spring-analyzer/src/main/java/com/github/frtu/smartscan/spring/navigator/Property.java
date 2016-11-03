package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class Property extends AbtractBaseNavigator {
	protected PropertyValue propertyValue;

	private Property(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		super(registry);
		this.propertyValue = propertyValue;
	}

	static Property build(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		return new Property(registry, propertyValue);
	}

	static Bean buildRef(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		Object value = propertyValue.getValue();
		if (value instanceof RuntimeBeanReference) {
			RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;
			return Bean.build(registry, runtimeBeanReference.getBeanName());
		}
		throw new IllegalStateException(
				"The <entry> doesn't contains an attribute ref={} but rather :" + value.getClass());
	}
	
	public String value() {
		Object value = propertyValue.getValue();
		if (value instanceof TypedStringValue) {
			TypedStringValue typedStringValue = (TypedStringValue) value;
			return typedStringValue.getValue();
		}
		throw new IllegalStateException("The <value> is not type of Integer but rather :" + value.getClass());
	}
}
