package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Represent one tag <property>
 * 
 * @author fred
 */
public class Property extends AbtractBaseNavigator {
	protected PropertyValue propertyValue;

	Property(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		super(registry);
		this.propertyValue = propertyValue;
	}

	static Property build(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		return new Property(registry, propertyValue);
	}

	/**
	 * Correspond to <property name="integerProperty2" value="1"/>
	 * 
	 * @return
	 */
	public String value() {
		return buildString(propertyValue.getValue());
	}
	
	/**
	 * Correspond to <property name="beanTwo" ref="yetAnotherBean"/>
	 * 
	 * @return
	 */
	public Bean ref() {
		return buildBean(this.registry, propertyValue.getValue());
	}

	public ListProperty list() {
		return buildList(this.registry, propertyValue.getValue());
	}
}
