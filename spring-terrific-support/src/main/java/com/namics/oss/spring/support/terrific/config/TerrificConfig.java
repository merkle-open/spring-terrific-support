/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.config;

import com.namics.oss.spring.support.terrific.aggregate.*;
import com.namics.oss.spring.support.terrific.compile.Compiler;
import com.namics.oss.spring.support.terrific.compile.CompilerFactory;
import com.namics.oss.spring.support.terrific.compile.LessCompiler;
import com.namics.oss.spring.support.terrific.file.ResourceResolverFactory;
import com.namics.oss.spring.support.terrific.filter.TerrificFilter;
import com.namics.oss.spring.support.terrific.minify.CssMinifier;
import com.namics.oss.spring.support.terrific.minify.JsMinifier;
import com.namics.oss.spring.support.terrific.minify.Minifier;
import com.namics.oss.spring.support.terrific.minify.MinifierFactory;
import com.namics.oss.spring.support.terrific.service.ContentService;
import com.namics.oss.spring.support.terrific.service.ContentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * TerrificConfig.
 *
 * @author aschaefer
 * @since 04.02.14 11:19
 */
@Configuration
@PropertySource("classpath:/META-INF/terrific-version.properties")
public class TerrificConfig {
	private static final Logger LOG = LoggerFactory.getLogger(TerrificConfig.class);
	@Inject
	protected ServletContext servletContext;

	@Inject
	protected ApplicationContext applicationContext;

	@Inject
	protected Environment environment;

	@Bean
	public TerrificFilter terrificFilter() {
		return new TerrificFilter(terrificContentService())
				.setMapping(mapping())
				.setDebug(debug())
				.setDebugParamName(debugParamName())
				.setCheckLastModified(checkLastModified())
				.setCacheControlAge(cacheControlAge())
				.setTerrificVersion(terrifcVersion());
	}

	@Bean
	@Primary
	public String terrificMappingString() {
		return mapping();
	}

	@Bean
	public ContentService terrificContentService() {
		return new ContentServiceImpl(resourceResolverFactory(),
		                              compilerFactory(),
		                              minifierFactory(),
		                              aggregatorFactory(),
		                              resourceConfig())
				.setMinify(minify())
				.setMapping(mapping())
				.setIgnorePathPattern(ignorePathPattern())
				.setCacheSingleResources(cacheSingleResources())
				.setDebug(debug())
				.setEncoding(encoding());
	}

	public ResourceResolverFactory resourceResolverFactory() {
		return new ResourceResolverFactory(resourceConfig(), servletContext);
	}

	public ResourceConfig resourceConfig() {
		String json = configJson();
		return new ResourceConfig(applicationContext.getResource(json));
	}

	public MinifierFactory minifierFactory() {
		ArrayList<Minifier> minifiers = new ArrayList<>();
		minifiers.add(jsMinifier());
		minifiers.add(cssMinifier());
		return new MinifierFactory(minifiers);
	}

	public JsMinifier jsMinifier() {
		return new JsMinifier().encoding(encoding());
	}

	public CssMinifier cssMinifier() {
		return new CssMinifier(encoding());
	}


	public CompilerFactory compilerFactory() {
		ArrayList<Compiler> compilers = new ArrayList<>();
		compilers.add(lessCompiler());
		return new CompilerFactory(compilers);
	}

	public LessCompiler lessCompiler() {
		return new LessCompiler(servletContext).setEncoding(encoding());
	}

	public AggregatorFactory aggregatorFactory() {
		TreeSet<Aggregator> aggregators = new TreeSet<>();
		aggregators.add(dynamicJsAggregator());
		aggregators.add(cStyleAggregator());
		aggregators.add(defaultAggregator());
		return new AggregatorFactory(aggregators);
	}

	public DynamicsJsAggregator dynamicJsAggregator() {
		return new DynamicsJsAggregator().setEncoding(encoding());
	}

	public CStyleTitlePrinterAggregator cStyleAggregator() {
		return new CStyleTitlePrinterAggregator().setEncoding(encoding());
	}

	public DefaultAggregator defaultAggregator() {
		DefaultAggregator aggregator = new DefaultAggregator();
		aggregator.setEncoding(encoding());
		return aggregator;
	}

	protected boolean cacheSingleResources() {
		return environment.getProperty("terrific.caching", boolean.class, true);
	}

	protected String ignorePathPattern() {
		return environment.getProperty("terrific.ignore.path", String.class, "");
	}

	protected String mapping() {
		return environment.getProperty("terrific.mapping", String.class, "/terrific/");
	}

	protected boolean minify() {
		return environment.getProperty("terrific.minify", boolean.class, true);
	}

	protected boolean debug() {
		return environment.getProperty("terrific.debug", boolean.class, false);
	}

	protected String debugParamName() {
		return environment.getProperty("terrific.debug.param", String.class, "debug");
	}

	protected String configJson() {
		return environment.getProperty("terrific.config",
		                               environment.getProperty("terrific.files.config.json", "/WEB-INF/terrific/config.json"));
	}

	protected boolean checkLastModified() {
		return environment.getProperty("terrific.check.modified", boolean.class, true);
	}

	protected String encoding() {
		return environment.getProperty("terrific.encoding", "UTF-8");
	}

	protected Integer cacheControlAge() {
		return environment.getProperty("terrific.cache.control.age", int.class, 365);
	}

	protected String terrifcVersion() {
		return environment.getProperty("terrific.id", "unknown");
	}
}
