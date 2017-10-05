/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.file;

import com.namics.oss.spring.support.terrific.util.FilenameIgnoreCaseResourceComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.*;

import static java.util.Collections.emptySet;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 * PatternResourceResolver has prototype scope, use a fresh instance for each resource.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 03.04.2013
 */
public class PatternResourceResolver implements ConfigurableResourceResolver {

	private static final Logger LOG = LoggerFactory.getLogger(PatternResourceResolver.class);

	protected String scanPath = "";

	protected String pattern;

	protected List<String> excludes = new ArrayList<>();

	protected List<String> appends = new ArrayList<>();

	protected Comparator<Resource> resourceComparator = new FilenameIgnoreCaseResourceComparator();

	protected ServletContext servletContext;

	protected ResourcePatternResolver resourceResolver;

	protected ResourcePatternResolver excludeResourceResolver;

	public PatternResourceResolver(ServletContext servletContext) {
		this.servletContext = servletContext;
		if (this.servletContext != null) {
			this.resourceResolver = new ServletContextResourcePatternResolver(this.servletContext);
			this.excludeResourceResolver = new ServletContextResourcePatternResolver(this.servletContext);
		} else {
			this.resourceResolver = new PathMatchingResourcePatternResolver();
			this.excludeResourceResolver = new PathMatchingResourcePatternResolver();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Set<Resource>> getResources() {
		Set<Resource> sorted = this.collectSortedResources();

		// gathered resources in sorted order
		// create sets with append files keep order of sorted and preserve order of sub sets
		Set<Set<Resource>> preserveSortOrderResult = new LinkedHashSet<>();
		for (Resource resource : sorted) {
			Set<Resource> preserveConfigurationOrder = new LinkedHashSet<>();
			preserveSortOrderResult.add(preserveConfigurationOrder);

			preserveConfigurationOrder.add(resource); // main file always first!

			String resourcePostfix = resource.getFilename().substring(resource.getFilename().lastIndexOf("."));
			// gather files to append (e.g. +file.less)
			for (String currentAppendPattern : this.appends) {
				try {
					Resource[] appendResources = this.resourceResolver.getResources(currentAppendPattern);
					if (appendResources != null && appendResources.length > 0) {
						for (Resource append : appendResources) {
							if (append != null && append.getFilename().endsWith(resourcePostfix)) {
								preserveConfigurationOrder.add(append);
							}
						}
					}
				} catch (IOException e) {
					LOG.info("No matching files for append pattern {} ", currentAppendPattern);
				}
			}
		}

		return preserveSortOrderResult;
	}

	/**
	 * collectSortedResources.
	 *
	 * @return
	 */
	public Set<Resource> collectSortedResources() {
		LOG.debug("Collect files for pattern {} {} using resolver {}", this.scanPath, this.pattern, this.resourceResolver);
		Set<Resource> sorted = this.resourceComparator != null ? new TreeSet<>(this.resourceComparator) : new TreeSet<>();
		try {
			Resource[] resources = this.resourceResolver.getResources(this.pattern);
			if (resources != null && resources.length > 0) {
				Set<Resource> excludes = findExcludedResources();
				for (Resource resource : resources) {
					if (isFile(resource)) {
						boolean addResource = !excludes.contains(resource);
						if (addResource) {
							addResource = this.checkAppend(resource);
						}
						if (addResource) {
							LOG.debug("Add resource {}, filename '{}'", resource, resource.getFilename());
							sorted.add(resource);
						}
					}
				}
			}
		} catch (IOException e) {
			LOG.info("No matching files for pattern {} {} {}", this.scanPath, this.pattern, e);
		}
		return sorted;
	}

	protected boolean isFile(Resource resource) {
		String filename = resource.getFilename();
		return hasText(filename) && filename.indexOf('.') != -1;
	}

	protected Set<Resource> findExcludedResources() {
		if (!isEmpty(this.excludes)) {
			Set<Resource> excludes = new HashSet<>();
			for (String excludePattern : getExcludes()) {
				Resource[] foundExcludes = null;
				try {
					foundExcludes = this.excludeResourceResolver.getResources(excludePattern);
				} catch (IOException e) {
					LOG.debug("No exclude s found {} {}", new Object[] { excludePattern, e });
				}
				if (foundExcludes != null) {
					for (Resource foundExclude : foundExcludes) {
						excludes.add(foundExclude);
					}
				}
			}
			return excludes;
		}
		return emptySet();
	}


	protected boolean checkAppend(Resource resource) throws IOException {
		boolean addResource = true;
		if (!isEmpty(this.appends)) {
			appends:
			for (String append : this.appends) {
				Resource[] appendResources = this.resourceResolver.getResources(append);
				if (appendResources != null && appendResources.length > 0) {
					for (Resource appendResource : appendResources) {
						if (appendResource != null && appendResource.equals(resource)) {
							LOG.debug("File {} is append file {}, skip resource", resource, append);
							addResource = false;
							break appends;
						}
					}
				}
			}
		}
		return addResource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addExclude(String exclude) {
		this.excludes.add(exclude);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAppend(String append) {
		this.appends.add(append);
	}

	/**
	 * Getter for pattern. @return the pattern
	 */
	public String getPattern() {
		return this.pattern;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * Getter for resourceComparator. @return the resourceComparator
	 */
	public Comparator<Resource> getResourceComparator() {
		return this.resourceComparator;
	}

	/**
	 * Setter for resourceComparator. @param resourceComparator the resourceComparator to set
	 */
	public void setResourceComparator(Comparator<Resource> resourceComparator) {
		this.resourceComparator = resourceComparator;
	}

	/**
	 * Getter for resourceResolver. @return the resourceResolver
	 */
	public ResourcePatternResolver getResourceResolver() {
		return this.resourceResolver;
	}

	/**
	 * Getter for excludes. @return the excludes
	 */
	public List<String> getExcludes() {
		return this.excludes;
	}

	/**
	 * Setter for excludes. @param excludes the excludes to set
	 */
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	/**
	 * Getter for appends. @return the appends
	 */
	public List<String> getAppends() {
		return this.appends;
	}

	/**
	 * Setter for appends. @param appends the appends to set
	 */
	public void setAppends(List<String> appends) {
		this.appends = appends;
	}

	@Override
	public String toString() {
		return "PatternResourceResolver{" +
		       "scanPath='" + scanPath + '\'' +
		       ", pattern='" + pattern + '\'' +
		       ", excludes=" + excludes +
		       ", appends=" + appends +
		       ", servletContext=" + (servletContext != null) +
		       ", resourceResolver=" + resourceResolver +
		       ", excludeResourceResolver=" + excludeResourceResolver +
		       '}';
	}

	/**
	 * Getter for scanPath. @return the scanPath
	 */
	public String getScanPath() {
		return this.scanPath;
	}

	public void setScanPath(String scanPath) {
		this.scanPath = scanPath;
	}
}
