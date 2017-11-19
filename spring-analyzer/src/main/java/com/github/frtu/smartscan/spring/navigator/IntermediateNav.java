package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public abstract class IntermediateNav extends AbtractBaseNavigator {
	protected IntermediateNav(BeanDefinitionRegistry registry) {
		super(registry);
	}

	protected abstract Object innerObject();

	/**
	 * Correspond to &lt;property name="integerProperty2" value="1"/&gt;
	 * 
	 * @return String value contained in the tag
	 */
	public String value() {
		return buildString(innerObject());
	}

	/**
	 * Correspond to &lt;property name="beanTwo" ref="yetAnotherBean"/&gt;
	 * 
	 * @return the BeanNav referred using ref
	 */
	public BeanNav ref() {
		return buildBean(super.getRegistry(), innerObject());
	}

	public ListBeansNav listBeans() {
		return buildList(super.getRegistry(), innerObject());
	}

	public SetBeansNav setBeans() {
		return buildSet(super.getRegistry(), innerObject());
	}

	public MapBeansNav mapBeans() {
		return buildMap(super.getRegistry(), innerObject());
	}
}