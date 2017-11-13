/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.compile;

import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.springframework.util.Assert.notNull;

/**
 * CompilerFactory.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.04.2013
 */
public class CompilerFactory {
	protected List<Compiler> compilers;

	/**
	 * @param compilers must not be null, but can be empty collection.
	 */
	public CompilerFactory(List<Compiler> compilers) {
		notNull(compilers);
		this.compilers = compilers;
	}

	/**
	 * Resolve a compiler that is applicable to this file.
	 *
	 * @param filename file name to get a compatible compiler for
	 * @return a matching compiler if any, null else
	 */
	public Compiler getCompiler(String filename) {
		if (!CollectionUtils.isEmpty(this.compilers)) {
			for (Compiler compiler : this.compilers) {
				if (compiler.isApplicable(filename)) {
					return compiler;
				}
			}
		}
		return null;
	}

}
