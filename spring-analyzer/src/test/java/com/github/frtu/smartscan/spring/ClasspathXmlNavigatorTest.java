package com.github.frtu.smartscan.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.github.frtu.smartscan.spring.navigator.BeanNav;
import com.github.frtu.smartscan.spring.navigator.EntryNav;
import com.github.frtu.smartscan.spring.navigator.IntermediateNav;
import com.github.frtu.smartscan.spring.navigator.ListNav;
import com.github.frtu.smartscan.spring.navigator.MapNav;
import com.github.frtu.smartscan.spring.navigator.SetNav;

public class ClasspathXmlNavigatorTest {
	private static ClasspathXmlNavigator classpathXmlNavigator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		classpathXmlNavigator = new ClasspathXmlNavigator("file:src/test/resources/application-context-partA.xml",
		        "file:src/test/resources/application-context-partB.xml");
	}

	@Test(expected = BeanDefinitionStoreException.class)
	public void testNonExistingFile() {
		new ClasspathXmlNavigator("file:src/test/resources/NON_EXISTING.xml");
	}

	@Test
	public void testOneLvlBean() {
		BeanNav bean = classpathXmlNavigator.getBean("anotherExampleBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertFalse(bean.isClass((String) null));
		assertFalse(bean.isClass((Class<?>) null));
		assertTrue(bean.isClass("examples.AnotherBean"));
	}

	@Test
	public void testTwoLvlBean() {
		BeanNav bean = classpathXmlNavigator.getBean("exampleBean");
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
		BeanNav bean = classpathXmlNavigator.getBean("emailsList");
		ListNav listProperty = bean.property("sourceList").list();
		assertNotNull(listProperty);

		assertEquals("pechorin@hero.org", listProperty.value(0));
		assertEquals("raskolnikov@slums.org", listProperty.value(1));
		assertEquals("stavrogin@gov.org", listProperty.value(2));
		assertEquals("porfiry@gov.org", listProperty.value(3));
	}

	@Test
	public void testListStreamString() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsList");
		ListNav listProperty = bean.property("sourceList").list();
		assertNotNull(listProperty);

		String stringToFind = "pechorin@hero.org";
		Stream<String> streamString = listProperty.streamString();
		assertTrue("Cannot find in the Set the string:" + stringToFind,
		        streamString.anyMatch(str -> str.equals(stringToFind)));
	}

	@Test
	public void testListStreamBean() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsListBean");
		ListNav listProperty = bean.property("sourceList").list();
		assertNotNull(listProperty);

		String className = "examples.AnotherBean";
		Stream<BeanNav> streamBean = listProperty.streamBean();
		assertTrue("Should have found in the list one class =" + className,
		        streamBean.anyMatch(beanNav -> beanNav.isClass(className)));
	}

	@Test
	public void testSetStreamString() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsSet");
		SetNav setNav = bean.property("sourceSet").set();
		assertNotNull(setNav);

		String stringToFind = "pechorin@hero.org";
		Stream<String> streamValue = setNav.streamString();
		assertTrue("Cannot find in the Set the string:" + stringToFind,
		        streamValue.anyMatch(str -> str.equals(stringToFind)));
	}

	@Test
	public void testSetStreamBean() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsSetBean");
		SetNav setNav = bean.property("sourceSet").set();
		assertNotNull(setNav);

		String className = "examples.AnotherBean";
		Stream<BeanNav> streamBean = setNav.streamBean();
		assertTrue("Should have found in the list one class =" + className,
		        streamBean.anyMatch(beanNav -> beanNav.isClass(className)));
	}

	@Test
	public void testMapString() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsMap");
		MapNav mapProperty = bean.property("sourceMap").map();
		assertNotNull(mapProperty);

		HashMap<String, String> mapString = mapProperty.toMapString();
		assertEquals(4, mapString.size());
		assertEquals("pechorin@hero.org", mapString.get("pechorin"));
	}

	@Test
	public void testMapBean() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsBeanMap");
		MapNav mapProperty = bean.property("sourceMap").map();
		assertNotNull(mapProperty);

		HashMap<String, BeanNav> mapBean = mapProperty.toMapBean();
		assertEquals(2, mapBean.size());
		assertTrue(mapBean.get("beanOne").isClass("examples.AnotherBean"));
		assertTrue(mapBean.get("beanTwo").isClass("examples.YetAnotherBean"));
	}

	@Test
	public void testMapListOfBeans() {
		// Test all kinds of embeded object Map that contains List of Bean of
		// Property of ref
		BeanNav bean = classpathXmlNavigator.getBean("allComposite");
		MapNav mapProperty = bean.property("sourceMap").map();
		assertNotNull(mapProperty);

		EntryNav entry = mapProperty.entry("beanOne");
		BeanNav firstBeanOfList = entry.list().bean(0);
		assertTrue(firstBeanOfList.isClass("examples.ExampleBean"));
		BeanNav beanNav = firstBeanOfList.property("beanTwo").ref();
		assertTrue(beanNav.isClass("examples.AnotherBean"));
	}

	@Test
	public void testEmptyBean() {
		BeanNav bean = classpathXmlNavigator.getBean("noClassBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertTrue(bean.isClass((String) null));
		assertTrue(bean.isClass((Class<?>) null));
		assertFalse(bean.isClass("examples.AnotherBean"));
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testNonExistingBean() {
		BeanNav bean = classpathXmlNavigator.getBean("nonExistingBean");
		assertNull(bean);
	}
}
