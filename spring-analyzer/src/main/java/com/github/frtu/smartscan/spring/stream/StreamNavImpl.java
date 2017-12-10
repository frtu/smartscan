package com.github.frtu.smartscan.spring.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.github.frtu.smartscan.spring.navigator.AbstractBaseNavigator;
import com.github.frtu.smartscan.spring.navigator.BeanNav;

public class StreamNavImpl<T extends AbstractBaseNavigator> extends AbstractBaseNavigator implements StreamNav<T> {
	private List<Predicate<? super T>> predicates = new ArrayList<>();

	public StreamNavImpl(BeanDefinitionRegistry registry) {
		super(registry);
	}

	/**
	 * @param beanDefinitionRegistry
	 * @return
	 */
	public Stream<BeanNav> streamBean() {
		Stream<String> streamBeanName = Arrays.asList(getRegistry().getBeanDefinitionNames()).stream();
		return streamBeanName.map(beanName -> BeanNav.build(getRegistry(), beanName));
	}

	@Override
	public StreamNav<T> filter(Predicate<? super T> predicate) {
		predicates.add(predicate);
		return this;
	}

//	@Override
//	public <R> StreamNav<R> map(Function<? super T, ? extends R> mapper) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public StreamNav<T> peek(Consumer<? super T> action) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public StreamNav<T> limit(long maxSize) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public StreamNav<T> skip(long n) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R, A> R collect(Collector<? super T, A, R> collector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean anyMatch(Predicate<? super T> predicate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allMatch(Predicate<? super T> predicate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean noneMatch(Predicate<? super T> predicate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<T> findFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<T> findAny() {
		// TODO Auto-generated method stub
		return null;
	}
}
