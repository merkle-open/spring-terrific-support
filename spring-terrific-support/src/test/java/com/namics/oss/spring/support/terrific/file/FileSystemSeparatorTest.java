/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.file;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * FileSystemSeparatorTest.
 *
 * @author aschaefer, Namics AG
 * @since 20.03.14 14:51
 */
public class FileSystemSeparatorTest {
	@Test
	public void testReplacement() throws IOException, URISyntaxException {
		File file = File.createTempFile("separatortest", ".txt");
		file.deleteOnExit();
		Resource resource = new FileSystemResource(file);
		assertTrue(resource.getFile().isFile());
		String filename = resource.getFile().getPath();
		String separator = System.getProperty("file.separator");
		if (!"/".equals(separator)) {
			filename = filename.replaceAll(Pattern.quote(separator), "/");
			assertFalse(filename.contains(separator));
		}
	}
}
