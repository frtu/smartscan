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
public class MapNav extends AbtractBaseNavigator {
	private Map<TypedStringValue, Object> innerObject;

	@SuppressWarnings("unchecked")
	MapNav(BeanDefinitionRegistry registry, Map<?, ?> value) {
		super(registry);
		innerObject = (Map<TypedStringValue, Object>) value;
	}

	/**
	 * Call this method if 
	 * <map><entry key="key">
	 * 
	 * @param key
	 * @return 
	 */
	public EntryNav entry(String entryName) {
		Object result = innerObject.get(new TypedStringValue(entryName));
		return new EntryNav(this.registry, result);
	}

	public void visit(MapVisitor<EntryNav> mapVisitorString) {
		Set<Entry<TypedStringValue, Object>> entrySet = innerObject.entrySet();
		for (Entry<TypedStringValue, Object> entry : entrySet) {
			EntryNav entryNav = new EntryNav(this.registry, entry.getValue());
			mapVisitorString.visit(entry.getKey().getValue(), entryNav);
		}
	}
	
	public HashMap<String, String> toMapString() {
		HashMap<String, String> result = new HashMap<>();
		visit((key, entry) -> result.put(key, entry.value()));
		return result;
	}

	public HashMap<String, BeanNav> toMapBean() {
		HashMap<String, BeanNav> result = new HashMap<>();
		visit((key, entry) -> result.put(key, entry.ref()));
		return result;
	}
}
