package com.github.frtu.smartscan.spring.navigator;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.frtu.smartscan.spring.ClasspathXmlNavigator;

public class BeanNavTest {
	private static ClasspathXmlNavigator classpathXmlNavigator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		classpathXmlNavigator = new ClasspathXmlNavigator("application-context-partB.xml",
		        "subfolder/application-context-partA.xml");
	}

	@Test
	public void testListBeans() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsList");
		ListBeansNav listProperty = bean.listBeansOf("sourceList");
		assertNotNull(listProperty);
		
		assertEquals("pechorin@hero.org", listProperty.value(0));
		assertEquals("raskolnikov@slums.org", listProperty.value(1));
		assertEquals("stavrogin@gov.org", listProperty.value(2));
		assertEquals("porfiry@gov.org", listProperty.value(3));
	}

	@Test
	public void testSetBeans() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsSet");
		SetBeansNav setNav = bean.setBeansOf("sourceSet");
		assertNotNull(setNav);
		
		String stringToFind = "pechorin@hero.org";
		Stream<String> streamValue = setNav.streamString();
		assertTrue("Cannot find in the Set the string:" + stringToFind,
		        streamValue.anyMatch(str -> str.equals(stringToFind)));
	}

	@Test
	public void testMapBeans() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsMap");
		MapBeansNav mapProperty = bean.mapBeansOf("sourceMap");
		assertNotNull(mapProperty);

		Map<String, String> mapString = mapProperty.toMapString();
		assertEquals(4, mapString.size());
		assertEquals("pechorin@hero.org", mapString.get("pechorin"));
	}

	@Test
	public void testValue() {
		BeanNav bean = classpathXmlNavigator.getBean("exampleBean");
		assertEquals("1", bean.valueOf("integerProperty"));
	}

	@Test
	public void testRef() {
		BeanNav bean = classpathXmlNavigator.getBean("exampleBean");
		BeanNav beanProperty = bean.refOf("beanOne");
		assertNotNull(beanProperty);
		assertTrue(beanProperty.isClass("examples.AnotherBean"));
	}
}
