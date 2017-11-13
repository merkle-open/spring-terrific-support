/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.minify;

import java.util.List;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * MinifierFactory.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.04.2013
 */
public class MinifierFactory {
	protected List<Minifier> minifiers;

	public MinifierFactory(List<Minifier> minifiers) {
		notNull(minifiers);
		this.minifiers = minifiers;
	}

	/**
	 * Resolve a minifier that is applicable to this file.
	 *
	 * @param filename file name to get a compatible minifier for
	 * @return a matching minifier if any, null else
	 */
	public Minifier getMinifier(String filename) {
		if (!isEmpty(this.minifiers)) {
			for (Minifier minifier : this.minifiers) {
				if (minifier.isApplicable(filename)) {
					return minifier;
				}
			}
		}
		return null;
	}
}
