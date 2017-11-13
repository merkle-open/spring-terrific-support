/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.aggregate;

/**
 * Aggregator for unified content aggregation, Implementations are stateful and require unique instances per use case.
 * There is no intend for recycling of aggregators. Create a new instance for each usage!
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.04.2013
 */
public abstract class Aggregator<T extends Aggregator> implements Comparable<Aggregator> {
	protected int priority = Integer.MAX_VALUE;
	protected String encoding = "UTF-8";

	/**
	 * Appends new content with a certain title to the current content.
	 *
	 * @param title   title of the added content
	 * @param content
	 */
	public abstract T append(String title,
	                         byte[] content);

	/**
	 * Get the current content of the aggregator.
	 *
	 * @return current content
	 */
	public abstract byte[] getContent();

	/**
	 * Check if this Implementation is compatible with file.
	 *
	 * @param filename name of file, required to check type
	 * @param debug    is this Aggregator suitable for debug mode
	 * @return true if content can be aggregated with this Impl
	 */
	public abstract boolean isApplicable(String filename,
	                                     boolean debug);

	/**
	 * Create a new instance of this aggregator and set relevant properties.
	 *
	 * @return fresh aggregator instance
	 */
	public Aggregator getInstance() {
		try {
			Aggregator aggregator = this.getClass().newInstance();
			aggregator.setEncoding(encoding);
			return aggregator;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int compareTo(Aggregator compare) {
		return this.priority - compare.priority;
	}

	/**
	 * encoding to use, default by property ${terrific.encoding:UTF-8}.
	 *
	 * @param encoding the encoding to set
	 */
	public T setEncoding(String encoding) {
		this.encoding = encoding;
		return (T) this;
	}
}
