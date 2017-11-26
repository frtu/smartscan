package com.github.frtu.smartscan.spring.navigator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class BaseBuilder {
	static String buildString(BeanDefinitionRegistry registry, Object object) {
		if (object instanceof TypedStringValue) {
			TypedStringValue typedStringValue = (TypedStringValue) object;
			return typedStringValue.getValue();
		}
		throw new IllegalStateException("The target is not type or attribute <value> but rather :" + object.getClass());
	}

	static BeanNav buildBean(BeanDefinitionRegistry registry, Object object) {
		if (object instanceof RuntimeBeanReference) {
			RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) object;
			return BeanNav.build(registry, runtimeBeanReference.getBeanName());
		} else if (object instanceof BeanDefinitionHolder) {
			BeanDefinitionHolder beanDefinitionHolder = (BeanDefinitionHolder) object;
			return BeanNav.build(registry, beanDefinitionHolder.getBeanDefinition());
		}
		throw new IllegalStateException(
		        "The target is not type or attribute <ref> or <bean> but rather :" + object.getClass());
	}

	static ListBeansNav buildList(BeanDefinitionRegistry registry, Object value) {
		if (value == null) {
			// Friendly to stream APIs so that forEach doesn't have to test against null
			return new ListBeansNav(registry, (List<?>) null);
		}
		if (List.class.isAssignableFrom(value.getClass())) {
			return new ListBeansNav(registry, (List<?>) value);
		}
		throw new IllegalStateException("The <property> doesn't contains a <list> but rather :" + value.getClass());
	}

	static SetBeansNav buildSet(BeanDefinitionRegistry registry, Object value) {
		if (value == null) {
			// Friendly to stream APIs so that forEach doesn't have to test against null
			return new SetBeansNav(registry, (Set<?>) null);
		}
		if (Set.class.isAssignableFrom(value.getClass())) {
			return new SetBeansNav(registry, (Set<?>) value);
		}
		throw new IllegalStateException("The <property> doesn't contains a <map> but rather :" + value.getClass());
	}

	static MapBeansNav buildMap(BeanDefinitionRegistry registry, Object value) {
		if (value == null) {
			// Friendly to stream APIs so that forEach doesn't have to test against null
			return new MapBeansNav(registry, (Map<?, ?>) null);
		}
		if (Map.class.isAssignableFrom(value.getClass())) {
			return new MapBeansNav(registry, (Map<?, ?>) value);
		}
		throw new IllegalStateException("The <property> doesn't contains a <set> but rather :" + value.getClass());
	}

}
