/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.file;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * PatternResourceResolverTest.
 *
 * @author aschaefer, Namics AG
 * @since 14.03.14 08:04
 */
public class PatternResourceResolverTest {
	@Test
	public void testGetResourcesServletContext() throws Exception {
		PatternResourceResolver resolver = new PatternResourceResolver(new MockServletContext("/exclude"));
		assertEquals(ServletContextResourcePatternResolver.class, resolver.getResourceResolver().getClass());
		resolver.setPattern("/**");
		resolver.setScanPath("");
		resolver.setExcludes(Arrays.asList("test/**/*.*"));
		Set<Set<Resource>> resources = resolver.getResources();
		assertNotNull(resources);

		Set<Resource> mainFiles = new HashSet<Resource>();
		for (Set<Resource> resource : resources) {
			Iterator<Resource> iterator = resource.iterator();
			if (iterator.hasNext()) {
				mainFiles.add(iterator.next());
			}
		}
		assertEquals(1, mainFiles.size());
	}

	@Test
	public void testGetResourcesServletContextRegexPattern() throws Exception {
		PatternResourceResolver resolver = new PatternResourceResolver(new MockServletContext(""));
		assertEquals(ServletContextResourcePatternResolver.class, resolver.getResourceResolver().getClass());
		resolver.setPattern("/exclude/**/*.css");
		resolver.setScanPath("");
		resolver.setExcludes(Arrays.asList("/exclude/test/**/hierarchy/*"));
		Set<Set<Resource>> resources = resolver.getResources();
		assertNotNull(resources);

		Set<Resource> mainFiles = new HashSet<Resource>();
		for (Set<Resource> resource : resources) {
			Iterator<Resource> iterator = resource.iterator();
			if (iterator.hasNext()) {
				mainFiles.add(iterator.next());
			}
		}
		assertEquals(2, mainFiles.size());
	}

	@Test
	public void testGetResourcesFileSystem() throws Exception {
		PatternResourceResolver resolver = new PatternResourceResolver(null);
		resolver.setPattern("/exclude/**/*.css");
		resolver.setScanPath(PatternResourceResolver.class.getResource("/").getFile());
		resolver.setExcludes(Arrays.asList("/exclude/**/test/**/*.css"));
		Set<Set<Resource>> resources = resolver.getResources();
		assertNotNull(resources);

		Set<Resource> mainFiles = new HashSet<Resource>();
		for (Set<Resource> resource : resources) {
			Iterator<Resource> iterator = resource.iterator();
			if (iterator.hasNext()) {
				mainFiles.add(iterator.next());
			}
		}
		assertEquals(1, mainFiles.size());
	}

	@Test
	public void testGetResourcesFileSystemStarPattern() throws Exception {
		PatternResourceResolver resolver = new PatternResourceResolver(null);
		resolver.setPattern("/exclude/**/*.css");
		resolver.setExcludes(Arrays.asList("/exclude/**/test/*/*-skin*.*"));
		Set<Set<Resource>> resources = resolver.getResources();
		assertNotNull(resources);

		Set<Resource> mainFiles = new HashSet<Resource>();
		for (Set<Resource> resource : resources) {
			Iterator<Resource> iterator = resource.iterator();
			if (iterator.hasNext()) {
				mainFiles.add(iterator.next());
			}
		}
		assertEquals(3, mainFiles.size());
	}

	/**
	 * FIXME create test that spans multiple jar files
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetResourcesClasspath() throws Exception {
		PatternResourceResolver resolver = new PatternResourceResolver(null);
		resolver.setPattern("/exclude/**/*.css");
		resolver.setScanPath("classpath:/");
		resolver.setExcludes(Arrays.asList("/exclude/**/test/**/*"));
		Set<Set<Resource>> resources = resolver.getResources();
		assertNotNull(resources);

		Set<Resource> mainFiles = new HashSet<Resource>();
		for (Set<Resource> resource : resources) {
			Iterator<Resource> iterator = resource.iterator();
			if (iterator.hasNext()) {
				mainFiles.add(iterator.next());
			}
		}
		assertEquals(1, mainFiles.size());
	}
}
