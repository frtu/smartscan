package com.github.frtu.smartscan.spring.navigator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Support <map> tag.
 * 
 * @author fred
 */
public class MapProperty extends AbtractBaseNavigator {
	private Map<TypedStringValue, Object> innerObject;

	@SuppressWarnings("unchecked")
	MapProperty(BeanDefinitionRegistry registry, Map<?, ?> value) {
		super(registry);
		innerObject = (Map<TypedStringValue, Object>) value;
	}

	/**
	 * Call this method if 
	 * <map><entry key="key" value="value"/></map>
	 * 
	 * @param key
	 * @return 
	 */
	public String value(String key) {
		Object objResult = innerObject.get(new TypedStringValue(key));
		return buildString(objResult);
	}

	public void visitString(MapVisitor<String> mapVisitorString) {
		Set<Entry<TypedStringValue, Object>> entrySet = innerObject.entrySet();
		for (Entry<TypedStringValue, Object> entry : entrySet) {
			String stringValue = buildString(entry.getValue());
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
		Object result = innerObject.get(new TypedStringValue(key));
		return buildBean(this.registry, result);
	}

	public void visitBean(MapVisitor<Bean> mapVisitorBean) {
		Set<Entry<TypedStringValue, Object>> entrySet = innerObject.entrySet();
		for (Entry<TypedStringValue, Object> entry : entrySet) {
			Bean bean = buildBean(this.registry, entry.getValue());
			mapVisitorBean.visit(entry.getKey().getValue(), bean);
		}
	}

	public HashMap<String, Bean> toMapBean() {
		HashMap<String, Bean> result = new HashMap<>();
		visitBean((key, value) -> result.put(key, value));
		return result;
	}
}
