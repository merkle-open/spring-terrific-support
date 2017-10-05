/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * MimeUtils.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 05.04.2013
 */
public class MimeUtils {

	/**
	 * Mime Type HTML.
	 */
	public static final String HTML = "text/html";
	/**
	 * Mime Type TEXT.
	 */
	public static final String TEXT = "text/plain";
	/**
	 * Mime Type CSS.
	 */
	public static final String CSS = "text/css";
	/**
	 * Mime Type JAVASCRIPT.
	 */
	public static final String JAVASCRIPT = "text/javascript";
	/**
	 * Mime Type JSON.
	 */
	public static final String JSON = "application/json";

	/**
	 * Image Mime Type GIF.
	 */
	public static final String GIF = "image/gif";
	/**
	 * Image Mime Type PNG.
	 */
	public static final String PNG = "image/png";
	/**
	 * Image Mime Type JPG.
	 */
	public static final String JPG = "image/jpeg";
	/**
	 * Image Mime Type BMP.
	 */
	public static final String BMP = "image/bmp";
	/**
	 * Image Mime Type TIFF.
	 */
	public static final String TIFF = "image/tiff";
	/**
	 * Image Mime Type SVG.
	 */
	public static final String SVG = "image/svg+xml";

	/**
	 * Font Mime Type WOFF.
	 */
	public static final String WOFF = "application/font-woff";
	/**
	 * Font Mime Type TTF.
	 */
	public static final String TTF = "font/truetype";
	/**
	 * Font Mime Type OTF.
	 */
	public static final String OTF = "font/opentype";


	/**
	 * Mime Type DEFAULT.
	 */
	public static final String DEFAULT = HTML;
	// consider fonts

	private static Map<String, String> extensionToMimeTypeMap = new HashMap<String, String>();

	static {
		// see http://de.selfhtml.org/diverses/mimetypen.htm
		extensionToMimeTypeMap.put("js", JAVASCRIPT);
		extensionToMimeTypeMap.put("css", CSS);
		extensionToMimeTypeMap.put("txt", TEXT);
		extensionToMimeTypeMap.put("htm", HTML);
		extensionToMimeTypeMap.put("html", HTML);

		extensionToMimeTypeMap.put("jpg", JPG);
		extensionToMimeTypeMap.put("jpeg", JPG);
		extensionToMimeTypeMap.put("jpe", JPG);
		extensionToMimeTypeMap.put("png", PNG);
		extensionToMimeTypeMap.put("gif", GIF);
		extensionToMimeTypeMap.put("bmp", BMP);
		extensionToMimeTypeMap.put("tiff", TIFF);
		extensionToMimeTypeMap.put("svg", SVG);

		extensionToMimeTypeMap.put("woff", WOFF);
		extensionToMimeTypeMap.put("ttf", TTF);
		extensionToMimeTypeMap.put("otf", OTF);

	}

	/**
	 * Detect a mime type based on the file name.
	 *
	 * @param file name of the file
	 * @return mime type
	 */
	public static String detectMimeType(String file) {
		if (StringUtils.hasText(file)) {
			String normalized = file.toLowerCase();
			if (normalized.contains(".")) {
				normalized = normalized.substring(file.lastIndexOf('.') + 1);
			}
			if (extensionToMimeTypeMap.containsKey(normalized)) {
				return extensionToMimeTypeMap.get(normalized);
			}
		}
		return DEFAULT;
	}

	/**
	 * Add a MIME type mapping for the file extension, override existing ones.
	 *
	 * @param fileExtension file name extension to map with this mime type
	 * @param mimeType      mime type to map for this file name extension
	 */
	public static void mapExtension(String fileExtension, String mimeType) {
		extensionToMimeTypeMap.put(fileExtension, mimeType);
	}

	/**
	 * Add a MIME type mapping for the file extension.
	 *
	 * @param fileExtension file name extension to map with this mime type
	 * @param mimeType      mime type to map for this file name extension
	 * @param override      wheter to override existing mapping for this file extension or not
	 * @return wheter the file name extension was set or not
	 */
	public static boolean mapExtension(String fileExtension, String mimeType, boolean override) {
		if (override) {
			extensionToMimeTypeMap.put(fileExtension, mimeType);
			return true;
		} else if (!extensionToMimeTypeMap.containsKey(fileExtension)) {
			extensionToMimeTypeMap.put(fileExtension, mimeType);
			return true;
		}
		return false;
	}

	/**
	 * Prevent initialization.
	 */
	protected MimeUtils() {
		super();
	}

}
