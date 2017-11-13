/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific;

import com.namics.oss.spring.support.terrific.util.FilenameIgnoreCaseResourceComparator;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;

/**
 * FilenameIgnorecaseResourceComparator.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 28.05.2013
 */
public class FilenameIgnorecaseResourceComparatorTest {
	Comparator<Resource> comparator = new FilenameIgnoreCaseResourceComparator();

	@Test
	public void testIgnoreCase() {
		Resource left = new ClassPathResource("/AAA.test");
		Resource right = new ClassPathResource("/aaa.test");
		assertTrue("left must be less then right", this.comparator.compare(left, right) == 0);
	}

	@Test
	public void testIgnorePathLess() {
		Resource left = new ClassPathResource("/b-demo/a1.test");
		Resource right = new ClassPathResource("/a-demo/a2.test");
		assertTrue("left must be less then right", this.comparator.compare(left, right) < 0);
	}

	@Test
	public void testIgnorePathEqual() {
		Resource left = new ClassPathResource("/b-demo/a1.test");
		Resource right = new ClassPathResource("/a-demo/a1.test");
		assertTrue("left must be less then right", this.comparator.compare(left, right) == 0);
	}

	@Test
	public void testIgnorePathMore() {
		Resource left = new ClassPathResource("/b-demo/b.test");
		Resource right = new ClassPathResource("/a-demo/a.test");
		assertTrue("left must be less then right", this.comparator.compare(left, right) > 0);
	}

	@Test
	public void testDotDash() {
		Resource left = new ClassPathResource("/b-demo/a.test");
		Resource right = new ClassPathResource("/a-demo/a-skin.test");
		assertTrue("left must be less then right", this.comparator.compare(left, right) < 0);
	}

	@Test
	public void testDotDot() {
		Resource left = new ClassPathResource("/b-demo/a.test");
		Resource right = new ClassPathResource("/a-demo/a.skin.test");
		assertTrue("left must be less then right", this.comparator.compare(left, right) < 0);
	}

	@Test
	public void testNoDot() {
		Resource left = new ClassPathResource("/b-demo/abc");
		Resource right = new ClassPathResource("/a-demo/abc.test");
		assertTrue("left must be less then right", this.comparator.compare(left, right) == 0);
	}
}
