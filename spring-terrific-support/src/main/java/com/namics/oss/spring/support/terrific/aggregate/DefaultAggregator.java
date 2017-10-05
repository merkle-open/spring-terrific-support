/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.aggregate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;

/**
 * CStyleTitleAggregator.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.04.2013
 */
public class DefaultAggregator<T extends DefaultAggregator> extends Aggregator<T> {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultAggregator.class);
	protected final ByteArrayOutputStream buffer;

	public DefaultAggregator() {
		this.priority = Integer.MAX_VALUE;
		this.buffer = new ByteArrayOutputStream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T append(String title,
	                byte[] content) {
		try {
			if (StringUtils.hasText(title)) {
				if (this.getTitlePrefix() != null) {
					this.buffer.write(this.getTitlePrefix().getBytes(encoding));
				}

				this.buffer.write(title.getBytes(encoding));

				if (this.getTitlePostfix() != null) {
					this.buffer.write(this.getTitlePostfix().getBytes(encoding));
				}
			}
			this.buffer.write(content);
		} catch (Exception e) {
			LOG.debug("could not append {} {}", new Object[] { title, e });
		}
		return (T) this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getContent() {
		return this.buffer.toByteArray();
	}

	@Override
	public boolean isApplicable(String filename,
	                            boolean debug) {
		return true;
	}

	/**
	 * Returns a prefix to use for title printing, protected method may be overwritten for polymorph usage.
	 *
	 * @return the prefix to use
	 */
	protected String getTitlePrefix() {
		return "";
	}

	/**
	 * Returns a psotfix to use for title printing, protected method may be overwritten for polymorph usage.
	 *
	 * @return the psotfix to use
	 */
	protected String getTitlePostfix() {
		return "";
	}


}
