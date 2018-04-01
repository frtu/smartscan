# smartscan
A toolbox to scan config file and provide high level architecture information

## text-toolbox

### Import

```XML
<dependency>
    <groupId>com.github.frtu.smartscan</groupId>
    <artifactId>text-toolbox</artifactId>
    <version>0.2.6</version>
</dependency>
```
To see the latest version, please refer to : 
[Maven Centralized repository search](
http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.frtu.smartscan%22%20AND%20a%3A%22text-toolbox%22)

### PatternBuilder - Simple usage

Since 2.2, build a _PatternBuilder_ with _PatternBuilder.capture()_.

Define a sequence of elements (seq) you want to capture and separator (splitWith) using :

- seq(String sequenceName, String sequencePattern)
- splitWith(String text)

To facilitate the sequence definition, use shortcut of capture with :

- digits [0-9]
- word [a-zA-Z_0-9]
- wordContaining [a-zA-Z_0-9] + Custom chars
- any (.*)
- enumeration (xx|yy|zz)

```
@Test
public void testCapture() {
	Pattern pattern = PatternBuilder.capture()
        .digits("year")
          .splitWithSpaces()
        .word("word")
          .splitWithDots()
        .enumeration("field", "name", "type")
          .build();

		Matcher matcher = pattern.matcher("2017    subpackage....name");
		assertTrue(matcher.find());
		assertEquals("name", matcher.group("field"));
		assertEquals("subpackage", matcher.group("word"));
		assertEquals("2017", matcher.group("year"));
	}
```

```
@Test
public void testWordContaining() {
	Pattern pattern = PatternBuilder.capture()
        .wordContaining("first", '.', ',')
          .splitWith(';')
        .wordContaining("second", ' ', ':')
          .build();

	String firstWord = "a.word..with.dot,and,,comma";
	String secondWord = "another one with space:and::semicolon";
	Matcher matcher = pattern.matcher(firstWord + ";" + secondWord 
	     + ";anything else.with:other;char");

	assertTrue(matcher.find());
	assertEquals(secondWord, matcher.group("second"));
	assertEquals(firstWord, matcher.group("first"));
}
```

You can also Annotate previously defined with :

- isOptional()


### StringStreamUtils

Since 2.6, allow the easy manipulation of String into Stream.map() 

- .getLastWord() : sentence -> last word 
- .getClassname() : Class.getCanonicalName() -> Class.getSimpleName()

```
List<String> wordList = Stream.of(null, "word1", "This sentence last word2", 
			"Another sentence last word3", "A third sentence last word4")
		.map(StringStreamUtils::getLastWord)
			.collect(Collectors.toList());
```

To skip null or blank text, just use :

- .isNotEmpty() : null & ""
- .isNotBlank() : null, "" & " "

```
List<String> wordList = Stream.of(null, "word1", "This sentence last word2", 
			"Another sentence last word3", "A third sentence last word4")
		.filter(StringStreamUtils::isNotBlank)
			.map(StringStreamUtils::getLastWord)
			.collect(Collectors.toList());
```

### FileWriterCollector

Since 2.6, allow to write all kind of Stream into a file.

```
@Test
public void testToCollector() {
	Stream<TestObject> stream = Stream.of(
		new TestObject("key1", "value1"), 
		new TestObject("key2", "value2"),
		new TestObject("someKey", "someValue"));

	stream.filter(obj -> obj.getKey().startsWith("key"))
		.collect(
	        FileWriterCollector.toCollector(
	        	obj -> Paths.get("target/" + obj.getKey()), 
	        	obj -> obj.getValue()
	        )
	    );
	assertTrue(Paths.get("target/key1").toFile().exists());
	assertTrue(Paths.get("target/key2").toFile().exists());
}
```


## spring-analyzer

### Import

```XML
<dependency>
    <groupId>com.github.frtu.smartscan</groupId>
    <artifactId>spring-analyzer</artifactId>
    <version>0.2.4</version>
</dependency>
```
To see the latest version, please refer to : 
[Maven Centralized repository search](
http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.frtu.smartscan%22%20AND%20a%3A%22spring-analyzer%22)

### Simple usage

Suppose that you have an application-context.xml like :

