package com.github.frtu.smartscan.spring.navigator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;

public class AbtractBaseNavigatorTest {
	private BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

	@Test
	public void testBuildList() {
		ListBeansNav emptyBuildList = AbtractBaseNavigator.buildList(registry, null);
		assertEquals(0, emptyBuildList.streamBean().count());
	}

	@Test
	public void testBuildSet() {
		SetBeansNav emptyBuildSet = AbtractBaseNavigator.buildSet(registry, null);
		assertEquals(0, emptyBuildSet.streamBean().count());
	}

	@Test
	public void testBuildMap() {
		MapBeansNav emptyBuildMap = AbtractBaseNavigator.buildMap(registry, null);
		assertEquals(0, emptyBuildMap.streamEntries().count());
	}

}
