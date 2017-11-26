package com.github.frtu.smartscan.spring.registry;

import java.util.stream.Stream;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.github.frtu.smartscan.spring.navigator.BeanNav;
import com.github.frtu.smartscan.spring.stream.StreamNav;
import com.github.frtu.smartscan.spring.stream.StreamNavImpl;

/**
 * Base class for all Spring configurations location &amp; initialization
 * 
 * Call {{@link #afterPropertiesSet()}, before calling {{@link #getBean(String)}
 * 
 * @author fred
 * @since 2.0
 */
public abstract class AbstractSpringRegistryNavigator implements InitializingBean {
	private String[] configLocations;

	private BeanDefinitionRegistry registry;

	/**
	 * Create a new RegistryNavigator for bean-style configuration.
	 * 
	 * @see #setConfigLocation
	 * @see #setConfigLocations
	 * @see #afterPropertiesSet()
	 */
	public AbstractSpringRegistryNavigator() {
		this(new SimpleBeanDefinitionRegistry());
	}

	public AbstractSpringRegistryNavigator(BeanDefinitionRegistry registry) {
		super();
		this.registry = registry;
	}

	protected BeanDefinitionRegistry getRegistry() {
		return registry;
	}

	/**
	 * Get a Bean definition using his beanName.
	 * 
	 * @param beanName a Spring bean name
	 * @return an instance of BeanNav
	 */
	public BeanNav getBean(String beanName) {
		return BeanNav.build(this.registry, beanName);
	}

	private StreamNav streamNav() {
		return new StreamNavImpl(this.registry);
	}
	
	public Stream<BeanNav> streamBean() {
		return new StreamNavImpl(this.registry).streamBean();
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		loadBeanDefinitions();
	}

	/**
	 * Inspired from {@link AbstractXmlApplicationContext#loadBeanDefinitions(DefaultListableBeanFactory)}
	 */
	protected void loadBeanDefinitions() {
		if (configLocations != null) {
			XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(getRegistry());
			// Allow to use AntPathMatcher for location declaration
			xmlBeanDefinitionReader.setResourceLoader(new PathMatchingResourcePatternResolver());
			xmlBeanDefinitionReader.loadBeanDefinitions(configLocations);
		} else {
			throw new IllegalStateException("Please call setConfigLocation methods with some Xml Config paths!");
		}
	}

	/**
	 * Set the config locations for this application context in init-param style, i.e. with distinct locations separated
	 * by commas, semicolons or whitespace.
	 * <p>
	 * If not set, the implementation may use a default as appropriate.
	 * 
	 * @param location resource location
	 */
	public void setConfigLocation(String location) {
		setConfigLocations(StringUtils.tokenizeToStringArray(location, CONFIG_LOCATION_DELIMITERS));
	}

	/**
	 * Inspired from {@link AbstractRefreshableConfigApplicationContext#setConfigLocations(String...)}
	 * 
	 * Set the config locations for this application context.
	 * <p>
	 * If not set, the implementation may use a default as appropriate.
	 * 
	 * @param locations array of resource locations
	 */
	public void setConfigLocations(String... locations) {
		if (locations != null) {
			Assert.noNullElements(locations, "Config locations must not be null");
			this.configLocations = new String[locations.length];
			for (int i = 0; i < locations.length; i++) {
				this.configLocations[i] = resolvePath(locations[i]).trim();
			}
		} else {
			this.configLocations = null;
		}
	}

	protected String resolvePath(String path) {
		// Extends to resolve placeholder in the path ${} IF needed.
		// Can use new PropertySourcesPropertyResolver(this.propertySources);
		return path;
	}

	/**
	 * Any number of these characters are considered delimiters between multiple context config paths in a single String
	 * value.
	 */
	String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
}