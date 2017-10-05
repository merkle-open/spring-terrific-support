/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.file;

import org.springframework.core.io.Resource;

import java.util.Set;

/**
 * FileResolver.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 03.04.2013
 */
public interface ResourceResolver {
	/**
	 * Resolves Sets of {@link Resource} objects of files in a order to be preserved!.
	 * Content of Resources belongs together and should be processed as a single unit.
	 * e.g.:
	 * <pre>
	 * 	[
	 * 		[file1.less,append1.less,append2.less],
	 * 		[file2.less,append1.less,append2.less]
	 * 	]
	 * </pre>
	 * You should use iterators for processing only to preserve this order.
	 *
	 * @return set of files in order that should be preserved for further processing, empty Set if there are no matching resources!
	 */
	Set<Set<Resource>> getResources();

}
