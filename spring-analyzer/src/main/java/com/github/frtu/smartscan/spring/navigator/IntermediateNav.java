package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public abstract class IntermediateNav extends AbtractBaseNavigator {
	protected IntermediateNav(BeanDefinitionRegistry registry) {
		super(registry);
	}

	protected abstract Object innerObject();

	/**
	 * Correspond to <property name="integerProperty2" value="1"/>
	 * 
	 * @return
	 */
	public String value() {
		return buildString(innerObject());
	}

	/**
	 * Correspond to <property name="beanTwo" ref="yetAnotherBean"/>
	 * 
	 * @return
	 */
	public BeanNav ref() {
		return buildBean(this.registry, innerObject());
	}

	public ListNav list() {
		return buildList(this.registry, innerObject());
	}

	public SetNav set() {
		return buildSet(this.registry, innerObject());
	}

	public MapNav map() {
		return buildMap(this.registry, innerObject());
	}
}