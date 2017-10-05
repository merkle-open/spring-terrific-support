/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.compile;

/**
 * Compiler.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 03.04.2013
 */
public interface Compiler {
	/**
	 * Compile source to a certain output.
	 *
	 * @param source source to compile
	 * @return compiled content
	 * @throws CompileException when compilation failed
	 */
	public byte[] compile(byte[] source) throws CompileException;

	/**
	 * Detects if this compiler is applicable to a certain file by its file name.
	 *
	 * @param filename file name to check
	 * @return whether it can be applied or not
	 */
	public boolean isApplicable(String filename);
}
