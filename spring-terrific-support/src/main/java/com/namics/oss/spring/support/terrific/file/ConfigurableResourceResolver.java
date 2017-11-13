/*
 * Copyright 2013-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.file;

/**
 * This interface is required to be able to create aop proxies on the resolver and still be able to configure the resolver.
 *
 * @author aschaefer, Namics AG
 * @since 2.1.0 05.07.2013
 */
public interface ConfigurableResourceResolver extends ResourceResolver {

	/**
	 * Add an exclude pattern, supports asterisk (*) as wildcard.
	 *
	 * @param exclude exclude pattern to add to the list
	 */
	void addExclude(String exclude);

	/**
	 * Add an append pattern, supports asterisk (*) as wildcard.
	 *
	 * @param append pattern to add to the list
	 */
	void addAppend(String append);

	/**
	 * Setter for pattern. @param pattern the pattern to set
	 */
	void setPattern(String pattern);

}