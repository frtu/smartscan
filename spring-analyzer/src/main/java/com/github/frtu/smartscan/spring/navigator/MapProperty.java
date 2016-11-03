package com.github.frtu.smartscan.spring.navigator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class MapProperty extends Property {
	private Map<TypedStringValue, Object> map;

	@SuppressWarnings("unchecked")
	MapProperty(BeanDefinitionRegistry registry, PropertyValue propertyValue, Map<?, ?> value) {
		super(registry, propertyValue);
		map = (Map<TypedStringValue, Object>) value;
	}

	/**
	 * Call this method if 
	 * <map><entry key="key" value="value"/></map>
	 * 
	 * @param key
	 * @return 
	 */
	public String value(String key) {
		Object objResult = map.get(new TypedStringValue(key));
		return checkTypedStringValue(objResult);
	}

	public void visitString(MapVisitor<String> mapVisitorString) {
		Set<Entry<TypedStringValue, Object>> entrySet = map.entrySet();
		for (Entry<TypedStringValue, Object> entry : entrySet) {
			String stringValue = checkTypedStringValue(entry.getValue());
			mapVisitorString.visit(entry.getKey().getValue(), stringValue);
		}
	}
	
	public HashMap<String, String> toMapString() {
		HashMap<String, String> result = new HashMap<>();
		visitString((key, value) -> result.put(key, value));
		return result;
	}
	

	/**
	 * Call this method if 
	 * <map><entry key="key" value-ref="reference"/></map>
	 * 
	 * @param key
	 * @return 
	 */
	public Bean ref(String key) {
		Object result = map.get(new TypedStringValue(key));
		return checkRuntimeBeanReference(result, this.registry);
	}

	public void visitBean(MapVisitor<Bean> mapVisitorBean) {
		Set<Entry<TypedStringValue, Object>> entrySet = map.entrySet();
		for (Entry<TypedStringValue, Object> entry : entrySet) {
			Bean bean = checkRuntimeBeanReference(entry.getValue(), this.registry);
			mapVisitorBean.visit(entry.getKey().getValue(), bean);
		}
	}

	public HashMap<String, Bean> toMapBean() {
		HashMap<String, Bean> result = new HashMap<>();
		visitBean((key, value) -> result.put(key, value));
		return result;
	}
}
