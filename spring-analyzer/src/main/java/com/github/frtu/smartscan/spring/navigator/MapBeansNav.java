package com.github.frtu.smartscan.spring.navigator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Support &lt;map&gt; tag.
 * 
 * @author fred
 */
public class MapBeansNav extends AbtractBaseNavigator {
	private Map<TypedStringValue, Object> innerObject;

	@SuppressWarnings("unchecked")
	MapBeansNav(BeanDefinitionRegistry registry, Map<?, ?> value) {
		super(registry);
		innerObject = (Map<TypedStringValue, Object>) value;
	}

	/**
	 * Get a Stream of all the keys.
	 * 
	 * @return a Stream of keys
	 * @since 2.0
	 */
	public Stream<String> streamKeys() {
		Set<TypedStringValue> keySet = innerObject.keySet();
		return keySet.stream().map(key -> key.getValue());
	}

	/**
	 * Get a Stream of all the elements {@link EntryNav} of this map.
	 * 
	 * @return a Stream of EntryNav
	 * @since 2.0
	 */
	public Stream<EntryNav> streamEntries() {
		Set<Entry<TypedStringValue, Object>> entrySet = innerObject.entrySet();
		return entrySet.stream().map(entry -> new EntryNav(super.getRegistry(), entry));
	}

	/**
	 * Use regular {@link Consumer} to visit all {@link EntryNav} of a map closer to stream syntax
	 * {@link Stream#forEach(Consumer)}.
	 * 
	 * @param action Action to take for each EntryNav of this map
	 * @since 2.0
	 */
	public void forEachEntry(Consumer<EntryNav> action) {
		Set<Entry<TypedStringValue, Object>> entrySet = innerObject.entrySet();
		for (Entry<TypedStringValue, Object> entry : entrySet) {
			EntryNav entryNav = new EntryNav(super.getRegistry(), entry);
			action.accept(entryNav);
		}
	}

	/**
	 * Call this method if &lt;map&gt;&lt;entry key="key"&gt;
	 * 
	 * @param entryName the entry name of this tag
	 * @return EntryNav corresponding to this name
	 */
	public EntryNav entry(String entryName) {
		if (entryName == null || !innerObject.containsKey(new TypedStringValue(entryName))) {
			throw new NoSuchBeanDefinitionException("No bean named " + entryName);
		}

		Optional<Entry<TypedStringValue, Object>> findFirst = innerObject.entrySet().stream()
		        .filter(entry -> entryName.equals(entry.getKey().getValue())).findFirst();

		if (!findFirst.isPresent()) {
			throw new NoSuchBeanDefinitionException("No bean named " + entryName);
		}
		return new EntryNav(super.getRegistry(), findFirst.get());
	}

	/**
	 * Deprecated, use {@link #forEachEntry(Consumer)} instead !
	 * 
	 * @param mapVisitorString Visitor class
	 */
	@Deprecated
	public void visit(MapBeansVisitor<EntryNav> mapVisitorString) {
		Set<Entry<TypedStringValue, Object>> entrySet = innerObject.entrySet();
		for (Entry<TypedStringValue, Object> entry : entrySet) {
			EntryNav entryNav = new EntryNav(super.getRegistry(), entry);
			mapVisitorString.visit(entry.getKey().getValue(), entryNav);
		}
	}

	/**
	 * Convert inner spring map definition into Map&lt;String, String&gt; of a tuple (key, string_value).
	 * 
	 * @return a map of (key, string_value).
	 */
	public Map<String, String> toMapString() {
		HashMap<String, String> result = new HashMap<>();
		forEachEntry(entryNav -> result.put(entryNav.key(), entryNav.value()));
		return result;
	}

	/**
	 * Convert inner spring map definition into Map&lt;String, String&gt; of a tuple (key, ref-value).
	 * 
	 * @return a map of (key, ref-value)
	 */
	public Map<String, BeanNav> toMapBean() {
		HashMap<String, BeanNav> result = new HashMap<>();
		forEachEntry(entryNav -> result.put(entryNav.key(), entryNav.ref()));
		return result;
	}
}
