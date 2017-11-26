package com.github.frtu.smartscan.spring.navigator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;

public class BaseBuilderTest {
	private BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

	@Test
	public void testBuildList() {
		ListBeansNav emptyBuildList = BaseBuilder.buildList(registry, null);
		assertEquals(0, emptyBuildList.streamBean().count());
	}

	@Test
	public void testBuildSet() {
		SetBeansNav emptyBuildSet = BaseBuilder.buildSet(registry, null);
		assertEquals(0, emptyBuildSet.streamBean().count());
	}

	@Test
	public void testBuildMap() {
		MapBeansNav emptyBuildMap = BaseBuilder.buildMap(registry, null);
		assertEquals(0, emptyBuildMap.streamEntries().count());
	}
}
