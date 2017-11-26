package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public abstract class IntermediateNav extends AbstractBaseNavigator {
	protected IntermediateNav(BeanDefinitionRegistry registry) {
		super(registry);
	}

	/**
	 * Return the encapsulated object. MAY RETURN NULL.
	 * 
	 * @return internal object
	 */
	protected abstract Object innerObject();

	/**
	 * Correspond to &lt;property name="integerProperty2" value="1"/&gt;
	 * 
	 * @return String value contained in the tag
	 */
	public String value() {
		if (innerObject() == null) {
			return null;
		}
		return BaseBuilder.buildString(super.getRegistry(), innerObject());
	}

	/**
	 * Correspond to &lt;property name="beanTwo" ref="yetAnotherBean"/&gt;
	 * 
	 * @return the BeanNav referred using ref
	 */
	public BeanNav ref() {
		if (innerObject() == null) {
			return null;
		}
		return BaseBuilder.buildBean(super.getRegistry(), innerObject());
	}

	/**
	 * Correspond to &lt;property name="beanTwo"&gt;&lt;list&gt;
	 * 
	 * @return the list of BeanNav referred using list tag
	 */
	public ListBeansNav listBeans() {
		return BaseBuilder.buildList(super.getRegistry(), innerObject());
	}

	/**
	 * Correspond to &lt;property name="beanTwo"&gt;&lt;set&gt;
	 * 
	 * @return the set of BeanNav referred using set tag
	 */
	public SetBeansNav setBeans() {
		return BaseBuilder.buildSet(super.getRegistry(), innerObject());
	}

	/**
	 * Correspond to &lt;property name="beanTwo"&gt;&lt;map&gt;
	 * 
	 * @return the map of BeanNav referred using map tag
	 */
	public MapBeansNav mapBeans() {
		return BaseBuilder.buildMap(super.getRegistry(), innerObject());
	}
}