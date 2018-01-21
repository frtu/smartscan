package com.github.frtu.smartscan.text.stream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Allow to write into a filename based on a path mapper, using values from a value mapper using
 * {@link FileWriterCollector#toCollector(Function, Function)}.
 * 
 * <code>
 * Stream.collect(
		        FileWriterCollector.toCollector(obj -{@literal >} Paths.get("target/" + obj.getKey()), obj -{@literal >} obj.getValue())
 * </code>
 * 
 * @author frtu
 *
 * @param <T>
 * @since 2.5
 */
public class FileWriterCollector<T> implements Collector<T, Map<Path, BufferedWriter>, Set<Path>> {
	private Function<? super T, Path> pathMapper;
	private Function<? super T, String> dataMapper;

	FileWriterCollector(Function<? super T, Path> pathMapper, Function<? super T, String> dataMapper) {
		super();
		this.pathMapper = pathMapper;
		this.dataMapper = dataMapper;
	}

	public static <T> Collector<T, Map<Path, BufferedWriter>, Set<Path>> toCollector(
	        Function<? super T, Path> pathMapper, Function<? super T, String> dataMapper) {
		return new FileWriterCollector<>(pathMapper, dataMapper);
	}

	@Override
	public Supplier<Map<Path, BufferedWriter>> supplier() {
		return ConcurrentHashMap::new;
	}

	@Override
	public BiConsumer<Map<Path, BufferedWriter>, T> accumulator() {
		return (map, t) -> {
			Path path = pathMapper.apply(t);
			BufferedWriter bufferedWriter = map.get(path);
			try {
				if (bufferedWriter == null) {
					bufferedWriter = Files.newBufferedWriter(path);
					map.put(path, bufferedWriter);
				}
				bufferedWriter.write(dataMapper.apply(t));
				bufferedWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}

	@Override
	public BinaryOperator<Map<Path, BufferedWriter>> combiner() {
		return (map1, map2) -> {
			map1.putAll(map2);
			return map1;
		};
	}

	@Override
	public Function<Map<Path, BufferedWriter>, Set<Path>> finisher() {
		return map -> map.keySet();
	}

	@Override
	public Set<java.util.stream.Collector.Characteristics> characteristics() {
		return Collections.emptySet();
	}
}
