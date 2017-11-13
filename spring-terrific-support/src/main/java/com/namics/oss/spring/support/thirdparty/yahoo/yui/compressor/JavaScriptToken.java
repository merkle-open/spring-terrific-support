/*
 * YUI Compressor
 * http://developer.yahoo.com/yui/com.namics.commons.thirdparty.yahoo.yui.compressor.compressor/
 * Author: Julien Lecomte -  http://www.julienlecomte.net/
 * Copyright (c) 2011 Yahoo! Inc.  All rights reserved.
 * The copyrights embodied in the content of this file are licensed
 * by Yahoo! Inc. under the BSD (revised) open source license.
 */

package com.namics.oss.spring.support.thirdparty.yahoo.yui.compressor;

public class JavaScriptToken {

	private int type;
	private String value;

	JavaScriptToken(int type, String value) {
		this.type = type;
		this.value = value;
	}

	int getType() {
		return type;
	}

	String getValue() {
		return value;
	}
}
