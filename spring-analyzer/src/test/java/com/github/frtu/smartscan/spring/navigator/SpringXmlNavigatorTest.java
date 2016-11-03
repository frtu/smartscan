package com.github.frtu.smartscan.spring.navigator;

import static org.junit.Assert.*;

import java.util.HashMap;

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
	public void testOneLvlBean() {
		Bean bean = springXmlNavigator.bean("anotherExampleBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertFalse(bean.isClass((String) null));
		assertFalse(bean.isClass((Class<?>) null));
		assertTrue(bean.isClass("examples.AnotherBean"));
	}

	@Test
	public void testTwoLvlBean() {
		Bean bean = springXmlNavigator.bean("exampleBean");
		assertTrue(bean.isClass("examples.ExampleBean"));

		Bean beanProperty = bean.beanProperty("beanOne");
		assertNotNull(beanProperty);
		assertTrue(beanProperty.isClass("examples.AnotherBean"));

		Property property = bean.property("integerProperty");
		assertNotNull(property);
		assertEquals("1", property.value());
	}

	@Test
	public void testMapString() {
		Bean bean = springXmlNavigator.bean("emailsMap");
		MapProperty mapProperty = bean.mapProperty("sourceMap");
		assertNotNull(mapProperty);

		HashMap<String, String> mapString = mapProperty.toMapString();
		assertEquals(4, mapString.size());
		assertEquals("pechorin@hero.org", mapString.get("pechorin"));
	}

	@Test
	public void testEmptyBean() {
		Bean bean = springXmlNavigator.bean("noClassBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertTrue(bean.isClass((String) null));
		assertTrue(bean.isClass((Class<?>) null));
		assertFalse(bean.isClass("examples.AnotherBean"));
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testNonExistingBean() {
		Bean bean = springXmlNavigator.bean("nonExistingBean");
		assertNull(bean);
	}
}
