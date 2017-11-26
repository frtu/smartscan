package com.github.frtu.smartscan.spring.navigator;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class AbtractBaseNavigatorTest {
	private class TestBaseNavigator extends AbstractBaseNavigator {
		public TestBaseNavigator(BeanDefinitionRegistry registry) {
			super(registry);
		}
	};

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		new TestBaseNavigator(null);
	}
}
