/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.service;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ContentService.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.04.2013
 */
public interface ContentService {

	/**
	 * Method to check for last modified date of the requested file, for aggregated files the most recent is relevant.
	 *
	 * @param file requested file to check modified date for
	 * @return lastModified stampt of most recently modified file involved
	 */
	Long lastModified(String file);

	/**
	 * Get the content bytes for a certain file, either configured or directly from resource path.
	 *
	 * @param file requested file
	 * @return byte content of the file
	 * @throws FileNotFoundException no resource found matching file name
	 * @throws IOException           problem in file processing
	 */
	byte[] getContent(String file) throws IOException;

	/**
	 * Get the content bytes for a certain file, either configured or directly from resource path.
	 *
	 * @param file  requested file
	 * @param debug get file content for debug purpose
	 * @return byte content of the file
	 * @throws FileNotFoundException no resource found matching file name
	 * @throws IOException           problem in file processing
	 */
	byte[] getContent(String file,
	                  boolean debug) throws IOException;
}
