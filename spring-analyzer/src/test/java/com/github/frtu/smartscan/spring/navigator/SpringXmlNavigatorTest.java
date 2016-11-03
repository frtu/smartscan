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
		BeanNav bean = springXmlNavigator.bean("anotherExampleBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertFalse(bean.isClass((String) null));
		assertFalse(bean.isClass((Class<?>) null));
		assertTrue(bean.isClass("examples.AnotherBean"));
	}

	@Test
	public void testTwoLvlBean() {
		BeanNav bean = springXmlNavigator.bean("exampleBean");
		assertTrue(bean.isClass("examples.ExampleBean"));

		BeanNav beanProperty = bean.property("beanOne").ref();
		assertNotNull(beanProperty);
		assertTrue(beanProperty.isClass("examples.AnotherBean"));

		IntermediateNav property = bean.property("integerProperty");
		assertNotNull(property);
		assertEquals("1", property.value());
	}

	@Test
	public void testListString() {
		BeanNav bean = springXmlNavigator.bean("emailsList");
		ListNav listProperty = bean.property("sourceList").list();
		assertNotNull(listProperty);

		assertEquals("pechorin@hero.org", listProperty.value(0));
		assertEquals("raskolnikov@slums.org", listProperty.value(1));
		assertEquals("stavrogin@gov.org", listProperty.value(2));
		assertEquals("porfiry@gov.org", listProperty.value(3));
	}

	@Test
	public void testMapString() {
		BeanNav bean = springXmlNavigator.bean("emailsMap");
		MapNav mapProperty = bean.property("sourceMap").map();
		assertNotNull(mapProperty);

		HashMap<String, String> mapString = mapProperty.toMapString();
		assertEquals(4, mapString.size());
		assertEquals("pechorin@hero.org", mapString.get("pechorin"));
	}

	@Test
	public void testMapBean() {
		BeanNav bean = springXmlNavigator.bean("emailsBeanMap");
		MapNav mapProperty = bean.property("sourceMap").map();
		assertNotNull(mapProperty);

		HashMap<String, BeanNav> mapBean = mapProperty.toMapBean();
		assertEquals(2, mapBean.size());
		assertTrue(mapBean.get("beanOne").isClass("examples.AnotherBean"));
		assertTrue(mapBean.get("beanTwo").isClass("examples.YetAnotherBean"));
	}

	@Test
	public void testEmptyBean() {
		BeanNav bean = springXmlNavigator.bean("noClassBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertTrue(bean.isClass((String) null));
		assertTrue(bean.isClass((Class<?>) null));
		assertFalse(bean.isClass("examples.AnotherBean"));
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testNonExistingBean() {
		BeanNav bean = springXmlNavigator.bean("nonExistingBean");
		assertNull(bean);
	}
}
