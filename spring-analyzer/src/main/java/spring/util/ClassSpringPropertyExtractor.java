package spring.util;

import java.util.HashMap;

import org.springframework.beans.factory.config.BeanDefinition;

public class ClassSpringPropertyExtractor {
	final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ClassSpringPropertyExtractor.class);

	private static final BeanDefinitionHelper BEAN_DEFINITION_HELPER = new BeanDefinitionHelper();

	private String classType;
	private String propertyName;
	
	private HashMap<String, String> data = new HashMap<>();
	
	public ClassSpringPropertyExtractor() {
		super();
	}

	public ClassSpringPropertyExtractor(String classType, String propertyName) {
		super();
		this.classType = classType;
		this.propertyName = propertyName;
	}

	public HashMap<String, String> getData() {
		return data;
	}

	protected void visitBeanDefinition(String beanDefinitionName, BeanDefinition beanDefinition) {
		if (classType.equals(beanDefinition.getBeanClassName())) {
			String propertyValue = BEAN_DEFINITION_HELPER.extractPropertyString(beanDefinition, propertyName);
			logger.debug("Found beanDefinitionName={} type={} propertyValue={}",
					new Object[] { beanDefinitionName, beanDefinition.getBeanClassName(), propertyValue });
			store(beanDefinitionName, propertyValue);
		}
	}

	private void store(String beanDefinitionName, String propertyValue) {
		data.put(beanDefinitionName, propertyValue);
	}
}