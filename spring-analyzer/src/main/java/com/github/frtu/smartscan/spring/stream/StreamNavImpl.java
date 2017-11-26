package com.github.frtu.smartscan.spring.stream;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.github.frtu.smartscan.spring.navigator.AbstractBaseNavigator;
import com.github.frtu.smartscan.spring.navigator.BeanNav;

public class StreamNavImpl extends AbstractBaseNavigator implements StreamNav<BeanNav> {
	public StreamNavImpl(BeanDefinitionRegistry registry) {
		super(registry);
	}

	/**
	 * @param beanDefinitionRegistry
	 * @return
	 */
	public Stream<BeanNav> streamBean() {
		Stream<String> streamBeanName = Arrays.asList(getRegistry().getBeanDefinitionNames()).stream();
		return streamBeanName.map(beanName -> BeanNav.build(getRegistry(), beanName));
	}
}
