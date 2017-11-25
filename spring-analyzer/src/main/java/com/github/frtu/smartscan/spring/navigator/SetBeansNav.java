package com.github.frtu.smartscan.spring.navigator;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Support &lt;set&gt; tag.
 * 
 * @author fred
 */
public class SetBeansNav extends AbstractBaseNavigator {
	private Set<?> innerObject;

	SetBeansNav(BeanDefinitionRegistry registry, Set<?> value) {
		super(registry);
		this.innerObject = value;
	}
	
	public Stream<String> streamString() {
		if (this.innerObject == null) {
			return Stream.empty();
		}
		@SuppressWarnings("unchecked")
		Set<TypedStringValue> beanSet = (Set<TypedStringValue>) innerObject;
		return beanSet.stream().map(s -> s.getValue());
	}
	
	public Stream<BeanNav> streamBean() {
		if (this.innerObject == null) {
			return Stream.empty();
		}
		@SuppressWarnings("unchecked")
		Set<BeanDefinitionHolder> beanSet = (Set<BeanDefinitionHolder>) innerObject;
		return beanSet.stream().map(m -> BeanNav.build(super.getRegistry(), m.getBeanDefinition()));
	}
}
