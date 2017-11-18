package com.github.frtu.smartscan.spring.navigator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Base class for all config navigation classes.
 * 
 * @author fred
 */
public abstract class AbtractBaseNavigator extends AbstractSpringRegistryNavigator {
	public AbtractBaseNavigator(BeanDefinitionRegistry registry) {
		super(registry);
	}

	protected static String buildString(Object object) {
		if (object instanceof TypedStringValue) {
			TypedStringValue typedStringValue = (TypedStringValue) object;
			return typedStringValue.getValue();
		}
		throw new IllegalStateException("The target is not type or attribute <value> but rather :" + object.getClass());
	}

	protected static BeanNav buildBean(BeanDefinitionRegistry registry, Object object) {
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

	protected static ListNav buildList(BeanDefinitionRegistry registry, Object value) {
		if (List.class.isAssignableFrom(value.getClass())) {
			return new ListNav(registry, (List<?>) value);
		}
		throw new IllegalStateException("The <property> doesn't contains a <map> but rather :" + value.getClass());
	}

	protected static SetNav buildSet(BeanDefinitionRegistry registry, Object value) {
		if (Set.class.isAssignableFrom(value.getClass())) {
			return new SetNav(registry, (Set<?>) value);
		}
		throw new IllegalStateException("The <property> doesn't contains a <map> but rather :" + value.getClass());
	}

	protected static MapNav buildMap(BeanDefinitionRegistry registry, Object value) {
		if (Map.class.isAssignableFrom(value.getClass())) {
			return new MapNav(registry, (Map<?, ?>) value);
		}
		throw new IllegalStateException("The <property> doesn't contains a <map> but rather :" + value.getClass());
	}
}