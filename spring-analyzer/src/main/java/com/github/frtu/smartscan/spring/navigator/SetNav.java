package com.github.frtu.smartscan.spring.navigator;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Support <set> tag.
 * 
 * @author fred
 */
public class SetNav extends AbtractBaseNavigator {
	private Set<?> innerObject;

	SetNav(BeanDefinitionRegistry registry, Set<?> value) {
		super(registry);
		this.innerObject = value;
	}
	
	public Stream<String> streamString() {
		@SuppressWarnings("unchecked")
		Set<TypedStringValue> beanSet = (Set<TypedStringValue>) innerObject;
		return beanSet.stream().map(s -> s.getValue());
	}
	
	public Stream<BeanNav> streamBean() {
		@SuppressWarnings("unchecked")
		Set<BeanDefinitionHolder> beanSet = (Set<BeanDefinitionHolder>) innerObject;
		return beanSet.stream().map(m -> BeanNav.build(registry, m.getBeanDefinition()));
	}
}
