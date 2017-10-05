package com.namics.oss.spring.support.terrific.file;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import static junit.framework.TestCase.assertEquals;

public class RangePatternResourceResolverWrapperTest {
	RangePatternResourceResolverWrapper resolver = new RangePatternResourceResolverWrapper(new PathMatchingResourcePatternResolver());

	@Test
	public void testGetResources() throws Exception {
		Resource[] resources = resolver.getResources("/exclude/test/hierarchy/*.*");
		assertEquals(3, resources.length);

		resources = resolver.getResources("/exclude/test/hierarchy/*.css");
		assertEquals(3, resources.length);

		resources = resolver.getResources("/exclude/test/hierarchy/[a-z].css");
		assertEquals(1, resources.length);

		resources = resolver.getResources("/exclude/test/hierarchy/[a-z]-[a-z].css");
		assertEquals(1, resources.length);

		resources = resolver.getResources("/exclude/test/hierarchy/[a-z]-[a-z1-9].css");
		assertEquals(2, resources.length);

		resources = resolver.getResources("/exclude/*/hierarchy/[a-z].css");
		assertEquals(1, resources.length);

		resources = resolver.getResources("/exclude/*/hierarchy/[a-z]-[a-z].css");
		assertEquals(1, resources.length);

		resources = resolver.getResources("/exclude/**/[a-z].css");
		assertEquals(3, resources.length);

		resources = resolver.getResources("/exclude/**/[a-z]-[a-z1-9].css");
		assertEquals(2, resources.length);

		resources = resolver.getResources("/exclude/test/**/[a-z].css");
		assertEquals(2, resources.length);

		resources = resolver.getResources("/exclude/test/**/notExisting/[a-z].css");
		assertEquals(0, resources.length);
	}
}