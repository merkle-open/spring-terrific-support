/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.file;

import com.namics.oss.spring.support.terrific.config.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import java.util.*;
import java.util.Map.Entry;

/**
 * ResourceResolverFactory.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 04.04.2013
 */
public class ResourceResolverFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceResolverFactory.class);

	protected ResourceConfig resourceConfig;
	protected ServletContext servletContext;

	protected Map<String, Set<ResourceResolver>> outputResolvers = new HashMap<>();


	protected ResourceResolverFactory() {
	}

	public ResourceResolverFactory(ResourceConfig resourceConfig, ServletContext servletContext) {
		this.resourceConfig = resourceConfig;
		this.servletContext = servletContext;
		this.loadConfig();
	}

	/**
	 * Load configuration to initialize factory.
	 */
	public void loadConfig() {
		for (Entry<String, List<String>> fileEntry : this.resourceConfig.getFiles().entrySet()) {
			String outputFile = fileEntry.getKey();
			Set<ResourceResolver> resolvers = new LinkedHashSet<>(); // ordered by insert
			this.outputResolvers.put(outputFile, resolvers);

			for (String scanPath : this.resourceConfig.getScan()) {
				List<String> configs = fileEntry.getValue();
				List<String> files = new ArrayList<>();
				List<String> excludes = new ArrayList<>();
				List<String> appends = new ArrayList<>();

				for (String config : configs) {
					if (config.startsWith("!")) {
						excludes.add(this.createResourcePattern(scanPath, config.substring(1)));
					} else if (config.startsWith("+")) {
						// appends must be resolvable by all scan paths
						for (String appendScanPath : this.resourceConfig.getScan()) {
							appends.add(this.createResourcePattern(appendScanPath, config.substring(1)));
						}
					} else {
						files.add(this.createResourcePattern(scanPath, config));
					}
				}

				for (String file : files) {
					// prototype bean type, always get a new instance!
					ConfigurableResourceResolver resolver = new PatternResourceResolver(this.servletContext);
					resolver.setPattern(file);
					for (String exclude : excludes) {
						resolver.addExclude(exclude);
					}
					for (String append : appends) {
						resolver.addAppend(append);
					}
					resolvers.add(resolver);
					LOG.info("Add Resolver for file {} with pattern {}: {}", outputFile, file, resolver);
				}
			}
		}
	}

	/**
	 * Factory method returns resource resolvers to resolve the requested file in the search path.
	 *
	 * @param filename file to resolve, is known file that aggregates several source files
	 * @return a set of resolvers to resolve the file, may be empty set
	 */
	public Set<ResourceResolver> getResourceResolvers(String filename) {
		if (StringUtils.hasText(filename) && this.outputResolvers.containsKey(filename)) {
			return this.outputResolvers.get(filename);
		}
		return Collections.emptySet();
	}

	/**
	 * getResourceResolver.
	 *
	 * @param filename file name to get a matching resolver for
	 * @return matching resolver
	 */
	public ResourceResolver getResourceResolver(String filename) {
		PatternResourceResolver resolver = new PatternResourceResolver(servletContext);
		resolver.setPattern(this.resourceConfig.getBase() + filename);
		return resolver;
	}

	protected String createResourcePattern(String scanPath,
	                                       String pattern) {
		StringBuilder resource = new StringBuilder();
		// append scan path
		if (scanPath != null) {
			resource.append(scanPath);
		}
		// remove trailing space
		if (resource.length() > 0 && '/' == resource.charAt(resource.length() - 1)) {
			resource.deleteCharAt(resource.length() - 1);
		}
		// append file pattern
		if (StringUtils.hasText(pattern)) {
			// check for trailing space of pattern
			if (!pattern.startsWith("/")) {
				resource.append("/");
			}
			resource.append(pattern);
		}
		return resource.toString();
	}
}
