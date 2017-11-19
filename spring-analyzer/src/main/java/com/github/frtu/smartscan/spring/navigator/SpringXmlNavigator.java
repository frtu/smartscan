package com.github.frtu.smartscan.spring.navigator;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.github.frtu.smartscan.spring.ClasspathXmlNavigator;

/**
 * Main class to deal with Spring XML Application Config file.
 * 
 * @author fred
 * @deprecated since 2.0, use {@link ClasspathXmlNavigator} rather !
 */
@Deprecated
public class SpringXmlNavigator extends AbstractSpringRegistryNavigator {
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
		XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(super.getRegistry());
		xmlBeanDefinitionReader.loadBeanDefinitions(springXmlResource);
	}

	/**
	 * Get the Bean using the parameter beanName.
	 * 
	 * @param beanName a Spring bean name
	 * @return an instance of BeanNav
	 * @deprecated use {@link #getBean(String)} instead
	 */
	@Deprecated
	public BeanNav bean(String beanName) {
		return getBean(beanName);
	}
}