package spring.util;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;

/**
 * Helper class to extract data from {@link BeanDefinition}
 * All methods are stateless and reusable.
 * 
 * @author frdtu
 */
public class BeanDefinitionHelper {
	private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BeanDefinitionHelper.class);

	public BeanDefinitionHelper() {
		super();
	}

	/**
	 * Extract the <property> object from a beanDefinition.
	 * 
	 * @param beanDefinition
	 * @param propertyName
	 * @return
	 */
	public Object extractProperty(BeanDefinition beanDefinition, String propertyName) {
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		Object result = propertyValues.getPropertyValue(propertyName).getValue();
		logger.debug("Extract property name={} type={}", propertyName, result.getClass());
		return result;
	}

	/**
	 * Extract the String value or reference name from the <property> tag.
	 * 
	 * @param beanDefinition
	 * @param propertyName
	 * @return
	 */
	public String extractPropertyString(BeanDefinition beanDefinition, String propertyName) {
		Object valueObject = extractProperty(beanDefinition, propertyName);

		String propertyValue = null;
		if (valueObject instanceof TypedStringValue) {
			// <property name="propertyName" value="propertyValue"/>
			propertyValue = ((TypedStringValue) valueObject).getValue();
		} else if (valueObject instanceof RuntimeBeanReference) {
			// <property name="propertyName" ref="beanName"/>
			propertyValue = ((RuntimeBeanReference) valueObject).getBeanName();
		} else {
			logger.error("Cannot handle class={}  toString={}", valueObject.getClass(), valueObject.toString());
		}
		return propertyValue;
	}
}