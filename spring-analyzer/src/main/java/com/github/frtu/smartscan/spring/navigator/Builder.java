package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Allow the Builders to be used as Functional Interfaces
 * 
 * @author fred
 * @since 2.4
 */
public interface Builder<T> {
	public T accept(BeanDefinitionRegistry registry, Object value);
}
