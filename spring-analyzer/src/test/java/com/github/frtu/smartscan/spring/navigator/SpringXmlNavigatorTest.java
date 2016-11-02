package com.github.frtu.smartscan.spring.navigator;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class SpringXmlNavigatorTest {
	private static SpringXmlNavigator springXmlNavigator = new SpringXmlNavigator();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		springXmlNavigator.loadXmlResource("file:src/test/resources/application-context.xml");
	}

	@Test
	public void testExistingBean() {
		Bean bean = springXmlNavigator.bean("exampleBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
	}

	@Test(expected=NoSuchBeanDefinitionException.class)
	public void testNonExistingBean() {
		Bean bean = springXmlNavigator.bean("nonExistingBean");
		assertNull(bean);
	}
}
