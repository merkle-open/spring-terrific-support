/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.aggregate;

import org.springframework.util.StringUtils;

/**
 * CStyleTitleAggregator.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.04.2013
 */
public class CStyleTitlePrinterAggregator extends DefaultAggregator<CStyleTitlePrinterAggregator> {
	public CStyleTitlePrinterAggregator() {
		this.priority = 1000;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getTitlePrefix() {
		return "\n\n/* ";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getTitlePostfix() {
		return " */\n";
	}

	@Override
	public boolean isApplicable(String filename,
	                            boolean debug) {
		return StringUtils.hasText(filename) && (filename.endsWith(".js") || filename.endsWith(".css"));
	}
}
