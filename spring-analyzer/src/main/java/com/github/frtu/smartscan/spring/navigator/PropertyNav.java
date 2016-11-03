package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Represent one tag <property>
 * 
 * @author fred
 */
public class PropertyNav extends IntermediateNav {
	protected PropertyValue propertyValue;

	PropertyNav(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		super(registry);
		this.propertyValue = propertyValue;
	}

	protected Object innerObject() {
		return propertyValue.getValue();
	}
}
