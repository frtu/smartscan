package com.github.frtu.smartscan.spring;

import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.frtu.smartscan.spring.registry.AbstractSpringRegistryNavigator;

/**
 * Very similar usage like {@link ClassPathXmlApplicationContext}.
 * 
 * Call {{@link #afterPropertiesSet()}, before calling {{@link #getBean(String)}
 * 
 * @author fred
 * @since 2.0
 */
public class ClasspathXmlNavigator extends AbstractSpringRegistryNavigator {
	/**
	 * Create a new ClasspathXmlNavigator for bean-style configuration.
	 * 
	 * @see #setConfigLocation
	 * @see #setConfigLocations
	 * @see #afterPropertiesSet()
	 */
	public ClasspathXmlNavigator() {
		super(new SimpleBeanDefinitionRegistry());
	}

	/**
	 * Create a new ClasspathXmlNavigator, loading the definitions
	 * from the given XML file.
	 * @param configLocation resource location
	 */
	public ClasspathXmlNavigator(String configLocation) {
		this();
		setConfigLocation(configLocation);
		loadBeanDefinitions();
	}

	/**
	 * Create a new ClasspathXmlNavigator, loading the definitions
	 * from the given XML files.
	 * @param configLocations array of resource locations
	 */
	public ClasspathXmlNavigator(String... configLocations) {
		this();
		setConfigLocations(configLocations);
		loadBeanDefinitions();
	}
}