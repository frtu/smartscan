package com.github.frtu.smartscan.spring.navigator;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Support <list> tag.
 * 
 * @author fred
 */
public class ListProperty extends AbtractBaseNavigator {
	private List<?> innerObject;
	
	ListProperty(BeanDefinitionRegistry registry, List<?> value) {
		super(registry);
		this.innerObject = value;
	}
	
	public String value(int index) {
		Object objResult = innerObject.get(index);
		return buildString(objResult);
	}
}
