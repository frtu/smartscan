package com.github.frtu.smartscan.spring.navigator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Just kept for backward compatiblity check
 * 
 * @author fred
 */
@Deprecated
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
}
