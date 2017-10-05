/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.service;

import com.namics.oss.spring.support.terrific.aggregate.Aggregator;
import com.namics.oss.spring.support.terrific.aggregate.AggregatorFactory;
import com.namics.oss.spring.support.terrific.compile.Compiler;
import com.namics.oss.spring.support.terrific.compile.CompilerFactory;
import com.namics.oss.spring.support.terrific.config.ResourceConfig;
import com.namics.oss.spring.support.terrific.file.ResourceResolver;
import com.namics.oss.spring.support.terrific.file.ResourceResolverFactory;
import com.namics.oss.spring.support.terrific.minify.Minifier;
import com.namics.oss.spring.support.terrific.minify.MinifierFactory;
import com.namics.oss.spring.support.terrific.minify.MinifyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Implementation of content service, that ties to resolve optimized and aggregated source files as well as normal
 * files.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 04.04.2013
 */
public class ContentServiceImpl implements ContentService {

	private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

	protected ResourceConfig resourceConfig;
	protected ResourceResolverFactory resourceResolverFactory;
	protected CompilerFactory compilerFactory;
	protected MinifierFactory minifierFactory;
	protected AggregatorFactory aggregatorFactory;

	protected boolean minify = true;
	protected String encoding = "UTF-8";
	protected Cache resourceContentCache;
	protected String ignorePathPattern = "";
	protected String mapping = "/terrific/";
	protected boolean debug = false;

	public ContentServiceImpl(ResourceResolverFactory resourceResolverFactory, CompilerFactory compilerFactory, MinifierFactory minifierFactory, AggregatorFactory aggregatorFactory, ResourceConfig resourceConfig) {
		notNull(resourceResolverFactory);
		notNull(compilerFactory);
		notNull(minifierFactory);
		notNull(aggregatorFactory);
		notNull(resourceConfig);

		this.resourceResolverFactory = resourceResolverFactory;
		this.compilerFactory = compilerFactory;
		this.minifierFactory = minifierFactory;
		this.aggregatorFactory = aggregatorFactory;
		this.resourceConfig = resourceConfig;
		this.setCacheSingleResources(true);
	}

