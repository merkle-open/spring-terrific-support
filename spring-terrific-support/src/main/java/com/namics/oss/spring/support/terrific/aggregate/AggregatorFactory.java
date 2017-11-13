/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.aggregate;

import org.springframework.util.CollectionUtils;

import java.util.SortedSet;

import static org.springframework.util.Assert.notNull;

/**
 * CompilerFactory.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.04.2013
 */
public class AggregatorFactory {
	protected SortedSet<Aggregator> aggregators;

	/**
	 * @param aggregators must not be null, but can be empty set
	 */
	public AggregatorFactory(SortedSet<Aggregator> aggregators) {
		notNull(aggregators);
		this.aggregators = aggregators;
	}

	/**
	 * Get an aggragator instance that supports file by name pattern.
	 *
	 * @param filename file name to get a fitting aggregator for
	 * @param debug
	 * @return aggregator instance
	 */
	public Aggregator getAggregator(String filename,
	                                boolean debug) {
		if (!CollectionUtils.isEmpty(aggregators)) {
			for (Aggregator aggregator : aggregators) {
				if (aggregator.isApplicable(filename, debug)) {
					return aggregator.getInstance();
				}
			}
		}
		return new DefaultAggregator();
	}
}
