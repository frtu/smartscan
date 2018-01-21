package com.github.frtu.smartscan.text.stream;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Test;

import com.github.frtu.smartscan.text.stream.FileWriterCollector;
import com.github.frtu.smartscan.text.stream.FileWriterCollectorTest.TestObject;

public class FileWriterCollectorTest {
	public class TestObject {
		private String key;
		private String value;

		public TestObject(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}

	@Test
	public void testToCollector() {
		Stream<TestObject> stream = Stream.of(new TestObject("key1", "value1"), new TestObject("key2", "value2"),
		        new TestObject("someKey", "someValue"));

		stream.filter(obj -> obj.getKey().startsWith("key")).collect(
		        FileWriterCollector.toCollector(obj -> Paths.get("target/" + obj.getKey()), obj -> obj.getValue()));

		assertTrue(Paths.get("target/key1").toFile().exists());
		assertTrue(Paths.get("target/key2").toFile().exists());
	}
}
