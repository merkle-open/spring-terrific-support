/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.aggregate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * DynamicsJsAggregator.
 *
 * @author aschaefer
 * @since 19.09.13 14:48
 */
public class DynamicsJsAggregator extends Aggregator<DynamicsJsAggregator> {
	private static final Logger LOG = LoggerFactory.getLogger(DynamicsJsAggregator.class);
	protected final StringBuffer buffer;
	protected String prefix = "var urls=[\n";
	protected String postfix = "];\nfor ( var url in urls ){ document.write('<script src=\"'+urls[url]+'\" type=\"text/javascript\"></script>');} ";

	public DynamicsJsAggregator() {
		this.priority = 100;
		buffer = new StringBuffer(prefix);
	}

	@Override
	public DynamicsJsAggregator append(String title, byte[] content) {
		this.buffer.append("\n'");
		this.buffer.append(title);
		this.buffer.append("',");
		return this;
	}

	@Override
	public byte[] getContent() {
		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(postfix);
		try {
			return buffer.toString().getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			LOG.debug("could not append {}", new Object[] { e });
		}
		return new byte[0];
	}

	@Override
	public boolean isApplicable(String filename,
	                            boolean debug) {
		return debug && StringUtils.hasText(filename) && filename.endsWith(".js");
	}

}
