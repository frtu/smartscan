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
public class ListBeansNav extends AbstractBaseNavigator {
	private List<?> innerObject;
	
	ListBeansNav(BeanDefinitionRegistry registry, List<?> value) {
		super(registry);
		this.innerObject = value;
	}
	
	public String value(int index) {
		if (this.innerObject == null) {
			return null;
		}
		Object objResult = innerObject.get(index);
		return buildString(super.getRegistry(), objResult);
	}
	
	public BeanNav bean(int index) {
		if (this.innerObject == null) {
			return null;
		}
		Object objResult = innerObject.get(index);
		return buildBean(super.getRegistry(), objResult);
	}
	
	public Stream<String> streamString() {
		if (this.innerObject == null) {
			return Stream.empty();
		}
		@SuppressWarnings("unchecked")
		List<TypedStringValue> beanList = (List<TypedStringValue>) innerObject;
		return beanList.stream().map(s -> s.getValue());
	}
	
	public Stream<BeanNav> streamBean() {
		if (this.innerObject == null) {
			return Stream.empty();
		}
		@SuppressWarnings("unchecked")
		List<BeanDefinitionHolder> beanList = (List<BeanDefinitionHolder>) innerObject;
		return beanList.stream().map(m -> BeanNav.build(super.getRegistry(), m.getBeanDefinition()));
	}
}
