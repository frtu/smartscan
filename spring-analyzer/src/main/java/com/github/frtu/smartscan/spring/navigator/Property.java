package com.github.frtu.smartscan.spring.navigator;

import java.util.Map;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Represent one tag <property>
 * 
 * @author fred
 */
public class Property extends AbtractBaseNavigator {
	protected PropertyValue propertyValue;

	Property(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		super(registry);
		this.propertyValue = propertyValue;
	}

	static Property build(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		return new Property(registry, propertyValue);
	}

	/**
	 * Correspond to <property name="beanTwo" ref="yetAnotherBean"/>
	 * 
	 * @return
	 */
	static Bean buildRef(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		Object value = propertyValue.getValue();
		if (value instanceof RuntimeBeanReference) {
			RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;
			return Bean.build(registry, runtimeBeanReference.getBeanName());
		}
		throw new IllegalStateException(
				"The <entry> doesn't contains an attribute ref={} but rather :" + value.getClass());
	}

	/**
	 * Correspond to
	 * 
	 * <pre>
	 * <property name="sourceMap">
	 * 	<map>
	 * 		<entry key="porfiry" value="porfiry@gov.org" />
	 * 	</map>
	 * </property>
	 * </pre>
	 * 
	 * @return
	 */
	static MapProperty buildMap(BeanDefinitionRegistry registry, PropertyValue propertyValue) {
		Object value = propertyValue.getValue();
		if (Map.class.isAssignableFrom(value.getClass())) {
			return new MapProperty(registry, propertyValue, (Map<?, ?>) value);
		}
		throw new IllegalStateException("The <property> doesn't contains a <map> but rather :" + value.getClass());
	}

	/**
	 * Correspond to <property name="integerProperty2" value="1"/>
	 * 
	 * @return
	 */
	public String value() {
		Object value = propertyValue.getValue();
		if (value instanceof TypedStringValue) {
			TypedStringValue typedStringValue = (TypedStringValue) value;
			return typedStringValue.getValue();
		}
		throw new IllegalStateException("The <value> is not type of Integer but rather :" + value.getClass());
	}
}
