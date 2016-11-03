package com.github.frtu.smartscan.spring.navigator;

import java.util.List;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Support <list> tag.
 * 
 * @author fred
 */
public class ListProperty extends Property {
	private List<?> innerObject;
	
	ListProperty(BeanDefinitionRegistry registry, PropertyValue propertyValue, List<?> value) {
		super(registry, propertyValue);
		this.innerObject = value;
	}
	
	public String value(int index) {
		Object objResult = innerObject.get(index);
		return checkTypedStringValue(objResult);
	}
}
