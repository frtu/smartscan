package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Represent one tag <property>
 * 
 * @author fred
 */
public class PropertyNav extends AbtractBaseNavigator {
	protected PropertyValue propertyValue;

	PropertyNav(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		super(registry);
		this.propertyValue = propertyValue;
	}

	static PropertyNav build(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		return new PropertyNav(registry, propertyValue);
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
	public BeanNav ref() {
		return buildBean(this.registry, propertyValue.getValue());
	}

	public ListNav list() {
		return buildList(this.registry, propertyValue.getValue());
	}
}