```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="..."> 
	<!-- bean definitions here -->
	<bean id="anotherExampleBean" class="examples.AnotherBean" />
	<bean id="yetAnotherBean" class="examples.YetAnotherBean" />

	<bean id="exampleBean" class="examples.ExampleBean">
		<property name="beanOne">
			<ref bean="anotherExampleBean" />
		</property>
		<property name="beanTwo" ref="yetAnotherBean" />
		<property name="integerProperty">
			<value>1</value>
		</property>
		<property name="integerProperty2" value="1" />
	</bean>
</beans>
```

Parse this file and **_navigate_** from bean to bean like :

```Java
	@Test
	public void testTwoLvlBean() {
		ClasspathXmlNavigator classpathXmlNavigator = 
			new ClasspathXmlNavigator("**/application-context.xml");
		
		BeanNav bean = classpathXmlNavigator.getBean("exampleBean");
		assertTrue(bean.isClass("examples.ExampleBean"));

		BeanNav beanProperty = bean.property("beanOne").ref();
		assertTrue(beanProperty.isClass("examples.AnotherBean"));

		IntermediateNav property = bean.property("integerProperty");
		assertEquals("1", property.value());
		
		IntermediateNav integerProperty2 = bean.property("integerProperty2");
		assertEquals("1", integerProperty2.value());
	}		        
```


### More advance usage (using Stream<?>)

Suppose that you have two application-context.xml.

- application-context-partA.xml

```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="..."> 
	<!-- bean definitions here -->
	<bean id="yetAnotherBean" class="examples.YetAnotherBean" />
	
	<bean id="yetAnotherBean1" name="1" class="examples.YetAnotherBean">
		<property name="field" value="1"/>
	</bean>
	<bean id="yetAnotherBean2" class="examples.YetAnotherBean">
		<property name="field" value="2"/>
	</bean>
	<bean id="yetAnotherBean3" class="examples.YetAnotherBean">
		<property name="field" value="3"/>
	</bean>
	<bean id="yetAnotherBean4" class="examples.YetAnotherBean">
		<property name="field" value="4"/>
	</bean>
</beans>
```

- application-context-partB.xml

```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="..."> 
	<!-- bean definitions here -->
	<bean id="emailsSetBean" class="org.springframework.beans.factory.config.SetFactoryBean">
		<property name="sourceSet">
			<set>
				<bean id="bean3" class="examples.AnotherBean" />
				<bean id="exampleBean1" class="examples.ExampleBean">
					<property name="beanTwo" ref="yetAnotherBean1" />
				</bean>
				<bean id="exampleBean2" class="examples.ExampleBean">
					<property name="beanTwo" ref="yetAnotherBean2" />
				</bean>
				<bean id="exampleBean3" class="examples.ExampleBean">
					<property name="beanTwo" ref="yetAnotherBean3" />
				</bean>
				<bean id="exampleBean4" class="examples.ExampleBean">
					<property name="beanTwo" ref="yetAnotherBean4" />
				</bean>
			</set>
		</property>
	</bean>
</beans>
```

Parse this file and **_navigate_** using Stream with filter() & map() to the edge of the Spring config node like :

```Java
	@Test
	public void testSetStreamBean() {
		ClasspathXmlNavigator classpathXmlNavigator = 
			new ClasspathXmlNavigator("application-context-partB.xml",
		        "subfolder/application-context-partA.xml");
		        
		BeanNav bean = classpathXmlNavigator.getBean("emailsSetBean");
		SetBeansNav setNav = bean.property("sourceSet").setBeans();
		assertNotNull(setNav);

		String className = "examples.ExampleBean";
		Stream<BeanNav> streamBean = setNav.streamBean();
		assertTrue("Should have found in the list one class =" + className,
		        streamBean.anyMatch(beanNav -> beanNav.isClass(className)));

		// A stream cannot be reused
		streamBean = setNav.streamBean();

		List<String> collect = streamBean
				.filter(beanNav -> beanNav.isClass(className))
		        .map(beanNav -> beanNav.property("beanTwo").ref().property("field").value())
		        .collect(Collectors.toList());
		assertEquals("1", collect.get(0));
		assertEquals("2", collect.get(1));
		assertEquals("3", collect.get(2));
		assertEquals("4", collect.get(3));
	}
	        
```
