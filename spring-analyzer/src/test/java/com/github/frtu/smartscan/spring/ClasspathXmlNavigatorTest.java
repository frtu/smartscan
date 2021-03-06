package com.github.frtu.smartscan.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.github.frtu.smartscan.spring.navigator.BeanNav;
import com.github.frtu.smartscan.spring.navigator.EntryNav;
import com.github.frtu.smartscan.spring.navigator.IntermediateNav;
import com.github.frtu.smartscan.spring.navigator.ListBeansNav;
import com.github.frtu.smartscan.spring.navigator.MapBeansNav;
import com.github.frtu.smartscan.spring.navigator.PropertyNav;
import com.github.frtu.smartscan.spring.navigator.SetBeansNav;

public class ClasspathXmlNavigatorTest {
	private static ClasspathXmlNavigator classpathXmlNavigator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		classpathXmlNavigator = new ClasspathXmlNavigator("application-context-partB.xml",
		        "subfolder/application-context-partA.xml");
	}

	@Test(expected = BeanDefinitionStoreException.class)
	public void testNonExistingPath() {
		new ClasspathXmlNavigator("NON_EXISTING.xml");
	}

	@Test
	public void testClasspathProtocol() {
		ClasspathXmlNavigator xmlNavigator = new ClasspathXmlNavigator(
		        "classpath:/subfolder/application-context-partA.xml");
		BeanNav bean = xmlNavigator.getBean("anotherExampleBean");
		assertNotNull(bean);
	}

	@Test
	public void testClasspathSimpleWildcard() {
		ClasspathXmlNavigator xmlNavigator = new ClasspathXmlNavigator("application-context-part*.xml");
		// Found application-context-partB.xml
		assertNotNull(xmlNavigator.getBean("exampleBean"));
		// Not found subfolder/application-context-partA.xml
		try {
			xmlNavigator.getBean("anotherExampleBean");
			assertTrue(false);
		} catch (NoSuchBeanDefinitionException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testClasspathSubfolderWildcard() {
		ClasspathXmlNavigator xmlNavigator = new ClasspathXmlNavigator("**/application-context-partA.xml");
		BeanNav bean = xmlNavigator.getBean("anotherExampleBean");
		assertNotNull(bean);
	}

	@Test
	public void testClasspathFullWildcard() {
		ClasspathXmlNavigator xmlNavigator = new ClasspathXmlNavigator("**/application-context-part*.xml");
		// Found application-context-partB.xml
		assertNotNull(xmlNavigator.getBean("exampleBean"));
		// Found subfolder/application-context-partA.xml
		assertNotNull(xmlNavigator.getBean("anotherExampleBean"));
	}

	@Test
	public void testFileProtocol() {
		ClasspathXmlNavigator xmlNavigator = new ClasspathXmlNavigator(
		        "file:src/test/resources/subfolder/application-context-partA.xml");
		BeanNav bean = xmlNavigator.getBean("anotherExampleBean");
		assertNotNull(bean);
	}

	@Test
	public void testOneLvlBean() {
		BeanNav bean = classpathXmlNavigator.getBean("anotherExampleBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertTrue(bean.isClass("examples.AnotherBean"));
	}

	@Test
	public void testOneLvlBeanOnlyId() {
		String beanName = "noClassBean";
		BeanNav bean = classpathXmlNavigator.getBean(beanName);
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertEquals(beanName, bean.id().get());
		assertFalse(bean.isClass("AnyClass"));
		
		PropertyNav emptyProperty = bean.property("no-property");
		assertNull(emptyProperty.value());
		assertNull(emptyProperty.ref());
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

		IntermediateNav integerProperty2 = bean.property("integerProperty2");
		assertNotNull(integerProperty2);
		assertEquals("1", integerProperty2.value());
	}

	@Test
	public void testListString() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsList");
		ListBeansNav listProperty = bean.property("sourceList").listBeans();
		assertNotNull(listProperty);

		assertEquals("pechorin@hero.org", listProperty.value(0));
		assertEquals("raskolnikov@slums.org", listProperty.value(1));
		assertEquals("stavrogin@gov.org", listProperty.value(2));
		assertEquals("porfiry@gov.org", listProperty.value(3));
	}

	@Test
	public void testListStreamString() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsList");
		ListBeansNav listProperty = bean.property("sourceList").listBeans();
		assertNotNull(listProperty);

		String stringToFind = "pechorin@hero.org";
		Stream<String> streamString = listProperty.streamString();
		assertTrue("Cannot find in the Set the string:" + stringToFind,
		        streamString.anyMatch(str -> str.equals(stringToFind)));
	}

	@Test
	public void testListStreamBean() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsListBean");
		ListBeansNav listProperty = bean.property("sourceList").listBeans();
		assertNotNull(listProperty);

		String className = "examples.AnotherBean";
		Stream<BeanNav> streamBean = listProperty.streamBean();
		assertTrue("Should have found in the list one class =" + className,
		        streamBean.anyMatch(beanNav -> beanNav.isClass(className)));
	}

	@Test
	public void testSetStreamString() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsSet");
		SetBeansNav setNav = bean.property("sourceSet").setBeans();
		assertNotNull(setNav);

		String stringToFind = "pechorin@hero.org";
		Stream<String> streamValue = setNav.streamString();
		assertTrue("Cannot find in the Set the string:" + stringToFind,
		        streamValue.anyMatch(str -> str.equals(stringToFind)));
	}

	@Test
	public void testSetStreamBean() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsSetBean");
		SetBeansNav setNav = bean.property("sourceSet").setBeans();
		assertNotNull(setNav);

		String className = "examples.ExampleBean";
		Stream<BeanNav> streamBean = setNav.streamBean();
		assertTrue("Should have found in the list one class =" + className,
		        streamBean.anyMatch(beanNav -> beanNav.isClass(className)));

		// A stream cannot be reused
		streamBean = setNav.streamBean();

		// <bean id="exampleBean1" class="examples.ExampleBean">
		// <property name="beanTwo" ref="yetAnotherBean1" />
		// </bean>
		// <bean id="exampleBean2" class="examples.ExampleBean">
		// <property name="beanTwo" ref="yetAnotherBean2" />
		// </bean>
		// <bean id="exampleBean3" class="examples.ExampleBean">
		// <property name="beanTwo" ref="yetAnotherBean3" />
		// </bean>
		// <bean id="exampleBean4" class="examples.ExampleBean">
		// <property name="beanTwo" ref="yetAnotherBean4" />
		// </bean>
		//
		// <bean id="yetAnotherBean1" class="examples.YetAnotherBean">
		// <property name="field" value="1"/>
		// </bean>
		// <bean id="yetAnotherBean2" class="examples.YetAnotherBean">
		// <property name="field" value="2"/>
		// </bean>
		// <bean id="yetAnotherBean3" class="examples.YetAnotherBean">
		// <property name="field" value="3"/>
		// </bean>
		// <bean id="yetAnotherBean4" class="examples.YetAnotherBean">
		// <property name="field" value="4"/>
		// </bean>

		List<String> collect = streamBean.filter(beanNav -> beanNav.isClass(className))
		        .map(beanNav -> beanNav.property("beanTwo").ref().property("field").value())
		        .collect(Collectors.toList());
		assertEquals("1", collect.get(0));
		assertEquals("2", collect.get(1));
		assertEquals("3", collect.get(2));
		assertEquals("4", collect.get(3));
	}

	@Test
	public void testMapConsumer() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsMap");
		MapBeansNav mapProperty = bean.property("sourceMap").mapBeans();
		assertNotNull(mapProperty);

		final AtomicInteger count = new AtomicInteger();
		mapProperty.forEachEntry(entryNav -> count.getAndIncrement());
		assertEquals(4, count.get());
	}

	@Test
	public void testMapString() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsMap");
		MapBeansNav mapProperty = bean.property("sourceMap").mapBeans();
		assertNotNull(mapProperty);

		Map<String, String> mapString = mapProperty.toMapString();
		assertEquals(4, mapString.size());
		assertEquals("pechorin@hero.org", mapString.get("pechorin"));
	}

	@Test
	public void testMapBean() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsBeanMap");
		MapBeansNav mapProperty = bean.property("sourceMap").mapBeans();
		assertNotNull(mapProperty);

		Map<String, BeanNav> mapBean = mapProperty.toMapBean();
		assertEquals(2, mapBean.size());
		assertTrue(mapBean.get("beanOne").isClass("examples.AnotherBean"));
		assertTrue(mapBean.get("beanTwo").isClass("examples.YetAnotherBean"));
	}

	@Test
	public void testMapListOfBeans() {
		// Test all kinds of embeded object Map that contains List of Bean of
		// Property of ref
		BeanNav bean = classpathXmlNavigator.getBean("allComposite");
		MapBeansNav mapProperty = bean.property("sourceMap").mapBeans();
		assertNotNull(mapProperty);

		EntryNav entry = mapProperty.entry("beanOne");
		BeanNav firstBeanOfList = entry.listBeans().bean(0);
		assertTrue(firstBeanOfList.isClass("examples.ExampleBean"));
		BeanNav beanNav = firstBeanOfList.property("beanTwo").ref();
		assertTrue(beanNav.isClass("examples.AnotherBean"));
	}

	@Test
	public void testMapListOfBeansStreamKey() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsMap");
		MapBeansNav mapProperty = bean.property("sourceMap").mapBeans();
		assertNotNull(mapProperty);

		Stream<String> streamKeys = mapProperty.streamKeys();

		String stringToFind = "raskolnikov";
		assertTrue("Cannot find in the Set the key :" + stringToFind,
		        streamKeys.anyMatch(str -> str.equals(stringToFind)));
	}

	@Test
	public void testMapListOfBeansStreamEntries() {
		BeanNav bean = classpathXmlNavigator.getBean("emailsMap");
		MapBeansNav mapProperty = bean.property("sourceMap").mapBeans();
		assertNotNull(mapProperty);

		Stream<EntryNav> streamEntries = mapProperty.streamEntries();

		String stringToFind = "stavrogin@gov.org";
		assertTrue("Cannot find in the Set the key :" + stringToFind,
		        streamEntries.anyMatch(entryNav -> entryNav.value().equals(stringToFind)));
	}

	@Test
	public void testEmptyBean() {
		BeanNav bean = classpathXmlNavigator.getBean("noClassBean");
		assertNotNull(bean);
		assertNotNull(bean.getBeanDefinition());
		assertFalse(bean.isClass("examples.AnotherBean"));
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testNonExistingBean() {
		BeanNav bean = classpathXmlNavigator.getBean("nonExistingBean");
		assertNull(bean);
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testMapEntryNameNull() {
		BeanNav bean = classpathXmlNavigator.getBean("allComposite");
		MapBeansNav mapProperty = bean.property("sourceMap").mapBeans();
		mapProperty.entry(null);
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testMapEntryNonExistent() {
		BeanNav bean = classpathXmlNavigator.getBean("allComposite");
		MapBeansNav mapProperty = bean.property("sourceMap").mapBeans();
		mapProperty.entry("nonExistentEntry");
	}

	@Test
	public void testStreamAllProperty() {
		Stream<BeanNav> streamBean = classpathXmlNavigator.streamBean();
		List<String> fields = streamBean.filter(beanNav -> beanNav.isClass("examples.YetAnotherBean"))
		        .map(beanNav -> beanNav.property("field").value()).collect(Collectors.toList());
		assertEquals(6, fields.size());
	}

	@Test
	public void testStreamAllId() {
		Stream<BeanNav> streamBean = classpathXmlNavigator.streamBean();
		List<Optional<String>> fields = streamBean.filter(beanNav -> beanNav.isClass("examples.YetAnotherBean"))
		        .map(beanNav -> beanNav.id()).collect(Collectors.toList());
		
		assertEquals(6, fields.size());
	}
}
