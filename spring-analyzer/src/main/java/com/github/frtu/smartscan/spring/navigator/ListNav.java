package com.github.frtu.smartscan.spring.navigator;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Support &lt;list&gt; tag.
 * 
 * @author fred
 */
public class ListNav extends AbtractBaseNavigator {
	private List<?> innerObject;
	
	ListNav(BeanDefinitionRegistry registry, List<?> value) {
		super(registry);
		this.innerObject = value;
	}
	
	public String value(int index) {
		Object objResult = innerObject.get(index);
		return buildString(objResult);
	}
	
	public BeanNav bean(int index) {
		Object objResult = innerObject.get(index);
		return buildBean(this.registry, objResult);
	}
	
	public Stream<String> streamString() {
		@SuppressWarnings("unchecked")
		List<TypedStringValue> beanList = (List<TypedStringValue>) innerObject;
		return beanList.stream().map(s -> s.getValue());
	}
	
	public Stream<BeanNav> streamBean() {
		@SuppressWarnings("unchecked")
		List<BeanDefinitionHolder> beanList = (List<BeanDefinitionHolder>) innerObject;
		return beanList.stream().map(m -> BeanNav.build(registry, m.getBeanDefinition()));
	}
}
