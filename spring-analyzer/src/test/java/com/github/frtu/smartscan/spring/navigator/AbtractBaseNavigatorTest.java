package com.github.frtu.smartscan.spring.navigator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;

public class AbtractBaseNavigatorTest {
	private BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

	private class TestBaseNavigator extends AbstractBaseNavigator {
		public TestBaseNavigator(BeanDefinitionRegistry registry) {
			super(registry);
		}
	};

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		new TestBaseNavigator(null);
	}


	@Test
	public void testBuildList() {
		ListBeansNav emptyBuildList = AbstractBaseNavigator.buildList(registry, null);
		assertEquals(0, emptyBuildList.streamBean().count());
	}

	@Test
	public void testBuildSet() {
		SetBeansNav emptyBuildSet = AbstractBaseNavigator.buildSet(registry, null);
		assertEquals(0, emptyBuildSet.streamBean().count());
	}

	@Test
	public void testBuildMap() {
		MapBeansNav emptyBuildMap = AbstractBaseNavigator.buildMap(registry, null);
		assertEquals(0, emptyBuildMap.streamEntries().count());
	}

}
