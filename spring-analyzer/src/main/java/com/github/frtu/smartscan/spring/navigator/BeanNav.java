package com.github.frtu.smartscan.spring.navigator;

import java.util.Optional;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.Assert;

/**
 * Represent one tag &lt;bean&gt;
 * 
 * @author fred
 */
public class BeanNav extends AbstractBaseNavigator {
	private Optional<String> id;
	private BeanDefinition beanDefinition;
	
	private BeanNav(BeanDefinitionRegistry registry, Optional<String> id, BeanDefinition beanDefinition) {
		super(registry);
		this.id = id;
		this.beanDefinition = beanDefinition;
	}

	static BeanNav build(BeanDefinitionRegistry registry, String beanName) throws NoSuchBeanDefinitionException {
		BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
		Assert.notNull(beanDefinition, String.format("beanName=%s is not found in the registry", beanName));
		return build(registry, Optional.ofNullable(beanName), beanDefinition);
	}

	static BeanNav build(BeanDefinitionRegistry registry, BeanDefinition beanDefinition) {
		return build(registry, Optional.empty(), beanDefinition);
	}
	
	static BeanNav build(BeanDefinitionRegistry registry, Optional<String> id, BeanDefinition beanDefinition) {
		return new BeanNav(registry, id, beanDefinition);
	}

	/**
	 * Check if the current &lt;bean class&gt; class attribute from bean tag is equals.
	 * @param clazz Class instance.
	 * @return if logically equals
	 */
	public boolean isClass(Class<?> clazz) {
		Assert.notNull(clazz, "clazz parameter cannot be null!");
		return isClass(clazz.getCanonicalName());
	}

	/**
	 * Check if the current &lt;bean class&gt; class attribute from bean tag is equals.
	 * @param className Class name.
	 * @return if logically equals
	 */
	public boolean isClass(String className) {
		Assert.notNull(className, "className parameter cannot be null");
		
		if (beanDefinition.getBeanClassName() != null) {
			return beanDefinition.getBeanClassName().equals(className);
		}
		return (className == null);
	}
	
	/**
	 * Get the id attribute when exist from &lt;bean id&gt;. May return null.
	 * @return Id attribute
	 * @since 2.3
	 */
	public Optional<String> id() {
		return id;
	}

	public BeanDefinition getBeanDefinition() {
		return beanDefinition;
	}
	
	/**
	 * Navigate to the inner &lt;property&gt; tag when exist. DOESN'T RETURN NULL for Stream api friendly purpose but
	 * all following methods will return null!
	 * 
	 * @param propertyName the name of the property
	 * @return Property nav object
	 */
	public PropertyNav property(String propertyName) {
		PropertyValue propertyValue = beanDefinition.getPropertyValues().getPropertyValue(propertyName);
		return new PropertyNav(super.getRegistry(), propertyValue);
	}
	
	/**
	 * Correspond to &lt;property name="beanTwo"&gt;&lt;list&gt;
	 * 
	 * @param propertyName the name of the property
	 * @return the list of BeanNav referred using list tag
	 * @since 2.4
	 */
	public ListBeansNav listBeansOf(String propertyName) {
		return buildWithPropertyValue(propertyName, AbstractBaseNavigator::buildList, false);
	}

	/**
	 * Correspond to &lt;property name="beanTwo"&gt;&lt;set&gt;
	 * 
	 * @param propertyName the name of the property
	 * @return the set of BeanNav referred using set tag
	 * @since 2.4
	 */
	public SetBeansNav setBeansOf(String propertyName) {
		return buildWithPropertyValue(propertyName, AbstractBaseNavigator::buildSet, false);
	}

	/**
	 * Correspond to &lt;property name="beanTwo"&gt;&lt;map&gt;
	 * 
	 * @param propertyName the name of the property
	 * @return the map of BeanNav referred using map tag
	 * @since 2.4
	 */
	public MapBeansNav mapBeansOf(String propertyName) {
		return buildWithPropertyValue(propertyName, AbstractBaseNavigator::buildMap, false);
	}
	
	/**
	 * Correspond to &lt;property name="integerProperty2" value="1"/&gt;
	 * 
	 * @param propertyName the name of the property
	 * @return String value contained in the tag
	 * @since 2.4
	 */
	public String valueOf(String propertyName) {
		return buildWithPropertyValue(propertyName, AbstractBaseNavigator::buildString, true);
	}

	/**
	 * Correspond to &lt;property name="beanTwo" ref="yetAnotherBean"/&gt;
	 * 
	 * @param propertyName the name of the property
	 * @return the BeanNav referred using ref
	 * @since 2.4
	 */
	public BeanNav refOf(String propertyName) {
		return buildWithPropertyValue(propertyName, AbstractBaseNavigator::buildBean, true);
	}

	/*
	 * @param propertyName the name of the property
	 * @param builder
	 * @return
	 */
	private <T> T buildWithPropertyValue(String propertyName, Builder<T> builder, boolean isReturnNull) {
		PropertyValue propertyValue = beanDefinition.getPropertyValues().getPropertyValue(propertyName);
		if (propertyValue == null) {
			if (isReturnNull) {
				return null;
			}
			return builder.accept(super.getRegistry(), null);
		}
		return builder.accept(super.getRegistry(), propertyValue.getValue());
	}
}
