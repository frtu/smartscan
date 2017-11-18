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
	 * @return
	 */
	public String value() {
		return buildString(innerObject());
	}

	/**
	 * Correspond to &lt;property name="beanTwo" ref="yetAnotherBean"/&gt;
	 * 
	 * @return
	 */
	public BeanNav ref() {
		return buildBean(super.getRegistry(), innerObject());
	}

	public ListNav list() {
		return buildList(super.getRegistry(), innerObject());
	}

	public SetNav set() {
		return buildSet(super.getRegistry(), innerObject());
	}

	public MapNav map() {
		return buildMap(super.getRegistry(), innerObject());
	}
}