	@Override
	public Long lastModified(String file) {
		file = this.handleIgnorePathPattern(file);
		Set<ResourceResolver> resourceResolvers = this.resourceResolverFactory.getResourceResolvers(file);
		Long lastModified = null;
		// resolvers found, so perform all aggregation steps
		if (!isEmpty(resourceResolvers)) {
			for (ResourceResolver resolver : resourceResolvers) {
				for (Set<Resource> resources : resolver.getResources()) {
					for (Resource resource : resources) {
						try {
							long modified = resource.lastModified();
							if (lastModified == null || modified > lastModified) {
								lastModified = modified;
							}
						} catch (IOException e) {
							// ignore
						}
					}
				}
			}
		}
		if (lastModified == null) {
			// we create a Resolver to get a specific file relative to base path
			String path = file.startsWith("/") ? file : "/" + file;
			ResourceResolver resolver = this.resourceResolverFactory.getResourceResolver(path);
			Set<Set<Resource>> resourcesSets = resolver.getResources();
			if (!isEmpty(resourcesSets)) {
				// process first match only!
				Set<Resource> resources = resourcesSets.iterator().next();
				if (!isEmpty(resources)) {
					Resource resource = resources.iterator().next();
					try {
						lastModified = resource.lastModified();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}
		return lastModified;
	}


	/**
	 * {@inheritDoc}
	 *
	 * @throws FileNotFoundException no resource found matching file name
	 * @throws IOException           problem in file processing
	 */
	@Override
	@Cacheable(value = "terrificContentCache", key = "#file.concat(#root.target.hashCode()).concat(#debug)",
			condition = "!#root.target.debug")
	public byte[] getContent(String file) throws FileNotFoundException, IOException {
		return getContent(file, debug);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws FileNotFoundException no resource found matching file name
	 * @throws IOException           problem in file processing
	 */
	@Override
	@Cacheable(value = "terrificContentCache", key = "#file.concat(#root.target.hashCode()).concat(#debug)", condition = "!#debug")
	public byte[] getContent(String file,
	                         boolean debug) throws FileNotFoundException, IOException {
		LOG.info("Load content for file {}", file);
		file = this.handleIgnorePathPattern(file);

		byte[] result = this.getAggregatorContent(file, debug);
		if (result != null) {
			return result;
		} else {
			// we create a Resolver to get a specific file relative to base path
			String path = file.startsWith("/") ? file : "/" + file;
			ResourceResolver resolver = this.resourceResolverFactory.getResourceResolver(path);
			Set<Set<Resource>> resourcesSets = resolver.getResources();
			if (!isEmpty(resourcesSets)) {
				// process first match only!
				Set<Resource> resources = resourcesSets.iterator().next();
				if (!isEmpty(resources)) {
					Resource resource = resources.iterator().next();
					return FileCopyUtils.copyToByteArray(resource.getInputStream());
				}
			}
		}
		throw new FileNotFoundException("Content for file " + file + " could not be found");
	}

	/**
	 * Check and cut of ignore path of file.
	 *
	 * @param file file to check
	 * @return actual file to resolve
	 */
	protected String handleIgnorePathPattern(String file) {
		String result = file;
		if (StringUtils.hasText(this.ignorePathPattern)) {
			if (result.startsWith(this.ignorePathPattern)) {
				result = result.substring(this.ignorePathPattern.length());
			} else {
				LOG.warn("Requested file {} is missing path pattern {}", result, this.ignorePathPattern);
			}
		}
		return result;
	}

	protected byte[] getAggregatorContent(String file,
	                                      boolean debug) {
		// get Resolvers for known files, they are text base and will be aggregated
		Set<ResourceResolver> resourceResolvers = this.resourceResolverFactory.getResourceResolvers(file);

		// resolvers found, so perform all aggregation steps
		if (!isEmpty(resourceResolvers)) {
			Set<Set<Resource>> resourcesSets = new LinkedHashSet<>();
			for (ResourceResolver resolver : resourceResolvers) {
				resourcesSets.addAll(resolver.getResources());
			}

			Aggregator aggregator = aggregatorFactory.getAggregator(file, debug);
			for (Set<Resource> resources : resourcesSets) {
				if (!isEmpty(resources)) {
					Resource resource = resources.iterator().next(); // first resource decides about all processings required!
					String fallbackFileName = resource.getFilename();

					String filename;
					try {
						filename = resource.getFile().getPath();

						// normalize windows
						String separator = System.getProperty("file.separator");
						if (!"/".equals(separator)) {
							filename = filename.replaceAll(Pattern.quote(separator), "/");
						}

						int index = filename.indexOf(resourceConfig.getStrippedBase());
						index = index + resourceConfig.getStrippedBase().length();
						filename = filename.substring(index);
						filename = mapping + filename;
						filename = filename.replaceAll("//", "/");
					} catch (IOException e) {
						LOG.debug("Problem extracting file path {}", e.getMessage());
						filename = fallbackFileName;
					}
					byte[] content = this.loadSingleResourceContent(file, resources, filename, debug);
					aggregator.append(filename, content);
				}

			}
			return aggregator.getContent();
		}
		return null;
	}

	/**
	 * Load content of a single resource from repository, use caching if enabled and usable.
	 *
	 * @param file      requested file
	 * @param resources resources to be loaded
	 * @param filename  master file name of resource to load (the one without a + in config)
	 * @param debug
	 * @return content, either form cache or direct loaded
	 */
	protected byte[] loadSingleResourceContent(String file,
	                                           Set<Resource> resources,
	                                           String filename,
	                                           boolean debug) {
		byte[] content = null;

		// if caching is enabled
		Long modified = null;
		if (this.resourceContentCache != null) {
			modified = this.modifiedValue(resources);
			if (modified != null) {
				ValueWrapper value = this.resourceContentCache.get(filename);
				// get by filename
				if (value != null) {
					Object[] data = (Object[]) value.get();
					// check if still valid based on modified stamp
					if (modified.equals(data[0])) {
						LOG.debug("Hit cache for {}", filename);
						content = (byte[]) data[1];
					}
				}
			}
		}
		if (content == null) {
			content = this.loadContent(resources, filename);
			content = this.compileContent(file, filename, content);
			if (!debug) {
				content = this.minifyContent(file, filename, content);
			}
			if (modified != null) {
				LOG.debug("Cache content for {} ", filename);
				// cache by filename and keep modified stamp in cache object to avoid zombies of outdated resources
				// cache entry will be overwritten for filename key when modified stamp changed
				this.resourceContentCache.put(filename, new Object[] { modified, content });
			}
		}
		return content;
	}

	/**
	 * Create a modification signature.
	 *
	 * @param resources set of resources to create modification signature
	 * @return sigantue (sum of lastModified stamps)
	 */
	protected Long modifiedValue(Set<Resource> resources) {
		Long modified = null;
		for (Resource resource : resources) {
			try {
				long lastModified = resource.getFile().lastModified();
				if (lastModified == 0) {
					return null; // resource not cachable if no valid value
				}
				modified = modified == null ? lastModified : modified + lastModified;
			} catch (IOException e) {
				return null;// resource not cachable if no valid value
			}
		}
		return modified;
	}

	/**
	 * load actual content form resoures in repository.
	 *
	 * @param resources set of resources to be loaded
	 * @param filename  file name to be resolved
	 * @return raw content of the resource set
	 */
	protected byte[] loadContent(Set<Resource> resources,
	                             String filename) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		for (Resource resource : resources) {
			try {
				byte[] data = FileCopyUtils.copyToByteArray(resource.getInputStream());
				buffer.write(data);
				buffer.write("\n".getBytes());
			} catch (Exception e) {
				final Object[] args = new Object[] { filename, e.getClass(), e.getMessage() };
				final String msg = "Failed to get content of file {} because of {}:{}";
				LOG.warn(msg, args);
				LOG.debug(msg, args, e);
			}
		}
		return buffer.toByteArray();
	}

	/**
	 * Compile content if required.
	 *
	 * @param file     requested file
	 * @param filename current resources file name
	 * @param content  content to be compiled
	 * @return compiled content
	 */
	protected byte[] compileContent(String file,
	                                String filename,
	                                byte[] content) {
		byte[] compiled = content;
		try {
			if (compiled != null && compiled.length > 0) {
				Compiler compiler = this.compilerFactory.getCompiler(filename);
				if (compiler != null) {
					compiled = compiler.compile(content);
				}
			}
		} catch (Exception e) {
			final Object[] args = new Object[] { filename, e.getClass(), e.getMessage() };
			final String msg = "Failed to get content of file {} because of {}:{}";
			LOG.warn(msg, args);
			LOG.debug(msg, args, e);
		}
		return compiled;
	}

	/**
	 * minify content.
	 *
	 * @param file     requested file
	 * @param filename filename of currently processed resource
	 * @param content  content to be minified
	 * @return minified content, original content, on minify failed
	 */
	protected byte[] minifyContent(String file,
	                               String filename,
	                               byte[] content) {
		byte[] minified = content;
		if (this.minify) {
			Minifier minifier = this.minifierFactory.getMinifier(filename);
			if (minifier != null) {
				try {
					minified = minifier.minify(content);
				} catch (MinifyException e) {
					final Object[] args = new Object[] { filename, e };
					final String msg = "Could not minify {} proceed with uncompressed content {}";
					LOG.warn(msg, args);
					LOG.debug(msg, args, e);
				}
			}
		}
		return minified;
	}

	/**
	 * use minify, default by property ${terrific.minify:true}.
	 *
	 * @param minify the minify to set
	 */
	public ContentServiceImpl setMinify(boolean minify) {
		this.minify = minify;
		return this;
	}

	/**
	 * encoding to use, default by property ${terrific.encoding:UTF-8}.
	 *
	 * @param encoding the encoding to set
	 */
	public ContentServiceImpl setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	/**
	 * cache unchanged resource file content, default by property @Value("${terrific.caching.single.resources:false}").
	 *
	 * @param cacheSingleResources the flag to set
	 */
	public final ContentServiceImpl setCacheSingleResources(boolean cacheSingleResources) {
		if (cacheSingleResources && this.resourceContentCache == null) {
			this.resourceContentCache = new ConcurrentMapCache("resourceContentCache");
		} else if (!cacheSingleResources) {
			this.resourceContentCache = null;
		}
		return this;
	}


	/**
	 * Set a specific pattern to be ignored at the beginning of the file path.
	 * This can be used to implement build depending pathes to enable advanced caching strategies such as: eternal caching and altering path per
	 * release.
	 * E.g.: Set this to 08092013/ => requested file 08092013/main.css => delivered file main.css
	 *
	 * @param ignorePathPattern path pattern to be ignored
	 */
	public ContentServiceImpl setIgnorePathPattern(String ignorePathPattern) {
		String pattern = ignorePathPattern;
		if (StringUtils.hasText(pattern)) {
			if (!pattern.endsWith("/")) {
				pattern = pattern + "/";
			}
			if (pattern.startsWith("/")) {
				pattern = pattern.substring(1);
			}
		}
		this.ignorePathPattern = pattern;
		return this;
	}

	/**
	 * Setter for resourceContentCache. @param resourceContentCache the resourceContentCache to set
	 */
	public ContentServiceImpl setResourceContentCache(Cache resourceContentCache) {
		this.resourceContentCache = resourceContentCache;
		return this;
	}


	public ContentServiceImpl setMapping(String mapping) {
		this.mapping = mapping;
		return this;
	}

	public ContentServiceImpl setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}
}
