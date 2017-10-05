/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.Assert.notNull;

/**
 * RangePatternResourceResolver.
 * Extend Ant resolver to resolve regex patterns with '[a-z1-9]'
 *
 * @author lboesch, Namics AG
 * @since 28.08.2014
 */
@Deprecated
public class RangePatternResourceResolverWrapper implements ResourcePatternResolver {

	private static final Logger LOG = LoggerFactory.getLogger(PatternResourceResolver.class);

	public static final String RANGE = "(?<range>\\[.*\\])";
	public static final Pattern REGEX_CONFIG_PATTERN = Pattern.compile("(.*)" + RANGE + "(.*)");

	protected PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver;

	public RangePatternResourceResolverWrapper(PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver) {
		notNull(pathMatchingResourcePatternResolver);
		this.pathMatchingResourcePatternResolver = pathMatchingResourcePatternResolver;
	}

	@Override
	public Resource[] getResources(String locationPattern) throws IOException {
		Matcher matcher = REGEX_CONFIG_PATTERN.matcher(locationPattern);
		if (!matcher.matches()) {
			return this.pathMatchingResourcePatternResolver.getResources(locationPattern);
		}
		LOG.debug("Resolved locationPatter [{}] with regex-style", locationPattern);
		String antPattern = locationPattern.replaceAll(RANGE, "*");

		LOG.debug("use ant pattern [{}] first to get possible resources", antPattern);
		Resource[] resources = this.pathMatchingResourcePatternResolver.getResources(antPattern);

		List<Resource> filteredResources = new ArrayList<>(resources.length);
		String regexPattern = convertLocationPatternToRegexPattern(locationPattern);
		LOG.debug("filter {} resources with the regex pattern [{}]", resources.length, regexPattern);
		for (Resource resource : resources) {
			if (resource.getURL().getPath().matches(regexPattern)) {
				filteredResources.add(resource);
			}
		}
		return filteredResources.toArray(new Resource[filteredResources.size()]);
	}

	@Override
	public Resource getResource(String location) {
		Matcher matcher = REGEX_CONFIG_PATTERN.matcher(location);
		if (matcher.matches()) {
			Resource resource = this.pathMatchingResourcePatternResolver.getResource(location.replaceAll(RANGE, "*"));
			if (((FileSystemResource) resource).getPath().matches(convertLocationPatternToRegexPattern(location))) {
				return resource;
			} else {
				return null;
			}
		}
		return this.pathMatchingResourcePatternResolver.getResource(location);
	}

	@Override
	public ClassLoader getClassLoader() {
		return this.pathMatchingResourcePatternResolver.getClassLoader();
	}

	private String convertLocationPatternToRegexPattern(String locationPattern) {
		return ".*" + //allow any base path
		       locationPattern
				       .replaceAll("\\.", "\\\\.")  //escape .
				       .replaceAll("\\*", ".*")    //replace * to regex style (.*)
				       .replaceAll("\\.\\*\\.\\*/", "\\(\\.\\*\\/)\\*") //replace ** to regex style ((.*/)*)
				       .replaceAll("\\]", "\\]+");    //allow regex range 1-n times
	}
}
