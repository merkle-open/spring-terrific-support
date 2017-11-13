/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific;

import com.namics.oss.spring.support.terrific.config.ResourceConfig;
import com.namics.oss.spring.support.terrific.file.ResourceResolver;
import com.namics.oss.spring.support.terrific.file.ResourceResolverFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResource;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ResourceResolverFacrtoryTest.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 05.04.2013
 */
public class ResourceResolverFacrtoryTest {
	ServletContext servletContext = new MockServletContext();
	ResourceResolverFactory factory = new ResourceResolverFactory(new ResourceConfig(new ServletContextResource(servletContext, "/WEB-INF/terrific/config.json")), servletContext);

	@Test
	public void testGetResourceResolvingMainJs() throws IOException {
		Set<ResourceResolver> resolvers = this.factory.getResourceResolvers("main.js");
		Assert.assertEquals(4, resolvers.size());
		Set<Set<Resource>> resources = new LinkedHashSet<Set<Resource>>();
		for (ResourceResolver resolver : resolvers) {
			Set<Set<Resource>> iteration = resolver.getResources();
			resources.addAll(iteration);
		}

		Assert.assertEquals(6, resources.size());
		for (Set<Resource> resource : resources) {
			InputStream in = resource.iterator().next().getInputStream();
			String content = FileCopyUtils.copyToString(new InputStreamReader(in));
			Assert.assertTrue(StringUtils.hasText(content));
		}
	}

	@Test
	public void testGetResourceResolvingMainCss() throws IOException {
		Set<ResourceResolver> resolvers = this.factory.getResourceResolvers("main.css");
		Assert.assertEquals(6, resolvers.size());
		Set<Set<Resource>> resources = new LinkedHashSet<Set<Resource>>();
		for (ResourceResolver resolver : resolvers) {
			Set<Set<Resource>> iteration = resolver.getResources();
			resources.addAll(iteration);
		}

		Assert.assertEquals(7, resources.size());
		for (Set<Resource> resource : resources) {
			InputStream in = resource.iterator().next().getInputStream();
			String content = FileCopyUtils.copyToString(new InputStreamReader(in));
			Assert.assertTrue(StringUtils.hasText(content));
		}
	}
}
