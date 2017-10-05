/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.util;

import org.springframework.core.io.Resource;

import java.io.Serializable;
import java.util.Comparator;

/**
 * FilenameIgnoreCaseResourceComparator.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.03.2013
 */
public class FilenameIgnoreCaseResourceComparator implements Comparator<Resource>, Serializable {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -762511457294576368L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Resource left,
	                   Resource right) {
		return this.getFilename(left).compareTo(this.getFilename(right));
	}

	/**
	 * Extracts and normalizes file name string null save from resource.
	 *
	 * @param resource to get file name string of
	 * @return normalized file name string, empty string "" if none, never null.
	 */
	private String getFilename(Resource resource) {
		String filename = resource != null && resource.getFilename() != null ? resource.getFilename() : "";
		filename = filename.toLowerCase();
		int index = filename.lastIndexOf('.');
		if (index > 0) {
			filename = filename.substring(0, index);
		}
		return filename;
	}

}
