package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * Main class to deal with Spring XML Application Config file.
 * 
 * @author fred
 */
public class SpringXmlNavigator extends AbtractBaseNavigator {
	public SpringXmlNavigator() {
		super(new SimpleBeanDefinitionRegistry());
	}

	public SpringXmlNavigator(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public void loadXmlResource(String springXmlFile) {
		loadXmlResource(new DefaultResourceLoader().getResource(springXmlFile));
	}

	public void loadXmlResource(Resource springXmlResource) {
		XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(registry);
		xmlBeanDefinitionReader.loadBeanDefinitions(springXmlResource);
	}

	public Bean bean(String beanName) {
		return new Bean(this.registry, beanName);
	}
}